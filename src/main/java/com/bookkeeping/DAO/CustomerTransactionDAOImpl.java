package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Customer;
import com.bookkeeping.model.CustomerTransaction;
import com.bookkeeping.model.InventoryObj;
import com.bookkeeping.model.Tuple;
import com.bookkeeping.persistence.MongoConnection;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chandan on 6/4/16.
 */
public class CustomerTransactionDAOImpl extends MongoCollectionDAOImpl<CustomerTransaction> implements CustomerTransactionDAO {

    Gson gson;
    static final Logger logger = LoggerFactory.getLogger(CustomerTransactionDAOImpl.class);


    public CustomerTransactionDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_CUSTOMER_TRANSACTIONS_COLLECTION);
        gson = new Gson();
    }

    protected List<CustomerTransaction> getList(List<Document> documentList) {
        List<CustomerTransaction> CustomerTransactionList = new ArrayList<CustomerTransaction>();
        for(Document itemDoc : documentList) {
            CustomerTransaction item = gson.fromJson(itemDoc.toJson(), CustomerTransaction.class);
            CustomerTransactionList.add(item);
        }
        return CustomerTransactionList;
    }


    @Override
    public double transactionSumForCustomer(String customerUniqueKey,String targetDate) {
        List<Bson> bsonList = new ArrayList<Bson>();

        String docKey = "total";
        String matchStr = "{$match:{\"eventDate\":\""+targetDate+"\", \"customer\":\""+customerUniqueKey+"\"}}";
        //String matchStr = "{$match:{\"eventDate\":\""+targetDate+"\", \"customer.uniqueKey\":\""+customerUniqueKey+"\"}}";
        bsonList.add(BsonDocument.parse(matchStr));
        String jsonGroupStr = "{$group:{_id:null,"+docKey+":{$sum:\"$amount\"}}}";
        // String jsonGroupStr = "{total:{$sum:\"$amount\"}}";
        //String jsonGroupStr = "{total:{$sum:\"$amount\"}}";
        bsonList.add(BsonDocument.parse(jsonGroupStr));
        Document doc = mongoCollection.aggregate(bsonList).first();

        if(doc!=null) return Double.valueOf(doc.get(docKey)+"");
        return 0d;

    }

    @Override
    public double transactionSumForEntity(String entity, String uniqueKey,String targetDate) {
        List<Bson> bsonList = new ArrayList<Bson>();

        String docKey = "total";
        String matchStr = "{$match:{\"eventDate\":\""+targetDate+"\", \""+entity+"\":\""+uniqueKey+"\"}}";
        //String matchStr = "{$match:{\"eventDate\":\""+targetDate+"\", \""+entity+".uniqueKey\":\""+uniqueKey+"\"}}";
        bsonList.add(BsonDocument.parse(matchStr));
        String jsonGroupStr = "{$group:{_id:null,"+docKey+":{$sum:\"$amount\"}}}";
        // String jsonGroupStr = "{total:{$sum:\"$amount\"}}";
        //String jsonGroupStr = "{total:{$sum:\"$amount\"}}";
        bsonList.add(BsonDocument.parse(jsonGroupStr));
        Document doc = mongoCollection.aggregate(bsonList).first();

        if(doc!=null) return Double.valueOf(doc.get(docKey)+"");
        return 0d;

    }

    @Override
    public double transactionSum(String targetDate) {
        List<Bson> bsonList = new ArrayList<Bson>();

        String docKey = "total";
        String matchStr = "{$match:{\"eventDate\":\""+targetDate+"\"}}";
        bsonList.add(BsonDocument.parse(matchStr));
        String jsonGroupStr = "{"+docKey+":{$sum:\"$amount\"}}";
        bsonList.add(BsonDocument.parse(jsonGroupStr));
        Document doc = mongoCollection.aggregate(bsonList).first();

        if(doc!=null) return Double.valueOf(doc.get(docKey)+"");
        return 0d;
    }

    @Override
    public List<CustomerTransaction> getBasedOnCustomerKeyWithLimit(String customerUniqueKey,int rowCount) {
        Document doc = new Document();
        //doc.append("customer.uniqueKey",customerUniqueKey);
        doc.append("customer",customerUniqueKey);
        return getList(mongoCollection.find(doc).sort(new Document("creationTime", -1)).limit(10).into(new ArrayList<Document>()));
    }

    @Override
    public List<CustomerTransaction> getBasedOnEntityKeyWithLimit(String entity, String customerUniqueKey,int rowCount) {
        Document doc = new Document();
        //doc.append(entity+".uniqueKey",customerUniqueKey);
        doc.append(entity,customerUniqueKey);
        return getList(mongoCollection.find(doc).sort(new Document("creationTime", -1)).limit(10).into(new ArrayList<Document>()));
    }


    @Override
    public List<CustomerTransaction> getBasedOnCustomerKey(String customerUniqueKey, String targetDate) {
        Document doc = new Document();
        //doc.append("customer.uniqueKey", customerUniqueKey);
        doc.append("customer", customerUniqueKey);
        doc.append("eventDate",targetDate);
        return getList(mongoCollection.find(doc).into(new ArrayList<Document>()));
    }
    
    @Override
    public List<CustomerTransaction> getBasedOnDate(String targetDate) {
        Document doc = new Document();
        doc.append("eventDate",targetDate);

        return getList(mongoCollection.find(doc).into(new ArrayList<Document>()));
    }

    @Override
    public double transactionSumForCustomer(String customerUniqueKey) {

        String docKey = "total";
        //String matchStr = "{$match:{\"customer.uniqueKey\":\""+customerUniqueKey+"\"}}";
        String matchStr = "{$match:{\"customer\":\""+customerUniqueKey+"\"}}";
        //String jsonGroupStr = "{"+docKey+":{$sum:\"$amount\"}}";
        String jsonGroupStr = "{$group:{_id:null,"+docKey+":{$sum:\"$amount\"}}}";
        //String jsonGroupStr = "{total:{$sum:\"$amount\"}}";

        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(BsonDocument.parse(matchStr));
        bsonList.add(BsonDocument.parse(jsonGroupStr));

        logger.info(matchStr);
        logger.info(jsonGroupStr);
        Document doc = mongoCollection.aggregate(bsonList).first();
        if(doc!=null) return Double.valueOf(doc.get(docKey)+"");
        return 0d;

    }

    @Override
    public double transactionSumForEntity(String entity,String uniqueKey) {

        String docKey = "total";
        //String matchStr = "{$match:{\""+entity+".uniqueKey\":\""+uniqueKey+"\"}}";
        String matchStr = "{$match:{\""+entity+"\":\""+uniqueKey+"\"}}";
        //String jsonGroupStr = "{"+docKey+":{$sum:\"$amount\"}}";
        String jsonGroupStr = "{$group:{_id:null,"+docKey+":{$sum:\"$amount\"}}}";
        //String jsonGroupStr = "{total:{$sum:\"$amount\"}}";

        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(BsonDocument.parse(matchStr));
        bsonList.add(BsonDocument.parse(jsonGroupStr));

        logger.info(matchStr);
        logger.info(jsonGroupStr);
        Document doc = mongoCollection.aggregate(bsonList).first();
        if(doc!=null) return Double.valueOf(doc.get(docKey)+"");
        return 0d;

    }

    @Override
    public CustomerTransaction getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), CustomerTransaction.class);
    }

    public List<Document> getSummaryByEntity(String entity) {

        String docKey = "total";
        //String jsonGroupStr = "{"+docKey+":{$sum:\"$amount\"}}";
        String jsonGroupStr = "{$group:{_id:\"$"+entity+"\", "+docKey+":{$sum:\"$amount\"}}}";
        //String jsonGroupStr = "{$group:{_id:\"$"+entity+".uniqueKey\", "+docKey+":{$sum:\"$amount\"}}}";
        //String jsonGroupStr = "{total:{$sum:\"$amount\"}}";

        logger.debug(jsonGroupStr);
        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(BsonDocument.parse(jsonGroupStr));


        List<Document> documentList = mongoCollection.aggregate(bsonList).into(new ArrayList<Document>());
        logger.info(gson.toJson(documentList));
        return documentList;
    }

    public static void main(String[] args) {
        MongoConnection.init(Constants.MONGO_PORT,Constants.MONGO_HOST,Constants.MONGO_INSTALLATION);
        CustomerTransactionDAO customerTransactionDAO = new CustomerTransactionDAOImpl();
        List<Document> docList1= customerTransactionDAO.getSummaryByEntity("customer");

        CustomerPaymentDAO customerPaymentDAO = new CustomerPaymentDAOImpl();
        List<Document> docList2= customerPaymentDAO.getSummaryByEntity("customer");
        Map<String,Tuple<Document>> joinMap = ControllerUtilities.joinByIdKey(docList1,docList2);

        List<Document> finalList = ControllerUtilities.convertJoinedMapDocumentList(joinMap);
        Gson gson = new Gson();
        logger.info(gson.toJson(finalList));

        CustomerDAO customerDAO = new CustomerDAOImpl();

        List<Customer> customerList = customerDAO.list();

        Map<String,Tuple<Document>> joinMapEntityRecs = ControllerUtilities.joinByKey(customerList,finalList);
        logger.info(gson.toJson(joinMapEntityRecs));
    }
}
