package com.app.plywood.data;

public class PurchaseData {

    private int id;
    private String thick;
    private String size;
    private int price;
    private String quantity;

    public PurchaseData(int id, String thick, String size, int price, String quantity) {
        this.id = id;
        this.thick = thick;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getThick() {
        return thick;
    }

    public String getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }
}
