<%-- 
    Document   : wordcloud
    Created on : 05-Oct-2016, 07:54:39
    Author     : ccchia.2014
--%>

<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.google.common.collect.Lists"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.ResponseDAO"%>
<%@page import="model.LectureDAO"%>
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
            int responsesCount = keywordCount.remove("responsesCount");
            String[] filteredWordsList = request.getParameterValues("filteredWords[]");
            ArrayList<String> filteredWords = new ArrayList<String>();
            Gson gson = new Gson();
            String json;
            boolean displayMissingKeywords = (null != request.getParameter("displayMissingKeywords") ) ? true : false ;
            double tolerance = 0;
            if(displayMissingKeywords){
                tolerance = Double.parseDouble(request.getParameter("tolerance")) / 100;
                double minimumResponses = (responsesCount * tolerance) + 1; 
                    // this is the minimum number of responses a keyword needs to be displayed
                    // +1 so that at 100% tolerance level, all keywords will be displayed
                LectureDAO lectureDAO = (LectureDAO) session.getAttribute("lectureDAO");
                ArrayList<String> lectureKeywords = lectureDAO.getLectureKeywords(lectureID);
                HashMap<String, Integer> missingKeywords = new HashMap<String, Integer>();
                                System.out.println("Minresponse: " + minimumResponses);
                for(String keyword: lectureKeywords){
                    if(!keywordCount.containsKey(keyword)){ //not found in all responses, so make the keyword big in the wordcloud
                        missingKeywords.put(keyword, (int) Math.round(minimumResponses*2) );
                                System.out.println("lecturekeywords: " + keyword + " value: " + Math.round(minimumResponses*2) );
                    } else if (keywordCount.get(keyword) < minimumResponses){
                        int currentKeywordCount = keywordCount.get(keyword);
                        missingKeywords.put(keyword,(int) Math.round(minimumResponses - currentKeywordCount) );
                                System.out.println("lecturekeywords: " + keyword + " original: " + currentKeywordCount + ", value: " + Math.round(minimumResponses - currentKeywordCount) );
                        // (minimumResponses - currentKeywordCount) is done to inverse the order of the keywords
                        // as we want keywords that have low response counts to be bigger in the wordcloud
                        // currentKeyWordCount is guaranteed to be smaller than minimumResponses.
                    }
                }
                json = gson.toJson(missingKeywords);
            }
            else{
                if(null != filteredWordsList){
                    filteredWords = new ArrayList<String>(Arrays.asList(filteredWordsList));
                    if(null != filteredWords && filteredWords.size()> 0){
                        for(String s: filteredWords){
                            //make the keyword count negative so it does not appear in the wordcloud
                            keywordCount.put(s,-1);
                        }
                    }
                }
                json = gson.toJson(keywordCount);
            }
            
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
                        for(String s: new TreeSet<String>(keywordCount.keySet())){
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
                <input type="checkbox" name="displayMissingKeywords" <%= (displayMissingKeywords ? "checked" : "") %>>Display Missing Keywords<br>
                <input type="number" name="tolerance" min="0" max="100" step="1" value=<%= "\"" + (displayMissingKeywords ? tolerance*100 : 60) + "\""%> >% Tolerance Level<br>
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
