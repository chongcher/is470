<%-- 
    Document   : addLecture
    Created on : 05-Oct-2016, 13:44:52
    Author     : ccchia.2014
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Lecture</title>
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
            <form action="addLectureServlet" method="post">
                <table>
                    <tr>
                        <td>Lecture ID:</td>
                        <td><input type="text" name="lectureID"></td>
                    </tr>
                    <tr>
                        <td>Keywords:</td>
                        <td><input type="text" name="keywords"></td>
                    </tr>
                </table>
                <button type="submit">Add Lecture!</button>
            </form>
        </div>
        <div class="footer">
            <button onclick="goBack()">Go Back</button>
        </div>
    </body>
</html>