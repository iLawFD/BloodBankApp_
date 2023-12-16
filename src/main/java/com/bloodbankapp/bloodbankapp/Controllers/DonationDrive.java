package com.bloodbankapp.bloodbankapp.Controllers;

public class DonationDrive {
    int driveID;
    int count;

    public DonationDrive(int driveID, int count) {
        this.driveID = driveID;
        this.count = count;
    }

    public int getDriveID() {
        return driveID;
    }

    public void setDriveID(int driveID) {
        this.driveID = driveID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
