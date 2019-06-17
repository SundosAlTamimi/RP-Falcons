package com.tamimi.sundos.restpos.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Modifier {
    private String modifierName;
    private int modifierNumber;
    private int modifierActive;

    public Modifier() {

    }

    public Modifier(String modifierName, int modifierNumber, int modifierActive) {
        this.modifierName = modifierName;
        this.modifierNumber = modifierNumber;
        this.modifierActive = modifierActive;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public void setModifierNumber(int modifierNumber) {
        this.modifierNumber = modifierNumber;
    }

    public void setModifierActive(int active) {
        this.modifierActive = active;
    }

    public String getModifierName() {

        return modifierName;
    }

    public int getModifierNumber() {
        return modifierNumber;
    }

    public int getModifierActive() {
        return modifierActive;
    }

    public JSONObject getJSONObject() { // for kitchen
        JSONObject obj = new JSONObject();
        try {
            obj.put("MODIFIER_NO", modifierNumber);
            obj.put("MODIFIER_NAME", modifierName);
            obj.put("ACTIVE", modifierActive);

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
        return obj;
    }
}
