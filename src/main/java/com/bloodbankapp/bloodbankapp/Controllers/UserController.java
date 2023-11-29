package com.bloodbankapp.bloodbankapp.Controllers;
import com.bloodbankapp.bloodbankapp.database.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;

public class UserController {


    @FXML
    TextField text;

    @FXML
    protected void reports(ActionEvent event) throws IOException, SQLException {


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/dashboard.fxml"));

        Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
        stage.setScene(scene2);
    }

    @FXML
    protected void goback(ActionEvent event) throws IOException, SQLException {


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/cop2.fxml"));

        Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
        stage.setScene(scene2);
    }









}
