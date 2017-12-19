package com.bookkeeping.controller;

import freemarker.template.Configuration;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by chandan on 6/20/16.
 */
public class SingletonConfiguration {

    private static volatile SingletonConfiguration instance;
    private Configuration configuration;
    private Configuration localeConfiguration;


    private SingletonConfiguration(Locale locale) {
        //EN
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(SingletonConfiguration.class, "/freemarker");
        retVal.setLocale(Locale.US);


        //HI
        configuration = retVal;
        localeConfiguration = (Configuration)retVal.clone();
        localeConfiguration.setClassForTemplateLoading(SingletonConfiguration.class, "/freemarker");
        localeConfiguration.setLocale(locale);


        //localeConfiguration.setNumberFormat(NumberFormat.getNumberInstance(Locale.US).toString());
        //System.out.println(configuration);
    }

    public static SingletonConfiguration getInstance() {
        if(instance==null) {
            synchronized (SingletonConfiguration.class) {
                if(instance==null) {
                    Locale locale = new Locale("hi","IN");
                    instance = new SingletonConfiguration(locale);
                }
            }

        }
        return instance;

    }

    public Configuration getConfiguration() {
        return configuration;
    }
    public Configuration getLocaleConfiguration() {
        return localeConfiguration;
    }

}
