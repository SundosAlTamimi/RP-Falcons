package com.tamimi.sundos.restpos.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderHeader {

    private int orderType;
    private int orderKind;
    private String voucherDate;
    private int pointOfSaleNumber;
    private int storeNumber;
    private String voucherNumber;
    private int voucherSerial;
    private double total;
    private double totalLineDiscount;
    private double totalDiscount;
    private double allDiscount;
    private double totalService;
    private double totalTax;
    private double totalServiceTax;
    private double subTotal;
    private double amountDue;
    private double deliveryCharge;
    private int sectionNO;
    private int tableNO;
    private double cashValue;
    private double cardsValue;
    private double chequeValue;
    private double couponValue;
    private double giftValue;
    private double pointValue;
    private String shiftName;
    private int shiftNumber;
    private String waiter;
    private int seatsNumber;
    private String userName;
    private int userNo;
    private String time;
    private  String orgNo ;
    private  int orgPos ;
    private  int isPost ;


    public OrderHeader() {

    }

    public OrderHeader(int orderType, int orderKind, String voucherDate, int pointOfSaleNumber,
                       int storeNumber, String voucherNumber, int voucherSerial, double total,
                       double totalLineDiscount, double totalDiscount, double allDiscount, double totalService,
                       double totalTax, double totalServiceTax, double subTotal, double amountDue, double deliveryCharge,
                       int sectionNO, int tableNO, double cashValue, double cardsValue, double chequeValue, double couponValue ,
                       double giftValue, double pointValue, String shiftName,int shiftNumber, String waiter,
                       int seatsNumber, String userName, int userNo, String time,String orgNo,int orgPos) {

        this.orderType = orderType;
        this.orderKind = orderKind;
        this.voucherDate = voucherDate;
        this.pointOfSaleNumber = pointOfSaleNumber;
        this.storeNumber = storeNumber;
        this.voucherNumber = voucherNumber;
        this.voucherSerial = voucherSerial;
        this.total = total;
        this.totalLineDiscount = totalLineDiscount;
        this.totalDiscount = totalDiscount;
        this.allDiscount = allDiscount;
        this.totalService = totalService;
        this.totalTax = totalTax;
        this.totalServiceTax = totalServiceTax;
        this.subTotal = subTotal;
        this.amountDue = amountDue;
        this.deliveryCharge = deliveryCharge;
        this.sectionNO = sectionNO;
        this.tableNO = tableNO;
        this.cashValue = cashValue;
        this.cardsValue = cardsValue;
        this.chequeValue = chequeValue;
        this.couponValue = couponValue;
        this.giftValue = giftValue;
        this.pointValue = pointValue;
        this.shiftName = shiftName;
        this.shiftNumber = shiftNumber;
        this.waiter = waiter;
        this.seatsNumber = seatsNumber;
        this.userName = userName;
        this.userNo = userNo;
        this.time = time;
        this.orgNo = orgNo;
        this.orgPos = orgPos;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
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

    public void setVoucherSerial(int voucherSerial) {
        this.voucherSerial = voucherSerial;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotalLineDiscount(double totalLineDiscount) {
        this.totalLineDiscount = totalLineDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public void setAllDiscount(double allDiscount) {
        this.allDiscount = allDiscount;
    }

    public void setTotalService(double totalService) {
        this.totalService = totalService;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public void setTotalServiceTax(double totalServiceTax) {
        this.totalServiceTax = totalServiceTax;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public void setSectionNO(int secondNO) {
        this.sectionNO = secondNO;
    }

    public void setTableNO(int tableNO) {
        this.tableNO = tableNO;
    }

    public void setCashValue(double cashValue) {
        this.cashValue = cashValue;
    }

    public void setCardsValue(double cardsValue) {
        this.cardsValue = cardsValue;
    }

    public void setChequeValue(double chequeValue) {
        this.chequeValue = chequeValue;
    }

    public void setCouponValue(double couponValue) {
        this.couponValue = couponValue;
    }

    public void setGiftValue(double giftValue) {
        this.giftValue = giftValue;
    }

    public void setPointValue(double pointValue) {
        this.pointValue = pointValue;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public void setShiftNumber(int shiftNumber) {
        this.shiftNumber = shiftNumber;
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

    public double getTotal() {
        return total;
    }

    public double getTotalLineDiscount() {
        return totalLineDiscount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public double getAllDiscount() {
        return allDiscount;
    }

    public double getTotalService() {
        return totalService;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public double getTotalServiceTax() {
        return totalServiceTax;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public int getSectionNO() {
        return sectionNO;
    }

    public int getTableNO() {
        return tableNO;
    }

    public double getCashValue() {
        return cashValue;
    }

    public double getCardsValue() {
        return cardsValue;
    }

    public double getChequeValue() {
        return chequeValue;
    }

    public double getCouponValue() {
        return couponValue;
    }

    public double getGiftValue() {
        return giftValue;
    }

    public double getPointValue() {
        return pointValue;
    }

    public String getShiftName() {
        return shiftName;
    }
    public String getUserName() {
        return userName;
    }

    public int getUserNo() {
        return userNo;
    }

    public int getShiftNumber() {
        return shiftNumber;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(int seatsNumber) {
        this.seatsNumber = seatsNumber;
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

    public int getIsPost() {
        return isPost;
    }

    public void setIsPost(int isPost) {
        this.isPost = isPost;
    }

    public JSONObject getJSONObject() { // for kitchen
        JSONObject obj = new JSONObject();
        try {
            obj.put("POSNO", pointOfSaleNumber);
            obj.put("ORDERNO", voucherNumber);
            obj.put("ORDERTYPE", orderType);
            obj.put("TABLENO", tableNO);
            obj.put("SECTIONNO", sectionNO);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
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
            obj.put("TOTAL", total);
            obj.put("TOTDISC", totalDiscount);
            obj.put("TOTLDISC", totalLineDiscount);
            obj.put("ALLDISC", allDiscount);
            obj.put("TOTSERVICE", totalService);
            obj.put("TOTTAX", totalTax);
            obj.put("TOTSERVICETAX", totalServiceTax);
            obj.put("SUBTOT", subTotal);
            obj.put("AMTDUE", amountDue);
            obj.put("DELVCHARGE", deliveryCharge);
            obj.put("TBLNO", tableNO);
            obj.put("SECNO", sectionNO);
            obj.put("CASHVAL", cashValue);
            obj.put("CARDVAL", cardsValue);
            obj.put("CHEQVAL", chequeValue);
            obj.put("CPNVAL", couponValue);
            obj.put("GIFTVAL", giftValue);
            obj.put("PNTVAL", pointValue);
            obj.put("USERNM", userName);
            obj.put("USERNO", userNo);
            obj.put("SHIFTNM", shiftName);
            obj.put("SHIFTNO", shiftNumber);
            obj.put("WAITER", waiter);
            obj.put("SEATNO", seatsNumber);
            obj.put("VHFTIME", time);
            obj.put("VHFDATE", voucherDate);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
