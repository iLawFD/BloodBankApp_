package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class adminRequestController {

    @FXML
    private TextField bloodTypeText;

    @FXML
    void confirm(ActionEvent event) {
        // do the logic here

        try {
            DataBase.getDataBase().initiateNewBloodDrive(bloodTypeText.getText());
            ((Node)(event.getSource())).getScene().getWindow().hide();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
