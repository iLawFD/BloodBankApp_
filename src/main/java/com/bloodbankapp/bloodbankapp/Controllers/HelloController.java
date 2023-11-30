package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;

public class HelloController {





    @FXML

    TextField text;


    @FXML
    private Button b1;

    @FXML
    private Button b2;








    @FXML
    protected void translate(ActionEvent event) throws IOException, SQLException {


        Node node = (Node) event.getSource();
        Window win = ((Scene) node.getScene()).getWindow();

        if (win != null) {
            Scene scene = win.getScene();
            if (scene != null) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/cop2.fxml"));

                Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
                String text1 = text.getText();
                try {
                    Person person = (Person) DataBase.getDataBase().retrieveUserInfo(Integer.parseInt(text1));
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Input Error");
                    alert.setHeaderText("The given ID does not exsist  ");
                    alert.setContentText("Please make sure of your ID! ");
                    alert.showAndWait();
                    return;
                }
                stage.setScene(scene2);
            }
        }

    }

}


