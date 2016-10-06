/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import utility.ConnectionManager;

/**
 *
 * @author ccchia.2014
 */
public class ResponseDAO {
    ArrayList<Response> allResponses;
    
    public ResponseDAO(){
        this.allResponses = readDatabase();
    }
    
    private ArrayList<Response> readDatabase(){
        ArrayList<Response> allResponses = new ArrayList<Response>();
        try(Connection conn = ConnectionManager.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RESPONSES");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int counter = 0;
                String userID = rs.getString(++counter);
                String lectureID = rs.getString(++counter);
                String response = rs.getString(++counter);
                String keywords = rs.getString(++counter);
                allResponses.add(new Response(userID, lectureID, response, keywords));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return allResponses;
    }
    
    public void addResponse(String userID, String lectureID, String response, String keywords){
        Response existingResponse = getResponse(userID,lectureID);
        if(null == existingResponse){
            Response newResponse = new Response(userID, lectureID, response, keywords);
            allResponses.add(newResponse);
            //write into DB
            try(Connection conn = ConnectionManager.getConnection();){
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO RESPONSES VALUES (?,?,?,?)");
                stmt.setString(1, userID);
                stmt.setString(2, lectureID);
                stmt.setString(3, response);
                stmt.setString(4, keywords);
                stmt.execute();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        else{
            existingResponse.updateResponse(response,keywords);
            //write into DB
            try(Connection conn = ConnectionManager.getConnection();){
                PreparedStatement stmt = conn.prepareStatement("UPDATE RESPONSES SET Response = ?, Keywords = ? WHERE User_ID = ? AND Lecture_ID = ?");
                stmt.setString(1, response);
                stmt.setString(2, keywords);
                stmt.setString(3, userID);
                stmt.setString(4, lectureID);
                stmt.executeUpdate();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    public Response getResponse(String userID, String lectureID){
        for(Response r: allResponses){
            if(r.getLectureID().equals(lectureID) && r.getUserID().equals(userID)){
                return r;
            }
        }
        return null;
    }
    
    public HashMap<String,Integer> getKeywordCount(String lectureID){
        HashMap<String,Integer> keywordCount = new HashMap<String,Integer>();
        for(Response r: allResponses){
            if(r.getLectureID().equals(lectureID)){
                String keywords = r.getKeywords();
                String[] parts = keywords.split("\\W+");
                for(String kw: parts){
                    if(!keywordCount.containsKey(kw)){
                        keywordCount.put(kw,1);
                    }
                    else{
                        int currentCount = keywordCount.get(kw);
                        keywordCount.put(kw, currentCount+1);
                    }
                }
            }
        }
        return keywordCount;
    }
}
