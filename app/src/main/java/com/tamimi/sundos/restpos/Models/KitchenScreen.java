package com.tamimi.sundos.restpos.Models;

public class KitchenScreen {

    private int kitchenNo;
    private String kitchenName;
    private String kitchenIP;
    private int kitchenType;
    public KitchenScreen() {

    }

    public KitchenScreen(int kitchenNo, String kitchenName, String kitchenIP, int kitchenType) {
        this.kitchenNo = kitchenNo;
        this.kitchenName = kitchenName;
        this.kitchenIP = kitchenIP;
        this.kitchenType = kitchenType;
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

    public int getKitchenType() {
        return kitchenType;
    }

    public void setKitchenType(int kitchenType) {
        this.kitchenType = kitchenType;
    }
}
