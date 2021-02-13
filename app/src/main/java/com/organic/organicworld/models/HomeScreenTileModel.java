package com.organic.organicworld.models;

public class HomeScreenTileModel {
    private final String name;
    private final String imageUrl;
    private final String categoryUrl;

    public HomeScreenTileModel(String name, String categoryUrl, String imageUrl) {
        this.name = name;
        this.categoryUrl = categoryUrl;
        this.imageUrl = imageUrl;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
