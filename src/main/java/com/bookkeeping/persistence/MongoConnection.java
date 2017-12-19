package com.bookkeeping.persistence;

import com.bookkeeping.constants.Constants;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chandan on 9/21/2015.
 */
public class MongoConnection {
    static final Logger logger = LoggerFactory.getLogger(MongoConnection.class);

    private static MongoDatabase database = null;
    private static MongoClient mongoClient =null;
    private static int mongoPort;
    private static String mongoHost;
    private static String mongoInstallationDir;
    private static boolean isInitialized = false;


    private MongoConnection() {
        //logger.info("Starting Application to mongoURI:{}",Constants.MONGO_URI);
        //mongoClient = new MongoClient(new MongoClientURI(Constants.MONGO_URI,mongoPort));
        mongoClient = new MongoClient(mongoHost,mongoPort);
        logger.info("Mongo host:{}, port: {} ",mongoClient.getAddress().getHost(), mongoClient.getAddress().getPort());
        database = mongoClient.getDatabase(Constants.MONGO_DATABASE);
    }

    public static MongoDatabase getInstance() {
        if(isInitialized) {
            if (database == null) {
                synchronized (MongoConnection.class) {
                    if (database == null) {

                        logger.info("Getting a new Connection for Mongo");
                        new MongoConnection();
                    }
                }
            }
        } else {
            logger.error("MongoConnection not initialized, first call init with port and dbDir, exiting...");
            System.exit(-3);
        }
        return database;
    }

    public static void shutDown() {
        if(mongoClient!=null) {
            mongoClient.close();
        }
    }

    public static void init(int port,String host, String installationDir) {
        mongoPort = port;
        mongoHost = host;
        mongoInstallationDir = installationDir;
        isInitialized = true;
    }

    public static void main(String[] args) {
        MongoConnection.init(27018,"localhost",Constants.MONGO_INSTALLATION);
        MongoDatabase db = MongoConnection.getInstance();

    }

    public static String getMongoInstallationDir() {
        return mongoInstallationDir;
    }

    public static int getMongoPort() {
        return mongoPort;
    }

}
