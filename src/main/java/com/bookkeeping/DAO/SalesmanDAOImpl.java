package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Salesman;
import com.bookkeeping.model.Salesman;
import com.bookkeeping.persistence.MongoConnection;
import com.google.gson.Gson;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public class SalesmanDAOImpl extends MongoCollectionDAOImpl<Salesman> implements SalesmanDAO {

    static final Logger logger = LoggerFactory.getLogger(SalesmanDAOImpl.class);

    public SalesmanDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_SALESMAN_COLLECTION);
    }



    @Override
    public Salesman getBasedOnId(String salesmanId) {

        Document query = new Document();
        query.append("_id",salesmanId);
        Document salesmanDocument = mongoCollection.find(query).first();
        Salesman salesman = gson.fromJson(salesmanDocument.toJson(), Salesman.class);
        return salesman;
    }

    @Override
    public Salesman getBasedOnUniqueKey(String uniqueKey) {
        Document doc = mongoCollection.find(this.getUniqueDocument(uniqueKey)).first();
        if(doc!=null) {
            return gson.fromJson(doc.toJson(),Salesman.class);
        }
        return null;
    }

    /*
    @Override
    public Salesman getBasedOnFirstName(String salesmanName) {
        Document query = new Document();
        query.append("firstName",salesmanName);
        Document salesmanDocument = mongoCollection.find(query).first();
        return gson.fromJson(salesmanDocument.toJson(), Salesman.class);
    }
    */
/*
    public List<Salesman> getBasedOnSalesmanFirstName(String salesman) {
        Document query = new Document();
        query.append("firstName",salesman);
        List<Document> salesmanDocumentList = mongoCollection.find(query).into(new ArrayList<Document>());
        return getList(salesmanDocumentList);
    }

    */
    /*
    @Override
    public Salesman getBasedOnFirmName(String firmName) {
        return getBasedOnUniqueKey(firmName);

        Document query = new Document();
        query.append("firmName",firmName);
        Document salesmanDocument = mongoCollection.find(query).first();
        return gson.fromJson(salesmanDocument.toJson(), Salesman.class);



    }

*/


    protected List<Salesman> getList(List<Document> documentList) {
        List<Salesman> SalesmanList = new ArrayList<Salesman>();
        for(Document salesmanDoc : documentList) {
            Salesman salesman = gson.fromJson(salesmanDoc.toJson(), Salesman.class);
            SalesmanList.add(salesman);
        }
        return SalesmanList;
    }

     /*
    @Override
    public List<String> listKeys() {
        List<String> keys = new ArrayList<String>();
        List<Salesman> list = super.list();
        for(Salesman k : list) {
            keys.add(k.getUniqueKey());
        }
        return keys;
    }
    @Override
    protected Document uniqueDocument(Salesman salesman) {
        return new Document()
                .append(Constants.UNIQUE_KEY,
                        salesman.getUniqueKey());
    }

    @Override
    protected Document getDocument(Salesman salesman) {
        return Document.parse(gson.toJson(salesman));
    }

    @Override
    protected String getJsonString(Salesman salesman) {
        return gson.toJson(salesman);
    }


*/
    @Override
    public Salesman getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), Salesman.class);
    }
}
