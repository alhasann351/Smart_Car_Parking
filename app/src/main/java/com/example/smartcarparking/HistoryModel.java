package com.example.smartcarparking;

public class HistoryModel {
    private String name, carNumber, rfidNumber, bookedTime, payAmount, slot;

    public HistoryModel() {
    }

    public HistoryModel(String name, String carNumber, String rfidNumber, String bookedTime, String payAmount, String slot) {
        this.name = name;
        this.carNumber = carNumber;
        this.rfidNumber = rfidNumber;
        this.bookedTime = bookedTime;
        this.payAmount = payAmount;
        this.slot = slot;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public String getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(String bookedTime) {
        this.bookedTime = bookedTime;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

}
