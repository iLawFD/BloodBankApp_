package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import com.bloodbankapp.bloodbankapp.database.EmailSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
    public void loadData(){
        List<SystemUser> list ;
        try {
            list = DataBase.getDataBase().getSystemUser();


            ID.setCellValueFactory(new PropertyValueFactory<Person,Integer>("ID"));
            firstName.setCellValueFactory(new PropertyValueFactory<Person,String>("firstName"));
            lastName.setCellValueFactory(new PropertyValueFactory<Person,String >("lastName"));
            address.setCellValueFactory(new PropertyValueFactory<Person,String>("address"));
            phoneNumber.setCellValueFactory(new PropertyValueFactory<Person,String>("phone_number")); // Make sure this matches the field name in UserInformation
            email.setCellValueFactory(new PropertyValueFactory<Person,String>("email"));
            bloodType.setCellValueFactory(new  PropertyValueFactory<Person,String>("bloodType") );
            medicalHistory.setCellValueFactory(new  PropertyValueFactory<Person,String>("medicalHistory") );

            ObservableList<SystemUser> observableListPerson = FXCollections.observableArrayList(list);
            personTableView.getSelectionModel().getSelectedItem();
            personTableView.setItems(observableListPerson);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private TableColumn<Person, Integer> ID;

    @FXML
    private TextField IDText;

    @FXML
    private TableColumn<Person, String> address;

    @FXML
    private TextField addressText;

    @FXML
    private TableColumn<Person, String> email;

    @FXML
    private TextField emailText;

    @FXML
    private TableColumn<Person, String> firstName;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField historyText;

    @FXML
    private TableColumn<Person, String> lastName;

    @FXML
    private TextField lastNameText;

    @FXML
    private TableView<SystemUser> personTableView;


    @FXML
    private TableColumn<Person, String> phoneNumber;

    @FXML
    private TextField phoneText;

    @FXML
    private TextField searchPersonText;

    @FXML
    private ListView<?> view;

    @FXML
    void reports(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }
    @FXML
    private TableColumn<Person, String> medicalHistory;

    @FXML
    private TableColumn<Person, String> bloodType;

    @FXML
    private void fillUserInfo(){
        Person selectedPerson = personTableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            firstNameText.setText(selectedPerson.getFirstName());
            lastNameText.setText(selectedPerson.getLastName());
            phoneText.setText(selectedPerson.getPhone_number());
            emailText.setText(selectedPerson.getEmail());
            addressText.setText(selectedPerson.getAddress());
            IDText.setText(selectedPerson.getID()+"");
        }

    }
    @FXML
    private  void insertNewSystemUser() throws SQLException {
        try{
            int id = Integer.parseInt(IDText.getText());
            String firstName = firstNameText.getText();
            String lastname = lastNameText.getText();
            String email = emailText.getText();
            String phone = phoneNumber.getText();
            String address = addressText.getText();

            DataBase.getDataBase().insertNewUser(id,firstName,lastname,address,phone,email);
            EmailSender.getEmailSender().SendMessage(
                    email,
                    "you were added our Bank system",
                    "thank you for joining us"

            );

        }catch (Exception e){

        }

    }

    @FXML
    private void updateUserInfo(){
        try{


        }catch (Exception e){

        }
    }



}
