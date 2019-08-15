package com.tamimi.sundos.restpos.Models;

public class TakeAway {

    private int serial;
    private String TANmae;

    public TakeAway() {

    }

    public TakeAway(int serial, String TANmae) {
        this.serial = serial;
        this.TANmae = TANmae;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getTANmae() {
        return TANmae;
    }

    public void setTANmae(String TANmae) {
        this.TANmae = TANmae;
    }
}
