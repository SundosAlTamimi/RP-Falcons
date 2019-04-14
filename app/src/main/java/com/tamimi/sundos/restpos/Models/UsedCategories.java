package com.tamimi.sundos.restpos.Models;

import android.graphics.Bitmap;

public class UsedCategories {

    private String categoryName ;
    private int numberOfItems ;
    private int background ;
    private int textColor ;
    private int position ;
    private Bitmap catPic;

    public UsedCategories(){}

    public UsedCategories(String categoryName, int numberOfItems, int background, int textColor, int position, Bitmap catPic) {
        this.categoryName = categoryName;
        this.numberOfItems = numberOfItems;
        this.background = background;
        this.textColor = textColor;
        this.position = position;
        this.catPic = catPic;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
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

    public Bitmap getCatPic() {
        return catPic;
    }

    public void setCatPic(Bitmap catPic) {
        this.catPic = catPic;
    }
}
