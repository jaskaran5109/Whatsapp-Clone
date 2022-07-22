package com.example.whatsapp.Models;

public class Users {

    String userId,profilePic,username,email,password,lastMeaase,status;

    public Users(String userId,String profilePic, String username, String email, String password, String lastMeaase,String status) {
        this.userId=userId;
        this.profilePic = profilePic;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastMeaase = lastMeaase;
        this.status=status;
    }
    public Users(){ }
    //signup constructor
    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastMeaase() {
        return lastMeaase;
    }

    public void setLastMeaase(String lastMeaase) {
        this.lastMeaase = lastMeaase;
    }
}
