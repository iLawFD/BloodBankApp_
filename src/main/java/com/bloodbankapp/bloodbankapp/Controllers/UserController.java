package com.bloodbankapp.bloodbankapp.Controllers;
import com.bloodbankapp.bloodbankapp.database.DataBase;
import com.bloodbankapp.bloodbankapp.database.Main;
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
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserController implements Initializable {





    @FXML
    private ListView<String> view; // Make sure to import correct ListView type

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            String info = DataBase.getDataBase().showRequests();
            String[] lst = info.split("\n\n");

            ObservableList<String> items = FXCollections.observableArrayList(lst);
            view.setItems(items);
          //  for (int i = 0 ; i < lst.length ; i ++){

            //}

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }


    }




    @FXML
    TextField text;

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





        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Request has been made");
        alert.setHeaderText("The given ID does not exsist  ");
        alert.setContentText("Please make sure of your ID! ");
        alert.showAndWait();
    }









}
