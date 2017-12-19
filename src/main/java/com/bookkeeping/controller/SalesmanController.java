package com.bookkeeping.controller;

import com.bookkeeping.DAO.CustomerTransactionDAO;
import com.bookkeeping.DAO.SalesmanDAO;
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
public class SalesmanController extends  DefaultController{


    SalesmanDAO salesmanDAO;
    CustomerTransactionDAO customerTransactionDAO;

    static final Logger logger = LoggerFactory.getLogger(SalesmanController.class);
    public SalesmanController() {
        salesmanDAO = SingletonManagerDAO.getInstance().getSalesmanDAO();
        customerTransactionDAO = SingletonManagerDAO.getInstance().getCustomerTransactionDAO();

    }

    public void initializeRoutes() throws IOException {
        // used to display Salesman Detail Page


        get(new FreemarkerBasedRoute("/salesmanHome", "entityHome.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String,Object> root = new HashMap<String, Object>();
                root.put("ENTITY_KEY_NAME", Constants.ENTITY_KEY_NAME);
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
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

        get(new Route("/listSalesmans") {
            @Override
            public Object handle(Request request, Response response) {
                List<Salesman> salesmanList = salesmanDAO.list();
                Gson gson = new Gson();
                return gson.toJson(salesmanList);
            }
        });



        get(new FreemarkerBasedRoute("/add/salesman", "addSalesman.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String,Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "salesman");
                templateOverride.process(root, writer);

            }

        });

        get(new FreemarkerBasedRoute("/update/salesman/:uniqueKey", "updateSalesman.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String,Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "salesman");
                String uniqueKey = request.params(":uniqueKey");
                logger.info("Calling route /customer: get " + uniqueKey);
                Salesman salesman = salesmanDAO.getBasedOnUniqueKey(uniqueKey);

                if (salesman == null) {
                    //TODO: create customer not found
                    response.redirect("/post_not_found");
                    return;
                } else {
                    root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                    root.put("entity", "salesman");

                    root.put("firstName", salesman.getFirstName());
                    root.put("lastName", salesman.getLastName());
                    root.put("nickName", salesman.getNickName());
                    root.put("key", salesman.getNickName());
                    root.put("age",salesman.getAge());
                    if(salesman.getNationalIdentity()!=null) {
                        root.put("aadhar", salesman.getNationalIdentity().getAadhar());
                        root.put("pan", salesman.getNationalIdentity().getPan());
                    }


                    root.put("commission",salesman.getCommission());

                    if(salesman.getLocation()!=null) {
                        root.put("place", salesman.getLocation().getPlace());
                        root.put("taluka", salesman.getLocation().getTaluka());
                        root.put("district", salesman.getLocation().getDistrict());
                        root.put("state", salesman.getLocation().getState());
                        root.put("address", salesman.getLocation().getAddress());
                    }
                    root.put("operation","Update");
                    templateOverride.process(root, writer);
                }

            }

        });

        post(new FreemarkerBasedRoute("/add/salesman", "addSalesman.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

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
                String commission = StringEscapeUtils.escapeHtml4(request.queryParams("commission"));

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "salesman");

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
                root.put("commission", commission);
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
                    int age = -1;
                    float commissionFloat = 0;
                    // extract tags
                    try {

                        if (StringUtils.isNotBlank(ageString)) {
                            age = Integer.parseInt(ageString);
                        }
                        if (StringUtils.isNotBlank(commission)) {
                            commissionFloat = Float.parseFloat(commission);
                        }
                    } catch (Exception e) {
                        root.put("errors", "Integer or float expected but found String/Other for one of the fields: ageString:" + ageString + " commissionFloat:" + commission);
                        logger.error("Some values were non integers/float", e);
                        templateOverride.process(root, writer);
                        return;

                    }

                    //TODO: handle inputs
                    logger.info("yay, welcoming Salesman:" + nickName + " to the system");

                    Salesman salesman = new Salesman(nickName);

                    salesman.setAge(age);
                    salesman.setFirstName(firstName);
                    salesman.setLastName(lastName);
                    if (StringUtils.isNotBlank(commission)) {
                        salesman.setCommission(commissionFloat);
                    }
                    Location location = new Location(place);
                    location.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    ControllerUtilities.handleLocation(location, address, district, taluka, state);
                    salesman.setLocation(location);
                    NationalIdentity nationalIdentity;
                    if (StringUtils.isNotBlank(pan)) {
                        nationalIdentity = new NationalIdentity(pan);
                        nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity.setAadhar(aadhar);
                            salesman.setNationalIdentity(nationalIdentity);
                        }
                    } else {
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity = new NationalIdentity(null, aadhar);
                            nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                            salesman.setNationalIdentity(nationalIdentity);
                        }
                    }

                    salesman.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    salesmanDAO.add(salesman);
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        post(new FreemarkerBasedRoute("/update/salesman", "updateSalesman.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

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
                String commission = StringEscapeUtils.escapeHtml4(request.queryParams("commission"));

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "salesman");

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
                root.put("commission", commission);
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
                    int age = -1;
                    float commissionFloat = 0;
                    // extract tags
                    try {

                        if (StringUtils.isNotBlank(ageString)) {
                            age = Integer.parseInt(ageString);
                        }
                        if (StringUtils.isNotBlank(commission)) {
                            commissionFloat = Float.parseFloat(commission);
                        }
                    } catch (Exception e) {
                        root.put("errors", "Integer or float expected but found String/Other for one of the fields: ageString:" + ageString + " commissionFloat:" + commission);
                        logger.error("Some values were non integers/float", e);
                        templateOverride.process(root, writer);
                        return;

                    }

                    //TODO: handle inputs
                    logger.info("yay, welcoming Salesman:" + nickName + " to the system");

                    Salesman salesman = new Salesman(nickName);

                    salesman.setAge(age);
                    salesman.setFirstName(firstName);
                    salesman.setLastName(lastName);
                    if (StringUtils.isNotBlank(commission)) {
                        salesman.setCommission(commissionFloat);
                    }
                    Location location = new Location(place);
                    location.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    ControllerUtilities.handleLocation(location, address, district, taluka, state);
                    salesman.setLocation(location);
                    NationalIdentity nationalIdentity;
                    if (StringUtils.isNotBlank(pan)) {
                        nationalIdentity = new NationalIdentity(pan);
                        nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity.setAadhar(aadhar);
                            salesman.setNationalIdentity(nationalIdentity);
                        }
                    } else {
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity = new NationalIdentity(null, aadhar);
                            nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                            salesman.setNationalIdentity(nationalIdentity);
                        }
                    }

                    salesman.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    salesmanDAO.add(salesman);
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        get(new FreemarkerBasedRoute("/salesman/:uniqueKey", "entity.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String uniqueKey = request.params(":uniqueKey");
                logger.info("Calling route /salesman: get " + uniqueKey);
                Salesman salesman = salesmanDAO.getBasedOnUniqueKey(uniqueKey);

                if (salesman == null) {
                    //TODO: create salesman not found


                    response.redirect("/post_not_found");
                    return;
                } else {

                    List<CustomerTransaction> customerTransactionList = customerTransactionDAO.getBasedOnEntityKeyWithLimit("salesman", uniqueKey, Constants.LIMIT_ROWS);

                    // empty comment to hold new comment in form at bottom of blog entry detail page

                    //Display Transactions

                    Map<String, Object> root = new HashMap<String, Object>();
                    root.put(Constants.TRANSACTIONS, customerTransactionList);
                    root.put(Constants.ENTITY, salesman);
                    root.put("rows", Constants.LIMIT_ROWS);
                    root.put(Constants.ENTITY_NAME, Constants.ENTITY_SALESMAN);
                    root.put(Constants.TOTAL_TRANSACTION_AMOUNT, customerTransactionDAO.transactionSumForEntity("salesman", uniqueKey));


                    //List<Document> list = mongoDAO.getDailySummaryForWeekEnding(currentDateStr);

                    //List<Document> weeklyPayments = customerPaymentDAO.getEntityDailySummaryNDatesEndingToday(-7, uniqueKey, "customer");
                    List<Document> weeklyTransactions = customerTransactionDAO.getEntityDailySummaryNDatesEndingToday(-7, uniqueKey, "salesman");
                    root.put("weeklyTransactions", weeklyTransactions);
                    //List<Document> monthlyPayments = customerPaymentDAO.getEntityMonthlySummaryEndingToday(-3, uniqueKey, "customer");
                    List<Document> monthlyTransactions = customerTransactionDAO.getEntityMonthlySummaryEndingToday(-3, uniqueKey, "salesman");

                    root.put("monthlyTransactions", monthlyTransactions);

                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        /*
        get(new FreemarkerBasedRoute("/salesman/:uniqueKey", "entitySalesman.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String uniqueKey = request.params(":uniqueKey");
                logger.info("Calling route /salesman: get " + uniqueKey);
                Salesman salesman = salesmanDAO.getBasedOnUniqueKey(uniqueKey);

                if (salesman == null) {
                    //TODO: create salesman not found
                    response.redirect("/post_not_found");
                    return;
                }
                else {


                    // empty comment to hold new comment in form at bottom of blog entry detail page

                    //Display Transactions

                    Map<String,Object> root = new HashMap<String, Object>();
                    root.put(Constants.ENTITY,salesman);
                    root.put("rows",Constants.LIMIT_ROWS);
                    root.put(Constants.ENTITY_NAME,Constants.ENTITY_SALESMAN);

                    templateOverride.process(root, writer);
                    return;
                }
            }
        });
        */



        get(new FreemarkerBasedRoute("/edit/salesman/:uniqueKey", "edit_salesman.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "salesman");

                String uniqueKey = StringEscapeUtils.escapeHtml4(request.params(":uniqueKey"));

                if (StringUtils.isBlank(uniqueKey)) {
                    response.redirect("/not_found/salesman/" + uniqueKey);
                }
                logger.info("Calling route /edit/salesman: get " + uniqueKey);
                root.put("nickName", uniqueKey);
                Salesman salesman = salesmanDAO.getBasedOnUniqueKey(uniqueKey);

                if (salesman == null) {
                    //TODO: create salesman not found
                    response.redirect("/post_not_found/salesman/" + uniqueKey);
                    return;
                } else {

                    root.put("firstName", salesman.getFirstName());
                    root.put("lastName", salesman.getLastName());
                    root.put("nickName", salesman.getNickName());
                    root.put("commission", salesman.getCommission());
                    if (salesman.getAge() != -1) {
                        root.put("age", salesman.getAge());
                    }
                    if (salesman.getNationalIdentity() != null) {
                        root.put("aadhar", salesman.getNationalIdentity().getAadhar());
                        root.put("pan", salesman.getNationalIdentity().getPan());
                    }
                    if (salesman.getLocation() != null) {
                        root.put("place", salesman.getLocation().getPlace());
                        root.put("taluka", salesman.getLocation().getTaluka());
                        root.put("district", salesman.getLocation().getDistrict());
                        root.put("state", salesman.getLocation().getState());
                        root.put("address", salesman.getLocation().getAddress());
                    }
                }
                templateOverride.process(root, writer);

            }

        });

        post(new FreemarkerBasedRoute("/edit/salesman", "edit_salesman.ftl") {
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
                String commission = StringEscapeUtils.escapeHtml4(request.queryParams("commission"));

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "salesman");

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
                root.put("commission", commission);
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
                    logger.info("yay, Updating Salesman:" + nickName + " to the system");

                    Salesman salesman = salesmanDAO.getBasedOnUniqueKey(nickName);

                    salesman.setAge(age);
                    salesman.setFirstName(firstName);
                    salesman.setLastName(lastName);
                    Location location = new Location(place);
                    location.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    ControllerUtilities.handleLocation(location, address, district, taluka, state);
                    salesman.setLocation(location);
                    if(StringUtils.isNotBlank(commission)) {
                        salesman.setCommission(Float.parseFloat(commission.replace(",", "")));
                    }
                    NationalIdentity nationalIdentity;
                    if (StringUtils.isNotBlank(pan)) {
                        nationalIdentity = new NationalIdentity(pan);
                        nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity.setAadhar(aadhar);
                            salesman.setNationalIdentity(nationalIdentity);
                        }
                    } else {
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity = new NationalIdentity(null, aadhar);
                            nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                            salesman.setNationalIdentity(nationalIdentity);
                        }
                    }

                    salesman.setUpdatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    salesman.setUpdateDate(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD());
                    logger.info(salesman.toString());
                    salesmanDAO.update(salesman);
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                    return;
                }
            }

        });


    }

}
