package com.noslen.lsaloginservice.constants;

/**
 * This class includes the name and API end points of other microservices that we need to communicate.
 * NOTE: WRITE EVERYTHING IN ALPHABETICAL ORDER
 */
public class MicroServiceConstants {

    public static final String BASE_API = "/api";

    public interface AdminMicroServiceConstants {
        String BASE = "lsa-admin-service";
        String SEARCH_ADMIN = "/api/search";
        String UPDATE_ADMIN = "/api/update";

    }
}
