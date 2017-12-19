package com.bookkeeping.controller;

/**
 * Created by chandan on 6/20/16.
 */
public class DefaultController {

    UserDAO userDAO;
    SessionDAO sessionDAO;
    public DefaultController() {
        userDAO = new UserDAO();
        sessionDAO = new SessionDAO();
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }

}
