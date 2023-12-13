package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
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


    @FXML
    void goBack(ActionEvent event) {

    }

    @FXML
    void processRequest(ActionEvent event) {

        try{
            BloodRequest bloodRequest =  bloodRequestTable.getSelectionModel().getSelectedItem();


        }catch (Exception e){

        }

    }

}
