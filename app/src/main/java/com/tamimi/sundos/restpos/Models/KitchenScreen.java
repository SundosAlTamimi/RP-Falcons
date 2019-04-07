package com.tamimi.sundos.restpos.Models;

public class KitchenScreen {

    private int kitchenNo;
    private String kitchenName;

    public KitchenScreen() {

    }

    public KitchenScreen(int kitchenNo, String kitchenName) {
        this.kitchenNo = kitchenNo;
        this.kitchenName = kitchenName;
    }

    public int getKitchenNo() {
        return kitchenNo;
    }

    public void setKitchenNo(int kitchenNo) {
        this.kitchenNo = kitchenNo;
    }

    public String getKitchenName() {
        return kitchenName;
    }

    public void setKitchenName(String kitchenName) {
        this.kitchenName = kitchenName;
    }
}
