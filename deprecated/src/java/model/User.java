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
class User {
    private String userID;
    private String passwordHash;
    private boolean isFaculty;
    
    public User(String userID, String passwordHash, boolean isFaculty){
        this.userID = userID;
        this.passwordHash = passwordHash;
        this.isFaculty = isFaculty;
    }
    
    public String getID(){
        return userID;
    }
    
    public boolean isFaculty(){
        return isFaculty;
    }
}
