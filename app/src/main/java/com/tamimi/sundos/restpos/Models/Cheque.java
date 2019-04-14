package com.tamimi.sundos.restpos.Models;

public class Cheque {

    public int serialCheque;
    public String bankName;
    public double received;
    public int chequeNumber;

    public Cheque() {

    }

    public Cheque(int serialCheque, String bankName, double received, int chequeNumber) {
        this.serialCheque = serialCheque;
        this.bankName = bankName;
        this.received = received;
        this.chequeNumber = chequeNumber;
    }

    public int getSerialCheque() {
        return serialCheque;
    }

    public void setSerialCheque(int serialCheque) {
        this.serialCheque = serialCheque;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public double getReceived() {
        return received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public int getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(int chequeNumber) {
        this.chequeNumber = chequeNumber;
    }
}
