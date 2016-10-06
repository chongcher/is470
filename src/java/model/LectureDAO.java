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
import utility.ConnectionManager;

/**
 *
 * @author ccchia.2014
 */
public class LectureDAO {
    ArrayList<Lecture> allLectures;
    
    public LectureDAO(){
        this.allLectures = readDatabase();
    }
    
    private ArrayList<Lecture> readDatabase(){
        ArrayList<Lecture> allLectures = new ArrayList<Lecture>();
        try(Connection conn = ConnectionManager.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM LECTURES");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int counter = 0;
                String lectureID = rs.getString(++counter);
                String keywords = rs.getString(++counter);
                allLectures.add(new Lecture(lectureID, keywords));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return allLectures;
    }
    
    public ArrayList<String> getAllLectureIDs(){
        ArrayList<String> allLectureIDs = new ArrayList<String>();
        for(Lecture lecture: allLectures){
            allLectureIDs.add(lecture.getLectureID());
        }
        return allLectureIDs;
    }
    
    public boolean addLecture(String lectureID, String keywords){
        boolean success = false;
        try(Connection conn = ConnectionManager.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO LECTURES VALUES(?,?);");
            stmt.setString(1, lectureID.replace(" ","_"));
            stmt.setString(2, keywords);
            int rowsChanged = stmt.executeUpdate();
            if(1 == rowsChanged && allLectures.add(new Lecture(lectureID.replace(" ","_"), keywords))){
                success = true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }
    
    public String getLectureKeywords(String lectureID){
        String keywords = "";
        for(Lecture l: allLectures){
            if(l.getLectureID().equals(lectureID)){
                keywords += l.getKeywords();
                break;
            }
        }
        return keywords;
    }
    
    public Lecture getLecture(String lectureID){
        for(Lecture l: allLectures){
            if(l.getLectureID().equals(lectureID)){
                return l;
            }
        }
        return null;
    }
    
    public boolean updateKeywords(String lectureID, String keywords){
        boolean success = false;
        try(Connection conn = ConnectionManager.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("UPDATE LECTURES SET Keywords = ? WHERE Lecture_ID = ?");
            stmt.setString(1, keywords);
            stmt.setString(2, lectureID.replace(" ","_"));
            int rowsChanged = stmt.executeUpdate();
            if(1 == rowsChanged){
                success = true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        if(success){
            Lecture lecture = getLecture(lectureID);
            lecture.setKeywords(keywords);
        }
        return success;
    }
}
