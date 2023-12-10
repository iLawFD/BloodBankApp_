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

    public Person getCurrentSystemUser() {
        return currentSystemUser;
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
        if ("admin".equals(personType)) {
            // create new admin object
           ResultSet resultSetUSer = eQ("select * from person natural join admin where id = "+ID);
           if(resultSetUSer.next()){
               int id = resultSetUSer.getInt("id");
               String firstName = resultSetUSer.getString("first_name");
               String lastName = resultSetUSer.getString("last_name");
               String address = resultSetUSer.getString("address");
               String phoneNumber = resultSetUSer.getString("phone_number");
               String email = resultSetUSer.getString("email");
               int officeNumber = resultSetUSer.getInt("office_number");
               currentSystemUser = new Admin(ID,firstName,lastName,address,phoneNumber,email,officeNumber);
               System.out.println(currentSystemUser);

           }
        } else if ("system_user".equals(personType)) {
            // create new system user object
            ResultSet resultSetUSer = eQ("select * from person natural join system_user where id = "+ID);
            if(resultSetUSer.next()){
                int id = resultSetUSer.getInt("id");
                String firstName = resultSetUSer.getString("first_name");
                String lastName = resultSetUSer.getString("last_name");
                String address = resultSetUSer.getString("address");
                String phoneNumber = resultSetUSer.getString("phone_number");
                String email = resultSetUSer.getString("email");
                String bloodType = resultSetUSer.getString("blood_type");
                String medicalHistory = resultSetUSer.getString("medical_history");
                currentSystemUser = new SystemUser(ID,firstName,lastName,address,phoneNumber,email,bloodType,medicalHistory);
                System.out.println(currentSystemUser);


            }

        }
        return  currentSystemUser;

    }
    public void updateUser(int id, String firstName, String lastName, String address, String phoneNumber, String email, String bloodType, String newMedicalHistory) throws SQLException {
        // Update the Person table
        String updatePersonQuery = "UPDATE Person " +
                "SET First_name = ?, Last_name = ?, " +
                "address = ?, Phone_number = ?, " +
                "email = ? " +
                "WHERE ID = ?";

        PreparedStatement preparedStatementPerson = connection.prepareStatement(updatePersonQuery);
        preparedStatementPerson.setString(1, firstName);
        preparedStatementPerson.setString(2, lastName);
        preparedStatementPerson.setString(3, address);
        preparedStatementPerson.setString(4, phoneNumber);
        preparedStatementPerson.setString(5, email);
        preparedStatementPerson.setInt(6, id);

        int rowsAffectedPerson = preparedStatementPerson.executeUpdate();


        String updateSystemUserQuery = "UPDATE System_user " +
                "SET Blood_type = ?, medical_history = ? " +
                "WHERE ID = ?";

        PreparedStatement preparedStatementSystemUser = connection.prepareStatement(updateSystemUserQuery);
        preparedStatementSystemUser.setString(1, bloodType);
        preparedStatementSystemUser.setString(2, newMedicalHistory);
        preparedStatementSystemUser.setInt(3, id);

        int rowsAffectedSystemUser = preparedStatementSystemUser.executeUpdate();

    }
    public void insertNewUser(int userID,String firstName, String lastName,
                                     String address, String phoneNumber, String email,String bloodType,String medicalHistory) throws SQLException{

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

        String insertQuery2 = "INSERT INTO system_user ( ID,blood_type, medical_history, user_status) " +
                "VALUES (?,?, ?, ? )";
        preparedStatement = connection.prepareStatement(insertQuery2);

        preparedStatement.setInt(1, userID);
        preparedStatement.setString(2, bloodType);
        preparedStatement.setString(3, medicalHistory);
        preparedStatement.setString(4, "donor");

        rowsAffected = preparedStatement.executeUpdate();


    }
    public void removeSystemUser(int id) throws SQLException{
        String[] deleteQueries = {
                "DELETE FROM User_Modification WHERE User_ID = ?",
                "DELETE FROM Donor WHERE ID = ?",
                "DELETE FROM Recipient WHERE ID = ?",
                "DELETE FROM System_user WHERE ID = ?",
                "DELETE FROM Person WHERE ID = ?",
                "DELETE FROM Blood_product WHERE Donor_ID = ?"
        };
        for(String query : deleteQueries){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }



    }

    public static void main(String[] args) {
        try {
            DataBase.getDataBase().removeSystemUser(3);
        } catch (SQLException e) {
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
        String query1 = "SELECT * FROM system_user natural JOIN person where Id = " + ID;
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

    public List<SystemUser> getSystemUsers() throws SQLException {

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
        return systemUsers;

    }

    public String getEmail(int ID) throws SQLException {
        ResultSet resultSet = eQ("select * from person where id= "+ID);
        if(resultSet.next()){
            return  resultSet.getString("email");
        }

        return  "";

    }

    public String getNewEmail(int ID) throws SQLException {
        ResultSet resultSet = eQ("select * from user_modification where user_id= "+ID);
        if(resultSet.next()){
            return  resultSet.getString("new_email");
        }

        return  "";

    }

    public void requestModification(int ID,String firstName,String lastName, String phone, String address, String bloodType, String medicalHistory, String email) throws SQLException {

        String sql = "INSERT INTO User_Modification (User_ID, New_First_Name, New_Last_Name, New_Address, " +
                "New_Phone_Number, New_Email, New_Blood_Type, New_Medical_History, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, ID);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, phone);
        preparedStatement.setString(6, email);
        preparedStatement.setString(7, bloodType);
        preparedStatement.setString(8, medicalHistory);
        preparedStatement.setString(9, "pending");

        int rowsAffected = preparedStatement.executeUpdate();

    }
    public void endCurrentUserSession(){
        currentSystemUser = null;
    }

//    public

    public void acceptModification(int ID) throws SQLException {
        String updatePersonQuery = "UPDATE user_modification " +"SET status = ? where modification_id ="+ID;
        PreparedStatement preparedStatementPerson = connection.prepareStatement(updatePersonQuery);
        preparedStatementPerson.setString(1, "approved");
        int rowsAffectedPerson = preparedStatementPerson.executeUpdate();
        String getSql = "SELECT * FROM user_modification WHERE modification_id ="+ID;
        ResultSet resultSet =  eQ(getSql);

        if(resultSet.next()){
            int userid = resultSet.getInt("user_id");
            String firstName = resultSet.getString("new_first_name");
            String lastName = resultSet.getString("new_last_name");
            String address = resultSet.getString("new_address");
            String phoneNumber = resultSet.getString("new_phone_number");
            String email = resultSet.getString("new_email");
            String bloodType = resultSet.getString("new_blood_type");
            String newMedicalHistory = resultSet.getString("new_medical_history");

            updateUser(userid,firstName,lastName,address,phoneNumber,email,bloodType,newMedicalHistory);

        }


    }
    public void rejectModification(int id) throws SQLException {
        String updatePersonQuery = "UPDATE user_modification " +"SET status = ?";
        PreparedStatement preparedStatementPerson = connection.prepareStatement(updatePersonQuery);
        preparedStatementPerson.setString(1, "rejected");
        int rowsAffectedPerson = preparedStatementPerson.executeUpdate();

    }


    public List<List<String>>  getRequests() throws SQLException {
        List<List<String>> requests = new ArrayList<>();

        ResultSet resultSet =  eQ("SELECT * FROM user_modification WHERE status = 'pending'");

        while (resultSet.next()){
            ArrayList<String> request  = new ArrayList<>();
            request.add(String.valueOf(resultSet.getInt("modification_id")));
            request.add(String.valueOf(resultSet.getInt("user_id")));
            requests.add(request);
        }

        return requests;
    }
    // for the recip
    public void requestBlood(){
        String checkingQuery = "SELECT * FROM system_user where Id " + currentSystemUser.getID();
    }
    // for the donor
    public void donateBlood(int weight,int age, int id){

        String checkingQuery = "INSERT INTO donor (weight,age,id) ";


        PreparedStatement preparedStatement;
        try {


            String insertQuery = "INSERT INTO donor (weight,age,id) " +
                    "VALUES (?,?,?)";

            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, weight);
            preparedStatement.setInt(1, age);
            preparedStatement.setInt(1, id);
            preparedStatement.setda(1, id);




            int rowsAffected = preparedStatement.executeUpdate();


        } catch (SQLException e) {

        }




    }


}
