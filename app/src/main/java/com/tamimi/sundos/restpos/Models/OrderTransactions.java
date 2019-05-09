package com.tamimi.sundos.restpos.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderTransactions {

    private int orderType;
    private int orderKind;
    private String voucherDate;
    private int posNo;
    private int storeNo;
    private String voucherNo;
    private int voucherSerial;
    private String itemBarcode;
    private String itemName;
    private String secondaryName;
    private String kitchenAlias;
    private String itemCategory;
    private String itemFamily;
    private int qty;
    private double price;
    private double total;
    private double discount;
    private double lDiscount;
    private double totalDiscount;
    private double taxValue;
    private double taxPerc;
    private int taxKind;
    private double service;
    private double serviceTax;
    private int tableNo;
    private int sectionNo;
    private int shiftNo;
    private String shiftName;
    private int userNo;
    private String userName;
    private String time;
    private int screenNo;
    private String note;
    private  String orgNo ;
    private  int orgPos ;
    private int returnQty;


    public OrderTransactions() {

    }

    public OrderTransactions(int orderType, int orderKind, String voucherDate, int posNo, int storeNo,
                             String voucherNo, int voucherSerial, String itemBarcode, String itemName,
                             String secondaryName, String kitchenAlias, String itemCategory, String itemFamily,
                             int qty, double price, double total, double discount, double lDiscount, double totalDiscount,
                             double taxValue, double taxPerc, int taxKind, double service, double serviceTax, int tableNo,
                             int sectionNo, int shiftNo, String shiftName, int userNo, String userName, String time, String orgNo, int orgPos, int returnQty) {
        this.orderType = orderType;
        this.orderKind = orderKind;
        this.voucherDate = voucherDate;
        this.posNo = posNo;
        this.storeNo = storeNo;
        this.voucherNo = voucherNo;
        this.voucherSerial = voucherSerial;
        this.itemBarcode = itemBarcode;
        this.itemName = itemName;
        this.secondaryName = secondaryName;
        this.kitchenAlias = kitchenAlias;
        this.itemCategory = itemCategory;
        this.itemFamily = itemFamily;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.discount = discount;
        this.lDiscount = lDiscount;
        this.totalDiscount = totalDiscount;
        this.taxValue = taxValue;
        this.taxPerc = taxPerc;
        this.taxKind = taxKind;
        this.service = service;
        this.serviceTax = serviceTax;
        this.tableNo = tableNo;
        this.sectionNo = sectionNo;
        this.shiftNo = shiftNo;
        this.shiftName = shiftName;
        this.userNo = userNo;
        this.userName = userName;
        this.time = time;
        this.orgNo = orgNo;
        this.orgPos = orgPos;
        this.returnQty = returnQty;
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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderKind() {
        return orderKind;
    }

    public void setOrderKind(int orderKind) {
        this.orderKind = orderKind;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public int getPosNo() {
        return posNo;
    }

    public void setPosNo(int posNo) {
        this.posNo = posNo;
    }

    public int getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(int storeNo) {
        this.storeNo = storeNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public int getVoucherSerial() {
        return voucherSerial;
    }

    public void setVoucherSerial(int voucherSerial) {
        this.voucherSerial = voucherSerial;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSecondaryName() {
        return secondaryName;
    }

    public void setSecondaryName(String seconaryName) {
        this.secondaryName = seconaryName;
    }

    public String getKitchenAlias() {
        return kitchenAlias;
    }

    public void setKitchenAlias(String kitchenAlias) {
        this.kitchenAlias = kitchenAlias;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemFamily() {
        return itemFamily;
    }

    public void setItemFamily(String itemFamily) {
        this.itemFamily = itemFamily;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getlDiscount() {
        return lDiscount;
    }

    public void setlDiscount(double lDiscount) {
        this.lDiscount = lDiscount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

    public double getTaxPerc() {
        return taxPerc;
    }

    public void setTaxPerc(double taxPerc) {
        this.taxPerc = taxPerc;
    }

    public int getTaxKind() {
        return taxKind;
    }

    public void setTaxKind(int taxKind) {
        this.taxKind = taxKind;
    }

    public double getService() {
        return service;
    }

    public void setService(double service) {
        this.service = service;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
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

    public int getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(int screenNo) {
        this.screenNo = screenNo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public int getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(int returnQty) {
        this.returnQty = returnQty;
    }

    public JSONObject getJSONObject() { // for kitchen
        JSONObject obj = new JSONObject();
        try {
            obj.put("ITEMCODE", itemBarcode);
            obj.put("ITEMNAME", itemName);
            obj.put("QTY", qty);
            obj.put("PRICE", price);
            obj.put("NOTE", note);
            if(getOrderKind()==998){
                Log.e("isUpdate1 =","up =1 "+"order kind = "+getOrderKind());
                obj.put("ISUPDATE", 1);}
            else if(getOrderKind()==0){
                Log.e("isUpdate2 =","up =0 "+"order kind = "+getOrderKind());
                obj.put("ISUPDATE", 0);}

            obj.put("SCREENNO", screenNo);

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
        return obj;
    }

    public JSONObject getJSONObject2() { // for server
        JSONObject obj = new JSONObject();
        try {

            obj.put("ORDERTYPE", orderType);
            obj.put("ORDERKIND", orderKind);
            obj.put("POSNO", posNo);
            obj.put("STRNO", storeNo);
            obj.put("VHFNO", voucherNo);
            obj.put("VHFSERIAL", voucherSerial);
            obj.put("ITEMBARCODE1", itemBarcode);
            obj.put("ITEMNM", itemName);
            obj.put("SECONDARYNAME", secondaryName);
            obj.put("KITCHENALIAS", kitchenAlias);
            obj.put("ITEMCAT", itemCategory);
            obj.put("ITEMFAM", itemFamily);
            obj.put("QTY", qty);
            obj.put("PRICE", price);
            obj.put("TOTAL", total);
            obj.put("DISC", discount);
            obj.put("LDISC", lDiscount);
            obj.put("TOTDISC", totalDiscount);
            obj.put("TAXVAL", taxValue);
            obj.put("TAXPERC", taxPerc);
            obj.put("TAXKIND", taxKind);
            obj.put("SERVICE", service);
            obj.put("SERVICETAX", serviceTax);
            obj.put("TBLNO", tableNo);
            obj.put("USERNO", userNo);
            obj.put("USERNM", userName);
            obj.put("SECNO", sectionNo);
            obj.put("SHIFTNO", shiftNo);
            obj.put("SHIFTNM", shiftName);
            obj.put("VHFTIME", time);
            obj.put("VHFDATE", voucherDate);

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
        return obj;
    }
}
