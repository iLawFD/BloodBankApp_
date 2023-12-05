package com.bloodbankapp.bloodbankapp.Controllers;

public class Admin extends Person {
    private int office_number;

    public int getOffice_number() {
        return office_number;
    }

    public Admin(int id,String firstName,String lastName, String address,String phoneNumber,String email,int office_number) {
        super(id,firstName,lastName,address,phoneNumber,email);
        this.office_number = office_number;

    }

    public Admin(){

    }


    @Override
    public String toString() {
        return super.toString() + "Admin{" +
                "office_number=" + office_number +
                '}';
    }

    public void setOffice_number(int office_number) {
        this.office_number = office_number;
    }

}
