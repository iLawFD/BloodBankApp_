package com.bloodbankapp.bloodbankapp.Controllers;

public class SystemUser extends Person {
    String bloodType;
    String medicalHistory;
    public SystemUser(int ID, String firstName, String lastName, String address, String phone_number, String email) {
        super(ID, firstName, lastName, address, phone_number, email);

    }

    public SystemUser(int ID, String firstName, String lastName, String address, String phone_number, String email,String bloodType, String medicalHistory) {
        super(ID, firstName, lastName, address, phone_number, email);
        this.bloodType = bloodType;
        this.medicalHistory = medicalHistory;

    }

    public SystemUser() {
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }
}
