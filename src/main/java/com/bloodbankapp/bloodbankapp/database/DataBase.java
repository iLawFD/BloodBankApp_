package com.bloodbankapp.bloodbankapp.database;


import java.sql.*;
import java.util.List;
//"jdbc:postgresql://ep-shrill-darkness-20221653.us-east-2.aws.neon.tech/blood%20system?user=ayed87&password=2KdnfuWpEa3e&sslmode=require"
public class DataBase {

    private static DataBase dataBase;
    private final String url = "jdbc:postgresql://ep-red-snowflake-74407913.us-east-2.aws.neon.tech/Admin?user=ggforever111&password=fnb9EwiL2ztg&sslmode=require";
    private final String username = "ggforever111";
    private final String password = "fnb9EwiL2ztg";
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
            ResultSet resultSet = statement.executeQuery("select * from Admin");

            // Process the resultSet
            while (resultSet.next()) {
                System.out.println(resultSet.getString("admin_id"));
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
