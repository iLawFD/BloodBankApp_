package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class dashboardController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            //getting the number
            loadPayments();
            donationPerMonthText.setText(String.valueOf(DataBase.getDataBase().getDonationCountForCurrentMonth()));
            donationPerWeekText.setText(String.valueOf(DataBase.getDataBase().getDonationCountForCurrentWeek()));
            Map<String, Integer>  res = DataBase.getDataBase().getBloodDonationStatisticsBloodType();


            XYChart.Series<String, Number> series = new XYChart.Series<>();

            for (Map.Entry<String, Integer> entry : res.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }






            // Add the series to the chart
            blood_type_barChart.getData().add(series);
            loadDrive();



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    private TableColumn<Payment, Integer> ID;

    @FXML
    private TableColumn<Payment, Integer> amount;

    @FXML
    private TableColumn<Payment, Integer> status;

    @FXML
    private TableView<Payment> paymentTable;


    @FXML
    protected void goback(ActionEvent event) throws IOException, SQLException {

        if(DataBase.getDataBase().getCurrentSystemUser() != null){

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/cop2.fxml"));

            Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
            stage.setScene(scene2);
        }else{
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/browseScene.fxml"));

            Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
            stage.setScene(scene2);
        }

    }


    @FXML
    private BarChart<String,Number> blood_type_barChart;

    @FXML
    private Label donationPerWeekText;

    @FXML
    private Label donationPerMonthText;
    @FXML
    private TableColumn<DonationDrive, Integer> driveID;
    @FXML
    private TableColumn<DonationDrive, Integer> count;

    @FXML
    private TableView<DonationDrive> donationDriveTable;




    public void loadPayments(){
        ArrayList<Payment> paymentArrayList;
        try {
            paymentArrayList = DataBase.getDataBase().getPayment();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        amount.setCellValueFactory(new PropertyValueFactory<Payment,Integer>("amount"));
        ID.setCellValueFactory(new PropertyValueFactory<Payment,Integer>("id"));
        status.setCellValueFactory(new PropertyValueFactory<Payment,Integer>("status"));



        ObservableList<Payment> bloodRequestObservableList = FXCollections.observableArrayList(paymentArrayList);
        paymentTable.getSelectionModel().getSelectedItem();
        paymentTable.setItems(bloodRequestObservableList);


    }


    private void loadDrive(){

        ArrayList<DonationDrive> paymentArrayList;
        try {
            paymentArrayList = DataBase.getDataBase().getDonationDrives();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        count.setCellValueFactory(new PropertyValueFactory<DonationDrive,Integer>("count"));
        driveID.setCellValueFactory(new PropertyValueFactory<DonationDrive,Integer>("driveID"));



        ObservableList<DonationDrive> bloodRequestObservableList = FXCollections.observableArrayList(paymentArrayList);
        donationDriveTable.getSelectionModel().getSelectedItem();
        donationDriveTable.setItems(bloodRequestObservableList);

    }



}
