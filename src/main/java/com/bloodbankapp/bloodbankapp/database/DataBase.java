package com.bloodbankapp.bloodbankapp.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataBase {

    private static DataBase dataBase;
    private Person currentSystemUser;
    private final Connection connection;
    private DataBase() throws SQLException {
        String url = "jdbc:postgresql://ep-shrill-darkness-20221653.us-east-2.aws.neon.tech"+
                "/blood%20system?user=ayed87&password=2KdnfuWpEa3e&sslmode=require";
        String username = "ayed87";
        String password = "2KdnfuWpEa3e";
        connection = DriverManager.getConnection(url, username, password);

    }

    public static DataBase getDataBase() throws SQLException{
        if(dataBase == null){
            dataBase = new DataBase();
        }
        return  dataBase;
    }

    public Person retrieveUserInfo(int ID) throws SQLException{

        String query = "SELECT * FROM person WHERE ID = ?";


        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ID);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String personType = resultSet.getString("person_type");
        if ("Admin".equals(personType)) {
//            System.out.println(resultSet.getString("first_name"));
//            System.out.println(resultSet.getString("ID"));
            currentSystemUser = new Admin();
        } else if ("system_user".equals(personType)) {
//            System.out.println(resultSet.getString("first_name"));
//            System.out.println(resultSet.getString("ID"));
            currentSystemUser = new SystemUser();
        }
        return  currentSystemUser;

    }
    public void update() throws SQLException {
        Statement s1 = connection.createStatement();
        String updateQuery = "UPDATE Person " +
                "SET First_name = '" + currentSystemUser.getFirstName() + "', Last_name = '" + currentSystemUser.getLastName() + "', " +
                "address = '" + currentSystemUser.getAddress() + "', Phone_number = '" + currentSystemUser.getPhone_number() + "', " +
                "email = '" + currentSystemUser.getEmail() + "' " +
                "WHERE ID = " + currentSystemUser.getID();
        int rowsAffected = s1.executeUpdate(updateQuery);

    }
    public void insertNewUser(int userID,String firstName, String lastName,
                                     String address, String phoneNumber, String email, String personType) throws SQLException{

        String insertQuery = "INSERT INTO Person (id ,First_name, Last_name, address, Phone_number, email, person_type) " +
                "VALUES (?,?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

        preparedStatement.setInt(1, userID);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, phoneNumber);
        preparedStatement.setString(6, email);
        preparedStatement.setString(7, personType);

        int rowsAffected = preparedStatement.executeUpdate();

    }
    void removeSystemUser(int id) throws SQLException{
        String deleteQuery = "DELETE FROM person WHERE ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, id);

        int rowsAffected = preparedStatement.executeUpdate();
    }

    public static void main(String[] args) {
        try {
            System.out.println(getDataBase().getDonationCountForCurrentWeek());
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public int getDonationCountForCurrentWeek() throws SQLException {
        String sqlQuery = "SELECT COUNT(*) " +
                "FROM Blood_product " +
                "WHERE Date >= CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE)::INTEGER " +
                "AND Date < CURRENT_DATE + (7 - EXTRACT(DOW FROM CURRENT_DATE)::INTEGER)";


        Statement s1 = connection.createStatement();
        ResultSet resultSet = s1.executeQuery(sqlQuery);
        if (resultSet.next()) {

            return resultSet.getInt(1);
        } else {
            return 0;
        }
    }
    // this function returns a formatted string of a searched user, used in the search bar
    public String searchUser(int ID) throws SQLException{
        String query = "SELECT * FROM Person P JOIN System_user S ON S.ID = " + ID + " WHERE P.person_type <> 'admin'";
        Statement s1 = connection.createStatement();
        ResultSet r1 = s1.executeQuery(query);
        String search_result = "ID: " + r1.getString("ID") + "\n";
        search_result += "First name: " +  r1.getString("First_name") + "\n";
        search_result += "Last name: " +  r1.getString("Last_name") + "\n";
        search_result += "Address: " +  r1.getString("address") + "\n";
        search_result += "Phone number: " +  r1.getString("Phone_number") + "\n";
        search_result += "Email: " +  r1.getString("email") + "\n";
        search_result += "Blood type: " +  r1.getString("Blood_type") + "\n";
        return search_result;
    }

    public void requestDonation() throws SQLException {

    }

    public String showRequests() throws SQLException {
        String query = "SELECT * FROM Blood_product WHERE DONOR_ID IS NULL";
        Statement s1 = connection.createStatement();
        ResultSet r1 = s1.executeQuery(query);
        String str = "";

        while (r1.next()){

            str +="Date: "+  r1.getString("Date") + " ";
        str+= "Type: "+ r1.getString("Blood_type") + " ";
        str+= "Rec ID " + r1.getString("recipient_id");
        str += "\n\n";
        }


        return str;


    }

    private ResultSet executeQuery(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlQuery);

    }
    public  int getDonationCountForCurrentMonth() throws SQLException {
        String sqlQuery = "SELECT COUNT(*) " +
                "FROM Blood_product " +
                "WHERE EXTRACT(YEAR FROM Date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
                "AND EXTRACT(MONTH FROM Date) = EXTRACT(MONTH FROM CURRENT_DATE)";

        ResultSet resultSet = executeQuery(sqlQuery);
        if (resultSet.next()) {

            return resultSet.getInt(1);
        } else {
            return 0;
        }
    }

    public Map<String, Integer> getBloodDonationStatisticsBloodType() throws SQLException {
        Map<String, Integer> donationStatistics = new HashMap<>();

        String sqlQuery = "SELECT blood_type, COUNT(blood_type) AS Donation_Count " +
                "FROM Blood_product " +
                "GROUP BY blood_type";

        ResultSet resultSet = executeQuery(sqlQuery);
        while (resultSet.next()) {
            String bloodType = resultSet.getString("blood_type");
            int donationCount = resultSet.getInt("donation_count");
            donationStatistics.put(bloodType, donationCount);
        }
        return  donationStatistics;
    }
}
//    Statement s1 = connection.createStatement();
    // a query to retrieve the specific info of the current user
//    ResultSet r1 = s1.executeQuery("SELECT * " +
//            "FROM person " +
//            "WHERE ID = " + snn);
//        if (r1.getString("person_type").equals("Admin")){
//                System.out.println(r1.getString("first_name"));
//                System.out.println(r1.getString("Id"));
//                currentSystemUser = new Admin ();
//                } else if (r1.getString("person_type").equals("system_user")) {
//                currentSystemUser = new SystemUser();
//                }
//                return currentSystemUser;
