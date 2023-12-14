package com.bloodbankapp.bloodbankapp.Controllers;

import java.util.Date;

public class Donation {
    private int donationID;
    private String donationStatus;
    private int requestID;
    private Date donationDate;

    private String bloodType;
    private  int bloodDriveNumber;

    public void setDonationID(int donationID) {
        this.donationID = donationID;
    }

    public void setDonationStatus(String donationStatus) {
        this.donationStatus = donationStatus;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public void setDonationDate(Date donationDate) {
        this.donationDate = donationDate;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setBloodDriveNumber(int bloodDriveNumber) {
        this.bloodDriveNumber = bloodDriveNumber;
    }

    public Donation(int donationID, String donationStatus, int requestID, Date donationDate, String bloodType, int bloodDriveNumber) {
        this.donationID = donationID;
        this.donationStatus = donationStatus;
        this.requestID = requestID;
        this.donationDate = donationDate;
        this.bloodType = bloodType;
        this.bloodDriveNumber = bloodDriveNumber;
    }

    public int getDonationID() {
        return donationID;
    }

    public String getDonationStatus() {
        return donationStatus;
    }

    public int getRequestID() {
        return requestID;
    }

    public Date getDonationDate() {
        return donationDate;
    }

    public String getBloodType() {
        return bloodType;
    }

    public int getBloodDriveNumber() {
        return bloodDriveNumber;
    }
}
