package com.turbolessons.adminservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the Keycloak Admin REST API client.
 *
 * <p>Bound from the {@code keycloak.admin.*} properties (served from the config
 * server in deployed environments). admin-service authenticates to Keycloak with
 * the {@code client_credentials} grant using the confidential {@code admin-service-kc}
 * service-account client.
 */
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakAdminProperties {

    /** Keycloak base URL, e.g. {@code https://auth.nelsonjohns.com}. */
    private String serverUrl;

    /** Realm that holds the users, e.g. {@code turbolessons}. */
    private String realm;

    /** Service-account client id (default {@code admin-service-kc}). */
    private String clientId = "admin-service-kc";

    /** Service-account client secret (confidential). */
    private String clientSecret;

    /** Group new users are added to on creation. */
    private String defaultStudentGroup = "Student";

    /** Prefix for per-teacher cohort groups: {@code <prefix><teacherUsername>}. */
    private String cohortGroupPrefix = "active_student_";

    /** Max users/members fetched per list call. */
    private int fetchLimit = 150;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getDefaultStudentGroup() {
        return defaultStudentGroup;
    }

    public void setDefaultStudentGroup(String defaultStudentGroup) {
        this.defaultStudentGroup = defaultStudentGroup;
    }

    public String getCohortGroupPrefix() {
        return cohortGroupPrefix;
    }

    public void setCohortGroupPrefix(String cohortGroupPrefix) {
        this.cohortGroupPrefix = cohortGroupPrefix;
    }

    public int getFetchLimit() {
        return fetchLimit;
    }

    public void setFetchLimit(int fetchLimit) {
        this.fetchLimit = fetchLimit;
    }
}
