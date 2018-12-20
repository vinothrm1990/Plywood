package com.app.plywood.data;

public class CustomerMenu {

    private int image;
    private String title;

    public CustomerMenu( String title,int image) {
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
