package com.bloodbankapp.bloodbankapp.Controllers;

import java.util.Date;

public class userBloodRequest {

    int requestID;
    String requestStatus;
    Date requestDate;

    String bloodType;
    int cost;
    String paymentStatus;
    int paymentID;

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public userBloodRequest(int requestID, String requestStatus, Date requestDate, String bloodType, int cost, String paymentStatus, int paymentID) {

        this.requestID = requestID;
        this.requestStatus = requestStatus;
        this.requestDate = requestDate;
        this.bloodType = bloodType;
        this.cost = cost;
        this.paymentStatus = paymentStatus;
        this.paymentID = paymentID;
    }
}
