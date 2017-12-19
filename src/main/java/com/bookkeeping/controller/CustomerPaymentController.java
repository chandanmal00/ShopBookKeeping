package com.bookkeeping.controller;

import com.bookkeeping.DAO.*;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Customer;
import com.bookkeeping.model.CustomerPayment;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
public class CustomerPaymentController {


    CustomerDAO customerDAO;
    CustomerPaymentDAO customerPaymentDAO;
    SessionDAO sessionDAO;
    static final Logger logger = LoggerFactory.getLogger(CustomerPaymentController.class);
    public CustomerPaymentController() {
        customerDAO = SingletonManagerDAO.getInstance().getCustomerDAO();
        customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();
        sessionDAO = SingletonManagerDAO.getInstance().getSessionDAO();

    }

    public void initializeRoutes() throws IOException {

        get(new Route("/listCustomerPayment/customerName/:uniqueKey") {
            @Override
            public Object handle(Request request, Response response) {
                String uniqueKey = request.params(":uniqueKey");
                List<CustomerPayment> customerPaymentList = customerPaymentDAO.getBasedOnCustomer(uniqueKey);
                Gson gson = new Gson();
                return gson.toJson(customerPaymentList);
            }
        });

        get(new Route("/listCustomerPayment/customerName//") {
            @Override
            public Object handle(Request request, Response response) {
                String[] args= request.splat();
                int size = args.length;
                if(size==2) {
                    String uniqueKey = args[0];
                    String targetDate = args[1];
                    List<CustomerPayment> customerPaymentList = customerPaymentDAO.getBasedOnCustomer(uniqueKey, targetDate);
                    Gson gson = new Gson();
                    return gson.toJson(customerPaymentList);
                }
                logger.error("Bad request:{}",request.splat());
                return "[]";

            }
        });


        get(new FreemarkerBasedRoute("/add/payment", "addCustomerPayment.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String,String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_CUSTOMER_PAYMENT);
                root.put("entity", "customer");
                root.put("entityActual", "payment");
                root.put("dt", ControllerUtilities.getCurrentDateStrInYYYY_MM_DD());

                templateOverride.process(root, writer);

            }
        });

        post(new FreemarkerBasedRoute("/add/payment", "addCustomerPayment.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String,Object> root = new HashMap<String, Object>();

                root.put("ENTITY_NAME", Constants.ENTITY_CUSTOMER_PAYMENT);
                root.put("entity", "customer");
                root.put("entityActual", "payment");

                String customerName= StringEscapeUtils.escapeHtml4(request.queryParams("customer"));
                String amount= StringEscapeUtils.escapeHtml4(request.queryParams("amount"));
                String tag= StringEscapeUtils.escapeHtml4(request.queryParams("tag"));
                String dt= StringEscapeUtils.escapeHtml4(request.queryParams("dt"));
                String paymentType= StringEscapeUtils.escapeHtml4(request.queryParams("paymentType"));

                root.put("customer", customerName);
                root.put("amount", amount);
                root.put("tag", tag);
                root.put("dt", dt);

                if(StringUtils.isBlank(customerName) || StringUtils.isBlank(amount)) {
                    root.put(Constants.ERROR, "Mandatory fields missing, fields marked with * are compulsory, please enter values.");
                    templateOverride.process(root, writer);
                    return;
                }

                if(StringUtils.isBlank(dt) || !ControllerUtilities.verifyDateInFormat(dt)) {
                    root.put(Constants.ERROR, "Date field is empty or Input Date not in yyyy-mm-dd format...,please correct");
                    templateOverride.process(root, writer);
                    return;
                }

                double amountDouble = 0d;
                try {
                    amountDouble = Double.parseDouble(amount);
                } catch (Exception e) {
                    root.put(Constants.ERROR, "Amount field has to be integer/decimal, no strings allowed");
                    logger.error("Amount field is not double {}",amount,e);
                    templateOverride.process(root, writer);
                    return;
                }
                if(amountDouble<=0) {
                    root.put(Constants.ERROR, "Negative values not allowed for amount");
                    logger.error("Amount field is negative {}",amountDouble);
                    templateOverride.process(root, writer);
                    return;
                }

                Customer customer = customerDAO.getBasedOnUniqueKey(customerName);
                if(customer==null) {
                    root.put(Constants.ERROR, "Customer:"+customerName+" does not exists, Please create him first");
                    logger.error("Customer: {} does not exist in the system, we need to create him",customerName);
                    templateOverride.process(root, writer);
                    return;
                }
                //verifyInputs()
                CustomerPayment customerPayment = new CustomerPayment(customer.getUniqueKey(),dt,amountDouble);
                if(StringUtils.isNotBlank(tag)) {
                    customerPayment.setTag(tag);
                }
                if(StringUtils.isNotBlank(paymentType)) {
                    customerPayment.setPaymentType(paymentType);
                }

                customerPayment.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                customerPaymentDAO.add(customerPayment);
                CustomerPayment customerPayment1 = customerPaymentDAO.getBasedOnUniqueKey(customerPayment.getUniqueKey());
                if(customerPayment1!=null) {
                    root.put("entityObject", customerPayment1);
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                } else {

                    root.put(Constants.ERROR, "Some error in saving the customerPayment, contact ADMIN, key:" + customerPayment.getUniqueKey());
                    logger.error("Not able to find the inserted CustomerPayment for customer:{}, key:{}, Investigate mongo issue"
                            , customerName, customerPayment.getUniqueKey());
                    templateOverride.process(root, writer);
                }
            }
        });



    }

    public static void main(String[] args) {
        CustomerPaymentDAO customerPaymentDAO = new CustomerPaymentDAOImpl();
        Gson gson = new Gson();
        System.out.println( gson.toJson(customerPaymentDAO.getDailySummaryForWeekEnding("2016-06-24")));

        CustomerTransactionDAO customerTransactionDAO = new CustomerTransactionDAOImpl();
        gson = new Gson();
        System.out.println( gson.toJson(customerTransactionDAO.getDailySummaryForWeekEnding("2016-06-24")));

        System.out.println("Quartely monthly Summary:");
        System.out.println( gson.toJson(customerTransactionDAO.getMonthlySummaryEndingToday(-3)));
    }

}
