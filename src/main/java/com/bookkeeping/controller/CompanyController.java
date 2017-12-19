package com.bookkeeping.controller;

import com.bookkeeping.DAO.CompanyDAO;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Location;
import com.bookkeeping.model.NationalIdentity;
import com.bookkeeping.model.Company;
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
public class CompanyController extends  DefaultController{


    CompanyDAO companyDAO;
    static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    public CompanyController() {
        companyDAO = SingletonManagerDAO.getInstance().getCompanyDAO();
    }

    public void initializeRoutes() throws IOException {
        // used to display Company Detail Page


        get(new FreemarkerBasedRoute("/companyHome", "entityHome.ftl") {
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

        get(new Route("/listCompanies") {
            @Override
            public Object handle(Request request, Response response) {
                List<Company> companyList = companyDAO.list();
                Gson gson = new Gson();
                return gson.toJson(companyList);
            }
        });



        get(new FreemarkerBasedRoute("/add/company", "addCompany.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_SALESMAN);
                root.put("entity", "company");
                templateOverride.process(root, writer);

            }

        });

        get(new FreemarkerBasedRoute("/update/company/:uniqueKey", "updateCompany.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String,Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_COMPANY);
                root.put("entity", "company");
                String uniqueKey = request.params(":uniqueKey");
                logger.info("Calling route /update company: get " + uniqueKey);
                Company company = companyDAO.getBasedOnUniqueKey(uniqueKey);

                if (company == null) {
                    //TODO: create company not found
                    response.redirect("/post_not_found");
                    return;
                }
                else {

                    root.put("ENTITY_NAME", Constants.ENTITY_COMPANY);
                    root.put("entity", "company");

                    root.put("firstName", company.getFirstName());
                    root.put("lastName", company.getLastName());
                    root.put("nickName", company.getNickName());
                    root.put("key", company.getNickName());
                    root.put("age",company.getAge());
                    if(company.getNationalIdentity()!=null) {
                        root.put("aadhar", company.getNationalIdentity().getAadhar());
                        root.put("pan", company.getNationalIdentity().getPan());
                    }

                    root.put("tin",company.getTin());

                    if(company.getLocation()!=null) {
                        root.put("place", company.getLocation().getPlace());
                        root.put("taluka", company.getLocation().getTaluka());
                        root.put("district", company.getLocation().getDistrict());
                        root.put("state", company.getLocation().getState());
                        root.put("address", company.getLocation().getAddress());
                    }
                    root.put("operation","Update");
                    templateOverride.process(root, writer);
                    return;

                }

            }

        });

        post(new FreemarkerBasedRoute("/add/company", "addCompany.ftl") {
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
                String tin = StringEscapeUtils.escapeHtml4(request.queryParams("tin"));

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_COMPANY);
                root.put("entity", "company");

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
                root.put("tin", tin);
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
                    logger.info("yay, welcoming Company:" + nickName + " to the system");

                    Company company = new Company(nickName);

                    company.setAge(age);
                    company.setFirstName(firstName);
                    company.setLastName(lastName);
                    Location location = new Location(place);
                    location.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    ControllerUtilities.handleLocation(location, address, district, taluka, state);
                    company.setLocation(location);
                    NationalIdentity nationalIdentity;
                    if (StringUtils.isNotBlank(pan)) {
                        nationalIdentity = new NationalIdentity(pan);
                        nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity.setAadhar(aadhar);
                            company.setNationalIdentity(nationalIdentity);
                        }
                    } else {
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity = new NationalIdentity(null, aadhar);
                            nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                            company.setNationalIdentity(nationalIdentity);
                        }
                    }

                    company.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    if (StringUtils.isNotBlank(tin)) {
                        company.setTin(tin);
                    }
                    companyDAO.add(company);
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });


        post(new FreemarkerBasedRoute("/update/company", "addCompany.ftl") {
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
                String tin = StringEscapeUtils.escapeHtml4(request.queryParams("tin"));

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_COMPANY);
                root.put("entity", "company");

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
                root.put("tin", tin);
                /*
                Set<String> set = request.queryParams();
                for(String s: set) {
                    logger.info(s + "::"+request.queryParams(s));
                }
                */

                if (StringUtils.isBlank(nickName)) {
                    root.put("errors", "Do not play with the form fields using HTML tools in browser, we cannot accept blank nickname");
                    templateOverride.process(root, writer);
                    return;
                }

                if (StringUtils.isBlank(firstName)
                        || StringUtils.isBlank(lastName)
                        || StringUtils.isBlank(place)) {
                    root.put("errors", "firstName, last Name, place are mandatory fields,please enter values.");
                    templateOverride.process(root, writer);
                    return;
                } else {
                    // extract tags
                    int age = -1;
                    if (StringUtils.isNotBlank(ageString)) {
                        age = Integer.parseInt(ageString);
                    }

                    //TODO: handle inputs
                    logger.info("yay, Update Company:" + nickName + " to the system");
                    Company company = companyDAO.getBasedOnUniqueKey(nickName);
                    company.setAge(age);
                    company.setFirstName(firstName);
                    company.setLastName(lastName);
                    Location location;
                    if (company.getLocation() == null) {
                        location = new Location(place);
                    } else {
                        location = company.getLocation();
                    }

                    location.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    ControllerUtilities.handleLocation(location, address, district, taluka, state);
                    company.setLocation(location);
                    NationalIdentity nationalIdentity;
                    if (StringUtils.isNotBlank(pan)) {
                        nationalIdentity = new NationalIdentity(pan);
                        nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity.setAadhar(aadhar);
                            company.setNationalIdentity(nationalIdentity);
                        }
                    } else {
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity = new NationalIdentity(null, aadhar);
                            nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                            company.setNationalIdentity(nationalIdentity);
                        }
                    }

                    company.setUpdatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    company.setUpdateDate(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD());
                    if (StringUtils.isNotBlank(tin)) {
                        company.setTin(tin);
                    }
                    companyDAO.update(company);
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                    return;
                }
            }
        });

        get(new FreemarkerBasedRoute("/company/:uniqueKey", "entityCompany.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String uniqueKey = request.params(":uniqueKey");
                logger.info("Calling route /company: get " + uniqueKey);
                Company company = companyDAO.getBasedOnUniqueKey(uniqueKey);

                if (company == null) {
                    //TODO: create company not found
                    response.redirect("/post_not_found");
                    return;
                } else {


                    // empty comment to hold new comment in form at bottom of blog entry detail page

                    //Display Transactions

                    Map<String, Object> root = new HashMap<String, Object>();
                    root.put(Constants.ENTITY, company);
                    root.put("rows", Constants.LIMIT_ROWS);
                    root.put(Constants.ENTITY_NAME, Constants.ENTITY_COMPANY);
                    List<Document> summaryByBarcode = SingletonManagerDAO.getInstance().getItemBuyDAO().inventoryStatus("company", uniqueKey);
                    root.put("entityList",summaryByBarcode);
                    //List<Document> weeklyPayments = SingletonManagerDAO.getInstance().getItemBuyDAO().getEntityDailySummaryNDatesEndingToday(-7, uniqueKey, "company");
                    //List<Document> weeklyTransactions = SingletonManagerDAO.getInstance().getItemBuyDAO().getEntityDailySummaryNDatesEndingToday(-7, uniqueKey, "company");

                    templateOverride.process(root, writer);
                    return;
                }
            }
        });


        get(new FreemarkerBasedRoute("/edit/company/:uniqueKey", "edit_company.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                Map<String, Object> root = new HashMap<String, Object>();
                root.put("ENTITY_NAME", Constants.ENTITY_COMPANY);
                root.put("entity", "company");

                String uniqueKey = StringEscapeUtils.escapeHtml4(request.params(":uniqueKey"));

                if (StringUtils.isBlank(uniqueKey)) {
                    response.redirect("/not_found/company/" + uniqueKey);
                }
                logger.info("Calling route /edit/company: get " + uniqueKey);
                root.put("nickName", uniqueKey);
                Company company = companyDAO.getBasedOnUniqueKey(uniqueKey);

                if (company == null) {
                    //TODO: create company not found
                    response.redirect("/post_not_found/company/" + uniqueKey);
                    return;
                } else {

                    root.put("firstName", company.getFirstName());
                    root.put("lastName", company.getLastName());
                    root.put("nickName", company.getNickName());
                    root.put("tin", company.getTin());
                    if (company.getAge() != -1) {
                        root.put("age", company.getAge());
                    }
                    if (company.getNationalIdentity() != null) {
                        root.put("aadhar", company.getNationalIdentity().getAadhar());
                        root.put("pan", company.getNationalIdentity().getPan());
                    }
                    if (company.getLocation() != null) {
                        root.put("place", company.getLocation().getPlace());
                        root.put("taluka", company.getLocation().getTaluka());
                        root.put("district", company.getLocation().getDistrict());
                        root.put("state", company.getLocation().getState());
                        root.put("address", company.getLocation().getAddress());
                    }
                }
                templateOverride.process(root, writer);

            }

        });

        post(new FreemarkerBasedRoute("/edit/company", "edit_company.ftl") {
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
                String tin = StringEscapeUtils.escapeHtml4(request.queryParams("tin"));

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("ENTITY_NAME", Constants.ENTITY_COMPANY);
                root.put("entity", "company");

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
                root.put("tin", tin);
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
                    logger.info("yay, Updating Company:" + nickName + " to the system");

                    Company company = companyDAO.getBasedOnUniqueKey(nickName);

                    company.setAge(age);
                    company.setFirstName(firstName);
                    company.setLastName(lastName);
                    Location location = new Location(place);
                    location.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    ControllerUtilities.handleLocation(location, address, district, taluka, state);
                    company.setLocation(location);
                    NationalIdentity nationalIdentity;
                    if (StringUtils.isNotBlank(pan)) {
                        nationalIdentity = new NationalIdentity(pan);
                        nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity.setAadhar(aadhar);
                            company.setNationalIdentity(nationalIdentity);
                        }
                    } else {
                        if (StringUtils.isNotBlank(aadhar)) {
                            nationalIdentity = new NationalIdentity(null, aadhar);
                            nationalIdentity.setCreatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                            company.setNationalIdentity(nationalIdentity);
                        }
                    }

                    company.setUpdatedBy(sessionDAO.findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
                    company.setUpdateDate(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD());
                    company.setTin(tin);
                    logger.info(company.toString());
                    companyDAO.update(company);
                    root.put("success", "true");
                    templateOverride.process(root, writer);
                    return;
                }
            }

        });

    }

}
