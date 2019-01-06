package com.tamimi.sundos.restpos.Models;

public class Cashier {

    private String cashierName ;
    private String checkInDate ;
    private String categoryName ;
    private double categoryValue ;
    private int categoryQty ; // 2
    private int orderKind ;

    public Cashier (){

    }

    public Cashier(String cashierName, String checkInDate, String categoryName,
                   double categoryValue, int categoryQty, int orderKind) {
        this.cashierName = cashierName;
        this.checkInDate = checkInDate;
        this.categoryName = categoryName;
        this.categoryValue = categoryValue;
        this.categoryQty = categoryQty;
        this.orderKind = orderKind;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(double categoryValue) {
        this.categoryValue = categoryValue;
    }

    public int getCategoryQty() {
        return categoryQty;
    }

    public void setCategoryQty(int categoryQty) {
        this.categoryQty = categoryQty;
    }

    public int getOrderKind() {
        return orderKind;
    }

    public void setOrderKind(int orderKind) {
        this.orderKind = orderKind;
    }
}
