package com.bookkeeping.model;

/**
 * Created by chandan on 9/27/2015.
 */
public class AppLogs {
    private AppRequest appRequest;
    private AppResponse appResponse;

    public AppLogs() {
    }

    public AppLogs(AppRequest appRequest, AppResponse appResponse) {
        this.appRequest = appRequest;
        this.appResponse = appResponse;
    }

    public AppRequest getAppRequest() {
        return appRequest;
    }

    public void setAppRequest(AppRequest appRequest) {
        this.appRequest = appRequest;
    }

    public AppResponse getAppResponse() {
        return appResponse;
    }

    public void setAppResponse(AppResponse appResponse) {
        this.appResponse = appResponse;
    }
}
