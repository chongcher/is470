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
import utility.BCrypt;
import static utility.BCrypt.gensalt;
import utility.ConnectionManager;

/**
 *
 * @author ccchia.2014
 */
public class UserDAO {
    private ArrayList<User> allUsers;
    
    public UserDAO(){
        this.allUsers = readDatabase();
    }
    
    private ArrayList<User> readDatabase(){
        ArrayList<User> allUsers = new ArrayList<User>();
        try(Connection conn = ConnectionManager.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USERS");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int counter = 0;
                String userID = rs.getString(++counter);
                String passwordHash = rs.getString(++counter);
                boolean isFaculty = rs.getBoolean(++counter);
                allUsers.add(new User(userID, passwordHash, isFaculty));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return allUsers;
    }
    
    public boolean addUser(String userID, String candidate, boolean isFaculty){
        boolean success = false;
        String passwordHash = BCrypt.hashpw(candidate, gensalt());
        try(Connection conn = ConnectionManager.getConnection();){
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO USERS VALUES(?,?,?);");
            stmt.setString(1, userID);
            stmt.setString(2, passwordHash);
            stmt.setBoolean(3, isFaculty);
            int rowsChanged = stmt.executeUpdate();
            if(1 == rowsChanged && allUsers.add(new User(userID, passwordHash, isFaculty))){
                success = true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }
    
    public boolean batchAddUser(ArrayList<HashMap<String, String>> userData) {
        boolean success;
        success = true;
        String sql = "INSERT INTO USERS VALUES(?,?,?)";
        int counter = 0;
        try(Connection conn = ConnectionManager.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (HashMap<String, String> thisUser : userData) {
                String passwordHash = BCrypt.hashpw((String)thisUser.get("passwordHash"), (String)BCrypt.gensalt());
                stmt.setString(1, thisUser.get("userID"));
                stmt.setString(2, passwordHash);
                stmt.setBoolean(3, Boolean.parseBoolean(thisUser.get("isFaculty")));
                stmt.addBatch();
                counter++;
                if(counter >= 10){
                    int[] rowsChanged = stmt.executeBatch();
                    for(int i: rowsChanged){
                        if(i != 1) success = false;
                    }
                    stmt.clearBatch();
                    counter = 0;
                }
            }
            if (counter > 0) {
                int[] rowsChanged = stmt.executeBatch();
                for(int i: rowsChanged){
                    if(i != 1) success = false;
                }
            }
            if (success) {
                this.allUsers = this.readDatabase();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    
    public boolean login(String userID, String candidate){
        try{
            Connection conn = ConnectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT PasswordHash FROM USERS WHERE User_ID LIKE ?");
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String res = rs.getString(1);
                return BCrypt.checkpw(candidate, res);
            }
            stmt.close();
            conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false; //if rs.next() != true
    }
    
    public User getUser(String userID){
        for(User u: allUsers){
            if(u.getID().equals(userID)){
                return u;
            }
        }
        return null;
    }
    
    public boolean isFaculty(String userID){
        User u = this.getUser(userID);
        if(null != u) return true;
        return false;
    }
}
