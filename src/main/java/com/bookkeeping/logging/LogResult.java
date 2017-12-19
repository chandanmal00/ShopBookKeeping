package com.bookkeeping.logging;


import com.bookkeeping.controller.SessionDAO;
import com.bookkeeping.model.AppLogs;
import com.bookkeeping.model.AppRequest;
import com.bookkeeping.model.AppResponse;
import com.bookkeeping.model.User;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.Map;

/**
 * Created by chandan on 9/27/2015.
 */
public class LogResult {
    static final Logger appLogger = LoggerFactory.getLogger("AppLogging");
    static final Logger logger = LoggerFactory.getLogger(LogResult.class);

    public static void logIntoApplicationLogs(Request request, Response response, Map<String,Object> data) {

        try {
            SessionDAO sessionDAO = new SessionDAO();
            String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
            AppRequest appRequest = new AppRequest(request);
            User user = new User();
            user.setUsername(username);
            user.setSession(ControllerUtilities.getSessionCookie(request));
            appRequest.setUser(user);

            AppResponse appResponse = new AppResponse();
            //appResponse.setResponse(data);
            appResponse.setRawResponse(response.body());
            AppLogs appLogs = new AppLogs(appRequest, appResponse);
            Gson gson = new Gson();
            appLogger.info(gson.toJson(appLogs));
        } catch(Exception e) {
            logger.error("Error in logging", request.pathInfo());
        }

    }


    public static void logIntoApplicationLogs(Request request, Response response, Object data) {

        try {
            SessionDAO sessionDAO = new SessionDAO();
            String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
            AppRequest appRequest = new AppRequest(request);
            User user = new User();
            user.setUsername(username);
            user.setSession(ControllerUtilities.getSessionCookie(request));
            appRequest.setUser(user);

            AppResponse appResponse = new AppResponse();
            //appResponse.setResponse(data);
            appResponse.setRawResponse(response.body());
            AppLogs appLogs = new AppLogs(appRequest, appResponse);
            Gson gson = new Gson();
            appLogger.info(gson.toJson(appLogs));
        }catch(Exception e) {
            logger.error("Error in logging", request.pathInfo());
        }

    }
}
