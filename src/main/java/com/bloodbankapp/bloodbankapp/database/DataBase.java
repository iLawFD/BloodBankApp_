package com.bloodbankapp.bloodbankapp.database;


import java.sql.*;
import java.util.List;

public class DataBase {

    private static DataBase dataBase;
    private final String url = "jdbc:postgresql://ep-shrill-darkness-20221653.us-east-2.aws.neon.tech/blood%20system?user=ayed87&password=2KdnfuWpEa3e&sslmode=require";
    private final String username = "ayed87";
    private final String password = "2KdnfuWpEa3e";
    Connection connection;

    private DataBase() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }
    public static DataBase getDataBase() throws  SQLException{
        if(dataBase == null){
            dataBase = new DataBase();
        }
        return  dataBase;
    }
    public List<String> getUserInfo(String snn)
    throws Exception{
        return null;
    }
    public  void method(){

        try {

            // Execute a query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
select first_name from person where id = 3
""");

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
    public static void main(String[] args) {
        try {
            DataBase.getDataBase().method();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
