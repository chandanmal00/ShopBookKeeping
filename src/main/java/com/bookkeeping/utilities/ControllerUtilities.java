package com.bookkeeping.utilities;

import com.bookkeeping.DAO.*;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.controller.Message;
import com.bookkeeping.controller.SingletonManagerDAO;
import com.bookkeeping.factory.factoryDAO;
import com.bookkeeping.model.*;
import com.bookkeeping.persistence.MongoConnection;
import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.servlet.http.Cookie;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chandan on 9/27/2015.
 */
public class ControllerUtilities {

    // helper function to get session cookie as string
    /*
    public static Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(ControllerUtilities.class, "/freemarker");
        return retVal;
    }
    */

    static final Logger logger = LoggerFactory.getLogger(ControllerUtilities.class);

    public static Map<String,Tuple<Document>> joinByIdKey(List<Document> list1, List<Document> list2) {

        Map<String,Tuple<Document>> documentMap = new TreeMap<String, Tuple<Document>>();
        for(Document doc : list1) {
            String key = doc.get("_id").toString();
            Tuple<Document> tuple = new Tuple<Document>();
            tuple.setFirst(doc);
            documentMap.put(key,tuple);
        }

        for(Document doc : list2) {

            String key = doc.get("_id").toString();
            Tuple<Document> tuple;
            if(documentMap.get(key)!=null) {
                tuple = documentMap.get(key);
                tuple.setSecond(doc);

            } else {
                tuple = new Tuple<Document>();
                tuple.setSecond(doc);
            }
            documentMap.put(key,tuple);

        }
        //Gson gson = new Gson();

        //logger.info(gson.toJson(documentMap));
        return documentMap;

    }

    /**
     * Specialized join which does a list<InventoryObj> and List<Document>
     * @param list1
     * @param list2
     * @return
     */
    public static Map<String,Tuple<Document>> joinByKey(List<? extends InventoryObj> list1, List<Document> list2) {


        Map<String,Tuple<Document>> documentMap = new TreeMap<String, Tuple<Document>>();
        for(InventoryObj doc : list1) {
            String key = doc.getUniqueKey();
            Tuple<Document> tuple = new Tuple<Document>();
            Document document = new Document();
            document.append("obj",doc);
            tuple.setFirst(document);
            documentMap.put(key, tuple);
        }

        for(Document doc : list2) {

            String key = doc.get("_id").toString();
            Tuple<Document> tuple;
            if(documentMap.get(key)!=null) {
                tuple = documentMap.get(key);
                tuple.setSecond(doc);

            } else {
                tuple = new Tuple<Document>();
                tuple.setSecond(doc);
            }
            documentMap.put(key,tuple);

        }
        //Gson gson = new Gson();

        //logger.info(gson.toJson(documentMap));
        return documentMap;

    }
    public static List<Document> convertJoinedMapDocumentList(Map<String,Tuple<Document>> joinMap) {

        List<Document> documentList = new ArrayList<Document>();
        for(String key : joinMap.keySet()) {

            Tuple tuple = joinMap.get(key);
            Document document = new Document();
            document.append("_id",key);
            //Gson gson = new Gson();
            double transAmount = 0;
            if(tuple.getFirst()!=null) {
                //logger.info("first:"+gson.toJson(tuple.getFirst()));
                Document document1 = (Document)tuple.getFirst();
                transAmount = document1.getDouble("total");
            }
            //logger.info("Tran: {}"+transAmount);

            double paymentAmount = 0;
            if(tuple.getSecond()!=null) {
                //logger.info("Sec:"+gson.toJson(tuple.getSecond()));
                Document document1 = (Document)tuple.getSecond();
                paymentAmount = document1.getDouble("total");
            }
            //logger.info("pay: {}"+paymentAmount);

            document.append("transactionAmount",transAmount);
            document.append("paymentAmount",paymentAmount);

            //logger.info(gson.toJson(document));
            document.append("balance", transAmount - paymentAmount);
            documentList.add(document);

        }
        return documentList;

    }

    public static void messageSoftwareTrial() {
        System.out.println("*************************************************************************************");
        System.out.println("*************************************************************************************");
        System.out.println("****You have reached the limit of adding: " + Constants.TRIAL_EDITION_LIMIT + " documents for trial edition");
        System.out.println("****To continue usage you need to purchase this software!!!");
        System.out.println("****Please contact the creator of the system!!!");
        System.out.println("*************************************************************************************");
        System.out.println("*************************************************************************************");
    }
    public static String replaceSpaceWithSeparator(String data) {
        data.trim();
        data.replaceAll("  ", " ");
        data.replaceAll(" ", Constants.UNIQUE_KEY_SEPARATOR);
        return data;
    }

    public static String formatUniqueKey(String uniqueKey) {
        String[] arr= uniqueKey.split(" ");
        return StringUtils.join(arr,Constants.UNIQUE_KEY_SEPARATOR);
    }

    public static String getCurrentDateStrInYYYY_MM_DD() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        String currentDateStr =  dateFormat.format(currentDate);
        return currentDateStr;
    }

    public static Date getNDaysDateFromCurrentDate(int n) {
        String currentDateStr = getCurrentDateStrInYYYY_MM_DD();
        return getNDaysDate(getDateInFormat(currentDateStr), n);
    }

    public static String formatDateInYYYY_MM_DD(Date inputDate) {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        String currentDateStr =  dateFormat.format(inputDate);
        return currentDateStr;
    }


    public static boolean verifyDateInFormat(String dateStr) {
        DateFormat dateFormatInput = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        logger.debug("Input DateStr:{}",dateStr);

        try {
            Date targetDate = dateFormatInput.parse(dateStr);
            return true;
        } catch(Exception e) {
            logger.error("Input date:{} is not in format: ",dateStr,Constants.DATE_FORMAT_YYYY_MM_DD,e);
        }
        return false;
    }

    public static Date getDateInFormat(String dateStr) {
        DateFormat dateFormatInput = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        logger.debug("Input DateStr:{}", dateStr);

        try {
            Date targetDate = dateFormatInput.parse(dateStr);
            return targetDate;
        }catch(Exception e) {
            logger.error("Input date:{} is not in format: ",dateStr,Constants.DATE_FORMAT_YYYY_MM_DD,e);
        }
        return null;
    }


    public static Map<String,String>  listIndexes() {
        Map<String,String> mapCollectionIndexes = new HashMap<String, String>();
        //mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_TRANSACTIONS_COLLECTION,
          //      "{ uniqueKey: \"text\", \"customer\": \"text\", \"salesman\": \"text\" }");
        mapCollectionIndexes.put(Constants.MONGO_ITEM_COLLECTION,
                "{ uniqueKey: \"text\", brand:\"text\", productType:\"text\", itemType:\"text\" }");

        mapCollectionIndexes.put(Constants.MONGO_ITEM_COLLECTION+"__2",
                "{ barcode: 1}");


        mapCollectionIndexes.put(Constants.MONGO_ITEM_TRANSACTION_COLLECTION,
                "{ uniqueKey: \"text\",\"barcode\": \"text\", \"company\":\"text\" }");

        mapCollectionIndexes.put(Constants.MONGO_ITEM_TRANSACTION_COLLECTION + "__2",
                "{ barcode: 1, company:1 }");

        //mapCollectionIndexes.put(Constants.MONGO_ITEM_TRANSACTION_COLLECTION + "_3", "{ eventDate:\"text\",barcode: \"text\", \"company\":\"text\" }");
        mapCollectionIndexes.put(Constants.MONGO_ITEM_TRANSACTION_COLLECTION + "__3", "{ eventDate:1,barcode:1, company:1}");

        /*
        mapCollectionIndexes.put(Constants.MONGO_ITEM_SOLD_COLLECTION,
                "{ uniqueKey: \"text\",\"barcode\": \"text\"}");
                */

        mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_PAYMENT_COLLECTION,
                "{ uniqueKey: \"text\",\"customer\": \"text\", tag:\"text\" }");

        mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_PAYMENT_COLLECTION + "__2",
                "{ customer:1, tag:1 }");

        mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_PAYMENT_COLLECTION + "__3",
                "{ eventDate:1,customer: 1, tag:1}");


        mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_TRANSACTIONS_COLLECTION,
                "{ uniqueKey: \"text\",\"customer\": \"text\", salesman:\"text\" }");

        mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_TRANSACTIONS_COLLECTION + "__2",
                "{ customer: 1, salesman:1 }");

        mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_TRANSACTIONS_COLLECTION + "__3",
                "{ eventDate:1,customer:1, salesman:1 }");

        mapCollectionIndexes.put(Constants.MONGO_SALESMAN_COLLECTION,
                "{ uniqueKey: \"text\",\"firstName\": \"text\", lastName:\"text\" }");
        mapCollectionIndexes.put(Constants.MONGO_SALESMAN_COLLECTION+"__2",
                "{ uniqueKey: 1}");

        mapCollectionIndexes.put(Constants.MONGO_COMPANY_COLLECTION,
                "{ uniqueKey: \"text\",\"firstName\": \"text\", lastName:\"text\" }");
        mapCollectionIndexes.put(Constants.MONGO_COMPANY_COLLECTION+"__2",
                "{ uniqueKey: 1" +
                        "}");

        mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_COLLECTION,
                "{ uniqueKey: \"text\",\"firstName\": \"text\", lastName:\"text\" }");

        mapCollectionIndexes.put(Constants.MONGO_CUSTOMER_COLLECTION+"__2",
                "{ uniqueKey: 1}");
        /*
        mapCollectionIndexes.put(Constants.MONGO_ITEM_SOLD_COLLECTION,
                "{ item.uniqueKey: \"text\"}");
        mapCollectionIndexes.put(Constants.MONGO_SALESMAN_COLLECTION,
                "{ uniqueKey: \"text\", \"salesman.uniqueKey\": \"text\", tag:\"text\"}");
        ;
        */
        return mapCollectionIndexes;

    }


    public static void initSetupCreateIndex() {
        MongoDatabase instance = MongoConnection.getInstance();
        Map<String, String> mapCollectionIndexes = listIndexes();

        int i=0;
        for (String collection : mapCollectionIndexes.keySet()) {
            try {

                String key = collection;
                if(collection.split("__").length>=2) {
                    key = collection.split("__")[0];
                }
                logger.info("Creating index {} with key:{} and key:{} -> {}",i,key,collection,mapCollectionIndexes.get(collection));
                instance.getCollection(key).createIndex(Document.parse(mapCollectionIndexes.get(collection)));

            } catch (Exception e) {
                logger.error("Index already exists for collection:{}, error:{}",collection, e.getMessage());
            } finally {
                i++;
            }
        }

    }

    /**
     * Positve date means ahead, negative means back
     * @param toDate
     * @param n
     * @return
     */
    public static Date getNDaysDate(Date toDate, int n) {
        logger.debug("Input Date:{} and diff: {}",toDate,n);
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDate);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    public static Date getNMonthssDate(Date toDate, int n) {
        logger.debug("Input Date:{} and diff: {}",toDate,n);
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDate);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }



    public static Map<String,Object> multiSearch(String search, int limit) {
        //String searchString = "{ $text: { $search:\""+search+"\" } }";

        CustomerPaymentDAO customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();
        List<CustomerPayment> customerPaymentList = customerPaymentDAO.search(search,limit);

        CustomerTransactionDAO customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
        List<CustomerTransaction> customerTransactionList = customerTransactionDAO.search(search,limit);

        SalesmanDAO salesmanDAO = SingletonManagerDAO.getInstance().getSalesmanDAO();
        List<Salesman> salesmanList = salesmanDAO.search(search,limit);

        CompanyDAO companyDAO = SingletonManagerDAO.getInstance().getCompanyDAO();
        List<Company> companyList = companyDAO.search(search,limit);

        ItemDAO itemDAO = SingletonManagerDAO.getInstance().getItemDAO();
        List<Item> itemList = itemDAO.search(search,limit);

        CustomerDAO customerDAO = SingletonManagerDAO.getInstance().getCustomerDAO();
        List<Customer> customerList = customerDAO.search(search,limit);

        Map<String,Object> root = new HashMap<String, Object>();

        root.put("item",itemList);
        root.put("payment",customerPaymentList);
        root.put("transaction",customerTransactionList);
        root.put("salesman",salesmanList);
        root.put("company",companyList);
        root.put("customer",customerList);

        return root;

    }

    public static String getCookieKey(final Request request, String key) {
        return request.host().split(":")[0]+"_"+request.port()+"_"+key;
    }

    public static String getSessionCookie(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals(getCookieKey(request,"session"))) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // helper function to get session cookie as string
    public static Cookie getSessionCookieActual(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals(getCookieKey(request,"session"))) {
                return cookie;
            }
        }
        return null;
    }

    public static void deleteProfileCookie(final Request request, Response response) {
        deleteCookie(request,response,"profile");
    }

    public static void deleteSessionCookie(final Request request, Response response) {
        deleteCookie(request,response,"session");
    }

    public static void deleteLangCookie(final Request request, Response response) {
        deleteCookie(request,response,"lang");
    }

    public static void deleteCookie(final Request request, Response response,String cookieStr) {
        if (request.raw().getCookies() == null) {
            return;
        }
        //response.removeCookie(cookieStr);
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals(getCookieKey(request,cookieStr))) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setValue("");
                response.raw().addCookie(cookie);
                logger.info("cleaned cookie:"+cookieStr);
            }
        }
    }

    public static void deleteAllCookies(final Request request, Response response) {
        deleteProfileCookie(request,response);
        deleteSessionCookie(request, response);

    }
    // helper function to get session cookie as string
    public static Cookie getProfileCookieActual(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals(getCookieKey(request,"profile"))) {
                return cookie;
            }
        }
        return null;
    }

    public static Cookie getLangCookieActual(final Request request) {
        //logger.info("Here to get Lang cookie");

        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals(getCookieKey(request,"lang"))) {
                //logger.info("Got lang cookie");
                if(cookie.getValue().equals("hi_IN")) {
                    return cookie;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    // tags the tags string and put it into an array
    public static ArrayList<String> extractTags(String tags) {

        // probably more efficent ways to do this.
        //
        // whitespace = re.compile('\s')

        tags = tags.replaceAll("\\s", "");
        String tagArray[] = tags.split(",");

        // let's clean it up, removing the empty string and removing dups
        ArrayList<String> cleaned = new ArrayList<String>();
        for (String tag : tagArray) {
            if (!tag.equals("") && !cleaned.contains(tag)) {
                cleaned.add(tag);
            }
        }

        return cleaned;
    }

    // validates that the registration form has been filled out right and username conforms


    public static boolean validateSignupV2(String username, String password, String verify, String email,
                                         HashMap<String, String> errors) {
        //String USER_RE = "^[a-zA-Z0-9_-]{3,20}$";
        String PASS_RE = "^.{3,20}$";
        String EMAIL_RE = "^[\\S]+@[\\S]+\\.[\\S]+$";

        //errors.put("username_error", "");
        errors.put("password_error", "");
        errors.put("verify_error", "");
        errors.put("email_error", "");

        /*
        if (!username.matches(USER_RE)) {
            errors.put("username_error", "invalid username. try just letters and numbers");
            return false;
        }
        */

        if (!password.matches(PASS_RE)) {
            errors.put("password_error", "Invalid password length!!");
            return false;
        }


        if (!password.equals(verify)) {
            errors.put("verify_error", "Password must match!!");
            return false;
        }

        if (!email.equals("")) {
            if (!email.matches(EMAIL_RE)) {
                errors.put("email_error", "Invalid Email Address!!");
                return false;
            }
        }

        return true;
    }

    public static Boolean emailVerify(String email) {
        String EMAIL_RE = "^[\\S]+@[\\S]+\\.[\\S]+$";
        if (!email.equals("")) {
            if (!email.matches(EMAIL_RE)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateSignup(String username, String password, String verify, String email,
                                           HashMap<String, String> errors) {
        String USER_RE = "^[a-zA-Z0-9_-]{3,20}$";
        String PASS_RE = "^.{3,20}$";
        String EMAIL_RE = "^[\\S]+@[\\S]+\\.[\\S]+$";

        errors.put("username_error", "");
        errors.put("password_error", "");
        errors.put("verify_error", "");
        errors.put("email_error", "");


        if (!username.matches(USER_RE)) {
            errors.put("username_error", "invalid username. try just letters and numbers");
            return false;
        }


        if (!password.matches(PASS_RE)) {
            errors.put("password_error", "invalid password.");
            return false;
        }


        if (!password.equals(verify)) {
            errors.put("verify_error", "password must match");
            return false;
        }

        if (!email.equals("")) {
            if (!email.matches(EMAIL_RE)) {
                errors.put("email_error", "Invalid Email Address");
                return false;
            }
        }

        return true;
    }


    public static Boolean verifyAdmin(String username) {

       if( username != null
               && username.equals("master@test.com")) {
            return true;
        }
        return  false;
    }

    public static boolean shouldReturnHtml(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("text/html");
    }

    public static double calculateTotal(float priceFloat, float quantityFloat, float hamaaliRateFloat,
                                        float mapariateFloat, float cashSpecialRate,
                                        float brokerageRate) {

        double totalAmount = (double)quantityFloat*priceFloat;
        totalAmount=(100-brokerageRate)*totalAmount;

        if(cashSpecialRate>0) {
            totalAmount = (100-cashSpecialRate)*totalAmount;
        }
        totalAmount-= mapariateFloat*quantityFloat;
        totalAmount-=hamaaliRateFloat*quantityFloat;
        return totalAmount;

    }

    public static void handleLocation(Location location, String address, String district, String taluka, String state) {

        if(StringUtils.isNotBlank(address)) {
            location.setAddress(address);
        }

        if(StringUtils.isNotBlank(district)) {
            location.setDistrict(district);
        }

        if(StringUtils.isNotBlank(taluka)) {
            location.setTaluka(taluka);
        }
        if(StringUtils.isNotBlank(state)) {
            location.setState(state);
        }
    }

    public static double calculateTotalKhareeddar(float priceFloat, float quantityFloat)
    {

        double totalAmount = quantityFloat*priceFloat;
        return totalAmount;

    }

    public static boolean verifyEntityInputs(String entity, Map<String,Object> root) {
        if(StringUtils.isNotBlank(entity)) {
            MongoCollectionDAO mongoDAO = factoryDAO.getDAO(entity).getMongoCollectionDAO();
            String entityName = factoryDAO.getDAO(entity).getEntity();
            if(mongoDAO!=null && entityName!=null) {
                return true;
            } else {
                logger.error("Bad request, Bad entity, input:{}",entity);
                root.put("error","bad request");
                return false;
            }

        } else {
            logger.error("Bad request, Empty entity");
            root.put("error","bad request");
            return false;

        }

    }


    public static float formatDecimalValue(float num) {
        String s= formatDecimal(num);
        return Float.valueOf(s);
    }

    public static double formatDecimalValue(double num) {
        String s= formatDecimal(num);
        return Double.valueOf(s);
    }

    public static String formatDecimal(float num) {
        DecimalFormat df=new DecimalFormat("0.00");
        return df.format(num);
    }

    public static String formatDecimal(double num) {
        DecimalFormat df=new DecimalFormat("0.00");
        return df.format(num);
    }

    public static void main(String[] args) {
        String dateStr="2016-06-10";
        System.out.println(verifyDateInFormat(dateStr));
        System.out.println(getDateInFormat(dateStr));
        System.out.println(getNDaysDate(getDateInFormat(dateStr),-7));
    }

}
