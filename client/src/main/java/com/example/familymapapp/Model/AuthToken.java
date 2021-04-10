package com.example.familymapapp.Model;

/**
 * AuthToken model class to store all AuthToken data while in memory.
 */
public class AuthToken {
    private String token;
    private String associated_user_ID;

    void AuthToken(){
        this.token = null;
        this. associated_user_ID = null;
    }

    /**
     *
     * @return Returns the associated user ID.
     */
    public String getAssociated_user_ID() {
        return associated_user_ID;
    }

    /**
     * Sets associated_user_ID to the given associated_user_ID value.
     * @param associated_user_ID
     */
    public void setAssociated_user_ID(String associated_user_ID) {
        this.associated_user_ID = associated_user_ID;
    }

    /**
     *
     * @return Returns the token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token to the given token value.
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }
}

