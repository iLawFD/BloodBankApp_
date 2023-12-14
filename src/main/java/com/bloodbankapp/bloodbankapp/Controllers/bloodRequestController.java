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
            DataBase.getDataBase().fulfillBloodRequests(bloodRequest.getID(),bloodRequest.getRequestID(),bloodRequest.getBloodType());


        }catch (Exception e){

        }

    }

}
