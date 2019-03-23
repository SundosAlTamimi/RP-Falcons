package com.tamimi.sundos.restpos.Models;

public class TableActions {
    private int POSNumber;
    private String userName;
    private int userNo;
    private String shiftName;
    private int shiftNo;
    private int actionType;
    private String actionDate;
    private String actionTime;
    private int tableNo;
    private int sectionNo;
    private int toTable;
    private int toSection;

    public TableActions(int POSNumber, String userName, int userNo, String shiftName, int shiftNo, int actionType, String actionDate,
                        String actionTime, int tableNo, int sectionNo, int toTable, int toSection) {
        this.userName = userName;
        this.userNo = userNo;
        this.shiftName = shiftName;
        this.shiftNo = shiftNo;
        this.actionType = actionType;
        this.actionDate = actionDate;
        this.actionTime = actionTime;
        this.tableNo = tableNo;
        this.sectionNo = sectionNo;
        this.toTable = toTable;
        this.toSection = toSection;
    }

    public TableActions() {

    }

    public int getPOSNumber() {
        return POSNumber;
    }

    public void setPOSNumber(int POSNumber) {
        this.POSNumber = POSNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public int getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(int shiftNo) {
        this.shiftNo = shiftNo;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public int getSectionNo() {
        return sectionNo;
    }

    public void setSectionNo(int sectionNo) {
        this.sectionNo = sectionNo;
    }

    public int getToTable() {
        return toTable;
    }

    public void setToTable(int toTable) {
        this.toTable = toTable;
    }

    public int getToSection() {
        return toSection;
    }

    public void setToSection(int toSection) {
        this.toSection = toSection;
    }
}
