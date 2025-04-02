#!/bin/bash

# Source environment variables
source /Users/noslen/DevProjects/turbolessons_scripts/root.env

# Run specific test class if provided, otherwise run all tests
if [ -n "$1" ]; then
    ./mvnw test -Dtest=$1 \
    -Dspring.security.oauth2.client.registration.okta.client-id="$OKTA_API_SERVICES_ID" \
    -Dspring.security.oauth2.client.registration.okta.client-secret="$OKTA_API_SERVICES_SECRET" \
    -Dokta.oauth2.client-id="$OKTA_API_SERVICES_ID" \
    -Dokta.oauth2.client-secret="$OKTA_API_SERVICES_SECRET"
else
    ./mvnw test \
    -Dspring.security.oauth2.client.registration.okta.client-id="$OKTA_API_SERVICES_ID" \
    -Dspring.security.oauth2.client.registration.okta.client-secret="$OKTA_API_SERVICES_SECRET" \
    -Dokta.oauth2.client-id="$OKTA_API_SERVICES_ID" \
    -Dokta.oauth2.client-secret="$OKTA_API_SERVICES_SECRET"
fi
