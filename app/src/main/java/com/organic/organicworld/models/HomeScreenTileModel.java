package com.organic.organicworld.models;

public class HomeScreenTileModel {
    private final String name;
    private final String imageUrl;

    public HomeScreenTileModel(String name, String categoryUrl, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
