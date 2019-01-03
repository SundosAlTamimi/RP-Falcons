package com.tamimi.sundos.restpos.Models;

import android.graphics.Bitmap;

public class Money {

    private int serial;
    private String catName;
    private double catValue;
    private int show;
    private Bitmap picture;

    public Money(){

    }

    public Money(int serial, String catName, int catValue, int show, Bitmap picture) {
        this.serial = serial;
        this.catName = catName;
        this.catValue = catValue;
        this.show = show;
        this.picture = picture;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public double getCatValue() {
        return catValue;
    }

    public void setCatValue(double catValue) {
        this.catValue = catValue;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
