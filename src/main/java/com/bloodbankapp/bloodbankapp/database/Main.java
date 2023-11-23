package com.bloodbankapp.bloodbankapp.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");


            String url = "jdbc:postgresql://ep-shrill-darkness-20221653.us-east-2.aws.neon.tech/blood%20system?user=ayed87&password=2KdnfuWpEa3e&sslmode=require";
            String username = "ayed87";
            String password = "2KdnfuWpEa3e";
            Connection connection = DriverManager.getConnection(url, username, password);

            // Execute a query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select first_name from person where id = 3 ");

            // Process the resultSet
            while (resultSet.next()) {
                System.out.println(resultSet.getString("first_name"));
            }

            // Close the connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}