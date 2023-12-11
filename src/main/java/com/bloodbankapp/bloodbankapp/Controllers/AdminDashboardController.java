package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            loadNumberOfUser();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private BarChart<?, ?> blood_type_barChart;

    @FXML
    private Label donationPerMonthText;

    @FXML
    private Label donationPerWeekText;

    @FXML
    private BarChart<String, Number> numberOfUsersDashBoard;

    @FXML
    void goback(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/com/bloodbankapp/bloodbankapp/admin.fxml"));

        Scene scene2 = null;
        try {
            scene2 = new Scene(fxmlLoader2.load(), 900, 600);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene2);

    }

    void loadNumberOfUser() throws SQLException {
        Map<String,Integer> res =  DataBase.getDataBase().getNumberOfUser();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();

        series1.getData().add(new XYChart.Data<>( "number of donors", res.get("number of donors")));
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<>( "number of recipients", res.get("number of recipients")));
        numberOfUsersDashBoard.getData().addAll(series1,series2);




    }




}
