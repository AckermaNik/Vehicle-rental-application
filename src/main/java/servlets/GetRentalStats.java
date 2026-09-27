/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.google.gson.Gson;
import database.tables.RentTable;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.StatPack;

/**
 *
 * @author dimit
 */
public class GetRentalStats extends HttpServlet {

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

        RentTable rent_table = new RentTable();

        StatPack rent_stats_car = rent_table.calculateRentalStats("car");
        StatPack rent_stats_motorbike = rent_table.calculateRentalStats("motorbike");
        StatPack rent_stats_bike = rent_table.calculateRentalStats("bike");
        StatPack rent_stats_scooter = rent_table.calculateRentalStats("skate");

        String rent_stats_car_str = statPackToDetails(rent_stats_car);
        String rent_stats_motorbike_str = statPackToDetails(rent_stats_motorbike);
        String rent_stats_bike_str = statPackToDetails(rent_stats_bike);
        String rent_stats_scooter_str = statPackToDetails(rent_stats_scooter);

        // Create a data object
        RentalStatsObject data = new RentalStatsObject(rent_stats_car_str, rent_stats_motorbike_str,
                rent_stats_bike_str, rent_stats_scooter_str);

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

    public class RentalStatsObject {

        private String rent_stats_car;
        private String rent_stats_motorbike;
        private String rent_stats_bike;
        private String rent_stats_scooter;

        public RentalStatsObject(String rent_stats_car, String rent_stats_motorbike, String rent_stats_bike, String rent_stats_scooter) {
            this.rent_stats_car = rent_stats_car;
            this.rent_stats_motorbike = rent_stats_motorbike;
            this.rent_stats_bike = rent_stats_bike;
            this.rent_stats_scooter = rent_stats_scooter;
        }
    }

    private String statPackToDetails(StatPack stats) {
        if (stats != null) {
            return "Max: " + stats.getMaxRentDuration() + " days, Min: "
                    + stats.getMinRentDuration() + " days, Avg: "
                    + stats.getAvgRentDuration() + " days";
        }

        return "No available statistics";
    }
}
