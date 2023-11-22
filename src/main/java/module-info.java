module com.bloodbankapp.bloodbankapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires courier.java;
    requires java.sql;


    opens com.bloodbankapp.bloodbankapp to javafx.fxml;
    exports com.bloodbankapp.bloodbankapp;
    exports com.bloodbankapp.bloodbankapp.Controllers;
    opens com.bloodbankapp.bloodbankapp.Controllers to javafx.fxml;
}