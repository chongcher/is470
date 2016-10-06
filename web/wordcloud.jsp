<%-- 
    Document   : wordcloud
    Created on : 05-Oct-2016, 07:54:39
    Author     : ccchia.2014
--%>

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
        <canvas id="wordcloud_canvas" width="600" height="600" style="border:1px solid #000000;"></canvas></br>
        <button onclick="goBack()">Go Back</button>
    </body>
</html>

<script>
    function goBack() {
        window.history.back();
    }
</script>
