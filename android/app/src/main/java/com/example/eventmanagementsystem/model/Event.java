package com.example.eventmanagementsystem.model;

public class Event {
    private int id;
    private String eventName;
    private String eventType;
    private String eventVenue;
    private String eventDate;
    private String eventStime;
    private String eventEtime;
    private String eventDescription;
    private String dateItemAdded;
    private int sync_status;

    @Override
    public String toString() {
        return id +
                "," + eventName +
                "," + eventType +
                ", " + eventVenue +
                ", " + eventDate +
                ", " + eventStime +
                ", " + eventEtime +
                ", " + eventDescription;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    public Event(){

    }
    public Event(int id, String eventName, String eventType, String eventVenue, String eventDate, String eventStime, String eventEtime, String eventDescription, String dateItemAdded,int sync_status) {
        this.id = id;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventVenue = eventVenue;
        this.eventDate = eventDate;
        this.eventStime = eventStime;
        this.eventEtime = eventEtime;
        this.eventDescription = eventDescription;
        this.dateItemAdded = dateItemAdded;
        this.sync_status=sync_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventStime() {
        return eventStime;
    }

    public void setEventStime(String eventStime) {
        this.eventStime = eventStime;
    }

    public String getEventEtime() {
        return eventEtime;
    }

    public void setEventEtime(String eventEtime) {
        this.eventEtime = eventEtime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }
}
