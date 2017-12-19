package com.bookkeeping.controller;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.utilities.ControllerUtilities;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Created by chandan on 6/20/16.
 */
abstract class FreemarkerBasedRoute extends Route {
    //final Template template;
    Template template;
    String templateName;
    final TemplateOverride templateOverride = new TemplateOverride();
    static final Logger logger = LoggerFactory.getLogger(FreemarkerBasedRoute.class);
    static Configuration cfg;
    SessionDAO sessionDAO = SingletonManagerDAO.getInstance().getSessionDAO();


    /**
     * Constructor
     *
     * @param path The route path which is used for matching. (e.g. /hello, users/:name)
     */
    public FreemarkerBasedRoute(final String path, final String templateName) throws IOException {
        super(path);
        this.setTemplateName(templateName);
        //cfg = SingletonConfiguration.getInstance().getConfiguration();
        /*
        this.setTemplateName(templateName);
        */
        //template = cfg.getTemplate(templateName);
        templateOverride.setTemplateName(templateName);
        //templateOverride.setTemplate(template);

        logger.info("RelaxedFreemarkerBasedRoute called for:{}, template:{}", path,templateName);
    }

    public FreemarkerBasedRoute(final String path) throws IOException {
        super(path);
        //cfg = SingletonConfiguration.getInstance().getConfiguration();
        //templateOverride.setCfg(cfg);
        //templateOverride.setTemplate(template);
        logger.info("RelaxedFreemarkerBasedRoute called for:{}",path);

    }

    @Override
    public Object handle(Request request, Response response) {
        //Only if langCookie is set
        try {
            request.raw().setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            logger.info("some format exception in FreemarketBasedRoute handle method");
        }
        /*
        //Below code helps with selection of lang based templates, since we switched to global properties we can avoid it
        if(ControllerUtilities.getLangCookieActual(request)!=null) {
            this.setCfg(SingletonConfiguration.getInstance().getLocaleConfiguration());
            try {
                //This pulls the needed template based on lang and then processes it
                template = this.getCfg().getTemplate(this.getTemplateName());
                templateOverride.setTemplate(template);
                templateOverride.setCfg(this.getCfg());
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
                    templateOverride.setTemplate(template);
                    //templateOverride.setTemplate(this.getTemplate());
                    templateOverride.setCfg(this.getCfg());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        */
        String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
        logger.debug("Checking if User Logged in, otherwise asking for login");
        if (username == null) {

            response.redirect("/login");
            return null;
        }

        if(username!= null
                && !username.equals(Constants.ADMIN_USER) &&
                ( request.pathInfo().startsWith("/remove")
                        || request.pathInfo().equals("/save")
                        || request.pathInfo().equals("/restore"))
                ){
            logger.error("User:{} is accessing privileged areas");
            halt(401,"You do not have privileges, this incident will be reported..., return to home <a href=\"/\">Home</a>");

        }
        StringWriter writer = new StringWriter();
        templateOverride.setRequest(request);
        templateOverride.setResponse(response);
        try {
            doHandle(request, response, writer);
        } catch (Exception e) {
            logger.error("Internal Error while reading the request,",e);
            response.redirect("/internal_error");
        }
        return writer;
    }

    protected abstract void doHandle(final Request request, final Response response, final Writer writer)
            throws IOException, TemplateException;

    public Configuration getCfg() {
        return cfg;
    }

    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    //protected abstract void doHandle(final Request request, final Response response, final Writer writer, final String templateName)
      //      throws IOException, TemplateException;

}
