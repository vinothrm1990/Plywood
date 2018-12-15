package com.app.plywood.data;

public class ProductMenu {

    private int image;
    private String title;

    public ProductMenu(String title, int image) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
