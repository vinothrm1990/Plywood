package com.app.plywood.data;

public class AddProduct {

    private String thick;
    private String size;
    private int price;
    private String quantity;


    public AddProduct(String thick, String size, int price, String quantity) {
        this.thick = thick;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    public String getThick() {
        return thick;
    }

    public String getSize() {
        return size;
    }

    public String getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
