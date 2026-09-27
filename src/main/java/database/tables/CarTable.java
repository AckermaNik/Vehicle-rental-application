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
import mainClasses.Car;

/**
 *
 * @author 30697
 */
public class CarTable {

    public void addCarFromJSON(String json) throws ClassNotFoundException {
        Car car = jsonToCar(json);
        addNewCar(car);
    }

    public Car jsonToCar(String json) {
        Gson gson = new Gson();

        Car car = gson.fromJson(json, Car.class);
        return car;
    }

    public String CarToJSON(Car car) {
        Gson gson = new Gson();

        String json = gson.toJson(car, Car.class);
        return json;
    }

    public Car databaseToCar(String reg_no) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM vehicles WHERE vehicles.type = 'car' AND reg_no = '" + reg_no + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Car car = gson.fromJson(json, Car.class);
            return car;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    // all the NOW available cars
    public ArrayList<Car> getAvailableCars() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Car> vehicles = new ArrayList<>();
        ResultSet rs = null;
        try {

            rs = stmt.executeQuery("SELECT * FROM vehicles v "
                    + "JOIN cars c ON v.reg_no = c.reg_no "
                    + "WHERE v.type = 'car' AND ((v.reg_no NOT IN (SELECT reg_no FROM rented_vehs WHERE reg_no = v.reg_no) "
                    + "      OR v.reg_no IN (SELECT reg_no FROM rented_vehs WHERE reg_no = v.reg_no AND valid_bit = 0)) "
                    + "AND (v.reg_no NOT IN (SELECT reg_no FROM service WHERE reg_no = v.reg_no) "
                    + "     OR v.reg_no IN (SELECT reg_no FROM service WHERE reg_no = v.reg_no "
                    + "AND DATE_ADD(import_date, INTERVAL (SELECT duration FROM service WHERE reg_no = v.reg_no) DAY) <= '" + LocalDate.now() + "')))");

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Car car = gson.fromJson(json, Car.class);
                vehicles.add(car);
            }
            return vehicles;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Car> getAll_Cars_For_Rent(Double cost, String date) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Car> vehicles = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM vehicles v JOIN cars c ON v.reg_no = c.reg_no WHERE type= 'car' AND daily_rent_cost BETWEEN 0 AND " + cost + "AND ((v.reg_no not in (select reg_no "
                    + "from rented_vehs where reg_no=v.reg_no) OR v.reg_no in (select reg_no from  rented_vehs where reg_no=v.reg_no AND valid_bit=0)) AND (v.reg_no not in (select reg_no "
                    + "from service where reg_no=v.reg_no) OR v.reg_no in(select reg_no from service where reg_no=v.reg_no AND DATE_ADD(import_date,INTERVAL (select duration from service where reg_no=v.reg_no) DAY) <=" + date + ")))\n");

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Car car = gson.fromJson(json, Car.class);
                vehicles.add(car);
            }
            return vehicles;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void createCarsTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE cars "
                + "(reg_no VARCHAR(10) not null,"
                + "    car_type VARCHAR(30) not null,"
                + "    passenger_num INTEGER not null,"
                + "FOREIGN KEY (reg_no) REFERENCES vehicles(reg_no))";

        stmt.execute(query);
        stmt.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewCar(Car car) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " cars (reg_no,car_type,passenger_num)"
                    + " VALUES ("
                    + "'" + car.getReg_no() + "',"
                    + "'" + car.getCar_type() + "',"
                    + "'" + car.getPassenger_num() + "')";

            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The car was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(CarTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
