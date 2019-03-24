package com.tamimi.sundos.restpos.Models;

public class ZReport {

    private String Date;
    private int posNo;
    private int userNo;
    private  String userName;
    private int serial;


    public ZReport() {

    }

    public ZReport(String date, int posNo, int userNo, String userName, int serial) {
        this.Date = date;
        this.posNo = posNo;
        this.userNo = userNo;
        this.userName = userName;
        this.serial = serial;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }
}
