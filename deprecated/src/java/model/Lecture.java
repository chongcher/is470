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
public class Lecture {
    private final String lectureID;
    private String keywords;
    
    public Lecture(String lectureID, String keywords){
        this.lectureID = lectureID;
        this.keywords = keywords;
    }
    
    public String getLectureID(){
        return lectureID;
    }
    
    public String getKeywords(){
        if(null == keywords || keywords.equals("NULL")) return "";
        return keywords;
    }
    
    public void setKeywords(String keywords){
        this.keywords = keywords;
    }
}
