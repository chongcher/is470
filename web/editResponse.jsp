<%-- 
    Document   : editResponse
    Created on : 27-Sep-2016, 08:15:46
    Author     : ccchia.2014
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Response</title>
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
            <form action="editResponseServlet" name="editResponseForm" method="post">
                <% 
                    String lectureID = request.getParameter("lectureID");
                %>
                <h2><%= lectureID.replace("_", " ") %> Response</h2>
                <input type="hidden" name="lectureID" value=<%= lectureID %>>
                <textarea rows="4" cols="50" name="response" placeholder="Enter keywords here"></textarea></br>
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