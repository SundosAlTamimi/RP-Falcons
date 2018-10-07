package com.tamimi.sundos.restpos.Models;

public class PayMethod {

    private int orderType;
    private int orderKind;
    private String voucherDate;
    private int pointOfSaleNumber;
    private int storeNumber;
    private String voucherNumber;
    private int voucherSerial;
    private String payType;
    private double payValue;
    private String payNumber;
    private String payName;
    private String shiftName;
    private int shiftNumber;


    public PayMethod() {

    }

    public PayMethod(int orderType, int orderKind, String voucherDate, int pointOfSaleNumber, int storeNumber, String voucherNumber, int voucherSerial, String payType, double payValue, String payNumber, String payName,int shiftNumber,String shiftName) {
        this.orderType = orderType;
        this.orderKind = orderKind;
        this.voucherDate = voucherDate;
        this.pointOfSaleNumber = pointOfSaleNumber;
        this.storeNumber = storeNumber;
        this.voucherNumber = voucherNumber;
        this.voucherSerial = voucherSerial;
        this.payType = payType;
        this.payValue = payValue;
        this.payNumber = payNumber;
        this.payName = payName;
        this.shiftNumber=shiftNumber;
        this.shiftName=shiftName;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public void setOrderKind(int orderKind) {
        this.orderKind = orderKind;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public void setPointOfSaleNumber(int pointOfSaleNumber) {
        this.pointOfSaleNumber = pointOfSaleNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public void setShiftNumber(int shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public void setVoucherSerial(int voucherSerial) {
        this.voucherSerial = voucherSerial;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setPayValue(double payValue) {
        this.payValue = payValue;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public int getOrderType() {
        return orderType;
    }

    public int getOrderKind() {
        return orderKind;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public int getPointOfSaleNumber() {
        return pointOfSaleNumber;
    }

    public int getStoreNumber() {
        return storeNumber;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public int getVoucherSerial() {
        return voucherSerial;
    }

    public String getPayType() {
        return payType;
    }

    public double getPayValue() {
        return payValue;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public String getPayName() {
        return payName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public int getShiftNumber() {
        return shiftNumber;
    }


}
