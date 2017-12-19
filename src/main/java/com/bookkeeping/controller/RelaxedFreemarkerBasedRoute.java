package com.bookkeeping.controller;

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
abstract class RelaxedFreemarkerBasedRoute extends Route {
    //final Template template;
    private Template template;
    private String templateName;
    final TemplateOverride templateOverride = new TemplateOverride();
    static final Logger logger = LoggerFactory.getLogger(FreemarkerBasedRoute.class);
    static Configuration cfg;
    SessionDAO sessionDAO = SingletonManagerDAO.getInstance().getSessionDAO();
    /**
     * Constructor
     *
     * @param path The route path which is used for matching. (e.g. /hello, users/:name)
     */
    public RelaxedFreemarkerBasedRoute(final String path, final String templateName) throws IOException {
        super(path);
        this.setTemplateName(templateName);


        //cfg = SingletonConfiguration.getInstance().getConfiguration();
        //template = cfg.getTemplate(templateName);
        templateOverride.setTemplateName(templateName);
        //templateOverride.setTemplate(template);
        //templateOverride.setCfg(cfg);
        logger.info("RelaxedFreemarkerBasedRoute called for:{}, template:{}",path,templateName);

    }

    public RelaxedFreemarkerBasedRoute(final String path) throws IOException {
        super(path);
        //cfg = SingletonConfiguration.getInstance().getConfiguration();
        //templateOverride.setCfg(cfg);
        logger.info("RelaxedFreemarkerBasedRoute called for:{}",path);

    }

    @Override
    public Object handle(Request request, Response response) {
        StringWriter writer = new StringWriter();

        /*
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
                template = this.getCfg().getTemplate(this.getTemplateName());
                templateOverride.setTemplate(template);
                templateOverride.setCfg(this.getCfg());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
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


    //protected abstract void doHandle(final Request request, final Response response, final Writer writer, final String templateName)
    //      throws IOException, TemplateException;


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

    public TemplateOverride getTemplateOverride() {
        return templateOverride;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
