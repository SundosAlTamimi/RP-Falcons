package com.tamimi.sundos.restpos.Models;

public class FirstInstlation {
    private int compNo;
    private String compYear;
    private String userNameMain;
    private int passwordMain;


    public FirstInstlation() {

    }

    public FirstInstlation(int compNo, String compYear, String userNameMain, int passwordMain) {
        this.compNo = compNo;
        this.compYear = compYear;
        this.userNameMain = userNameMain;
        this.passwordMain = passwordMain;
    }

    public int getCompNo() {
        return compNo;
    }

    public void setCompNo(int compNo) {
        this.compNo = compNo;
    }

    public String getCompYear() {
        return compYear;
    }

    public void setCompYear(String compYear) {
        this.compYear = compYear;
    }

    public String getUserNameMain() {
        return userNameMain;
    }

    public void setUserNameMain(String userNameMain) {
        this.userNameMain = userNameMain;
    }

    public int getPasswordMain() {
        return passwordMain;
    }

    public void setPasswordMain(int passwordMain) {
        this.passwordMain = passwordMain;
    }
}
