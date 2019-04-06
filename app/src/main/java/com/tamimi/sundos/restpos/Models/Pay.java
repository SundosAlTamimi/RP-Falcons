package com.tamimi.sundos.restpos.Models;

public class Pay {
    private int transType;
    private int posNo;
    private int userNo;
    private String userName;
    private String transDate;
    private double value;
    private String remark;
    private int shiftNo;
    private String shiftName;
    private String time;

    public Pay(){

    }

    public Pay(int transType, int posNo, int userNo, String userName, String transDate, double value, String remark ,
               int shiftNo , String shiftName ,String time) {
        this.transType = transType;
        this.posNo = posNo;
        this.userNo = userNo;
        this.userName = userName;
        this.transDate = transDate;
        this.value = value;
        this.remark = remark;
        this.shiftNo = shiftNo;
        this.shiftName = shiftName;
        this.time = time;
    }



    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public int getPosNo() {
        return posNo;
    }

    public void setPosNo(int posNo) {
        this.posNo = posNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(int shiftNo) {
        this.shiftNo = shiftNo;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
