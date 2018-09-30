package com.example.user.how_about_a_cafe;

public class Email_User_Data {
    private String userName;
    private String userEmail;
    private String userphotoUrl;

    public void User_Data(){
    }

    public Email_User_Data(String userName, String userEmail,String userphotoUrl){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userphotoUrl = userphotoUrl;

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

    public void setUserphotoUrl(String userphotoUrl) { this.userphotoUrl = userphotoUrl; }

    public String getUserphotoUrl() { return userphotoUrl; }

}
