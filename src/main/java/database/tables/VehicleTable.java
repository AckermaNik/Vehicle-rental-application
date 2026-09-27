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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Vehicle;

/**
 *
 * @author 30697
 */
public class VehicleTable {

    public void addVehicleFromJSON(String json) throws ClassNotFoundException {
        Vehicle vehicle = jsonToVehicle(json);
        addNewVehicle(vehicle);
    }

    public Vehicle jsonToVehicle(String json) {
        Gson gson = new Gson();

        Vehicle vehicle = gson.fromJson(json, Vehicle.class);
        return vehicle;
    }

    public String VehicleToJSON(Vehicle vehicle) {
        Gson gson = new Gson();

        String json = gson.toJson(vehicle, Vehicle.class);
        return json;
    }

    public ArrayList<String> getVehsForQuery(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<String> vehicles = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT v.reg_no, r.f_name, c.l_name "
                    + "FROM vehicles v "
                    + "LEFT JOIN rented_vehs r ON v.reg_no = r.reg_no AND r.valid_bit = 1 "
                    + "LEFT JOIN customers c ON r.username = c.username "
                    + "WHERE v.type = '" + type + "'");
            while (rs.next()) {
                //String regNo = rs.getString("reg_no");
                //System.out.println("Reg No: " + regNo);
                //String rentedFName = rs.getString("rented_vehs_f_name");
                //System.out.println("fname: " + rentedFName);

                String json = DB_Connection.getResultsToJSON(rs);
                System.out.println(json);
                //Gson gson = new Gson();
                //String veh = gson.fromJson(json, String.class);
                vehicles.add(json);
            }
            return vehicles;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


    public String databaseVehicleToJSON(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM vehicles WHERE reg_no = '" + reg_no + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    //the Available ones for NOW
    public ArrayList<Vehicle> getAll_Vehicles(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM vehicles v WHERE type= " + "'" + type + "' AND ((v.reg_no not in (select reg_no "
                    + "from rented_vehs where reg_no=v.reg_no) OR v.reg_no in (select reg_no from  rented_vehs where reg_no=v.reg_no AND valid_bit=0)) AND (v.reg_no not in (select reg_no "
                    + "from service where reg_no=v.reg_no) OR v.reg_no in(select reg_no from service where reg_no=v.reg_no AND DATE_ADD(import_date,INTERVAL (select duration from service where reg_no=v.reg_no) DAY) <='" + LocalDate.now() + "')))\n");

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Vehicle veh = gson.fromJson(json, Vehicle.class);
                vehicles.add(veh);
            }
            return vehicles;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Vehicle> getAll_Vehicles_For_Rent(String type, Double cost, String date) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM vehicles v WHERE type= " + "'" + type + "'" + "AND daily_rent_cost BETWEEN 0 AND " + cost + " AND ((v.reg_no not in (select reg_no "
                    + "from rented_vehs where reg_no=v.reg_no) OR v.reg_no in (select reg_no from  rented_vehs where reg_no=v.reg_no AND valid_bit=0)) AND (v.reg_no not in (select reg_no "
                    + "from service where reg_no=v.reg_no) OR v.reg_no in(select reg_no from service where reg_no=v.reg_no AND DATE_ADD(import_date,INTERVAL (select duration from service where reg_no=v.reg_no) DAY) <=" + date + ")))\n");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Vehicle veh = gson.fromJson(json, Vehicle.class);
                vehicles.add(veh);
            }
            return vehicles;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void createVehiclesTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE vehicles "
                + "(reg_no VARCHAR(10) not null,"
                + "    type VARCHAR(10) not null,"
                + "    count INTEGER,"
                + "    km Double not null,"
                + "    colour VARCHAR(20) not null,"
                + "    model  VARCHAR (50) not null,"
                + "    brand VARCHAR(30) not null,"
                + "    daily_rent_cost DOUBLE,"
                + "    daily_insurance_cost DOUBLE,"
                + " PRIMARY KEY (reg_no))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewVehicle(Vehicle vehicle) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " vehicles (reg_no,type,count,km,colour,model,brand,daily_rent_cost,daily_insurance_cost)"
                    + " VALUES ("
                    + "'" + vehicle.getReg_no() + "',"
                    + "'" + vehicle.getType() + "',"
                    + "'" + vehicle.getCount() + "',"
                    + "'" + vehicle.getKm() + "',"
                    + "'" + vehicle.getColour() + "',"
                    + "'" + vehicle.getModel() + "',"
                    + "'" + vehicle.getBrand() + "',"
                    + "'" + vehicle.getDaily_rent_cost() + "',"
                    + "'" + vehicle.getDaily_insurance_cost() + "')";

            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The vehicle was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(VehicleTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Vehicle databaseToVehicle(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM vehicles WHERE reg_no = '" + reg_no + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Vehicle vehicle = gson.fromJson(json, Vehicle.class);
            return vehicle;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Gathers and returns all vehicles satisfying a condition.
     *
     * @return A list of all the collected vehicles
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ArrayList<Vehicle> getAvailableVehicles() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        ResultSet rs = null;
        try {

            rs = stmt.executeQuery("SELECT * FROM vehicles v WHERE ((v.reg_no not in (select reg_no "
                    + "from rented_vehs where reg_no=v.reg_no) OR v.reg_no in (select reg_no from  rented_vehs where reg_no=v.reg_no AND valid_bit=0)) AND (v.reg_no not in (select reg_no "
                    + "from service where reg_no=v.reg_no) OR v.reg_no in(select reg_no from service where reg_no=v.reg_no)))\n");

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Vehicle car = gson.fromJson(json, Vehicle.class);
                vehicles.add(car);
            }
            return vehicles;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Vehicle findMostPopular(String type) {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            Vehicle vehicle = null;
            rs = stmt.executeQuery("SELECT * FROM vehicles v1 "
                    + "WHERE v1.type=\'" + type + "\' AND v1.count >= "
                    + "(SELECT max(v2.count) FROM vehicles v2 "
                    + "WHERE v2.type=v1.type)\n");

            if (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                vehicle = gson.fromJson(json, Vehicle.class);
            }
            return vehicle;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

}
