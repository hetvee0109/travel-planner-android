package com.example.travelplanner;

public class Trip {
    public int id;
    public String destination;
    public String startDate;
    public String endDate;

    // Empty constructor
    public Trip() {}

    // Constructor with parameters
    public Trip(int id, String destination, String startDate, String endDate) {
        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}