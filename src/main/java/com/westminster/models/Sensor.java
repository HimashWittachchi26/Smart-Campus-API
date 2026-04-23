package com.westminster.models;

public class Sensor {

    private String id;
    private String type;
    private String status;
    private double currentValue;
    private String roomId;

    // Constructor
    public Sensor() {
    }

    // Get ID
    public String getId() {
        return id;
    }

    // Set ID
    public void setId(String id) {
        this.id = id;
    }

    // Get Type
    public String getType() {
        return type;
    }

    // Set Type
    public void setType(String type) {
        this.type = type;
    }

    // Get Status
    public String getStatus() {
        return status;
    }

    // Set Status
    public void setStatus(String status) {
        this.status = status;
    }

    // Get Current Value
    public double getCurrentValue() {
        return currentValue;
    }

    // Set Current Value
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    // Get Room ID
    public String getRoomId() {
        return roomId;
    }

    // Set Room ID
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}