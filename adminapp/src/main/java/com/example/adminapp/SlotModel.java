package com.example.adminapp;

public class SlotModel {
    private String id , slotName, status;

    public SlotModel() {
    }

    public SlotModel(String id, String slotName, String status) {
        this.slotName = slotName;
        this.status = status;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
