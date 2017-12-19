package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Company;
import com.bookkeeping.persistence.MongoConnection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public class CompanyDAOImpl extends MongoCollectionDAOImpl<Company> implements CompanyDAO {

    static final Logger logger = LoggerFactory.getLogger(CompanyDAOImpl.class);


    public CompanyDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_COMPANY_COLLECTION);
    }


    /*

    @Override
    public Company getBasedOnId(String companyId) {

        Document query = new Document();
        query.append("_id",companyId);
        Document companyDocument = mongoCollection.find(query).first();
        Company company = gson.fromJson(companyDocument.toJson(), Company.class);
        return company;
    }

    @Override
    public Company getBasedOnUniqueKey(String uniqueKey) {
        Document doc = mongoCollection.find(this.getUniqueDocument(uniqueKey)).first();
        if(doc!=null) {
            return gson.fromJson(doc.toJson(),Company.class);
        }
        return null;
    }

    /*
    @Override
    public Company getBasedOnFirstName(String companyName) {
        Document query = new Document();
        query.append("firstName",companyName);
        Document companyDocument = mongoCollection.find(query).first();
        return gson.fromJson(companyDocument.toJson(), Company.class);
    }
    */
/*
    public List<Company> getBasedOnCompanyFirstName(String company) {
        Document query = new Document();
        query.append("firstName",company);
        List<Document> companyDocumentList = mongoCollection.find(query).into(new ArrayList<Document>());
        return getList(companyDocumentList);
    }

    */
    /*
    @Override
    public Company getBasedOnFirmName(String firmName) {
        return getBasedOnUniqueKey(firmName);

        Document query = new Document();
        query.append("firmName",firmName);
        Document companyDocument = mongoCollection.find(query).first();
        return gson.fromJson(companyDocument.toJson(), Company.class);



    }

*/
    /*
    @Override
    public List<String> listKeys() {
        List<String> keys = new ArrayList<String>();
        List<Company> list = super.list();
        for(Company k : list) {
            keys.add(k.getUniqueKey());
        }
        return keys;
    }

*/
    protected List<Company> getList(List<Document> documentList) {
        List<Company> CompanyList = new ArrayList<Company>();
        for(Document companyDoc : documentList) {
            Company company = gson.fromJson(companyDoc.toJson(), Company.class);
            CompanyList.add(company);
        }
        return CompanyList;
    }

    /*
    @Override
    protected Document uniqueDocument(Company company) {
        return new Document()
                .append(Constants.UNIQUE_KEY,
                        company.getUniqueKey());
    }

    @Override
    protected Document getDocument(Company company) {
        return Document.parse(gson.toJson(company));
    }

    @Override
    protected String getJsonString(Company company) {
        return gson.toJson(company);
    }

*/


    @Override
    public Company getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), Company.class);
    }
}
