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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
    void goBack(ActionEvent event) {
        try{
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/admin.fxml"));
            Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
            stage.setScene(scene2);
        }catch (Exception e){


        }

    }
    public void loadDonation(int ID){
        ArrayList<Donation> bloodDonation;


        try {
            bloodDonation = DataBase.getDataBase().getCurrentUserDonations(ID);
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

    public void loadRequests(int ID){
        ArrayList<userBloodRequest> userBloodRequests;


        try {
            userBloodRequests = DataBase.getDataBase().getUserBloodRequest(ID);
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


}
