package com.bookkeeping.model;

/**
 * Created by chandan on 9/27/2015.
 */
public class User {

    private String username;
    private String session;

    public User(String session, String username) {
        this.session = session;
        this.username = username;
    }

    public User() {
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
