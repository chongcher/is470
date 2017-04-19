/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.ResponseDAO;
import model.UserDAO;
import utility.Spelling;

/**
 *
 * @author ccchia.2014
 */
public class addPastResponsesServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        UserDAO userDAO = (UserDAO)session.getAttribute("userDAO");
        ResponseDAO responseDAO = (ResponseDAO)session.getAttribute("responseDAO");
        String lectureID = request.getParameter("lectureID");
        String pastResponses = request.getParameter("pastResponses");
        ArrayList<String> allResponsesList = new ArrayList<String>(Arrays.asList(pastResponses.split("\r\n")));
        int counter = 0;
        ArrayList userData = new ArrayList();
        ArrayList responseData = new ArrayList();
        for (String r : allResponsesList) {
            String userID = lectureID + "-user" + String.format("%04d", counter++);
            String passwordHash = request.getParameter("passwordHash");
            HashMap<String, String> thisUser = new HashMap<String, String>();
            thisUser.put("userID", userID);
            thisUser.put("passwordHash", passwordHash);
            thisUser.put("isFaculty", "false");
            userData.add(thisUser);
            String[] parts = r.split("\\W+");
            ArrayList<String> responseArrayList = new ArrayList<String>(Arrays.asList(parts));
            ArrayList<String> spList = Spelling.spellCheck(responseArrayList);
            String responseString = "";
            for (String s : spList) {
                responseString = responseString + s + " ";
            }
            String keywords = "";
            try {
                keywords = ExudeData.getInstance().filterStoppingsKeepDuplicates(responseString);
            }
            catch (InvalidDataException ex) {
                System.out.println("exude failed: " + ex.getMessage());
            }
            HashMap<String, String> thisResponse = new HashMap<String, String>();
            thisResponse.put("userID", userID);
            thisResponse.put("lectureID", lectureID);
            thisResponse.put("response", responseString);
            thisResponse.put("keywords", keywords);
            responseData.add(thisResponse);
        }
        userDAO.batchAddUser(userData);
        responseDAO.batchAddResponse(responseData);
        response.sendRedirect("dashboard.jsp");
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
