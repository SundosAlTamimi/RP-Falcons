package com.tamimi.sundos.restpos.Models;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Items {

    public String menuCategory;
    public String menuName;
    public String familyName;
    public double tax;
    public int taxType;
    public String secondaryName;
    public String kitchenAlias;
    public int itemBarcode;
    public int status;
    public int itemType;
    public String inventoryUnit;
    public double wastagePercent;
    public int discountAvailable;
    public int pointAvailable;
    public int openPrice;
    public String kitchenPrinter;
    public int used;
    public int showInMenu;
    public Bitmap pic;

    private int background ;
    private int textColor ;
    private int position ;

    public String name;
    public String description;
    public double price;
    public int img;

    public Items() {

    }

    public Items(String name, String description, double price, int img){
        this.name = name ;
        this.description = description ;
        this.price = price ;
        this.img = img ;
    }

    public Items(String menuCategory, String menuName, String familyName ,double tax, int taxType, String secondaryName,
                 String kitchenAlias, int itemBarcode, int status, int itemType, String inventoryUnit,
                 double wastagePercent, int discountAvailable, int pointAvailable, int openPrice, String kitchenPrinter,
                  String description, double price , int used , int showInMenu , Bitmap pic) {

        this.menuCategory = menuCategory;
        this.menuName = menuName;
        this.familyName = familyName;
        this.tax = tax;
        this.taxType = taxType;
        this.secondaryName = secondaryName;
        this.kitchenAlias = kitchenAlias;
        this.itemBarcode = itemBarcode;
        this.status = status;
        this.itemType = itemType;
        this.inventoryUnit = inventoryUnit;
        this.wastagePercent = wastagePercent;
        this.discountAvailable = discountAvailable;
        this.pointAvailable = pointAvailable;
        this.openPrice = openPrice;
        this.kitchenPrinter = kitchenPrinter;
        this.description = description;
        this.price = price;
        this.used = used;
        this.showInMenu = showInMenu;
        this.pic = pic;
    }

    public Items(String menuCategory, String menuName, String familyName ,double tax, int taxType, String secondaryName,
                 String kitchenAlias, int itemBarcode, int status, int itemType, String inventoryUnit,
                 double wastagePercent, int discountAvailable, int pointAvailable, int openPrice, String kitchenPrinter,
                 String description, double price , int used , int showInMenu , Bitmap pic , int background ,
                 int textColor , int position ) {

        this.menuCategory = menuCategory;
        this.menuName = menuName;
        this.familyName = familyName;
        this.tax = tax;
        this.taxType = taxType;
        this.secondaryName = secondaryName;
        this.kitchenAlias = kitchenAlias;
        this.itemBarcode = itemBarcode;
        this.status = status;
        this.itemType = itemType;
        this.inventoryUnit = inventoryUnit;
        this.wastagePercent = wastagePercent;
        this.discountAvailable = discountAvailable;
        this.pointAvailable = pointAvailable;
        this.openPrice = openPrice;
        this.kitchenPrinter = kitchenPrinter;
        this.description = description;
        this.price = price;
        this.used = used;
        this.showInMenu = showInMenu;
        this.pic = pic;
        this.background = background;
        this.textColor = textColor;
        this.position = position;
    }

    public String getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(String menuCategory) {
        this.menuCategory = menuCategory;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public int getTaxType() {
        return taxType;
    }

    public void setTaxType(int taxType) {
        this.taxType = taxType;
    }

    public String getSecondaryName() {
        return secondaryName;
    }

    public void setSecondaryName(String secondaryName) {
        this.secondaryName = secondaryName;
    }

    public String getKitchenAlias() {
        return kitchenAlias;
    }

    public void setKitchenAlias(String kitchenAlias) {
        this.kitchenAlias = kitchenAlias;
    }

    public int getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(int itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getInventoryUnit() {
        return inventoryUnit;
    }

    public void setInventoryUnit(String inventoryUnit) {
        this.inventoryUnit = inventoryUnit;
    }

    public double getWastagePercent() {
        return wastagePercent;
    }

    public void setWastagePercent(double wastagePercent) {
        this.wastagePercent = wastagePercent;
    }

    public int getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(int discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public int getPointAvailable() {
        return pointAvailable;
    }

    public void setPointAvailable(int pointAvailable) {
        this.pointAvailable = pointAvailable;
    }

    public int getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(int openPrice) {
        this.openPrice = openPrice;
    }

    public String getKitchenPrinter() {
        return kitchenPrinter;
    }

    public void setKitchenPrinter(String kitchenPrinter) {
        this.kitchenPrinter = kitchenPrinter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getShowInMenu() {
        return showInMenu;
    }

    public void setShowInMenu(int showInMenu) {
        this.showInMenu = showInMenu;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("MENU_CATEGORY", menuCategory);
            obj.put("MENU_NAME", menuName);
            obj.put("FAMILY_NAME", familyName);
            obj.put("PRICE", price);
            obj.put("TAX_TYPE", taxType);
            obj.put("TAX_PERCENT", tax);
            obj.put("SECONDARY_NAME", secondaryName);
            obj.put("KITCHEN_NAME", kitchenAlias);
            obj.put("ITEM_BARCODE", itemBarcode);
            obj.put("STATUS", status);
            obj.put("ITEM_TYPE", itemType);
            obj.put("DESCRIPTION", description);
            obj.put("INVENTORY_UNIT", inventoryUnit);
            obj.put("WASTAGE_PERCENT", wastagePercent);
            obj.put("DISCOUNT_AVAILABLE", discountAvailable);
            obj.put("POINT_AVAILABLE", pointAvailable);
            obj.put("OPEN_PRICE", openPrice);
            obj.put("KITCHEN_PRINTER_TO_USE", kitchenPrinter);
            obj.put("USED", used);
            obj.put("SHOW_IN_MENU", showInMenu);
//            obj.put("ITEM_PICTURE", pic);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
