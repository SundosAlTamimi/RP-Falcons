package com.tamimi.sundos.restpos.Models;

public class UsedItems {

    private String categoryName ;
    private String itemName ;
    private int background ;
    private int textColor ;
    private int position ;

    public UsedItems(){

    }
    public UsedItems(String categoryName, String itemName, int background, int textColor, int position) {
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.background = background;
        this.textColor = textColor;
        this.position = position;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getitemName() {
        return itemName;
    }

    public void setitemName(String itemName) {
        this.itemName = itemName;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
