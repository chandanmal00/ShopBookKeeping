package com.bookkeeping.controller;

import com.bookkeeping.DAO.CustomerDAO;
import com.bookkeeping.DAO.CustomerPaymentDAO;
import com.bookkeeping.DAO.CustomerTransactionDAO;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.*;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static spark.Spark.get;
import static spark.Spark.post;


/**
 * Created by chandan on 6/20/16.
 */
public class CustomerController extends  DefaultController{


    CustomerDAO customerDAO;
    CustomerTransactionDAO customerTransactionDAO;
    CustomerPaymentDAO customerPaymentDAO;
    static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    public CustomerController() {
        customerDAO = SingletonManagerDAO.getInstance().getCustomerDAO();
        customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();
        customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
    }

    public void initializeRoutes() throws IOException {
        // used to display Customer Detail Page


        get(new FreemarkerBasedRoute("/customerHome", "entityHome.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String,Object> root = new HashMap<String, Object>();
                root.put("ENTITY_KEY_NAME", Constants.ENTITY_KEY_NAME);
                root.put("ENTITY_NAME", Constants.ENTITY_CUSTOMER);
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if (username == null) {
                    response.redirect("/login");
                    return;
                } else {
                    root.put("username", username);
                    templateOverride.process(root, writer);
                    return;
                }
            }

        });

        get(new Route("/listCustomers") {
            @Override
            public Object handle(Request request, Response response) {
                List<Customer> customerList = customerDAO.list();
                Gson gson = new Gson();
                return gson.toJson(customerList);
            }
        });

        get(new FreemarkerBasedRoute("/update/customer/:uniqueKey", "addCustomer.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String,Object> root = new HashMap<String, Object>();
                String uniqueKey = request.params(":uniqueKey");
                if(StringUtils.isBlank(uniqueKey)) {
                    response.redirect("/post_not_found");
                }
                logger.info("Calling update /customer: get " + uniqueKey);
                Customer customer = customerDAO.getBasedOnUniqueKey(uniqueKey);

                if (customer == null) {
                    //TODO: create customer not found
                    response.redirect("/post_not_found");
                    return;
                }
                root.put("ENTITY_NAME", Constants.ENTITY_CUSTOMER);
                root.put("entity", "customer");
                root.put("firstName", customer.getFirstName());
                root.put("lastName", customer.getLastName());
                root.put("nickName", customer.getNickName());
                root.put("key", customer.getNickName());
                root.put("age",customer.getAge());
                if(customer.getNationalIdentity()!=null) {
                    root.put("aadhar", customer.getNationalIdentity().getAadhar());
                    root.put("pan", customer.getNationalIdentity().getPan());
                }

                if(customer.getLoyalty()!=null) {
                    root.put("loyaltyType", customer.getLoyalty().getNumber());
                    root.put("loyaltyNumber", customer.getLoyalty().getType());
                }

                if(customer.getLocation()!=null) {
                    root.put("place", customer.getLocation().getPlace());
                    root.put("taluka", customer.getLocation().getTaluka());
                    root.put("district", customer.getLocation().getDistrict());
                    root.put("state", customer.getLocation().getState());
                    root.put("address", customer.getLocation().getAddress());
                }
                root.put("operation","Update");

                templateOverride.process(root, writer);

            }

        });


        get(new FreemarkerBasedRoute("/add/customer", "addCustomer.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_CUSTOMER);
                root.put("entity", "customer");
                root.put("operation","Add");
                templateOverride.process(root, writer);

            }

        });

        post(new FreemarkerBasedRoute("/add/customer", "addCustomer.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_CUSTOMER);
                root.put("entity", "customer");
                String firstName = StringEscapeUtils.escapeHtml4(request.queryParams("firstName"));
                String lastName = StringEscapeUtils.escapeHtml4(request.queryParams("lastName"));
                String nickName = StringEscapeUtils.escapeHtml4(request.queryParams("nickName"));
                String key = StringEscapeUtils.escapeHtml4(request.queryParams("key"));
                String ageString = StringEscapeUtils.escapeHtml4(request.queryParams("age"));
                String aadhar = StringEscapeUtils.escapeHtml4(request.queryParams("aadhar"));
                String pan = StringEscapeUtils.escapeHtml4(request.queryParams("pan"));
                String place = StringEscapeUtils.escapeHtml4(request.queryParams("place"));
                String taluka = StringEscapeUtils.escapeHtml4(request.queryParams("taluka"));
                String district = StringEscapeUtils.escapeHtml4(request.queryParams("district"));
                String state = StringEscapeUtils.escapeHtml4(request.queryParams("state"));
                String address = StringEscapeUtils.escapeHtml4(request.queryParams("address"));
                String loyaltyType = StringEscapeUtils.escapeHtml4(request.queryParams("loyaltyType"));
                String loyaltyNumber = StringEscapeUtils.escapeHtml4(request.queryParams("loyaltyNumber"));

                String operation = StringEscapeUtils.escapeHtml4(request.queryParams("operation"));
                root.put("operation", operation);
                if (StringUtils.isNotBlank(operation)
                        && operation.equals("Update")) {
                    nickName = key;
                    if (StringUtils.isBlank(key)) {
                        root.put("errors", "Key is missing, please do not try to change fields names using some browser functionaliy");
                        templateOverride.process(root, writer);
                        return;
                    } else {
                        Customer customer = customerDAO.getBasedOnUniqueKey(key);
                        if (customer == null) {
                            root.put("errors", "NO such customer with key:" + key + ", exists, so not updating");
                            templateOverride.process(root, writer);
                            return;
                        }
                    }
                }


                root.put("firstName", firstName);
                root.put("lastName", lastName);
                root.put("nickName", nickName);
                root.put("key", key);
                root.put("age", ageString);
                root.put("aadhar", aadhar);
                root.put("loyaltyType", loyaltyType);
                root.put("loyaltyNumber", loyaltyNumber);
                root.put("pan", pan);
                root.put("place", place);
                root.put("taluka", taluka);
                root.put("district", district);
                root.put("state", state);
                root.put("address", address);
                root.put("operation", operation);
                /*
                Set<String> set = request.queryParams();
                for(String s: set) {
                    logger.info(s + "::"+request.queryParams(s));
                }
                */


                if (StringUtils.isBlank(nickName)
                        || StringUtils.isBlank(firstName)
                        || StringUtils.isBlank(lastName)
                        || StringUtils.isBlank(place)) {
                    root.put("errors", "NickName, firstName, last Name, place are mandatory fields,please enter values.");
                    templateOverride.process(root, writer);
                    return;
                } else {
                    // extract tags
                    int age = -1;
                    try {
                        if (StringUtils.isNotBlank(ageString)) {
                            age = Integer.parseInt(ageString);
                        }
                    } catch (Exception e) {
                        root.put("errors", "Integer or float expected but found String/Other for one of the fields: ageString");
                        logger.error("Some values were non integers/float", e);
                        templateOverride.process(root, writer);
                        return;
                    }

                    //TODO: handle inputs
                    logger.info("yay, welcoming Customer:" + nickName + " to the system");

                    Customer customer = new Customer(nickName);

                    customer.setAge(age);
                    customer.setFirstName(firstName);
                    customer.setLastName(lastName);
                    Loyalty loyalty = new Loyalty();
                    loyalty.setNumber(loyaltyNumber);
                    loyalty.setType(loyaltyType);
                    customer.setLoyalty(loyalty);

                    Location location = new Location(place);
                    location.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    ControllerUtilities.handleLocation(location, address, district, taluka, state);
                    customer.setLocation(location);
                    NationalIdentity nationalIdentity;
                    if (StringUtils.isNotBlank(pan)) {
                        nationalIdentity = new NationalIdentity(pan);
                        nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity.setAadhar(aadhar);
                            customer.setNationalIdentity(nationalIdentity);
                        }
                    } else {
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity = new NationalIdentity(null, aadhar);
                            nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                            customer.setNationalIdentity(nationalIdentity);
                        }
                    }

                    customer.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));

                    if (StringUtils.isBlank(operation)) {
                        operation = "Add";
                    }

                    if (operation.equals("Update")) {
                        customerDAO.remove(customer);
                    }
                    customerDAO.add(customer);

                    if (customerDAO.getBasedOnUniqueKey(customer.getUniqueKey()) == null) {
                        logger.info("Trying again for the customer");
                        customerDAO.add(customer);
                    }
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });


        get(new FreemarkerBasedRoute("/customer/:uniqueKey", "entity.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                logger.info("Coding: "+request.raw().getCharacterEncoding());
                String uniqueKey = request.params(":uniqueKey");
                if (uniqueKey != null)
                    uniqueKey = new String(uniqueKey.getBytes(),"UTF8");
                {
                    logger.info("Calling route /customer: get " + uniqueKey);
                }
                logger.info("Calling route /customer: get " + uniqueKey);
                Customer customer = customerDAO.getBasedOnUniqueKey(uniqueKey);

                if (customer == null) {
                    //TODO: create customer not found
                    response.redirect("/post_not_found");
                    return;
                } else {

                    List<CustomerTransaction> customerTransactionList = customerTransactionDAO.getBasedOnCustomerKeyWithLimit(uniqueKey, Constants.LIMIT_ROWS);
                    List<CustomerPayment> customerPaymentList = customerPaymentDAO.getBasedOnCustomerWithLimit(uniqueKey, Constants.LIMIT_ROWS);
                    // empty comment to hold new comment in form at bottom of blog entry detail page

                    //Display Transactions

                    Map<String, Object> root = new HashMap<String, Object>();
                    root.put(Constants.TRANSACTIONS, customerTransactionList);
                    root.put(Constants.PAYMENTS, customerPaymentList);
                    root.put(Constants.ENTITY, customer);
                    root.put("rows", Constants.LIMIT_ROWS);
                    root.put(Constants.ENTITY_NAME, Constants.ENTITY_CUSTOMER);

                    root.put(Constants.TOTAL_PAYMENT_AMOUNT, customerPaymentDAO.paymentSumBasedOnCustomer(uniqueKey));
                    root.put(Constants.TOTAL_TRANSACTION_AMOUNT, customerTransactionDAO.transactionSumForCustomer(uniqueKey));

                    Gson gson = new Gson();

                    //List<Document> list = mongoDAO.getDailySummaryForWeekEnding(currentDateStr);
                    List<Document> weeklyPayments = customerPaymentDAO.getEntityDailySummaryNDatesEndingToday(-7, uniqueKey, "customer");
                    List<Document> weeklyTransactions = customerTransactionDAO.getEntityDailySummaryNDatesEndingToday(-7, uniqueKey, "customer");
                    root.put("weeklyPayment", weeklyPayments);
                    root.put("weeklyTransactions", weeklyTransactions);
                    logger.info(gson.toJson(weeklyPayments));
                    logger.info(gson.toJson(weeklyTransactions));
                    Map<String, Tuple<Document>> joinMapWeekly = ControllerUtilities.joinByIdKey(weeklyTransactions, weeklyPayments);
                    root.put("joinMapWeekly", joinMapWeekly);

                    List<Document> monthlyPayments = customerPaymentDAO.getEntityMonthlySummaryEndingToday(-3, uniqueKey, "customer");
                    List<Document> monthlyTransactions = customerTransactionDAO.getEntityMonthlySummaryEndingToday(-3, uniqueKey, "customer");

                    root.put("monthlyPayment", monthlyPayments);
                    root.put("monthlyTransactions", monthlyTransactions);
                    Map<String, Tuple<Document>> joinMapMonthly = ControllerUtilities.joinByIdKey(monthlyTransactions, monthlyPayments);
                    root.put("joinMapMonthly", joinMapMonthly);

                    templateOverride.process(root, writer);
                    return;
                }
            }
        });


        get(new FreemarkerBasedRoute("/edit/customer/:uniqueKey", "edit_customer.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_CUSTOMER);
                root.put("entity", "customer");

                String uniqueKey = StringEscapeUtils.escapeHtml4(request.params(":uniqueKey"));

                if (StringUtils.isBlank(uniqueKey)) {
                    response.redirect("/not_found/customer/" + uniqueKey);
                }
                logger.info("Calling route /edit/customer: get " + uniqueKey);
                root.put("nickName", uniqueKey);
                Customer customer = customerDAO.getBasedOnUniqueKey(uniqueKey);

                if (customer == null) {
                    //TODO: create customer not found
                    response.redirect("/post_not_found/customer/" + uniqueKey);
                    return;
                } else {

                    root.put("firstName", customer.getFirstName());
                    root.put("lastName", customer.getLastName());
                    root.put("nickName", customer.getNickName());
                    if (customer.getAge() != -1) {
                        root.put("age", customer.getAge());
                    }
                    if (customer.getNationalIdentity() != null) {
                        root.put("aadhar", customer.getNationalIdentity().getAadhar());
                        root.put("pan", customer.getNationalIdentity().getPan());
                    }
                    if (customer.getLocation() != null) {
                        root.put("place", customer.getLocation().getPlace());
                        root.put("taluka", customer.getLocation().getTaluka());
                        root.put("district", customer.getLocation().getDistrict());
                        root.put("state", customer.getLocation().getState());
                        root.put("address", customer.getLocation().getAddress());
                    }
                }
                templateOverride.process(root, writer);

            }

        });

        post(new FreemarkerBasedRoute("/edit/customer", "edit_customer.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String firstName = StringEscapeUtils.escapeHtml4(request.queryParams("firstName"));
                String lastName = StringEscapeUtils.escapeHtml4(request.queryParams("lastName"));
                String nickName = StringEscapeUtils.escapeHtml4(request.queryParams("nickName"));
                String ageString = StringEscapeUtils.escapeHtml4(request.queryParams("age"));
                String aadhar = StringEscapeUtils.escapeHtml4(request.queryParams("aadhar"));
                String pan = StringEscapeUtils.escapeHtml4(request.queryParams("pan"));
                String place = StringEscapeUtils.escapeHtml4(request.queryParams("place"));
                String taluka = StringEscapeUtils.escapeHtml4(request.queryParams("taluka"));
                String district = StringEscapeUtils.escapeHtml4(request.queryParams("district"));
                String state = StringEscapeUtils.escapeHtml4(request.queryParams("state"));
                String address = StringEscapeUtils.escapeHtml4(request.queryParams("address"));

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_CUSTOMER);
                root.put("entity", "customer");

                root.put("firstName", firstName);
                root.put("lastName", lastName);
                root.put("nickName", nickName);
                root.put("age", ageString);
                root.put("aadhar", aadhar);
                root.put("pan", pan);
                root.put("place", place);
                root.put("taluka", taluka);
                root.put("district", district);
                root.put("state", state);
                root.put("address", address);
                /*
                Set<String> set = request.queryParams();
                for(String s: set) {
                    logger.info(s + "::"+request.queryParams(s));
                }
                */

                if (StringUtils.isBlank(nickName)
                        || StringUtils.isBlank(firstName)
                        || StringUtils.isBlank(lastName)
                        || StringUtils.isBlank(place)) {
                    root.put("errors", "NickName, FirstName, LastName, Place are mandatory fields!! Please enter values...");
                    templateOverride.process(root, writer);
                    return;
                } else {
                    // extract tags
                    int age = -1;
                    if (StringUtils.isNotBlank(ageString)) {
                        age = Integer.parseInt(ageString);
                    }

                    //TODO: handle inputs
                    logger.info("yay, Updating Customer:" + nickName + " to the system");

                    Customer customer = customerDAO.getBasedOnUniqueKey(nickName);

                    customer.setAge(age);
                    customer.setFirstName(firstName);
                    customer.setLastName(lastName);
                    Location location = new Location(place);
                    location.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    ControllerUtilities.handleLocation(location, address, district, taluka, state);
                    customer.setLocation(location);
                    NationalIdentity nationalIdentity;
                    if (StringUtils.isNotBlank(pan)) {
                        nationalIdentity = new NationalIdentity(pan);
                        nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity.setAadhar(aadhar);
                            customer.setNationalIdentity(nationalIdentity);
                        }
                    } else {
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity = new NationalIdentity(null, aadhar);
                            nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                            customer.setNationalIdentity(nationalIdentity);
                        }
                    }

                    customer.setUpdatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    customer.setUpdateDate(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD());
                    logger.info(customer.toString());
                    customerDAO.update(customer);
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                    return;
                }
            }

        });

    }

}
