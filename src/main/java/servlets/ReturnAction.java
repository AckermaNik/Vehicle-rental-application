/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.tables.RentTable;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vickymil
 */
@WebServlet(name = "ReturnAction", urlPatterns = {"/ReturnAction"})
public class ReturnAction extends HttpServlet {

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

        try {
            String reg_no = request.getParameter("reg_no");
//            String ridString = request.getParameter("rid");
//            int rid = Integer.parseInt(ridString);

            System.out.println("NO: " + reg_no);
//            System.out.println("rid: " + rid);

            String returndate = request.getParameter("returndate");
            String returnhour = request.getParameter("returnhour");

            RentTable rt = new RentTable();
            double extra_cost = rt.CalculateCost(reg_no, returndate, returnhour);

            if (extra_cost == 0.0) {
                System.out.println("NO EXTRA COST");
            } else {
                System.out.println("EXTRA COST:" + extra_cost);
            }
            double og_cost = rt.Get_Total_Cost(reg_no);
            double total = og_cost + extra_cost;
            System.out.println("TOTAL COST:" + total);
            rt.UpdateCostandValid(reg_no, total);

            try (PrintWriter out = response.getWriter()) {

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet Return</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>The return was completed!</h1>");
                out.println("<h2>Customer was charged extra: " + extra_cost + "euros!</h2>");
            }


        } catch (SQLException ex) {
            Logger.getLogger(ReturnAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReturnAction.class.getName()).log(Level.SEVERE, null, ex);
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
