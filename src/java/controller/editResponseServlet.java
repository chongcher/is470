package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.ResponseDAO;
import utility.Spelling;

/**
 *
 * @author ccchia.2014
 */
public class editResponseServlet extends HttpServlet {
    
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
        String lectureID = request.getParameter("lectureID");
        String responseString = request.getParameter("response");        
        //spelling check
        String[] parts = responseString.split("\\W+");
        ArrayList<String> responseArrayList = new ArrayList<String>(Arrays.asList(parts));
        ArrayList<String> spList = Spelling.spellCheck(responseArrayList);
        //replace responseString with corrected spelling
        responseString = "";
        for(String s: spList){
            responseString += s + " ";
        }
        //keyword extraction
        String keywords = "";
        try {
            keywords = ExudeData.getInstance().filterStoppingsKeepDuplicates(responseString);
        } catch (InvalidDataException ex) {
            System.out.println("exude failed: " + ex.getMessage());
        }        
        //write Response object to database
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");
        ResponseDAO responseDAO = (ResponseDAO) session.getAttribute("responseDAO");
        responseDAO.addResponse(userID, lectureID, responseString, keywords);//write response with corrected spelling into database, so user knows that it has been corrected
        //send response to dashboard
        response.sendRedirect("dashboard.jsp");
        return;
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
