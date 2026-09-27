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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Rent;
import mainClasses.StatPack;
import mainClasses.Vehicle;

/**
 *
 * @author 30697
 */
public class RentTable {

    public void addRentFromJSON(String json) throws ClassNotFoundException, SQLException {

        Rent rented_veh = jsonToRent(json);
        addNewRent(rented_veh);
    }

    public Rent jsonToRent(String json) {
        Gson gson = new Gson();

        Rent rented_veh = gson.fromJson(json, Rent.class);
        return rented_veh;
    }

    public double CalculateCost(String reg_no, String final_date, String final_hour) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject json1, json2;

        String ret_date = "SELECT return_date FROM rented_vehs WHERE reg_no= '" + reg_no + "'AND valid_bit=1";
        rs = stmt.executeQuery(ret_date);
        rs.next();

        json1 = DB_Connection.getResultsToJSONObject(rs);
        String expected_date = json1.get("return_date").getAsString();
        System.out.println(expected_date);

        String ret_hour = "SELECT return_hour FROM rented_vehs WHERE reg_no= '" + reg_no + "'AND valid_bit=1";
        rs = stmt.executeQuery(ret_hour);
        rs.next();

        json2 = DB_Connection.getResultsToJSONObject(rs);
        String expected_hour = json2.get("return_hour").getAsString();
        System.out.println(expected_hour);

        // Convert strings to LocalDate and LocalTime
        LocalDate finalDate = LocalDate.parse(final_date);
        LocalTime finalTime = LocalTime.parse(final_hour);

        LocalDate expectedDate = LocalDate.parse(expected_date);
        LocalTime expectedTime = LocalTime.parse(expected_hour);

        // Compare dates and times
        if (finalDate.isBefore(expectedDate) || (finalDate.isEqual(expectedDate) && finalTime.isBefore(expectedTime))) {
            // The final date and time are before the expected ones
            return 0.0;
        } else if (finalDate.isEqual(expectedDate) && finalTime.equals(expectedTime)) {
            // The final date and time are equal to the expected ones
            return 0.0;
        } else {
            // The final date and time are after the expected ones

            // Combine date and time to create LocalDateTime instances
            LocalDateTime finalDateTime = LocalDateTime.of(finalDate, finalTime);
            LocalDateTime expectedDateTime = LocalDateTime.of(expectedDate, expectedTime);

            // Calculate the duration between expected and final return times
            Duration duration = Duration.between(expectedDateTime, finalDateTime);

            // Calculate cost based on the number of hours passed
            long hoursPassed = duration.toHours();
            double hourlyRate = 10.0;
            double extraCost = hoursPassed * hourlyRate;
            return extraCost;
        }

        //return null;
    }

    public void UpdateCostandValid(String reg_no, double total) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        System.out.println("reg no and total: " + reg_no + total);
        String update = "UPDATE rented_vehs SET total_cost=" + total + ", valid_bit=0 WHERE  reg_no= '" + reg_no + "'AND valid_bit=1";
        stmt.executeUpdate(update);
        stmt.close();
    }

    public Double Get_CostByTime(String startDate, String endDate) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject json;
        Double totalCost = 0.0;

        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);

        String select = "SELECT SUM(total_cost) FROM rented_vehs WHERE rent_date BETWEEN ? AND ?";
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

    public Double Get_CostByType(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject json;
        Double totalCost = 0.0;

        System.out.println("type: " + type);

        String select = "SELECT SUM(total_cost) FROM rented_vehs r "
                + "JOIN vehicles v ON r.reg_no = v.reg_no "
                + "WHERE v.type = '" + type + "'";

        rs = stmt.executeQuery(select);

        if (rs.next()) {
            // Assuming DB_Connection.getResultsToJSONObject returns a JsonObject correctly
            //json = DB_Connection.getResultsToJSONObject(rs);
            totalCost = rs.getDouble(1);
            System.out.println("vehicle cost: " + totalCost);

            // Extract the total cost from the JsonObject
            //totalCost = json.get("total_cost_sum").getAsDouble();
        }

        return totalCost;
    }

    public int Get_StatusByTime(String startDate, String endDate) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject json;

        String select = "SELECT COUNT(*) AS rental_count FROM rented_vehs WHERE rent_date BETWEEN ? AND ?";
        PreparedStatement preparedStatement = con.prepareStatement(select);
        preparedStatement.setString(1, startDate);
        preparedStatement.setString(2, endDate);

        rs = preparedStatement.executeQuery();
        int rentalCount = 0;
        if (rs.next()) {
            rentalCount = rs.getInt("rental_count");
            System.out.println("Number of rentals between " + startDate + " and " + endDate + ": " + rentalCount);
        }

        return rentalCount;
    }

    public ArrayList<Rent> getAll_Rents() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Rent> rented_vehs = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM rented_vehs WHERE valid_bit=1");

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Rent rented_veh = gson.fromJson(json, Rent.class);
                rented_vehs.add(rented_veh);
            }
            return rented_vehs;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Double Get_Total_Cost(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject json;

        String select = "SELECT total_cost FROM rented_vehs WHERE  reg_no= '" + reg_no + "'AND valid_bit=1";
        rs = stmt.executeQuery(select);

        rs.next();

        json = DB_Connection.getResultsToJSONObject(rs);
        return json.get("total_cost").getAsDouble();
    }


    public String LookUp_DriversName(String f_name, String l_name, String username) throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        System.out.println(f_name + "  " + l_name + "  " + username);
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM rented_vehs WHERE username = '" + username + "' AND valid_bit= 1 AND driver_fname = '" + f_name + "' AND driver_lname = '" + l_name + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            return json;    //an DEN yparxei hdh pelaths me oxhma kai auton ton odhgo epistrefei json alliws exw Exception kai synexizw
        } catch (Exception e) {
            System.err.println("Got an exception SQL! ");
            System.err.println(e.getMessage());
        }

        try {    //an pelaths nikoiazei 1h fora me to diko tou onoma gia odigo
            rs = stmt.executeQuery("SELECT * FROM customers WHERE username = '" + username + "' AND  f_name = '" + f_name + "' AND  l_name = '" + l_name + "'");
            rs.next();
            System.out.println(DB_Connection.getResultsToJSON(rs)); // trick - anagkaio
        } catch (SQLException e) {
            System.err.println("Got an exception driver! NULL ");
            System.err.println(e.getMessage());
            return "NOT the customer as driver";   // trick gia thn !=null synthikh sto CheckDriver.java

        } catch (NullPointerException n) {
            System.err.println("Got an exception driver! NULL ");
            return "NOT the customer as driver";   // trick gia thn !=null synthikh sto CheckDriver.java
        }

        return null;

    }

    public void Update_Count(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String update = "UPDATE vehicles SET count=count + 1 WHERE  reg_no= '" + reg_no + "'"; // update rent count for the vehicle
        stmt.executeUpdate(update);
        stmt.close();
    }

    public Double Get_Daily_Cost(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject json;

        String select = "SELECT daily_rent_cost FROM vehicles WHERE  reg_no= '" + reg_no + "'";
        rs = stmt.executeQuery(select);

        rs.next();

        json = DB_Connection.getResultsToJSONObject(rs);
        return json.get("daily_rent_cost").getAsDouble();
    }

    // get Daily insurance cost
    public Double Get_Insurance_Cost(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject json;

        String select = "SELECT daily_insurance_cost FROM vehicles WHERE  reg_no= '" + reg_no + "'";
        rs = stmt.executeQuery(select);

        rs.next();

        json = DB_Connection.getResultsToJSONObject(rs);
        return json.get("daily_insurance_cost").getAsDouble();

    }

    public void createRentsTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE rented_vehs "
                + "( rid INTEGER AUTO_INCREMENT not NULL,"
                + "    username VARCHAR(30) not null,"
                + "    reg_no VARCHAR(10) not null,"
                + "    total_cost DOUBLE not null,"
                + "    rent_date DATE not null,"
                + "    rent_hour TIME not null,"
                + "    return_date DATE not null,"
                + "    return_hour TIME not null,"
                + "    f_name VARCHAR(30) not null,"
                + "    driver_fname  VARCHAR (30) not null,"
                + "    driver_lname VARCHAR(30) not null,"
                + "    insurance_cost DOUBLE not null,"
                + "    valid_bit INTEGER not null,"
                + " FOREIGN KEY (reg_no) REFERENCES vehicles(reg_no),"
                + " PRIMARY KEY (rid))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewRent(Rent rented_veh) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " rented_vehs (username,reg_no,total_cost,rent_date,rent_hour,return_date,return_hour,f_name,driver_fname,driver_lname,insurance_cost,valid_bit)"
                    + " VALUES ("
                    + "'" + rented_veh.getUsername() + "',"
                    + "'" + rented_veh.getReg_no() + "',"
                    + "'" + rented_veh.getTotal_cost() + "',"
                    + "'" + rented_veh.getRent_date() + "',"
                    + "'" + rented_veh.getRent_hour() + "',"
                    + "'" + rented_veh.getReturn_date() + "',"
                    + "'" + rented_veh.getReturn_hour() + "',"
                    + "'" + rented_veh.getF_name() + "',"
                    + "'" + rented_veh.getDriver_fname() + "',"
                    + "'" + rented_veh.getDriver_lname() + "',"
                    + "'" + rented_veh.getInsurance_cost() + "',"
                    + "'" + rented_veh.getValid_bit() + "')";

            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The rented_veh was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(RentTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Establishes a database connection and invalidates the current vehicle
     * rental.
     *
     * @throws ClassNotFoundException
     */
    public void removeVehicle(Vehicle vehicle) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String removeQuery = "DELETE FROM rented_vehs "
                    + "WHERE "
                    + "valid_bit = 1 and reg_no ='" + vehicle.getReg_no() + "';";

            //stmt.execute(table);
            System.out.println(removeQuery);
            stmt.executeUpdate(removeQuery);
            System.out.println("# The vehicle was successfully removed from the rents.");

            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(VehicleTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StatPack calculateRentalStats(String type) {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs_max = null;
            ResultSet rs_min = null;
            ResultSet rs_avg = null;
            StatPack stats = new StatPack();

            rs_max = stmt.executeQuery("SELECT MAX(DATEDIFF(return_date,rent_date)) AS VAL "
                    + "FROM rented_vehs JOIN vehicles ON rented_vehs.reg_no = vehicles.reg_no WHERE type='" + type + "'\n");
            //
            try {
                rs_max.next();

                int val = rs_max.getInt("VAL");
                if (rs_max.wasNull()) {
                    throw new Exception();
                }
                stats.setMaxRentDuration(val + 1);
            } catch (Exception e) {
                stats.setMaxRentDuration(0);
            }
            //
            rs_min = stmt.executeQuery("SELECT MIN(DATEDIFF(return_date,rent_date)) AS VAL "
                    + "FROM rented_vehs JOIN vehicles ON rented_vehs.reg_no = vehicles.reg_no WHERE type='" + type + "'\n");
            //
            try {
                rs_min.next();

                int val = rs_min.getInt("VAL");
                if (rs_min.wasNull()) {
                    throw new Exception();
                }
                stats.setMinRentDuration(val + 1);
            } catch (Exception e) {
                stats.setMinRentDuration(0);
            }
            //
            rs_avg = stmt.executeQuery("SELECT AVG(DATEDIFF(return_date,rent_date)) AS VAL "
                    + "FROM rented_vehs JOIN vehicles ON rented_vehs.reg_no = vehicles.reg_no WHERE type='" + type + "'\n");
            //
            try {
                rs_avg.next();

                int val = rs_avg.getInt("VAL");
                if (rs_avg.wasNull()) {
                    throw new Exception();
                }
                stats.setAvgRentDuration(val + 1);
            } catch (Exception e) {
                stats.setAvgRentDuration(0);
            }
            //

            return stats;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Rent databaseToRent(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM rented_vehs WHERE reg_no = '" + reg_no + "'");
            if (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Rent rented_veh = gson.fromJson(json, Rent.class);
                return rented_veh;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

}
