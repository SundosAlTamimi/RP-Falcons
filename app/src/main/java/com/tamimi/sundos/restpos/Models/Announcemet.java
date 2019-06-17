package com.tamimi.sundos.restpos.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Announcemet {

    private String shiftName;
    private String AnnouncementDate;
    private String userName;
    private int posNo;
    private String message;
    private int  isShow;
    private int userNo;

    public Announcemet() {

    }

    public Announcemet(String shiftName, String
            announcementDate, String userName, int posNo, String message, int isShow,int userNo) {
        this.shiftName = shiftName;
        this.AnnouncementDate = announcementDate;
        this.userName = userName;
        this.posNo = posNo;
        this.message = message;
        this.isShow = isShow;
        this.userNo = userNo;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getAnnouncementDate() {
        return AnnouncementDate;
    }

    public void setAnnouncementDate(String announcementDate) {
        AnnouncementDate = announcementDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPosNo() {
        return posNo;
    }

    public void setPosNo(int posNo) {
        this.posNo = posNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public JSONObject getJSONObject() { // for kitchen
        JSONObject obj = new JSONObject();
        try {
            obj.put("SHIFT_NAME", shiftName);
            obj.put("USER_NAME", userName);
            obj.put("POS_NO", posNo);
            obj.put("MESSAGE", message);
            obj.put("IS_SHOW", isShow);
            obj.put("ANNOUNC_DATE", AnnouncementDate);

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
        return obj;
    }
}
