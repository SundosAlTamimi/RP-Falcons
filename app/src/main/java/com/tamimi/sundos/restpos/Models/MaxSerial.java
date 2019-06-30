package com.tamimi.sundos.restpos.Models;

public class MaxSerial {

    public String maxSerial;
    public String maxSerialRefund;


    public MaxSerial() {

    }

    public MaxSerial(String maxSerial, String maxSerialRefund) {
        this.maxSerial = maxSerial;
        this.maxSerialRefund = maxSerialRefund;
    }

    public String getMaxSerial() {
        return maxSerial;
    }

    public void setMaxSerial(String maxSerial) {
        this.maxSerial = maxSerial;
    }

    public String getMaxSerialRefund() {
        return maxSerialRefund;
    }

    public void setMaxSerialRefund(String maxSerialRefund) {
        this.maxSerialRefund = maxSerialRefund;
    }
}
