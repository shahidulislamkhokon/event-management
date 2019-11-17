package com.example.eventmanager.models;

import java.io.Serializable;

public class Event implements Serializable {
    private String key, eventName, location, startingDate, description, saveEventImage;
    private int timeSpinnerposition;

    public Event() {
    }

    public Event(String eventName, String location, String startingDate,int timeSpinnerposition,String description, String saveEventImage) {
        this.eventName = eventName;
        this.description = description;
        this.location=location;
        this.startingDate=startingDate;
        this.saveEventImage=saveEventImage;
        this.timeSpinnerposition=timeSpinnerposition;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public int getTimeSpinnerposition() {
        return timeSpinnerposition;
    }

    public void setTimeSpinnerposition(int timeSpinnerposition) {
        this.timeSpinnerposition = timeSpinnerposition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSaveEventImage() {
        return saveEventImage;
    }

    public void setSaveEventImage(String saveEventImage) {
        this.saveEventImage = saveEventImage;
    }
}
