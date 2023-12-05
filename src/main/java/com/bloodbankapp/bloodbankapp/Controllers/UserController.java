package com.bloodbankapp.bloodbankapp.Controllers;
import java.time.LocalDate;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserController implements Initializable {





    @FXML
    private ListView<String> view; // Make sure to import correct ListView type

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            SystemUser currentSystemUser = (SystemUser) DataBase.getDataBase().getCurrentSystemUser();
            userIDText.setText(String.valueOf(currentSystemUser.getID()));
            userFirstNameText.setText(currentSystemUser.getFirstName());
            userLastNameText.setText(currentSystemUser.getLastName());
            userAddressText.setText(currentSystemUser.getAddress());
            userEmailText.setText(currentSystemUser.getEmail());
            userBloodTypeText.setText(currentSystemUser.getBloodType());
            userPhoneText.setText(currentSystemUser.getPhone_number());
            userHistoryText.setText(currentSystemUser.getMedicalHistory());

            String info = DataBase.getDataBase().showRequests();
            String[] lst = info.split("\n\n");

            ObservableList<String> items = FXCollections.observableArrayList(lst);
            view.setItems(items);


        } catch (SQLException e) {
            System.out.println((e.getMessage()));
        }


    }




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
    private TextField  text1;
    @FXML
    private Button b1;

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
        text1.setOpacity(1);
        b1.setOpacity(1);







    }

    @FXML
    protected void submit() throws IOException, SQLException {



        SystemUser currentSystemUser = (SystemUser) DataBase.getDataBase().getCurrentSystemUser();


        LocalDate today = LocalDate.now();

        String str = "";




        str +="Date: "+  (today) + " ";
        str+= "Type: "+ (currentSystemUser.getBloodType()) + " ";
        str+= "Rec ID " + (currentSystemUser.getID());
        str += "\n\n";




        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Request has been made");
        alert.setHeaderText("The given ID does not exsist  ");
        alert.setContentText("Please make sure of your ID! ");
        alert.showAndWait();
        view.getItems().add(str);






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














}
