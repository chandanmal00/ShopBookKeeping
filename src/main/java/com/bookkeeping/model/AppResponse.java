package com.bookkeeping.model;

/**
 * Created by chandan on 9/27/2015.
 */
public class AppResponse {

    private Object response;
    private Object rawResponse;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public void setRawResponse(Object rawResponse) {
        this.rawResponse = rawResponse;
    }
}

