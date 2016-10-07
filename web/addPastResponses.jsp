<%-- 
    Document   : addPastResponses
    Created on : 07-Oct-2016, 23:00:13
    Author     : ccchia.2014
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Past Responses</title>
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
            <form action="addPastResponsesServlet" method="post">
                <% 
                    String lectureID = request.getParameter("lectureID");
                %>
                <h2><%= lectureID.replace("_", " ") %> Response</h2>
                <input type="hidden" name="lectureID" value=<%= lectureID %>>
                <textarea rows="10" cols="100" name="pastResponses" placeholder="Each response should be on a new line"></textarea></br>
                <button type="submit">Add responses</button>
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