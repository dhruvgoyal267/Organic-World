package com.organic.organicworld.models;

public class PromotionalModel {
    private String imageUrl, productUrl;

    public PromotionalModel(String imageUrl, String productUrl) {
        this.imageUrl = imageUrl;
        this.productUrl = productUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
}
