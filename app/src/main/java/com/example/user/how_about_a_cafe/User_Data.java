package com.example.user.how_about_a_cafe;

public class User_Data {
    private String userName;
    private String userEmail;

    public void User_Data(){
    }

    public User_Data(String userName, String userEmail){
        this.userName = userName;
        this.userEmail = userEmail;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
