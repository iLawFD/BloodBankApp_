package com.bloodbankapp.bloodbankapp.database;


import com.bloodbankapp.bloodbankapp.Controllers.Admin;
import com.bloodbankapp.bloodbankapp.Controllers.Person;
import com.bloodbankapp.bloodbankapp.Controllers.SystemUser;

import java.sql.*;
import java.util.*;

public class DataBase {

    private static DataBase dataBase;
    private Person currentSystemUser;
    private final Connection connection;
    private DataBase() throws SQLException {
        String url = "jdbc:postgresql://ep-empty-thunder-47709051.us-east-2.aws.neon.tech/blood%20bank%20system?user" +
                "=icsdatabase2&password=MNtaxLy0oFY6&sslmode=require";
        String username = "icsdatabase2";
        String password = "MNtaxLy0oFY6";
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
                                     String address, String phoneNumber, String email) throws SQLException{

        String insertQuery = "INSERT INTO Person (id ,First_name, Last_name, address, Phone_number, email, person_type) " +
                "VALUES (?,?, ?, ?, ?, ?,'system_user' )";

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

        preparedStatement.setInt(1, userID);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, phoneNumber);
        preparedStatement.setString(6, email);

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
        String query1 = "SELECT * FROM Person P JOIN System_user S ON S.ID = " + ID + " WHERE P.person_type <> 'admin'";
        String query2 = "";
        ResultSet r1 = eQ(query1);
        r1.next();
        if (r1.getString("user_status").equalsIgnoreCase("donor")){
            query2 =
                    "SELECT SUM(amount_blood) as sumAmountBlood," +
                            "COUNT(amount_blood) as countAmountBlood" +
                            " FROM Blood_product" +
                            " WHERE donor_ID =" + ID;
        } else if (r1.getString("user_status").equalsIgnoreCase("recipient")) {
            query2 =
                    "SELECT SUM(amount_blood) as sumAmountBlood," +
                            "COUNT(amount_blood) as countAmountBlood" +
                            " FROM Blood_product" +
                            " WHERE Recipient_ID =" + ID;
        }
        ResultSet r2 = eQ(query2);
        r2.next();
        String search_result = "ID: " + r1.getString("ID") + "\n";
        search_result += "First name: " +  r1.getString("First_name") + "\n";
        search_result += "Last name: " +  r1.getString("Last_name") + "\n";
        search_result += "Address: " +  r1.getString("address") + "\n";
        search_result += "Phone number: " +  r1.getString("Phone_number") + "\n";
        search_result += "Email: " +  r1.getString("email") + "\n";
        search_result += "Blood type: " +  r1.getString("Blood_type") + "\n";
        search_result += "as " +  r1.getString("user_status") + "\n";
        search_result += "Number of times" +  r2.getString("countAmountBlood") + "\n";
        search_result += "Total amount: " +  r2.getString("sumAmountBlood") + "\n";

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

    private ResultSet eQ(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlQuery);

    }
    public  int getDonationCountForCurrentMonth() throws SQLException {
        String sqlQuery = "SELECT COUNT(*) " +
                "FROM Blood_product " +
                "WHERE EXTRACT(YEAR FROM Date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
                "AND EXTRACT(MONTH FROM Date) = EXTRACT(MONTH FROM CURRENT_DATE)";

        ResultSet resultSet = eQ(sqlQuery);
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

        ResultSet resultSet = eQ(sqlQuery);
        while (resultSet.next()) {
            String bloodType = resultSet.getString("blood_type");
            int donationCount = resultSet.getInt("donation_count");
            donationStatistics.put(bloodType, donationCount);
        }
        return  donationStatistics;
    }

    public List<SystemUser> getSystemUser() throws SQLException {

        List<SystemUser> systemUsers = new ArrayList<>();
        String q = "select * \n" +
                "from Person NATURAL join system_user";
        ResultSet  resultSet = eQ(q);

        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String address = resultSet.getString("address");
            String phoneNumber = resultSet.getString("phone_number");
            String email = resultSet.getString("email");
            String bloodType = resultSet.getString("blood_type");
            String medicalHistory = resultSet.getString("medical_history");
            systemUsers.add(new SystemUser(id,firstName,lastName,address,phoneNumber,email,bloodType,medicalHistory));

        }
        System.out.println(systemUsers);
        return systemUsers;

    }
}
