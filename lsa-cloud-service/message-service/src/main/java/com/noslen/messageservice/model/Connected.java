package com.noslen.messageservice.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Connected {
    public static Map<String, String> USERS = new ConcurrentHashMap<>();
}
