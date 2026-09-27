/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database.tables;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Service;

/**
 *
 * @author 30697
 */
public class ServiceTable {


    public Service jsonToService(String json) {
        Gson gson = new Gson();

        Service vehicle = gson.fromJson(json, Service.class);
        return vehicle;
    }


    public Service databaseToService(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM service WHERE reg_no = '" + reg_no + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Service vehicle = gson.fromJson(json, Service.class);
            return vehicle;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Double GetTotalCost(String startDate, String endDate) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject json;
        Double totalCost = 0.0;

//        System.out.println("Start Date: " + startDate);
//        System.out.println("End Date: " + endDate);
        String select = "SELECT SUM(cost) FROM service WHERE import_date BETWEEN ? AND ?";
        PreparedStatement preparedStatement = con.prepareStatement(select);
        preparedStatement.setString(1, startDate);
        preparedStatement.setString(2, endDate);

        rs = preparedStatement.executeQuery();

        if (rs.next()) {
            // Assuming DB_Connection.getResultsToJSONObject returns a JsonObject correctly
            //json = DB_Connection.getResultsToJSONObject(rs);
            totalCost = rs.getDouble(1);
            System.out.println("total cost: " + totalCost);

            // Extract the total cost from the JsonObject
            //totalCost = json.get("total_cost_sum").getAsDouble();
        }

        return totalCost;
    }


    public void createServicesTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE service "
                + "(sid INTEGER AUTO_INCREMENT not NULL,"
                + "reg_no VARCHAR(10) not null,"
                + "    duration INTEGER,"
                + "    import_date DATE not null,"
                + "    cost DOUBLE not null,"
                + "PRIMARY KEY(sid),"
                + "FOREIGN KEY (reg_no) REFERENCES vehicles(reg_no))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewService(Service vehicle) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " service (reg_no,duration,import_date,cost)"
                    + " VALUES ("
                    + "'" + vehicle.getReg_no() + "',"
                    + "'" + vehicle.getDuration() + "',"
                    + "'" + vehicle.getImport_date() + "',"
                    + "'" + vehicle.getCost() + "')";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The vehicle for service was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(ServiceTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
