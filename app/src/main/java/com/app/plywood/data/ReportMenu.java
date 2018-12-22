package com.app.plywood.data;

public class ReportMenu {

    private int image;
    private String title;

    public ReportMenu(String title, int image) {
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
