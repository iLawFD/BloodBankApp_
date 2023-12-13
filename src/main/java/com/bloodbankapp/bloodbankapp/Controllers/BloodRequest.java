package com.bloodbankapp.bloodbankapp.Controllers;

import java.util.Date;

public class BloodRequest {

    int requestID;
    Date date;
    String bloodType;
    int ID;


    public BloodRequest(int requestID, Date date, String bloodType, int ID) {
        this.requestID = requestID;
        this.date = date;
        this.bloodType = bloodType;
        this.ID = ID;
    }

    public int getRequestID() {
        return requestID;
    }

    public Date getDate() {
        return date;
    }

    public String getBloodType() {
        return bloodType;
    }

    public int getID() {
        return ID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    @Override
    public String toString() {
        return "BloodRequest{" +
                "requestID=" + requestID +
                ", date=" + date +
                ", bloodType='" + bloodType + '\'' +
                ", ID=" + ID +
                '}';
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
