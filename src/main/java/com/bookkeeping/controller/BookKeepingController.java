package com.bookkeeping.controller;

import com.bookkeeping.DAO.*;
import com.bookkeeping.config.ShopSingleton;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.factory.factoryDAO;
import com.bookkeeping.model.*;
import com.bookkeeping.persistence.MongoConnection;
import com.bookkeeping.utilities.BookKeepingException;
import com.bookkeeping.utilities.ControllerUtilities;
import com.bookkeeping.utilities.MongoAdminCommand;
import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.Cookie;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

/**
 * This class encapsulates the controllers for the Ask for Help web application.
 * Entry point into the web application.
 */
public class BookKeepingController {
    private final Configuration cfg;
    private final int port;
    private static String backUpDir;

    private final CompanyDAO companyDAO;
    private final ItemDAO itemDAO;
    private final ItemSoldDAO itemSoldDAO;
    private final ItemInventoryDAO itemInventoryDAO;
    private final SalesmanDAO salesmanDAO;
    private final CustomerTransactionDAO customerTransactionDAO;
    private final CustomerDAO customerDAO;
    private final CustomerPaymentDAO customerPaymentDAO;

    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    static final Logger logger = LoggerFactory.getLogger(BookKeepingController.class);
    static final Logger appLogger = LoggerFactory.getLogger("AppLogging");
    final static ThreadPoolExecutor MONGO_DB_THREAD_POOL = new ThreadPoolExecutor(2, // core size
            30, // max size
            10, // idle timeout
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(40));

    /*
    final static ThreadPoolExecutor ELASTIC_SEARCH_THREAD_POOL = new ThreadPoolExecutor(2, // core size
            30, // max size
            30, // idle timeout
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(40));
*/

    public static void main(String[] args) throws IOException {
        logger.info("Starting Application:");
        try {

            logger.info("Input args length:{}",args.length);
            if (args.length == 0) {
                MongoConnection.init(Constants.MONGO_PORT,Constants.MONGO_HOST,Constants.MONGO_INSTALLATION);
                logger.info("Mongo installation:"+ MongoConnection.getMongoInstallationDir());
                backUpDir = Constants.MONGO_DATABASE_BACKUP_DIR_ROOT;
                ShopSingleton.init();
                ShopSingleton.getInstance("config.properties");
                //ShopSingleton.getInstance("properties");
                int trialLimit = Constants.TRIAL_EDITION_LIMIT;


                if(SingletonManagerDAO.getInstance().getCustomerDAO().count()>trialLimit
                        || SingletonManagerDAO.getInstance().getItemDAO().count()>trialLimit
                        || SingletonManagerDAO.getInstance().getCustomerTransactionDAO().count()>trialLimit) {
                    logger.info("You have reached the limit for trial edition, to continue usage you need to purchase this software. Please contact the system admin");
                    ControllerUtilities.messageSoftwareTrial();

                }
                new BookKeepingController(Constants.SITE_PORT);

            }
            else {
                int NO_ARGS = 6;
                if(args.length==NO_ARGS) {

                    String portStr = args[0];
                    String mongoPortStr = args[1];
                    String mongoHost = args[2];
                    String backupDirLocal = args[3];
                    String mongoInstallationDir = args[4];
                    String propertiesFullFilePath = args[5];

                    try {

                        int port = Integer.parseInt(portStr);
                        int mongoPort = Integer.parseInt(mongoPortStr);
                        checkDirExistsOtherwiseCreate(backupDirLocal);
                        if(!checkIfDirExists(mongoInstallationDir)) {
                            logger.error("MONGO installation dir:{} is not there, we cannot continue",mongoInstallationDir);
                            System.exit(-3);
                        }

                        logger.info("Port changed to:{}", port);
                        ShopSingleton.getInstance(propertiesFullFilePath);
                        MongoConnection.init(mongoPort, mongoHost, mongoInstallationDir);
                        backUpDir = backupDirLocal;
                        int trialLimit = Constants.TRIAL_EDITION_LIMIT;

                        if(SingletonManagerDAO.getInstance().getCustomerDAO().count()>trialLimit
                                || SingletonManagerDAO.getInstance().getItemDAO().count()>trialLimit
                                || SingletonManagerDAO.getInstance().getCustomerTransactionDAO().count()>trialLimit) {
                            logger.info("You have reached the limit for trial edition, to continue usage you need to purchase this software. Please contact the system admin");
                            ControllerUtilities.messageSoftwareTrial();
                        }

                        new BookKeepingController(port);

                        /*
                        int port = Integer.parseInt(portStr);
                        int mongoPort = Integer.parseInt(mongoPortStr);
                        checkDirExistsOtherwiseCreate(backupDirLocal);
                        if(!checkIfDirExists(mongoInstallationDir)) {
                            logger.error("MONGO installation dir:{} is not there, we cannot continue",mongoInstallationDir);
                            System.exit(-3);
                        }

                        logger.info("Port changed to:{}", port);
                        MongoConnection.init(mongoPort,mongoHost,mongoInstallationDir);
                        backUpDir = backupDirLocal;

                        new BookKeepingController(port);
                        */
                    } catch(Exception e) {
                        logger.error("Arguments not matching the format expected, so exiting, exception:",e);
                        e.printStackTrace();
                        System.exit(-3);
                    }

                } else {
                    logger.error("Required arguments cnt:{} did not come, so aborting",NO_ARGS);
                    System.out.println("Required arguments cnt:"+NO_ARGS+" did not come, so aborting, input arg length:"+ args.length);
                    System.out.println("Usage:: script <port> <mongoPort> <backupDir>");
                    System.exit(-2);
                }
            }
        } catch(Exception e) {
            logger.info("Application shutting down, closing all connections:", e);
            try {
                MONGO_DB_THREAD_POOL.shutdown();
                //ELASTIC_SEARCH_THREAD_POOL.shutdown();
                //while (!MONGO_DB_THREAD_POOL.isTerminated() || !ELASTIC_SEARCH_THREAD_POOL.isTerminated()) {
                while (!MONGO_DB_THREAD_POOL.isTerminated()) {
                    Thread.sleep(1000);
                    logger.info("waiting for MONGO and ELASTIC threads to shutdown");
                }
            } catch(Exception ez) {
                logger.error("Error in closing Threads",ez);
            } finally {
                //ElasticCacheConnection.shutDown();
                MongoConnection.shutDown();
                logger.info("Shutdown Complete");
            }
            System.out.println("Application closing, some error");
            e.printStackTrace();
        }
    }

    private static void checkDirExistsOtherwiseCreate(String dir) {
        if(checkIfDirExists(dir)) {
            logger.info("Directory already exists, dir:{}",dir);
        } else {
            File f = new File(dir);
            f.mkdirs();
            logger.info("Created dir:{}",dir);
        }
    }

    private static boolean checkIfDirExists(String dir) {
        File f = new File(dir);
        if(!f.exists()) {
            return false;
        }
        if(f.exists() && f.isDirectory()) {
            logger.info("We are good the backupDir exists");
            return true;
        }
        throw new IllegalArgumentException("Some issue with the dir:"+dir);
    }

    public BookKeepingController(int port) throws IOException {
        logger.info("Starting Application on port:{}",port);

        this.port = port;


        customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
        itemDAO = SingletonManagerDAO.getInstance().getItemDAO();
        itemSoldDAO = SingletonManagerDAO.getInstance().getItemSoldDAO();
        itemInventoryDAO = SingletonManagerDAO.getInstance().getItemInventoryDAO();
        salesmanDAO = SingletonManagerDAO.getInstance().getSalesmanDAO();
        companyDAO = SingletonManagerDAO.getInstance().getCompanyDAO();
        customerDAO = SingletonManagerDAO.getInstance().getCustomerDAO();
        customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();

        userDAO = SingletonManagerDAO.getInstance().getUserDAO();
        sessionDAO = SingletonManagerDAO.getInstance().getSessionDAO();

        //Create admin user for the first time if not exist.
        if(!userDAO.exists(Constants.ADMIN_USER)) {
            userDAO.addUser(Constants.ADMIN_USER, Constants.ADMIN_PASS, Constants.ADMIN_USER);
        }

        //Create required indexes for all collections
        ControllerUtilities.initSetupCreateIndex();

        String passwordForCertificate = "badman123";
        String keyStoreName = "D:\\ssl\\jainTraveller\\keystore.jks";
        String truststoreFile = "D:\\ssl\\truststore.ts";

        this.cfg = SingletonConfiguration.getInstance().getConfiguration();//createFreemarkerConfiguration();
        logger.info("Configuration::{}",cfg.toString());
        logger.info("Port configured: {}",port);
        setPort(port);

        //setSecure(keyStoreName, passwordForCertificate, truststoreFile, passwordForCertificate);
        externalStaticFileLocation("D:\\var\\www\\public\\");
        staticFileLocation("/public");
        //setSecure(keyStoreName,passwordForCertificate,truststoreFile,passwordForCertificate);
        logger.info("starting all routes");




        CustomerTransactionController customerTransactionController = new CustomerTransactionController();
        customerTransactionController.initializeRoutes();

        SalesmanController salesmanController = new SalesmanController();
        salesmanController.initializeRoutes();

        ItemController itemController = new ItemController();
        itemController.initializeRoutes();

        CustomerController customerController = new CustomerController();
        customerController.initializeRoutes();

        CustomerPaymentController customerPaymentController = new CustomerPaymentController();
        customerPaymentController.initializeRoutes();

        CompanyController companyController = new CompanyController();
        companyController.initializeRoutes();

        int maxThreads = 16;
        int minThreads = 3;
        int timeOutMillis = 30000;
        //threadPool(maxThreads, minThreads, timeOutMillis);


        initializeRoutes();
        initializeStandardLoginSignUpRoutes();
        initializeListingRoutes();
        initializeStandardCalls();
        initializeWeeklySummaryCalls();
        initializeMongoAdminCalls();
        logger.info("started all routes");


        /*
        Filter filter = new Filter() {
            @Override
            public void handle(Request request, Response response) {
                response.header("Content-Encoding", "gzip");
            }
        };
        after(filter);

        */
        /*
        Filter filter = new Filter() {
            @Override
            public void handle(Request request, Response response) {

                Map<String,Object> root = new HashMap<String, Object>();
                int trialLimit = Constants.TRIAL_EDITION_LIMIT;
                Set<String> pathsToIgnore = new HashSet<String>();
                pathsToIgnore.add("trial_end");
                pathsToIgnore.add("/");
                pathsToIgnore.add("/login");
                pathsToIgnore.add("/signup");
                pathsToIgnore.add("/internal_error");
                pathsToIgnore.add("/post_not_found");
                pathsToIgnore.add("/disclaimer");
                pathsToIgnore.add("/tos");
                pathsToIgnore.add("/about");
                pathsToIgnore.add("/privacy");
                logger.info(request.pathInfo());
                if(!pathsToIgnore.contains(request.pathInfo())) {
                } else {
                    if (SingletonManagerDAO.getInstance().getCustomerDAO().count() > trialLimit
                            || SingletonManagerDAO.getInstance().getItemDAO().count() > trialLimit
                            || SingletonManagerDAO.getInstance().getCustomerTransactionDAO().count() > trialLimit) {
                        logger.info("You have reached the limit for trial edition, to continue usage you need to purchase this software. Please contact the system admin");
                        ControllerUtilities.messageSoftwareTrial();
                        //throw new BookKeepingException("You have reached the limit for trial edition, to continue usage you need to purchase this software. Please contact the system admin");

                        response.redirect("/trial_end");

                        return;
                    }
                }


            }
        };

        before(filter);
        */
    }

    private void initializeMongoAdminCalls() throws IOException {

        get(new FreemarkerBasedRoute("/print/:entity/:key") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                Map<String,Object> root = new HashMap<String, Object>();

                String entity = StringEscapeUtils.escapeHtml4(request.params(":entity"));
                String key =  StringEscapeUtils.escapeHtml4(request.params(":key"));
                logger.info("Entity:{}, key:{} to print", entity, key);

                String dateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                root.put("dateStr",dateStr);
                root.put("operation","Print");
                root.put("key",key);
                root.put("entity",entity);

                //this.template = cfg.getTemplate("print_default.ftl");
                this.templateOverride.setTemplateName("print_default.ftl");


                if(StringUtils.isNotBlank(entity) && StringUtils.isNotBlank(key)) {
                    MongoCollectionDAO mongoCollectionDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    if(mongoCollectionDAO==null) {
                        root.put(Constants.ERROR,"Bad entity:"+entity);
                        templateOverride.process(root, writer);
                        return;
                    } else {
                        logger.info("Key:{}, entity:{}",key,entity);
                        Object mongoObject = mongoCollectionDAO.getBasedOnUniqueKey(key);
                        if(mongoObject!=null) {
                            try {
                                root.put("entityObject",mongoObject);
                                root.put(Constants.SUCCESS, "true");
                                String templateName = "print_"+entity +".ftl";
                                logger.info("Using template:"+templateName);
                                templateOverride.setTemplateName(templateName);
                                templateOverride.process(root, writer);

                                return;
                            } catch(Exception e) {
                                logger.error("Bad Template processing for entity:{} ",entity,e);
                                root.put(Constants.ERROR,"Bad template processing for entity:"+entity);
                                templateOverride.process(root, writer);
                                return;
                            }
                        } else {
                            logger.error("Bad Key:{} for entity:{}, we could not find anything in db",key,entity);
                            root.put(Constants.ERROR,"Bad Key:"+key+" for entity:"+entity+", we could not find anything in db");
                            templateOverride.process(root, writer);
                            return;

                        }

                    }
                } else {
                    logger.error("Bad request, One of the inputs is missing, entity:{} or query:{}",entity,key);
                    root.put("error","Bad request, One of the inputs is missing, entity:"+entity+" or key:"+key);
                    templateOverride.process(root, writer);
                }

            }
        });

        get(new FreemarkerBasedRoute("/details/:entity/:key") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                Map<String,Object> root = new HashMap<String, Object>();

                String entity = StringEscapeUtils.escapeHtml4(request.params(":entity"));
                String key =  StringEscapeUtils.escapeHtml4(request.params(":key"));
                // StringEscapeUtils.unescapeHtml4(":entity)
                logger.info("Entity:{}, key:{} to print", entity, key);

                String dateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                root.put("dateStr",dateStr);
                root.put("operation","Print");
                root.put("key",key);
                root.put("entity",entity);

                //this.template = cfg.getTemplate("print_default.ftl");
                this.templateOverride.setTemplateName("print_default.ftl");


                if(StringUtils.isNotBlank(entity) && StringUtils.isNotBlank(key)) {
                    MongoCollectionResponse mongoCollectionResponse = factoryDAO.getDAO(entity);
                    if(mongoCollectionResponse==null) {
                        root.put(Constants.ERROR,"Bad entity:"+entity);
                        templateOverride.process(root, writer);
                        return;
                    } else {
                        logger.info("Key:{}, entity:{}",key,entity);
                        Object mongoObject = mongoCollectionResponse.getMongoCollectionDAO().getBasedOnUniqueKey(key);
                        if(mongoObject!=null) {
                            try {
                                root.put("entityObject",mongoObject);
                                root.put(Constants.SUCCESS, "true");
                                String templateName = "print_" + entity + ".ftl";
                                logger.info("Using template:" + templateName);
                                templateOverride.setTemplateName(templateName);
                                templateOverride.process(root, writer);

                                return;
                            } catch(Exception e) {
                                logger.error("Bad Template processing for entity:{} ",entity,e);
                                root.put(Constants.ERROR,"Bad template processing for entity:"+entity);
                                templateOverride.process(root, writer);
                                return;
                            }
                        } else {
                            logger.error("Bad Key:{} for entity:{}, we could not find anything in db",key,entity);
                            root.put(Constants.ERROR,"Bad Key:"+key+" for entity:"+entity+", we could not find anything in db");
                            templateOverride.process(root, writer);
                            return;

                        }

                    }
                } else {
                    logger.error("Bad request, One of the inputs is missing, entity:{} or query:{}",entity,key);
                    root.put("error","Bad request, One of the inputs is missing, entity:"+entity+" or key:"+key);
                    templateOverride.process(root, writer);
                }

            }
        });

        get(new FreemarkerBasedRoute("/invoice/:entity/:key") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                Map<String,Object> root = new HashMap<String, Object>();

                String entity = StringEscapeUtils.escapeHtml4(request.params(":entity"));
                //String invoiceEntity = StringEscapeUtils.escapeHtml4(request.params(":invoiceEntity"));

                String key =  StringEscapeUtils.escapeHtml4(request.params(":key"));
                logger.info("Entity:{}, key:{}, invoiceEntity:{} to invoice", entity, key);

                String dateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                root.put("dateStr",dateStr);
                root.put("operation", "Invoice");
                root.put("key", key);
                root.put("entity",entity);
                root.put("broker",ShopSingleton.shop);
                //root.put("invoiceEntity", invoiceEntity);


                if(StringUtils.isNotBlank(entity)
                        && StringUtils.isNotBlank(key)) {
                    MongoCollectionResponse mongoCollectionResponse = factoryDAO.getDAO(entity);

                    if(mongoCollectionResponse==null) {
                        root.put(Constants.ERROR,"Bad entity:"+entity);
                    } else {
                        logger.info("Key:{}, entity:{}, ", key, entity);
                        Object mongoObject = mongoCollectionResponse.getMongoCollectionDAO().getBasedOnUniqueKey(key);
                        if(mongoObject!=null) {
                            try {

                                root.put("entityObject",mongoObject);
                                root.put(Constants.SUCCESS,"true");

                                String templateName = "invoice_" + entity + ".ftl";
                                logger.info("Using template:" + templateName);
                                templateOverride.setTemplateName(templateName);
                                templateOverride.process(root, writer);
                                return;
                            } catch(Exception e) {
                                logger.error("Bad Template processing for entity:{} ",entity,e);
                                root.put(Constants.ERROR,"Bad template processing for entity:"+entity);
                            }
                        } else {
                            logger.error("Bad Key:{} for entity:{}, we could not find anything in db",key,entity);
                            root.put(Constants.ERROR,"Bad Key:"+key+" for entity:"+entity+", we could not find anything in db");
                        }

                    }
                } else {
                    logger.error("Bad request, One of the inputs is missing, entity:{}" +
                            " or query:{} ",entity,key);
                    root.put("error", "Bad request, One of the inputs is missing, entity:" + entity + " or key:" + key);
                }
                //this.template = cfg.getTemplate("invoice.ftl");
                this.templateOverride.setTemplateName("invoice.ftl");
                templateOverride.process(root, writer);

            }
        });


/*
        get(new FreemarkerBasedRoute("/print/:entity/:key", "print.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                String key = request.params(":key");
                Map<String, Object> root = new HashMap<String, Object>();

                String dateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                root.put("dateStr",dateStr);
                root.put("operation","Print");
                if (ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();;

                    Object mongoObject = mongoDAO.getBasedOnId(key);
                    root.put("entityObject", mongoObject);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request, most like entity input:{} is bad", entity);
                    root.put(Constants.ERROR,"Some issue in processing this request, most like entity input:"+entity+" is bad");
                    templateOverride.process(root, writer);

                }

            }
        });
        */

        get(new FreemarkerBasedRoute("/save", "db_admin.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if(username==null || !username.equals(Constants.ADMIN_USER)) {
                    halt(401,"This request requires privilege");
                }
                */
                Map<String, Object> root = new HashMap<String, Object>();
                String dateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                root.put("dateStr",dateStr);
                String path = backUpDir +"/" +dateStr;
                root.put("dir",path);
                root.put("operation","Save");
                try {

                    MongoAdminCommand.exportMongoDatabaseToDir(dateStr,backUpDir);
                    root.put(Constants.SUCCESS,true);
                    BackupDAO backupDAO = SingletonManagerDAO.getInstance().getBackupDAO();
                    Backup backup = new Backup(dateStr,path);
                    backupDAO.add(backup);
                    logger.info("Added backup for date:{} in path:{}",dateStr,backup);

                } catch(BookKeepingException e) {
                    root.put(Constants.ERROR,e.getMessage());
                    logger.error("There was an error in exporting MongoDatabase, error:{}",e.getMessage(),e);
                } catch (Exception ex) {
                    logger.error("Mysterical error:",ex);
                    root.put(Constants.ERROR,"Please contact admin, there is some issue with MongoExport, error:"+ex.getMessage());

                }
                templateOverride.process(root, writer);

            }
        });

        get(new FreemarkerBasedRoute("/restore/:entity", "restore.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if(username==null || !username.equals(Constants.ADMIN_USER)) {
                    halt(401,"This request requires privilege");
                }
                */
                String entity = request.params(":entity");
                Map<String,Object> root = new HashMap<String, Object>();

                if(ControllerUtilities.verifyEntityInputs(entity,root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    String entityName = factoryDAO.getDAO(entity).getEntity();
                    root.put("ENTITY_NAME", entityName);
                    root.put("entity", entity);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        post(new FreemarkerBasedRoute("/restore", "db_admin.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if(username==null || !username.equals(Constants.ADMIN_USER)) {
                    halt(401,"This request requires privilege");
                }
                */
                String dateStr = StringEscapeUtils.escapeHtml4(request.queryParams("dateStr"));
                Map<String, Object> root = new HashMap<String, Object>();
                root.put("dateStr", dateStr);
                root.put("operation", "Restore");
                root.put("dir", backUpDir + "/" + dateStr);
                try {

                    MongoAdminCommand.importMongoDatabaseFromDir(backUpDir, dateStr);
                    root.put(Constants.SUCCESS, true);

                } catch (BookKeepingException e) {
                    root.put(Constants.ERROR, e.getMessage());
                    logger.error("There was an error in exporting MongoDatabase, error:{}", e.getMessage(),e);
                } catch (Exception ex) {
                    logger.error("Mysterical error", ex);
                    root.put(Constants.ERROR, "Please contact admin, there is some issue with MongoExport, error:"+ex.getMessage());

                }
                templateOverride.process(root, writer);

            }
        });

    }

    private void initializeWeeklySummaryCalls() throws IOException {

        get(new FreemarkerBasedRoute("/last7days", "daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("DAY",7);

                Gson gson = new Gson();
                String currentDateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                root.put("endingDate",currentDateStr);
                root.put("summaryType","daily");
                root.put("type", "days");

                List<Document> transactionList = SingletonManagerDAO.getInstance().getCustomerTransactionDAO().getDailySummaryForWeekEnding(currentDateStr);
                List<Document> paymentList = SingletonManagerDAO.getInstance().getCustomerPaymentDAO().getDailySummaryForWeekEnding(currentDateStr);

                root.put("transactionList", transactionList);
                root.put("paymentList", paymentList);
                Map<String,Tuple<Document>> joinMap = ControllerUtilities.joinByIdKey(transactionList,paymentList);
                root.put("joinMap", joinMap);
                //logger.info(gson.toJson(joinMap));
                templateOverride.process(root, writer);
                return;
            }
        });

        get(new FreemarkerBasedRoute("/last7days/:entity", "list_daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("ENTITY_NAME",entity);
                root.put("DAY",7);
                if (ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    String currentDateStr =  ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                    root.put("endingDate",currentDateStr);
                    root.put("summaryType","daily");
                    root.put("type","days");

                    List<Document> list = mongoDAO.getDailySummaryForWeekEnding(currentDateStr);
                    root.put("entityList", list);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request, most like entity input:{} is bad", entity);
                    root.put("error","Some issue in processing this request, most like entity input:"+entity+" is bad");
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);

                }
            }
        });

        get(new FreemarkerBasedRoute("/last7days/:entity/:toDate", "list_daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                String toDateStr = request.params(":toDate");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("ENTITY_NAME",entity);
                root.put("DAY",7);
                if (ControllerUtilities.verifyDateInFormat(toDateStr) && ControllerUtilities.verifyEntityInputs(entity,root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    root.put("endingDate",toDateStr);
                    root.put("summaryType","daily");
                    root.put("type","days");
                    List<Document> list = mongoDAO.getDailySummaryForWeekEnding(toDateStr);
                    root.put("entityList", list);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request, most like entity input:{} is bad", entity);
                    String error = null;
                    StringBuffer sb = new StringBuffer();
                    if(root.get("error")!=null) {
                        sb.append(root.get("error"));
                        sb.append("<br>");
                    }
                    sb.append("Some issue in processing this request, most like entity input:"+entity+" is bad or date input:"+toDateStr+" is bad");
                    root.put("error",sb.toString());
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);

                }
            }
        });


        get(new FreemarkerBasedRoute("/last30days", "daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("DAY",30);

                Gson gson = new Gson();
                String currentDateStr =  ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                root.put("endingDate",currentDateStr);
                root.put("summaryType","daily");
                root.put("type","days");

                /*
                List<Document> list = kisaanPaymentDAO.getDailySummaryFor30daysEnding(currentDateStr);
                root.put("kisaanPaymentList", list);
                */

                List<Document> transactionList = SingletonManagerDAO.getInstance().getCustomerTransactionDAO().getDailySummaryFor30daysEnding(currentDateStr);
                List<Document> paymentList = SingletonManagerDAO.getInstance().getCustomerPaymentDAO().getDailySummaryFor30daysEnding(currentDateStr);
                root.put("transactionList", transactionList);
                root.put("paymentList", paymentList);
                Map<String,Tuple<Document>> joinMap = ControllerUtilities.joinByIdKey(transactionList,paymentList);
                root.put("joinMap",joinMap);

                templateOverride.process(root, writer);
                return;
            }
        });

        get(new FreemarkerBasedRoute("/last30days/:entity", "list_daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("ENTITY_NAME",entity);
                root.put("DAY", 30);
                if (ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    String currentDateStr =  ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                    root.put("endingDate",currentDateStr);
                    root.put("summaryType","daily");
                    root.put("type","days");
                    List<Document> list = mongoDAO.getDailySummaryFor30daysEnding(currentDateStr);
                    root.put("entityList", list);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request, most like entity input:{} is bad", entity);
                    root.put("error", "Some issue in processing this request, most like entity input:" + entity + " is bad");
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);

                }
            }
        });

        get(new FreemarkerBasedRoute("/last30days/:entity/:toDate", "list_daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                String toDateStr = request.params(":toDate");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("ENTITY_NAME",entity);
                root.put("DAY", 30);
                if (ControllerUtilities.verifyDateInFormat(toDateStr) && ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    root.put("endingDate",toDateStr);
                    root.put("summaryType","daily");
                    root.put("type","days");
                    List<Document> list = mongoDAO.getDailySummaryFor30daysEnding(toDateStr);
                    root.put("entityList", list);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request, most like entity input:{} is bad", entity);
                    String error = null;
                    StringBuffer sb = new StringBuffer();
                    if(root.get("error")!=null) {
                        sb.append(root.get("error"));
                        sb.append("<br>");
                    }
                    sb.append("Some issue in processing this request, most like entity input:"+entity+" is bad or date input:"+toDateStr+" is bad");
                    root.put("error", sb.toString());
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);

                }
            }
        });

        get(new FreemarkerBasedRoute("/quarterly", "daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("DAY",3);
                Gson gson = new Gson();
                String currentDateStr =  ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                root.put("endingDate",currentDateStr);
                root.put("summaryType","quarterly");
                root.put("type","months");

                List<Document> transactionList = SingletonManagerDAO.getInstance().getCustomerTransactionDAO().getMonthlySummary(currentDateStr,-3);
                List<Document> paymentList = SingletonManagerDAO.getInstance().getCustomerPaymentDAO().getMonthlySummary(currentDateStr, -3);
                root.put("transactionList", transactionList);
                root.put("paymentList", paymentList);
                Map<String,Tuple<Document>> joinMap = ControllerUtilities.joinByIdKey(transactionList,paymentList);
                root.put("joinMap",joinMap);

                templateOverride.process(root, writer);
                return;
            }
        });


        get(new FreemarkerBasedRoute("/quarterly/:entity", "list_daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("ENTITY_NAME",entity);
                root.put("DAY", 3);
                if (ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    String currentDateStr =  ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
                    root.put("endingDate",currentDateStr);
                    root.put("summaryType","quarterly");
                    root.put("type","months");
                    List<Document> list = mongoDAO.getMonthlySummary(currentDateStr,-3);
                    root.put("entityList", list);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request, most like entity input:{} is bad", entity);
                    root.put("error","Some issue in processing this request, most like entity input:"+entity+" is bad");
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);

                }
            }
        });

        get(new FreemarkerBasedRoute("/quarterly/:entity/:toDate", "list_daily_summary.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                String toDateStr = request.params(":toDate");
                Map<String, Object> root = new HashMap<String, Object>();

                root.put("ENTITY_NAME", entity);
                root.put("DAY", 3);
                if (ControllerUtilities.verifyDateInFormat(toDateStr) && ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    root.put("endingDate", toDateStr);
                    root.put("summaryType", "quarterly");
                    root.put("type", "months");
                    List<Document> list = mongoDAO.getMonthlySummary(toDateStr, -3);
                    root.put("entityList", list);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request, most like entity input:{} is bad", entity);
                    String error = null;
                    StringBuffer sb = new StringBuffer();
                    if (root.get("error") != null) {
                        sb.append(root.get("error"));
                        sb.append("<br>");
                    }
                    sb.append("Some issue in processing this request, most like entity input:" + entity + " is bad or date input:" + toDateStr + " is bad");
                    root.put("error", sb.toString());
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);

                }
            }
        });

    }


    private void initializeStandardCalls() throws IOException {

        get(new Route("/listKeys/:entity") {
            @Override
            public Object handle(Request request, Response response) {
                String entity = request.params(":entity");

                Map<String,Object> root = new HashMap<String, Object>();

                if(ControllerUtilities.verifyEntityInputs(entity,root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    return gson.toJson(mongoDAO.listKeys());
                } else {
                    logger.error("Some issue in processing this request, most like entity input is bad, input:{}", entity);
                    return "[]";

                }
            }
        });

        get(new Route("/listFull/:entity") {
            @Override
            public Object handle(Request request, Response response) {
                String entity = request.params(":entity");

                Map<String,Object> root = new HashMap<String, Object>();

                if(ControllerUtilities.verifyEntityInputs(entity,root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    return gson.toJson(mongoDAO.list());
                } else {
                    logger.error("Some issue in processing this request, most like entity input is bad, input:{}",entity);
                    return "[]";

                }
            }
        });

        //This only works for ItemModel because of the check
        //We can expand this to collections for which we have a field called as value;
        get(new Route("/search/:entity/:searchTerm") {
            @Override
            public Object handle(Request request, Response response) {
                String entity = request.params(":entity");
                String term = request.params(":searchTerm");
                if (StringUtils.isBlank(entity) || StringUtils.isBlank(term) || !entity.equals(Constants.ENTITY_ITEM)) {
                    return  "[]";
                }

                Map<String,Object> root = new HashMap<String, Object>();

                if(ControllerUtilities.verifyEntityInputs(entity,root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    return gson.toJson(mongoDAO.searchValueFieldWithLimit(term, 30));
                } else {
                    logger.error("Some issue in processing this request, most like entity input is bad, input:{}",entity);
                    return "[]";

                }
            }
        });

        get(new FreemarkerBasedRoute("/searchEntity/:entity", "search_entity.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if(username==null || !username.equals(Constants.ADMIN_USER)) {
                    halt(401,"This request requires privilege");
                }
                */
                String entity = request.params(":entity");
                Map<String, Object> root = new HashMap<String, Object>();

                if (ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    String entityName = factoryDAO.getDAO(entity).getEntity();
                    //root.put("ENTITY_NAME", entityName);
                    root.put("ENTITY_NAME", entityName);
                    root.put("entity", entity);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Input Entity {} is not in the system",entity);
                    root.put(Constants.ERROR,"Input Entity:"+entity+", is not supported");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        post(new FreemarkerBasedRoute("/searchEntity", "list.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                String entity = StringEscapeUtils.escapeHtml4(request.queryParams("entity"));
                String entityValue = StringEscapeUtils.escapeHtml4(request.queryParams("entityValue"));

                int limit = Constants.SEARCH_RESULTS_SIZE;
                root.put("entity", entity);
                root.put("entityValue", entityValue);
                root.put("limit",limit);
                if (StringUtils.isNotBlank(entityValue) && ControllerUtilities.verifyEntityInputs(entity, root)) {
                    //MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity);
                    //String entityName = factoryDAO.getEntityString(entity);
                    //MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    String entityName = factoryDAO.getDAO(entity).getEntity();
                    root.put("ENTITY_NAME", entityName);
                    List<InventoryObj> list = mongoDAO.search(entityValue,limit);

                    root.put("success", "true");
                    root.put("entityList", list);
                    //root.put("ENTITY_NAME", en);
                    root.put("entity", entity);
                    root.put("limit",limit);
                    root.put("search", true);
                    root.put("searchString", entityValue);
                    Gson gson = new Gson();
                    if(entity.equals("customer")) {

                        CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
                        List<Document> transactionList = customerTransactionDAO.getSummaryByEntity(entity);

                        CustomerPaymentDAO customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();
                        List<Document> paymentList = customerPaymentDAO.getSummaryByEntity(entity);

                        Map<String, Tuple<Document>> joinMap = ControllerUtilities.joinByIdKey(transactionList, paymentList);

                        List<Document> entityRecs = ControllerUtilities.convertJoinedMapDocumentList(joinMap);

                        Map<String, Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(list, entityRecs);
                        root.put("joinMap", joinMapEntityRecs);
                    }

                    if(entity.equals("salesman")) {
                        //Gson gson = new Gson();

                        CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
                        List<Document> transactionList = customerTransactionDAO.getSummaryByEntity(entity);
                        logger.info(gson.toJson(transactionList));

                        logger.info(gson.toJson(list));
                        Map<String, Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(list, transactionList);
                        root.put("joinMap", joinMapEntityRecs);
                    }
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });


        get(new Route("/searchKeys/:entity/:searchTerm") {
            @Override
            public Object handle(Request request, Response response) {
                String entity = request.params(":entity");
                String term = request.params(":searchTerm");
                if (StringUtils.isBlank(entity) || StringUtils.isBlank(term)) {
                    return  "[]";
                }

                Map<String,Object> root = new HashMap<String, Object>();

                if(ControllerUtilities.verifyEntityInputs(entity,root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    Gson gson = new Gson();
                    return gson.toJson(mongoDAO.searchKeysWithLimit(term, 30));
                } else {
                    logger.error("Some issue in processing this request, most like entity input is bad, input:{}",entity);
                    return "[]";

                }
            }
        });



        get(new FreemarkerBasedRoute("/remove/:entity", "remove.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if(username==null || !username.equals(Constants.ADMIN_USER)) {
                    halt(401,"This request requires privilege");
                }
                */
                String entity = request.params(":entity");
                Map<String,Object> root = new HashMap<String, Object>();

                if(ControllerUtilities.verifyEntityInputs(entity,root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();

                    String entityName = factoryDAO.getDAO(entity).getEntity();
                    root.put("ENTITY_NAME", entityName);
                    root.put("entity", entity);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        post(new FreemarkerBasedRoute("/remove", "remove.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                String entity = StringEscapeUtils.escapeHtml4(request.queryParams("entity"));
                String entityValue = StringEscapeUtils.escapeHtml4(request.queryParams("entityValue"));

                root.put("entity", entity);
                root.put("entityValue", entityValue);
                if (StringUtils.isNotBlank(entityValue) && ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    String entityName = factoryDAO.getDAO(entity).getEntity();
                    root.put("ENTITY_NAME", entityName);
                    mongoDAO.remove(entityValue);
                    root.put(Constants.SUCCESS, "true");
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });


        /**
         * Supports only <1000(Constants.MAX_ROWS) records recent
         */
        get(new FreemarkerBasedRoute("/list/:entity/:limit", "list.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                String limit = request.params(":limit");
                Map<String, Object> root = new HashMap<String, Object>();
                if(StringUtils.isBlank(entity) || StringUtils.isBlank(limit)) {
                    logger.error("Empty entity:{} or limit field:{}", entity, limit);
                    root.put(Constants.ERROR, String.format("Empty entity:%s or limit field:%s", entity, limit));
                    root.put("entityList", new ArrayList());
                    root.put("ENTITY_NAME",entity);

                    root.put("entity",entity);
                    templateOverride.process(root, writer);
                    return;
                }

                int limitInt = 30;
                try {
                    limitInt = Integer.parseInt(limit);
                } catch (Exception e) {
                    logger.error("Limit is expected to be an integer, input:{}", limit);
                    root.put(Constants.ERROR,String.format("Limit is expected to be an integer, input:%s",limit));
                    root.put("entityList", new ArrayList());
                    root.put("ENTITY_NAME",entity);
                    root.put("entity", entity);

                    templateOverride.process(root, writer);
                    return;

                }
                if(limitInt<0) {
                    limitInt = 30;
                }
                if(limitInt>Constants.MAX_ROWS) {
                    limitInt = Constants.MAX_ROWS;
                }

                if (ControllerUtilities.verifyEntityInputs(entity, root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    String entityName = factoryDAO.getDAO(entity).getEntity();
                    List<InventoryObj> list = null;
                    if(entityName.equals("itemBuy") || entity.equals("itemSell")) {
                        Document doc = Document.parse("{itemTransactionType:\"SELL\"}");
                        if(entityName.equals("itemBuy")) {
                            doc = Document.parse("{itemTransactionType:\"BUY\"}");
                        }
                        list = mongoDAO.list(doc,limitInt);
                    } else {
                        list = mongoDAO.list(limitInt);
                    }
                    root.put("entityList", list);
                    root.put("ENTITY_NAME", entityName);
                    root.put("entity", entity);

                    Gson gson = new Gson();
                    logger.info(gson.toJson(list));
                    logger.info(entity);
                    if(entity.equals("customer")) {

                        CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
                        List<Document> transactionList = customerTransactionDAO.getSummaryByEntity(entity);

                        CustomerPaymentDAO customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();
                        List<Document> paymentList = customerPaymentDAO.getSummaryByEntity(entity);

                        Map<String, Tuple<Document>> joinMap = ControllerUtilities.joinByIdKey(transactionList, paymentList);

                        List<Document> entityRecs = ControllerUtilities.convertJoinedMapDocumentList(joinMap);

                        Map<String, Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(list, entityRecs);
                        root.put("joinMap", joinMapEntityRecs);
                    }

                    if(entity.equals("salesman")) {
                        //Gson gson = new Gson();

                        CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
                        List<Document> transactionList = customerTransactionDAO.getSummaryByEntity(entity);
                        logger.info(gson.toJson(transactionList));

                        logger.info(gson.toJson(list));
                        Map<String, Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(list, transactionList);
                        root.put("joinMap", joinMapEntityRecs);
                    }

                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request");
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        get(new FreemarkerBasedRoute("/fullList/:entity", "list.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String entity = request.params(":entity");
                Map<String,Object> root = new HashMap<String, Object>();

                if(ControllerUtilities.verifyEntityInputs(entity,root)) {
                    MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    String entityName = factoryDAO.getDAO(entity).getEntity();
                    List<InventoryObj> list = mongoDAO.list();
                    root.put("entityList", list);
                    root.put("ENTITY_NAME", entityName);
                    root.put("entity", entity);

                    if(entity.equals("customer")) {

                        CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
                        List<Document> transactionList = customerTransactionDAO.getSummaryByEntity(entity);

                        CustomerPaymentDAO customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();
                        List<Document> paymentList = customerPaymentDAO.getSummaryByEntity(entity);

                        Map<String, Tuple<Document>> joinMap = ControllerUtilities.joinByIdKey(transactionList, paymentList);

                        List<Document> entityRecs = ControllerUtilities.convertJoinedMapDocumentList(joinMap);

                        Map<String, Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(list, entityRecs);
                        root.put("joinMap", joinMapEntityRecs);
                    }

                    if(entity.equals("salesman")) {

                        CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
                        List<Document> transactionList = customerTransactionDAO.getSummaryByEntity(entity);

                        Map<String, Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(list, transactionList);
                        root.put("joinMap", joinMapEntityRecs);
                    }
                    //logger.info(entity);
                    templateOverride.process(root, writer);
                    return;
                } else {
                    logger.error("Some issue in processing this request");
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        post(new FreemarkerBasedRoute("/search") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                Map<String, Object> root = new HashMap<String, Object>();

                int limit = Constants.SEARCH_RESULTS_SIZE;

                //this.template = cfg.getTemplate("list_default.ftl");
                this.templateOverride.setTemplateName("list_default.ftl");

                String entity = StringEscapeUtils.escapeHtml4(request.queryParams("entity"));
                String query = StringEscapeUtils.escapeHtml4(request.queryParams("query"));
                root.put("entity", entity);
                root.put("limit",limit);
                root.put("query", query);

                root.put("ENTITY_NAME", entity);
                if (StringUtils.isNotBlank(entity) && StringUtils.isNotBlank(query)) {
                    MongoCollectionDAO mongoCollectionDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
                    if (mongoCollectionDAO == null) {
                        root.put("entityList", new ArrayList());
                        root.put(Constants.ERROR, "Bad Entity input, entity:" + entity);
                        templateOverride.process(root, writer);
                        return;
                    } else {

                        String templateName = "list_" + entity + ".ftl";

                        //this.template = cfg.getTemplate(templateName);
                        this.templateOverride.setTemplateName(templateName);
                        List<InventoryObj> list = mongoCollectionDAO.search(query,limit);
                        try {
                            root.put("entityList", list);

                            if(entity.equals("customer")) {

                                CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
                                List<Document> transactionList = customerTransactionDAO.getSummaryByEntity(entity);

                                CustomerPaymentDAO customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();
                                List<Document> paymentList = customerPaymentDAO.getSummaryByEntity(entity);

                                Map<String, Tuple<Document>> joinMap = ControllerUtilities.joinByIdKey(transactionList, paymentList);

                                List<Document> entityRecs = ControllerUtilities.convertJoinedMapDocumentList(joinMap);

                                Map<String, Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(list, entityRecs);
                                root.put("joinMap", joinMapEntityRecs);
                            }

                            if(entity.equals("salesman")) {
                                Gson gson = new Gson();

                                CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
                                List<Document> transactionList = customerTransactionDAO.getSummaryByEntity(entity);
                                logger.info(gson.toJson(transactionList));

                                logger.info(gson.toJson(list));
                                Map<String, Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(list, transactionList);
                                root.put("joinMap", joinMapEntityRecs);
                            }

                            templateOverride.process(root, writer);
                            return;
                        } catch (Exception e) {
                            logger.error("Bad Template processing for entity:{} ", entity, e);
                            root.put("entityList", new ArrayList());
                            root.put(Constants.ERROR, "Bad Template processing for entity:" + entity);

                            templateOverride.process(root, writer);
                            return;
                        }

                    }
                } else {
                    logger.error("Bad request, One of the inputs is missing, entity:{} or query:{}", entity, query);
                    root.put("entityList", new ArrayList());
                    root.put(Constants.ERROR, "Bad request, One of the inputs is missing, entity:" + entity + "or query:" + query);
                    templateOverride.process(root, writer);
                }


            }
        });

        post(new FreemarkerBasedRoute("/multiSearch", "multi_search.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                Map<String, Object> root = new HashMap<String, Object>();
                String query = StringEscapeUtils.escapeHtml4(request.queryParams("query"));
                int limit = Constants.SEARCH_RESULTS_SIZE;
                root.put("query", query);
                root.put("limit",limit);
                logger.info("Multi Search called with:" + query);

                try {
                    if (StringUtils.isNotBlank(query)) {
                        root = ControllerUtilities.multiSearch(query,limit);
                        //root.put(Constants.ERROR, "function not enabled");
                        root.put("query", query);
                        root.put("limit",limit);
                        templateOverride.process(root, writer);
                        return;
                    } else {
                        logger.error("Bad request, Search query is missing,  query:{}", query);
                        root.put("query", query);
                        root.put("entityList", new ArrayList());
                        templateOverride.process(root, writer);
                    }
                 }catch(Exception e) {
                    logger.error("Most likely an text index is missing for a collection", e);
                    root.put("entityList", new ArrayList());
                    templateOverride.process(root, writer);
                }

            }
        });




    }


    private void initializeStandardLoginSignUpRoutes() throws IOException {

        //New

        get(new RelaxedFreemarkerBasedRoute("/login", "login.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                //SimpleHash root = new SimpleHash();
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));

                if (username != null) {
                    response.redirect("/");
                } else {
                    HashMap<String, Object> root = new HashMap<String, Object>();
                    String permalink = request.queryParams("permalink");
                    root.put("username", "");
                    root.put("permalink", permalink);
                    root.put("login", "true");

                    templateOverride.process(root, writer);
                }

            }
        });

        // process output coming from login form. On success redirect folks to the welcome page
        // on failure, just return an error and let them try again.
        post(new RelaxedFreemarkerBasedRoute("/login", "login.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String username = request.queryParams("email");
                String password = request.queryParams("password");
                logger.info("Login: User submitted: " + username);

                Document user = userDAO.validateLogin(username, password);

                if (user != null) {
                    // valid user, let's log them in
                    String sessionID = sessionDAO.startSession(user.get("_id").toString());

                    if (sessionID == null) {
                        response.redirect("/internal_error");
                    } else {
                        // set the cookie for the user's browser
                        String permalink = request.queryParams("permalink");
                        ControllerUtilities.deleteAllCookies(request, response);
                        response.raw().addCookie(new Cookie(ControllerUtilities.getCookieKey(request, "session"), sessionID));
                        response.raw().addCookie(new Cookie(ControllerUtilities.getCookieKey(request,"lang"), "hi_IN"));


                        if (permalink != null && StringUtils.isNotBlank(permalink)) {
                            response.redirect("/post/" + permalink);
                        } else {
                            response.redirect("/");
                        }
                    }
                } else {
                    HashMap<String, Object> root = new HashMap<String, Object>();
                    root.put("username", StringEscapeUtils.escapeHtml4(username));
                    root.put("password", "");
                    root.put("login_error", "Invalid Login");
                    root.put("login", "true");
                    templateOverride.process(root, writer);
                }
            }
        });


// present signup form for blog

        get(new RelaxedFreemarkerBasedRoute("/lang/:lang","lang.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                String lang = request.params(":lang");
                HashMap<String, Object> root = new HashMap<String, Object>();

                root.put("lang", lang);

                if(StringUtils.isNotBlank(lang)
                        && (lang.equals("en") || lang.equals("hi"))) {

                    if(lang.equals("hi")) {
                        logger.info("Hindi detected");
                        if(ControllerUtilities.getLangCookieActual(request)==null){
                            Cookie cookie = new Cookie(ControllerUtilities.getCookieKey(request,"lang"), "hi_IN");
                            cookie.setPath("/");
                            cookie.setValue("hi_IN");
                            cookie.setMaxAge(-1);
                            response.raw().addCookie(cookie);
                            //Only if langCookie is set

                        } else {
                            logger.info("Cookie already added, so doing nothing");
                        }
                        /*
                        this.setCfg(SingletonConfiguration.getInstance().getLocaleConfiguration());
                        FreemarkerBasedRoute.cfg = SingletonConfiguration.getInstance().getLocaleConfiguration();
                        RelaxedFreemarkerBasedRoute.cfg = SingletonConfiguration.getInstance().getLocaleConfiguration();
                        try {
                            //This pulls the needed template based on lang and then processes it
                            Template template = this.getCfg().getTemplate(this.getTemplateName());
                            templateOverride.setTemplate(template);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        */
                        root.put("success", "yes");
                        root.put("langDisplay", "Hindi");
                        logger.info("Added lang cookie");
                        response.redirect("/lang_set/hi");
                    } else {
                        ControllerUtilities.deleteCookie(request,response,"lang");
                        root.put("success", "yes");
                        root.put("langDisplay", "English");
                        logger.info("Deleted lang cookie");

                        /*
                        logger.info("Back to english");
                        this.setCfg(SingletonConfiguration.getInstance().getConfiguration());
                        FreemarkerBasedRoute.cfg = SingletonConfiguration.getInstance().getConfiguration();
                        RelaxedFreemarkerBasedRoute.cfg = SingletonConfiguration.getInstance().getConfiguration();
                        try {
                            //This pulls the needed template based on lang and then processes it
                            Template template = this.getCfg().getTemplate(this.getTemplateName());
                            templateOverride.setTemplate(template);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        */

                        response.redirect("/lang_set/en");
                    }
                } else {
                    logger.info("Language {} not supported",lang);
                    root.put("errors","lang:"+lang + " is not supported");
                }

                templateOverride.process(root, writer);
            }
        });

        get(new RelaxedFreemarkerBasedRoute("/lang_set/:lang", "lang.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                String lang = request.params(":lang");
                HashMap<String, Object> root = new HashMap<String, Object>();

                root.put("lang", lang);

                if(StringUtils.isNotBlank(lang)
                        && (lang.equals("en") || lang.equals("hi"))) {

                    if(lang.equals("hi")) {
                        root.put("success", "yes");
                        root.put("langDisplay", "Hindi");
                        logger.info("Added lang cookie");
                        //response.redirect("/lang/hi");
                    } else {
                        ControllerUtilities.deleteCookie(request, response, "lang");

                        root.put("success", "yes");
                        root.put("langDisplay", "English");
                        //response.redirect("/lang/en?yes=1");
                    }
                } else {
                    logger.info("Language {} not supported", lang);
                    root.put("errors", "lang:" + lang + " is not supported");
                }

                templateOverride.process(root, writer);
            }
        });


        // present signup form for blog
        get(new RelaxedFreemarkerBasedRoute("/signup", "login.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                String permalink = request.queryParams("permalink");
                logger.info("Signup request from user,{}", permalink);
                HashMap<String, Object> root = new HashMap<String, Object>();

                // initialize values for the form.
                root.put("username", "");
                root.put("password", "");
                root.put("email", "");
                root.put("permalink", permalink);
                root.put("signup", "true");

                templateOverride.process(root, writer);
            }
        });


        post(new RelaxedFreemarkerBasedRoute("/signup", "login.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String email = request.queryParams("email");
                String username = email;
                String password = request.queryParams("password");
                String verify = request.queryParams("password_confirm");
                logger.info("handler for Signup request from user,{}" + username);

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("username", StringEscapeUtils.escapeHtml4(username));
                root.put("email", StringEscapeUtils.escapeHtml4(email));

                if (ControllerUtilities.validateSignupV2(username, password, verify, email, root)) {
                    // good user
                    logger.info("Signup: Creating user with: " + username + " " + password);
                    if (!userDAO.addUser(username, password, email)) {
                        // duplicate user
                        logger.error("Username already in use, Please choose another, input:{}", username);
                        root.put("username_error", "Username already in use!!, Please choose another");
                        templateOverride.process(root, writer);
                    } else {
                        // good user, let's start a session
                        String sessionID = sessionDAO.startSession(username);
                        logger.info("Session ID is" + sessionID);
                        String permalink = request.queryParams("permalink");
                        response.raw().addCookie(new Cookie(ControllerUtilities.getCookieKey(request, "session"), sessionID));
                        if (permalink != null && StringUtils.isNotBlank(permalink)) {
                            /*
                            String cookie = ControllerUtilities.getSessionCookie(request);
                            String username = sessionDAO.findUserNameBySessionId(cookie);
                            */
                            logger.info("permalink:{} present so redirecting", permalink);
                            response.redirect("/post/" + permalink);
                        } else {
                            response.redirect("/");
                        }
                    }
                } else {
                    // bad signup
                    //root.put("username_error", "Validation issues, Please try again respecting each field type or the passwords are not identical");
                    logger.error("User Registration did not validate, user:{}", username);
                    root.put("signup_error", "Validation Issue");
                    root.put("signup", "true");
                    templateOverride.process(root, writer);
                }
            }
        });

        // present signup form for blog
        get(new RelaxedFreemarkerBasedRoute("/reset", "reset.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {


                logger.info("Reset request");
                if (ControllerUtilities.getSessionCookie(request) == null) {
                    //String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                    response.raw().addCookie(new Cookie(ControllerUtilities.getCookieKey(request,"session"), sessionDAO.startVanillaSession()));
                }
                HashMap<String, Object> root = new HashMap<String, Object>();
                // initialize values for the form.
                templateOverride.process(root, writer);
            }
        });

// present signup form for blog
        post(new RelaxedFreemarkerBasedRoute("/reset", "reset_success.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                if (ControllerUtilities.getSessionCookie(request) == null) {
                    //String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                    response.raw().addCookie(new Cookie(ControllerUtilities.getCookieKey(request, "session"), sessionDAO.startVanillaSession()));
                }
                HashMap<String, Object> root = new HashMap<String, Object>();
                String email = request.queryParams("email");
                root.put("email", email);
                logger.info("Reset request received for email:" + email);
                if (StringUtils.isNotBlank(email) && userDAO.exists(email) && userDAO.updatePassword(email)) {
                    root.put("success", "yes");
                    root.put("defaultPassword", Constants.DEFAULT_PASS);
                    //TODO email admin
                } else {
                    root.put("error", "User does not exist, input:" + email);
                    logger.error("Seems like some error in resetting for user:" + email);
                }
                // initialize values for the form.
                templateOverride.process(root, writer);
            }
        });




        // allows the user to logout of the blog
        get(new RelaxedFreemarkerBasedRoute("/logout", "login.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String sessionID = ControllerUtilities.getSessionCookie(request);
                HashMap<String, String> root = new HashMap<String, String>();
                root.put("logout", "true");
                root.put("login", "true");
                if (sessionID != null) {
                    // deletes from session table
                    sessionDAO.endSession(sessionID);
                    ControllerUtilities.deleteAllCookies(request, response);
                    //response.redirect("/login");
                }
                templateOverride.process(root, writer);
            }
        });


    }


    private void initializeSearchRoutes() throws IOException {

        // used to display actual blog post detail page
        post(new FreemarkerBasedRoute("/search", "search.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String phrase = StringEscapeUtils.escapeHtml4(request.queryParams("search"));
                //String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));

                System.out.println("/search: get " + phrase);

                if (phrase.equals("")) {
                    // redisplay page with errors
                    HashMap<String, String> root = new HashMap<String, String>();
                    root.put(Constants.ERROR, "Search is Empty, Go ahead and try the new improved Search");
                    root.put("search", phrase);
                    templateOverride.process(root, writer);
                } else {
                    //Here you need to call Elastic Search
                    response.redirect("/search/" + phrase);
                }
            }
        });

        get(new FreemarkerBasedRoute("/search", "search.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                HashMap<String, Object> root = new HashMap<String, Object>();
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                root.put("username", username);
                templateOverride.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/search/", "search.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                HashMap<String, Object> root = new HashMap<String, Object>();
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                root.put("username", username);
                templateOverride.process(root, writer);
            }
        });
    }

    private void initializeListingRoutes() throws IOException {

        get(new RelaxedFreemarkerBasedRoute("/", "/home.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                Map<String,Object> root = new HashMap<String, Object>();
                if (username != null) {
                    root.put("username", username);
                }

                templateOverride.process(root, writer);
            }
        });

        get(new RelaxedFreemarkerBasedRoute("/about", "/static/about.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                Map<String, Object> root = new HashMap<String, Object>();
                if (username != null) {
                    root.put("username", username);
                }

                templateOverride.process(root, writer);
            }
        });

        get(new RelaxedFreemarkerBasedRoute("/help", "/static/help.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                Map<String,Object> root = new HashMap<String, Object>();
                if (username != null) {
                    root.put("username", username);
                }

                templateOverride.process(root, writer);
            }
        });

        get(new RelaxedFreemarkerBasedRoute("/privacy", "/static/privacy.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                Map<String, Object> root = new HashMap<String, Object>();
                if (username != null) {
                    root.put("username", username);
                }

                templateOverride.process(root, writer);
            }
        });

        get(new RelaxedFreemarkerBasedRoute("/tos", "/static/tos.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                Map<String,Object> root = new HashMap<String, Object>();
                if (username != null) {
                    root.put("username", username);
                }

                templateOverride.process(root, writer);
            }
        });

        get(new RelaxedFreemarkerBasedRoute("/disclaimer", "/static/disclaimer.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                Map<String,Object> root = new HashMap<String, Object>();
                if (username != null) {
                    root.put("username", username);
                }

                templateOverride.process(root, writer);
            }
        });



    }

    private void initializeOthers() throws IOException {
        get(new Route("/getState") {
            @Override
            public Object handle(Request request, Response response) {
                List<Document> stateDocs =null;//= stateDAO.getAllStates();
                Gson gson = new Gson();
                return gson.toJson(stateDocs);
            }
        });

        post(new Route("/jainVote") {
            @Override
            public Object handle(Request request, Response response) {
                String permalink = request.queryParams("permalink");
                String cookie = ControllerUtilities.getSessionCookie(request);
                String username = sessionDAO.findUserNameBySessionId(cookie);
                Gson gson = new Gson();
                Message message = new Message();
                message.setContents("failure");

                if (permalink == null || username == null || StringUtils.isBlank(permalink)) {
                    logger.info("userName is {} or permalink is {}, so redirecting to signup...", username, permalink);
                    message.setRedirect("/login?permalink=" + permalink);
                    return gson.toJson(message);
                }

                logger.info("user name:" + username + ",/post: get " + permalink);
                try {
                    logger.info("Using Thread based update");
                    //  MONGO_DB_THREAD_POOL.execute(new MongoDbJainUpdateWorker(permalink, username));
                    //ELASTIC_SEARCH_THREAD_POOL.execute(new ElasticSearchWorkerDAO(permalink, username));
                    //askForHelpDAO.updateListingWithJainFoodOption(permalink, username)
                    message.setContents(Constants.SUCCESS);
                } catch (Exception e) {
                    logger.error("Exception, unable to update permalink:{} for user{}", permalink, username, e);
                    message.setRedirect("/login?permalink=" + permalink);
                }
                return gson.toJson(message);
            }
        });


    }

    private void initializeRoutes() throws IOException {


        // tells the user that the URL is dead

        get(new RelaxedFreemarkerBasedRoute("/trial_end", "/static/trial_edition.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                HashMap<String, Object> root = new HashMap<String, Object>();
                root.put("username", StringEscapeUtils.escapeHtml4(username));
                templateOverride.process(root, writer);
            }
        });

        get(new RelaxedFreemarkerBasedRoute("/post_not_found", "/static/post_not_found.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                HashMap<String, Object> root = new HashMap<String, Object>();
                root.put("username", StringEscapeUtils.escapeHtml4(username));
                templateOverride.process(root, writer);
            }
        });


        // used to process internal errors
        get(new RelaxedFreemarkerBasedRoute("/internal_error", "/static/internal_error.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                HashMap<String, Object> root = new HashMap<String, Object>();
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                root.put("username", username);
                root.put("error", "System has encountered an error.");
                templateOverride.process(root, writer);
            }
        });
    }

    /*
    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(BookKeepingController.class, "/freemarker");
        return retVal;
    }
*/

}
