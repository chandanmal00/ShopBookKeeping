package com.bookkeeping.controller;

import com.bookkeeping.DAO.*;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.*;
import com.bookkeeping.utilities.BookKeepingException;
import com.bookkeeping.utilities.ControllerUtilities;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.gson.Gson;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by chandan on 6/20/16.
 */
public class ItemController {

    CustomerDAO customerDAO;
    CustomerTransactionDAO customerTransactionDAO;
    SalesmanDAO salesmanDAO;
    ItemSoldDAO itemSoldDAO;
    SessionDAO sessionDAO;
    ItemDAO itemDAO;
    CompanyDAO companyDAO;
    ItemInventoryDAO itemInventoryDAO;

    static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    public ItemController() {
        customerDAO = SingletonManagerDAO.getInstance().getCustomerDAO();
        customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();
        sessionDAO = SingletonManagerDAO.getInstance().getSessionDAO();
        //transactionItemDAO = SingletonManagerDAO.getInstance().getTransactionItemDAO();
        itemDAO = SingletonManagerDAO.getInstance().getItemDAO();
        itemSoldDAO = SingletonManagerDAO.getInstance().getItemSoldDAO();
        itemInventoryDAO = SingletonManagerDAO.getInstance().getItemInventoryDAO();
        companyDAO = SingletonManagerDAO.getInstance().getCompanyDAO();

    }

    public void initializeRoutes() throws IOException {

        get(new FreemarkerBasedRoute("/inventory/status/:page", "inventory_status.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                root.put("username", username);
                */
                Map<String,Object> root = new HashMap<String, Object>();
                String page =  StringEscapeUtils.escapeHtml4(request.params(":page"));

                int pageValue = 0;
                root.put("page", pageValue);
                try {
                    if (StringUtils.isBlank(page)) {

                        throw new BookKeepingException("page value cannot be blank/null,page:" + page);
                    }
                    pageValue = Integer.parseInt(page);
                    if(pageValue<0) {
                        throw new BookKeepingException("page value cannot be negative,page:" + page);
                    }
                    root.put("page", pageValue);

                }catch (Exception e) {
                    logger.info(e.getMessage(),e);
                    root.put(Constants.ERROR, "page value cannot be blank or negative or NonInteger value,page:" + page);
                    templateOverride.process(root, writer);
                    return;
                }


                List<Document> list = SingletonManagerDAO.getInstance().getItemTransactionDAO().inventoryStatus(pageValue);
                Gson gson = new Gson();
                //logger.info(gson.toJson(list));
                root.put("inventoryList", list);

                Document doc = SingletonManagerDAO.getInstance().getItemTransactionDAO().itemTransactionSummary();
                //logger.info(gson.toJson(doc));
                root.put("itemTransactionSummary", doc);
                root.put("cntMax",Constants.MAX_ROWS);
                //root.put("markdownItemCandidates",SingletonManagerDAO.getInstance().getItemTransactionDAO().inventoryListSummaryGt(30));
                templateOverride.process(root, writer);

            }
        });

        get(new FreemarkerBasedRoute("/inventory/quickSummary", "inventory_status.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                root.put("username", username);
                */
                Map<String,Object> root = new HashMap<String, Object>();

                Document doc = SingletonManagerDAO.getInstance().getItemTransactionDAO().itemTransactionSummary();
                root.put("itemTransactionSummary", doc);
                templateOverride.process(root, writer);

            }
        });


        get(new FreemarkerBasedRoute("/inventory/markdown/:oldInDays", "inventory_status.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                Map<String,Object> root = new HashMap<String, Object>();
                String oldInDays =  StringEscapeUtils.escapeHtml4(request.params(":oldInDays"));

                int oldInDaysValue = 0;
                root.put("oldInDays", oldInDaysValue);
                root.put("limit",Constants.MAX_ROWS);
                try {
                    if (StringUtils.isBlank(oldInDays)) {

                        throw new BookKeepingException("oldInDays value cannot be blank/null,page:" + oldInDays);
                    }
                    oldInDaysValue = Integer.parseInt(oldInDays);
                    if(oldInDaysValue<0) {
                        throw new BookKeepingException("oldInDays value cannot be negative,page:" + oldInDays);
                    }
                    root.put("oldInDays", oldInDaysValue);

                }catch (Exception e) {
                    logger.info(e.getMessage(),e);
                    root.put(Constants.ERROR, "oldInDays value cannot be blank or negative or NonInteger value,page:" + oldInDays);
                    templateOverride.process(root, writer);
                    return;
                }

                root.put("dt",ControllerUtilities.formatDateInYYYY_MM_DD(ControllerUtilities.getNDaysDateFromCurrentDate(-oldInDaysValue)));

                root.put("markdownItemCandidates",SingletonManagerDAO.getInstance().getItemTransactionDAO().inventoryListSummaryGt(oldInDaysValue, Constants.MAX_ROWS));
                root.put("limit",Constants.MAX_ROWS);
                templateOverride.process(root, writer);

            }
        });

        get(new FreemarkerBasedRoute("/add/item", "addItem.ftl") {
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

                root.put("eventDate", ControllerUtilities.getCurrentDateStrInYYYY_MM_DD());
                //logger.info("uuid: {}", UUID.randomUUID().clockSequence());
                logger.info("uuid: {}", UUID.randomUUID().getLeastSignificantBits());
                root.put("barcode", UUID.randomUUID());

                templateOverride.process(root, writer);

            }
        });


        post(new FreemarkerBasedRoute("/add/item", "addItem.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                /*
                String username = sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request));
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                */
                /*
    private Customer customer;
    private List<ItemSell> itemSolds;
    private double amount;
    private float discoutPercent;
    private float discount;
    private float additionalCharges;
    private float paidAmount;
    private Salesman salesman;

                 */


                float listPriceVal = 0;
                float purchasePriceVal = 0;

                int quantity = 0;

                String barcode = StringEscapeUtils.escapeHtml4(request.queryParams("barcode"));

                String brand = StringEscapeUtils.escapeHtml4(request.queryParams("brand"));
                String productType = StringEscapeUtils.escapeHtml4(request.queryParams("productType"));
                String itemType = StringEscapeUtils.escapeHtml4(request.queryParams("itemType"));
                String listPrice = StringEscapeUtils.escapeHtml4(request.queryParams("listPrice"));
                String purchasePrice = StringEscapeUtils.escapeHtml4(request.queryParams("purchasePrice"));
                String quantityStr = StringEscapeUtils.escapeHtml4(request.queryParams("quantity"));
                String companyName = StringEscapeUtils.escapeHtml4(request.queryParams("company"));
                String eventDateStr = StringEscapeUtils.escapeHtml4(request.queryParams("eventDate"));
                //Optional

                String size = StringEscapeUtils.escapeHtml4(request.queryParams("size"));
                String group = StringEscapeUtils.escapeHtml4(request.queryParams("group"));


                //root.put("username", username);

                root.put("barcode", barcode);
                root.put("itemType", itemType);
                root.put("quantity", quantityStr);
                root.put("productType", productType);
                root.put("brand", brand);
                root.put("listPrice", listPrice);
                root.put("purchasePrice", purchasePrice);
                root.put("size", size);
                root.put("group", group);
                root.put("company", companyName);
                root.put("eventDate", eventDateStr);

                if (StringUtils.isBlank(eventDateStr) || !ControllerUtilities.verifyDateInFormat(eventDateStr)) {
                    root.put("errors", "Event date is either blank/null or not in yyyy-mm-dd format, input:" + eventDateStr);
                    templateOverride.process(root, writer);
                    return;
                }

                if (StringUtils.isBlank(barcode)
                        || StringUtils.isBlank(itemType)
                        || StringUtils.isBlank(quantityStr)
                        || StringUtils.isBlank(productType)
                        || StringUtils.isBlank(brand)
                        || StringUtils.isBlank(purchasePrice)
                        || StringUtils.isBlank(quantityStr)
                        || StringUtils.isBlank(companyName)
                        ) {
                    root.put("errors", "Mandatory fields missing, fields marked with * are compulsory, please enter values.");
                    templateOverride.process(root, writer);
                    return;
                } else {


                    try {

                        quantity = Integer.parseInt(quantityStr);
                        if (quantity < 0) {
                            logger.error("Negative quantities not allowed for quantity:{}, barcode:{}", quantity, barcode);
                            root.put("errors", "Quantity field cannot be negative barcode:" + barcode + ", quantity:" + quantityStr);
                            templateOverride.process(root, writer);
                            return;
                        }

                        if (StringUtils.isNotBlank(listPrice)) {
                            listPriceVal = Float.parseFloat(listPrice);
                        }
                        purchasePriceVal = Float.parseFloat(purchasePrice);

                    } catch (Exception e) {
                        logger.error("Some values were non integers/float for fields: quantity, listPrice, purchasePrice", e);
                        root.put("errors", "Integer or float expected but found String/Other for one of the fields: quantity, listPrice, purchasePrice");
                        templateOverride.process(root, writer);
                        return;

                    }


                    Item item = new Item(barcode, productType, brand, itemType, purchasePriceVal);
                    if (StringUtils.isNotBlank(listPrice)) {
                        item.setListPrice(listPriceVal);
                    }

                    if (StringUtils.isNotBlank(size)) {
                        item.setSize(size);
                    }

                    if (StringUtils.isNotBlank(group)) {
                        item.setGroup(group);
                    }

                    Company company = new Company(companyName);
                    companyDAO.add(company);

                    //START: TODO: needs to be done in one transaction
                    ItemInventory itemInventory = new ItemInventory(barcode, quantity, purchasePriceVal, eventDateStr, companyName);
                    itemInventory.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    itemInventoryDAO.add(itemInventory);

                    item.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    itemDAO.add(item);


                    //END transaction

                    root.put("entityActual", "item");

                    Item item1 = itemDAO.getBasedOnUniqueKey(item.getUniqueKey());
                    if (item1 != null) {
                        root.put("entityObject", item);
                        root.put("success", "true");
                        templateOverride.process(root, writer);
                    } else {
                        //delete inserted items
                        itemInventoryDAO.remove(itemInventory);
                        itemDAO.remove(item);
                        root.put("errors", "Some error in saving the Item, contact ADMIN, key:" + item.getUniqueKey());
                        logger.error("Not able to find the inserted customerTransaction for item:{}, Investigate mongo issue"
                                , item.getUniqueKey());
                        templateOverride.process(root, writer);
                    }
                    //**ENDS: NEED TO DO THIS PART IN ONE TRANSACTION**//
                }

            }
        });

        get(new FreemarkerBasedRoute("/item/:uniqueKey", "entityItem.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String uniqueKey = request.params(":uniqueKey");
                logger.info("Calling route /item: get " + uniqueKey);
                Item item = SingletonManagerDAO.getInstance().getItemDAO().getBasedOnUniqueKey(uniqueKey);

                if (item == null) {
                    //TODO: create company not found
                    response.redirect("/post_not_found");
                    return;
                } else {


                    // empty comment to hold new comment in form at bottom of blog entry detail page

                    //Display Transactions

                    Map<String, Object> root = new HashMap<String, Object>();
                    root.put(Constants.ENTITY, item);
                    root.put("rows", Constants.LIMIT_ROWS);
                    root.put(Constants.ENTITY_NAME, Constants.ENTITY_ITEM);
                    List<Document> summaryByBarcode = SingletonManagerDAO.getInstance().getItemTransactionDAO().inventoryStatus("item", uniqueKey);
                    root.put("entityList", summaryByBarcode);

                    List<ItemTransaction> transactions = SingletonManagerDAO.getInstance().getItemTransactionDAO().inventoryDetailsWithLimit("item", uniqueKey);
                    root.put("transactions", transactions);
                    //List<Document> weeklyPayments = SingletonManagerDAO.getInstance().getItemBuyDAO().getEntityDailySummaryNDatesEndingToday(-7, uniqueKey, "company");
                    //List<Document> weeklyTransactions = SingletonManagerDAO.getInstance().getItemBuyDAO().getEntityDailySummaryNDatesEndingToday(-7, uniqueKey, "company");

                    templateOverride.process(root, writer);
                    return;
                }
            }
        });


        get(new FreemarkerBasedRoute("/edit/item/:uniqueKey", "edit_item.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "item");

                String uniqueKey = StringEscapeUtils.escapeHtml4(request.params(":uniqueKey"));

                if (StringUtils.isBlank(uniqueKey)) {
                    response.redirect("/not_found/item/" + uniqueKey);
                }
                logger.info("Calling route /edit/item: get " + uniqueKey);
                root.put("barcode", uniqueKey);
                Item item = itemDAO.getBasedOnUniqueKey(uniqueKey);

                if (item == null) {
                    //TODO: create item not found
                    response.redirect("/post_not_found/item/" + uniqueKey);
                    return;
                } else {

                    root.put("barcode", item.getBarcode());
                    root.put("brand", item.getBrand());
                    root.put("productType", item.getProductType());
                    root.put("group", item.getGroup());
                    root.put("itemType", item.getItemType());
                    root.put("listPrice", item.getListPrice());
                    root.put("purchasePrice", item.getPurchasePrice());
                    root.put("size", item.getSize());
                }
                templateOverride.process(root, writer);

            }

        });

        post(new FreemarkerBasedRoute("/edit/item", "edit_item.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String barcode = StringEscapeUtils.escapeHtml4(request.queryParams("barcode"));
                String listprice = StringEscapeUtils.escapeHtml4(request.queryParams("listprice"));
                String brand = StringEscapeUtils.escapeHtml4(request.queryParams("brand"));
                String productType = StringEscapeUtils.escapeHtml4(request.queryParams("productType"));
                String itemType = StringEscapeUtils.escapeHtml4(request.queryParams("itemType"));
                String size = StringEscapeUtils.escapeHtml4(request.queryParams("size"));
                String group = StringEscapeUtils.escapeHtml4(request.queryParams("group"));
                String purchasePrice = StringEscapeUtils.escapeHtml4(request.queryParams("purchasePrice"));

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "item");

                root.put("barcode", barcode);
                root.put("brand", brand);
                root.put("productType", productType);
                root.put("group", group);
                root.put("itemType", itemType);
                root.put("listPrice", listprice);
                root.put("purchasePrice", purchasePrice);
                root.put("size", size);
                /*
                Set<String> set = request.queryParams();
                for(String s: set) {
                    logger.info(s + "::"+request.queryParams(s));
                }
                */

                if (StringUtils.isBlank(barcode)
                        || StringUtils.isBlank(itemType)
                        || StringUtils.isBlank(productType)
                        || StringUtils.isBlank(brand)) {
                    root.put("errors", "productType, barcode, itemType, brandName are mandatory fields!! Please enter values...");
                    templateOverride.process(root, writer);
                    return;
                } else {
                    // extract tags

                    //TODO: handle inputs
                    logger.info("yay, Updating Item:" + barcode + " to the system");

                    Item item = itemDAO.getBasedOnUniqueKey(barcode);

                    try {
                        if(StringUtils.isNotBlank(listprice)) {
                            item.setListPrice(Float.parseFloat(listprice.replace(",","")));
                        } else {
                            item.setListPrice(0);
                        }
                        item.setProductType(productType);
                        item.setBrand(brand);
                        item.setItemType(itemType);
                        if(StringUtils.isNotBlank(group)) {
                            item.setGroup(group);
                        }
                        if(StringUtils.isNotBlank(size)) {
                            item.setSize(size);
                        }
                        if(StringUtils.isNotBlank(purchasePrice)) {
                            //System.out.println("::"+purchasePrice);
                            item.setPurchasePrice(Float.parseFloat(purchasePrice.replace(",","")));
                        }

                        item.setUpdatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        item.setUpdateDate(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD());
                        logger.info(item.toString());
                        itemDAO.update(item);
                        root.put("success", "true");
                        logger.info("Successfully updated item:" + barcode);
                    } catch (Exception e) {
                        root.put("errors", "Fields expected as Float (purchase price and listPrice) but not as expected");
                        logger.error("Fields expected as Float (purchase price and listPrice) but not as expected, for barcode:"+barcode);
                        e.printStackTrace();
                    }
                    templateOverride.process(root, writer);
                    return;
                }
            }

        });



    }

    public static void main(String[] args) {
        System.out.println(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        System.out.println(Math.abs(UUID.randomUUID().getMostSignificantBits()));
    }
}
