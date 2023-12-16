package com.bloodbankapp.bloodbankapp.Controllers;

public class Payment {
//
//    int amount  = resultSet.getInt("amount");
//    String status = resultSet.getString("status");
//    int id = resultSet.getInt("payment_id");
    int amount;
    String status;
    int id;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Payment(int amount, String status, int id) {
        this.amount = amount;
        this.status = status;
        this.id = id;
    }
}
