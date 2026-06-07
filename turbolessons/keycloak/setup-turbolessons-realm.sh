#!/usr/bin/env bash
#
# setup-turbolessons-realm.sh
# ---------------------------------------------------------------------------
# Idempotent Keycloak setup for the Turbolessons backend (Okta -> Keycloak migration).
#
# Creates, in the target realm:
#   * client scopes:  stripe_client, email_client, test_client
#                     (each emitted into the access-token `scope` claim, so Spring's
#                      default converter maps them to SCOPE_* authorities)
#   * confidential M2M clients (service accounts / client_credentials), each with its scope:
#                     payment-service-m2m  -> stripe_client
#                     email-service-m2m    -> email_client
#                     api-tests-m2m        -> test_client
#   * gateway login client `api-gateway` (confidential, authorization_code flow):
#                created with proper redirect URIs / web origins; prints its secret.
#   * React SPA client `turbolessons-spa` (public, PKCE) for the frontend, with a
#                `groups` claim mapper (so the app's role routing on accessToken.claims.groups works).
#
# Safe to re-run: existing scopes/clients are detected and updated, not duplicated.
# Secrets are PRINTED at the end — paste them into turbolessons-config/*.yml and then
# ENCRYPT each one with the recovered keystore via the config-service /encrypt endpoint
# before committing (see turbolessons/README-qac.md / Part 3 of the plan).
#
# ---------------------------------------------------------------------------
# Usage:
#   KC_ADMIN_PASSWORD='***' ./setup-turbolessons-realm.sh
#
# Common overrides (env vars):
#   KC_SERVER          Keycloak base URL            (default: https://auth.nelsonjohns.com)
#   KC_REALM           target realm                 (default: turbolessons)
#   KC_ADMIN_REALM     admin realm to log in to     (default: master)
#   KC_ADMIN_USER      admin username               (default: admin)
#   KC_ADMIN_PASSWORD  admin password               (REQUIRED)
#   KCADM              path/command for kcadm.sh    (default: kcadm.sh on PATH)
#   GATEWAY_CLIENT_ID  gateway login client id      (default: api-gateway)
#   CONFIGURE_GATEWAY  create/ensure gateway client? (default: true)
#   EXPORT_REALM       write realm-export.json?     (default: false)
#
# If Keycloak runs in a container, point KCADM at it, e.g.:
#   KCADM='docker exec -i keycloak /opt/keycloak/bin/kcadm.sh'
# ---------------------------------------------------------------------------
set -euo pipefail

KC_SERVER="${KC_SERVER:-https://auth.nelsonjohns.com}"
KC_REALM="${KC_REALM:-turbolessons}"
KC_ADMIN_REALM="${KC_ADMIN_REALM:-master}"
KC_ADMIN_USER="${KC_ADMIN_USER:-admin}"
KC_ADMIN_PASSWORD="${KC_ADMIN_PASSWORD:?Set KC_ADMIN_PASSWORD (Keycloak admin password)}"
KCADM="${KCADM:-kcadm.sh}"
GATEWAY_CLIENT_ID="${GATEWAY_CLIENT_ID:-api-gateway}"
CONFIGURE_GATEWAY="${CONFIGURE_GATEWAY:-true}"
SPA_CLIENT_ID="${SPA_CLIENT_ID:-turbolessons-spa}"
CONFIGURE_SPA="${CONFIGURE_SPA:-true}"
EXPORT_REALM="${EXPORT_REALM:-false}"

# Public URLs used for the gateway login client's redirect/web-origin allow-lists.
GATEWAY_REDIRECT_URIS='["https://www.turbolessons.com/login/oauth2/code/keycloak","https://qac.turbolessons.com/login/oauth2/code/keycloak","http://localhost:8080/login/oauth2/code/keycloak"]'
GATEWAY_WEB_ORIGINS='["https://www.turbolessons.com","https://qac.turbolessons.com","http://localhost:3000"]'

# Redirect/web-origin allow-lists for the public React SPA client (PKCE).
SPA_REDIRECT_URIS='["https://www.turbolessons.com/login/callback","https://qac.turbolessons.com/login/callback","http://localhost:3000/login/callback"]'
SPA_WEB_ORIGINS='["https://www.turbolessons.com","https://qac.turbolessons.com","http://localhost:3000"]'
SPA_POST_LOGOUT='["https://www.turbolessons.com","https://qac.turbolessons.com","http://localhost:3000"]'

SUMMARY="$(mktemp)"
trap 'rm -f "$SUMMARY"' EXIT

command -v python3 >/dev/null 2>&1 || { echo "ERROR: python3 is required for JSON parsing." >&2; exit 1; }

# --- helpers ---------------------------------------------------------------
kc() { $KCADM "$@"; }

# first id of clients matching a clientId (empty if none)
client_uuid() {
  kc get clients -r "$KC_REALM" -q clientId="$1" --fields id 2>/dev/null \
    | python3 -c 'import sys,json; a=json.load(sys.stdin) or []; print(a[0]["id"] if a else "")'
}

# id of a client-scope by name (empty if none)
scope_id() {
  kc get client-scopes -r "$KC_REALM" --fields id,name 2>/dev/null \
    | SN="$1" python3 -c 'import sys,json,os; a=json.load(sys.stdin) or []; n=os.environ["SN"]; print(next((s["id"] for s in a if s["name"]==n),""))'
}

client_secret() {  # arg: client uuid
  kc get "clients/$1/client-secret" -r "$KC_REALM" 2>/dev/null \
    | python3 -c 'import sys,json; print((json.load(sys.stdin) or {}).get("value",""))'
}

client_field() {   # args: uuid field
  kc get "clients/$1" -r "$KC_REALM" --fields "$2" 2>/dev/null \
    | FLD="$2" python3 -c 'import sys,json,os; print((json.load(sys.stdin) or {}).get(os.environ["FLD"],""))' 2>/dev/null \
    || true
}

ensure_client_scope() {
  local name="$1" sid
  sid="$(scope_id "$name")"
  if [ -z "$sid" ]; then
    kc create client-scopes -r "$KC_REALM" \
      -s "name=$name" -s protocol=openid-connect \
      -s 'attributes."include.in.token.scope"=true' \
      -s 'attributes."display.on.consent.screen"=false' >/dev/null
    echo "  + client-scope '$name' created"
  else
    echo "  = client-scope '$name' already exists"
  fi
}

ensure_m2m_client() {
  local cid="$1" scope="$2" uuid sid secret
  uuid="$(client_uuid "$cid")"
  if [ -z "$uuid" ]; then
    kc create clients -r "$KC_REALM" \
      -s "clientId=$cid" -s enabled=true -s protocol=openid-connect \
      -s publicClient=false -s serviceAccountsEnabled=true \
      -s standardFlowEnabled=false -s directAccessGrantsEnabled=false \
      -s implicitFlowEnabled=false -s 'redirectUris=[]' -s 'webOrigins=[]' \
      -s "description=Turbolessons M2M client ($scope)" >/dev/null
    uuid="$(client_uuid "$cid")"
    echo "  + client '$cid' created ($uuid)"
  else
    kc update "clients/$uuid" -r "$KC_REALM" \
      -s publicClient=false -s serviceAccountsEnabled=true \
      -s standardFlowEnabled=false -s directAccessGrantsEnabled=false >/dev/null
    echo "  = client '$cid' exists ($uuid) — config ensured"
  fi
  # assign the custom scope as a default client scope so it lands in every token
  sid="$(scope_id "$scope")"
  if [ -n "$sid" ]; then
    kc update "clients/$uuid/default-client-scopes/$sid" -r "$KC_REALM" >/dev/null 2>&1 || true
    echo "    -> scope '$scope' assigned (default)"
  fi
  secret="$(client_secret "$uuid")"
  printf '%s\t%s\t%s\n' "$cid" "$scope" "$secret" >>"$SUMMARY"
}

ensure_gateway_client() {
  # Confidential OIDC login client for the gateway (authorization_code flow).
  local cid="$1" uuid secret
  uuid="$(client_uuid "$cid")"
  if [ -z "$uuid" ]; then
    kc create clients -r "$KC_REALM" \
      -s "clientId=$cid" -s enabled=true -s protocol=openid-connect \
      -s publicClient=false -s standardFlowEnabled=true \
      -s serviceAccountsEnabled=false -s directAccessGrantsEnabled=false \
      -s implicitFlowEnabled=false \
      -s "redirectUris=$GATEWAY_REDIRECT_URIS" \
      -s "webOrigins=$GATEWAY_WEB_ORIGINS" \
      -s 'attributes."post.logout.redirect.uris"=+' \
      -s "description=Turbolessons gateway OIDC login client (authorization_code)" >/dev/null
    uuid="$(client_uuid "$cid")"
    echo "  + gateway client '$cid' created ($uuid)"
  else
    kc update "clients/$uuid" -r "$KC_REALM" \
      -s publicClient=false -s standardFlowEnabled=true \
      -s serviceAccountsEnabled=false -s directAccessGrantsEnabled=false \
      -s implicitFlowEnabled=false \
      -s "redirectUris=$GATEWAY_REDIRECT_URIS" \
      -s "webOrigins=$GATEWAY_WEB_ORIGINS" >/dev/null
    echo "  = gateway client '$cid' exists ($uuid) — config ensured"
  fi
  secret="$(client_secret "$uuid")"
  printf '%s\t%s\t%s\n' "$cid" "(gateway login, confidential)" "$secret" >>"$SUMMARY"
}

ensure_groups_mapper() {
  # Add a "groups" claim (Keycloak group memberships) to a client's tokens, so the React
  # app's role routing (accessToken.claims.groups) works. Idempotent by mapper name.
  local uuid="$1"
  local has
  has=$(kc get "clients/$uuid/protocol-mappers/models" -r "$KC_REALM" --fields name 2>/dev/null \
        | python3 -c 'import sys,json; a=json.load(sys.stdin) or []; print("yes" if any(m.get("name")=="groups" for m in a) else "")')
  if [ -z "$has" ]; then
    kc create "clients/$uuid/protocol-mappers/models" -r "$KC_REALM" \
      -s name=groups -s protocol=openid-connect -s protocolMapper=oidc-group-membership-mapper \
      -s 'config."claim.name"=groups' -s 'config."full.path"=false' \
      -s 'config."access.token.claim"=true' -s 'config."id.token.claim"=true' \
      -s 'config."userinfo.token.claim"=true' >/dev/null
    echo "    -> groups mapper added"
  else
    echo "    -> groups mapper present"
  fi
}

ensure_spa_client() {
  # Public PKCE client for the React SPA (authorization_code + PKCE, no secret).
  local cid="$1" uuid
  uuid="$(client_uuid "$cid")"
  if [ -z "$uuid" ]; then
    kc create clients -r "$KC_REALM" \
      -s "clientId=$cid" -s enabled=true -s protocol=openid-connect \
      -s publicClient=true -s standardFlowEnabled=true \
      -s serviceAccountsEnabled=false -s directAccessGrantsEnabled=false \
      -s implicitFlowEnabled=false \
      -s "redirectUris=$SPA_REDIRECT_URIS" -s "webOrigins=$SPA_WEB_ORIGINS" \
      -s 'attributes."pkce.code.challenge.method"=S256' \
      -s "attributes.\"post.logout.redirect.uris\"=$(python3 -c 'import json,os;print("##".join(json.loads(os.environ["L"])))' L="$SPA_POST_LOGOUT")" \
      -s "description=Turbolessons React SPA (public, PKCE)" >/dev/null
    uuid="$(client_uuid "$cid")"
    echo "  + SPA client '$cid' created ($uuid)"
  else
    kc update "clients/$uuid" -r "$KC_REALM" \
      -s publicClient=true -s standardFlowEnabled=true \
      -s serviceAccountsEnabled=false -s directAccessGrantsEnabled=false \
      -s "redirectUris=$SPA_REDIRECT_URIS" -s "webOrigins=$SPA_WEB_ORIGINS" \
      -s 'attributes."pkce.code.challenge.method"=S256' >/dev/null
    echo "  = SPA client '$cid' exists ($uuid) — config ensured"
  fi
  ensure_groups_mapper "$uuid"
  printf '%s\t%s\t%s\n' "$cid" "(react SPA, public/PKCE)" "no secret — public client" >>"$SUMMARY"
}

# --- run -------------------------------------------------------------------
echo "==> Authenticating to $KC_SERVER (realm '$KC_ADMIN_REALM', user '$KC_ADMIN_USER')"
kc config credentials --server "$KC_SERVER" --realm "$KC_ADMIN_REALM" \
  --user "$KC_ADMIN_USER" --password "$KC_ADMIN_PASSWORD" >/dev/null

# sanity: target realm must exist
if ! kc get "realms/$KC_REALM" --fields realm >/dev/null 2>&1; then
  echo "ERROR: realm '$KC_REALM' not found on $KC_SERVER." >&2
  exit 1
fi
echo "==> Target realm: $KC_REALM"

echo "==> Ensuring client scopes"
ensure_client_scope stripe_client
ensure_client_scope email_client
ensure_client_scope test_client

echo "==> Ensuring M2M clients"
ensure_m2m_client payment-service-m2m stripe_client
ensure_m2m_client email-service-m2m   email_client
ensure_m2m_client api-tests-m2m       test_client

if [ "$CONFIGURE_GATEWAY" = "true" ]; then
  echo "==> Ensuring gateway login client"
  ensure_gateway_client "$GATEWAY_CLIENT_ID"
fi

if [ "$CONFIGURE_SPA" = "true" ]; then
  echo "==> Ensuring React SPA client (public/PKCE) + groups mapper"
  ensure_spa_client "$SPA_CLIENT_ID"
fi

if [ "$EXPORT_REALM" = "true" ]; then
  echo "==> Exporting realm to realm-export.json"
  kc create "realms/$KC_REALM/partial-export?exportClients=true&exportGroupsAndRoles=true" \
    > "realm-export.json" 2>/dev/null && echo "  wrote realm-export.json" || echo "  (partial-export failed; skip)"
fi

# --- output ----------------------------------------------------------------
echo
echo "============================================================================"
echo " Keycloak clients ready. Map these into turbolessons-config/*.yml:"
echo "============================================================================"
printf '%-22s %-26s %s\n' "CLIENT_ID" "SCOPE/ROLE" "CLIENT_SECRET"
printf '%-22s %-26s %s\n' "----------------------" "--------------------------" "------------------------------------"
while IFS=$'\t' read -r cid scope secret; do
  printf '%-22s %-26s %s\n' "$cid" "$scope" "$secret"
done <"$SUMMARY"
echo
cat <<'EOF'
Config mapping:
  payment-service.yml : registration.okta.client-id = payment-service-m2m ; client-secret = <secret>
  email-service.yml   : registration.okta.client-id = email-service-m2m   ; client-secret = <secret>
  api-tests.yml       : registration.okta.client-id = api-tests-m2m       ; client-secret = <secret>
  api-gateway.yml     : registration.keycloak.client-id = api-gateway        ; client-secret = <secret>
  frontend (.env)     : CLIENT_ID = turbolessons-spa (public, no secret) ; ISSUER = realm issuer

IMPORTANT: encrypt each client-secret before committing:
  curl -s -u "$CONFIG_USERNAME:$CONFIG_PASSWORD" -H 'Content-Type: text/plain' \
       --data-binary '<secret>' http://localhost:9999/encrypt
Paste the returned {cipher}... value (client-ids can stay plaintext).
EOF
