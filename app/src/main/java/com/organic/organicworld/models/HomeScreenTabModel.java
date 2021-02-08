package com.organic.organicworld.models;

import java.util.List;

public class HomeScreenTabModel {
    private List<HomeScreenTileModel> models;
    private String title;
    private String categoryUrl;

    public HomeScreenTabModel() {

    }

    public HomeScreenTabModel(List<HomeScreenTileModel> models, String title) {
        this.models = models;
        this.title = title;
    }

    public List<HomeScreenTileModel> getModels() {
        return models;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }
}
