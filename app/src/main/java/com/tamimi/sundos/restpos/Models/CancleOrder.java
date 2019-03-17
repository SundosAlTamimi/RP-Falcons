package com.tamimi.sundos.restpos.Models;

public class CancleOrder {

    private int orderNo;
    private String transDate;
    private String userName;
    private int userNo;
    private String shiftName;
    private int shiftNo;
    private String waiterName;
    private int waiterNo;
    private String itemCode;
    private String itemName;
    private double qty;
    private double price;
    private double total;
    private String reason;
    private int isAllCancel;

    public CancleOrder() {

    }

    public CancleOrder(int orderNo, String transDate, String userName, int userNo, String shiftName, int shiftNo, String waiterName, int waiterNo, String itemCode, String itemName,
                       double qty, double price, double total, String reason, int isAllCancel) {
        this.orderNo = orderNo;
        this.transDate = transDate;
        this.userName = userName;
        this.userNo = userNo;
        this.shiftName = shiftName;
        this.shiftNo = shiftNo;
        this.waiterName = waiterName;
        this.waiterNo = waiterNo;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.reason = reason;
        this.isAllCancel = isAllCancel;
    }


    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public void setShiftNo(int shiftNo) {
        this.shiftNo = shiftNo;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public void setWaiterNo(int waiterNo) {
        this.waiterNo = waiterNo;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setIsAllCancel(int isAllCancel) {
        this.isAllCancel = isAllCancel;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public String getTransDate() {
        return transDate;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserNo() {
        return userNo;
    }

    public String getShiftName() {
        return shiftName;
    }

    public int getShiftNo() {
        return shiftNo;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public int getWaiterNo() {
        return waiterNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public double getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }

    public String getReason() {
        return reason;
    }

    public int getIsAllCancel() {
        return isAllCancel;
    }
}
