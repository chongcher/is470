<%-- 
    Document   : addStudent
    Created on : 05-Oct-2016, 13:45:02
    Author     : ccchia.2014
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Student</title>
        <script src="hashes.js"></script><!-- taken from http://cdn.rawgit.com/h2non/jsHashes/master/hashes.js on 19/09/2016 -->
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
            <form action="addUserServlet" method="post" id="addUserServletForm">
                <input type="hidden" name="passwordHash" id="passwordHash">
                <table>
                    <tr>
                        <td>User ID:</td>
                        <td><input type="text" name="userID" required></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="text" name="unhashed" id="unhashed" required></td>
                    </tr>
                    <tr>
                        <td>Is Faculty</td>
                        <td>
                            <select name="isFaculty">
                                <option value="false" selected>No</option>
                                <option value="true">Yes</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <input type="button" onclick="hashPassword()" value="Add User">
            </form>
        </div>
        <div class="footer">
            <button onclick="goBack()">Go Back</button>
        </div>
    </body>
</html>

<script>
    function hashPassword(){
        var unhashed = document.getElementById("unhashed");
        var MD5 = new Hashes.MD5().hex(unhashed.value); //https://github.com/h2non/jshashes
        document.getElementById("passwordHash").value = MD5;
        document.getElementById("addUserServletForm").submit();
    }
</script>
