package com.bloodbankapp.bloodbankapp.database;


import java.sql.*;

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
            System.out.println(resultSet.getString("first_name"));
            System.out.println(resultSet.getString("ID"));
            currentSystemUser = new Admin();
        } else if ("system_user".equals(personType)) {
            System.out.println(resultSet.getString("first_name"));
            System.out.println(resultSet.getString("ID"));
            currentSystemUser = new SystemUser();
        }

        return  currentSystemUser;

    }
    public void updateCurrentUserSystemData() throws SQLException {
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
    void removeSystemUser(int id) throws SQLException {
        String deleteQuery = "DELETE FROM person WHERE ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, id);

        int rowsAffected = preparedStatement.executeUpdate();
    }

    public static void main(String[] args) {
        try {
            System.out.println(getDataBase().getDonationCountForCurrentWeek());
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
