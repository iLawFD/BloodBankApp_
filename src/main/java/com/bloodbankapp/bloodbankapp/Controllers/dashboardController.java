package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            //getting the number
            donationPerMonthText.setText(String.valueOf(DataBase.getDataBase().getDonationCountForCurrentMonth()));
            donationPerWeekText.setText(String.valueOf(DataBase.getDataBase().getDonationCountForCurrentWeek()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    protected void goback(ActionEvent event) throws IOException, SQLException {


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/cop2.fxml"));

        Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
        stage.setScene(scene2);
    }


    @FXML
    private BarChart<String,Number> blood_type_barChart;

    @FXML
    private Label donationPerWeekText;

    @FXML
    private Label donationPerMonthText;



}
