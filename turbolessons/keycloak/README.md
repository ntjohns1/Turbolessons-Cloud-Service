# Keycloak setup for Turbolessons

`setup-turbolessons-realm.sh` provisions the Keycloak objects the backend needs after the
Okta → Keycloak migration. It is **idempotent** (safe to re-run).

## What it creates (in realm `turbolessons`)
- **Client scopes:** `stripe_client`, `email_client`, `test_client` — emitted into the access
  token `scope` claim so Spring maps them to `SCOPE_*` authorities (matches the existing
  `@PreAuthorize("hasAuthority('SCOPE_stripe_client')")` checks).
- **Confidential M2M clients** (client_credentials / service accounts):
  - `payment-service-m2m` → `stripe_client`
  - `email-service-m2m` → `email_client`
  - `api-tests-m2m` → `test_client`
- **Gateway login client** `api-gateway` (confidential, authorization_code): created with
  redirect URIs + web origins for prod/qac/local; prints its secret. (Replaces the old
  random-id client — you can delete that one.)

## Run it
Requires `kcadm.sh` (ships with Keycloak) and `python3`.

```bash
# from a host that has kcadm.sh on PATH
KC_ADMIN_PASSWORD='your-admin-pw' ./setup-turbolessons-realm.sh

# Keycloak in a container:
KC_ADMIN_PASSWORD='your-admin-pw' \
KCADM='docker exec -i keycloak /opt/keycloak/bin/kcadm.sh' \
  ./setup-turbolessons-realm.sh
```

Key overrides: `KC_SERVER` (default `https://auth.nelsonjohns.com`), `KC_REALM`
(default `turbolessons`), `KC_ADMIN_USER` (default `admin`), `GATEWAY_CLIENT_ID`,
`CONFIGURE_GATEWAY` (default `true`), `EXPORT_REALM` (default `false`). See the script
header for the full list.

## After running
The script prints each client's secret. For each, update `turbolessons-config/*.yml`
(replace the `REPLACE_WITH_KEYCLOAK_*` placeholders) and **encrypt the secret** with the
recovered keystore before committing:

```bash
curl -s -u "$CONFIG_USERNAME:$CONFIG_PASSWORD" -H 'Content-Type: text/plain' \
     --data-binary '<secret>' http://localhost:9999/encrypt
```

Paste the returned `{cipher}…`. Client-ids can stay plaintext.

## For the QAC environment
Re-run with `KC_REALM=turbolessons-qac` once that realm exists, then fill the
`*-qac.yml` overlays the same way (using the `turbolessons-qac` issuer).
