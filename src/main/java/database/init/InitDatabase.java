/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.init;

import static database.DB_Connection.getInitialConnection;
import static database.DB_Connection.getDatabaseName;

import database.tables.CarTable;
import database.tables.CustomerTable;
import database.tables.RentTable;
import database.tables.VehicleTable;
import database.tables.ServiceTable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import mainClasses.Car;

/*
 *
 * @author micha
 */
public class InitDatabase {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        InitDatabase init = new InitDatabase();
        init.initDatabase();
        init.initTables();
        init.addToDatabaseExamples();
        init.databaseToJSON();

    }

    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE IF NOT EXISTS " + getDatabaseName());
        stmt.close();
        conn.close();
    }

    public void initTables() throws SQLException, ClassNotFoundException {

        VehicleTable veh = new VehicleTable();
        veh.createVehiclesTable();

        CarTable car = new CarTable();
        car.createCarsTable();

        CustomerTable cust = new CustomerTable();
        cust.createCustomersTable();

        ServiceTable service = new ServiceTable();
        service.createServicesTable();

        RentTable rent = new RentTable();
        rent.createRentsTable();

    }

    public void addToDatabaseExamples() throws ClassNotFoundException, SQLException {
        //Users

        VehicleTable veh = new VehicleTable();

        veh.addVehicleFromJSON(Resources.Veh1);
        veh.addVehicleFromJSON(Resources.Veh2);
        veh.addVehicleFromJSON(Resources.Veh3);
        veh.addVehicleFromJSON(Resources.Veh4);
        veh.addVehicleFromJSON(Resources.Veh5);
        veh.addVehicleFromJSON(Resources.Veh6);
        veh.addVehicleFromJSON(Resources.Veh7);
        veh.addVehicleFromJSON(Resources.Veh8);
        veh.addVehicleFromJSON(Resources.Veh9);
        veh.addVehicleFromJSON(Resources.Veh10);
        veh.addVehicleFromJSON(Resources.Veh11);
        veh.addVehicleFromJSON(Resources.Veh12);
        veh.addVehicleFromJSON(Resources.Veh13);
        veh.addVehicleFromJSON(Resources.Veh14);
        veh.addVehicleFromJSON(Resources.Veh15);
        veh.addVehicleFromJSON(Resources.Veh16);

        CarTable car = new CarTable();
        car.addCarFromJSON(Resources.Car1);
        car.addCarFromJSON(Resources.Car2);
        car.addCarFromJSON(Resources.Car3);
        car.addCarFromJSON(Resources.Car4);

    }

    public void databaseToJSON() throws ClassNotFoundException, SQLException {
//       //Get a car
        CarTable cartab = new CarTable();
        Car su = cartab.databaseToCar("ABC123");
        String json = cartab.CarToJSON(su);
        System.out.println("Car \n" + json + "\n");

        //Get  a Vehicle
        VehicleTable biketab = new VehicleTable();
        String bikejson = biketab.databaseVehicleToJSON("B3003");
        System.out.println("Vehicle \n" + bikejson + "\n");

    }

}
