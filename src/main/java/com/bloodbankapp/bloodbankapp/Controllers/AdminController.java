package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.MainApplication;
import com.bloodbankapp.bloodbankapp.database.DataBase;
import com.bloodbankapp.bloodbankapp.database.EmailSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class AdminController implements Initializable {
    private ObservableList<SystemUser> systemUsers;

    private ObservableList<HBox> systemUserRequestBoxes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Person currentUser = DataBase.getDataBase().getCurrentSystemUser();
            adminIdText.setText(String.valueOf(currentUser.getID()));
            adminAddressText.setText(currentUser.getAddress());
            adminFirsttNameText.setText(currentUser.getFirstName());
            adminLastNameText.setText(currentUser.getLastName());
            adminOfficeNumberText.setText(String.valueOf(((Admin)currentUser).getOffice_number()));
            adminPhoneText.setText(currentUser.getPhone_number());
            adminEmailText.setText(currentUser.getEmail());

            // loading the reqeust
            systemUserRequestBoxes = getUserRequest();
            modViewList.setItems(systemUserRequestBoxes);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadData();

    }
    public void loadData(){
        List<SystemUser> list ;
        try {
            list = DataBase.getDataBase().getSystemUsers();


            ID.setCellValueFactory(new PropertyValueFactory<Person,Integer>("ID"));
            firstName.setCellValueFactory(new PropertyValueFactory<Person,String>("firstName"));
            lastName.setCellValueFactory(new PropertyValueFactory<Person,String >("lastName"));
            address.setCellValueFactory(new PropertyValueFactory<Person,String>("address"));
            phoneNumber.setCellValueFactory(new PropertyValueFactory<Person,String>("phone_number")); // Make sure this matches the field name in UserInformation
            email.setCellValueFactory(new PropertyValueFactory<Person,String>("email"));
            bloodType.setCellValueFactory(new  PropertyValueFactory<Person,String>("bloodType") );
            medicalHistory.setCellValueFactory(new  PropertyValueFactory<Person,String>("medicalHistory") );

            systemUsers = FXCollections.observableArrayList(list);
            personTableView.getSelectionModel().getSelectedItem();
            personTableView.setItems(systemUsers);

            searchFilter();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private ListView<HBox> modViewList;
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
    private TextField bloodTypeText;

    @FXML
    private TextField searchPersonText;


    @FXML
    private TextField medicalHistoryText;

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
    private TextField adminAddressText;

    @FXML
    private TextField adminEmailText;

    @FXML
    private TextField adminFirsttNameText;

    @FXML
    private TextField adminIdText;

    @FXML
    private TextField adminLastNameText;

    @FXML
    private TextField adminOfficeNumberText;

    @FXML
    private TextField adminPhoneText;
    @FXML
    private TextField removeUserText;




    @FXML
    private void fillUserInfo(){
        SystemUser selectedPerson = personTableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            firstNameText.setText(selectedPerson.getFirstName());
            lastNameText.setText(selectedPerson.getLastName());
            phoneText.setText(selectedPerson.getPhone_number());
            emailText.setText(selectedPerson.getEmail());
            addressText.setText(selectedPerson.getAddress());
            IDText.setText(selectedPerson.getID()+"");
            bloodTypeText.setText(selectedPerson.getBloodType());
            medicalHistoryText.setText(selectedPerson.getMedicalHistory());
        }

    }
    @FXML
    private  void insertNewSystemUser() throws SQLException {


        try{
            int id = Integer.parseInt(IDText.getText());
            String firstName = firstNameText.getText();
            String lastname = lastNameText.getText();
            String email = emailText.getText();
            String phone = phoneText.getText();
            String address = addressText.getText();

            DataBase.getDataBase().insertNewUser(id,firstName,lastname,address,phone,email,bloodTypeText.getText(), medicalHistoryText.getText());
            EmailSender.getEmailSender().SendMessage(
                    email,
                    "you were added our Bank system",
                    "thank you for joining us"

            );
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("done successfully");
            alert.setHeaderText("new user is added");
            alert.setContentText("a message was send to the new user on email");
            alert.showAndWait();

            IDText.clear();
            firstNameText.clear();
            lastNameText.clear();
            emailText.clear();
            phoneText.clear();
            addressText.clear();
            bloodTypeText.clear();
            medicalHistoryText.clear();

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        loadData();

    }

    @FXML
    private void updateUserInfo(){

        try{

            int id = Integer.parseInt(IDText.getText());
            String firstName = firstNameText.getText();
            String lastname = lastNameText.getText();
            String email = emailText.getText();
            String phone = phoneText.getText();
            String address = addressText.getText();
            String bloodType = bloodTypeText.getText();
            String medHistory = medicalHistoryText.getText();


            DataBase.getDataBase().updateUser(
                    id,
                    firstName,
                    lastname,
                    address,
                    phone,
                    email,
                    bloodType,
                    medHistory
            );

            EmailSender.getEmailSender().SendMessage(
                    email,
                    "you information was added in our Bank system",
                    "check the new changes"

            );
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("done successfully");
            alert.setHeaderText("new user information is edited");
            alert.setContentText("a message was send to user on email about the edits");
            alert.showAndWait();


        }catch (Exception e){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();


        }
        loadData();

    }
    private void searchFilter(){
        FilteredList<SystemUser> filterData= new FilteredList<>(systemUsers, e->true);
        searchPersonText.setOnKeyReleased(e->{
            searchPersonText.textProperty().addListener((observable,oldValue,newValue)->{
                filterData.setPredicate((Predicate<? super SystemUser>) user ->{
                    if(newValue == null){
                        return true;
                    }
                    final String toLowerCaseFilter = newValue.toLowerCase();
                    if(String.valueOf(user.getID()).contains(newValue)){
                        return  true;
                    }else if(user.getFirstName().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getLastName().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getPhone_number().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getEmail().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getBloodType().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getMedicalHistory().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }

                return  false;
                });
            });
        final SortedList<SystemUser> users = new SortedList<>(filterData);
        users.comparatorProperty().bind(personTableView.comparatorProperty());
        personTableView.setItems(users);
        });

    }
// 111, 233
    @FXML
    private void removeUser(){
        try{
            if(removeUserText.getText().isEmpty() ){
                throw new RuntimeException("");

            }else{
                DataBase.getDataBase().removeSystemUser(Integer.parseInt(removeUserText.getText()));
                String email  = DataBase.getDataBase().getEmail(Integer.parseInt(removeUserText.getText()));
                EmailSender.getEmailSender().SendMessage(
                        email,
                        "Your account is removed",
                        "your account is removed from our system. sorry"
                );
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("remove is done");
                alert.setHeaderText("remove user is removed");
                alert.setContentText("an email was send to the user to tell him you were removed from the system");
                alert.showAndWait();
            }

        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("remove is failed");
            alert.setHeaderText("remove user is not removed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("remove is failed");
            alert.setHeaderText("remove user is not removed");
            alert.setContentText("you did not enter the ID");
            alert.showAndWait();

        }finally {
            loadData();
        }
    }

    @FXML
    protected void showUserHistory(ActionEvent event) throws IOException {

        int id = Integer.parseInt(IDText.getText());

        try{

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/history.fxml"));
            Parent parent = fxmlLoader2.load();

            HistoryController historyController = fxmlLoader2.getController();
            historyController.authenticateUser(id);
            historyController.loadDonation();
            historyController.loadRequests();
            Scene scene2 = new Scene(parent, 1540, 800);
            stage.setScene(scene2);





        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }
    private ObservableList<HBox> getUserRequest(){
        ArrayList<HBox> userreuestbox = new ArrayList<>();
        try {
            List<List<String>> requests = DataBase.getDataBase().getRequests();
            for(List<String> request : requests){
                HBox hBox = new HBox(50);
                hBox.setAlignment(Pos.CENTER);
                Button button = new Button("Accept");
                button.setMaxWidth(Double.MAX_VALUE);
                button.setPrefSize(80, 20);
                button.getStyleClass().add("deleteButton");
                button.setOnAction(e -> {
                    systemUserRequestBoxes.remove(hBox);
                    modViewList.setItems(systemUserRequestBoxes);
                    try {
                        DataBase.getDataBase().acceptModification(Integer.parseInt(request.get(0)));
                        String email = DataBase.getDataBase().getNewEmail(Integer.parseInt(request.get(1)));
                        EmailSender.getEmailSender().SendMessage(
                                email,
                                "modification is accepted",
                                "your information is updated you can check now"

                        );
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("process id done");
                        alert.setHeaderText("The information is updated  ");
                        alert.setContentText("An email wes sent to the user");
                        alert.showAndWait();

                    } catch (SQLException | IOException ex) {
                        throw new RuntimeException(ex);
                    }


                });
                Label label1 = new Label("request :"+request.get(0));
                Label label2 = new Label("user ID: " + request.get(1));
                hBox.getChildren().addAll(label1, label2, button);
                userreuestbox.add(hBox);
            }

            return FXCollections.observableArrayList(userreuestbox);

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    @FXML
    private void goToReports (ActionEvent event){
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/adminDashboard.fxml"));

            Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
            stage.setScene(scene2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void request(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("blood_request.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setWidth(800);
        stage.setHeight(800);
        stage.setScene(scene);
        stage.show();

    }



    @FXML
    void goToBloodRequestPage(ActionEvent event) {

        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/blood_processing.fxml"));

            Scene scene2 = new Scene(fxmlLoader2.load(), 800, 600);

            stage.setScene(scene2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
