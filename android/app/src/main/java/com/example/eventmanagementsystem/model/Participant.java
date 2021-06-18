package com.example.eventmanagementsystem.model;

public class Participant {
    private int participantID;
    private int eventID;
    private String participantName;
    private String participantRollNumber;
    private String participantDepartment;
    private String participantContact;
    private String participantRole;
    private String dateItemAdded;

    @Override
    public String toString() {
        return participantID +
                "," + eventID +
                ", " + participantName  +
                ", " + participantRollNumber +
                "," + participantDepartment +
                ", " + participantContact +
                ", " + participantRole ;
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

    public Participant(){

    }
    public Participant(int participantID, int eventID, String participantName, String participantRollNumber, String participantDepartment, String participantContact, String participantRole,int sync_status) {
        this.participantID = participantID;
        this.eventID = eventID;
        this.participantName = participantName;
        this.participantRollNumber = participantRollNumber;
        this.participantDepartment = participantDepartment;
        this.participantContact = participantContact;
        this.participantRole = participantRole;
        this.sync_status=sync_status;
    }

    public int getParticipantID() {
        return participantID;
    }

    public void setParticipantID(int participantID) {
        this.participantID = participantID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantRollNumber() {
        return participantRollNumber;
    }

    public void setParticipantRollNumber(String participantRollNumber) {
        this.participantRollNumber = participantRollNumber;
    }

    public String getParticipantDepartment() {
        return participantDepartment;
    }

    public void setParticipantDepartment(String participantDepartment) {
        this.participantDepartment = participantDepartment;
    }

    public String getParticipantContact() {
        return participantContact;
    }

    public void setParticipantContact(String participantContact) {
        this.participantContact = participantContact;
    }

    public String getParticipantRole() {
        return participantRole;
    }

    public void setParticipantRole(String participantRole) {
        this.participantRole = participantRole;
    }
}
