package com.tamimi.sundos.restpos.Models;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FamilyCategory {

    public int serial;
    public int type;
    public String name;
    public Bitmap catPic;

    public FamilyCategory() {

    }

    public FamilyCategory(int serial, int type, String name, Bitmap catPic) {
        this.serial = serial;
        this.type = type;
        this.name = name;
        this.catPic = catPic;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getCatPic() {
        return catPic;
    }

    public void setCatPic(Bitmap catPic) {
        this.catPic = catPic;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("SERIAL", serial);
            obj.put("ITYPE", type);
            obj.put("NAME_CATEGORY_FAMILY", name);
//            obj.put("CATEGORY_PIC", catPic);

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
        return obj;
    }
}
