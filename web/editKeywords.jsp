<%-- 
    Document   : editKeywords
    Created on : 06-Oct-2016, 10:29:54
    Author     : ccchia.2014
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="model.LectureDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Keywords</title>
    </head>
    <body>
        <div class="header">
            <%
                String displayMessage = "";
                if(session.getAttribute("displayMessage") != null ){
                    displayMessage = (String) session.getAttribute("displayMessage");
                }
                out.println(displayMessage);
                session.setAttribute("displayMessage","");
            %>
        </div>
        <div class="body">
            <form action="editKeywordsServlet" method="post">
                <% 
                    String lectureID = request.getParameter("lectureID");
                    LectureDAO lectureDAO = (LectureDAO) session.getAttribute("lectureDAO");
                    ArrayList<String> keywords = lectureDAO.getLectureKeywords(lectureID);
                %>
                <h2><%= lectureID.replace("_", " ") %> Keywords</h2>
                <input type="hidden" name="lectureID" value=<%= lectureID %>>
                <textarea rows="4" cols="50" name="keywords">
                    <% 
out.println(keywords.remove(0)); //weird indentation to fix display issue
                        for(String keyword: keywords){
                            out.print( ", " + keyword);
                        }
                    %>
                </textarea></br>
                <button type="submit">Submit response</button>
            </form>
        </div>
        <div class="footer">
            <button onclick="goBack()">Go Back</button>
        </div>
    </body>
</html>

<script>
    function goBack() {
        window.history.back();
    }
</script>