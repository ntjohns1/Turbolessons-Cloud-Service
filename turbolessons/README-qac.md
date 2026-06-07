# QAC environment (`qac.turbolessons.com`)

Pre-prod environment fed by **Git Flow**: feature → `dev` (**deploys QAC**) → `release/*` → `master` (**deploys prod**).

QAC reuses the same images/architecture as prod but with a separate Spring profile (`qac`),
separate `:qac` image tags, its own host/stack, and dedicated Okta app / Stripe-test / GCS
bucket / databases. Prod (master → build `:latest` → Jenkins) is unchanged.

## CI/CD flow (no Jenkins on the QAC path)

Per-service workflows `*-qac.yml` (in `.github/workflows/`) trigger on push to `dev`,
path-filtered to that service:

1. **build** job (`runs-on: ubuntu-latest`): build & push `noslenj/<service>:qac` to Docker Hub.
2. **deploy** job (`runs-on: self-hosted` = server_3): SSH to the QAC host and
   `docker compose pull <service> && docker compose up -d <service>`.

A `concurrency: qac-deploy` group serializes deploys so they can't collide with server_3's
other runner workload.

### Required GitHub repo secrets

| Secret | Purpose |
|---|---|
| `DOCKER_USERNAME`, `DOCKER_TOKEN` | Docker Hub push (already used by prod) |
| `QAC_HOST` | SSH host/IP of the QAC docker host (reachable from server_3) |
| `QAC_USER` | SSH user on the QAC host |
| `QAC_SSH_KEY` | private key authorized on the QAC host |
| `QAC_SSH_PORT` | SSH port (e.g. 22) |

### Self-hosted runner

server_3 must be registered as a GitHub Actions **self-hosted runner** for this repo and
have network routing + SSH access to `QAC_HOST`.

## QAC host setup (`~/turbolessons-qac/`)

1. Copy `turbolessons/docker-compose.qac.yml` → `~/turbolessons-qac/docker-compose.yml`.
2. Place the recovered **`config.p12`** keystore (see Part 1 of the plan) under the path the
   config-service volume expects.
3. Create **`root.env`** (your QAC secrets) and add `SPRING_PROFILES_ACTIVE=qac` — this is what
   makes every service request `<app>/qac` from the config server.
4. Provide QAC TLS certs for `qac.turbolessons.com` and the nginx conf (frontend block).
5. First bring-up: `docker compose up -d` (whole stack). Thereafter CI updates individual
   services on `dev` pushes.

## Config overlays (`turbolessons-config` repo, `main` branch)

Each `<service>-qac.yml` is merged OVER `<service>.yml` for profile `qac`; include **only the
keys that differ**. Secrets are `{cipher}` values bound to the recovered keystore — generate
them with the local config-service `/encrypt` endpoint:

```bash
# config-service running on :9999 against the recovered config.p12 (see Part 1)
curl -s -u "$CONFIG_USERNAME:$CONFIG_PASSWORD" -H 'Content-Type: text/plain' \
     --data-binary '<plaintext-qac-value>' http://localhost:9999/encrypt
```

### Per-service overlay checklist

| overlay file | keys to override for QAC |
|---|---|
| `api-gateway-qac.yml` | `cors.allowed-origins` (done); `okta.oauth2.client-id/secret` (QAC app) |
| `admin-service-qac.yml` | `okta.oauth2.issuer/client-id/secret`; `okta.client.token` & `orgUrl` (QAC admin token) |
| `payment-service-qac.yml` | `okta.*`; `spring.security…registration.okta.client-id/secret`; `STRIPE_PUBLIC_KEY` & `STRIPE_SECRET_KEY` (Stripe **test**) |
| `video-service-qac.yml` | `okta.*`; `google.bucket.name` (QAC bucket); `google.credentials.path`/`project.id` if different |
| `event-service-qac.yml` | `okta.*`; `spring.datasource.username/password` (QAC DB creds) |
| `email-service-qac.yml` | `spring.security…registration.okta.client-id/secret`; mongo/mail creds if different |
| `message-service-qac.yml` | `okta.oauth2.issuer/client-id/secret`; mongo creds if different |
| `service-registry-qac.yml` | usually none (no env-specific secrets) |

> Assumption: the QAC Okta app is in the **same Okta org**, so `issuer`/`token-uri` are
> unchanged and only `client-id`/`client-secret` differ. If QAC uses a different org/auth
> server, also override the issuer/token-uri ciphers.

## Code change

`api-gateway` CORS origins were externalized: `SecurityConfig` now reads
`cors.allowed-origins` from config (with a prod-safe fallback), so the gateway accepts the
qac origin via `api-gateway-qac.yml` instead of a hardcoded list.
