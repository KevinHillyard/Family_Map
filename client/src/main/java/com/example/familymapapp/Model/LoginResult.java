package com.example.familymapapp.Model;

public class LoginResult {
    private String authToken;
    private String userName;
    private String personID;

    public void LoginResult() {
        authToken = null;
        userName = null;
        personID = null;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
