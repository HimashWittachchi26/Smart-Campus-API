package com.westminster.models;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private String id;
    private String name;
    private int capacity;
    private List<String> sensorIds = new ArrayList<>();

    // Constructor
    public Room() {
    }

    // Get ID
    public String getId() {
        return id;
    }

    // Set ID
    public void setId(String id) {
        this.id = id;
    }

    // Get Name
    public String getName() {
        return name;
    }

    // Set Name
    public void setName(String name) {
        this.name = name;
    }

    // Get Capacity
    public int getCapacity() {
        return capacity;
    }

    // Set Capacity
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Get Sensor IDs
    public List<String> getSensorIds() {
        return sensorIds;
    }

    // Set Sensor IDs
    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = sensorIds;
    }
}