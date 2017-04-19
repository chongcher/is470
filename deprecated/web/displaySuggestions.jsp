<%-- 
    Document   : displaySuggestions
    Created on : 22-Oct-2016, 08:36:42
    Author     : ccchia.2014
--%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Display Suggestions</title>
    </head>
    <body>
        <div class="header">
            
        </div>
        <div class="body">
            <%
                HashMap<String, ArrayList<String>> suggestions = (HashMap<String, ArrayList<String>>) request.getAttribute("suggestions");
                String userResponse = (String) request.getAttribute("userResponse");
                String lectureID = (String) request.getAttribute("lectureID");
            %>
            <textarea rows="4" cols="50" readonly><%= userResponse %></textarea>
            <form action="editResponseServlet" name="editResponseForm" method="post">
                <input type="hidden" name="hasSpellingCorrections" value="true">
                <input type="hidden" name="response" value=<%= "\"" + userResponse + "\"" %> >
                <input type="hidden" name="lectureID" value=<%= "\"" + lectureID + "\"" %> >
                <input type="hidden" name="totalSuggestions" value=<%= "\"" + suggestions.keySet().size() + "\"" %> >
                <table>
                    <thead>
                        <tr>Word</tr>
                        <tr>Suggestion</tr>
                    </thead>
                    <tr>
                    <%
                        int counter = 0;
                        for(String word: suggestions.keySet()){
                            String inputName = "suggestion" + ++counter;
                            %>
                            <td><%= word %><input type="hidden" name=<%= "\"" + inputName + "Original" + "\"" %> value=<%= "\"" + word + "\"" %></td>
                            <td>
                                <fieldset>
                                    <input type="radio" value=<%= "\"" + word + "\"" %> name=<%= "\"" + inputName + "\"" %>><%= word %><br>
                                    <%
                                        for(String suggestion: suggestions.get(word)){
                                        %>
                                            <input type="radio" value=<%= "\"" + suggestion + "\"" %> name=<%= "\"" + inputName + "\"" %>><%= suggestion %><br>
                                        <%
                                        }
                                    %>
                                </fieldset>
                            </td>
                            <%
                        }
                    %>
                    </tr>
                </table>
                <button type="submit">Submit response</button>
            </form>
        </div>
        <div class="footer">
            <button onclick="goBack()">Go Back</button>
        </div>
    </body>
</html>
