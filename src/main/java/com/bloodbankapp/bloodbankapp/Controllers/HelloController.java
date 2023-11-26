package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class HelloController {


    @FXML

    TextField text;


    @FXML
    private Button b1;

    @FXML
    private Button b2;

    @FXML
    protected void translate(ActionEvent event) throws IOException {


        Node node = (Node) event.getSource();
        Window win = ((Scene) node.getScene()).getWindow();

        if (win != null) {
            Scene scene = win.getScene();
            if (scene != null) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/cop2.fxml"));

                Scene scene2 = new Scene(fxmlLoader2.load(), 900, 600);
                System.out.println(text.getText());
                stage.setScene(scene2);
            }
        }
    }

}


