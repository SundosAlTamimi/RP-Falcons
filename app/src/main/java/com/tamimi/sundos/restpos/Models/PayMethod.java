package com.tamimi.sundos.restpos.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String userName;
    private int userNo;
    private String time;
    private  String orgNo ;
    private  int orgPos ;


    public PayMethod() {

    }

    public PayMethod(int orderType, int orderKind, String voucherDate, int pointOfSaleNumber, int storeNumber,
                     String voucherNumber, int voucherSerial, String payType, double payValue, String payNumber,
                     String payName, String shiftName, int shiftNumber, String userName, int userNo, String time,String orgNo,int orgPos) {

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
        this.shiftName = shiftName;
        this.shiftNumber = shiftNumber;
        this.userName = userName;
        this.userNo = userNo;
        this.time = time;
        this.orgNo = orgNo;
        this.orgPos = orgPos;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public int getOrgPos() {
        return orgPos;
    }

    public void setOrgPos(int orgPos) {
        this.orgPos = orgPos;
    }

    public JSONObject getJSONObject2() { // for server
        JSONObject obj = new JSONObject();
        try {
            obj.put("ORDERTYPE", orderType);
            obj.put("ORDERKIND", orderKind);
            obj.put("POSNO", pointOfSaleNumber);
            obj.put("STRNO", storeNumber);
            obj.put("VHFNO", voucherNumber);
            obj.put("VHFSERIAL", voucherSerial);
            obj.put("PAYTYPE", payType);
            obj.put("PAYVAL", payValue);
            obj.put("PAYNO", payNumber);
            obj.put("PAYNAME", payName);
            obj.put("USERNM", userName);
            obj.put("USERNO", userNo);
            obj.put("SHIFTNM", shiftName);
            obj.put("SHIFTNO", shiftNumber);
            obj.put("VHFTIME", time);
            obj.put("VHFDATE", voucherDate);

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
        return obj;
    }
}
