
package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import com.bloodbankapp.bloodbankapp.database.EmailSender;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class UserController implements Initializable {





    @FXML
    private ListView<String> view; // Make sure to import correct ListView type

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addItemToComboBox();

        try {
            SystemUser currentSystemUser = (SystemUser) DataBase.getDataBase().getCurrentSystemUser();

            loadDonation();
            loadRequests();
            getCurrentDriveInfo();
            userIDText.setText(String.valueOf(currentSystemUser.getID()));
            userFirstNameText.setText(currentSystemUser.getFirstName());
            userLastNameText.setText(currentSystemUser.getLastName());
            userAddressText.setText(currentSystemUser.getAddress());
            userEmailText.setText(currentSystemUser.getEmail());
            userBloodTypeText.setText(currentSystemUser.getBloodType());
            userPhoneText.setText(currentSystemUser.getPhone_number());
            userHistoryText.setText(currentSystemUser.getMedicalHistory());





        } catch (SQLException e) {
            System.out.println((e.getMessage()));
        }


    }

    @FXML
    private TableColumn<Donation, Integer> bloodDriveNumber;

    @FXML
    private TableColumn<Donation, String> bloodType;

    @FXML
    private TableColumn<Donation, Date> donationDate;

    @FXML
    private TableColumn<Donation, Integer> donationID;

    @FXML
    private TableColumn<Donation, String> donationStatus;
    @FXML
    private TableColumn<Donation, Integer> requestID;

    @FXML
    private TableView<Donation> donationTable;

    @FXML
    private TableColumn<userBloodRequest, Integer> requestID2;
    @FXML
    private TableColumn<userBloodRequest, String> requestStatus;
    @FXML
    private TableColumn<userBloodRequest, Date> requestDate;
    @FXML
    private TableColumn<userBloodRequest, String> bloodType2;

    @FXML
    private TableColumn<userBloodRequest, Integer> cost;

    @FXML
    private TableColumn<userBloodRequest, String> paymentStatus;
    @FXML
    private TableColumn<userBloodRequest, Integer> paymentID;

    @FXML
    private TableView<userBloodRequest> userRequestsTable;

    @FXML
    private Button paymentButton;

    @FXML
    TextField text;

    @FXML
    private TextField userAddressText;

    @FXML
    private TextField userEmailText;

    @FXML
    private TextField userFirstNameText;

    @FXML
    private TextField userIDText;

    @FXML
    private TextField userLastNameText;

    @FXML
    private TextField userPhoneText;

    @FXML
    private TextField userHistoryText;

    @FXML
    private TextField userBloodTypeText;

    @FXML
    private TextField  ageText;
    @FXML
    private TextField  weightText;

    @FXML
    private Button submitDonateButton;
    @FXML
    private ComboBox<String> diseaseComboBox;
    @FXML
    private ComboBox<String> donateComboBox;

    @FXML
    private TextArea driveMessage;

    @FXML
    private Label driveNumberText;

    @FXML
    private Label numberOfDonation;


    @FXML
    private TextField  text2;

    @FXML
    protected void reports(ActionEvent event) throws IOException, SQLException {


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/dashboard.fxml"));

        Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
        stage.setScene(scene2);
    }

    @FXML
    protected void goback(ActionEvent event) throws IOException, SQLException {


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/cop2.fxml"));

        Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
        stage.setScene(scene2);
    }

    @FXML
    protected void search() throws IOException {

        String id = text.getText();

        System.out.println(id);
        try {
            String info = DataBase.getDataBase().searchUser(Integer.parseInt(id));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Info: ");

            alert.setContentText(info);
            alert.showAndWait();


        } catch (SQLException e) {

            System.out.println(e.getErrorCode());
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText("The given ID does not exsist  ");
            alert.setContentText("Please make sure of your ID! ");
            alert.showAndWait();
            return;
        }


    }

    @FXML
    protected void request(ActionEvent event) throws IOException, SQLException {
        try{
            DataBase.getDataBase().requestBlood();

        }catch (Exception e){
            System.out.println("ex");

        }finally {
            loadRequests();
        }
    }

    @FXML
    protected void donate(ActionEvent event) throws IOException, SQLException {

        donateComboBox.setDisable(false);
        submitDonateButton.setDisable(false);

    }

    @FXML
    protected void submit()  {

        try{
            String disease = diseaseComboBox.getSelectionModel().getSelectedItem();
            String firstTime = donateComboBox.getSelectionModel().getSelectedItem();

            if(firstTime.equals("yes")){
                int age = Integer.parseInt(ageText.getText());
                int weight = Integer.parseInt(weightText.getText());
                if(age >= 17  & disease.equals("yes") & weight >= 114 ){
                    DataBase.getDataBase().insertNewDonor(age, weight);
                    DataBase.getDataBase().donateBlood();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Donation is Successfully made");

                    alert.setContentText("Thank you for donating ");
                    alert.showAndWait();
                    loadDonation();


                }
                else{

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("You did not meet our donation rules ");
                    alert.setContentText("try again when you meet them ");
                    alert.showAndWait();

                }


            }
            else{
                boolean hasDonated  = DataBase.getDataBase().hasDonated(DataBase.getDataBase().getCurrentSystemUser().getID());
                if(hasDonated){
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("donation is failed");

                    alert.setContentText("you donated recently you should complete 2 months");
                    alert.showAndWait();
                }else{
                    DataBase.getDataBase().donateBlood();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Donation is Successfully made");

                    alert.setContentText("Thank you for donating ");
                    alert.showAndWait();
                    loadDonation();



                }

            }

        } catch (RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("donation is failed");

            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (SQLException e){

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("donation is failed");
            alert.setContentText("you entered wrong information: this is not the first time you donate");
            alert.showAndWait();
        } finally {
            disableAll();
        }




    }
    @FXML
    protected void enableEdit(){

        userAddressText.setDisable(false);
        userEmailText.setDisable(false);
        userFirstNameText.setDisable(false);
        userLastNameText.setDisable(false);
        userPhoneText.setDisable(false);
        userHistoryText.setDisable(false);
        userBloodTypeText.setDisable(false);

    }

    @FXML
    protected void signOut(){

    }

    @FXML
    protected void requestModification(){
        try {
            DataBase.getDataBase().requestModification(
                    Integer.parseInt(userIDText.getText()),
                    userFirstNameText.getText(),
                    userLastNameText.getText(),
                    userPhoneText.getText(),
                    userAddressText.getText(),
                    userBloodTypeText.getText(),
                    userHistoryText.getText(),
                    userEmailText.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Request has been made");
            alert.setHeaderText("your request will be send the admins of the system  ");
            alert.setContentText("Please wait for the approve ");
            alert.showAndWait();


        }catch (Exception e){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("your request failed ");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    @FXML
    private void signOut (ActionEvent event){
        try {
            DataBase.getDataBase().endCurrentUserSession();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/login-view.fxml"));

            Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
            stage.setScene(scene2);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadDonation(){
        ArrayList<Donation> bloodDonation;


        try {
            bloodDonation = DataBase.getDataBase().getCurrentUserDonations(DataBase.getDataBase().getCurrentSystemUser().getID());
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


    private void loadRequests(){
        ArrayList<userBloodRequest> userBloodRequests;


        try {
            userBloodRequests = DataBase.getDataBase().getUserBloodRequest(DataBase.getDataBase().getCurrentSystemUser().getID());
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
        userRequestsTable.getSelectionModel().getSelectedItem();
        userRequestsTable.setItems(bloodRequestObservableList);

    }


    @FXML
    void checkConfirmPayment(MouseEvent event) {

        try{
            paymentButton.setDisable(!userRequestsTable.getSelectionModel().getSelectedItem().getPaymentStatus().equals("uncompleted"));

        }catch (Exception e){

        }
    }

    @FXML
    void pay(ActionEvent event) {

        try {
            DataBase.getDataBase().confirmPayments(userRequestsTable.getSelectionModel().getSelectedItem().getPaymentID());

            String email = DataBase.getDataBase().getCurrentSystemUser().getEmail();

            EmailSender.getEmailSender().SendMessage(
                    email,
                    "payment is done",
                    "thank you"
            );
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("process id done");
            alert.setHeaderText("The information of payment is updated  ");
            alert.setContentText("An email wes sent to the user");
            alert.showAndWait();
            loadRequests();


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("process id failed");
            alert.setHeaderText("The information of payment is not updated  ");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void getCurrentDriveInfo(){

        try {
            driveNumberText.setText("drive number : "+String.valueOf(DataBase.getDataBase().getCurrentDriveNumber()));
            driveMessage.setText(DataBase.getDataBase().getCurrentDriveMessage());
            numberOfDonation.setText(String.valueOf(DataBase.getDataBase().getCurrentDriveNumberOfDonations()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void showOtherInfo(ActionEvent event) {
        try {
            if(donateComboBox.getSelectionModel().getSelectedItem().equals("yes")){
                ageText.setDisable(false);
                weightText.setDisable(false);
                diseaseComboBox.setDisable(false);
            }else{
                ageText.setDisable(true);
                weightText.setDisable(true);
                diseaseComboBox.setDisable(true);
            }
        } catch (Exception e){

        }


    }

    private void addItemToComboBox(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("yes");
        strings.add("no");

        ObservableList<String> items = FXCollections.observableArrayList(strings);


        diseaseComboBox.setItems(items);
        donateComboBox.setItems(items);
    }

    private  void disableAll(){
        ageText.clear();
        weightText.clear();
        diseaseComboBox.setPromptText("free from desise");
        donateComboBox.setPromptText("first time donating?");
        submitDonateButton.setDisable(true);
        ageText.setDisable(true);
        weightText.setDisable(true);
        diseaseComboBox.setDisable(true);
        donateComboBox.setDisable(true);
    }
















}
