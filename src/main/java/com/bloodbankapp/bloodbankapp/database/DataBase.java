package com.bloodbankapp.bloodbankapp.database;
import com.bloodbankapp.bloodbankapp.Controllers.*;

import javax.xml.xpath.XPathEvaluationResult;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
            DataBase.getDataBase().retrieveUserInfo(233);
//            int ID, int requestID, String bloodType
            DataBase.getDataBase().insertNewDonor(23,75);
//            DataBase.getDataBase().fulfillBloodRequests(111,5,"A+");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        try {
//            System.out.println(DataBase.getDataBase().bloodRequests());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }



    public int getDonationCountForCurrentWeek() throws SQLException {
        String sqlQuery = "SELECT COUNT(*) " +
                "FROM donation " +
                "WHERE donation_date >= CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE)::INTEGER " +
                "AND donation_date < CURRENT_DATE + (7 - EXTRACT(DOW FROM CURRENT_DATE)::INTEGER)";


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
                "FROM donation " +
                "WHERE EXTRACT(YEAR FROM donation_date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
                "AND EXTRACT(MONTH FROM donation_date) = EXTRACT(MONTH FROM CURRENT_DATE)";

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
                "FROM donation " +
                "WHERE donation_status = 'stored' "+
                "GROUP BY blood_type";

        ResultSet resultSet = eQ(sqlQuery);
        while (resultSet.next()) {
            String bloodType = resultSet.getString("blood_type");
            int donationCount = resultSet.getInt("donation_count");
            donationStatistics.put(bloodType, donationCount);
        }
        return  donationStatistics;
    }

    public Map<String, Integer> getNumberOfUser() throws SQLException {
        Map<String, Integer> donationStatistics = new HashMap<>();

        String sqlQuery = "SELECT COUNT(ID) AS donors_number " +
                "FROM donor";
        ResultSet resultSet = eQ(sqlQuery);
        resultSet.next();
        donationStatistics.put("number of donors", resultSet.getInt("donors_number"));

        sqlQuery = "SELECT COUNT(ID) AS recipients_number  " +
                "FROM recipient";

        resultSet = eQ(sqlQuery);
        resultSet.next();
        donationStatistics.put("number of recipients", resultSet.getInt("recipients_number"));
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
        PreparedStatement preparedStatement;
        try{
            String insertQuery = "INSERT INTO recipient (ID) " +
                    "VALUES (?)";

            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setInt(1, currentSystemUser.getID());
            int rowsAffected = preparedStatement.executeUpdate();

            String insertQuery2 = "INSERT INTO recipient_request (request_status,ID,blood_type,request_date) " +
                    "VALUES (?,?,?,current_date)";
            preparedStatement = connection.prepareStatement(insertQuery2);

            preparedStatement.setString(1, "pending");
            preparedStatement.setInt(2, currentSystemUser.getID());
            preparedStatement.setString(3, ((SystemUser) currentSystemUser).getBloodType());


            rowsAffected = preparedStatement.executeUpdate();

        }catch (SQLException e){
            try {

                String insertQuery2 = "INSERT INTO recipient_request (request_status,ID,blood_type,request_date) " +
                        "VALUES (?,?,?,current_date)";
                preparedStatement = connection.prepareStatement(insertQuery2);

                preparedStatement.setString(1, "pending");
                preparedStatement.setInt(2, currentSystemUser.getID());
                preparedStatement.setString(3, ((SystemUser) currentSystemUser).getBloodType());


                preparedStatement.executeUpdate();


            }catch (SQLException e2){
                System.out.println("erroro");
            }


        }
        String query = "";
    }

    // for the donor

    // if you want to donate without request
    public void donateBlood() throws SQLException {


        //
        String query = "INSERT INTO donation(donation_status, blood_type,blood_drive_number,donation_date,ID) " +
                "VALUES (?, ?, ?, ?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,"stored");
        preparedStatement.setString(2, ((SystemUser) currentSystemUser).getBloodType());
        preparedStatement.setInt(3, getCurrentDriveNumber());
        preparedStatement.setDate(4, new Date(Calendar.getInstance().getTime().getTime()));
        preparedStatement.setInt(5, currentSystemUser.getID());
        preparedStatement.executeUpdate();

    }

//    public void donateBloodFillRequest (int requestNumber) throws SQLException {
//
//        String query = "INSERT INTO donation(donation_status, blood_type,blood_drive_number,request_date,request_id,ID) " +
//                "VALUES (?, ?, ?, ?)";
//        PreparedStatement preparedStatement = connection.prepareStatement(query);
//        preparedStatement.setString(1,"donated");
//        preparedStatement.setString(2, ((SystemUser) currentSystemUser).getBloodType());
//        preparedStatement.setInt(3, getCurrentDriveNumber());
//        preparedStatement.setDate(3, new Date(Calendar.getInstance().getTime().getTime()));
//        preparedStatement.setInt(4, currentSystemUser.getID());
//        preparedStatement.executeUpdate();
//        // also update the request
//
//
//
//    }


    //it creates request for the admin and them in the "admin request" table and "recipient request table"
//    public void createRequest(String bloodType) throws SQLException{
//        String query = "INSERT INTO admin_request(status, date,blood_type,ID) " +
//                "VALUES (?, ?, ?, ?)";
//        String queryUpdate = "UPDATE System_user SET blood_type = '" + bloodType + "' WHERE id = " + currentSystemUser.getID();
//
//        PreparedStatement preparedStatement = connection.prepareStatement(query);
//        preparedStatement.setString(1,"Completed");
//        preparedStatement.setDate(2, new Date(Calendar.getInstance().getTime().getTime()));
//        preparedStatement.setString(3, bloodType);
//        preparedStatement.setInt(4, currentSystemUser.getID());
//        preparedStatement.executeUpdate();
//
//        preparedStatement = connection.prepareStatement(queryUpdate);
//        preparedStatement.executeUpdate();
//        requestBlood();
//    }
    //it fulfills the requests in the "recipient request table" by changing the status to completed
    //and adding a new entry in the donation table
    public void insertNewDonor(int age, int weight) throws SQLException {
        String st= "INSERT INTO donor(weight,age,id) "+
                "VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(st);
        preparedStatement.setInt(1,weight);
        preparedStatement.setInt(2,age);
        preparedStatement.setInt(3,getCurrentSystemUser().getID());
        preparedStatement.executeUpdate();

    }

    public void createPayments() throws SQLException {
        int amount = 0;
        int id = getCurrentSystemUser().getID();
        ResultSet resultSet =  eQ("SELECT Blood_type FROM System_user WHERE ID=" + id);
        String bloodType =  resultSet.getString("Blood_type");

        if ("A+".equals(bloodType)) {
            amount = 100;
        } else if ("A-".equals(bloodType)) {
            amount = 120;
        } else if ("B+".equals(bloodType)) {
            amount = 100;
        } else if ("B-".equals(bloodType)) {
            amount = 150;
        } else if ("AB+".equals(bloodType)) {
            amount = 180;
        } else if ("AB-".equals(bloodType)) {
            amount = 200;
        } else if ("O+".equals(bloodType)) {
            amount = 90;
        } else if ("O-".equals(bloodType)) {
            amount = 220; // O- is often considered the universal donor and might be in higher demand
        } else {
            amount = 50; // Default amount if blood type doesn't match any of the above
        }
        // create payment with this amount
        String checkingQuery = "";
    }
    public static String[] checkBloodCompatibility(String bloodType) {
        String[] acceptedBloodTypes;

        // Check A+
        if (bloodType.equals("A+")) {
            acceptedBloodTypes = new String[]{"A+", "AB+"};
        }
        // Check A-
        else if (bloodType.equals("A-")) {
            acceptedBloodTypes = new String[]{"A+", "A-", "AB+", "AB-"};
        }
        // Check B+
        else if (bloodType.equals("B+")) {
            acceptedBloodTypes = new String[]{"B+", "AB+"};
        }
        // Check B-
        else if (bloodType.equals("B-")) {
            acceptedBloodTypes = new String[]{"B+", "B-", "AB+", "AB-"};
        }
        // Check AB+
        else if (bloodType.equals("AB+")) {
            acceptedBloodTypes = new String[]{"AB+"};
        }
        // Check AB-
        else if (bloodType.equals("AB-")) {
            acceptedBloodTypes = new String[]{"AB+", "AB-"};
        }
        // Check O+
        else if (bloodType.equals("O+")) {
            acceptedBloodTypes = new String[]{"A+", "B+", "AB+", "O+"};
        }
        // Check O-
        else if (bloodType.equals("O-")) {
            acceptedBloodTypes = new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        }
        // Default case
        else {
            acceptedBloodTypes = new String[0]; // Empty array for unknown blood types
        }
        return acceptedBloodTypes;
    }



    public static boolean isComp(String bloodTypeDon, String bloodTypeRec) {

        String[] acceptedBloodTypes = checkBloodCompatibility(bloodTypeRec);
        for (int i = 0; i < acceptedBloodTypes.length; i++) {
            if (acceptedBloodTypes[i].equals(bloodTypeDon)) {
                return true;
            }
        }
        return false;


    }

    public  String getRecpBlood(int id) throws SQLException {


        ResultSet resultSet = eQ("SELECT Blood_type FROM System_user WHERE ID=" + id);
        String bloodType = "";

        if (resultSet.next()) {
            resultSet.getString("Blood_type");
        }

        return bloodType;
    }

    public void initiateNewBloodDrive(String message) throws SQLException {

        String query = "INSERT INTO blood_drive(status, start_date,message,ID) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,"open");
        preparedStatement.setDate(2, new Date(Calendar.getInstance().getTime().getTime()));
        preparedStatement.setString(3, message);
        preparedStatement.setInt(4, currentSystemUser.getID());
        preparedStatement.executeUpdate();

    }
    public int getCurrentDriveNumber() throws SQLException {

        String qeury = "SELECT blood_drive_number FROM blood_drive where status = 'open'";

        ResultSet resultSet = eQ(qeury);

        resultSet.next();
        return  resultSet.getInt("blood_drive_number");
    }
    public String getCurrentDriveMessage() throws SQLException {

        String qeury = "SELECT * FROM blood_drive where status = 'open'";

        ResultSet resultSet = eQ(qeury);

        resultSet.next();
        return  resultSet.getString("message");
    }
    public int getCurrentDriveNumberOfDonations() throws SQLException {

        String qeury = "SELECT count(donation_id) FROM donation where blood_drive_number ="+getCurrentDriveNumber();

        ResultSet resultSet = eQ(qeury);

        resultSet.next();
        return  resultSet.getInt("count");
    }

    public ArrayList<BloodRequest> bloodRequests() throws SQLException {
        ArrayList<BloodRequest> bloodRequests = new ArrayList<>();
        String res = "select * from recipient_request where request_status = 'pending'";
        ResultSet resultSet = eQ(res);

        while (resultSet.next()){
            BloodRequest bloodRequest = new BloodRequest(
                    resultSet.getInt("request_id"),
                    resultSet.getDate("request_date"),
                    resultSet.getString("blood_type"),
                    resultSet.getInt("id")

            );
            bloodRequests.add(bloodRequest);
        }
        return  bloodRequests;
    }
    public int bloodTypeToPrice(String bloodType){
        int amount = 0;
        if ("A+".equals(bloodType)) {
            amount = 100;
        } else if ("A-".equals(bloodType)) {
            amount = 120;
        } else if ("B+".equals(bloodType)) {
            amount = 100;
        } else if ("B-".equals(bloodType)) {
            amount = 150;
        } else if ("AB+".equals(bloodType)) {
            amount = 180;
        } else if ("AB-".equals(bloodType)) {
            amount = 200;
        } else if ("O+".equals(bloodType)) {
            amount = 90;
        } else if ("O-".equals(bloodType)) {
            amount = 220; // O- is often considered the universal donor and might be in higher demand
        } else {
            amount = 50; // Default amount if blood type doesn't match any of the above
        }
        return amount;
    }
//    public void fulfillBloodRequests(int ID, int requestID, String bloodType) throws SQLException, IOException {
//
//        String donQ = "SELECT * FROM donation WHERE donation_status = 'stored'";
//        String reqQUpdate = "UPDATE recipient_request SET request_status = 'completed' WHERE request_id = " + requestID;
//        String donQUpdateFrag1 = "UPDATE donation SET donation_status = 'donated', request_id = ";
//        String donQUpdateFrag2 = " WHERE donation_id = ";
//
//        String[] acceptedBloodTypes = checkBloodCompatibility(bloodType);
//        ResultSet r1 = eQ(donQ);
//        while(r1.next()){
//            if (Arrays.stream(acceptedBloodTypes).anyMatch(r1.getString("blood_type")::equals)){
//                donQUpdateFrag1 += r1.getString("request_id")+ donQUpdateFrag2 +r1.getInt("donation_id");
//                connection.prepareStatement(donQUpdateFrag1).executeUpdate();
//                connection.prepareStatement(reqQUpdate).executeUpdate();
//                fulfillPayment(requestID,bloodType);
//                EmailSender.getEmailSender().SendMessage(
//                        getEmail(ID),
//                        "A blood was send to you",
//                        "Payment has been created"
//                );
//                return;
//            }
//        }
//    }
public void fulfillBloodRequests(int ID, int requestID, String bloodType) throws SQLException, IOException {
    String donQ = "SELECT * FROM donation WHERE donation_status = 'stored'";
    String reqQUpdate = "UPDATE recipient_request SET request_status = 'completed' WHERE request_id = ?";
    String donQUpdateFrag1 = "UPDATE donation SET donation_status = 'donated', request_id = ? WHERE donation_id = ?";

    String[] acceptedBloodTypes = checkBloodCompatibility(bloodType);
    ResultSet r1 = eQ(donQ);
    while (r1.next()) {
        if (Arrays.stream(acceptedBloodTypes).anyMatch(r1.getString("blood_type")::equals)) {
            int donationId = r1.getInt("donation_id");
            connection.setAutoCommit(false);

            PreparedStatement updateDonationStmt = connection.prepareStatement(donQUpdateFrag1);
            updateDonationStmt.setInt(1, requestID);
            updateDonationStmt.setInt(2, donationId);
            updateDonationStmt.executeUpdate();

            PreparedStatement updateRequestStmt = connection.prepareStatement(reqQUpdate);
            updateRequestStmt.setInt(1, requestID);
            updateRequestStmt.executeUpdate();

            connection.commit();
            fulfillPayment(requestID, bloodType);
            EmailSender.getEmailSender().SendMessage(
                    getEmail(ID),
                    "A blood was sent to you",
                    "Payment has been created"
            );
            connection.setAutoCommit(true);
            return;
        }
    }
}


    public void fulfillPayment(int requestID, String bloodType) throws SQLException{
        String insertQuery = "INSERT INTO payment(amount,status, request_ID) VALUES(?, ?, ?)";
        PreparedStatement p = connection.prepareStatement(insertQuery);
        p.setInt(1,bloodTypeToPrice(bloodType));
        p.setString(2,"uncompleted");
        p.setInt(3,requestID);
        p.executeUpdate();
    }



    public ArrayList<Donation> getDonations() throws SQLException {
        ArrayList<Donation> donationArrayList = new ArrayList<>();

        String res = "select * from donation where id = "+currentSystemUser.getID();
        ResultSet resultSet = eQ(res);

        while (resultSet.next()){
            Donation bloodRequest = new Donation(
                    resultSet.getInt("donation_id"),
                    resultSet.getString("donation_status"),
                    resultSet.getInt("request_id"),
                    resultSet.getDate("donation_date"),
                    resultSet.getString("blood_type"),
                    resultSet.getInt("blood_drive_number")


            );
            donationArrayList.add(bloodRequest);
        }
        return  donationArrayList;
    }

    public ArrayList<userBloodRequest> getUserBloodRequest() throws SQLException {
        ArrayList<userBloodRequest> userBloodRequests = new ArrayList<>();

        String res = "select * from recipient_request natural join payment where id= "+currentSystemUser.getID();
        ResultSet resultSet = eQ(res);

        while (resultSet.next()){
            userBloodRequest userBloodRequest = new userBloodRequest(
                    resultSet.getInt("request_id"),
                    resultSet.getString("request_status"),
                    resultSet.getDate("request_date"),
                    resultSet.getString("blood_type"),
                    resultSet.getInt("amount"),
                    resultSet.getString("status"),
                    resultSet.getInt("payment_id")


            );
            userBloodRequests.add(userBloodRequest);
        }
        return  userBloodRequests;
    }

    public void confirmPayments(int ID) throws SQLException {

        String statement = "UPDATE payment " +
                "SET status = 'completed' " +
                "WHERE payment_id = " + ID;

        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.executeUpdate();

    }



}
