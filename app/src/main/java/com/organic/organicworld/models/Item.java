package com.organic.organicworld.models;

public class Item {
    private final String name;
    private final String iconUrl;
    private String productUrl;

    public Item(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public Item(String name, String iconUrl, String productUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.productUrl = productUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
