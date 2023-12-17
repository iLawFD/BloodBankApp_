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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    private TableColumn<Donation, Integer> bloodDriveNumber;

    @FXML
    private TableView<userBloodRequest> bloodRequestTable;

    @FXML
    private TableColumn<Donation, String> bloodType;

    @FXML
    private TableColumn<userBloodRequest, String> bloodType2;

    @FXML
    private TableColumn<userBloodRequest, Integer> cost;

    @FXML
    private TableColumn<Donation, Date> donationDate;

    @FXML
    private TableColumn<Donation, Integer> donationID;

    @FXML
    private TableColumn<Donation, String> donationStatus;

    @FXML
    private TableView<Donation> donationTable;

    @FXML
    private TableColumn<userBloodRequest, Integer> paymentID;

    @FXML
    private TableColumn<userBloodRequest, String> paymentStatus;

    @FXML
    private TableColumn<userBloodRequest, Date> requestDate;

    @FXML
    private TableColumn<Donation, Integer> requestID;

    @FXML
    private TableColumn<userBloodRequest, Integer> requestID2;

    @FXML
    private TableColumn<userBloodRequest, String> requestStatus;
    @FXML
    private TextField bloodDriveText;
    @FXML
    private TextField bloodTypeText1;

    @FXML
    private TextField bloodTypeText2;
    @FXML
    private TextField costText;
    @FXML
    private DatePicker donationDateText;
    @FXML
    private TextField donationStatusText;
    @FXML
    private TextField dontionIDText;
    @FXML
    private TextField paymentIDText;

    @FXML
    private TextField paymentStatusText;
    @FXML
    private DatePicker requestDateText;
    @FXML
    private TextField requestIDText1;

    @FXML
    private TextField requestIDText2;

    @FXML
    private TextField requestStatusText;
    int currentUserID;



    @FXML
    void goBack(ActionEvent event) {
        try{
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/admin.fxml"));
            Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
            stage.setScene(scene2);
        }catch (Exception e){


        }

    }
    public void authenticateUser(int userID){
        currentUserID = userID;
    }
    public void loadDonation(){
        ArrayList<Donation> bloodDonation;


        try {
            bloodDonation = DataBase.getDataBase().getCurrentUserDonations(currentUserID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        donationID.setCellValueFactory(new PropertyValueFactory<Donation,Integer>("donationID"));
        donationStatus.setCellValueFactory(new PropertyValueFactory<Donation,String>("donationStatus"));
        requestID.setCellValueFactory(new PropertyValueFactory<Donation,Integer>("requestID"));
        donationDate.setCellValueFactory(new PropertyValueFactory<Donation, Date>("donationDate"));
        bloodType.setCellValueFactory(new PropertyValueFactory<Donation,String>("bloodType"));
        bloodDriveNumber.setCellValueFactory(new PropertyValueFactory<Donation,Integer>("bloodDriveNumber"));


        ObservableList<Donation> bloodRequestObservableList = FXCollections.observableArrayList(bloodDonation);
        donationTable.getSelectionModel().getSelectedItem();
        donationTable.setItems(bloodRequestObservableList);

    }

    public void loadRequests(){
        ArrayList<userBloodRequest> userBloodRequests;


        try {
            userBloodRequests = DataBase.getDataBase().getUserBloodRequest(currentUserID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        requestID2.setCellValueFactory(new PropertyValueFactory<userBloodRequest,Integer>("requestID"));
        requestStatus.setCellValueFactory(new PropertyValueFactory<userBloodRequest,String>("requestStatus"));
        requestDate.setCellValueFactory(new PropertyValueFactory<userBloodRequest,Date>("requestDate"));
        bloodType2.setCellValueFactory(new PropertyValueFactory<userBloodRequest, String>("bloodType"));
        cost.setCellValueFactory(new PropertyValueFactory<userBloodRequest,Integer>("cost"));
        paymentStatus.setCellValueFactory(new PropertyValueFactory<userBloodRequest,String>("paymentStatus"));
        paymentID.setCellValueFactory(new PropertyValueFactory<userBloodRequest,Integer>("paymentID"));


        ObservableList<userBloodRequest> bloodRequestObservableList = FXCollections.observableArrayList(userBloodRequests);
        bloodRequestTable.getSelectionModel().getSelectedItem();
        bloodRequestTable.setItems(bloodRequestObservableList);

    }


    @FXML
    void fillDonationInfo(MouseEvent event) {
        Donation donation = donationTable.getSelectionModel().getSelectedItem();
        dontionIDText.setText(String.valueOf(donation.getDonationID()));
        requestIDText1.setText(String.valueOf(donation.getRequestID()));
        donationStatusText.setText(donation.getDonationStatus());
        donationDateText.setValue(donation.getDonationDate().toLocalDate());
        bloodTypeText1.setText(donation.getBloodType());
        bloodDriveText.setText(String.valueOf(donation.getBloodDriveNumber()));
    }

    @FXML
    void fillRequestInfo(MouseEvent event) {
        userBloodRequest bloodRequest = bloodRequestTable.getSelectionModel().getSelectedItem();
        requestIDText2.setText(String.valueOf(bloodRequest.getRequestID()));
        requestStatusText.setText(bloodRequest.getRequestStatus());
        bloodTypeText2.setText(bloodRequest.getBloodType());
        requestDateText.setValue(bloodRequest.getRequestDate().toLocalDate());
        costText.setText(String.valueOf(bloodRequest.getCost()));
        paymentStatusText.setText(bloodRequest.getPaymentStatus());
        paymentIDText.setText(String.valueOf(bloodRequest.getPaymentID()));



    }

    @FXML
    void updateRequest(ActionEvent event) {
        try {
            DataBase.getDataBase().updateBloodRequest (
                    Integer.parseInt(requestIDText2.getText()),
                    requestStatusText.getText(),
                    Date.valueOf(requestDateText.getValue()),
                    Integer.parseInt(costText.getText()),
                    paymentStatusText.getText()
                    );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("process is done");
            alert.setHeaderText("The information is updated  ");
            alert.setContentText("");
            alert.showAndWait();
        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("process is failed");
            alert.setHeaderText("The information is not updated  ");
            alert.setContentText("");
            alert.showAndWait();
        }finally {
            loadRequests();
            loadDonation();
        }
    }
    @FXML
    void updateDonation(ActionEvent event) {

        try {
            DataBase.getDataBase().updateDonation(Integer.parseInt(dontionIDText.getText()),
                    donationStatusText.getText(),Date.valueOf(donationDateText.getValue()) ,
                    Integer.parseInt(bloodDriveText.getText()));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("process is done");
            alert.setHeaderText("The information is updated  ");
            alert.setContentText("");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("process is failed");
            alert.setHeaderText("The information is not updated  ");
            alert.setContentText("");
            alert.showAndWait();
        }finally {
            loadDonation();
            loadRequests();

        }

    }



}
