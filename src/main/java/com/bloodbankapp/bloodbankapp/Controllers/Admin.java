package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.Controllers.Person;

public class Admin extends Person {
    private String Office_number;

    public String getOffice_number() {
        return Office_number;
    }

    public Admin(int id,String firstName,String lastName, String address,String phoneNumber,String email) {
        super(id,firstName,lastName,address,phoneNumber,email);
    }

    public Admin(){

    }

    public void setOffice_number(String office_number) {
        Office_number = office_number;
    }

}
