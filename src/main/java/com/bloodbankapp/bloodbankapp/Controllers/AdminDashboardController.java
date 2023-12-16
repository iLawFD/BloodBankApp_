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
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            donationPerMonthText.setText(String.valueOf(DataBase.getDataBase().getDonationCountForCurrentMonth()));
            donationPerWeekText.setText(String.valueOf(DataBase.getDataBase().getDonationCountForCurrentWeek()));
            loadNumberOfUser();
            loadBloodType();
            loadDonationInfo();
            loadPayments();
            loadAgesCount();
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
    private TableView<Payment> paymentTable;

    @FXML
    private TableColumn<Payment, Integer> status;
    @FXML
    private BarChart<String, Number> blood_type_barChart;

    @FXML
    private Label donationPerMonthText;

    @FXML
    private TableColumn<DonationDrive, Integer> driveID;
    @FXML
    private TableColumn<DonationDrive, Integer> count;

    @FXML
    private TableView<DonationDrive> donationDriveTable;

    @FXML
    private Label donationPerWeekText;

    @FXML
    private BarChart<String, Number> ageChart;
    @FXML
    private BarChart<String, Number> bloodDonationStatusTable;

    @FXML
    private BarChart<String, Number> numberOfUsersDashBoard;

    @FXML
    void goback(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/admin.fxml"));

        Scene scene2 = null;
        try {
            scene2 = new Scene(fxmlLoader2.load(), 900, 600);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene2);

    }

    void loadBloodType(){
        Map<String, Integer>  res = null;
        try {
            res = DataBase.getDataBase().getBloodDonationStatisticsBloodType();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Integer> entry : res.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        blood_type_barChart.getData().add(series);
    }

    void loadDonationInfo(){
        int donatedDonationCount;
        int storedDonationCount;

        try {
            donatedDonationCount = DataBase.getDataBase().getNumberOfDoneDonation();
            storedDonationCount = DataBase.getDataBase().getNumberOfAvailableDonation();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.getData().add(new XYChart.Data<>("available donations", storedDonationCount));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();

        series2.getData().add(new XYChart.Data<>("done donations", donatedDonationCount));

        bloodDonationStatusTable.getData().addAll(series,series2);
    }

    void loadNumberOfUser() throws SQLException {
        Map<String,Integer> res =  DataBase.getDataBase().getNumberOfUser();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();

        series1.getData().add(new XYChart.Data<>( "number of donors", res.get("number of donors")));
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<>( "number of recipients", res.get("number of recipients")));
        numberOfUsersDashBoard.getData().addAll(series1,series2);




    }

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

    private void loadAgesCount(){

        Map<String, Integer>  res = null;
        try {
            res = DataBase.getDataBase().getAgesCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Integer> entry : res.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        ageChart.getData().add(series);
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
