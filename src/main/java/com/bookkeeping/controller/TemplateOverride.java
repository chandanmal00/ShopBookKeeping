package com.bookkeeping.controller;

import com.bookkeeping.config.I18nSingleton;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.logging.LogResult;
import com.bookkeeping.utilities.ControllerUtilities;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by chandan on 9/27/2015.
 */
public class TemplateOverride {

    Template template;
    String templateName;
    Request request;
    Response response;
    Configuration cfg;
    SessionDAO sessionDAO = SingletonManagerDAO.getInstance().getSessionDAO();
    static final Logger logger = LoggerFactory.getLogger(TemplateOverride.class);
    public TemplateOverride() {
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }


    public void setRequest(Request request) {
        this.request = request;
    }


    public void setResponse(Response response) {
        this.response = response;
    }

    public TemplateOverride(Request request, Response response, Template template) {
        this.request = request;
        this.response = response;
        this.template = template;
    }

    public void process(Object rootMap, Writer out) throws TemplateException, IOException {


        String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
        /*
        System.out.println("User name found:"+username);
        if (username == null) {
            response.redirect("/login");
            return;
        }*/

        Map<String,Object> root = null;
        try {
            root = (Map<String, Object>) rootMap;
            //IMP: for login control
            root.put("username",username);
            if(username!=null && username.equals(Constants.ADMIN_USER)) {
                root.put("admin", "true");
            }

            root.put("APP_TITLE", Constants.APP_TITLE);
            root.put("APP_LINK", Constants.APP_LINK);
            root.put("APP_MESSAGE", Constants.APP_MESSAGE);
            root.put("APP_WEBSITE", Constants.APP_WEBSITE);
            root.put("APP_WEBSITE_BIZ", Constants.APP_WEBSITE_BIZ);
            root.put("INFO_EMAIL", Constants.INFO_EMAIL);
            root.put("ADDRESS", Constants.APP_ADDRESS);
            //Hindi

            if(ControllerUtilities.getLangCookieActual(request)!=null) {
                this.setCfg(SingletonConfiguration.getInstance().getLocaleConfiguration());
                try {
                    //This pulls the needed template based on lang and then processes it
                    template = this.getCfg().getTemplate(this.getTemplateName());
                    this.setTemplate(template);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("Back to english");
                this.setCfg(SingletonConfiguration.getInstance().getConfiguration());
                try {
                    //This pulls the needed template based on lang and then processes it
                    logger.info("Name ::"+this.getTemplateName());
                    template = this.getCfg().getTemplate(this.getTemplateName());
                    this.setTemplate(template);
                    //templateOverride.setTemplate(this.getTemplate());
                    //templateOverride.setCfg(this.getCfg());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Map<String,String> localMap = null;
            if(this.cfg!=null && this.cfg.getLocale().getLanguage().equals("hi")) {
                localMap = I18nSingleton.get_HiMap();

            } else {
                localMap = I18nSingleton.get_EnMap();
            }

            root.put("local",localMap);
            logger.info("key:"+localMap.get("global.username"));


            //Handling Hindi
            if(ControllerUtilities.getLangCookieActual(request)!=null) {
                //root.put("APP_TITLE", Constants.APP_TITLE_HI);
                root.put("APP_MESSAGE", Constants.APP_MESSAGE_HI);
            }

            if (SingletonManagerDAO.getInstance().getCustomerDAO().count() > Constants.TRIAL_EDITION_LIMIT
                    || SingletonManagerDAO.getInstance().getItemDAO().count() > Constants.TRIAL_EDITION_LIMIT
                    || SingletonManagerDAO.getInstance().getCustomerTransactionDAO().count() > Constants.TRIAL_EDITION_LIMIT) {
                logger.info("You have reached the limit for trial edition, to continue usage you need to purchase this software. Please contact the system admin");
                ControllerUtilities.messageSoftwareTrial();
                root.put("trial","yes");
            }

            root.put("session", ControllerUtilities.getSessionCookie(request));
            this.template.process(root, out);
            LogResult.logIntoApplicationLogs(this.request, this.response, root);
        } catch(Exception e) {
            logger.warn("{} not matching map class",request.pathInfo());
            this.template.process(rootMap, out);
            LogResult.logIntoApplicationLogs(this.request, this.response,rootMap.toString());
        }
        /*
        } else {
            logger.warn("{} not matching map class",request.pathInfo());
            LogResult.logIntoApplicationLogs(this.request, this.response, "NULL");
        }
        */

    }

    public Configuration getCfg() {
        return cfg;
    }

    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }
}
