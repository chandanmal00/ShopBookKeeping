package com.bookkeeping.config;

import com.bookkeeping.controller.SingletonConfiguration;
import com.bookkeeping.model.Location;
import com.bookkeeping.model.Shop;
import com.bookkeeping.utilities.BookKeepingException;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by chandanm on 6/26/16.
 */
public class I18nSingleton {


    private static Map<String,String> en_map =null;
    private static Map<String,String> hi_map =null;
    private static volatile I18nSingleton  instance = null;
    static final Logger logger = LoggerFactory.getLogger(I18nSingleton.class);

    private I18nSingleton() {
    }

    public static I18nSingleton getInstance() {
        if(instance ==null) synchronized (I18nSingleton.class) {
            if (instance == null) {
                //ResourceBundle rb = ResourceBundle.getBundle("global", Locale.US);
                try {
                    logger.info("Reading properties passed as an argument:" + "global");
                    //InputStream inputStream = new FileInputStream("global","UTF-8");
                    InputStream stream = I18nSingleton.class.getClassLoader().getResourceAsStream("global_hi_IN"+".properties");
                    ResourceBundle bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));

                    en_map = getResourceMap(ResourceBundle.getBundle("global", Locale.US));
                    //hi_map = getResourceMap(ResourceBundle.getBundle("global", new Locale("hi", "IN")));
                    hi_map = getResourceMap(bundle);
                } catch (Exception e) {
                    logger.error("Exception in reading properties for i18n, loading only EN", e);
                    ResourceBundle resourceBundle = ResourceBundle.getBundle("global", Locale.US);
                    en_map = getResourceMap(resourceBundle);
                    hi_map = getResourceMap(resourceBundle);
                }
                instance = new I18nSingleton();
            }

        }
        return instance;
    }


    private static Map<String,String> getResourceMap(ResourceBundle rb) {

        Map<String,String> map = new HashMap<String, String>();
        for(String key : rb.keySet()) {
            String value = rb.getString(key);
            //logger.info("Old Value:" + value);
            try {
                byte ptext[] = value.getBytes("UTF-8");
                String newValue = new String(ptext, "UTF-8");
                //logger.info("New Value:" + newValue);
                map.put(key,newValue);
            }catch (Exception e) {
                logger.error("Unsupported encoding for key:{}",key,e);
                map.put(key, value);
            }

        }
        return map;
    }

    public static Map<String,String> get_EnMap() {
        getInstance();
        return en_map;
    }

    public static Map<String,String> get_HiMap() {
        getInstance();
        return hi_map;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String p = "%E0%A4%A6%E0%A4%B0%E0%A5%8D%E0%A4%B6%E0%A4%A8";
        String v = "दर्शन";

        String q = new String(p.getBytes("UTF8"),"UTF8");
        System.out.println(q + ":"+ p );
        System.out.println(v);
        System.out.println(new String(v.getBytes("UTF8"),"UTF8"));
    }


}
