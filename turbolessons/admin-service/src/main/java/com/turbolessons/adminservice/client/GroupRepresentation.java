package com.turbolessons.adminservice.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Minimal Jackson mapping of Keycloak's Admin API {@code GroupRepresentation}.
 *
 * <p>Used for group lookup (by name) and cohort membership. {@link #subGroups} is
 * modeled so nested groups returned by {@code GET /groups?search=} can be searched.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupRepresentation {

    private String id;
    private String name;
    private String path;
    private List<GroupRepresentation> subGroups;

    public GroupRepresentation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<GroupRepresentation> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<GroupRepresentation> subGroups) {
        this.subGroups = subGroups;
    }
}
