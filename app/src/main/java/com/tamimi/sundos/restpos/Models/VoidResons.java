package com.tamimi.sundos.restpos.Models;

public class VoidResons {
    private int shiftNo;
    private String shiftName;
    private int userNo;
    private String userName;
    private String voidReason;
    private String date;
    private int activeated;

    public VoidResons(int shiftNo, String shiftName, int userNo, String userName, String voidReason, String date, int activeated) {
        this.shiftNo = shiftNo;
        this.shiftName = shiftName;
        this.userNo = userNo;
        this.userName = userName;
        this.voidReason = voidReason;
        this.date = date;
        this.activeated = activeated;
    }

    public VoidResons() {

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

    public String getVoidReason() {
        return voidReason;
    }

    public void setVoidReason(String voidReason) {
        this.voidReason = voidReason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getActiveated() {
        return activeated;
    }

    public void setActiveated(int activeated) {
        this.activeated = activeated;
    }
}
