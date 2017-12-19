package com.bookkeeping.model;

/**
 * Created by chandan on 9/27/2015.
 */

import spark.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by chandan on 9/27/2015.
 */
public class AppRequest {

    private String ip;
    private String host;
    private String useragent;
    private Map<String,String[]> queryMap;
    private String queryString;
    private Map<String,Object> headers;
    private Map<String,Object> attributes;
    private Set<String> queryParams;
    private String pathInfo;
    private String requestMethod;
    private String servletPath;
    private String scheme;
    private long requestTime;
    private User user;

    //NOTE: NEVER Log the request, otherwise we may get GSON errors, its just for extracting.
    public AppRequest(Request request) {

        this.useragent = request.userAgent();
        this.ip = request.ip();
        this.queryString = request.queryString();
        this.queryParams = request.queryParams();
        this.pathInfo = request.pathInfo();
        this.requestMethod = request.requestMethod();
        this.servletPath = request.servletPath();
        this.scheme = request.scheme();
        this.requestTime = System.currentTimeMillis();

        Map<String,Object> headerMap = new HashMap<String,Object>();
        for(String header : request.headers()) {
            headerMap.put(header,request.headers(header));
        }
        this.headers = headerMap;

        Map<String,Object> attributeMap = new HashMap<String,Object>();
        for(String attribute : request.attributes()) {
            attributeMap.put(attribute,request.attribute(attribute));
        }
        this.attributes = attributeMap;
        this.queryMap = request.queryMap().toMap();
    }

    public void setQueryMap(Map<String, String[]> queryMap) {
        this.queryMap = queryMap;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String,Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String,Object> headers) {
        this.headers = headers;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public Set<String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Set<String> queryParams) {
        this.queryParams = queryParams;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
