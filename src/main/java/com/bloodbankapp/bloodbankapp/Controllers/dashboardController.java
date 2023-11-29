package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

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
    private BarChart<String,Number> blood_type_barChart;

    @FXML
    private Label donationPerWeekText;

    @FXML
    private Label donationPerMonthText;



}
