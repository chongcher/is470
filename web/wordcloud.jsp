<%-- 
    Document   : wordcloud
    Created on : 05-Oct-2016, 07:54:39
    Author     : ccchia.2014
--%>

<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.google.common.collect.Lists"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.ResponseDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="js/jquery-3.1.1.js"></script>
        <script type="text/javascript" src="js/wordcloud2.js"></script>
        <title>Word Cloud</title>
        
        <% 
            String lectureID = (String) request.getParameter("lectureID");
            if(null == lectureID){
                response.sendRedirect("overview.jsp");
                return;
            }
            ResponseDAO responseDAO = (ResponseDAO) session.getAttribute("responseDAO");
            HashMap<String,Integer> keywordCount = responseDAO.getKeywordCount(lectureID);
            String[] filteredWordsList = request.getParameterValues("filteredWords[]");
            ArrayList<String> filteredWords = new ArrayList<String>();
            if(null != filteredWordsList){
                filteredWords = new ArrayList<String>(Arrays.asList(filteredWordsList));
                if(null != filteredWords && filteredWords.size()> 0){
                    for(String s: filteredWords){
                        //make the keyword count negative so it does not appear in the wordcloud
                        keywordCount.put(s,-1);
                    }
                }
            }
            Gson gson = new Gson();
            String json = gson.toJson(keywordCount);
        %>
        
        <script type="text/javascript">
            
            $(document).ready(function(){
                
                $(wordcloud_canvas).ready(function(){
                    var json = $.makeArray(<%= json %>);
                    var list = []
                    json.forEach(function(outer){
                        Object.keys(outer).forEach(function(inner){
                            list.push([inner,outer[inner]]);
                        });
                    });
                    <%
                        for(String s: keywordCount.keySet()){
                            out.println();
                        }
                    %>
                    var wordcloudObject = { list: list, fontweight: "600", weightFactor: "10"};
                    WordCloud(document.getElementById('wordcloud_canvas'), wordcloudObject );
                });
            });
        </script>
    </head>
    <body>
        <div class="header">
        </div>
        <div class="body">
            <canvas id="wordcloud_canvas" width="600" height="600" style="border:1px solid #000000;"></canvas></br>
            
            <h2>Filter Words</h2>
            <form action="wordcloud.jsp" method="post">
                <input type="hidden" name="lectureID" value=<%= "\"" + lectureID + "\"" %>>
                <table>
                    <%
                        for(String s: keywordCount.keySet()){
                            boolean alreadyFiltered = filteredWords.contains(s);
                        %>
                            <tr>
                                <td><%= s %></td>
                                <td><input type="checkbox" name="filteredWords[]" value=<%= "\"" + s + "\""%> <% if(alreadyFiltered) out.println("checked");%>></td>
                            </tr>
                        <%
                        }
                        //clear filteredWords from session
                        request.setAttribute("filteredWords[]", new String[0]);
                    %>
                </table>
                <button type="submit">Filter selected words</button>
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
