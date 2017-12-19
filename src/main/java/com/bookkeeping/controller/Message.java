package com.bookkeeping.controller;

/**
 * Created by chandan on 9/19/2015.
 */
public class Message {

    String contents;
    String redirect;
    //used to signify success/failure: [TODO] not used as of now
    String status;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
