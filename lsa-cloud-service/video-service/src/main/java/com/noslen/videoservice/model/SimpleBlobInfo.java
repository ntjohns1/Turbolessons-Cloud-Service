package com.noslen.videoservice.model;

public class SimpleBlobInfo {
    private String name;
    private String id;

    public SimpleBlobInfo(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

}

