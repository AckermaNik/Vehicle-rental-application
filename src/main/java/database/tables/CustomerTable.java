/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Customer;

/**
 *
 * @author 30697
 */
public class CustomerTable {
    public void addCustomerFromJSON(String json) throws ClassNotFoundException {
        Customer customer = jsonToCustomer(json);
        addNewCustomer(customer);
    }

    public Customer jsonToCustomer(String json) {
        Gson gson = new Gson();

        Customer customer = gson.fromJson(json, Customer.class);
        return customer;
    }

    public String CustomerToJSON(Customer customer) {
        Gson gson = new Gson();

        String json = gson.toJson(customer, Customer.class);
        return json;
    }

    public void printCustomerDetails(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM customers WHERE  username= '" + username + "'");
            while (rs.next()) {
                System.out.println("===Result===");
                DB_Connection.printResults(rs);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }


    public String databaseCustomerToJSON(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM customers WHERE username = '" + username + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void createCustomersTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE customers "
                + "(   cid INTEGER not NULL AUTO_INCREMENT,"
                + "    f_name VARCHAR(30) not null,"
                + "    l_name VARCHAR (30) not null,"
                + "    address VARCHAR(40) not null,"
                + "    birthdate DATE not null,"
                + "    card_no VARCHAR(20) not null,"
                + "    driver_lic_no VARCHAR (15) not null," //10 for Greece
                + "    username VARCHAR(30) not null,"
                + " PRIMARY KEY (cid))";

        stmt.execute(query);
        stmt.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewCustomer(Customer customer) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " customers (f_name,l_name,address,birthdate,card_no,driver_lic_no,username)"
                    + " VALUES ("
                    + "'" + customer.getF_name() + "',"
                    + "'" + customer.getL_name() + "',"
                    + "'" + customer.getAddress() + "',"
                    + "'" + customer.getBirthdate() + "',"
                    + "'" + customer.getCard_no() + "',"
                    + "'" + customer.getDriver_lic_no() + "',"
                    + "'" + customer.getUsername() + "')";

            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The customer was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
//            insertQuery = "ALTER TABLE rented_vehs ADD COLUMN valid_bit INTEGER";
//            stmt.execute(insertQuery);

            stmt.close();


        } catch (SQLException ex) {
            Logger.getLogger(CustomerTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Customer databaseToCustomerFromUsername(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM customers WHERE username = '" + username + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Customer customer = gson.fromJson(json, Customer.class);
            return customer;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
}
