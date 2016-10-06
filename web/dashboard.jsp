<%-- 
    Document   : dashboard
    Created on : 27-Sep-2016, 08:11:57
    Author     : ccchia.2014
--%>

<%@page import="model.Response"%>
<%@page import="model.ResponseDAO"%>
<%@page import="model.LectureDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
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
            
            <%
                LectureDAO lectureDAO = (LectureDAO) session.getAttribute("lectureDAO");
            %>
            <form id="editResponse" action="editResponse.jsp" method="get">
                <table>
                    <tr>
                        <td>
                            <select name="lectureID" id="lectureID">
                            <%
                                for(String lectureID: lectureDAO.getAllLectureIDs()){
                            %>
                            <option value=<%= lectureID %>><%= lectureID.replace("_", " ") %></option>
                            <%
                                }
                            %>
                            </select>
                        </td>
                        <td>
                            <button type="submit">Edit your response</button>
                        </td>
                    </tr>
                </table>
            </form>
            <table>
                <tr>
                    <th>Lecture ID</th>
                    <th>Response</th>
                    <th>Keywords</th>
                </tr>
                <%
                    for(String s: lectureDAO.getAllLectureIDs()){
                        String displayResponse = "";
                        String displayKeywords = "";
                        String lectureID = s;
                        String wordcloudLink = "wordcloud.jsp?lectureID=" + lectureID;
                        ResponseDAO responseDAO = (ResponseDAO) session.getAttribute("responseDAO");
                        Response userResponse = responseDAO.getResponse((String) session.getAttribute("userID"), lectureID);
                        if(null != userResponse){
                            displayResponse = userResponse.getResponse();
                            displayKeywords = userResponse.getKeywords();
                        }
                %>
                <tr>
                    <td><a href= <%= wordcloudLink %>><%= lectureID.replace("_", " ") %></a></td>
                    <td><%= displayResponse %></td>
                    <td><%= displayKeywords %></td>
                </tr>
                <%
                    }
                %>
            </table>
            <div class="adminFunctions">
                <h2>Administrative Functions</h2>
                <%
                    if(null != session.getAttribute("isFaculty") && (Boolean) session.getAttribute("isFaculty")){
                %>
                <a href="addLecture.jsp">Add Lecture</a></br>
                <a href="addUser.jsp">Add User</a>
                <form action="editKeywords.jsp">
                    <select name="lectureID">
                    <%
                        for(String lectureID: lectureDAO.getAllLectureIDs()){
                    %>
                        <option value=<%= lectureID %>><%= lectureID.replace("_", " ") %></option>
                    <%
                        }
                    %>  
                    </select>
                    <button type="submit">Edit Lecture Keywords</a>
                <%
                    }
                %>
            </div>
            <div class="backButton">
                <button onclick="goBack()">Go Back</button>            
            </div>
        </div>
    </body>
</html>

<script>
    function goBack() {
        window.history.back();
    }
</script>