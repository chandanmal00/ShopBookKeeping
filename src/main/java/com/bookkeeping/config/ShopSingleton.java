package com.bookkeeping.config;

import com.bookkeeping.model.Shop;
import com.bookkeeping.model.Location;
import com.bookkeeping.utilities.BookKeepingException;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chandanm on 6/26/16.
 */
public class ShopSingleton {


    public static boolean NO_INPUT=false;
    public static boolean INITIALIZED=false;
    public static volatile Shop shop;
    static final Logger logger = LoggerFactory.getLogger(ShopSingleton.class);
    public static volatile String propFile;

    private ShopSingleton() {
    }

    public static Shop getInstance(String configFile) {
        if(shop ==null) synchronized (ShopSingleton.class) {
            if (shop == null) {
                InputStream inputStream = null;
                //tackle the first time when propFile is not SET
                if (propFile == null) {
                    propFile = configFile;
                }

                try {
                    new ShopSingleton();
                    Properties prop = new Properties();
                    if (NO_INPUT) {
                        //inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
                        logger.info("Reading local config.properties file:" + propFile);
                        inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(propFile);
                        //inputStream = ShopSingleton.class.getResourceAsStream(propFile);
                    } else {
                        logger.info("Reading properties passed as an argument:" + propFile);
                        inputStream = new FileInputStream(propFile);
                    }

                    if (inputStream == null) {
                        throw new Exception("InputStream in null");
                    }
                    prop.load(inputStream);
                    String name = prop.getProperty("NAME");
                    Shop shopLocal = new Shop(name);
                    shopLocal.setProprietor(prop.getProperty("PROPRIETOR"));
                    shopLocal.setFirmName(prop.getProperty("FIRM_NAME"));
                    shopLocal.setMarkupPercent(Float.parseFloat(prop.getProperty("MARKUP_PERCENT")));
                    if(shopLocal.getMarkupPercent()<=100 && shopLocal.getMarkupPercent()>=0) {
                        logger.info("MarkupPercent:{} is in range", shopLocal.getMarkupPercent());
                    } else {
                        throw new BookKeepingException("MarkupPercentage out of range, it should be >=0 and <=100");
                    }
                    shopLocal.setMarkupMultiplier((shopLocal.getMarkupPercent()/100)+1);


                    String place = prop.getProperty("PLACE");
                    Location location = new Location(place);
                    location.setAddress(prop.getProperty("ADDRESS"));
                    location.setDistrict(prop.getProperty("DISTRICT"));
                    location.setState(prop.getProperty("STATE"));
                    location.setTaluka(prop.getProperty("TALUKA"));
                    shopLocal.setLocation(location);

                    shop = shopLocal;
                    Gson gson = new Gson();
                    logger.info(gson.toJson(shopLocal));
                } catch (Exception e) {
                    logger.info("Error in reading file:{}", configFile, e);
                    System.out.println("Error in reading file:" + configFile);
                    e.printStackTrace();
                    System.exit(-3);

                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        }
        return shop;
    }

    /**
     * Tells the code to use config.properties part of the code resource vs external
     * if true we will use properties file passed as a argument
     */
    public static void init() {
        NO_INPUT=true;
    }

    public static void main(String[] args) {
       // ShopSingleton.init();
        ShopSingleton.init();
        ShopSingleton.getInstance("config.properties");
    }

}
