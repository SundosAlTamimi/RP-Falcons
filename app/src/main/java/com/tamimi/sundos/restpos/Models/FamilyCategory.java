package com.tamimi.sundos.restpos.Models;

public class FamilyCategory {

    public int serial;
    public int type;
    public String name;

    public FamilyCategory() {

    }

    public FamilyCategory(int serial, int type, String name) {
        this.serial = serial;
        this.type = type;
        this.name = name;
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
}
