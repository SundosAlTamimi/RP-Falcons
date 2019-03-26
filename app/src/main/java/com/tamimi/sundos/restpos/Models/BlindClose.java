package com.tamimi.sundos.restpos.Models;

public class BlindClose {
    private int transNo;
    private String date;
    private String time;
    private int POSNo;
    private int shiftNo;
    private String shiftName;
    private int userNo;
    private String userName;
    private double sysSales;
    private double userSales;
    private double salesDiff;
    private double sysCash;
    private double userCash;
    private double cashDiff;
    private double sysOthers;
    private double userOthers;
    private double othersDiff;
    private int tillOk;
    private int transType;
    private String reason;
    private String toUser;

    public BlindClose(){
    }

    public BlindClose(int transNo, String date, String time, int POSNo, int shiftNo, String shiftName, int userNo, String userName,
                      double sysSales, double userSales, double salesDiff, double sysCash, double userCash, double cashDiff,
                      double sysOthers, double userOthers, double othersDiff, int tillOk, int transType, String reason, String toUser) {
        this.transNo = transNo;
        this.date = date;
        this.time = time;
        this.POSNo = POSNo;
        this.shiftNo = shiftNo;
        this.shiftName = shiftName;
        this.userNo = userNo;
        this.userName = userName;
        this.sysSales = sysSales;
        this.userSales = userSales;
        this.salesDiff = salesDiff;
        this.sysCash = sysCash;
        this.userCash = userCash;
        this.cashDiff = cashDiff;
        this.sysOthers = sysOthers;
        this.userOthers = userOthers;
        this.othersDiff = othersDiff;
        this.tillOk = tillOk;
        this.transType = transType;
        this.reason = reason;
        this.toUser = toUser;
    }

    public int getTransNo() {
        return transNo;
    }

    public void setTransNo(int transNo) {
        this.transNo = transNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPOSNo() {
        return POSNo;
    }

    public void setPOSNo(int POSNo) {
        this.POSNo = POSNo;
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

    public double getSysSales() {
        return sysSales;
    }

    public void setSysSales(double sysSales) {
        this.sysSales = sysSales;
    }

    public double getUserSales() {
        return userSales;
    }

    public void setUserSales(double userSales) {
        this.userSales = userSales;
    }

    public double getSalesDiff() {
        return salesDiff;
    }

    public void setSalesDiff(double salesDiff) {
        this.salesDiff = salesDiff;
    }

    public double getSysCash() {
        return sysCash;
    }

    public void setSysCash(double sysCash) {
        this.sysCash = sysCash;
    }

    public double getUserCash() {
        return userCash;
    }

    public void setUserCash(double userCash) {
        this.userCash = userCash;
    }

    public double getCashDiff() {
        return cashDiff;
    }

    public void setCashDiff(double cashDiff) {
        this.cashDiff = cashDiff;
    }

    public double getSysOthers() {
        return sysOthers;
    }

    public void setSysOthers(double sysOthers) {
        this.sysOthers = sysOthers;
    }

    public double getUserOthers() {
        return userOthers;
    }

    public void setUserOthers(double userOthers) {
        this.userOthers = userOthers;
    }

    public double getOthersDiff() {
        return othersDiff;
    }

    public void setOthersDiff(double othersDiff) {
        this.othersDiff = othersDiff;
    }

    public int getTillOk() {
        return tillOk;
    }

    public void setTillOk(int tillOk) {
        this.tillOk = tillOk;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
}
