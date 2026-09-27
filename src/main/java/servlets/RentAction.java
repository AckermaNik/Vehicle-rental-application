/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.tables.RentTable;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author 30697
 */
public class RentAction extends HttpServlet {

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

        RentTable rent_table = new RentTable();
        JSONObject json = new JSONObject();

        Double total_cost = 0.0, insurance_cost = 0.0;
        String reg_no = request.getParameter("reg_no");
        String rent_date = request.getParameter("rent_date");
        String return_date = request.getParameter("return_date");
        String rent_hour = request.getParameter("rent_hour");
        String return_hour = request.getParameter("return_hour");
        String username = request.getParameter("username");
        String f_name = request.getParameter("f_name");
        String driver_fname = request.getParameter("driver_fname");
        String driver_lname = request.getParameter("driver_lname");

        // Define the format of the input string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate in_date = LocalDate.parse(rent_date, formatter);
        LocalDate out_date = LocalDate.parse(return_date, formatter);

        long daysBetween = ChronoUnit.DAYS.between(in_date, out_date) + 1;  //days between rent and return

        String checkboxValue = request.getParameter("insurance_checkbox"); // to see if he want to pay insurance cost too

        try {
            if (checkboxValue != null && checkboxValue.equals("checked")) {
                System.out.println("Checkbox is checked");
                insurance_cost = rent_table.Get_Insurance_Cost(reg_no);
                total_cost = daysBetween * (rent_table.Get_Daily_Cost(reg_no) + rent_table.Get_Insurance_Cost(reg_no));

            } else {

                total_cost = daysBetween * rent_table.Get_Daily_Cost(reg_no);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RentAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RentAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Rent</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>The rental was completed succesfully!</h1>");


        }

        try {
            json.put("valid_bit", "1"); // akoma einai rent to oxhma
            json.put("reg_no", reg_no);
            json.put("username", username);
            json.put("rent_date", rent_date);
            json.put("rent_hour", rent_hour);
            json.put("return_date", return_date);
            json.put("return_hour", return_hour);
            json.put("f_name", f_name);
            json.put("driver_fname", driver_fname);
            json.put("driver_lname", driver_lname);
            json.put("total_cost", total_cost.toString());
            json.put("insurance_cost", insurance_cost.toString());

            System.err.println(json.toString());

            try {
                rent_table.addRentFromJSON(json.toString());
                rent_table.Update_Count(reg_no);
            } catch (SQLException ex) {
                Logger.getLogger(RentAction.class.getName()).log(Level.SEVERE, null, ex);
            }



        } catch (ClassNotFoundException e) {
            System.err.println("Got an exception2! ");
            System.err.println(e.getMessage());
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

}
