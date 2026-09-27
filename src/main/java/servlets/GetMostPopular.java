/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.google.gson.Gson;
import database.tables.VehicleTable;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Vehicle;

/**
 *
 * @author dimit
 */
public class GetMostPopular extends HttpServlet {

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

        VehicleTable veh_table = new VehicleTable();

        Vehicle mp_car = veh_table.findMostPopular("car");
        Vehicle mp_motorbike = veh_table.findMostPopular("motorbike");
        Vehicle mp_bike = veh_table.findMostPopular("bike");
        Vehicle mp_scooter = veh_table.findMostPopular("skate");

        String mp_car_str = vehicleToDetails(mp_car);
        String mp_motorbike_str = vehicleToDetails(mp_motorbike);
        String mp_bike_str = vehicleToDetails(mp_bike);
        String mp_scooter_str = vehicleToDetails(mp_scooter);

        // Create a data object
        PopularVehicleObject data = new PopularVehicleObject(mp_car_str, mp_motorbike_str, mp_bike_str, mp_scooter_str);

        // Convert the data object to JSON
        String jsonData = new Gson().toJson(data);

        // Send JSON response to the client
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonData);
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

    public class PopularVehicleObject {

        private String mp_car;
        private String mp_motorbike;
        private String mp_bike;
        private String mp_scooter;

        public PopularVehicleObject(String mp_car, String mp_motorbike, String mp_bike, String mp_scooter) {
            this.mp_car = mp_car;
            this.mp_motorbike = mp_motorbike;
            this.mp_bike = mp_bike;
            this.mp_scooter = mp_scooter;
        }
    }

    private String vehicleToDetails(Vehicle vehicle) {
        if (vehicle != null) {
            return "" + vehicle.getBrand() + " " + vehicle.getModel() + ", " + vehicle.getColour() + ", reg_no: " + vehicle.getReg_no();
        }

        return "No vehicles here";
    }
}
