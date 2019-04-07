package com.tamimi.sundos.restpos.Models;

public class ItemWithScreen {
    private int itemCode;
    private String itemName;
    private int screenNo;
    private String screenName;

    public ItemWithScreen(){

    }

    public ItemWithScreen(int itemCode, String itemName, int screenNo, String screenName) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.screenNo = screenNo;
        this.screenName = screenName;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(int screenNo) {
        this.screenNo = screenNo;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
