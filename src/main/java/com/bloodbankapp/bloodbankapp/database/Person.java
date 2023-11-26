package com.bloodbankapp.bloodbankapp.database;

abstract public class Person {
    private int ID;
    private  String firstName;
    private  String lastName;
    private String address;

    private String phone_number;
    private  String email;

    public int getID() {
        return ID;
    }

    private  String personType;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getPersonType() {
        return personType;
    }
}
