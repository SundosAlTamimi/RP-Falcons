package com.tamimi.sundos.restpos.Models;

public class Recipes {

    private int itemBarcode;
    private int barcode;
    private String item;
    private String unit;
    private double qty;
    private double cost;

    public Recipes(int itemBarcode, int barcode, String item, String unit, double qty, double cost) {
        this.itemBarcode = itemBarcode;
        this.barcode = barcode;
        this.item = item;
        this.unit = unit;
        this.qty = qty;
        this.cost = cost;
    }

    public int getBarcode() {
        return barcode;
    }

    public int getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(int itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
