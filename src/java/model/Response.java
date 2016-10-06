/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author ccchia.2014
 */
public class Response {
    private String userID;
    private String lectureID;
    private String response;
    private String keywords;
    
    public Response(String userID, String lectureID, String response, String keywords){
        this.userID = userID;
        this.lectureID = lectureID;
        this.response = response;
        this.keywords = keywords;
    }
    
    public String getUserID(){
        return userID;
    }
    
    public String getLectureID(){
        return lectureID;
    }
    
    public String getKeywords(){
        return keywords;
    }
    
    public String getResponse(){
        return response;
    }
    
    public void updateResponse(String response, String keywords){
        this.response = response;
        this.keywords = keywords;
    }
    
}
