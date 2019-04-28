package com.tamimi.sundos.restpos.Models;

public class KitchenScreen {

    private int kitchenNo;
    private String kitchenName;
    private String kitchenIP;

    public KitchenScreen() {

    }

    public KitchenScreen(int kitchenNo, String kitchenName,String kitchenIP) {
        this.kitchenNo = kitchenNo;
        this.kitchenName = kitchenName;
        this.kitchenIP = kitchenIP;

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

    public String getKitchenIP() {
        return kitchenIP;
    }

    public void setKitchenIP(String kitchenIP) {
        this.kitchenIP = kitchenIP;
    }
}
