package com.tamimi.sundos.restpos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.tamimi.sundos.restpos.Models.Announcemet;
import com.tamimi.sundos.restpos.Models.BlindClose;
import com.tamimi.sundos.restpos.Models.BlindCloseDetails;
import com.tamimi.sundos.restpos.Models.BlindShift;
import com.tamimi.sundos.restpos.Models.CancleOrder;
import com.tamimi.sundos.restpos.Models.Cashier;
import com.tamimi.sundos.restpos.Models.CategoryWithModifier;
import com.tamimi.sundos.restpos.Models.Cheque;
import com.tamimi.sundos.restpos.Models.ClockInClockOut;
import com.tamimi.sundos.restpos.Models.CreditCard;
import com.tamimi.sundos.restpos.Models.CustomerPayment;
import com.tamimi.sundos.restpos.Models.CustomerRegistrationModel;
import com.tamimi.sundos.restpos.Models.EmployeeRegistrationModle;
import com.tamimi.sundos.restpos.Models.FamilyCategory;
import com.tamimi.sundos.restpos.Models.ForceQuestions;
import com.tamimi.sundos.restpos.Models.ItemWithFq;
import com.tamimi.sundos.restpos.Models.ItemWithModifier;
import com.tamimi.sundos.restpos.Models.ItemWithScreen;
import com.tamimi.sundos.restpos.Models.Items;
import com.tamimi.sundos.restpos.Models.JobGroup;
import com.tamimi.sundos.restpos.Models.KitchenScreen;
import com.tamimi.sundos.restpos.Models.MemberShipGroup;
import com.tamimi.sundos.restpos.Models.Modifier;
import com.tamimi.sundos.restpos.Models.Money;
import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.Pay;
import com.tamimi.sundos.restpos.Models.PayMethod;
import com.tamimi.sundos.restpos.Models.Recipes;
import com.tamimi.sundos.restpos.Models.Shift;
import com.tamimi.sundos.restpos.Models.TableActions;
import com.tamimi.sundos.restpos.Models.Tables;
import com.tamimi.sundos.restpos.Models.UsedCategories;
import com.tamimi.sundos.restpos.Models.UsedItems;
import com.tamimi.sundos.restpos.Models.VoidResons;
import com.tamimi.sundos.restpos.Models.ZReport;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tamimi.sundos.restpos.Settings.shift_name;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Versions
    private static final int DATABASE_VERSION = 33;

    // Database Name
    private static final String DATABASE_NAME = "RestPos";
    static SQLiteDatabase db;

    //___________________________________________________________________________________
    private static final String MAIN_SETTINGS = "MAIN_SETTINGS";

    private static final String MAIN_SETTINGS_USERNAME = "USERNAME";
    private static final String MAIN_SETTINGS_PASSWORD = "PASSWORD";
    private static final String MAIN_SETTINGS_USER_NO = "USER_NO";
    private static final String MAIN_SETTINGS_POS_NO = "POS_NO";
    private static final String MAIN_SETTINGS_STORE_NO = "STORE_NO";
    private static final String MAIN_SETTINGS_SHIFT_NO = "SHIFT_NO";
    private static final String MAIN_SETTINGS_SHIFT_NAME = "SHIFT_NAME";
    private static final String MAIN_SETTINGS_SERVICE_TAX = "SERVICE_TAX";
    private static final String MAIN_SETTINGS_SERVICE_VALUE = "SERVICE_VALUE";
    private static final String MAIN_SETTINGS_TAX_TYPE = "TAX_TYPE";
    private static final String MAIN_SETTINGS_TIME_CARD = "TIME_CARD";

    //___________________________________________________________________________________
    private static final String ITEMS = "ITEMS";

    private static final String MENU_CATEGORY = "MENU_CATEGORY";
    private static final String MENU_NAME = "MENU_NAME";
    private static final String FAMILY_NAME = "FAMILY_NAME";
    private static final String PRICE = "PRICE";
    private static final String TAX_TYPE = "TAX_TYPE";
    private static final String TAX_PERCENT = "TAX_PERCENT";
    private static final String SECONDARY_NAME = "SECONDARY_NAME";
    private static final String KITCHEN_ALIAS = "KITCHEN_NAME";
    private static final String ITEM_BARCODE = "ITEM_BARCODE";
    private static final String STATUS = "STATUS";
    private static final String ITEM_TYPE = "ITEM_TYPE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String INVENTORY_UNIT = "INVENTORY_UNIT";
    private static final String WASTAGE_PERCENT = "WASTAGE_PERCENT";
    private static final String DISCOUNT_AVAILABLE = "DISCOUNT_AVAILABLE";
    private static final String POINT_AVAILABLE = "POINT_AVAILABLE";
    private static final String OPEN_PRICE = "OPEN_PRICE";
    private static final String KITCHEN_PRINTER_TO_USE = "KITCHEN_PRINTER_TO_USE";
    private static final String USED = "USED";
    private static final String SHOW_IN_MENU = "SHOW_IN_MENU";
    private static final String ITEM_PICTURE = "ITEM_PICTURE";

    //___________________________________________________________________________________
    private static final String RECIPES = "RECIPES";

    private static final String BARCODE = "BARCODE";
    private static final String ITEM = "ITEM";
    private static final String UNIT = "UNIT";
    private static final String QTY = "QTY";
    private static final String COST = "COST";

    //___________________________________________________________________________________
    private static final String USED_CATEGORIES = "USED_CATEGORIES";

    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private static final String CATEGORY_BACKGROUND = "CATEGORY_BACKGROUND";
    private static final String CATEGORY_TEXT_COLOR = "CATEGORY_TEXT_COLOR";
    private static final String CATEGORY_POSITION = "CATEGORY_POSITION";

    //___________________________________________________________________________________
    private static final String USED_ITEMS = "USED_ITEMS";

    private static final String CATEGORY_NAME2 = "CATEGORY_NAME2";
    private static final String ITEM_NAME = "ITEM_NAME";
    private static final String ITEM_BACKGROUND = "ITEM_BACKGROUND";
    private static final String ITEM_TEXT_COLOR = "ITEM_TEXT_COLOR";
    private static final String ITEM_POSITION = "ITEM_POSITION";

    //___________________________________________________________________________________
    private static final String TABLES = "TABLES";

    private static final String HEIGHT = "HEIGHT";
    private static final String WIDTH = "WIDTH";
    private static final String IMAGE_RESOURCE = "IMAGE_RESOURCE";
    private static final String MARGIN_LEFT = "MARGIN_LEFT";
    private static final String MARGIN_TOP = "MARGIN_TOP";
    private static final String FLOOR = "FLOOR";
    private static final String TABLE_NUMBER = "TABLE_NUMBER";

    //___________________________________________________________________________________
    private static final String MONEY_CATEGORIES = "MONEY_CATEGORIES";

    private static final String SERIAL1 = "SERIAL";
    private static final String CATEGORY_NAME1 = "CATEGORY_NAME";
    private static final String CATEGORY_VALUE1 = "CATEGORY_VALUE";
    private static final String SHOW1 = "SHOW";
    private static final String PICTURE1 = "PICTURE1";

    //___________________________________________________________________________________
    private static final String CASHIER_IN_OUT = "CASHIER_IN_OUT";

    private static final String CASHIER_NAME3 = "CASHIER_NAME";
    private static final String DATE3 = "DATE";
    private static final String CATEGORY_NAME3 = "CATEGORY_NAME";
    private static final String CATEGORY_VALUE3 = "CATEGORY_VALUE";
    private static final String CATEGORY_QTY3 = "CATEGORY_QTY";
    private static final String ORDER_KIND3 = "ORDER_KIND";


    //___________________________________________________________________________________
    private static final String PAY_IN_OUT = "PAY_IN_OUT";

    private static final String TRANS_TYPE = "TRANS_TYPE";
    private static final String POS_NO = "POS_NO";
    private static final String USER_NO = "USER_NO";
    private static final String USER_NAME = "USER_NAME";
    private static final String TRANS_DATE = "TRANS_DATE";
    private static final String VALUE = "VALUE";
    private static final String REMARK = "REMARK";
    private static final String SHEFT_NO = "SHIFT_NO";
    private static final String SHEFT_NAME = "SHIFT_NAME";
    private static final String TIME = "TIME";

    //___________________________________________________________________________________
    private static final String CREDIT_CARDS = "CREDIT_CARDS";

    private static final String SERIAL = "SERIAL";
    private static final String CARD_NAME = "CARD_NAME";
    private static final String ACC_CODE = "ACC_CODE";

    //___________________________________________________________________________________
    private static final String CHEQUES = "CHEQUES";

    private static final String SERIAL_CHEEK = "SERIAL_CHEEK";
    private static final String BANK_NAME = "BANK_NAME";
    private static final String CHEQUE_NUMBER = "CHEQUE_NUMBER";
    private static final String RECIVIDE = "RECIVIDE";

    //___________________________________________________________________________________
    private static final String ORDER_TRANSACTIONS = "ORDER_TRANSACTIONS";
    private static final String ORDER_TRANSACTIONS_TEMP = "ORDER_TRANSACTIONS_TEMP";

    private static final String ORDER_TYPE1 = "ORDER_TYPE";
    private static final String ORDER_KIND1 = "ORDER_KIND";
    private static final String VOUCHER_DATE1 = "VOUCHER_DATE";
    private static final String POS_NO1 = "POS_NO";
    private static final String STORE_NO1 = "STORE_NO";
    private static final String VOUCHER_NO1 = "VOUCHER_NO";
    private static final String VOUCHER_SERIAL1 = "VOUCHER_SERIAL";
    private static final String ITEM_BARCODE1 = "ITEM_BARCODE1";
    private static final String ITEM_NAME1 = "ITEM_NAME";
    private static final String SECONDARY_NAME1 = "SECONDARY_NAME";
    private static final String KITCHEN_ALIAS1 = "KITCHEN_ALIAS";
    private static final String ITEM_CATEGORY1 = "ITEM_CATEGORY";
    private static final String ITEM_FAMILY1 = "ITEM_FAMILY";
    private static final String QTY1 = "QTY";
    private static final String PRICE1 = "PRICE";
    private static final String TOTAL1 = "TOTAL";
    private static final String DISCOUNT1 = "DISCOUNT";
    private static final String L_DISCOUNT1 = "L_DISCOUNT";
    private static final String TOTAL_DISCOUNT1 = "TOTAL_DISCOUNT";
    private static final String TAX_VLUE1 = "TAX_VLUE";
    private static final String TAX_PERC1 = "TAX_PERC";
    private static final String TAX_KIND1 = "TAX_KIND";
    private static final String SERVICE1 = "SERVICE";
    private static final String SERVICE_TAX1 = "SERVICE_TAX";
    private static final String TABLE_NO1 = "TABLE_NO";
    private static final String SECTION_NO1 = "SECTION_NO";
    private static final String SHIFT_NO1 = "SHIFT_NO";
    private static final String SHIFT_NAME1 = "SHIFT_NAME";
    private static final String USER_NAME1 = "USER_NAME";
    private static final String USER_NO1 = "USER_NO";
    private static final String TIME1 = "TIME";
    private static final String ORG_NO1 = "ORG_NO";
    private static final String ORG_POS1 = "ORG_POS";
    private static final String RETURN_QTY1 = "RETURN_QTY";
    //____________________________________________________________________________________
    private static final String PAY_METHOD = "PAY_METHOD";

    private static final String ORDER_TYPE = "ORDER_TYPE";
    private static final String ORDER_KIND = "ORDER_KIND";
    private static final String VOUCHER_DATE = "VOUCHER_DATE";
    private static final String POINT_OF_SALE_NUMBER = "POINT_OF_SALE_NUMBER";
    private static final String STORE_NUMBER = "STORE_NUMBER";
    private static final String VOUCHER_NUMBER = "VOUCHER_NUMBER";
    private static final String VOUCHER_SERIAL = "VOUCHER_SERIAL";
    private static final String PAY_TYPE = "PAY_TYPE";
    private static final String PAY_VALUE = "PAY_VALUE";
    private static final String PAY_NUMBER = "PAY_NUMBER";
    private static final String PAY_NAME = "PAY_NAME";
    private static final String SHIFT_NO = "SHIFT_NO";
    private static final String SHIFT_NAME = "SHIFT_NAME";
    private static final String USER_NAME14 = "USER_NAME";
    private static final String USER_NO14 = "USER_NO";
    private static final String TIME14 = "TIME";
    private static final String ORG_NO14 = "ORG_NO";
    private static final String ORG_POS14 = "ORG_POS";
    //________________________________________________________________________________________
    private static final String ORDER_HEADER = "ORDER_HEADER";
    private static final String ORDER_HEADER_TEMP = "ORDER_HEADER_TEMP";

    private static final String ORDER_TYPE2 = "ORDER_TYPE";
    private static final String ORDER_KIND2 = "ORDER_KIND";
    private static final String VOUCHER_DATE2 = "VOUCHER_DATE";
    private static final String POINT_OF_SALE_NUMBER2 = "POINT_OF_SALE_NUMBER";
    private static final String STORE_NUMBER2 = "STORE_NUMBER";
    private static final String VOUCHER_NUMBER2 = "VOUCHER_NUMBER";
    private static final String VOUCHER_SERIAL2 = "VOUCHER_SERIAL";
    private static final String TOTAL2 = "TOTAL";
    private static final String TOTAL_DISCOUNT2 = "TOTAL_DISCOUNT";
    private static final String TOTAL_LINE_DISCOUNT2 = "TOTAL_LINE_DISCOUNT";
    private static final String ALL_DISCOUNT2 = "ALL_DISCOUNT";
    private static final String TOTAL_SERVICES2 = "TOTAL_SERVICES";
    private static final String TOTAL_TAX2 = "TOTAL_TAX";
    private static final String AMOUNT_DUE2 = "AMOUNT_DUE";
    private static final String SUB_TOTAL2 = "SUB_TOTAL";
    private static final String TOTAL_SERVICES_TAX2 = "TOTAL_SERVICES_TAX";
    private static final String DELIVERY_CHARGE2 = "DELIVERY_CHARGE";
    private static final String TABLE_NUMBER2 = "TABLE_NUMBER";
    private static final String SECTION_NUMBER2 = "SECTION_NUMBER";
    private static final String CASH_VALUE2 = "CASH_VALUE";
    private static final String CARDS_VALUE2 = "CARDS_VALUE";
    private static final String CHEQUE_VALUE2 = "CHEQUE_VALUE";
    private static final String COUPON_VALUE2 = "COUPON_VALUE";
    private static final String GIFT_VALUE2 = "GIFT_VALUE";
    private static final String POINT_VALUE2 = "POINT_VALUE";
    private static final String SHIFT_NO2 = "SHIFT_NUMBER";
    private static final String SHIFT_NAME2 = "SHIFT_NAME";
    private static final String WAITER2 = "WAITER";
    private static final String SEATS_NUMBER2 = "SEATS_NUMBER";
    private static final String USER_NAME2 = "USER_NAME";
    private static final String USER_NO2 = "USER_NO";
    private static final String TIME2 = "TIME";
    private static final String ORG_NO2 = "ORG_NO";
    private static final String ORG_POS2 = "ORG_POS";

    //___________________________________________________________________________________
    private static final String FORCE_QUESTIONS = "FORCE_QUESTIONS";

    private static final String QUESTION_NO = "QUESTION_NO";
    private static final String QUESTION_TEXT = "QUESTION_TEXT";
    private static final String MULTIPLE_ANSWER = "MULTIPLE_ANSWER";
    private static final String ANSWER = "ANSWER";

    //___________________________________________________________________________________
    private static final String MODIFIER = "MODIFIER";

    private static final String MODIFIER_NO1 = "MODIFIER_NO";
    private static final String MODIFIER_NAME1 = "MODIFIER_NAME";
    private static final String ACTIVE1 = "ACTIVE";

    //___________________________________________________________________________________
    private static final String CATEGORY_WITH_MODIFIER = "CATEGORY_WITH_MODIFIER";

    private static final String MODIFIER_NAME2 = "MODIFIER_NAME";
    private static final String CATEGORY2 = "CATEGORY";

    //___________________________________________________________________________________
    private static final String ITEM_WITH_MODIFIER = "ITEM_WITH_MODIFIER";

    private static final String ITEM_CODE = "ITEM_CODE";
    private static final String MODIFIER_NO = "MODIFIER_NO";
    private static final String MODIFIER_TEXT = "MODIFIER_TEXT";

    //___________________________________________________________________________________
    private static final String ITEM_WITH_SCREEN = "ITEM_WITH_SCREEN";

    private static final String ITEM_CODE3 = "ITEM_CODE";
    private static final String ITEM_NAME3 = "ITEM_NAME";
    private static final String SCREEN_NO3 = "SCREEN_NO";
    private static final String SCREEN_NAME3 = "SCREEN_NAME";

    //___________________________________________________________________________________
    private static final String ITEM_WITH_FQ = "ITEM_WITH_FQ";

    private static final String ITEM_CODE2 = "ITEM_CODE";
    private static final String QUESTION_NO2 = "QUESTION_NO";
    private static final String QUESTION_TEXT2 = "QUESTION_TEXT";

    //__________________________________________________________________________________

    private static final String CUSTOMER_PAYMENT = "CUSTOMER_PAYMENT";

    private static final String POINT_OF_SALE_NUMBER3 = "POINT_OF_SALE_NUMBER";
    private static final String USER_NAME3 = "USER_NAME";
    private static final String USER_NO3 = "USER_NO";
    private static final String CUSTOMER_NO3 = "CUSTOMER_NO";
    private static final String CUSTOMER_NAME3 = "CUSTOMER_NAME";
    private static final String CUSTOMER_BALANCE3 = "CUSTOMER_BALANCE";
    private static final String TRANS_NO3 = "TRANS_NO";
    private static final String TRANS_DATE3 = "TRANS_DATE";
    private static final String PAYMENT_TYPE3 = "PAYMENT_TYPE";
    private static final String VALUE3 = "VALUE";
    private static final String SHIFT_NUMBER3 = "SHIFT_NUMBER";
    private static final String SHIFT_NAME3 = "SHIFT_NAME";
    //__________________________________________________________________

    private static final String CLOCK_IN_CLOCK_OUT = "CLOCK_IN_CLOCK_OUT";

    private static final String POINT_OF_SALE_NUMBER4 = "POINT_OF_SALE_NUMBER";
    private static final String DATE4 = "DATE";
    private static final String USER_NO4 = "USER_NO";
    private static final String USER_NAME4 = "USER_NAME";
    private static final String TRANS_TYPE4 = "TRANS_TYPE";
    private static final String DATE_CARD4 = "DATE_CARD";
    private static final String TIME_CARD4 = "TIME_CARD";
    private static final String REMARK4 = "REMARK";
    private static final String SHIFT_NUMBER4 = "SHIFT_NUMBER";
    private static final String SHIFT_NAME4 = "SHIFT_NAME";
    //__________________________________________________________________________________
    private static final String JOB_GROUP_TABLE = "JOB_GROUP_TABLE";

    private static final String JOB_GROUP5 = "JOB_GROUP";
    private static final String USER_NAME5 = "USER_NAME";
    private static final String USER_NO5 = "USER_NO";
    private static final String IN_DATE5 = "IN_DATE";
    private static final String ACTIVE5 = "ACTIVE";
    private static final String SHIFT_NO5 = "SHIFT_NO";
    private static final String SHIFT_NAME5 = "SHIFT_NAME";
    //____________________________________________________________________________________
    private static final String MEMBER_SHIP_GROUP_MANAGEMENT_TABLE = "MEMBER_SHIP_GROUP_MANAGEMENT_TABLE";

    private static final String MEMBER_SHIP_GROUP = "MEMBER_SHIP_GROUP";
    private static final String USER_NAME6 = "USER_NAME";
    private static final String USER_NO6 = "USER_NO";
    private static final String IN_DATE6 = "IN_DATE";
    private static final String SHIFT_NO6 = "SHIFT_NO";
    private static final String SHIFT_NAME6 = "SHIFT_NAME";
    private static final String ACTIVE6 = "ACTIVITY";

    //____________________________________________________________________________________
    private static final String SHIFT_REGISTRATION = "SHIFT_REGISTRATION";

    private static final String SHIFT_NO9 = "SHIFT_NO";
    private static final String SHIFT_NAME9 = "SHIFT_NAME";
    private static final String FROM_TIME9 = "FROM_TIME";
    private static final String TO_TIME9 = "TO_TIME";

    //____________________________________________________________________________________
    private static final String BLIND_SHIFT_IN = "BLIND_SHIFT_IN";

    private static final String DATE10 = "DATE";
    private static final String TIME10 = "TIME";
    private static final String POS_NO10 = "POS_NO";
    private static final String SHIFT_NO10 = "SHIFT_NO";
    private static final String SHIFT_NAME10 = "SHIFT_NAME";
    private static final String USER_NO10 = "USER_NO";
    private static final String USER_NAME10 = "USER_NAME";
    private static final String STATUS10 = "STATUS";

    //____________________________________________________________________________________
    private static final String BLIND_CLOSE = "BLIND_CLOSE";

    private static final String TRANS_NO11 = "TRANS_NO";
    private static final String DATE11 = "DATE";
    private static final String TIME11 = "TIME";
    private static final String POS_NO11 = "POS_NO";
    private static final String SHIFT_NO11 = "SHIFT_NO";
    private static final String SHIFT_NAME11 = "SHIFT_NAME";
    private static final String USER_NO11 = "USER_NO";
    private static final String USER_NAME11 = "USER_NAME";
    private static final String SYS_SALES11 = "SYS_SALES";
    private static final String USER_SALES11 = "USER_SALES";
    private static final String SALES_DIFF11 = "SALES_DIFF";
    private static final String SYS_CASH11 = "SYS_CASH";
    private static final String USER_CASH11 = "USER_CASH";
    private static final String CASH_DIFF11 = "CASH_DIFF";
    private static final String SYS_OTHER_PAYMENTS11 = "SYS_OTHER_PAYMENTS";
    private static final String USER_OTHER_PAYMENTS11 = "USER_OTHER_PAYMENTS";
    private static final String OTHER_PAYMENTS_DIFF11 = "OTHER_PAYMENTS_DIFF";
    private static final String TILL_OK11 = "TILL_OK";
    private static final String TRANS_TYPE11 = "TRANS_TYPE";
    private static final String REASON11 = "REASON";
    private static final String TO_USER11 = "TO_USER";

    //____________________________________________________________________________________
    private static final String BLIND_CLOSE_DETAILS = "BLIND_CLOSE_DETAILS";

    private static final String TRANS_NO12 = "TRANS_NO";
    private static final String DATE12 = "DATE";
    private static final String TIME12 = "TIME";
    private static final String POS_NO12 = "POS_NO";
    private static final String SHIFT_NO12 = "SHIFT_NO";
    private static final String SHIFT_NAME12 = "SHIFT_NAME";
    private static final String USER_NO12 = "USER_NO";
    private static final String USER_NAME12 = "USER_NAME";
    private static final String CAT_NAME12 = "CAT_NAME";
    private static final String CAT_QTY12 = "CAT_QTY";
    private static final String CAT_VALUE12 = "CAT_VALUE";
    private static final String CAT_TOTAL12 = "CAT_TOTAL";
    private static final String TYPE12 = "TYPE";
    private static final String UPDATE_DATE12 = "UPDATE_DATE";
    private static final String UPDATE_TIME12 = "UPDATE_TIME";
    private static final String UPDATE_USER_NO12 = "UPDATE_USER_NO";
    private static final String UPDATE_USER_NAME12 = "UPDATE_USER_NAME";

    //________________________________________________________________________________________
    private static final String EMPLOYEE_REGISTRATION_TABLE = "EMPLOYEE_REGISTRATION_TABLE";

    private static final String JOB_GROUP7 = "JOB_GROUP";
    private static final String EMPLOYEE_NAME7 = "EMPLOYEE_NAME";
    private static final String EMPLOYEE_NO7 = "EMPLOYEE_NO";
    private static final String MOBILE_NO7 = "MOBILE_NO";
    private static final String SECURITY_LEVEL7 = "SECURITY_LEVEL";
    private static final String USER_PASSWORD7 = "USER_PASSWORD";
    private static final String ACTIVE7 = "ACTIVE";
    private static final String HIRE_DATA7 = "HIRE_DATA";
    private static final String TERMINATION_DATE7 = "TERMINATION_DATE";
    private static final String PAY_BASIC7 = "PAY_BASIC";
    private static final String PAY_RATE7 = "PAY_RATE";
    private static final String HOLIDAY_PAY7 = "HOLIDAY_PAY";
    private static final String EMPLOYEE_TYPE7 = "EMPLOYEE_TYPE";
    private static final String SHIFT_NO7 = "SHIFT_NO";
    private static final String SHIFT_NAME7 = "SHIFT_NAME";

    //________________________________________________________________________________________
    private static final String CUSTOMER_REGISTRATION_TABLE = "CUSTOMER_REGISTRATION_TABLE";

    private static final String MEMBER_SHIP_GROUP8 = "MEMBER_SHIP_GROUP";
    private static final String CUSTOMER_NAME8 = "CUSTOMER_NAME";
    private static final String CUSTOMER_CODE8 = "CUSTOMER_CODE";
    private static final String MEMBER_SHIP_CARD8 = "MEMBER_SHIP_CARD";
    private static final String GENDER8 = "GENDER";
    private static final String REMARK8 = "REMARK";
    private static final String STREET_NO_NAME8 = "STREET_NO_NAME";
    private static final String CITY8 = "CITY";
    private static final String PHONE_NO8 = "PHONE_NO";
    private static final String MOBILE_NO8 = "MOBILE_NO";
    private static final String NAME_SHOW8 = "NAME_SHOW";
    private static final String BIRTHDAY8 = "BIRTHDAY";
    private static final String ANNIVERSARY8 = "ANNIVERSARY";
    private static final String OCCUPATION8 = "OCCUPATION";
    private static final String EMAIL8 = "EMAIL";
    private static final String TOTAL_POINT8 = "TOTAL_POINT";
    private static final String REDEEMED_POINT8 = "REDEEMED_POINT";
    private static final String REMAINING_POINT8 = "REMAINING_POINT";
    private static final String SHIFT_NO8 = "SHIFT_NO";
    private static final String SHIFT_NAME8 = "SHIFT_NAME";


    //____________________________________________________________________________________
    private static final String FAMILY_CATEGORY_TABLE = "FAMILY_CATEGORY_TABLE";

    private static final String SERIAL2 = "SERIAL";
    private static final String TYPE2 = "TYPE";
    private static final String NAME_CATEGORY_FAMILY2 = "NAME_CATEGORY_FAMILY";
    private static final String CATEGORY_PIC2 = "CATEGORY_PIC";

    //____________________________________________________________________________________
    private static final String VOID_REASONS = "VOID_REASONS";

    private static final String SHIFT_NAME15 = "SHIFT_NAME";
    private static final String SHIFT_NO15 = "SHIFT_NO";
    private static final String USER_NAME15 = "USER_NAME";
    private static final String USER_NUMBER15 = "USER_NUMBER";
    private static final String VOID_REASON15 = "VOID_REASON";
    private static final String DATE15 = "DATE";
    private static final String ACTIVEATED15 = "ACTIVEATED";

    //____________________________________________________________________________________
    private static final String CANCEL_ORDER = "CANCEL_ORDER";

    private static final String ORDER_NO13 = "ORDER_NO";
    private static final String TRANCE_DATE13 = "TRANCE_DATE";
    private static final String USER_NO13 = "USER_NO";
    private static final String USER_NAME13 = "USER_NAME";
    private static final String SHIFT_NO13 = "SHIFT_NO";
    private static final String SHIFT_NAME13 = "SHIFT_NAME";
    private static final String WAITER_NO13 = "WAITER_NO";
    private static final String WAITER_NAME13 = "WAITER_NAME";
    private static final String ITEM_CODE13 = "ITEM_CODE";
    private static final String ITEM_NAME13 = "ITEM_NAME";
    private static final String QTY13 = "QTY";
    private static final String PRICE13 = "PRICE";
    private static final String TOTAL13 = "TOTAL";
    private static final String REASON13 = "REASON";
    private static final String IS_ALL_CANCEL13 = "IS_ALL_CANCEL";
    private static final String TIME13 = "TIME";
    private static final String POS_NO13 = "POS_NO";

    //____________________________________________________________________________________
    private static final String TABLE_ACTIONS = "TABLE_ACTIONS";

    private static final String POS_NO16 = "POS_NO";
    private static final String USER_NO16 = "USER_NO";
    private static final String USER_NAME16 = "USER_NAME";
    private static final String SHIFT_NO16 = "SHIFT_NO";
    private static final String SHIFT_NAME16 = "SHIFT_NAME";
    private static final String ACTION_TYPE16 = "ACTION_TYPE";
    private static final String ACTION_DATE16 = "ACTION_DATE";
    private static final String ACTION_TIME16 = "ACTION_TIME";
    private static final String TABLE_NO16 = "TABLE_NO";
    private static final String SECTION_NO16 = "SECTION_NO";
    private static final String TO_TABLE16 = "TO_TABLE";
    private static final String TO_SECTION16 = "TO_SECTION";

    //____________________________________________________________________________________
    private static final String Z_REPORT_TABLE = "Z_REPORT_TABLE";

    private static final String DATE17 = "DATE";
    private static final String USER_NO17 = "USER_NO";
    private static final String USER_NAME17 = "USER_NAME";
    private static final String POS_NO17 = "POS_NO";
    private static final String SERIAL17 = "SERIAL";


    //____________________________________________________________________________________
    private static final String ANNOUNCEMENT_TABLE = "ANNOUNCEMENT_TABLE";

    private static final String SHIFT_NAME18 = "SHIFT_NAME";
    private static final String USER_NAME18 = "USER_NAME";
    private static final String ANNOUNCEMENT_DATE18 = "ANNOUNCEMENT_DATE";
    private static final String POS_NO18 = "POS_NO";
    private static final String MESSAGE18 = "MESSAGE";
    private static final String IS_SHOW18 = "IS_SHOW";
    private static final String USER_NO18 = "USER_NO";

    //____________________________________________________________________________________
    private static final String KITCHEN_SCREEN_TABLE = "KITCHEN_SCREEN_TABLE";

    private static final String KITCHEN_NO = "KITCHEN_NO";
    private static final String KITCHEN_NAME = "KITCHEN_NAME";
    private static final String KITCHEN_IP = "KITCHEN_IP";


    //___________________________________________________________________________________
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    void createTables(SQLiteDatabase db) {

        String CREATE_TABLE_MAIN_SETTINGS = "CREATE TABLE " + MAIN_SETTINGS + "("
                + MAIN_SETTINGS_USERNAME + " TEXT,"
                + MAIN_SETTINGS_PASSWORD + " INTEGER,"
                + MAIN_SETTINGS_USER_NO + " INTEGER,"
                + MAIN_SETTINGS_POS_NO + " INTEGER,"
                + MAIN_SETTINGS_STORE_NO + " INTEGER,"
                + MAIN_SETTINGS_SHIFT_NO + " INTEGER,"
                + MAIN_SETTINGS_SHIFT_NAME + " TEXT,"
                + MAIN_SETTINGS_SERVICE_TAX + " REAL,"
                + MAIN_SETTINGS_SERVICE_VALUE + " REAL,"
                + MAIN_SETTINGS_TAX_TYPE + " INTEGER,"
                + MAIN_SETTINGS_TIME_CARD + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_MAIN_SETTINGS);
        //___________________________________________________________________________________

        String CREATE_TABLE_ITEMS = "CREATE TABLE " + ITEMS + "("
                + MENU_CATEGORY + " TEXT,"
                + MENU_NAME + " TEXT,"
                + FAMILY_NAME + " TEXT,"
                + PRICE + " INTEGER,"
                + TAX_TYPE + " INTEGER,"
                + TAX_PERCENT + " INTEGER,"
                + SECONDARY_NAME + " TEXT,"
                + KITCHEN_ALIAS + " TEXT,"
                + ITEM_BARCODE + " TEXT,"
                + STATUS + " INTEGER,"
                + ITEM_TYPE + " INTEGER,"
                + DESCRIPTION + " TEXT,"
                + INVENTORY_UNIT + " TEXT,"
                + WASTAGE_PERCENT + " INTEGER,"
                + DISCOUNT_AVAILABLE + " INTEGER,"
                + POINT_AVAILABLE + " INTEGER,"
                + OPEN_PRICE + " INTEGER,"
                + KITCHEN_PRINTER_TO_USE + " TEXT,"
                + USED + " INTEGER,"
                + SHOW_IN_MENU + " INTEGER,"
                + ITEM_PICTURE + " BLOB" + ")";
        db.execSQL(CREATE_TABLE_ITEMS);
        //___________________________________________________________________________________

        String CREATE_TABLE_RECIPES = "CREATE TABLE " + RECIPES + "("
                + BARCODE + " INTEGER,"
                + ITEM + " TEXT,"
                + UNIT + " TEXT,"
                + QTY + " INTEGER,"
                + COST + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_RECIPES);

        //___________________________________________________________________________________

        String CREATE_TABLE_USED_CATEGORIES = "CREATE TABLE " + USED_CATEGORIES + "("
                + CATEGORY_NAME + " TEXT,"
                + NUMBER_OF_ITEMS + " INTEGER,"
                + CATEGORY_BACKGROUND + " INTEGER,"
                + CATEGORY_TEXT_COLOR + " INTEGER,"
                + CATEGORY_POSITION + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_USED_CATEGORIES);

        //___________________________________________________________________________________

        String CREATE_TABLE_USED_ITEMS = "CREATE TABLE " + USED_ITEMS + "("
                + CATEGORY_NAME2 + " TEXT,"
                + ITEM_NAME + " TEXT,"
                + ITEM_BACKGROUND + " INTEGER,"
                + ITEM_TEXT_COLOR + " INTEGER,"
                + ITEM_POSITION + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_USED_ITEMS);

        //___________________________________________________________________________________

        String CREATE_TABLE_TABLES = "CREATE TABLE " + TABLES + "("
                + HEIGHT + " INTEGER,"
                + WIDTH + " INTEGER,"
                + IMAGE_RESOURCE + " INTEGER,"
                + MARGIN_LEFT + " INTEGER,"
                + MARGIN_TOP + " INTEGER,"
                + FLOOR + " INTEGER,"
                + TABLE_NUMBER + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_TABLES);

        //___________________________________________________________________________________

        String CREATE_TABLE_MONEY_CATEGORIES = "CREATE TABLE " + MONEY_CATEGORIES + "("
                + SERIAL1 + " INTEGER,"
                + CATEGORY_NAME1 + " TEXT,"
                + CATEGORY_VALUE1 + " INTEGER,"
                + SHOW1 + " INTEGER,"
                + PICTURE1 + " BLOB" + ")";
        db.execSQL(CREATE_TABLE_MONEY_CATEGORIES);

        //___________________________________________________________________________________

        String CREATE_TABLE_CASHIER_IN_OUT = "CREATE TABLE " + CASHIER_IN_OUT + "("
                + CASHIER_NAME3 + " TEXT,"
                + DATE3 + " TEXT,"
                + CATEGORY_NAME3 + " TEXT,"
                + CATEGORY_VALUE3 + " INTEGER,"
                + CATEGORY_QTY3 + " INTEGER,"
                + ORDER_KIND3 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_CASHIER_IN_OUT);

        //___________________________________________________________________________________

        String CREATE_TABLE_PAY_IN_OUT = "CREATE TABLE " + PAY_IN_OUT + "("
                + TRANS_TYPE + " INTEGER,"
                + POS_NO + " INTEGER,"
                + USER_NO + " INTEGER,"
                + USER_NAME + " TEXT,"
                + TRANS_DATE + " TEXT,"
                + VALUE + " INTEGER,"
                + REMARK + " TEXT,"
                + SHEFT_NO + " INTEGER,"
                + SHEFT_NAME + " TEXT,"
                + TIME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_PAY_IN_OUT);

        //___________________________________________________________________________________

        String CREATE_TABLE_CREDIT_CARDS = "CREATE TABLE " + CREDIT_CARDS + "("
                + SERIAL + " INTEGER,"
                + CARD_NAME + " TEXT,"
                + ACC_CODE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_CREDIT_CARDS);

        //_______________________________________________________________________________

        String CREATE_TABLE_CHEEK = "CREATE TABLE " + CHEQUES + "(" +
                SERIAL_CHEEK + " INTEGER ,"
                + BANK_NAME + " TEXT,"
                + CHEQUE_NUMBER + " TEXT,"
                + RECIVIDE + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_CHEEK);

        //_______________________________________________________________________________

        String CREATE_TABLE_ORDER_TRANSACTIONS = "CREATE TABLE " + ORDER_TRANSACTIONS + "("
                + ORDER_TYPE1 + " INTEGER,"
                + ORDER_KIND1 + " INTEGER,"
                + VOUCHER_DATE1 + " TEXT,"
                + POS_NO1 + " INTEGER,"
                + STORE_NO1 + " INTEGER,"
                + VOUCHER_NO1 + " TEXT,"
                + VOUCHER_SERIAL1 + " TEXT,"
                + ITEM_BARCODE1 + " TEXT,"
                + ITEM_NAME1 + " TEXT,"
                + SECONDARY_NAME1 + " TEXT,"
                + KITCHEN_ALIAS1 + " TEXT,"
                + ITEM_CATEGORY1 + " TEXT,"
                + ITEM_FAMILY1 + " TEXT,"
                + QTY1 + " INTEGER,"
                + PRICE1 + " INTEGER,"
                + TOTAL1 + " INTEGER,"
                + DISCOUNT1 + " INTEGER,"
                + L_DISCOUNT1 + " INTEGER,"
                + TOTAL_DISCOUNT1 + " INTEGER,"
                + TAX_VLUE1 + " INTEGER,"
                + TAX_PERC1 + " INTEGER,"
                + TAX_KIND1 + " INTEGER,"
                + SERVICE1 + " INTEGER,"
                + SERVICE_TAX1 + " INTEGER,"
                + TABLE_NO1 + " INTEGER,"
                + USER_NO1 + " INTEGER,"
                + USER_NAME1 + " TEXT,"
                + SECTION_NO1 + " INTEGER,"
                + SHIFT_NO1 + " INTEGER,"
                + SHIFT_NAME1 + " INTEGER,"
                + TIME1 + " TEXT,"
                + ORG_NO1 + " TEXT,"
                + ORG_POS1 + " INTEGER,"
                + RETURN_QTY1 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_ORDER_TRANSACTIONS);

        //_______________________________________________________________________________

        String CREATE_TABLE_ORDER_TRANSACTIONS_TEMP = "CREATE TABLE " + ORDER_TRANSACTIONS_TEMP + "("
                + ORDER_TYPE1 + " INTEGER,"
                + ORDER_KIND1 + " INTEGER,"
                + VOUCHER_DATE1 + " TEXT,"
                + POS_NO1 + " INTEGER,"
                + STORE_NO1 + " INTEGER,"
                + VOUCHER_NO1 + " TEXT,"
                + VOUCHER_SERIAL1 + " TEXT,"
                + ITEM_BARCODE1 + " TEXT,"
                + ITEM_NAME1 + " TEXT,"
                + SECONDARY_NAME1 + " TEXT,"
                + KITCHEN_ALIAS1 + " TEXT,"
                + ITEM_CATEGORY1 + " TEXT,"
                + ITEM_FAMILY1 + " TEXT,"
                + QTY1 + " INTEGER,"
                + PRICE1 + " INTEGER,"
                + TOTAL1 + " INTEGER,"
                + DISCOUNT1 + " INTEGER,"
                + L_DISCOUNT1 + " INTEGER,"
                + TOTAL_DISCOUNT1 + " INTEGER,"
                + TAX_VLUE1 + " INTEGER,"
                + TAX_PERC1 + " INTEGER,"
                + TAX_KIND1 + " INTEGER,"
                + SERVICE1 + " INTEGER,"
                + SERVICE_TAX1 + " INTEGER,"
                + TABLE_NO1 + " INTEGER,"
                + USER_NO1 + " INTEGER,"
                + USER_NAME1 + " TEXT,"
                + SECTION_NO1 + " INTEGER,"
                + SHIFT_NO1 + " INTEGER,"
                + SHIFT_NAME1 + " INTEGER,"
                + TIME1 + " TEXT,"
                + ORG_NO1 + " TEXT,"
                + ORG_POS1 + " INTEGER,"
                + RETURN_QTY1 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_ORDER_TRANSACTIONS_TEMP);

        //___________________________________________________________________________________

        String CREATE_TABLE_PAYMETHOD = "CREATE TABLE " + PAY_METHOD + "("
                + ORDER_TYPE + " INTEGER ,"
                + ORDER_KIND + " INTEGER,"
                + VOUCHER_DATE + " TEXT,"
                + POINT_OF_SALE_NUMBER + " INTEGER,"
                + STORE_NUMBER + " INTEGER,"
                + VOUCHER_NUMBER + " INTEGER,"
                + VOUCHER_SERIAL + " INTEGER,"
                + PAY_TYPE + " TEXT,"
                + PAY_VALUE + " INTEGER,"
                + PAY_NUMBER + " INTEGER,"
                + PAY_NAME + " TEXT ,"
                + USER_NAME14 + " TEXT,"
                + USER_NO14 + " INTEGER ,"
                + SHIFT_NAME + " TEXT ,"
                + SHIFT_NO + " INTEGER ,"
                + TIME14 + " TEXT ,"
                + ORG_NO14 + " TEXT ,"
                + ORG_POS14 + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_PAYMETHOD);


        //_______________________________________________________________________________________
        String CREATE_TABLE_ORDER_HEADER = "CREATE TABLE " + ORDER_HEADER + "("
                + ORDER_TYPE2 + " INTEGER ,"
                + ORDER_KIND2 + " INTEGER,"
                + VOUCHER_DATE2 + " TEXT,"
                + POINT_OF_SALE_NUMBER2 + " INTEGER,"
                + STORE_NUMBER2 + " INTEGER,"
                + VOUCHER_NUMBER2 + " TEXT,"
                + VOUCHER_SERIAL2 + " INTEGER,"
                + TOTAL2 + " INTEGER,"
                + TOTAL_DISCOUNT2 + " INTEGER,"
                + TOTAL_LINE_DISCOUNT2 + " INTEGER,"
                + ALL_DISCOUNT2 + " INTEGER ,"
                + TOTAL_SERVICES2 + " INTEGER ,"
                + TOTAL_TAX2 + " INTEGER ,"
                + TOTAL_SERVICES_TAX2 + " INTEGER ,"
                + SUB_TOTAL2 + " INTEGER ,"
                + AMOUNT_DUE2 + " INTEGER ,"
                + DELIVERY_CHARGE2 + " INTEGER ,"
                + TABLE_NUMBER2 + " INTEGER ,"
                + SECTION_NUMBER2 + " INTEGER ,"
                + CASH_VALUE2 + " INTEGER ,"
                + CARDS_VALUE2 + " INTEGER ,"
                + CHEQUE_VALUE2 + " INTEGER ,"
                + COUPON_VALUE2 + " INTEGER ,"
                + GIFT_VALUE2 + " INTEGER ,"
                + POINT_VALUE2 + " INTEGER ,"
                + USER_NAME2 + " TEXT ,"
                + USER_NO2 + " INTEGER ,"
                + SHIFT_NAME2 + " TEXT ,"
                + SHIFT_NO2 + " INTEGER ,"
                + WAITER2 + " TEXT ,"
                + SEATS_NUMBER2 + " INTEGER ,"
                + TIME2 + " TEXT,"
                + ORG_NO2 + " TEXT,"
                + ORG_POS2 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_ORDER_HEADER);

        //_______________________________________________________________________________________
        String CREATE_TABLE_ORDER_HEADER_TEMP = "CREATE TABLE " + ORDER_HEADER_TEMP + "("
                + ORDER_TYPE2 + " INTEGER ,"
                + ORDER_KIND2 + " INTEGER,"
                + VOUCHER_DATE2 + " TEXT,"
                + POINT_OF_SALE_NUMBER2 + " INTEGER,"
                + STORE_NUMBER2 + " INTEGER,"
                + VOUCHER_NUMBER2 + " TEXT,"
                + VOUCHER_SERIAL2 + " INTEGER,"
                + TOTAL2 + " INTEGER,"
                + TOTAL_DISCOUNT2 + " INTEGER,"
                + TOTAL_LINE_DISCOUNT2 + " INTEGER,"
                + ALL_DISCOUNT2 + " INTEGER ,"
                + TOTAL_SERVICES2 + " INTEGER ,"
                + TOTAL_TAX2 + " INTEGER ,"
                + TOTAL_SERVICES_TAX2 + " INTEGER ,"
                + SUB_TOTAL2 + " INTEGER ,"
                + AMOUNT_DUE2 + " INTEGER ,"
                + DELIVERY_CHARGE2 + " INTEGER ,"
                + TABLE_NUMBER2 + " INTEGER ,"
                + SECTION_NUMBER2 + " INTEGER ,"
                + CASH_VALUE2 + " INTEGER ,"
                + CARDS_VALUE2 + " INTEGER ,"
                + CHEQUE_VALUE2 + " INTEGER ,"
                + COUPON_VALUE2 + " INTEGER ,"
                + GIFT_VALUE2 + " INTEGER ,"
                + POINT_VALUE2 + " INTEGER ,"
                + USER_NAME2 + " TEXT ,"
                + USER_NO2 + " INTEGER ,"
                + SHIFT_NAME2 + " TEXT ,"
                + SHIFT_NO2 + " INTEGER ,"
                + WAITER2 + " TEXT ,"
                + SEATS_NUMBER2 + " INTEGER ,"
                + TIME2 + " TEXT,"
                + ORG_NO2 + " TEXT,"
                + ORG_POS2 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_ORDER_HEADER_TEMP);

        //_______________________________________________________________________________
        String CREATE_TABLE_FORCE_QUESTIONS = "CREATE TABLE " + FORCE_QUESTIONS + "("
                + QUESTION_NO + " INTEGER ,"
                + QUESTION_TEXT + " TEXT,"
                + MULTIPLE_ANSWER + " INTEGER,"
                + ANSWER + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_FORCE_QUESTIONS);

        //__________________________________________________________________________________

        String CREATE_TABLE_MODIFIER = "CREATE TABLE " + MODIFIER + " ("
                + MODIFIER_NO1 + " INTEGER ,"
                + MODIFIER_NAME1 + " TEXT ,"
                + ACTIVE1 + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_MODIFIER);

        //___________________________________________________________________________________
        String CREATE_TABLE_CATEGORY_WITH_MODIFIER = "CREATE TABLE " + CATEGORY_WITH_MODIFIER + " ("
                + MODIFIER_NAME2 + " TEXT ,"
                + CATEGORY2 + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_CATEGORY_WITH_MODIFIER);

        //_______________________________________________________________________________
        String CREATE_TABLE_ITEM_WITH_MODIFIER = "CREATE TABLE " + ITEM_WITH_MODIFIER + "("
                + ITEM_CODE + " INTEGER ,"
                + MODIFIER_NO + " TEXT,"
                + MODIFIER_TEXT + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_ITEM_WITH_MODIFIER);

        //_______________________________________________________________________________
        String CREATE_TABLE_ITEM_WITH_SCREEN = "CREATE TABLE " + ITEM_WITH_SCREEN + "("
                + ITEM_CODE3 + " INTEGER ,"
                + ITEM_NAME3 + " TEXT,"
                + SCREEN_NO3 + " INTEGER,"
                + SCREEN_NAME3 + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_ITEM_WITH_SCREEN);

        //_______________________________________________________________________________
        String CREATE_TABLE_ITEM_WITH_FQ = "CREATE TABLE " + ITEM_WITH_FQ + "("
                + ITEM_CODE2 + " INTEGER ,"
                + QUESTION_NO2 + " TEXT,"
                + QUESTION_TEXT2 + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_ITEM_WITH_FQ);

        //___________________________________________________________________________________
        String CREATE_TABLE_CUSTOMER_PAYMENT = "CREATE TABLE " + CUSTOMER_PAYMENT + "("
                + POINT_OF_SALE_NUMBER3 + " INTEGER ,"
                + USER_NO3 + " INTEGER,"
                + USER_NAME3 + " TEXT,"
                + CUSTOMER_NO3 + " INTEGER,"
                + CUSTOMER_NAME3 + " TEXT,"
                + CUSTOMER_BALANCE3 + " DOUBLE,"
                + TRANS_NO3 + " INTEGER,"
                + TRANS_DATE3 + " TEXT,"
                + PAYMENT_TYPE3 + " TEXT,"
                + SHIFT_NUMBER3 + " INTEGER,"
                + SHIFT_NAME3 + " TEXT,"
                + VALUE3 + " DOUBLE " + ")";
        db.execSQL(CREATE_TABLE_CUSTOMER_PAYMENT);
        //_____________________________________________________________________________________

        String CREATE_TABLE_CLOCK_IN_CLOCK_OUT = "CREATE TABLE " + CLOCK_IN_CLOCK_OUT + "("
                + POINT_OF_SALE_NUMBER4 + " INTEGER ,"
                + DATE4 + " TEXT,"
                + USER_NO4 + " INTEGER,"
                + USER_NAME4 + " TEXT,"
                + TRANS_TYPE4 + " TEXT,"
                + DATE_CARD4 + " TEXT,"
                + TIME_CARD4 + " TEXT,"
                + REMARK4 + " TEXT,"
                + SHIFT_NUMBER4 + " INTEGER,"
                + SHIFT_NAME4 + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_CLOCK_IN_CLOCK_OUT);
        //___________________________________________________________________________________

        String CREATE_JOB_GROUP_TABLES = "CREATE TABLE " + JOB_GROUP_TABLE + "("
                + JOB_GROUP5 + " TEXT,"
                + USER_NAME5 + " TEXT,"
                + USER_NO5 + " INTEGER,"
                + IN_DATE5 + " TEXT,"
                + ACTIVE5 + " INTEGER,"
                + SHIFT_NO5 + " INTEGER,"
                + SHIFT_NAME5 + " TEXT" + ")";
        db.execSQL(CREATE_JOB_GROUP_TABLES);
        //___________________________________________________________________________________

        String CREATE_MEMBER_SHIP_MANAGEMENT_GROUP_TABLES = "CREATE TABLE " + MEMBER_SHIP_GROUP_MANAGEMENT_TABLE + "("
                + MEMBER_SHIP_GROUP + " TEXT,"
                + USER_NAME6 + " TEXT,"
                + USER_NO6 + " INTEGER,"
                + IN_DATE6 + " TEXT,"
                + ACTIVE6 + " INTEGER,"
                + SHIFT_NO6 + " INTEGER,"
                + SHIFT_NAME6 + " TEXT" + ")";
        db.execSQL(CREATE_MEMBER_SHIP_MANAGEMENT_GROUP_TABLES);

        //___________________________________________________________________________________

        String CREATE_TABLE_SHIFT_REGISTRATION = "CREATE TABLE " + SHIFT_REGISTRATION + "("
                + SHIFT_NO9 + " INTEGER,"
                + SHIFT_NAME9 + " TEXT,"
                + FROM_TIME9 + " TEXT,"
                + TO_TIME9 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_SHIFT_REGISTRATION);

        //___________________________________________________________________________________

        String CREATE_TABLE_BLIND_SHIFT_IN = "CREATE TABLE " + BLIND_SHIFT_IN + "("
                + DATE10 + " TEXT,"
                + TIME10 + " TEXT,"
                + POS_NO10 + " INTEGER,"
                + SHIFT_NO10 + " INTEGER,"
                + SHIFT_NAME10 + " TEXT,"
                + USER_NO10 + " INTEGER,"
                + USER_NAME10 + " TEXT,"
                + STATUS10 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_BLIND_SHIFT_IN);

        //___________________________________________________________________________________

        String CREATE_TABLE_BLIND_CLOSE = "CREATE TABLE " + BLIND_CLOSE + "("
                + TRANS_NO11 + " INTEGER,"
                + DATE11 + " TEXT,"
                + TIME11 + " TEXT,"
                + POS_NO11 + " INTEGER,"
                + SHIFT_NO11 + " INTEGER,"
                + SHIFT_NAME11 + " TEXT,"
                + USER_NO11 + " INTEGER,"
                + USER_NAME11 + " TEXT,"
                + SYS_SALES11 + " INTEGER,"
                + USER_SALES11 + " TEXT,"
                + SALES_DIFF11 + " INTEGER,"
                + SYS_CASH11 + " INTEGER,"
                + USER_CASH11 + " INTEGER,"
                + CASH_DIFF11 + " INTEGER,"
                + SYS_OTHER_PAYMENTS11 + " INTEGER,"
                + USER_OTHER_PAYMENTS11 + " INTEGER,"
                + OTHER_PAYMENTS_DIFF11 + " INTEGER,"
                + TILL_OK11 + " INTEGER,"
                + TRANS_TYPE11 + " INTEGER,"
                + REASON11 + " TEXT,"
                + TO_USER11 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_BLIND_CLOSE);

        //___________________________________________________________________________________

        String CREATE_TABLE_BLIND_CLOSE_DETAILS = "CREATE TABLE " + BLIND_CLOSE_DETAILS + "("
                + TRANS_NO12 + " INTEGER,"
                + DATE12 + " TEXT,"
                + TIME12 + " TEXT,"
                + POS_NO12 + " INTEGER,"
                + SHIFT_NO12 + " INTEGER,"
                + SHIFT_NAME12 + " TEXT,"
                + USER_NO12 + " INTEGER,"
                + USER_NAME12 + " TEXT,"
                + CAT_NAME12 + " TEXT,"
                + CAT_QTY12 + " INTEGER,"
                + CAT_VALUE12 + " INTEGER,"
                + CAT_TOTAL12 + " INTEGER,"
                + TYPE12 + " TEXT,"
                + UPDATE_DATE12 + " TEXT,"
                + UPDATE_TIME12 + " TEXT,"
                + UPDATE_USER_NO12 + " INTEGER,"
                + UPDATE_USER_NAME12 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_BLIND_CLOSE_DETAILS);

        //___________________________________________________________________________________

        String CREATE_TABLE_EMPLOYEE_REGISTRATION_TABLE = "CREATE TABLE " + EMPLOYEE_REGISTRATION_TABLE + "("
                + JOB_GROUP7 + " TEXT ,"
                + EMPLOYEE_NAME7 + " TEXT ,"
                + EMPLOYEE_NO7 + " INTEGER,"
                + MOBILE_NO7 + " INTEGER,"
                + SECURITY_LEVEL7 + " INTEGER,"
                + USER_PASSWORD7 + " INTEGER,"
                + ACTIVE7 + " TEXT,"
                + HIRE_DATA7 + " INTEGER,"
                + TERMINATION_DATE7 + " INTEGER,"
                + PAY_BASIC7 + " INTEGER,"
                + PAY_RATE7 + " INTEGER,"
                + HOLIDAY_PAY7 + " INTEGER ,"
                + EMPLOYEE_TYPE7 + " INTEGER ,"
                + SHIFT_NAME7 + " TEXT ,"
                + SHIFT_NO7 + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_EMPLOYEE_REGISTRATION_TABLE);

        //___________________________________________________________________________________

        String CREATE_TABLE_CUSTOMER_REGISTRATION_TABLE = "CREATE TABLE " + CUSTOMER_REGISTRATION_TABLE + "("
                + MEMBER_SHIP_GROUP8 + " TEXT ,"
                + CUSTOMER_NAME8 + " TEXT,"
                + CUSTOMER_CODE8 + " TEXT,"
                + MEMBER_SHIP_CARD8 + " TEXT,"
                + GENDER8 + " TEXT,"
                + REMARK8 + " TEXT,"
                + STREET_NO_NAME8 + " TEXT,"
                + CITY8 + " TEXT,"
                + PHONE_NO8 + " INTEGER,"
                + MOBILE_NO8 + " INTEGER,"
                + NAME_SHOW8 + " TEXT ,"
                + BIRTHDAY8 + " TEXT ,"
                + ANNIVERSARY8 + " TEXT ,"
                + OCCUPATION8 + " TEXT ,"
                + EMAIL8 + " TEXT ,"
                + TOTAL_POINT8 + " INTEGER ,"
                + REDEEMED_POINT8 + " INTEGER ,"
                + REMAINING_POINT8 + " INTEGER ,"
                + SHIFT_NAME8 + " TEXT ,"
                + SHIFT_NO8 + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_CUSTOMER_REGISTRATION_TABLE);


        //___________________________________________________________________________________

        String CREATE_TABLE_FAMILY_CATEGORY_TABLE = "CREATE TABLE " + FAMILY_CATEGORY_TABLE + "("
                + SERIAL2 + " INTEGER ,"
                + TYPE2 + " INTEGER ,"
                + NAME_CATEGORY_FAMILY2 + " TEXT ,"
                + CATEGORY_PIC2 + " BLOB " + ")";
        db.execSQL(CREATE_TABLE_FAMILY_CATEGORY_TABLE);

        //___________________________________________________________________________________

        String CREATE_TABLE_VOID_REASONS = "CREATE TABLE " + VOID_REASONS + "("
                + SHIFT_NO15 + " INTEGER,"
                + SHIFT_NAME15 + " TEXT,"
                + USER_NUMBER15 + " INTEGER,"
                + USER_NAME15 + " TEXT,"
                + VOID_REASON15 + " TEXT,"
                + DATE15 + " TEXT,"
                + ACTIVEATED15 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_VOID_REASONS);


        //_____________________________________________________________________

        String CREATE_TABLE_CANCLE_ORDER_TABLE = "CREATE TABLE " + CANCEL_ORDER + "("
                + ORDER_NO13 + " TEXT ,"
                + TRANCE_DATE13 + " TEXT,"
                + USER_NO13 + " INTEGER,"
                + USER_NAME13 + " TEXT,"
                + SHIFT_NAME13 + " TEXT,"
                + SHIFT_NO13 + " INTEGER,"
                + WAITER_NAME13 + " TEXT,"
                + WAITER_NO13 + " INTEGER,"
                + ITEM_CODE13 + " INTEGER,"
                + ITEM_NAME13 + " TEXT,"
                + QTY13 + " INTEGER ,"
                + PRICE13 + " INTEGER ,"
                + TOTAL13 + " INTEGER ,"
                + REASON13 + " TEXT ,"
                + IS_ALL_CANCEL13 + " INTEGER ,"
                + TIME13 + " TEXT ,"
                + POS_NO13 + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_CANCLE_ORDER_TABLE);

        //_____________________________________________________________________

        String CREATE_TABLE_TABLE_ACTIONS = "CREATE TABLE " + TABLE_ACTIONS + "("
                + POS_NO16 + " INTEGER,"
                + USER_NO16 + " INTEGER,"
                + USER_NAME16 + " TEXT,"
                + SHIFT_NAME16 + " TEXT,"
                + SHIFT_NO16 + " INTEGER,"
                + ACTION_TYPE16 + " INTEGER,"
                + ACTION_DATE16 + " TEXT,"
                + ACTION_TIME16 + " TEXT,"
                + TABLE_NO16 + " INTEGER,"
                + SECTION_NO16 + " INTEGER ,"
                + TO_TABLE16 + " INTEGER ,"
                + TO_SECTION16 + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_TABLE_ACTIONS);


        //_____________________________________________________________________

        String CREATE_TABLE_Z_REPORT_TABLE = "CREATE TABLE " + Z_REPORT_TABLE + "("
                + DATE17 + " TEXT,"
                + USER_NO17 + " INTEGER,"
                + USER_NAME17 + " TEXT,"
                + POS_NO17 + " INTEGER,"
                + SERIAL17 + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_Z_REPORT_TABLE);

        //_____________________________________________________________________

        String CREATE_TABLE_ANNOUNCEMENT_TABLE = "CREATE TABLE " + ANNOUNCEMENT_TABLE + "("
                + SHIFT_NAME18 + " TEXT,"
                + ANNOUNCEMENT_DATE18 + " TEXT,"
                + USER_NAME18 + " TEXT,"
                + POS_NO18 + " INTEGER,"
                + MESSAGE18 + " TEXT,"
                + IS_SHOW18 + " INTEGER,"
                + USER_NO18 + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_ANNOUNCEMENT_TABLE);

        //_____________________________________________________________________

        String CREATE_TABLE_KITCHEN_SCREEN_TABLE = "CREATE TABLE " + KITCHEN_SCREEN_TABLE + "("
                + KITCHEN_NAME + " TEXT,"
                + KITCHEN_NO + " INTEGER,"
                + KITCHEN_IP + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_KITCHEN_SCREEN_TABLE);

    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


//        db.execSQL("ALTER TABLE ANNOUNCEMENT_TABLE ADD USER_NO INTEGER NOT NULL DEFAULT '-1'");

//        db.execSQL("ALTER TABLE ORDER_TRANSACTIONS ADD ORG_NO TAXE NOT NULL DEFAULT '0'");
//        db.execSQL("ALTER TABLE ORDER_TRANSACTIONS ADD ORG_POS INTEGER NOT NULL DEFAULT '-1'");
//        db.execSQL("ALTER TABLE ORDER_TRANSACTIONS ADD RETURN_QTY INTEGER NOT NULL DEFAULT '0'");
//
//        db.execSQL("ALTER TABLE ORDER_HEADER ADD ORG_NO TAXE NOT NULL DEFAULT '0'");
//        db.execSQL("ALTER TABLE ORDER_HEADER ADD ORG_POS INTEGER NOT NULL DEFAULT '-1'");
//
//        db.execSQL("ALTER TABLE PAY_METHOD ADD ORG_NO TAXE NOT NULL DEFAULT '0'");
//        db.execSQL("ALTER TABLE PAY_METHOD ADD ORG_POS INTEGER NOT NULL DEFAULT '-1'");
////++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
//        db.execSQL("ALTER TABLE ORDER_TRANSACTIONS_TEMP ADD ORG_NO TAXE NOT NULL DEFAULT '0'");
//        db.execSQL("ALTER TABLE ORDER_TRANSACTIONS_TEMP ADD ORG_POS INTEGER NOT NULL DEFAULT '-1'");
//        db.execSQL("ALTER TABLE ORDER_TRANSACTIONS_TEMP ADD RETURN_QTY INTEGER NOT NULL DEFAULT '0'");
//
//        db.execSQL("ALTER TABLE ORDER_HEADER_TEMP ADD ORG_NO TAXE NOT NULL DEFAULT '0'");
//        db.execSQL("ALTER TABLE ORDER_HEADER_TEMP ADD ORG_POS INTEGER NOT NULL DEFAULT '-1'");


    }

    //Insert values to the table Items

    public void addMainSettings(){
//        db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);
//        values.put(MAIN_SETTINGS_PASSWORD, Settings.user_name);
//        values.put(MAIN_SETTINGS_USER_NO, Settings.user_name);
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);
//        values.put(MAIN_SETTINGS_USERNAME, Settings.user_name);


    }

    public void addItem(Items items) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        byte[] byteImage = {};
        if (items.getPic() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            items.getPic().compress(Bitmap.CompressFormat.PNG, 0, stream);
            byteImage = stream.toByteArray();
        }
        values.put(MENU_CATEGORY, items.getMenuCategory());
        values.put(MENU_NAME, items.getMenuName());
        values.put(FAMILY_NAME, items.getFamilyName());
        values.put(PRICE, items.getPrice());
        values.put(TAX_TYPE, items.getTaxType());
        values.put(TAX_PERCENT, items.getTax());
        values.put(SECONDARY_NAME, items.getSecondaryName());
        values.put(KITCHEN_ALIAS, items.getKitchenAlias());
        values.put(ITEM_BARCODE, items.getItemBarcode());
        values.put(STATUS, items.getStatus());
        values.put(ITEM_TYPE, items.getItemType());
        values.put(DESCRIPTION, items.getDescription());
        values.put(INVENTORY_UNIT, items.getInventoryUnit());
        values.put(WASTAGE_PERCENT, items.getWastagePercent());
        values.put(DISCOUNT_AVAILABLE, items.getDiscountAvailable());
        values.put(POINT_AVAILABLE, items.getPointAvailable());
        values.put(OPEN_PRICE, items.getOpenPrice());
        values.put(KITCHEN_PRINTER_TO_USE, items.getKitchenPrinter());
        values.put(USED, items.getUsed());
        values.put(SHOW_IN_MENU, items.getShowInMenu());
        values.put(ITEM_PICTURE, byteImage);

        db.insert(ITEMS, null, values);
        db.close();
    }

    public void addRecipe(Recipes recipe) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(BARCODE, recipe.getBarcode());
        values.put(ITEM, recipe.getItem());
        values.put(UNIT, recipe.getUnit());
        values.put(QTY, recipe.getQty());
        values.put(COST, recipe.getCost());

        db.insert(RECIPES, null, values);
        db.close();
    }

    public void addUsedCategory(UsedCategories category) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_NAME, category.getCategoryName());
        values.put(NUMBER_OF_ITEMS, category.getNumberOfItems());
        values.put(CATEGORY_BACKGROUND, category.getBackground());
        values.put(CATEGORY_TEXT_COLOR, category.getTextColor());
        values.put(CATEGORY_POSITION, category.getPosition());

        db.insert(USED_CATEGORIES, null, values);
        db.close();
    }

    public void addUsedItems(UsedItems item) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_NAME2, item.getCategoryName());
        values.put(ITEM_NAME, item.getitemName());
        values.put(ITEM_BACKGROUND, item.getBackground());
        values.put(ITEM_TEXT_COLOR, item.getTextColor());
        values.put(ITEM_POSITION, item.getPosition());

        db.insert(USED_ITEMS, null, values);
        db.close();
    }

    public void addTables(ArrayList<Tables> tables, int floor) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < tables.size(); i++) {
            values.put(HEIGHT, tables.get(i).getHeight());
            values.put(WIDTH, tables.get(i).getWidth());
            values.put(IMAGE_RESOURCE, tables.get(i).getImageResource());
            values.put(MARGIN_LEFT, tables.get(i).getMarginLeft());
            values.put(MARGIN_TOP, tables.get(i).getMarginTop());
            values.put(FLOOR, floor);
            values.put(TABLE_NUMBER, tables.get(i).getTableNumber());

            db.insert(TABLES, null, values);
        }
        db.close();
    }

    public void addMoneyCategory(ArrayList<Money> money) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        byte[] byteImage = {};
        for (int i = 0; i < money.size(); i++) {

            if (money.get(i).getPicture() != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                money.get(i).getPicture().compress(Bitmap.CompressFormat.PNG, 0, stream);
                byteImage = stream.toByteArray();
            }

            values.put(SERIAL1, money.get(i).getSerial());
            values.put(CATEGORY_NAME1, money.get(i).getCatName());
            values.put(CATEGORY_VALUE1, money.get(i).getCatValue());
            values.put(SHOW1, money.get(i).getShow());
            values.put(PICTURE1, byteImage);

            db.insert(MONEY_CATEGORIES, null, values);
        }
        db.close();
    }

    public void addCashierInOut(ArrayList<Cashier> cashier) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < cashier.size(); i++) {

            values.put(CASHIER_NAME3, cashier.get(i).getCashierName());
            values.put(DATE3, cashier.get(i).getCheckInDate());
            values.put(CATEGORY_NAME3, cashier.get(i).getCategoryName());
            values.put(CATEGORY_VALUE3, cashier.get(i).getCategoryValue());
            values.put(CATEGORY_QTY3, cashier.get(i).getCategoryQty());
            values.put(ORDER_KIND3, cashier.get(i).getOrderKind());

            db.insert(CASHIER_IN_OUT, null, values);
        }
        db.close();
    }
    public ArrayList<Cashier> getCacher()
    {
        ArrayList<Cashier> allCasher=new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CASHIER_IN_OUT ;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
               Cashier cash=new Cashier();
               cash.setCashierName(cursor.getString(0));
               cash.setCheckInDate(cursor.getString(1));
               cash.setCategoryName(cursor.getString(2));
               cash.setCategoryValue(Double.parseDouble(cursor.getString(3)));
                cash.setCategoryQty(Integer.parseInt(cursor.getString(4)));
                cash.setOrderKind(Integer.parseInt(cursor.getString(5)));



                allCasher.add(cash);

            } while (cursor.moveToNext());
        }
        return allCasher;
            }

    public void addBlindClose(BlindClose obj) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRANS_NO11, obj.getTransNo());
        values.put(DATE11, obj.getDate());
        values.put(TIME11, obj.getTime());
        values.put(POS_NO11, obj.getPOSNo());
        values.put(SHIFT_NO11, obj.getShiftNo());
        values.put(SHIFT_NAME11, obj.getShiftName());
        values.put(USER_NO11, obj.getUserNo());
        values.put(USER_NAME11, obj.getUserName());
        values.put(SYS_SALES11, obj.getSysSales());
        values.put(USER_SALES11, obj.getUserSales());
        values.put(SALES_DIFF11, obj.getSalesDiff());
        values.put(SYS_CASH11, obj.getSysCash());
        values.put(USER_CASH11, obj.getUserCash());
        values.put(CASH_DIFF11, obj.getCashDiff());
        values.put(SYS_OTHER_PAYMENTS11, obj.getSysOthers());
        values.put(USER_OTHER_PAYMENTS11, obj.getUserOthers());
        values.put(OTHER_PAYMENTS_DIFF11, obj.getOthersDiff());
        values.put(TILL_OK11, obj.getTillOk());
        values.put(TRANS_TYPE11, obj.getTransType());
        values.put(REASON11, obj.getReason());
        values.put(TO_USER11, obj.getToUser());

        db.insert(BLIND_CLOSE, null, values);

        db.close();
    }

    public void addBlindCloseDetails(BlindCloseDetails obj) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRANS_NO12, obj.getTransNo());
        values.put(DATE12, obj.getDate());
        values.put(TIME12, obj.getTime());
        values.put(POS_NO12, obj.getPOSNo());
        values.put(SHIFT_NO12, obj.getShiftNo());
        values.put(SHIFT_NAME12, obj.getShiftName());
        values.put(USER_NO12, obj.getUserNo());
        values.put(USER_NAME12, obj.getUserName());
        values.put(CAT_NAME12, obj.getCatName());
        values.put(CAT_QTY12, obj.getCatQty());
        values.put(CAT_VALUE12, obj.getCatValue());
        values.put(CAT_TOTAL12, obj.getCatTotal());
        values.put(TYPE12, obj.getType());
        values.put(UPDATE_DATE12, obj.getUpdateDate());
        values.put(UPDATE_TIME12, obj.getUpdateTime());
        values.put(UPDATE_USER_NO12, obj.getUpdateUserNo());
        values.put(UPDATE_USER_NAME12, obj.getUpdateUserName());

        db.insert(BLIND_CLOSE_DETAILS, null, values);

        db.close();
    }

    public void addPayInOut(Pay pay) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRANS_TYPE, pay.getTransType());
        values.put(POS_NO, pay.getPosNo());
        values.put(USER_NO, pay.getUserNo());
        values.put(USER_NAME, pay.getUserName());
        values.put(TRANS_DATE, pay.getTransDate());
        values.put(VALUE, pay.getValue());
        values.put(REMARK, pay.getRemark());
        values.put(SHEFT_NO, pay.getShiftNo());
        values.put(SHEFT_NAME, pay.getShiftName());
        values.put(TIME, pay.getTime());
        db.insert(PAY_IN_OUT, null, values);

        db.close();
    }

    public void addCreditCard(CreditCard card) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERIAL, card.getSerial());
        values.put(CARD_NAME, card.getCardName());
        values.put(ACC_CODE, card.getAccCode());

        db.insert(CREDIT_CARDS, null, values);

        db.close();
    }

    public void addCheque(Cheque cheeks) {

        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERIAL_CHEEK, cheeks.getSerialCheque());
        values.put(BANK_NAME, cheeks.getBankName());
        values.put(CHEQUE_NUMBER, cheeks.getChequeNumber());
        values.put(RECIVIDE, cheeks.getReceived());

        db.insert(CHEQUES, null, values);
        db.close();
    }

    public void addOrderTransaction(OrderTransactions items) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ORDER_TYPE1, items.getOrderType());
        values.put(ORDER_KIND1, items.getOrderKind());
        values.put(VOUCHER_DATE1, items.getVoucherDate());
        values.put(POS_NO1, items.getPosNo());
        values.put(STORE_NO1, items.getStoreNo());
        values.put(VOUCHER_NO1, items.getVoucherNo());
        values.put(VOUCHER_SERIAL1, items.getVoucherSerial());
        values.put(ITEM_BARCODE1, items.getItemBarcode());
        values.put(ITEM_NAME1, items.getItemName());
        values.put(SECONDARY_NAME1, items.getSecondaryName());
        values.put(KITCHEN_ALIAS1, items.getKitchenAlias());
        values.put(QTY1, items.getQty());
        values.put(PRICE1, items.getPrice());
        values.put(TOTAL1, items.getTotal());
        values.put(DISCOUNT1, items.getDiscount());
        values.put(L_DISCOUNT1, items.getlDiscount());
        values.put(TOTAL_DISCOUNT1, items.getTotalDiscount());
        values.put(TAX_VLUE1, items.getTaxValue());
        values.put(TAX_PERC1, items.getTaxPerc());
        values.put(TAX_KIND1, items.getTaxKind());
        values.put(SERVICE1, items.getService());
        values.put(SERVICE_TAX1, items.getServiceTax());
        values.put(ITEM_CATEGORY1, items.getItemCategory());
        values.put(ITEM_FAMILY1, items.getItemFamily());
        values.put(TABLE_NO1, items.getTableNo());
        values.put(SECTION_NO1, items.getSectionNo());
        values.put(SHIFT_NO1, items.getShiftNo());
        values.put(SHIFT_NAME1, items.getShiftName());
        values.put(USER_NAME1, items.getUserName());
        values.put(USER_NO1, items.getUserNo());
        values.put(TIME1, items.getTime());
        values.put(ORG_NO1, items.getOrgNo());
        values.put(ORG_POS1, items.getOrgPos());
        values.put(RETURN_QTY1, items.getReturnQty());

        db.insert(ORDER_TRANSACTIONS, null, values);
        db.close();
    }

    public void addOrderTransactionTemp(OrderTransactions items) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ORDER_TYPE1, items.getOrderType());
        values.put(ORDER_KIND1, items.getOrderKind());
        values.put(VOUCHER_DATE1, items.getVoucherDate());
        values.put(POS_NO1, items.getPosNo());
        values.put(STORE_NO1, items.getStoreNo());
        values.put(VOUCHER_NO1, items.getVoucherNo());
        values.put(VOUCHER_SERIAL1, items.getVoucherSerial());
        values.put(ITEM_BARCODE1, items.getItemBarcode());
        values.put(ITEM_NAME1, items.getItemName());
        values.put(SECONDARY_NAME1, items.getSecondaryName());
        values.put(KITCHEN_ALIAS1, items.getKitchenAlias());
        values.put(QTY1, items.getQty());
        values.put(PRICE1, items.getPrice());
        values.put(TOTAL1, items.getTotal());
        values.put(DISCOUNT1, items.getDiscount());
        values.put(L_DISCOUNT1, items.getlDiscount());
        values.put(TOTAL_DISCOUNT1, items.getTotalDiscount());
        values.put(TAX_VLUE1, items.getTaxValue());
        values.put(TAX_PERC1, items.getTaxPerc());
        values.put(TAX_KIND1, items.getTaxKind());
        values.put(SERVICE1, items.getService());
        values.put(SERVICE_TAX1, items.getServiceTax());
        values.put(ITEM_CATEGORY1, items.getItemCategory());
        values.put(ITEM_FAMILY1, items.getItemFamily());
        values.put(TABLE_NO1, items.getTableNo());
        values.put(SECTION_NO1, items.getSectionNo());
        values.put(USER_NAME1, items.getUserName());
        values.put(USER_NO1, items.getUserNo());
        values.put(SHIFT_NO1, items.getShiftNo());
        values.put(SHIFT_NAME1, items.getShiftName());
        values.put(TIME1, items.getTime());
        values.put(ORG_NO1, items.getOrgNo());
        values.put(ORG_POS1, items.getOrgPos());
        values.put(RETURN_QTY1, items.getReturnQty());

        db.insert(ORDER_TRANSACTIONS_TEMP, null, values);
        db.close();
    }

    public void addAllPayMethodItem(PayMethod payMethod) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ORDER_TYPE, payMethod.getOrderType());
        values.put(ORDER_KIND, payMethod.getOrderKind());
        values.put(VOUCHER_DATE, payMethod.getVoucherDate());
        values.put(POINT_OF_SALE_NUMBER, payMethod.getPointOfSaleNumber());
        values.put(STORE_NUMBER, payMethod.getStoreNumber());
        values.put(VOUCHER_NUMBER, payMethod.getVoucherNumber());
        values.put(VOUCHER_SERIAL, payMethod.getVoucherSerial());
        values.put(PAY_TYPE, payMethod.getPayType());
        values.put(PAY_VALUE, payMethod.getPayValue());
        values.put(PAY_NUMBER, payMethod.getPayNumber());
        values.put(PAY_NAME, payMethod.getPayName());
        values.put(SHIFT_NAME, payMethod.getShiftName());
        values.put(SHIFT_NO, payMethod.getShiftNumber());
        values.put(USER_NAME14, payMethod.getUserName());
        values.put(USER_NO14, payMethod.getUserNo());
        values.put(TIME14, payMethod.getTime());
        values.put(ORG_NO14, payMethod.getOrgNo());
        values.put(ORG_POS14, payMethod.getOrgPos());


        db.insert(PAY_METHOD, null, values);
        db.close();

    }

    public void addOrderHeader(OrderHeader orderHeader) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ORDER_TYPE2, orderHeader.getOrderType());
        values.put(ORDER_KIND2, orderHeader.getOrderKind());
        values.put(VOUCHER_DATE2, orderHeader.getVoucherDate());
        values.put(POINT_OF_SALE_NUMBER2, orderHeader.getPointOfSaleNumber());
        values.put(STORE_NUMBER2, orderHeader.getStoreNumber());
        values.put(VOUCHER_NUMBER2, orderHeader.getVoucherNumber());
        values.put(VOUCHER_SERIAL2, orderHeader.getVoucherSerial());
        values.put(TOTAL2, orderHeader.getTotal());
        values.put(TOTAL_DISCOUNT2, orderHeader.getTotalDiscount());
        values.put(TOTAL_LINE_DISCOUNT2, orderHeader.getTotalLineDiscount());
        values.put(ALL_DISCOUNT2, orderHeader.getAllDiscount());
        values.put(TOTAL_SERVICES2, orderHeader.getTotalService());
        values.put(TOTAL_TAX2, orderHeader.getTotalTax());
        values.put(SUB_TOTAL2, orderHeader.getSubTotal());
        values.put(TOTAL_SERVICES_TAX2, orderHeader.getTotalServiceTax());
        values.put(AMOUNT_DUE2, orderHeader.getAmountDue());
        values.put(DELIVERY_CHARGE2, orderHeader.getDeliveryCharge());
        values.put(TABLE_NUMBER2, orderHeader.getTableNO());
        values.put(SECTION_NUMBER2, orderHeader.getSectionNO());
        values.put(CASH_VALUE2, orderHeader.getCashValue());
        values.put(CARDS_VALUE2, orderHeader.getCardsValue());
        values.put(CHEQUE_VALUE2, orderHeader.getChequeValue());
        values.put(COUPON_VALUE2, orderHeader.getCouponValue());
        values.put(GIFT_VALUE2, orderHeader.getGiftValue());
        values.put(POINT_VALUE2, orderHeader.getPointValue());
        values.put(USER_NAME2, orderHeader.getUserName());
        values.put(USER_NO2, orderHeader.getUserNo());
        values.put(SHIFT_NAME2, orderHeader.getShiftName());
        values.put(SHIFT_NO2, orderHeader.getShiftNumber());
        values.put(WAITER2, orderHeader.getWaiter());
        values.put(SEATS_NUMBER2, orderHeader.getSeatsNumber());
        values.put(TIME2, orderHeader.getTime());
        values.put(ORG_NO2, orderHeader.getOrgNo());
        values.put(ORG_POS2, orderHeader.getOrgPos());


        db.insert(ORDER_HEADER, null, values);
        db.close();
    }

    public void addOrderHeaderTemp(OrderHeader orderHeader) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ORDER_TYPE2, orderHeader.getOrderType());
        values.put(ORDER_KIND2, orderHeader.getOrderKind());
        values.put(VOUCHER_DATE2, orderHeader.getVoucherDate());
        values.put(POINT_OF_SALE_NUMBER2, orderHeader.getPointOfSaleNumber());
        values.put(STORE_NUMBER2, orderHeader.getStoreNumber());
        values.put(VOUCHER_NUMBER2, orderHeader.getVoucherNumber());
        values.put(VOUCHER_SERIAL2, orderHeader.getVoucherSerial());
        values.put(TOTAL2, orderHeader.getTotal());
        values.put(TOTAL_DISCOUNT2, orderHeader.getTotalDiscount());
        values.put(TOTAL_LINE_DISCOUNT2, orderHeader.getTotalLineDiscount());
        values.put(ALL_DISCOUNT2, orderHeader.getAllDiscount());
        values.put(TOTAL_SERVICES2, orderHeader.getTotalService());
        values.put(TOTAL_TAX2, orderHeader.getTotalTax());
        values.put(SUB_TOTAL2, orderHeader.getSubTotal());
        values.put(TOTAL_SERVICES_TAX2, orderHeader.getTotalServiceTax());
        values.put(AMOUNT_DUE2, orderHeader.getAmountDue());
        values.put(DELIVERY_CHARGE2, orderHeader.getDeliveryCharge());
        values.put(TABLE_NUMBER2, orderHeader.getTableNO());
        values.put(SECTION_NUMBER2, orderHeader.getSectionNO());
        values.put(CASH_VALUE2, orderHeader.getCashValue());
        values.put(CARDS_VALUE2, orderHeader.getCardsValue());
        values.put(CHEQUE_VALUE2, orderHeader.getChequeValue());
        values.put(COUPON_VALUE2, orderHeader.getCouponValue());
        values.put(GIFT_VALUE2, orderHeader.getGiftValue());
        values.put(POINT_VALUE2, orderHeader.getPointValue());
        values.put(USER_NAME2, orderHeader.getUserName());
        values.put(USER_NO2, orderHeader.getUserNo());
        values.put(SHIFT_NAME2, orderHeader.getShiftName());
        values.put(SHIFT_NO2, orderHeader.getShiftNumber());
        values.put(WAITER2, orderHeader.getWaiter());
        values.put(SEATS_NUMBER2, orderHeader.getSeatsNumber());
        values.put(TIME2, orderHeader.getTime());
        values.put(ORG_NO2, orderHeader.getOrgNo());
        values.put(ORG_POS2, orderHeader.getOrgPos());

        db.insert(ORDER_HEADER_TEMP, null, values);
        db.close();
    }

    public void addForceQuestion(ForceQuestions question) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(QUESTION_NO, question.getQuestionNo());
        values.put(QUESTION_TEXT, question.getQuestionText());
        values.put(MULTIPLE_ANSWER, question.getMultipleAnswer());
        values.put(ANSWER, question.getAnswer());

        db.insert(FORCE_QUESTIONS, null, values);
        db.close();
    }

    public void addModifierItem(Modifier modifier) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(MODIFIER_NAME1, modifier.getModifierName());
        values.put(MODIFIER_NO1, modifier.getModifierNumber());
        values.put(ACTIVE1, modifier.getModifierActive());

        db.insert(MODIFIER, null, values);
        db.close();
    }

    public void addCategoriesModifier(CategoryWithModifier categoryModifier) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(MODIFIER_NAME2, categoryModifier.getModifierName());
        values.put(CATEGORY2, categoryModifier.getCategoryName());

        db.insert(CATEGORY_WITH_MODIFIER, null, values);
        db.close();
    }

    public void addItemWithModifier(ItemWithModifier modifier) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_CODE, modifier.getItemCode());
        values.put(MODIFIER_NO, modifier.getModifierNo());
        values.put(MODIFIER_TEXT, modifier.getModifierText());

        db.insert(ITEM_WITH_MODIFIER, null, values);
        db.close();
    }

    public void addItemWithScreen(ItemWithScreen screen) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_CODE3, screen.getItemCode());
        values.put(ITEM_NAME3, screen.getItemName());
        values.put(SCREEN_NO3, screen.getScreenNo());
        values.put(SCREEN_NAME3, screen.getScreenName());

        db.insert(ITEM_WITH_SCREEN, null, values);
        db.close();
    }

    public void addItemWithFQ(ItemWithFq fq) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_CODE2, fq.getItemCode());
        values.put(QUESTION_NO2, fq.getQuestionNo());
        values.put(QUESTION_TEXT2, fq.getQuestionText());

        db.insert(ITEM_WITH_FQ, null, values);
        db.close();
    }

    public void addCustomerPayment(CustomerPayment customerPayment) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(POINT_OF_SALE_NUMBER3, customerPayment.getPointOfSaleNumber());
        values.put(USER_NO3, customerPayment.getUserNO());
        values.put(USER_NAME3, customerPayment.getUserName());
        values.put(CUSTOMER_NO3, customerPayment.getCustomerNo());
        values.put(CUSTOMER_NAME3, customerPayment.getCustomerName());
        values.put(CUSTOMER_BALANCE3, customerPayment.getCustomerBalance());
        values.put(TRANS_NO3, customerPayment.getTransNo());
        values.put(TRANS_DATE3, customerPayment.getTransDate());
        values.put(PAYMENT_TYPE3, customerPayment.getPayMentType());
        values.put(VALUE3, customerPayment.getValue());
        values.put(SHIFT_NUMBER3, customerPayment.getShiftNo());
        values.put(SHIFT_NAME3, customerPayment.getShiftName());

        db.insert(CUSTOMER_PAYMENT, null, values);
        db.close();
    }

    public void addClockInClockOut(ClockInClockOut clockInClockOut) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(POINT_OF_SALE_NUMBER4, clockInClockOut.getPointOfSaleNumber());
        values.put(DATE4, clockInClockOut.getDate());
        values.put(USER_NO4, clockInClockOut.getUserNO());
        values.put(USER_NAME4, clockInClockOut.getUserName());
        values.put(TRANS_TYPE4, clockInClockOut.getTranstype());
        values.put(DATE_CARD4, clockInClockOut.getDateCard());
        values.put(TIME_CARD4, clockInClockOut.getTimeCard());
        values.put(REMARK4, clockInClockOut.getRemark());
        values.put(SHIFT_NUMBER4, clockInClockOut.getShiftNo());
        values.put(SHIFT_NAME4, clockInClockOut.getShiftName());

        db.insert(CLOCK_IN_CLOCK_OUT, null, values);
        db.close();

    }

    public void addJobGroup(JobGroup jobGroup) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(JOB_GROUP5, jobGroup.getJobGroup());
        values.put(USER_NAME5, jobGroup.getUserName());
        values.put(USER_NO5, jobGroup.getUserNo());
        values.put(IN_DATE5, jobGroup.getInDate());
        values.put(ACTIVE5, jobGroup.getActive());
        values.put(SHIFT_NO5, jobGroup.getShiftNo());
        values.put(SHIFT_NAME5, jobGroup.getShiftName());

        db.insert(JOB_GROUP_TABLE, null, values);

        db.close();
    }

    public void addMemberShipGroup(MemberShipGroup memberShipGroup) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(MEMBER_SHIP_GROUP, memberShipGroup.getMemberShipGroup());
        values.put(USER_NAME6, memberShipGroup.getUserName());
        values.put(USER_NO6, memberShipGroup.getUserNo());
        values.put(IN_DATE6, memberShipGroup.getInDate());
        values.put(ACTIVE6, memberShipGroup.getActive());
        values.put(SHIFT_NO6, memberShipGroup.getShiftNo());
        values.put(SHIFT_NAME6, memberShipGroup.getShiftName());

        db.insert(MEMBER_SHIP_GROUP_MANAGEMENT_TABLE, null, values);

        db.close();
    }

    public void addShift(Shift shift) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(SHIFT_NO9, shift.getShiftNo());
        values.put(SHIFT_NAME9, shift.getShiftName());
        values.put(FROM_TIME9, shift.getFromTime());
        values.put(TO_TIME9, shift.getToTime());

        db.insert(SHIFT_REGISTRATION, null, values);

        db.close();
    }

    public void addBlindShiftInOut(BlindShift blindShift) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(DATE10, blindShift.getDate());
        values.put(TIME10, blindShift.getTime());
        values.put(POS_NO10, blindShift.getPosNo());
        values.put(SHIFT_NO10, blindShift.getShiftNo());
        values.put(SHIFT_NAME10, blindShift.getShiftName());
        values.put(USER_NO10, blindShift.getUserNo());
        values.put(USER_NAME10, blindShift.getUserName());
        values.put(STATUS10, blindShift.getStatus());

        db.insert(BLIND_SHIFT_IN, null, values);

        db.close();
    }

    public void addEmployeeRegistration(EmployeeRegistrationModle employeeRegistrationModle) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(JOB_GROUP7, employeeRegistrationModle.getJobGroup());
        values.put(EMPLOYEE_NAME7, employeeRegistrationModle.getEmployeeName());
        values.put(EMPLOYEE_NO7, employeeRegistrationModle.getEmployeeNO());
        values.put(MOBILE_NO7, employeeRegistrationModle.getMobileNo());
        values.put(SECURITY_LEVEL7, employeeRegistrationModle.getSecurityLevel());
        values.put(USER_PASSWORD7, employeeRegistrationModle.getUserPassword());
        values.put(ACTIVE7, employeeRegistrationModle.getActive());
        values.put(HIRE_DATA7, employeeRegistrationModle.getHireDate());
        values.put(TERMINATION_DATE7, employeeRegistrationModle.getTerminationDate());
        values.put(PAY_BASIC7, employeeRegistrationModle.getPayBasic());
        values.put(PAY_RATE7, employeeRegistrationModle.getPayRate());
        values.put(HOLIDAY_PAY7, employeeRegistrationModle.getHolidayPay());
        values.put(EMPLOYEE_TYPE7, employeeRegistrationModle.getEmployeeType());
        values.put(SHIFT_NO7, employeeRegistrationModle.getShiftNo());
        values.put(SHIFT_NAME7, employeeRegistrationModle.getShiftName());

        db.insert(EMPLOYEE_REGISTRATION_TABLE, null, values);

        db.close();
    }

    public void addCustomerRegistration(CustomerRegistrationModel customerRegistrationModel) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();


        values.put(MEMBER_SHIP_GROUP8, customerRegistrationModel.getMemberShipGroup());
        values.put(CUSTOMER_NAME8, customerRegistrationModel.getCustomerName());
        values.put(CUSTOMER_CODE8, customerRegistrationModel.getCustomerCode());
        values.put(MEMBER_SHIP_CARD8, customerRegistrationModel.getMemberShipGroup());
        values.put(GENDER8, customerRegistrationModel.getCoender());
        values.put(REMARK8, customerRegistrationModel.getRemark());
        values.put(STREET_NO_NAME8, customerRegistrationModel.getStreetNoName());
        values.put(CITY8, customerRegistrationModel.getCity());
        values.put(PHONE_NO8, customerRegistrationModel.getPhoneNo());
        values.put(MOBILE_NO8, customerRegistrationModel.getMobileNo());
        values.put(NAME_SHOW8, customerRegistrationModel.getNameShow());
        values.put(BIRTHDAY8, customerRegistrationModel.getBirthday());
        values.put(ANNIVERSARY8, customerRegistrationModel.getAnniversary());
        values.put(OCCUPATION8, customerRegistrationModel.getOccupation());
        values.put(EMAIL8, customerRegistrationModel.getEmail());
        values.put(TOTAL_POINT8, customerRegistrationModel.getTotalPoint());
        values.put(REDEEMED_POINT8, customerRegistrationModel.getRedeemedPoint());
        values.put(REMAINING_POINT8, customerRegistrationModel.getRemainingPoint());
        values.put(SHIFT_NO8, customerRegistrationModel.getShiftNO());
        values.put(SHIFT_NAME8, customerRegistrationModel.getShiftName());

        db.insert(CUSTOMER_REGISTRATION_TABLE, null, values);

        db.close();
    }

    public void addFamilyCategory(FamilyCategory familyCategory) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        byte[] byteImage = {};
        if (familyCategory.getCatPic() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            familyCategory.getCatPic().compress(Bitmap.CompressFormat.PNG, 0, stream);
            byteImage = stream.toByteArray();
        }

        values.put(SERIAL2, familyCategory.getSerial());
        values.put(TYPE2, familyCategory.getType());
        values.put(NAME_CATEGORY_FAMILY2, familyCategory.getName());
        values.put(CATEGORY_PIC2, byteImage);

        db.insert(FAMILY_CATEGORY_TABLE, null, values);

        db.close();
    }


    public void addCancleOrder(CancleOrder cancleOrder) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ORDER_NO13, cancleOrder.getOrderNo());
        values.put(TRANCE_DATE13, cancleOrder.getTransDate());
        values.put(USER_NAME13, cancleOrder.getUserName());
        values.put(USER_NO13, cancleOrder.getUserNo());
        values.put(SHIFT_NAME13, cancleOrder.getShiftName());
        values.put(SHIFT_NO13, cancleOrder.getShiftNo());
        values.put(WAITER_NAME13, cancleOrder.getWaiterName());
        values.put(WAITER_NO13, cancleOrder.getWaiterNo());
        values.put(ITEM_CODE13, cancleOrder.getItemCode());
        values.put(ITEM_NAME13, cancleOrder.getItemName());
        values.put(QTY13, cancleOrder.getQty());
        values.put(PRICE13, cancleOrder.getPrice());
        values.put(TOTAL13, cancleOrder.getTotal());
        values.put(REASON13, cancleOrder.getReason());
        values.put(IS_ALL_CANCEL13, cancleOrder.getIsAllCancel());
        values.put(TIME13, cancleOrder.getTime());
        values.put(POS_NO13, cancleOrder.getPosNO());
        db.insert(CANCEL_ORDER, null, values);

        db.close();
    }

    public void addTableAction(TableActions action) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(POS_NO16, action.getPOSNumber());
        values.put(USER_NAME16, action.getUserName());
        values.put(USER_NO16, action.getUserNo());
        values.put(SHIFT_NAME16, action.getShiftName());
        values.put(SHIFT_NO16, action.getShiftNo());
        values.put(ACTION_TYPE16, action.getActionType());
        values.put(ACTION_DATE16, action.getActionDate());
        values.put(ACTION_TIME16, action.getActionTime());
        values.put(TABLE_NO1, action.getTableNo());
        values.put(SECTION_NO1, action.getSectionNo());
        values.put(TO_TABLE16, action.getToTable());
        values.put(TO_SECTION16, action.getToSection());

        db.insert(TABLE_ACTIONS, null, values);

        db.close();
    }


    public void addAnnouncement(Announcemet announcemet) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(SHIFT_NAME18, announcemet.getShiftName());
        values.put(ANNOUNCEMENT_DATE18, announcemet.getAnnouncementDate());
        values.put(USER_NAME18, announcemet.getUserName());
        values.put(POS_NO18, announcemet.getPosNo());
        values.put(MESSAGE18, announcemet.getMessage());
        values.put(IS_SHOW18, announcemet.getIsShow());
        values.put(USER_NO18, announcemet.getUserNo());

        db.insert(ANNOUNCEMENT_TABLE, null, values);

        db.close();
    }


    public void addKitchenScreen(KitchenScreen kitchenScreen) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(KITCHEN_NAME, kitchenScreen.getKitchenName());
        values.put(KITCHEN_NO, kitchenScreen.getKitchenNo());
        values.put(KITCHEN_IP, kitchenScreen.getKitchenIP());


        db.insert(KITCHEN_SCREEN_TABLE, null, values);

        db.close();
    }

    public void addZReportTable(ZReport zReport) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(DATE17, zReport.getDate());
        values.put(USER_NAME17, zReport.getUserName());
        values.put(USER_NO17, zReport.getUserNo());
        values.put(POS_NO17, zReport.getPosNo());
        values.put(SERIAL17, zReport.getSerial());

        db.insert(Z_REPORT_TABLE, null, values);

        db.close();
    }

    public void addVoidReason(VoidResons obj) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(SHIFT_NO15, obj.getShiftNo());
        values.put(SHIFT_NAME15, obj.getShiftName());
        values.put(USER_NUMBER15, obj.getUserNo());
        values.put(USER_NAME15, obj.getUserName());
        values.put(VOID_REASON15, obj.getVoidReason());
        values.put(DATE15, obj.getDate());
        values.put(ACTIVEATED15, obj.getActiveated());

        db.insert(VOID_REASONS, null, values);

        db.close();
    }

    public ArrayList<ClockInClockOut> getAllExistingClockInClockOut() {
        ArrayList<ClockInClockOut> clockInClockOuts = new ArrayList<ClockInClockOut>();

        String selectQuery = "SELECT * FROM " + CLOCK_IN_CLOCK_OUT;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
            do {
                ClockInClockOut clockInClockOut = new ClockInClockOut();

                clockInClockOut.setPointOfSaleNumber(cursor.getInt(0));
                clockInClockOut.setDate(cursor.getString(1));
                clockInClockOut.setUserNO(cursor.getInt(2));
                clockInClockOut.setUserName(cursor.getString(3));
                clockInClockOut.setTranstype(cursor.getString(4));
                clockInClockOut.setDateCard(cursor.getString(5));
                clockInClockOut.setTimeCard(cursor.getString(6));
                clockInClockOut.setRemark(cursor.getString(7));
                clockInClockOut.setShiftNo(cursor.getInt(8));
                clockInClockOut.setShiftName(cursor.getString(9));

                clockInClockOuts.add(clockInClockOut);

            } while (cursor.moveToNext());

        return clockInClockOuts;

    }

    public List<Items> getAllItems() {
        List<Items> items = new ArrayList<Items>();

        String selectQuery = "SELECT  * FROM " + ITEMS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Items item = new Items();

                item.setMenuCategory(cursor.getString(0));
                item.setMenuName(cursor.getString(1));
                item.setFamilyName(cursor.getString(2));
                item.setPrice(Double.parseDouble(cursor.getString(3)));
                item.setTaxType(Integer.parseInt(cursor.getString(4)));
                item.setTax(Double.parseDouble(cursor.getString(5)));
                item.setSecondaryName(cursor.getString(6));
                item.setKitchenAlias(cursor.getString(7));
                item.setItemBarcode(Integer.parseInt(cursor.getString(8)));
                item.setStatus(Integer.parseInt(cursor.getString(9)));
                item.setItemType(Integer.parseInt(cursor.getString(10)));
                item.setDescription(cursor.getString(11));
                item.setInventoryUnit(cursor.getString(12));
                item.setWastagePercent(Double.parseDouble(cursor.getString(13)));
                item.setDiscountAvailable(Integer.parseInt(cursor.getString(14)));
                item.setPointAvailable(Integer.parseInt(cursor.getString(15)));
                item.setOpenPrice(Integer.parseInt(cursor.getString(16)));
                item.setKitchenPrinter(cursor.getString(17));
                item.setUsed(Integer.parseInt(cursor.getString(18)));
                item.setShowInMenu(Integer.parseInt(cursor.getString(19)));

                if (cursor.getBlob(20).length == 0)
                    item.setPic(null);
                else
                    item.setPic(BitmapFactory.decodeByteArray(cursor.getBlob(20), 0, cursor.getBlob(20).length));

                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public List<String> getAllExistingCategories() {
        List<String> categories = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT MENU_CATEGORY FROM " + ITEMS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return categories;
    }

    public List<UsedCategories> getAllCategories() {
        List<UsedCategories> categories = new ArrayList<>();

        String selectQuery = "select distinct U.CATEGORY_NAME , U.NUMBER_OF_ITEMS , U.CATEGORY_BACKGROUND , U.CATEGORY_TEXT_COLOR , U.CATEGORY_POSITION , F.CATEGORY_PIC\n" +
                "from USED_CATEGORIES U , FAMILY_CATEGORY_TABLE F\n" +
                "where U.CATEGORY_NAME = F.NAME_CATEGORY_FAMILY and F.TYPE = '2'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UsedCategories UsedCategory = new UsedCategories();

                UsedCategory.setCategoryName(cursor.getString(0));
                UsedCategory.setNumberOfItems(Integer.parseInt(cursor.getString(1)));
                UsedCategory.setBackground(Integer.parseInt(cursor.getString(2)));
                UsedCategory.setTextColor(Integer.parseInt(cursor.getString(3)));
                UsedCategory.setPosition(Integer.parseInt(cursor.getString(4)));
                if (cursor.getBlob(5).length == 0)
                    UsedCategory.setCatPic(null);
                else
                    UsedCategory.setCatPic(BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).length));

                categories.add(UsedCategory);
            } while (cursor.moveToNext());
        }
        return categories;
    }

    public List<UsedCategories> getUsedCategories() {
        List<UsedCategories> categories = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + USED_CATEGORIES;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                UsedCategories UsedCategory = new UsedCategories();

                UsedCategory.setCategoryName(cursor.getString(0));
                UsedCategory.setNumberOfItems(Integer.parseInt(cursor.getString(1)));
                UsedCategory.setBackground(Integer.parseInt(cursor.getString(2)));
                UsedCategory.setTextColor(Integer.parseInt(cursor.getString(3)));
                UsedCategory.setPosition(Integer.parseInt(cursor.getString(4)));
                categories.add(UsedCategory);

            } while (cursor.moveToNext());
        }
        return categories;
    }

    public ArrayList<Pay> getAllPayInOut() {
        ArrayList<Pay> pays = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + PAY_IN_OUT;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Pay pay = new Pay();

                pay.setTransType(Integer.parseInt(cursor.getString(0)));
                pay.setPosNo(Integer.parseInt(cursor.getString(1)));
                pay.setUserNo(Integer.parseInt(cursor.getString(2)));
                pay.setUserName(cursor.getString(3));
                pay.setTransDate(cursor.getString(4));
                pay.setValue(Double.parseDouble(cursor.getString(5)));
                pay.setRemark(cursor.getString(6));
                pay.setShiftNo(Integer.parseInt(cursor.getString(7)));
                pay.setShiftName(cursor.getString(8));
                pay.setTime(cursor.getString(9));
                pays.add(pay);

            } while (cursor.moveToNext());
        }
        return pays;
    }

    public ArrayList<Money> getAllMoneyCategory() {
        ArrayList<Money> items = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + MONEY_CATEGORIES + " order by CATEGORY_VALUE  Asc";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Money item = new Money();

                item.setSerial(Integer.parseInt(cursor.getString(0)));
                item.setCatName(cursor.getString(1));
                item.setCatValue(Double.parseDouble(cursor.getString(2)));
                item.setShow(Integer.parseInt(cursor.getString(3)));
                if (cursor.getBlob(4).length == 0)
                    item.setPicture(null);
                else
                    item.setPicture(BitmapFactory.decodeByteArray(cursor.getBlob(4), 0, cursor.getBlob(4).length));
                items.add(item);

            } while (cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<UsedItems> getRequestedItems(String categoryName) {
        ArrayList<UsedItems> items = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + USED_ITEMS + " where CATEGORY_NAME2 = '" + categoryName + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UsedItems item = new UsedItems();

                item.setCategoryName(cursor.getString(0));
                item.setitemName(cursor.getString(1));
                item.setBackground(Integer.parseInt(cursor.getString(2)));
                item.setTextColor(Integer.parseInt(cursor.getString(3)));
                item.setPosition(Integer.parseInt(cursor.getString(4)));
                items.add(item);

            } while (cursor.moveToNext());
        }
        return items;
    }

    public List<String> getAllExistingFamilies() {
        List<String> families = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT FAMILY_NAME FROM " + ITEMS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                families.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return families;
    }

    public List<String> getAllExistingUnits() {
        List<String> units = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT INVENTORY_UNIT FROM " + ITEMS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                units.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return units;
    }

    public ArrayList<Tables> getRequestedTables(int floor) {
        ArrayList<Tables> tables = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLES + " where FLOOR = '" + floor + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Tables table = new Tables();

                table.setHeight(Integer.parseInt(cursor.getString(0)));
                table.setWidth(Integer.parseInt(cursor.getString(1)));
                table.setImageResource(Integer.parseInt(cursor.getString(2)));
                table.setMarginLeft(Float.parseFloat(cursor.getString(3)));
                table.setMarginTop(Float.parseFloat(cursor.getString(4)));
                table.setTableNumber(Integer.parseInt(cursor.getString(6)));
                tables.add(table);

            } while (cursor.moveToNext());
        }
        return tables;
    }

    public ArrayList<CreditCard> getAllCreditCards() {
        ArrayList<CreditCard> cards = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CREDIT_CARDS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CreditCard card = new CreditCard();
                card.setSerial(Integer.parseInt(cursor.getString(0)));
                card.setCardName(cursor.getString(1));
                card.setAccCode(cursor.getString(2));

                cards.add(card);

            } while (cursor.moveToNext());
        }
        return cards;
    }

    public ArrayList<Cheque> getAllCheques() {
        ArrayList<Cheque> cheeks_iteam = new ArrayList<Cheque>();

        String selectQuery = "SELECT * FROM " + CHEQUES;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Cheque cheque = new Cheque();

                cheque.setSerialCheque(cursor.getInt(0));
                cheque.setBankName(cursor.getString(1));
                cheque.setChequeNumber(cursor.getInt(2));
                cheque.setReceived(Double.parseDouble(cursor.getString(3)));

                cheeks_iteam.add(cheque);
            } while (cursor.moveToNext());
        }
        return cheeks_iteam;
    }

    public final ArrayList<OrderTransactions> getAllRequestVoucher(String Vfh_No,String POS) {
        final ArrayList<OrderTransactions> orderTransactions = new ArrayList<>();
//        String selectQuery = "SELECT * FROM " + ORDER_TRANSACTIONS + " where VOUCHER_NO = '" + Vfh_No + "'" + " and ORDER_KIND = '0'";
        String selectQuery = "SELECT * FROM " + ORDER_TRANSACTIONS + " where VOUCHER_NO = '" + Vfh_No + "'" + " and ORDER_KIND = '0"+"'" + " and POS_NO = '"+POS+"'" ;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderTransactions item = new OrderTransactions();

                item.setOrderType(Integer.parseInt(cursor.getString(0)));
                item.setOrderKind(Integer.parseInt(cursor.getString(1)));
                item.setVoucherDate(cursor.getString(2));
                item.setPosNo(Integer.parseInt(cursor.getString(3)));
                item.setStoreNo(Integer.parseInt(cursor.getString(4)));
                item.setVoucherNo(cursor.getString(5));
                item.setVoucherSerial(Integer.parseInt(cursor.getString(6)));
                item.setItemBarcode(cursor.getString(7));
                item.setItemName(cursor.getString(8));
                item.setSecondaryName(cursor.getString(9));
                item.setKitchenAlias(cursor.getString(10));
                item.setItemCategory(cursor.getString(11));
                item.setItemFamily(cursor.getString(12));
                item.setQty(Integer.parseInt(cursor.getString(13)));
                item.setPrice(Double.parseDouble(cursor.getString(14)));
                item.setTotal(Double.parseDouble(cursor.getString(15)));
                item.setDiscount(Double.parseDouble(cursor.getString(16)));
                item.setlDiscount(Double.parseDouble(cursor.getString(17)));
                item.setTotalDiscount(Double.parseDouble(cursor.getString(18)));
                item.setTaxValue(Double.parseDouble(cursor.getString(19)));
                item.setTaxPerc(Double.parseDouble(cursor.getString(20)));
                item.setTaxKind(Integer.parseInt(cursor.getString(21)));
                item.setService(Integer.parseInt(cursor.getString(22)));
                item.setServiceTax(Double.parseDouble(cursor.getString(23)));
                item.setTableNo(Integer.parseInt(cursor.getString(24)));
                item.setUserNo(Integer.parseInt(cursor.getString(25)));
                item.setUserName(cursor.getString(26));
                item.setSectionNo(Integer.parseInt(cursor.getString(27)));
                item.setShiftNo(Integer.parseInt(cursor.getString(28)));
                item.setShiftName(cursor.getString(29));
                item.setTime(cursor.getString(30));
                item.setOrgNo(cursor.getString(31));
                item.setOrgPos(cursor.getInt(32));
                item.setReturnQty(cursor.getInt(33));

                orderTransactions.add(item);

            } while (cursor.moveToNext());
        }

        return orderTransactions;
    }

    public final String getAllRequestVoucherHeader(String Vfh_No,String POS) {
         String waterName ="";
//        String selectQuery = "SELECT * FROM " + ORDER_TRANSACTIONS + " where VOUCHER_NO = '" + Vfh_No + "'" + " and ORDER_KIND = '0'";
        String selectQuery = "SELECT WAITER FROM " + ORDER_HEADER + " where VOUCHER_NUMBER = '" + Vfh_No + "'" + " and ORDER_KIND = '0"+"'" + " and POINT_OF_SALE_NUMBER = '"+POS+"'" ;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderHeader item = new OrderHeader();

                item.setWaiter(cursor.getString(0));
                waterName=item.getWaiter();

            } while (cursor.moveToNext());
        }

        return waterName;
    }

    public final ArrayList<PayMethod> getAllRequestPayMethod(String Vfh_No,String pos) {
        final ArrayList<PayMethod> orderTransactions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PAY_METHOD + " where VOUCHER_NUMBER = '" + Vfh_No + "'" + " and ORDER_KIND = '0'"+"and POINT_OF_SALE_NUMBER = '"+pos+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PayMethod item = new PayMethod();

                item.setOrderType(cursor.getInt(0));
                item.setOrderKind(cursor.getInt(1));
                item.setVoucherDate(cursor.getString(2));
                item.setPointOfSaleNumber(cursor.getInt(3));
                item.setStoreNumber(cursor.getInt(4));
                item.setVoucherNumber(cursor.getString(5));
                item.setVoucherSerial(cursor.getInt(6));
                item.setPayType(cursor.getString(7));
                item.setPayValue(cursor.getDouble(8));
                item.setPayNumber(cursor.getString(9));
                item.setPayName(cursor.getString(10));
                item.setShiftName(cursor.getString(11));
                item.setShiftNumber(cursor.getInt(12));
                item.setUserName(cursor.getString(13));
                item.setUserNo(cursor.getInt(14));
                item.setTime(cursor.getString(15));
                item.setOrgNo(cursor.getString(16));
                item.setOrgPos(cursor.getInt(17));

                orderTransactions.add(item);

            } while (cursor.moveToNext());
        }

        return orderTransactions;
    }

    public List<String> getAllOrderedCategories() {
        List<String> categories = new ArrayList<>();

        String selectQuery = "select distinct(ITEM_CATEGORY) from ORDER_TRANSACTIONS;";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return categories;
    }

    public List<OrderTransactions> getOrdersTransactionsByCategory(String category) {
        List<OrderTransactions> items = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + ORDER_TRANSACTIONS + " where ITEM_CATEGORY = '" + category + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderTransactions item = new OrderTransactions();

                item.setOrderType(Integer.parseInt(cursor.getString(0)));
                item.setOrderKind(Integer.parseInt(cursor.getString(1)));
                item.setVoucherDate(cursor.getString(2));
                item.setPosNo(Integer.parseInt(cursor.getString(3)));
                item.setStoreNo(Integer.parseInt(cursor.getString(4)));
                item.setVoucherNo(cursor.getString(5));
                item.setVoucherSerial(Integer.parseInt(cursor.getString(6)));
                item.setItemBarcode(cursor.getString(7));
                item.setItemName(cursor.getString(8));
                item.setSecondaryName(cursor.getString(9));
                item.setKitchenAlias(cursor.getString(10));
                item.setItemCategory(cursor.getString(11));
                item.setItemFamily(cursor.getString(12));
                item.setQty(Integer.parseInt(cursor.getString(13)));
                item.setPrice(Double.parseDouble(cursor.getString(14)));
                item.setTotal(Double.parseDouble(cursor.getString(15)));
                item.setDiscount(Double.parseDouble(cursor.getString(16)));
                item.setlDiscount(Double.parseDouble(cursor.getString(17)));
                item.setTotalDiscount(Double.parseDouble(cursor.getString(18)));
                item.setTaxValue(Double.parseDouble(cursor.getString(19)));
                item.setTaxPerc(Double.parseDouble(cursor.getString(20)));
                item.setTaxKind(Integer.parseInt(cursor.getString(21)));
                item.setService(Integer.parseInt(cursor.getString(22)));
                item.setServiceTax(Double.parseDouble(cursor.getString(23)));
                item.setTableNo(Integer.parseInt(cursor.getString(24)));
                item.setUserNo(Integer.parseInt(cursor.getString(25)));
                item.setUserName(cursor.getString(26));
                item.setSectionNo(Integer.parseInt(cursor.getString(27)));
                item.setShiftNo(Integer.parseInt(cursor.getString(28)));
                item.setShiftName(cursor.getString(29));
                item.setTime(cursor.getString(30));
                item.setOrgNo(cursor.getString(31));
                item.setOrgPos(cursor.getInt(32));
                item.setReturnQty(cursor.getInt(33));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public List<OrderTransactions> getAllOrderTransactions() {
        List<OrderTransactions> items = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + ORDER_TRANSACTIONS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderTransactions item = new OrderTransactions();

                item.setOrderType(Integer.parseInt(cursor.getString(0)));
                item.setOrderKind(Integer.parseInt(cursor.getString(1)));
                item.setVoucherDate(cursor.getString(2));
                item.setPosNo(Integer.parseInt(cursor.getString(3)));
                item.setStoreNo(Integer.parseInt(cursor.getString(4)));
                item.setVoucherNo(cursor.getString(5));
                item.setVoucherSerial(Integer.parseInt(cursor.getString(6)));
                item.setItemBarcode(cursor.getString(7));
                item.setItemName(cursor.getString(8));
                item.setSecondaryName(cursor.getString(9));
                item.setKitchenAlias(cursor.getString(10));
                item.setItemCategory(cursor.getString(11));
                item.setItemFamily(cursor.getString(12));
                item.setQty(Integer.parseInt(cursor.getString(13)));
                item.setPrice(Double.parseDouble(cursor.getString(14)));
                item.setTotal(Double.parseDouble(cursor.getString(15)));
                item.setDiscount(Double.parseDouble(cursor.getString(16)));
                item.setlDiscount(Double.parseDouble(cursor.getString(17)));
                item.setTotalDiscount(Double.parseDouble(cursor.getString(18)));
                item.setTaxValue(Double.parseDouble(cursor.getString(19)));
                item.setTaxPerc(Double.parseDouble(cursor.getString(20)));
                item.setTaxKind(Integer.parseInt(cursor.getString(21)));
                item.setService(Integer.parseInt(cursor.getString(22)));
                item.setServiceTax(Double.parseDouble(cursor.getString(23)));
                item.setTableNo(Integer.parseInt(cursor.getString(24)));
                item.setUserNo(Integer.parseInt(cursor.getString(25)));
                item.setUserName(cursor.getString(26));
                item.setSectionNo(Integer.parseInt(cursor.getString(27)));
                item.setShiftNo(Integer.parseInt(cursor.getString(28)));
                item.setShiftName(cursor.getString(29));
                item.setTime(cursor.getString(30));
                item.setOrgNo(cursor.getString(31));
                item.setOrgPos(cursor.getInt(32));
                item.setReturnQty(cursor.getInt(33));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public List<OrderTransactions> getAllOrderTransactionsTemp() {
        List<OrderTransactions> items = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + ORDER_TRANSACTIONS_TEMP;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderTransactions item = new OrderTransactions();

                item.setOrderType(Integer.parseInt(cursor.getString(0)));
                item.setOrderKind(Integer.parseInt(cursor.getString(1)));
                item.setVoucherDate(cursor.getString(2));
                item.setPosNo(Integer.parseInt(cursor.getString(3)));
                item.setStoreNo(Integer.parseInt(cursor.getString(4)));
                item.setVoucherNo(cursor.getString(5));
                item.setVoucherSerial(Integer.parseInt(cursor.getString(6)));
                item.setItemBarcode(cursor.getString(7));
                item.setItemName(cursor.getString(8));
                item.setSecondaryName(cursor.getString(9));
                item.setKitchenAlias(cursor.getString(10));
                item.setItemCategory(cursor.getString(11));
                item.setItemFamily(cursor.getString(12));
                item.setQty(Integer.parseInt(cursor.getString(13)));
                item.setPrice(Double.parseDouble(cursor.getString(14)));
                item.setTotal(Double.parseDouble(cursor.getString(15)));
                item.setDiscount(Double.parseDouble(cursor.getString(16)));
                item.setlDiscount(Double.parseDouble(cursor.getString(17)));
                item.setTotalDiscount(Double.parseDouble(cursor.getString(18)));
                item.setTaxValue(Double.parseDouble(cursor.getString(19)));
                item.setTaxPerc(Double.parseDouble(cursor.getString(20)));
                item.setTaxKind(Integer.parseInt(cursor.getString(21)));
                item.setService(Integer.parseInt(cursor.getString(22)));
                item.setServiceTax(Double.parseDouble(cursor.getString(23)));
                item.setTableNo(Integer.parseInt(cursor.getString(24)));
                item.setUserNo(Integer.parseInt(cursor.getString(25)));
                item.setUserName(cursor.getString(26));
                item.setSectionNo(Integer.parseInt(cursor.getString(27)));
                item.setShiftNo(Integer.parseInt(cursor.getString(28)));
                item.setShiftName(cursor.getString(29));
                item.setTime(cursor.getString(30));
                item.setOrgNo(cursor.getString(31));
                item.setOrgPos(cursor.getInt(32));
                item.setReturnQty(cursor.getInt(33));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public List<OrderTransactions> getOrderTransactionsTemp(String sectionNo, String tableNo) {
        List<OrderTransactions> items = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ORDER_TRANSACTIONS_TEMP + " WHERE SECTION_NO = '" + sectionNo + "' and TABLE_NO = '" + tableNo + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderTransactions item = new OrderTransactions();

                item.setOrderType(Integer.parseInt(cursor.getString(0)));
                item.setOrderKind(Integer.parseInt(cursor.getString(1)));
                item.setVoucherDate(cursor.getString(2));
                item.setPosNo(Integer.parseInt(cursor.getString(3)));
                item.setStoreNo(Integer.parseInt(cursor.getString(4)));
                item.setVoucherNo(cursor.getString(5));
                item.setVoucherSerial(Integer.parseInt(cursor.getString(6)));
                item.setItemBarcode(cursor.getString(7));
                item.setItemName(cursor.getString(8));
                item.setSecondaryName(cursor.getString(9));
                item.setKitchenAlias(cursor.getString(10));
                item.setItemCategory(cursor.getString(11));
                item.setItemFamily(cursor.getString(12));
                item.setQty(Integer.parseInt(cursor.getString(13)));
                item.setPrice(Double.parseDouble(cursor.getString(14)));
                item.setTotal(Double.parseDouble(cursor.getString(15)));
                item.setDiscount(Double.parseDouble(cursor.getString(16)));
                item.setlDiscount(Double.parseDouble(cursor.getString(17)));
                item.setTotalDiscount(Double.parseDouble(cursor.getString(18)));
                item.setTaxValue(Double.parseDouble(cursor.getString(19)));
                item.setTaxPerc(Double.parseDouble(cursor.getString(20)));
                item.setTaxKind(Integer.parseInt(cursor.getString(21)));
                item.setService(Integer.parseInt(cursor.getString(22)));
                item.setServiceTax(Double.parseDouble(cursor.getString(23)));
                item.setTableNo(Integer.parseInt(cursor.getString(24)));
                item.setUserNo(Integer.parseInt(cursor.getString(25)));
                item.setUserName(cursor.getString(26));
                item.setSectionNo(Integer.parseInt(cursor.getString(27)));
                item.setShiftNo(Integer.parseInt(cursor.getString(28)));
                item.setShiftName(cursor.getString(29));
                item.setTime(cursor.getString(30));
                item.setOrgNo(cursor.getString(31));
                item.setOrgPos(cursor.getInt(32));
                item.setReturnQty(cursor.getInt(33));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public List<OrderTransactions> getTopSalesItemsByQty(String shiftName,String pos,String USER_No) {
        List<OrderTransactions> items = new ArrayList<>();

        String selectQuery = "select ITEM_BARCODE1,ITEM_NAME , GROUP_CONCAT( VOUCHER_DATE) ,GROUP_CONCAT(QTY), GROUP_CONCAT( TOTAL)  from ORDER_TRANSACTIONS  " +
                "where POS_NO = "+pos +" and SHIFT_NAME = "+shiftName +" and USER_NO = "+USER_No +
                " group by ITEM_BARCODE1 ORDER BY GROUP_CONCAT(QTY) DESC";


//        String selectQuery = "select ITEM_BARCODE1 , ITEM_NAME , VOUCHER_DATE , SHIFT_NAME , POS_NO  , USER_NAME , sum(QTY) , sum(TOTAL) \n" +
//                "from ORDER_TRANSACTIONS\n" +
//                "group by ITEM_BARCODE1 ORDER BY QTY DESC;";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                OrderTransactions item = new OrderTransactions();

                item.setItemBarcode(cursor.getString(0));
                item.setItemName(cursor.getString(1));//connt string of name
                item.setVoucherDate(cursor.getString(2));//connt string of date
                item.setShiftName(cursor.getString(3));//connt string of qty
                item.setUserName(cursor.getString(4));//connt string of total


                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

//    public List<OrderTransactions> getTopSalesItemsByTotal(String shiftName,String pos) {
//        List<OrderTransactions> items = new ArrayList<>();
//
//        String selectQuery = "select ITEM_BARCODE1,ITEM_NAME , GROUP_CONCAT( VOUCHER_DATE) ,GROUP_CONCAT(QTY), GROUP_CONCAT( TOTAL)  from ORDER_TRANSACTIONS  " +
//                "where POS_NO = "+pos +" and SHIFT_NAME = "+shiftName +
//                " group by ITEM_BARCODE1 ORDER BY TOTAL DESC";
//
//
//
////        String selectQuery = "select ITEM_BARCODE1 , ITEM_NAME , VOUCHER_DATE , SHIFT_NAME , POS_NO  , USER_NAME , sum(QTY) , sum(TOTAL) \n" +
////                "from ORDER_TRANSACTIONS\n" +
////                "group by ITEM_BARCODE1 ORDER BY TOTAL DESC;";
//
//        db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                OrderTransactions item = new OrderTransactions();
//
//                item.setItemBarcode(cursor.getString(0));
//                item.setItemName(cursor.getString(1));//connt string of name
//                item.setVoucherDate(cursor.getString(2));//connt string of date
//                item.setShiftName(cursor.getString(3));//connt string of qty
//                item.setUserName(cursor.getString(4));//connt string of total
//
////                Log.e("log ", "" + Double.parseDouble(cursor.getString(7)));
//                items.add(item);
//            } while (cursor.moveToNext());
//        }
//        return items;
//    }

    public List<String> getAllOrderedTables(int sectionNo) {
        List<String> tables = new ArrayList<>();

        String selectQuery = "SELECT  DISTINCT TABLE_NO FROM " + ORDER_TRANSACTIONS_TEMP + " WHERE SECTION_NO = '" + sectionNo + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                tables.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return tables;
    }

    public ArrayList<PayMethod> getAllExistingPay() {
        ArrayList<PayMethod> payMethodsList = new ArrayList<PayMethod>();

        String selectQuery = "SELECT * FROM " + PAY_METHOD;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
            do {
                PayMethod payMethod = new PayMethod();

                payMethod.setOrderType(cursor.getInt(0));
                payMethod.setOrderKind(cursor.getInt(1));
                payMethod.setVoucherDate(cursor.getString(2));
                payMethod.setPointOfSaleNumber(cursor.getInt(3));
                payMethod.setStoreNumber(cursor.getInt(4));
                payMethod.setVoucherNumber(cursor.getString(5));
                payMethod.setVoucherSerial(cursor.getInt(6));
                payMethod.setPayType(cursor.getString(7));
                payMethod.setPayValue(cursor.getDouble(8));
                payMethod.setPayNumber(cursor.getString(9));
                payMethod.setPayName(cursor.getString(10));
                payMethod.setUserName(cursor.getString(11));
                payMethod.setUserNo(cursor.getInt(12));
                payMethod.setShiftName(cursor.getString(13));
                payMethod.setShiftNumber(cursor.getInt(14));
                payMethod.setTime(cursor.getString(15));
                payMethod.setOrgNo(cursor.getString(16));
                payMethod.setOrgPos(cursor.getInt(17));

                payMethodsList.add(payMethod);

            } while (cursor.moveToNext());

        return payMethodsList;

    }

    public int getMaxSerial(String ColumeName, String TableName) {
        ArrayList<Integer> moneys = new ArrayList<>();
        int max;
        String selectQuery = "SELECT " + ColumeName + " FROM " + TableName;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                moneys.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        if (moneys.isEmpty())
            max = 0;
        else
            max = Collections.max(moneys);
        return max;
    }

    public int getMaxZReportSerial(String posNo) {
        ArrayList<Integer> moneys = new ArrayList<>();
        int max;
        String selectQuery = "SELECT " + SERIAL17 + " FROM " + Z_REPORT_TABLE + " where POS_NO = " + posNo;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                moneys.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        if (moneys.isEmpty())
            max = 0;
        else
            max = Collections.max(moneys);
        return max;
    }

    public ArrayList<OrderHeader> getAllOrderHeader() {
        ArrayList<OrderHeader> orderHeaders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ORDER_HEADER;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
            do {
                OrderHeader order_header = new OrderHeader();

                order_header.setOrderType(cursor.getInt(0));
                order_header.setOrderKind(cursor.getInt(1));
                order_header.setVoucherDate(cursor.getString(2));
                order_header.setPointOfSaleNumber(cursor.getInt(3));
                order_header.setStoreNumber(cursor.getInt(4));
                order_header.setVoucherNumber(cursor.getString(5));
                order_header.setVoucherSerial(cursor.getInt(6));
                order_header.setTotal(cursor.getDouble(7));
                order_header.setTotalLineDiscount(cursor.getDouble(8));
                order_header.setTotalDiscount(cursor.getDouble(9));
                order_header.setAllDiscount(cursor.getDouble(10));
                order_header.setTotalService(cursor.getDouble(11));
                order_header.setTotalTax(cursor.getDouble(12));
                order_header.setTotalServiceTax(cursor.getDouble(13));
                order_header.setSubTotal(cursor.getDouble(14));
                order_header.setAmountDue(cursor.getDouble(15));
                order_header.setDeliveryCharge(cursor.getDouble(16));
                order_header.setTableNO(cursor.getInt(17));
                order_header.setSectionNO(cursor.getInt(18));
                order_header.setCashValue(cursor.getDouble(19));
                order_header.setCardsValue(cursor.getDouble(20));
                order_header.setChequeValue(cursor.getDouble(21));
                order_header.setCouponValue(cursor.getDouble(22));
                order_header.setGiftValue(cursor.getDouble(23));
                order_header.setPointValue(cursor.getDouble(24));
                order_header.setUserName(cursor.getString(25));
                order_header.setUserNo(cursor.getInt(26));
                order_header.setShiftName(cursor.getString(27));
                order_header.setShiftNumber(cursor.getInt(28));
                order_header.setWaiter(cursor.getString(29));
                order_header.setSeatsNumber(cursor.getInt(30));
                order_header.setTime(cursor.getString(31));
                order_header.setOrgNo(cursor.getString(32));
                order_header.setOrgPos(cursor.getInt(33));



                orderHeaders.add(order_header);

            } while (cursor.moveToNext());
        return orderHeaders;


    }

    public ArrayList<OrderHeader> getAllOrderHeaderTemp() {
        ArrayList<OrderHeader> orderHeaders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ORDER_HEADER_TEMP;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
            do {
                OrderHeader order_header = new OrderHeader();

                order_header.setOrderType(cursor.getInt(0));
                order_header.setOrderKind(cursor.getInt(1));
                order_header.setVoucherDate(cursor.getString(2));
                order_header.setPointOfSaleNumber(cursor.getInt(3));
                order_header.setStoreNumber(cursor.getInt(4));
                order_header.setVoucherNumber(cursor.getString(5));
                order_header.setVoucherSerial(cursor.getInt(6));
                order_header.setTotal(cursor.getDouble(7));
                order_header.setTotalLineDiscount(cursor.getDouble(8));
                order_header.setTotalDiscount(cursor.getDouble(9));
                order_header.setAllDiscount(cursor.getDouble(10));
                order_header.setTotalService(cursor.getDouble(11));
                order_header.setTotalTax(cursor.getDouble(12));
                order_header.setTotalServiceTax(cursor.getDouble(13));
                order_header.setSubTotal(cursor.getDouble(14));
                order_header.setAmountDue(cursor.getDouble(15));
                order_header.setDeliveryCharge(cursor.getDouble(16));
                order_header.setTableNO(cursor.getInt(17));
                order_header.setSectionNO(cursor.getInt(18));
                order_header.setCashValue(cursor.getDouble(19));
                order_header.setCardsValue(cursor.getDouble(20));
                order_header.setChequeValue(cursor.getDouble(21));
                order_header.setCouponValue(cursor.getDouble(22));
                order_header.setGiftValue(cursor.getDouble(23));
                order_header.setPointValue(cursor.getDouble(24));
                order_header.setUserName(cursor.getString(25));
                order_header.setUserNo(cursor.getInt(26));
                order_header.setShiftName(cursor.getString(27));
                order_header.setShiftNumber(cursor.getInt(28));
                order_header.setWaiter(cursor.getString(29));
                order_header.setSeatsNumber(cursor.getInt(30));
                order_header.setTime(cursor.getString(31));
                order_header.setOrgNo(cursor.getString(32));
                order_header.setOrgPos(cursor.getInt(33));

                orderHeaders.add(order_header);

            } while (cursor.moveToNext());
        return orderHeaders;


    }

    public ArrayList<OrderHeader> getOrderHeaderTemp(String sectionNo, String tableNo) {
        ArrayList<OrderHeader> orderHeaders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ORDER_HEADER_TEMP + " WHERE SECTION_NUMBER = '" + sectionNo + "' and TABLE_NUMBER = '" + tableNo + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
            do {
                OrderHeader order_header = new OrderHeader();

                order_header.setOrderType(cursor.getInt(0));
                order_header.setOrderKind(cursor.getInt(1));
                order_header.setVoucherDate(cursor.getString(2));
                order_header.setPointOfSaleNumber(cursor.getInt(3));
                order_header.setStoreNumber(cursor.getInt(4));
                order_header.setVoucherNumber(cursor.getString(5));
                order_header.setVoucherSerial(cursor.getInt(6));
                order_header.setTotal(cursor.getDouble(7));
                order_header.setTotalDiscount(cursor.getDouble(8));
                order_header.setTotalLineDiscount(cursor.getDouble(9));
                order_header.setAllDiscount(cursor.getDouble(10));
                order_header.setTotalService(cursor.getDouble(11));
                order_header.setTotalTax(cursor.getDouble(12));
                order_header.setTotalServiceTax(cursor.getDouble(13));
                order_header.setSubTotal(cursor.getDouble(14));
                order_header.setAmountDue(cursor.getDouble(15));
                order_header.setDeliveryCharge(cursor.getDouble(16));
                order_header.setTableNO(cursor.getInt(17));
                order_header.setSectionNO(cursor.getInt(18));
                order_header.setCashValue(cursor.getDouble(19));
                order_header.setCardsValue(cursor.getDouble(20));
                order_header.setChequeValue(cursor.getDouble(21));
                order_header.setCouponValue(cursor.getDouble(22));
                order_header.setGiftValue(cursor.getDouble(23));
                order_header.setPointValue(cursor.getDouble(24));
                order_header.setUserName(cursor.getString(25));
                order_header.setUserNo(cursor.getInt(26));
                order_header.setShiftName(cursor.getString(27));
                order_header.setShiftNumber(cursor.getInt(28));
                order_header.setWaiter(cursor.getString(29));
                order_header.setSeatsNumber(cursor.getInt(30));
                order_header.setTime(cursor.getString(31));
                order_header.setOrgNo(cursor.getString(32));
                order_header.setOrgPos(cursor.getInt(33));

                orderHeaders.add(order_header);

            } while (cursor.moveToNext());
        return orderHeaders;


    }

    public ArrayList<ForceQuestions> getAllForceQuestions() {
        ArrayList<ForceQuestions> questions = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + FORCE_QUESTIONS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ForceQuestions question = new ForceQuestions();
                question.setQuestionNo(Integer.parseInt(cursor.getString(0)));
                question.setQuestionText(cursor.getString(1));
                question.setMultipleAnswer(Integer.parseInt(cursor.getString(2)));
                question.setAnswer(cursor.getString(3));

                questions.add(question);

            } while (cursor.moveToNext());
        }
        return questions;
    }

    public ArrayList<ForceQuestions> getRequestedForceQuestions(int questionNo) {
        ArrayList<ForceQuestions> questions = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + FORCE_QUESTIONS + " where QUESTION_NO ='" + questionNo + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ForceQuestions question = new ForceQuestions();
                question.setQuestionNo(Integer.parseInt(cursor.getString(0)));
                question.setQuestionText(cursor.getString(1));
                question.setMultipleAnswer(Integer.parseInt(cursor.getString(2)));
                question.setAnswer(cursor.getString(3));

                questions.add(question);

            } while (cursor.moveToNext());
        }
        return questions;
    }

    public ArrayList<ForceQuestions> getSomeForceQuestions() {
        ArrayList<ForceQuestions> questions = new ArrayList<>();

        String selectQuery = "SELECT distinct QUESTION_NO , QUESTION_TEXT FROM " + FORCE_QUESTIONS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ForceQuestions question = new ForceQuestions();
                question.setQuestionNo(Integer.parseInt(cursor.getString(0)));
                question.setQuestionText(cursor.getString(1));

                questions.add(question);

            } while (cursor.moveToNext());
        }
        return questions;
    }

    public ArrayList<Modifier> getAllModifiers() {
        ArrayList<Modifier> modifiers = new ArrayList<Modifier>();
        String selectQuery = "SELECT * FROM " + MODIFIER;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
            do {
                Modifier modifier = new Modifier();
                modifier.setModifierNumber(cursor.getInt(0));
                modifier.setModifierName(cursor.getString(1));
                modifier.setModifierActive(cursor.getInt(2));

                modifiers.add(modifier);
            } while (cursor.moveToNext());
        return modifiers;
    }

    public ArrayList<CustomerPayment> getAllCustomerPayment() {
        ArrayList<CustomerPayment> customerPayments = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + CUSTOMER_PAYMENT;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomerPayment customerPayment = new CustomerPayment();

                customerPayment.setPointOfSaleNumber(cursor.getInt(0));
                customerPayment.setUserNO(cursor.getInt(1));
                customerPayment.setUserName(cursor.getString(2));
                customerPayment.setCustomerNo(cursor.getInt(3));
                customerPayment.setCustomerName(cursor.getString(4));
                customerPayment.setCustomerBalance(cursor.getDouble(5));
                customerPayment.setTransNo(cursor.getInt(6));
                customerPayment.setTransDate(cursor.getString(7));
                customerPayment.setPayMentType(cursor.getString(8));
                customerPayment.setValue(cursor.getDouble(9));
                customerPayment.setShiftNo(cursor.getInt(10));
                customerPayment.setShiftName(cursor.getString(11));

                customerPayments.add(customerPayment);
            } while (cursor.moveToNext());
        }
        return customerPayments;
    }

    public ArrayList<CategoryWithModifier> getAllCategoryModifier() {
        ArrayList<CategoryWithModifier> categoryModifiers = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CATEGORY_WITH_MODIFIER;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
            do {
                CategoryWithModifier categoryModifier = new CategoryWithModifier();

                categoryModifier.setCategoryName(cursor.getString(0));
                categoryModifier.setModifierName(cursor.getString(1));

                categoryModifiers.add(categoryModifier);
            } while (cursor.moveToNext());
        return categoryModifiers;
    }

    public ArrayList<ItemWithModifier> getItemWithModifiers(int itemBarcode) {
        ArrayList<ItemWithModifier> modifiers = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ITEM_WITH_MODIFIER + " where ITEM_CODE = '" + itemBarcode + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemWithModifier modifier = new ItemWithModifier();
                modifier.setItemCode(Integer.parseInt(cursor.getString(0)));
                modifier.setModifierNo(Integer.parseInt(cursor.getString(1)));
                modifier.setModifierText(cursor.getString(2));

                modifiers.add(modifier);

            } while (cursor.moveToNext());
        }
        return modifiers;
    }

    public ArrayList<ItemWithFq> getAllItemWithFqs() {
        ArrayList<ItemWithFq> fqs = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ITEM_WITH_FQ;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemWithFq fq = new ItemWithFq();
                fq.setItemCode(Integer.parseInt(cursor.getString(0)));
                fq.setQuestionNo(Integer.parseInt(cursor.getString(1)));
                fq.setQuestionText(cursor.getString(2));

                fqs.add(fq);

            } while (cursor.moveToNext());
        }
        return fqs;
    }

    public ArrayList<ItemWithFq> getItemWithFqs(int itemBarcode) {
        ArrayList<ItemWithFq> fqs = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ITEM_WITH_FQ + " where ITEM_CODE = '" + itemBarcode + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemWithFq fq = new ItemWithFq();
                fq.setItemCode(Integer.parseInt(cursor.getString(0)));
                fq.setQuestionNo(Integer.parseInt(cursor.getString(1)));
                fq.setQuestionText(cursor.getString(2));

                fqs.add(fq);

            } while (cursor.moveToNext());
        }
        return fqs;
    }

    public ArrayList<JobGroup> getAllJobGroup() {
        ArrayList<JobGroup> jobGroups = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + JOB_GROUP_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                JobGroup jobGroup = new JobGroup();

                jobGroup.setJobGroup(cursor.getString(0));
                jobGroup.setUserName(cursor.getString(1));
                jobGroup.setUserNo(Integer.parseInt(cursor.getString(2)));
                jobGroup.setInDate(cursor.getString(3));
                jobGroup.setActive(Integer.parseInt(cursor.getString(4)));
                jobGroup.setShiftNo(Integer.parseInt(cursor.getString(5)));
                jobGroup.setShiftName(cursor.getString(6));
                jobGroups.add(jobGroup);

            } while (cursor.moveToNext());
        }
        return jobGroups;
    }

    public ArrayList<MemberShipGroup> getAllMemberShipGroup() {
        ArrayList<MemberShipGroup> memberShipGroups = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + MEMBER_SHIP_GROUP_MANAGEMENT_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MemberShipGroup memberShipGroup = new MemberShipGroup();

                memberShipGroup.setMemberShipGroup(cursor.getString(0));
                memberShipGroup.setUserName(cursor.getString(1));
                memberShipGroup.setUserNo(Integer.parseInt(cursor.getString(2)));
                memberShipGroup.setInDate(cursor.getString(3));
                memberShipGroup.setActive(Integer.parseInt(cursor.getString(4)));
                memberShipGroup.setShiftNo(Integer.parseInt(cursor.getString(5)));
                memberShipGroup.setShiftName(cursor.getString(6));

                memberShipGroups.add(memberShipGroup);

            } while (cursor.moveToNext());
        }
        return memberShipGroups;
    }

    public ArrayList<Shift> getAllShifts() {
        ArrayList<Shift> shifts = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + SHIFT_REGISTRATION;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Shift shift = new Shift();

                shift.setShiftNo(Integer.parseInt(cursor.getString(0)));
                shift.setShiftName(cursor.getString(1));
                shift.setFromTime(cursor.getString(2));
                shift.setToTime(cursor.getString(3));

                shifts.add(shift);

            } while (cursor.moveToNext());
        }
        return shifts;
    }

    public BlindShift getOpenedShifts(String date, int status) {
        BlindShift shift = new BlindShift();

        String selectQuery = "SELECT * FROM " + BLIND_SHIFT_IN + " where DATE = '" + date + "' and STATUS = '" + status + "'";
        Log.e("*****", selectQuery);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            shift.setDate(cursor.getString(0));
            shift.setTime(cursor.getString(1));
            shift.setPosNo(Integer.parseInt(cursor.getString(2)));
            shift.setShiftNo(Integer.parseInt(cursor.getString(3)));
            shift.setShiftName(cursor.getString(4));
            shift.setUserNo(Integer.parseInt(cursor.getString(5)));
            shift.setUserName(cursor.getString(6));
            shift.setStatus(Integer.parseInt(cursor.getString(7)));
        }
        return shift;
    }

    public ArrayList<BlindClose> getAllBlindClose() {
        ArrayList<BlindClose> shifts = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + BLIND_CLOSE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BlindClose shift = new BlindClose();

                shift.setTransNo(Integer.parseInt(cursor.getString(0)));
                shift.setDate(cursor.getString(1));
                shift.setTime(cursor.getString(2));
                shift.setPOSNo(Integer.parseInt(cursor.getString(3)));
                shift.setShiftNo(Integer.parseInt(cursor.getString(4)));
                shift.setShiftName(cursor.getString(5));
                shift.setUserNo(Integer.parseInt(cursor.getString(6)));
                shift.setUserName(cursor.getString(7));
                shift.setSysSales(Double.parseDouble(cursor.getString(8)));
                shift.setUserSales(Double.parseDouble(cursor.getString(9)));
                shift.setSalesDiff(Double.parseDouble(cursor.getString(10)));
                shift.setSysCash(Double.parseDouble(cursor.getString(11)));
                shift.setUserCash(Double.parseDouble(cursor.getString(12)));
                shift.setCashDiff(Double.parseDouble(cursor.getString(13)));
                shift.setSysOthers(Double.parseDouble(cursor.getString(14)));
                shift.setUserOthers(Double.parseDouble(cursor.getString(15)));
                shift.setOthersDiff(Double.parseDouble(cursor.getString(16)));
                shift.setTillOk(Integer.parseInt(cursor.getString(17)));
                shift.setTransType(Integer.parseInt(cursor.getString(18)));
                shift.setReason(cursor.getString(19));
                shift.setToUser(cursor.getString(20));

                shifts.add(shift);

            } while (cursor.moveToNext());
        }
        return shifts;
    }

    public ArrayList<BlindCloseDetails> getAllBlindCloseDetails() {
        ArrayList<BlindCloseDetails> shifts = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + BLIND_CLOSE_DETAILS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BlindCloseDetails shift = new BlindCloseDetails();

                shift.setTransNo(Integer.parseInt(cursor.getString(0)));
                shift.setDate(cursor.getString(1));
                shift.setTime(cursor.getString(2));
                shift.setPOSNo(Integer.parseInt(cursor.getString(3)));
                shift.setShiftNo(Integer.parseInt(cursor.getString(4)));
                shift.setShiftName(cursor.getString(5));
                shift.setUserNo(Integer.parseInt(cursor.getString(6)));
                shift.setUserName(cursor.getString(7));
                shift.setCatName(cursor.getString(8));
                shift.setCatQty(Integer.parseInt(cursor.getString(9)));
                shift.setCatValue(Double.parseDouble(cursor.getString(10)));
                shift.setCatTotal(Double.parseDouble(cursor.getString(11)));
                shift.setType(cursor.getString(12));
                shift.setUpdateDate(cursor.getString(13));
                shift.setUpdateTime(cursor.getString(14));
                shift.setUpdateUserNo(Integer.parseInt(cursor.getString(15)));
                shift.setUpdateUserName(cursor.getString(16));

                shifts.add(shift);

            } while (cursor.moveToNext());
        }
        return shifts;
    }

    public ArrayList<BlindCloseDetails> getBlindCloseDetails(int transNo) {
        ArrayList<BlindCloseDetails> shifts = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + BLIND_CLOSE_DETAILS + " where TRANS_NO = '" + transNo + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BlindCloseDetails shift = new BlindCloseDetails();

                shift.setTransNo(Integer.parseInt(cursor.getString(0)));
                shift.setDate(cursor.getString(1));
                shift.setTime(cursor.getString(2));
                shift.setPOSNo(Integer.parseInt(cursor.getString(3)));
                shift.setShiftNo(Integer.parseInt(cursor.getString(4)));
                shift.setShiftName(cursor.getString(5));
                shift.setUserNo(Integer.parseInt(cursor.getString(6)));
                shift.setUserName(cursor.getString(7));
                shift.setCatName(cursor.getString(8));
                shift.setCatQty(Integer.parseInt(cursor.getString(9)));
                shift.setCatValue(Double.parseDouble(cursor.getString(10)));
                shift.setCatTotal(Double.parseDouble(cursor.getString(11)));
                shift.setType(cursor.getString(12));
                shift.setUpdateDate(cursor.getString(13));
                shift.setUpdateTime(cursor.getString(14));
                shift.setUpdateUserNo(Integer.parseInt(cursor.getString(15)));
                shift.setUpdateUserName(cursor.getString(16));

                shifts.add(shift);

            } while (cursor.moveToNext());
        }
        return shifts;
    }

    public ArrayList<CustomerRegistrationModel> getAllCustomerRegistration() {
        ArrayList<CustomerRegistrationModel> customerRegistrationModels = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + CUSTOMER_REGISTRATION_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomerRegistrationModel customerRegistrationModel = new CustomerRegistrationModel();

                customerRegistrationModel.setMemberShipGroup(cursor.getString(0));
                customerRegistrationModel.setCustomerName(cursor.getString(1));
                customerRegistrationModel.setCustomerCode(cursor.getString(2));
                customerRegistrationModel.setMemberShipCardNO(cursor.getInt(3));
                customerRegistrationModel.setCustomerName(cursor.getString(4));
                customerRegistrationModel.setCoender(cursor.getString(5));
                customerRegistrationModel.setRemark(cursor.getString(6));
                customerRegistrationModel.setStreetNoName(cursor.getString(7));
                customerRegistrationModel.setCity(cursor.getString(8));
                customerRegistrationModel.setPhoneNo(cursor.getInt(9));
                customerRegistrationModel.setMobileNo(cursor.getInt(10));
                customerRegistrationModel.setNameShow(cursor.getString(11));
                customerRegistrationModel.setBirthday(cursor.getString(12));
                customerRegistrationModel.setAnniversary(cursor.getString(13));
                customerRegistrationModel.setOccupation(cursor.getString(14));
                customerRegistrationModel.setEmail(cursor.getString(15));
                customerRegistrationModel.setTotalPoint(cursor.getInt(16));
                customerRegistrationModel.setRedeemedPoint(cursor.getInt(17));
                customerRegistrationModel.setRemainingPoint(cursor.getInt(18));
                customerRegistrationModel.setShiftNO(cursor.getInt(19));
                customerRegistrationModel.setShiftName(cursor.getString(20));

                customerRegistrationModels.add(customerRegistrationModel);
            } while (cursor.moveToNext());
        }
        return customerRegistrationModels;
    }


    public ArrayList<EmployeeRegistrationModle> getAllEmployeeRegistration() {
        ArrayList<EmployeeRegistrationModle> employeeRegistrationModels = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + EMPLOYEE_REGISTRATION_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                EmployeeRegistrationModle employeeRegistrationModels1 = new EmployeeRegistrationModle();

                employeeRegistrationModels1.setJobGroup(cursor.getString(0));
                employeeRegistrationModels1.setEmployeeName(cursor.getString(1));
                employeeRegistrationModels1.setEmployeeNO(cursor.getInt(2));
                employeeRegistrationModels1.setMobileNo(cursor.getInt(3));
                employeeRegistrationModels1.setSecurityLevel(cursor.getString(4));
                employeeRegistrationModels1.setUserPassword(cursor.getInt(5));
                employeeRegistrationModels1.setActive(cursor.getInt(6));
                employeeRegistrationModels1.setHireDate(cursor.getString(7));
                employeeRegistrationModels1.setTerminationDate(cursor.getString(8));
                employeeRegistrationModels1.setPayBasic(cursor.getString(9));
                employeeRegistrationModels1.setPayRate(cursor.getString(10));
                employeeRegistrationModels1.setHolidayPay(cursor.getString(11));
                employeeRegistrationModels1.setEmployeeType(cursor.getInt(12));
                employeeRegistrationModels1.setShiftNo(cursor.getInt(13));
                employeeRegistrationModels1.setShiftName(cursor.getString(14));

                employeeRegistrationModels.add(employeeRegistrationModels1);
            } while (cursor.moveToNext());
        }
        return employeeRegistrationModels;
    }

    public ArrayList<FamilyCategory> getAllFamilyCategory() {
        ArrayList<FamilyCategory> familyCategoryArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + FAMILY_CATEGORY_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FamilyCategory familyCategory = new FamilyCategory();

                familyCategory.setSerial(Integer.parseInt(cursor.getString(0)));
                familyCategory.setType(Integer.parseInt(cursor.getString(1)));
                familyCategory.setName(cursor.getString(2));

                if (cursor.getBlob(3).length == 0)
                    familyCategory.setCatPic(null);
                else
                    familyCategory.setCatPic(BitmapFactory.decodeByteArray(cursor.getBlob(3), 0, cursor.getBlob(3).length));


                familyCategoryArrayList.add(familyCategory);

            } while (cursor.moveToNext());
        }
        return familyCategoryArrayList;
    }

    public List<CancleOrder> getAllCanselOrder() {
        List<CancleOrder> items = new ArrayList<CancleOrder>();

        String selectQuery = "SELECT  * FROM " + CANCEL_ORDER;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CancleOrder cancleOrder = new CancleOrder();

                cancleOrder.setOrderNo(cursor.getString(0));
                cancleOrder.setTransDate(cursor.getString(1));
                cancleOrder.setUserName(cursor.getString(3));
                cancleOrder.setUserNo(cursor.getInt(2));
                cancleOrder.setShiftName(cursor.getString(4));
                cancleOrder.setShiftNo(cursor.getInt(5));
                cancleOrder.setWaiterName(cursor.getString(6));
                cancleOrder.setWaiterNo(cursor.getInt(7));
                cancleOrder.setItemCode(cursor.getString(8));
                cancleOrder.setItemName(cursor.getString(9));
                cancleOrder.setQty(Double.parseDouble(cursor.getString(10)));
                cancleOrder.setPrice(Double.parseDouble(cursor.getString(11)));
                cancleOrder.setTotal(Double.parseDouble(cursor.getString(12)));
                cancleOrder.setReason(cursor.getString(13));
                cancleOrder.setIsAllCancel(Integer.parseInt(cursor.getString(14)));
                cancleOrder.setTime(cursor.getString(15));
                cancleOrder.setPosNO(Integer.parseInt(cursor.getString(16)));

                items.add(cancleOrder);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public List<TableActions> getAllTableActions() {
        List<TableActions> items = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_ACTIONS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TableActions action = new TableActions();

                action.setPOSNumber(cursor.getInt(0));
                action.setUserNo(cursor.getInt(1));
                action.setUserName(cursor.getString(2));
                action.setShiftName(cursor.getString(3));
                action.setShiftNo(cursor.getInt(4));
                action.setActionType(cursor.getInt(5));
                action.setActionDate(cursor.getString(6));
                action.setActionTime(cursor.getString(7));
                action.setTableNo(cursor.getInt(8));
                action.setSectionNo(cursor.getInt(9));
                action.setToTable(cursor.getInt(10));
                action.setToSection(cursor.getInt(11));

                items.add(action);
            } while (cursor.moveToNext());
        }
        return items;
    }


    public ArrayList<Announcemet> getAllTableAnnouncement() {
        ArrayList<Announcemet> announcementArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + ANNOUNCEMENT_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Announcemet announcemet = new Announcemet();

                announcemet.setShiftName(cursor.getString(0));
                announcemet.setAnnouncementDate(cursor.getString(1));
                announcemet.setUserName(cursor.getString(2));
                announcemet.setPosNo(cursor.getInt(3));
                announcemet.setMessage(cursor.getString(4));
                announcemet.setIsShow(cursor.getInt(5));
                announcemet.setUserNo(cursor.getInt(6));

                announcementArrayList.add(announcemet);
            } while (cursor.moveToNext());
        }
        return announcementArrayList;
    }


    public ArrayList<KitchenScreen> getAllKitchenScreen() {
        ArrayList<KitchenScreen> kitchenScreens = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + KITCHEN_SCREEN_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                KitchenScreen kitchenScreen = new KitchenScreen();

                kitchenScreen.setKitchenName(cursor.getString(0));
                kitchenScreen.setKitchenNo(cursor.getInt(1));
                kitchenScreen.setKitchenIP(cursor.getString(2));


                kitchenScreens.add(kitchenScreen);
            } while (cursor.moveToNext());
        }
        return kitchenScreens;
    }


    public ArrayList<ZReport> getAllZReport() {
        ArrayList<ZReport> items = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Z_REPORT_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ZReport zReport = new ZReport();

                zReport.setDate(cursor.getString(0));
                zReport.setUserNo(cursor.getInt(1));
                zReport.setUserName(cursor.getString(2));
                zReport.setPosNo(cursor.getInt(3));
                zReport.setSerial(cursor.getInt(4));

                items.add(zReport);
            } while (cursor.moveToNext());
        }
        return items;
    }


    public ArrayList<OrderTransactions> getXReport(String shiftName, String PosNo, String fDate, String toDate) {
        ArrayList<OrderTransactions> orderTransactionsArrayList = new ArrayList<>();


        String selectQuery = "select ITEM_NAME, GROUP_CONCAT( VOUCHER_DATE), GROUP_CONCAT( TOTAL), GROUP_CONCAT(TAX_VLUE) , GROUP_CONCAT(TOTAL_DISCOUNT),GROUP_CONCAT(ORDER_KIND) from ORDER_TRANSACTIONS  " +
                "where  SHIFT_NAME =" + shiftName + " and POS_NO= " + PosNo + "  GROUP BY ITEM_BARCODE1";


        Log.e("se12", "" + selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderTransactions orderTransactions = new OrderTransactions();

                orderTransactions.setItemName(cursor.getString(0));
                orderTransactions.setVoucherDate(cursor.getString(1));

                orderTransactions.setTime(cursor.getString(2));
                orderTransactions.setShiftName(cursor.getString(3)); //con list of tax value
                orderTransactions.setUserName(cursor.getString(4));
                orderTransactions.setNote(cursor.getString(5));


                orderTransactionsArrayList.add(orderTransactions);

            } while (cursor.moveToNext());
        }
        return orderTransactionsArrayList;
    }


    public ArrayList<OrderHeader> getMarketReport(String fDate, String toDate) {
        ArrayList<OrderHeader> orderHeaders = new ArrayList<>();


        String selectQuery = "select POINT_OF_SALE_NUMBER, GROUP_CONCAT( TOTAL), GROUP_CONCAT(TOTAL_TAX),GROUP_CONCAT(AMOUNT_DUE) , GROUP_CONCAT( VOUCHER_DATE) , GROUP_CONCAT( ALL_DISCOUNT) from ORDER_HEADER  " +
                " GROUP BY POINT_OF_SALE_NUMBER";


        Log.e("se12", "" + selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderHeader orderHeaderModel = new OrderHeader();

                orderHeaderModel.setPointOfSaleNumber(cursor.getInt(0));
                orderHeaderModel.setTime(cursor.getString(1));
                orderHeaderModel.setShiftName(cursor.getString(2));
                orderHeaderModel.setUserName(cursor.getString(3));
                orderHeaderModel.setVoucherDate(cursor.getString(4));
                orderHeaderModel.setWaiter(cursor.getString(5));

                orderHeaders.add(orderHeaderModel);

            } while (cursor.moveToNext());
        }
        return orderHeaders;
    }


    public ArrayList<OrderTransactions> getXReportPercent(String shiftName, String PosNo, String fDate, String toDate) {
        ArrayList<OrderTransactions> orderTransactionsArrayList = new ArrayList<>();


        String selectQuery = "select TAX_PERC, GROUP_CONCAT( VOUCHER_DATE), GROUP_CONCAT(TOTAL), GROUP_CONCAT(TAX_VLUE), GROUP_CONCAT(TOTAL_DISCOUNT),GROUP_CONCAT(ORDER_KIND) from ORDER_TRANSACTIONS  " +
                "where  SHIFT_NAME =" + shiftName + " and POS_NO= " + PosNo + "  GROUP BY TAX_PERC";

        Log.e("se123", "" + selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderTransactions orderTransactions = new OrderTransactions();

                orderTransactions.setTaxPerc(Double.parseDouble(cursor.getString(0)));
                orderTransactions.setVoucherDate(cursor.getString(1));
                orderTransactions.setTime(cursor.getString(2));
                orderTransactions.setShiftName(cursor.getString(3));
                orderTransactions.setSecondaryName(cursor.getString(4));
                orderTransactions.setNote(cursor.getString(5));

                orderTransactionsArrayList.add(orderTransactions);

            } while (cursor.moveToNext());
        }

//        Log.e("orderTrans ::: ",""+orderTransactionsArrayList.get(0).getTime().toString()+"...."+orderTransactionsArrayList.get(0).getVoucherDate().toString());

        return orderTransactionsArrayList;
    }

    public ArrayList<OrderHeader> getUserNameReport(String userName, String PosNo) {
        ArrayList<OrderHeader> orderHeaderArrayList = new ArrayList<>();

        String selectQuery = "select USER_NAME , GROUP_CONCAT( VOUCHER_DATE), GROUP_CONCAT( AMOUNT_DUE) from ORDER_HEADER  " +
                "WHERE USER_NO = " + userName + " and POINT_OF_SALE_NUMBER= " + PosNo + " GROUP BY USER_NAME";

        Log.e("se123", "" + selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderHeader orderHeader = new OrderHeader();

                orderHeader.setUserName(cursor.getString(0));
                orderHeader.setVoucherDate(cursor.getString(1));
                orderHeader.setShiftName(cursor.getString(2));

                orderHeaderArrayList.add(orderHeader);

            } while (cursor.moveToNext());
        }

        Log.e("orderTrans ::: ", "" + orderHeaderArrayList.toString());

        return orderHeaderArrayList;
    }


    public ArrayList<VoidResons> getAllVoidReasons() {
        ArrayList<VoidResons> reasons = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + VOID_REASONS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                VoidResons reason = new VoidResons();
                reason.setShiftNo(Integer.parseInt(cursor.getString(0)));
                reason.setShiftName(cursor.getString(1));
                reason.setUserNo(Integer.parseInt(cursor.getString(2)));
                reason.setUserName(cursor.getString(3));
                reason.setVoidReason(cursor.getString(4));
                reason.setDate(cursor.getString(5));
                reason.setActiveated(Integer.parseInt(cursor.getString(6)));

                reasons.add(reason);

            } while (cursor.moveToNext());
        }
        return reasons;
    }

    public List<ItemWithScreen> getAllItemsWithScreen() {
        ArrayList<ItemWithScreen> items = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ITEM_WITH_SCREEN;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemWithScreen item = new ItemWithScreen();
                item.setItemCode(Integer.parseInt(cursor.getString(0)));
                item.setItemName(cursor.getString(1));
                item.setScreenNo(Integer.parseInt(cursor.getString(2)));
                item.setScreenName(cursor.getString(3));

                items.add(item);

            } while (cursor.moveToNext());
        }
        return items;
    }

    //Updating single record

    public void updateUsedCategories(UsedCategories usedCategories) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_NAME, usedCategories.getCategoryName());
        values.put(NUMBER_OF_ITEMS, usedCategories.getNumberOfItems());
        values.put(CATEGORY_BACKGROUND, usedCategories.getBackground());
        values.put(CATEGORY_TEXT_COLOR, usedCategories.getTextColor());
        values.put(CATEGORY_POSITION, usedCategories.getPosition());

        // updating row
        db.update(USED_CATEGORIES, values, CATEGORY_NAME + " = '" + usedCategories.getCategoryName() + "'", null);
    }

    public void updateAnnounementIsShow(String message, String date) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String filter = MESSAGE18 + " = '" + message + "' and " + ANNOUNCEMENT_DATE18 + "= '" + date + "'";
        values.put(IS_SHOW18, 1);

        // updating row
        db.update(ANNOUNCEMENT_TABLE, values, filter, null);
    }

    public void updateOrderTrancactionReturn(int Pos, String itemBarcode,String Vserial,String OrderKind, int returnQty) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String filter = POS_NO1 + " = '" + Pos + "' and " + ITEM_BARCODE1 + "= '" + itemBarcode +"' and " + VOUCHER_NO1 + "= '" + Vserial + "' and " + ORDER_KIND1 + "= '" + OrderKind +"'";
        values.put(RETURN_QTY1, returnQty);

        // updating row
        db.update(ORDER_TRANSACTIONS, values, filter, null);
    }

    public void updateStatusInBlindShiftIn(String userName, String date) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String filter = USER_NAME10 + " = '" + userName + "' and " + DATE10 + "= '" + date + "'";
        values.put(STATUS10, 0);

        // updating row
        db.update(BLIND_SHIFT_IN, values, filter, null);
    }


    public void updateBlindCloseDetails(int transNo, String catName, int catQty, double catValue, double catTotal, String date,
                                        String time, int userNo, String userName) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CAT_QTY12, catQty);
        values.put(CAT_TOTAL12, catTotal);
        values.put(CAT_VALUE12, catValue);
        values.put(UPDATE_DATE12, date);
        values.put(UPDATE_TIME12, time);
        values.put(UPDATE_USER_NO12, userNo);
        values.put(UPDATE_USER_NAME12, userName);

        // updating row
        db.update(BLIND_CLOSE_DETAILS, values, "TRANS_NO = '" + transNo + "' and CAT_NAME = '" + catName + "'", null);
    }

    public void updateBlindClose(int transNo, double physical, double diff, double otherPayments, double diff2) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_SALES11, physical);
        values.put(SALES_DIFF11, diff);
        values.put(USER_OTHER_PAYMENTS11, otherPayments);
        values.put(OTHER_PAYMENTS_DIFF11, diff2);

        // updating row
        db.update(BLIND_CLOSE, values, "TRANS_NO = '" + transNo + "'", null);
    }

    public void updateBlindCloseTillOk(int transNo) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TILL_OK11, 1);

        // updating row
        db.update(BLIND_CLOSE, values, "TRANS_NO = '" + transNo + "'", null);
    }

    public void updateBlindCloseReason(int transNo, String reason) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(REASON11, reason);

        // updating row
        db.update(BLIND_CLOSE, values, "TRANS_NO = '" + transNo + "'", null);
    }

    public void updateUsedItems(String catName, String itemName, int backColor, int fontColor, int position) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(ITEM_BACKGROUND, backColor);
        values.put(ITEM_TEXT_COLOR, fontColor);

        db.update(USED_ITEMS, values, "CATEGORY_NAME2 = '" + catName + "' and ITEM_POSITION = '" + position + "'", null);
    }

    public void moveTablesTemp(int oldSectionNo, int oldTableNo, int sectionNo, int tableNo) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();

        values.put(SECTION_NO1, sectionNo);
        values.put(TABLE_NO1, tableNo);

        values2.put(SECTION_NUMBER2, sectionNo);
        values2.put(TABLE_NUMBER2, tableNo);

        db.update(ORDER_TRANSACTIONS_TEMP, values, SECTION_NO1 + " = '" + oldSectionNo + "' and " + TABLE_NO1 + " = '" + oldTableNo + "'", null);
        db.update(ORDER_HEADER_TEMP, values2, SECTION_NUMBER2 + " = '" + oldSectionNo + "' and " + TABLE_NUMBER2 + " = '" + oldTableNo + "'", null);
    }

    public void mergeTablesTemp(int oldSectionNo, int oldTableNo, int sectionNo, int tableNo) {
        db = this.getWritableDatabase();

        String selectQuery = "SELECT VOUCHER_SERIAL , VOUCHER_NO FROM " + ORDER_TRANSACTIONS_TEMP + " WHERE SECTION_NO = '" + sectionNo + "' and TABLE_NO = '" + tableNo + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String voucherSerial = "", voucherNo = "";
        if (cursor.moveToFirst()) {
            voucherSerial = cursor.getString(0);
            voucherNo = cursor.getString(1);
        }
        ContentValues values = new ContentValues();

        values.put(SECTION_NO1, sectionNo);
        values.put(TABLE_NO1, tableNo);
        values.put(VOUCHER_NO1, voucherNo);
        values.put(VOUCHER_SERIAL1, voucherSerial);

        db.update(ORDER_TRANSACTIONS_TEMP, values, SECTION_NO1 + " = '" + oldSectionNo + "' and " + TABLE_NO1 + " = '" + oldTableNo + "'", null);
        //______________________________________________________

        String selectQuery2 = "SELECT TOTAL , TOTAL_DISCOUNT , TOTAL_LINE_DISCOUNT , ALL_DISCOUNT  , SUB_TOTAL , AMOUNT_DUE " +
                "FROM " + ORDER_HEADER_TEMP + " WHERE SECTION_NUMBER = '" + oldSectionNo + "' and TABLE_NUMBER = '" + oldTableNo + "'";

        String selectQuery3 = "SELECT TOTAL , TOTAL_DISCOUNT , TOTAL_LINE_DISCOUNT , ALL_DISCOUNT  , SUB_TOTAL , AMOUNT_DUE " +
                "FROM " + ORDER_HEADER_TEMP + " WHERE SECTION_NUMBER = '" + sectionNo + "' and TABLE_NUMBER = '" + tableNo + "'";

        db = this.getWritableDatabase();
        Cursor cursor2 = db.rawQuery(selectQuery2, null);
        Cursor cursor3 = db.rawQuery(selectQuery3, null);

        Double total = 0.0, totalDis = 0.0, totalLineDis = 0.0, allDis = 0.0, subTotal = 0.0, amountDue = 0.0;
        if (cursor2.moveToFirst()) {
            total = cursor2.getDouble(0);
            totalDis = cursor2.getDouble(1);
            totalLineDis = cursor2.getDouble(2);
            allDis = cursor2.getDouble(3);
            subTotal = cursor2.getDouble(4);
            amountDue = cursor2.getDouble(5);
        }
        if (cursor3.moveToFirst()) {
            total += cursor3.getDouble(0);
            totalDis += cursor3.getDouble(1);
            totalLineDis += cursor3.getDouble(2);
            allDis += cursor3.getDouble(3);
            subTotal += cursor3.getDouble(4);
            amountDue += cursor3.getDouble(5);
        }
        ContentValues values2 = new ContentValues();
        values2.put(TOTAL2, total);
        values2.put(TOTAL_DISCOUNT2, totalDis);
        values2.put(TOTAL_LINE_DISCOUNT2, totalLineDis);
        values2.put(ALL_DISCOUNT2, allDis);
        values2.put(SUB_TOTAL2, subTotal);
        values2.put(AMOUNT_DUE2, amountDue);

        db.update(ORDER_HEADER_TEMP, values2, SECTION_NUMBER2 + " = '" + sectionNo + "' and " + TABLE_NUMBER2 + " = '" + tableNo + "'", null);
        db.execSQL("delete from " + ORDER_HEADER_TEMP +
                " where " + SECTION_NUMBER2 + " = '" + oldSectionNo + "' and " + TABLE_NUMBER2 + " = '" + oldTableNo + "'");
    }

    public void deleteAllUsedCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + USED_CATEGORIES);
        db.close();
    }


    public void deleteAllMoneyCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + MONEY_CATEGORIES);
        db.close();
    }

    public void deleteUsedItems(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from USED_ITEMS where CATEGORY_NAME2 = '" + categoryName + "'");
        db.close();
    }

    public void deleteCategory(String categorySerial) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from FAMILY_CATEGORY_TABLE where SERIAL = '" + categorySerial + "'");
        db.close();
    }

    public void deleteFromOrderHeaderTemp(String sectionNo, String tableNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from ORDER_HEADER_TEMP WHERE SECTION_NUMBER = '" + sectionNo + "' and TABLE_NUMBER = '" + tableNo + "'");
        db.close();
    }

    public void deleteFromOrderTransactionTemp(String sectionNo, String tableNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from ORDER_TRANSACTIONS_TEMP WHERE SECTION_NO = '" + sectionNo + "' and TABLE_NO = '" + tableNo + "'");
        db.close();
    }

    public void deleteFromOrderTransactionTemp2(String sectionNo, String tableNo, int itemCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from ORDER_TRANSACTIONS_TEMP WHERE SECTION_NO = '" + sectionNo + "' and TABLE_NO = '" + tableNo + "' and ITEM_BARCODE1 = '" + itemCode + "'");
        db.close();
    }

    public void deleteModifierAndForce(int itemCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from ITEM_WITH_FQ WHERE ITEM_CODE = '" + itemCode + "'");
        db.execSQL("delete from ITEM_WITH_MODIFIER WHERE ITEM_CODE = '" + itemCode + "'");
        db.close();
    }

    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLES);
        db.close();
    }

    public void deleteAllOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + ORDER_HEADER_TEMP);
        db.execSQL("delete from " + ORDER_TRANSACTIONS_TEMP);
        db.execSQL("delete from " + ORDER_HEADER);
        db.execSQL("delete from " + ORDER_TRANSACTIONS);
        db.close();
    }

    public void deleteAllVoidReasons() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + VOID_REASONS);
        db.close();
    }

}
