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
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.ResponseDAO;
import utility.LuceneSpellChecker;

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
        String userResponse = request.getParameter("response");
        if(null == request.getParameter("hasSpellingCorrections") || "".equals(request.getParameter("hasSpellingCorrections"))){
            LuceneSpellChecker spellChecker = new LuceneSpellChecker();
            //Check spelling of userResponse
            HashMap<String, ArrayList<String>> suggestions = spellChecker.getSuggestions(new ArrayList<String>(Arrays.asList(userResponse.split("\\W+"))));
            if(suggestions.keySet().size() > 0){
                RequestDispatcher rd = request.getRequestDispatcher("/displaySuggestions.jsp");
                HttpSession session = request.getSession();
                request.setAttribute("suggestions", suggestions);
                request.setAttribute("userResponse", userResponse);
                request.setAttribute("lectureID", lectureID);
                rd.forward(request, response);
                return;
            }
        } else {
            int totalSuggestions = Integer.parseInt(request.getParameter("totalSuggestions"));
            for(int i = 1; i <= totalSuggestions; i++){
                String inputName = "suggestion" + i;
                String word = request.getParameter((inputName + "Original"));
                String suggestion = request.getParameter(inputName);
                userResponse = userResponse.replace(word, suggestion);
            }
        }
        
        String keywords = extractKeywords(userResponse);
        //write Response object to database
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");
        ResponseDAO responseDAO = (ResponseDAO) session.getAttribute("responseDAO");
        responseDAO.addResponse(userID, lectureID, userResponse, keywords);//write response with corrected spelling into database, so user knows that it has been corrected
        //send response to dashboard
        response.sendRedirect("dashboard.jsp");
        return;
    }
    
    private String extractKeywords(String response){
        String keywords = "";
        try {
            keywords = ExudeData.getInstance().filterStoppingsKeepDuplicates(response);
        } catch (InvalidDataException ex) {
            System.out.println("exude failed: " + ex.getMessage());
        }
        return keywords;
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
