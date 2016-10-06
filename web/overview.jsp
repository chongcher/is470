<%-- 
    Document   : overview
    Created on : 19-Sep-2016, 19:05:10
    Author     : ccchia.2014
--%>

<%@page import="model.ResponseDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.LectureDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Overview</title>
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
            <%
                if(null == session.getAttribute("lectureDAO")){
                    session.setAttribute("lectureDAO", new LectureDAO());
                    session.setAttribute("responseDAO", new ResponseDAO());                    
                }
            %>
            <form id="generateWordCloudForm" action="wordcloud.jsp" method="get">
                <table>
                    <tr>
                        <td>
                            <select name="lectureID" id="lectureID">
                            <%
                                LectureDAO lectureDAO = (LectureDAO) session.getAttribute("lectureDAO");
                                for(String lectureID: lectureDAO.getAllLectureIDs()){
                            %>
                            <option value=<%= lectureID %>><%= lectureID.replace("_", " ") %></option>
                            <%
                                }
                            %>
                            </select>
                        </td>
                        <td>
                            <button type="submit">Generate word cloud!</button>
                        </td>
                    </tr>
                </table>
            </form>
            <form id="loginForm" action="LoginServlet" method="post">
                <input type="hidden" id="candidate" name="candidate">
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" id="userID" name="userID"></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" id="unhashed" name="unhashed"></td>
                    </tr>
                    <tr>
                        <td class="centered" colspan="2"><input type="button" onclick="hashPassword()" value="Login"></td>                
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>

<script>
    function hashPassword(){
        var unhashed = document.getElementById("unhashed");
        var MD5 = new Hashes.MD5().hex(unhashed.value); //https://github.com/h2non/jshashes
        document.getElementById("candidate").value = MD5;
        document.getElementById("loginForm").submit();
    }
</script>