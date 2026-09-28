/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import database.tables.CustomerTable;
import database.tables.RentTable;
import database.tables.ServiceTable;
import database.tables.VehicleTable;
import mainClasses.Rent;
import mainClasses.Service;
import mainClasses.Vehicle;
import mainClasses.Customer;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Incident servlet to manage admin reports: 1)Malfunction: The vehicle is
 * replaced free of cost. 2)Accident: The vehicle is replaced at the expense of
 * the customer * if it's not been insured. 3)Service: The vehicle is moved away
 * for service (3 days). 4)Maintenance: The vehicle is moved away for
 * maintenance (1 day).
 *
 * @author dimitris
 */
public class Incident extends HttpServlet {

    private enum ReportType {
        MALFUNCTION, ACCIDENT, SERVICE, MAINTENANCE
    };

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String type = request.getParameter("chosenReport");
        String plate_no = request.getParameter("reg_no");
        String incident_date = request.getParameter("incident_date");

        CustomerTable cust_table = new CustomerTable();
        VehicleTable vehicle_table = new VehicleTable();
        RentTable rent_table = new RentTable();

        JSONObject json = new JSONObject();

        try {
            System.err.println(json.toString());

            Vehicle vehicle = vehicle_table.databaseToVehicle(plate_no);

            if (vehicle == null) {
                System.err.println("WARNING: Incident: query returned null, vehicle not found.");
                output_condition(json, request, response, "Error, vehicle doesn't exist!");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            ReportType report_type = stringToReportType(type);
            if (report_type == null) {
                throw new ClassNotFoundException();
            }

            /**
             * Malfunction and Accident reports require for the vehicle in
             * question to be already rented.
             */
            switch (report_type) {
                case MALFUNCTION: {
                    Rent oldRent = rent_table.databaseToRent(vehicle.getReg_no());
                    if (oldRent == null) {//Nobody has rented that vehicle
                        output_condition(json, request, response, "Error, vehicle not rented!");
                        return;
                    }

                    Customer customer = cust_table.databaseToCustomerFromUsername(oldRent.getUsername());
                    if (customer == null) {//Nobody has rented that vehicle
                        output_condition(json, request, response, "Error, vehicle not rented!");
                        return;
                    }

                    Vehicle replacement = findVehicleReplacement(vehicle_table, vehicle, incident_date);
                    if (replacement == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        output_condition(json, request, response, "Error, no replacement found!");
                        return;
                    }

                    if (!withinRentalPeriod(incident_date, oldRent)) {
                        output_condition(json, request, response, "Error, vehicle not rented at the provided date "
                                + "(rent period: " + oldRent.getRent_date() + " - " + oldRent.getReturn_date() + ")");
                        return;
                    }

                    changeRent(rent_table, oldRent, vehicle, replacement, incident_date, false);
                    break;
                }
                case ACCIDENT: {
                    Rent oldRent = rent_table.databaseToRent(vehicle.getReg_no());
                    if (oldRent == null) {//Nobody has rented that vehicle
                        output_condition(json, request, response, "Error, vehicle not rented!");
                        return;
                    }

                    Customer customer = cust_table.databaseToCustomerFromUsername(oldRent.getUsername());
                    if (customer == null) {//Nobody has rented that vehicle
                        output_condition(json, request, response, "Error, vehicle not rented!");
                        return;
                    }

                    Vehicle replacement = findVehicleReplacement(vehicle_table, vehicle, incident_date);

                    if (replacement == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        output_condition(json, request, response, "No replacement found!");
                        return;
                    }

                    if (!withinRentalPeriod(incident_date, oldRent)) {
                        output_condition(json, request, response, "Error, vehicle not rented at the provided date "
                                + "(rent period: " + oldRent.getRent_date() + " - " + oldRent.getReturn_date() + ")");
                        return;
                    }

                    changeRent(rent_table, oldRent, vehicle, replacement, incident_date, true);
                    break;
                }
                case SERVICE: {
                    Rent rent = rent_table.databaseToRent(vehicle.getReg_no());
                    if (rent != null && rent.getValid_bit() == 1) {
                        output_condition(json, request, response, "Error, vehicle currently rented!");
                        return;
                    }
                    sendToService(vehicle.getReg_no(), 3, incident_date, calculateServiceCost(vehicle, 3));
                    break;
                }
                case MAINTENANCE: {
                    Rent rent = rent_table.databaseToRent(vehicle.getReg_no());
                    if (rent != null && rent.getValid_bit() == 1) {
                        output_condition(json, request, response, "Error, vehicle currently rented!");
                        return;
                    }
                    sendToService(vehicle.getReg_no(), 1, incident_date, calculateServiceCost(vehicle, 1));
                    break;
                }
            }
            output_condition(json, request, response, "Report completed successfully!");

            cust_table.printCustomerDetails("admin123");

        } catch (ClassNotFoundException e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
            output_condition(json, request, response, "Internal errors, report failed.");
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
            output_condition(json, request, response, "Internal errors, report failed.");
        }
    }

    private void output_condition(JSONObject json, HttpServletRequest request, HttpServletResponse response, String message) {
        //Outputs a message on completion.
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Report</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + message + "</h1>");
            out.println("<table><tr><th>Category</th><th>Value</th></tr>");

            Enumeration paramNames = request.getParameterNames();

            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();

                out.print("<tr><td>" + paramName + "\n<td>");

                String[] paramValues = request.getParameterValues(paramName);

                if (paramValues.length == 1) {// only one param
                    String paramValue = paramValues[0];

                    json.put(paramName, paramValue);

                    out.println(paramValue);

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Incident.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Conversion of report type string to enum.
     */
    private ReportType stringToReportType(String str) {
        switch (str) {
            case "malfunctionreport": {
                return ReportType.MALFUNCTION;
            }
            case "accidentreport": {
                return ReportType.ACCIDENT;
            }
            case "servicerequest": {
                return ReportType.SERVICE;
            }
            case "maintainancerequest": {
                return ReportType.MAINTENANCE;
            }
            default: {
                return null;
            }
        }
    }

    /**
     * This method finds the closest possible unrented vehicle, as compared to
     * 'old'. Main criteria for choice: 1) Vehicle type. 2) Vehicle price.
     *
     * @param prototype The vehicle to replace
     * @return A similar vehicle
     */
    private Vehicle findVehicleReplacement(VehicleTable vehicle_table, Vehicle prototype, String incident_date)
            throws ClassNotFoundException, SQLException {
        ServiceTable service_table = new ServiceTable();

        ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) vehicle_table.getAvailableVehicles().stream().
                filter(x -> x.getType().equals(prototype.getType())).
                filter(x -> {
                    try {
                        Service service = service_table.databaseToService(x.getReg_no());
                        if (service == null) {
                            return true;
                        }

                        return !service.isActive(incident_date);
                    } catch (Exception e) {
                        return false;
                    }
                }).collect(Collectors.toCollection(ArrayList::new));

        if (vehicles.isEmpty()) {
            System.err.println("WARNING: No matching vehicles found.");
            return null;
        }

        Vehicle closest = vehicles.stream().filter(x -> !x.getReg_no().
                equals(prototype.getReg_no())).findFirst().get();

        System.err.println("TEST_LOG: The Initially chosen vehicle is: " + closest.getReg_no());

        for (int i = 0; i < vehicles.size(); i++) {
            if (Math.abs(prototype.getDaily_rent_cost()
                    - vehicles.get(i).getDaily_rent_cost()
            ) < Math.abs(prototype.getDaily_rent_cost()
                    - closest.getDaily_rent_cost()
            )) {
                closest = vehicles.get(i);
            }
        }

        System.err.println("TEST_LOG: The Chosen vehicle is: " + closest.getReg_no());

        return closest;
    }

    /**
     * @param rent_table The rent table used.
     * @param rent The rent to be changed.
     * @param old_vehicle The old vehicle object.
     * @param new_vehicle The new vehicle object.
     * @param report_date The date of the report.
     * @param extraCharge Whether extra should be charged for uninsured rentals.
     * @throws ClassNotFoundException
     */
    private void changeRent(RentTable rent_table, Rent rent, Vehicle old_vehicle,
            Vehicle new_vehicle, String report_date, boolean extraCharge)
            throws ClassNotFoundException {
        rent_table.removeVehicle(old_vehicle);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate in_date = LocalDate.parse(rent.getRent_date(), formatter);
        LocalDate inc_date = LocalDate.parse(report_date, formatter);
        LocalDate out_date = LocalDate.parse(rent.getReturn_date(), formatter);
        long daysBetweenSI = ChronoUnit.DAYS.between(in_date, inc_date);  //days between rent and incident
        long daysBetweenIE = ChronoUnit.DAYS.between(inc_date, out_date) + 1;  //days between incident and return

        if (isInsured(rent) || !extraCharge) {
            //In an attempt to make the replacement fair,
            //the customer is not charged extra reagardless if
            //their assigned replacement vehicle has a higher total
            //cost.
            double total_cost_old = rent.getTotal_cost();

            rent.setReg_no(new_vehicle.getReg_no());

            Double old_insurance_cost = Double.valueOf(0);
            Double new_insurance_cost = Double.valueOf(0);

            if (isInsured(rent)) {
                old_insurance_cost = old_vehicle.getDaily_insurance_cost();
                new_insurance_cost = new_vehicle.getDaily_insurance_cost();
            }

            double remaining_cost_new;
            remaining_cost_new = daysBetweenIE * (new_vehicle.getDaily_rent_cost()
                    + new_insurance_cost);

            double remaining_cost_old;
            remaining_cost_old = daysBetweenIE * (old_vehicle.getDaily_rent_cost()
                    + old_insurance_cost);

            /*-Debugging checks for development process, remove for final version.*/
            System.err.println("TC Before: " + (daysBetweenSI * (old_vehicle.getDaily_rent_cost()
                    + old_insurance_cost) + remaining_cost_old));

            System.err.println("TC After: " + (daysBetweenSI * (new_vehicle.getDaily_rent_cost()
                    + new_insurance_cost) + remaining_cost_new));
            /*                           ------------                             */

            if (remaining_cost_old < remaining_cost_new) {
                remaining_cost_new = remaining_cost_old;
            }

            double total_cost_final = daysBetweenSI * (old_vehicle.getDaily_rent_cost()
                    + old_insurance_cost) + remaining_cost_new;

            if (total_cost_old < total_cost_final) {
                total_cost_final = total_cost_old;
            }

            rent.setTotal_cost(total_cost_final);

            rent.setInsurance_cost(new_insurance_cost);
            rent.setValid_bit(1);
        } else {
            rent.setTotal_cost(3 * rent.getTotal_cost());
            rent.setValid_bit(0);
        }

        rent_table.addNewRent(rent);
    }

    /**
     * @param rent The rent to check
     * @return Whether the rent is insured or not.
     */
    private boolean isInsured(Rent rent) {
        return (rent.getInsurance_cost() > 0);
    }

    /**
     * Moves the vehicle with reg_no to the service table for the specified
     * duration.
     *
     * @param reg_no The reg_num of the targeted vehicle.
     * @param duration The duration of the service.
     * @throws ClassNotFoundException
     */
    private void sendToService(String reg_no, int duration, String import_date, Double cost)
            throws ClassNotFoundException {
        ServiceTable service_table = new ServiceTable();
        Service service = new Service();
        service.setReg_no(reg_no);
        service.setDuration(duration);
        service.setImport_date(import_date);
        service.setCost(cost);
        service_table.addNewService(service);
    }

    private Double calculateServiceCost(Vehicle vehicle, int duration) {
        //Cooking up a convincing cost :)

        double final_cost;
        final_cost = (vehicle.getDaily_rent_cost()) / 4.0;
        final_cost *= duration;

        Random random = new Random();

        final_cost += random.nextDouble() * (vehicle.getDaily_rent_cost() / 10.0);

        //Finally, get rid of decimals...
        final_cost = (int) final_cost;

        return final_cost;
    }

    private boolean withinRentalPeriod(String incident_date, Rent rent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate in_date = LocalDate.parse(rent.getRent_date(), formatter);
        LocalDate inc_date = LocalDate.parse(incident_date, formatter);
        LocalDate out_date = LocalDate.parse(rent.getReturn_date(), formatter);

        //Boundary check.
        if (in_date.compareTo(inc_date) > 0) {
            return false;
        }
        if (out_date.compareTo(inc_date) < 0) {
            return false;
        }

        return true;
    }
}
