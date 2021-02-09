package com.organic.organicworld.models;

public class Item {
    private final String name;
    private final String iconUrl;

    public Item(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
