package com.app.plywood.data;

public class DashboardMenu {

    private int image;
    private String title;

    public DashboardMenu(String title, int image) {
        this.title = title;
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public int getImage() {
        return image;
    }
}
