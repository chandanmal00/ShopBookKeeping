package com.bookkeeping.controller;

import com.bookkeeping.DAO.*;
import com.bookkeeping.config.ShopSingleton;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.*;
import com.bookkeeping.utilities.BookKeepingException;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by chandan on 6/20/16.
 */
public class CustomerTransactionController {

    CustomerDAO customerDAO;
    CustomerTransactionDAO customerTransactionDAO;
    SalesmanDAO salesmanDAO;
    ItemSoldDAO itemSoldDAO;
    ItemInventoryDAO itemInventoryDAO;
    SessionDAO sessionDAO;
    ItemDAO itemDAO;
    CustomerPaymentDAO customerPaymentDAO;

    static final Logger logger = LoggerFactory.getLogger(CustomerTransactionController.class);
    public CustomerTransactionController() {
        customerDAO = SingletonManagerDAO.getInstance().getCustomerDAO();
        customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
        sessionDAO = SingletonManagerDAO.getInstance().getSessionDAO();
        //transactionItemDAO = SingletonManagerDAO.getInstance().getTransactionItemDAO();
        itemDAO = SingletonManagerDAO.getInstance().getItemDAO();
        itemSoldDAO = SingletonManagerDAO.getInstance().getItemSoldDAO();
        salesmanDAO = SingletonManagerDAO.getInstance().getSalesmanDAO();
        customerPaymentDAO = SingletonManagerDAO.getInstance().getCustomerPaymentDAO();
        itemInventoryDAO = SingletonManagerDAO.getInstance().getItemInventoryDAO();

    }

    public void initializeRoutes() throws IOException {




        get(new FreemarkerBasedRoute("/add/transaction", "addTransactionNew.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                Map<String,Object> root = new HashMap<String, Object>();
                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                root.put("username", username);
                */
                root.put("ENTITY_SALESMAN", Constants.ENTITY_SALESMAN);
                root.put("ENTITY_CUSTOMER", Constants.ENTITY_CUSTOMER);
                root.put("ENTITY_ITEM", Constants.ENTITY_ITEM);
                root.put("entity_customer", "customer");
                root.put("entity_salesman", "salesman");
                root.put("entity_item", "item");
                root.put("entityActual", "transaction");
                root.put("shop", ShopSingleton.shop);
                root.put("dt",ControllerUtilities.getCurrentDateStrInYYYY_MM_DD());


                templateOverride.process(root, writer);

            }
        });

        post(new FreemarkerBasedRoute("/add/transaction", "addTransactionNew.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                float delta = 0.2f;
                double deltaDouble = 0.2d;
                Map<String,Object> root = new HashMap<String, Object>();
                root.put("ENTITY_SALESMAN", Constants.ENTITY_SALESMAN);
                root.put("ENTITY_CUSTOMER", Constants.ENTITY_CUSTOMER);
                root.put("ENTITY_ITEM", Constants.ENTITY_ITEM);
                root.put("entity_customer", "customer");
                root.put("entity_salesman", "salesman");
                root.put("entity_item", "item");
                root.put("entityActual", "transaction");
                root.put("shop", ShopSingleton.shop);

                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                */

                List<ItemSell> itemSoldList = new ArrayList<ItemSell>();

                double amount = 0;
                float discoutPercent = 0;
                float discount = 0;
                float additionalCharges = 0;
                double paidAmount = 0;
                double totalAmount = 0;
                double actualTotal=0;

                int quantity=0;
                float priceFloat = 0f;
                String customerName= StringEscapeUtils.escapeHtml4(request.queryParams("customer"));
                String dt= StringEscapeUtils.escapeHtml4(request.queryParams("dt"));
                String actualTotalStr= StringEscapeUtils.escapeHtml4(request.queryParams("actualTotal"));
                String countItems= StringEscapeUtils.escapeHtml4(request.queryParams("countItems"));
                String salesmanName= StringEscapeUtils.escapeHtml4(request.queryParams("salesman"));

                root.put("dt", dt);
                root.put("customer", customerName);
                root.put("salesman", salesmanName);
                root.put("actualTotal", actualTotalStr);
                if(StringUtils.isBlank(dt) || !ControllerUtilities.verifyDateInFormat(dt)) {
                    root.put(Constants.ERROR, "Transaction date field is either empty or is not in yyyy-mm-dd format, e.g 2016-12-01 for 1st Dec, 2016, input:"+dt);
                    logger.error("Transaction date field is either empty or is not in yyyy-mm-dd format, e.g 2016-12-01 for 1st Dec, 2016, input:"+dt);
                    templateOverride.process(root, writer);
                    return;
                }

                String itemBarcode=null;
                String quantityStr=null;
                String priceStr=null;
                String amountStr=null;
                int actualCounter = 0;
                double runningTotal = 0;
                logger.info("No. of items most probably is {}",countItems);

                    try {
                        int countItemsInt = Integer.parseInt(countItems);
                        if(countItemsInt<0) {
                            throw new Exception("Negative countItems value not allowed:"+countItems);
                        }
                        int counter=0;

                        while(counter<=countItemsInt) {
                            itemBarcode = StringEscapeUtils.escapeHtml4(request.queryParams("barcode_"+counter));
                            quantityStr = StringEscapeUtils.escapeHtml4(request.queryParams("quantity_"+counter));
                            priceStr = StringEscapeUtils.escapeHtml4(request.queryParams("price_"+counter));
                            amountStr = StringEscapeUtils.escapeHtml4(request.queryParams("amount_"+counter));

                            counter++;
                            if( StringUtils.isBlank(itemBarcode)
                                    || StringUtils.isBlank(quantityStr)
                                    || StringUtils.isBlank(amountStr)
                                    || StringUtils.isBlank(priceStr) ) {
                                continue;
                                //We are ignoring this input as all required fields are missing, there was some issue here
                            } else {
                                logger.info("Additional:{} --> {},{},{},{}",counter,itemBarcode,quantityStr,priceStr,amountStr);
                                quantity = Integer.parseInt(quantityStr);
                                priceFloat = ControllerUtilities.formatDecimalValue(Float.parseFloat(priceStr));
                                ItemSell itemSell = new ItemSell(itemBarcode,quantity,priceFloat,dt);
                                logger.info(itemSell.toString());
                                amount = ControllerUtilities.formatDecimalValue(Double.parseDouble(amountStr));
                                if(quantity<=0 || priceFloat <=0 || amount <=0 || itemSell.getAmount()!=amount) {
                                    logger.error("Negative or ZERO values as well amountTotal changes not allowed for quantity:{}, price:{}, amount:{}, amountCalc:{}, barcode:{}",quantity,priceStr,amountStr,itemSell.getAmount(),itemBarcode);
                                    root.put(Constants.ERROR, "Quantity/Price/Amount field cannot be NEGATIVE or ZERO and calcAmount has to be same as amount input, barcode:"+itemBarcode+", quantity:" + quantityStr +", price:"+priceStr+", amount:"+amountStr+", calcAmount:"+itemSell.getAmount());
                                    templateOverride.process(root, writer);
                                    return;
                                }
                                itemSell.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                                itemSoldList.add(itemSell);
                                runningTotal+=itemSell.getAmount();
                                actualCounter++;
                            }
                        }

                    } catch (Exception e) {
                        root.put(Constants.ERROR, "There was some change in the countItems or one of the fields have bad inputs, please report this to sysadmin, countItem:" + countItems);
                        logger.error("Bad countItems:{} field or one of the fields have bad inputs, Most likely its quantity:{}, price:{}, amount:{} , this needs to be reported"
                                , countItems, quantityStr,priceStr,amountStr,e);
                        templateOverride.process(root, writer);
                        return;

                    }


                String discountPercentStr= StringEscapeUtils.escapeHtml4(request.queryParams("discountPercent"));
                //Optional
                String discountAmountStr= StringEscapeUtils.escapeHtml4(request.queryParams("discountAmount"));
                String additionalChargesStr= StringEscapeUtils.escapeHtml4(request.queryParams("additionalCharges"));
                String paidAmountStr= StringEscapeUtils.escapeHtml4(request.queryParams("paidAmount"));
                String totalAmountStr= StringEscapeUtils.escapeHtml4(request.queryParams("totalAmount"));
                String paymentType= StringEscapeUtils.escapeHtml4(request.queryParams("paymentType"));


                //If paidAmount is not set, make it 0 for now
                if(StringUtils.isBlank(paidAmountStr)) {
                    paidAmountStr="0";
                }

                root.put("customer", customerName);
                root.put("salesman", salesmanName);
                root.put("dt", dt);
                /*
                root.put("item", itemBarcode);
                root.put("quantity", quantityStr);
                root.put("price", priceStr);
                root.put("amount", amountStr);
                */
                root.put("discountPercent", discountPercentStr);
                root.put("discountAmount", discountAmountStr);
                root.put("additionalCharges", additionalChargesStr);
                root.put("paidAmount", paidAmountStr);
                root.put("totalAmount", totalAmountStr);
                root.put("paymentType", paymentType);


                logger.info("{},{},{},{},no. of items Added:{}",customerName,paymentType,salesmanName,paidAmountStr,actualCounter);
                if(StringUtils.isBlank(customerName)
                        || StringUtils.isBlank(paymentType)
                        || StringUtils.isBlank(salesmanName)
                        || StringUtils.isBlank(paidAmountStr)
                        || StringUtils.isBlank(totalAmountStr)

                        ) {
                    root.put(Constants.ERROR, "Mandatory fields missing, fields marked with * are compulsory, please enter values.");
                    templateOverride.process(root, writer);
                    return;
                } else {

                    logger.info("HERE, we are good until now");

                    try {
                        if(StringUtils.isNotBlank(discountPercentStr)) {
                            discoutPercent = ControllerUtilities.formatDecimalValue(Float.parseFloat(discountPercentStr));
                            if(discoutPercent<0 && discoutPercent>100) {
                                throw new BookKeepingException("Discount Percent cannot be less than 0 or greater than 100, value:"+discoutPercent);
                            }
                        }

                        if(StringUtils.isNotBlank(discountAmountStr)) {
                            discount = ControllerUtilities.formatDecimalValue(Float.parseFloat(discountAmountStr));
                            if(discount<0) {
                                throw new BookKeepingException("Discount value cannot be less than 0, value:"+discount);
                            }
                        }
                        if(StringUtils.isNotBlank(additionalChargesStr)) {
                            additionalCharges = ControllerUtilities.formatDecimalValue(Float.parseFloat(additionalChargesStr));
                            if(additionalCharges<0) {
                                throw new BookKeepingException("additionalCharges value cannot be less than 0, value:"+additionalCharges);
                            }
                        }

                        if(discount>0 && discoutPercent>0) {
                            //Check if we want to allow both discounts and if one is there what is the precedence
                        }

                        paidAmount = ControllerUtilities.formatDecimalValue(Double.parseDouble(paidAmountStr));
                        totalAmount = ControllerUtilities.formatDecimalValue(Double.parseDouble(totalAmountStr));
                        actualTotal = ControllerUtilities.formatDecimalValue(Double.parseDouble(actualTotalStr));


                    } catch(Exception e) {
                        root.put(Constants.ERROR, "Integer or float expected but found String/Other for one of the fields: paidAmount, totalAmount, additionalCharges, discoutPercent, discount");
                        logger.error("Some values were non integers/float",e);
                        templateOverride.process(root, writer);
                        return;
                    }


                    if(runningTotal!=actualTotal || Math.abs(runningTotal-actualTotal)>=deltaDouble) {
                        root.put(Constants.ERROR, String.format("RunningTotal:"+runningTotal+" does not match ActualTotal:"+actualTotal+", so not adding transaction, please check with sysadmin"));
                        logger.error("RunningTotal:{} does not match ActualTotal:{}, so not adding transaction, please check with sysadmin",runningTotal,actualTotal);
                        templateOverride.process(root, writer);
                        return;

                    }

                    double additionalDiscounts = 0;
                    if(discoutPercent>0) {
                        additionalDiscounts = discoutPercent/100*actualTotal;
                    }
                    if(discount>0) {
                        additionalDiscounts+=discount;
                    }
                    if(additionalCharges>0) {
                        additionalDiscounts-=additionalCharges;
                    }

                    if(actualTotal - additionalDiscounts!=totalAmount || Math.abs((actualTotal - additionalDiscounts)-totalAmount)>deltaDouble) {
                        logger.error("CalculatedTotal(A=B-C) and TotalAmount from the form do not match, totalAmount(A):{},actualTotal(B):{},additionalDiscounts(C):{}",actualTotal,totalAmount,additionalDiscounts);
                        root.put(Constants.ERROR, String.format("CalculatedTotal(A=B-C) and TotalAmount from the form do not match, totalAmount(A):"+totalAmount+",actualTotal(B):"+totalAmount+",additionalDiscounts(C):"+additionalDiscounts));
                        templateOverride.process(root, writer);
                        return;
                    }

                    if(actualTotal<=0 || totalAmount<=0) {
                        logger.error("Negative or ZERO value not allowed in actualTotal:{} and totalAmount:{}",actualTotal,totalAmount);
                        root.put(Constants.ERROR, String.format("Negative or ZERO value not allowed in actualTotal:"+actualTotal+" and totalAmount:"+totalAmount));
                        templateOverride.process(root, writer);
                        return;
                    }
                    Salesman salesman = salesmanDAO.getBasedOnUniqueKey(salesmanName);
                    if(salesman==null) {
                        logger.info("Creating salesman:{} as it does not exists in the system");
                        salesman = new Salesman(salesmanName);
                        salesman.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        salesmanDAO.add(salesman);
                    }



                    Customer customer = customerDAO.getBasedOnUniqueKey(customerName);
                    if(customer==null) {
                        logger.info("Customer:{} does not exists, so creating it",customer);
                        customer = new Customer(customerName);
                        customer.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        customerDAO.add(customer);
                    }



                    CustomerTransaction customerTransaction = new CustomerTransaction(
                            customer.getUniqueKey(),
                            itemSoldList,
                            totalAmount,
                            paidAmount,
                            salesman.getUniqueKey(),
                            paymentType,
                            dt
                    );


                    customerTransaction.setActualAmount(actualTotal);
                    customerTransaction.setDiscount(discount);
                    customerTransaction.setDiscountPercent(discoutPercent);
                    customerTransaction.setAdditionalCharges(additionalCharges);
                    customerTransaction.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    for(ItemSell itemSell: itemSoldList) {
                        itemSell.setTransactionId(customerTransaction.getUniqueKey());
                    }

                    //We should confirm to add here the transaction

                    //**STARTS: NEED TO DO THIS PART IN ONE TRANSACTION**//
                    CustomerPayment customerPayment = null;
                    if(paidAmount>0) {
                        //add
                        customerPayment = new CustomerPayment(customer.getUniqueKey(), dt, paidAmount);
                        customerPayment.setTag("InitialPayment");
                        customerPayment.setPaymentType(paymentType);
                        customerPayment.setTransactionId(customerTransaction.getUniqueKey());
                        customerPayment.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        customerPaymentDAO.add(customerPayment);
                    }
                    SingletonManagerDAO.getInstance().getItemSellDAO().add(itemSoldList);
                    customerTransactionDAO.add(customerTransaction);

                    CustomerTransaction customerTransaction1 = customerTransactionDAO.getBasedOnUniqueKey(customerTransaction.getUniqueKey());

                   /* if(customerTransaction1==null) {
                        //Reverting the transaction as something went wrong

                        root.put(Constants.ERROR, "Something strange happened while inserting transaction for salesman: \"<b>" + salesmanName + "</b>\", Please contact system admin");
                        logger.error("There was an issue with the transaction, should not happend, need to be checked with admin, transaction", customerTransaction);
                        templateOverride.process(root, writer);
                        return;
                    }
                    */
                    root.put("entity", "transaction");

                    if(customerTransaction1!=null) {
                        root.put("entityObject", customerTransaction1);
                        root.put("success", "true");
                        templateOverride.process(root, writer);
                    } else {
                        //delete inserted items
                        SingletonManagerDAO.getInstance().getItemSellDAO().remove(itemSoldList);
                        if(paidAmount>0 && customerPayment!=null) {
                            customerPaymentDAO.remove(customerPayment);
                        }
                        root.put(Constants.ERROR, "Some error in saving the customerTransaction, contact ADMIN, key:"+customerTransaction.getUniqueKey());
                        logger.error("Not able to find the inserted customerTransaction for customer:{}, key:{}, Investigate mongo issue"
                                , customerName, customerTransaction.getUniqueKey());
                        templateOverride.process(root, writer);
                    }
                    //**ENDS: NEED TO DO THIS PART IN ONE TRANSACTION**//
                }

            }
        });


    }
}
