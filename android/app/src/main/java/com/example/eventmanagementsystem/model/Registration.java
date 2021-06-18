package com.example.eventmanagementsystem.model;

public class Registration {
    private int registrationID;
    private int eventID;
    private String rName;
    private String rRollNumber;
    private String rDepartment;
    private String rContact;
    private String rStatus;

    @Override
    public String toString() {
        return  registrationID +
                "," + eventID +
                "," + rName +
                "," + rRollNumber +
                "," + rDepartment +
                "," + rContact +
                "," + rStatus ;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    private int sync_status;

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    private String dateItemAdded;
    public Registration(){

    }
    public Registration(int registrationID, int eventID, String rName, String rRollNumber, String rDepartment, String rContact, String rStatus, String dateItemAdded,int sync_status) {
        this.registrationID = registrationID;
        this.eventID = eventID;
        this.rName = rName;
        this.rRollNumber = rRollNumber;
        this.rDepartment = rDepartment;
        this.rContact = rContact;
        this.rStatus = rStatus;
        this.dateItemAdded = dateItemAdded;
        this.sync_status=sync_status;
    }

    public int getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrRollNumber() {
        return rRollNumber;
    }

    public void setrRollNumber(String rRollNumber) {
        this.rRollNumber = rRollNumber;
    }

    public String getrDepartment() {
        return rDepartment;
    }

    public void setrDepartment(String rDepartment) {
        this.rDepartment = rDepartment;
    }

    public String getrContact() {
        return rContact;
    }

    public void setrContact(String rContact) {
        this.rContact = rContact;
    }

    public String getrStatus() {
        return rStatus;
    }

    public void setrStatus(String rStatus) {
        this.rStatus = rStatus;
    }
}
