package com.bloodbankapp.bloodbankapp.Controllers;

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

    public Person(int ID, String firstName, String lastName, String address, String phone_number, String email) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
    }

    public  Person(){}

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "Person{" +
                "ID=" + ID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
