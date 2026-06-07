package com.turbolessons.adminservice.client;

import com.turbolessons.adminservice.config.KeycloakAdminProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * Thin client over the Keycloak Admin REST API.
 *
 * <p>Authenticates with the {@code client_credentials} grant (the confidential
 * {@code admin-service-kc} service-account client) and exposes the ~8 user/group
 * operations admin-service needs. Uses {@link RestTemplate} to avoid pulling in the
 * {@code jakarta}-based {@code keycloak-admin-client} on this {@code javax} module.
 */
@Component
public class KeycloakUserClient {

    private static final Logger log = LoggerFactory.getLogger(KeycloakUserClient.class);

    private final RestTemplate restTemplate;
    private final KeycloakAdminProperties props;

    // Cached service-account token (refreshed shortly before expiry).
    private volatile String cachedToken;
    private volatile Instant tokenExpiresAt = Instant.EPOCH;

    public KeycloakUserClient(RestTemplate keycloakRestTemplate, KeycloakAdminProperties props) {
        this.restTemplate = keycloakRestTemplate;
        this.props = props;
    }

    // --- token -------------------------------------------------------------

    /** Returns a valid bearer token, fetching/refreshing via client_credentials as needed. */
    public synchronized String token() {
        if (cachedToken != null && Instant.now().isBefore(tokenExpiresAt)) {
            return cachedToken;
        }
        String tokenUri = props.getServerUrl() + "/realms/" + props.getRealm()
                + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", props.getClientId());
        form.add("client_secret", props.getClientSecret());

        ResponseEntity<TokenResponse> resp = restTemplate.exchange(
                tokenUri, HttpMethod.POST, new HttpEntity<>(form, headers), TokenResponse.class);

        TokenResponse body = resp.getBody();
        if (body == null || !StringUtils.hasText(body.accessToken)) {
            throw new IllegalStateException("Keycloak token endpoint returned no access_token");
        }
        cachedToken = body.accessToken;
        // refresh 30s before the token actually expires
        long ttl = Math.max(0, body.expiresIn - 30);
        tokenExpiresAt = Instant.now().plusSeconds(ttl);
        return cachedToken;
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private String adminBase() {
        return props.getServerUrl() + "/admin/realms/" + props.getRealm();
    }

    // --- users -------------------------------------------------------------

    /** GET /users — all users (up to the configured fetch limit). */
    public List<UserRepresentation> listUsers() {
        URI uri = UriComponentsBuilder.fromHttpUrl(adminBase() + "/users")
                .queryParam("first", 0)
                .queryParam("max", props.getFetchLimit())
                .build().toUri();
        ResponseEntity<UserRepresentation[]> resp = restTemplate.exchange(
                uri, HttpMethod.GET, new HttpEntity<>(authHeaders()), UserRepresentation[].class);
        return toList(resp.getBody());
    }

    /** GET /users/{id} — single user, or empty if the user does not exist. */
    public Optional<UserRepresentation> getUser(String id) {
        URI uri = URI.create(adminBase() + "/users/" + id);
        try {
            ResponseEntity<UserRepresentation> resp = restTemplate.exchange(
                    uri, HttpMethod.GET, new HttpEntity<>(authHeaders()), UserRepresentation.class);
            return Optional.ofNullable(resp.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    /**
     * POST /users — creates a user and returns the new user id (parsed from the
     * {@code Location} header).
     */
    public String createUser(UserRepresentation user) {
        URI uri = URI.create(adminBase() + "/users");
        ResponseEntity<Void> resp = restTemplate.exchange(
                uri, HttpMethod.POST, new HttpEntity<>(user, authHeaders()), Void.class);
        URI location = resp.getHeaders().getLocation();
        if (location == null) {
            throw new IllegalStateException("Keycloak create-user returned no Location header");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /** PUT /users/{id} — updates a user. */
    public void updateUser(String id, UserRepresentation user) {
        URI uri = URI.create(adminBase() + "/users/" + id);
        restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(user, authHeaders()), Void.class);
    }

    /** DELETE /users/{id}. */
    public void deleteUser(String id) {
        URI uri = URI.create(adminBase() + "/users/" + id);
        restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(authHeaders()), Void.class);
    }

    // --- groups ------------------------------------------------------------

    /**
     * GET /groups?search={name} — finds a group by exact name (searches nested
     * subgroups too, since Keycloak's search matches a path prefix).
     */
    public Optional<GroupRepresentation> findGroupByName(String name) {
        URI uri = UriComponentsBuilder.fromHttpUrl(adminBase() + "/groups")
                .queryParam("search", name)
                .queryParam("max", props.getFetchLimit())
                .build().toUri();
        ResponseEntity<GroupRepresentation[]> resp = restTemplate.exchange(
                uri, HttpMethod.GET, new HttpEntity<>(authHeaders()), GroupRepresentation[].class);

        Deque<GroupRepresentation> stack = new ArrayDeque<>(toList(resp.getBody()));
        while (!stack.isEmpty()) {
            GroupRepresentation g = stack.pop();
            if (name.equals(g.getName())) {
                return Optional.of(g);
            }
            if (g.getSubGroups() != null) {
                stack.addAll(g.getSubGroups());
            }
        }
        return Optional.empty();
    }

    /** GET /groups/{id}/members — members of a group. */
    public List<UserRepresentation> listGroupMembers(String groupId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(adminBase() + "/groups/" + groupId + "/members")
                .queryParam("first", 0)
                .queryParam("max", props.getFetchLimit())
                .build().toUri();
        ResponseEntity<UserRepresentation[]> resp = restTemplate.exchange(
                uri, HttpMethod.GET, new HttpEntity<>(authHeaders()), UserRepresentation[].class);
        return toList(resp.getBody());
    }

    /** PUT /users/{userId}/groups/{groupId} — adds a user to a group. */
    public void addUserToGroup(String userId, String groupId) {
        URI uri = URI.create(adminBase() + "/users/" + userId + "/groups/" + groupId);
        restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(authHeaders()), Void.class);
    }

    private static <T> List<T> toList(T[] arr) {
        return arr == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(arr));
    }

    /** Minimal token-endpoint response. */
    private static final class TokenResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("access_token")
        String accessToken;
        @com.fasterxml.jackson.annotation.JsonProperty("expires_in")
        long expiresIn;
    }
}
