package com.example.eventmanager.models;

import java.io.Serializable;

public class Event implements Serializable {
    private String eventName, location, startingDate, timeSpinner, description;

    public Event() {
    }

    public Event(String eventName, String location, String startingDate,String timeSpinner,String description) {
        this.eventName = eventName;
        this.description = description;
        this.location=location;
        this.startingDate=startingDate;
        this.timeSpinner=timeSpinner;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String eventName) {
        this.location=location;
    }

    public String getStartingDate() {
        return startingDate;
    }


    public void setStartingDate(String description) {
        this.startingDate=startingDate;
    }

    public String getTimeSpinner() {
        return timeSpinner;
    }


    public void setTimeSpinner(String description) {
        this.timeSpinner = timeSpinner;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }
}
