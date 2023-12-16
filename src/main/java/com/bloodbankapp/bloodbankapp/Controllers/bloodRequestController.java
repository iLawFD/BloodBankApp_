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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Message;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class bloodRequestController implements Initializable {


    @FXML
    private TableColumn<BloodRequest, Integer> ID;

    @FXML
    private TableView<BloodRequest> bloodRequestTable;
    @FXML
    private TableColumn<BloodRequest, String> bloodType;

    @FXML
    private TableColumn<BloodRequest, Date> date;

    @FXML
    private TableColumn<BloodRequest, Integer> requestID;

    @FXML
    private TableColumn<Donation, Date> ExpirationDate;
    @FXML
    private TableColumn<Donation, Integer> bloodDriveNumber;

    @FXML
    private TableColumn<Donation, String> bloodType2;
    @FXML
    private TableColumn<Donation, Date> donationDate;

    @FXML
    private TableColumn<Donation, Integer> donationID;
    @FXML
    private TableColumn<Donation, Integer> requestID2;

    @FXML
    private TableView<Donation> donationTable;

    @FXML
    private Button sendButton;
    @FXML
    private ComboBox<String> selectComboBox;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        loadDonations();
        loadOrganization();
    }

    void loadData(){
        ArrayList<BloodRequest> bloodRequests;


        try {
            bloodRequests = DataBase.getDataBase().bloodRequests();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        ID.setCellValueFactory(new PropertyValueFactory<BloodRequest,Integer>("ID"));
        bloodType.setCellValueFactory(new PropertyValueFactory<BloodRequest,String>("bloodType"));
        date.setCellValueFactory(new PropertyValueFactory<BloodRequest, Date>("date"));
        requestID.setCellValueFactory(new PropertyValueFactory<BloodRequest,Integer>("requestID"));


        ObservableList<BloodRequest> bloodRequestObservableList = FXCollections.observableArrayList(bloodRequests);
        bloodRequestTable.getSelectionModel().getSelectedItem();
        bloodRequestTable.setItems(bloodRequestObservableList);
    }

    void loadDonations(){
        ArrayList<Donation> bloodRequests;


        try {
            bloodRequests = DataBase.getDataBase().getDonations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        donationID.setCellValueFactory(new PropertyValueFactory<Donation,Integer>("donationID"));
        requestID2.setCellValueFactory(new PropertyValueFactory<Donation,Integer>("requestID"));
        donationDate.setCellValueFactory(new PropertyValueFactory<Donation, Date>("donationDate"));
        ExpirationDate.setCellValueFactory(new PropertyValueFactory<Donation,Date>("ExpirationDate"));
        bloodType2.setCellValueFactory(new PropertyValueFactory<Donation,String>("bloodType"));
        bloodDriveNumber.setCellValueFactory(new PropertyValueFactory<Donation,Integer>("bloodDriveNumber"));





        ObservableList<Donation> bloodRequestObservableList = FXCollections.observableArrayList(bloodRequests);
        donationTable.getSelectionModel().getSelectedItem();
        donationTable.setItems(bloodRequestObservableList);
    }


    @FXML
    void goBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/admin.fxml"));

            Scene scene2 = new Scene(fxmlLoader2.load(), 800, 600);

            stage.setScene(scene2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void processRequest(ActionEvent event) {

        try{
            BloodRequest bloodRequest =  bloodRequestTable.getSelectionModel().getSelectedItem();
            Boolean isProcessed = DataBase.getDataBase().fulfillBloodRequests(bloodRequest.getID(),bloodRequest.getRequestID(),bloodRequest.getBloodType());

            if(isProcessed){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("process id done");
                alert.setHeaderText("The blood request is fulfilled");
                alert.setContentText("An email wes sent to the user");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("process is failed");
                alert.setHeaderText("The blood request is not fulfilled");
                alert.setContentText("there is no any blood type that can be compatible with this request");
                alert.showAndWait();
            }


        }catch (Exception e){

        }finally {
            loadData();
            loadDonations();
        }




    }


    @FXML
    void openOrganizationRequest(ActionEvent event) {
        selectComboBox.setDisable(false);
        sendButton.setDisable(false);
    }


    @FXML
    void sendDonationToOrganization(ActionEvent event) {



        try {
            int ID = donationTable.getSelectionModel().getSelectedItem().getDonationID();
            String org = selectComboBox.getSelectionModel().getSelectedItem();

            DataBase.getDataBase().sendBloodToOrg(ID,org);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("process id done");
            alert.setHeaderText("The blood is send to the Organization");
            alert.setContentText("");
            alert.showAndWait();

        } catch (SQLException | RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("process is failed");
            alert.setHeaderText("The blood is not send");
            alert.setContentText("");
            alert.showAndWait();

        }finally {
            sendButton.setDisable(true);
            selectComboBox.setDisable(true);
            loadDonations();
        }


    }

    private void  loadOrganization(){
        ArrayList<String> stringArrayList;
        try {
            stringArrayList = DataBase.getDataBase().getOrganization();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> bloodRequestObservableList = FXCollections.observableArrayList(stringArrayList);
        selectComboBox.setItems(bloodRequestObservableList);





    }

}
