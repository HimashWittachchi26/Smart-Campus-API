package com.westminster.models;

import java.util.UUID;

public class SensorReading {

    private String id;
    private long timestamp;
    private double value;

    // Constructor
    public SensorReading() {
    }

    // This method sets id and timestamp automatically
    public void initialise() {
        this.id = UUID.randomUUID().toString();   // generate unique ID
        this.timestamp = System.currentTimeMillis(); // current time
    }

    // Get ID
    public String getId() {
        return id;
    }

    // Set ID
    public void setId(String id) {
        this.id = id;
    }

    // Get Timestamp
    public long getTimestamp() {
        return timestamp;
    }

    // Set Timestamp
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Get Value
    public double getValue() {
        return value;
    }

    // Set Value
    public void setValue(double value) {
        this.value = value;
    }
}