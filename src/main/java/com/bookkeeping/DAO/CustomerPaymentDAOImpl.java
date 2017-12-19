package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Customer;
import com.bookkeeping.model.CustomerPayment;
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

/**
 * Created by chandan on 7/18/16.
 */
public class CustomerPaymentDAOImpl extends MongoCollectionDAOImpl<CustomerPayment> implements CustomerPaymentDAO {
    //Client client;
    static final Logger logger = LoggerFactory.getLogger(CustomerPaymentDAOImpl.class);


    public CustomerPaymentDAOImpl() {
        //this.client = ElasticCacheConnection.getInstance();
        super();
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_CUSTOMER_PAYMENT_COLLECTION);


    }

    @Override
    public List<String> listKeys() {
        List<String> keys = new ArrayList<String>();
        List<CustomerPayment> list = super.list();
        for(CustomerPayment k : list) {
            keys.add(k.getUniqueKey());
        }
        return keys;
    }


    @Override
    public List<CustomerPayment> getBasedOnCustomer(String uniqueKey) {
        Document doc = new Document();
        doc.append("customer", uniqueKey);
        //doc.append("customer.uniqueKey", uniqueKey);
        List<Document> customerPaymentDocList = mongoCollection.find(doc).into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }

    @Override
    public List<CustomerPayment> getBasedOnCustomerWithLimit(String uniqueKey, int rowCount) {
        Document doc = new Document();
        //doc.append("customer.uniqueKey", uniqueKey);
        doc.append("customer", uniqueKey);
        List<Document> customerPaymentDocList = mongoCollection.find(doc).sort(new Document("creationTime", -1)).limit(10).into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }


    @Override
    public List<CustomerPayment> getBasedOnCustomer(String uniqueKey,String targetDate) {
        Document doc = new Document();
        //doc.append("customer.uniqueKey", uniqueKey);
        doc.append("customer", uniqueKey);
        doc.append("eventDate", targetDate);
        List<Document> customerPaymentDocList = mongoCollection.find(doc).into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }

    /*
    @Override
    public List<CustomerPayment> getBasedOnCustomerFirstName(String firstName) {
        Document doc = new Document();
        doc.append("customer.firstName", firstName);
        List<Document> customerPaymentDocList = mongoCollection.find(doc).into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }

    @Override
    public List<CustomerPayment> getBasedOnCustomerLastName(String lastName) {
        Document doc = new Document();
        doc.append("customer.lastName", lastName);
        List<Document> customerPaymentDocList = mongoCollection.find(doc).into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }

    @Override
    public List<CustomerPayment> getBasedOnCustomerFullName(String firstName, String lastName) {
        Document doc = new Document();
        doc.append("customer.firstName", firstName);
        doc.append("customer.lastName", lastName);
        List<Document> customerPaymentDocList = mongoCollection.find(doc).into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }
*/
    @Override
    public double paymentSumBasedOnCustomer(String customerUniqueKey) {
        List<Bson> bsonList = new ArrayList<Bson>();
        String docKey = "total";

        //String matchStr = "{$match:{\"customer.uniqueKey\":\""+customerUniqueKey+"\"}}";
        String matchStr = "{$match:{\"customer\":\""+customerUniqueKey+"\"}}";
        //String jsonGroupStr = "{$group:{_id:\"$creationDate\",total:{$sum:\"$amount\"}}}";
       // String jsonGroupStr = "{"+docKey+":{$sum:\"$amount\"}}}";
        String jsonGroupStr = "{$group:{_id:null,"+docKey+":{$sum:\"$amount\"}}}";
        //BsonDocument bsonDocument = BsonDocument.parse(jsonGroupStr);


        bsonList.add(BsonDocument.parse(matchStr));
        bsonList.add(BsonDocument.parse(jsonGroupStr));
        Document doc = mongoCollection.aggregate(bsonList).first();
        if(doc!=null) {
            return Double.valueOf(doc.getDouble(docKey)+"");
        }
        return 0d;
    }

    @Override
    public double paymentSumBasedOnCustomer(String customerUniqueKey, String targetDate) {
        List<Bson> bsonList = new ArrayList<Bson>();
        String docKey = "total";

       // String matchStr = "{$match:{\"eventDate\":\""+targetDate+"\", \"customer.uniqueKey\":\""+customerUniqueKey+"\"}}";
        String matchStr = "{$match:{\"eventDate\":\""+targetDate+"\", \"customer\":\""+customerUniqueKey+"\"}}";
        //String jsonGroupStr = "{"+docKey+":{$sum:\"$amount\"}}}";
        String jsonGroupStr = "{$group:{_id:null,"+docKey+":{$sum:\"$amount\"}}}";

        bsonList.add(BsonDocument.parse(matchStr));
        bsonList.add(BsonDocument.parse(jsonGroupStr));
        Document doc = mongoCollection.aggregate(bsonList).first();
        if(doc !=null) return Double.valueOf(doc.getDouble(docKey)+"");
        return 0d;

    }

    @Override
    public List<Document> paymentSummaryBasedOnDate(String targetDate) {
        List<Bson> bsonList = new ArrayList<Bson>();

        String matchStr = "{$match:{\"eventDate\":\""+targetDate+"\"}}";
        //String jsonGroupStr = "{$group:{_id:\"$customer.uniqueKey\",total:{$sum:\"$amount\"}}}";
        String jsonGroupStr = "{$group:{_id:\"$customer\",total:{$sum:\"$amount\"}}}";
        BsonDocument bsonDocument = BsonDocument.parse(jsonGroupStr);

        bsonList.add(BsonDocument.parse(matchStr));
        bsonList.add(bsonDocument);
        List<Document> paymentKhareeddarDocList = mongoCollection.aggregate(bsonList).into(new ArrayList<Document>());

        return paymentKhareeddarDocList;
    }

    @Override
    public List<Document> dailyPaymentSummaryBasedOnNickName(String nickName) {
        List<Bson> bsonList = new ArrayList<Bson>();

        String matchStr = "{$match:{\"customer\":\""+nickName+"\"}}";
       // String matchStr = "{$match:{\"customer.nickName\":\""+nickName+"\"}}";
        String jsonGroupStr = "{$group:{_id:\"$eventDate\",total:{$sum:\"$amount\"}}}";
        BsonDocument bsonDocument = BsonDocument.parse(jsonGroupStr);

        bsonList.add(BsonDocument.parse(matchStr));
        bsonList.add(bsonDocument);
        List<Document> paymentKhareeddarDocList = mongoCollection.aggregate(bsonList).into(new ArrayList<Document>());

        return paymentKhareeddarDocList;

    }

    @Override
    public Document paymentSummaryBasedOnNickName(String nickName) {
        List<Bson> bsonList = new ArrayList<Bson>();

        String matchStr = "{$match:{\"customer\":\""+nickName+"\"}}";
        //String matchStr = "{$match:{\"customer.nickName\":\""+nickName+"\"}}";
        //String jsonGroupStr = "{$group:{_id:\"$customer.nickName\",total:{$sum:\"$amount\"}}}";
        String jsonGroupStr = "{$group:{_id:\"$customer\",total:{$sum:\"$amount\"}}}";
        //System.out.println(matchStr);

        BsonDocument bsonDocument = BsonDocument.parse(jsonGroupStr);

        bsonList.add(BsonDocument.parse(matchStr));
        bsonList.add(bsonDocument);
        Document paymentKhareeddarDoc = mongoCollection.aggregate(bsonList).first();

        return paymentKhareeddarDoc;
    }

    @Override
    public List<Document> paymentSummaryByNickName() {
        List<Bson> bsonList = new ArrayList<Bson>();

        String jsonStr = "{$group:{_id:\"$customer\",total:{$sum:\"$amount\"}}}";
        //String jsonStr = "{$group:{_id:\"$customer.nickName\",total:{$sum:\"$amount\"}}}";
        BsonDocument bsonDocument = BsonDocument.parse(jsonStr);

        bsonList.add(bsonDocument);
        List<Document> docList = mongoCollection.aggregate(bsonList).into(new ArrayList<Document>());

        return docList;
    }


    @Override
    public CustomerPayment getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), CustomerPayment.class);
    }

    @Override
    protected List<CustomerPayment> getList(List<Document> documentList) {
        List<CustomerPayment> customerPaymentList = new ArrayList<CustomerPayment>();
        for (Document customerPaymentDoc : documentList) {
            // System.out.println("HERE you go::" + customerPaymentDoc.toJson());
            CustomerPayment customerPayment = gson.fromJson(customerPaymentDoc.toJson(), CustomerPayment.class);
            customerPaymentList.add(customerPayment);
        }
        return customerPaymentList;
    }

    /*
    public List<CustomerPayment> list(String customerId) {
        Document customer = new Document();
        customer.append("customerId",customerId);
        List<Document> customerPaymentDocList = mongoCollection.find(customer).into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }
    */
    /*
    private Document uniqueDocument(CustomerPayment customerPayment) {
        return new Document()
                .append("amount", customerPayment.getAmount())
                .append("customer.nickName",customerPayment.getCustomer().getNickName())
                .append("creationDate",customerPayment.getCreationDate());

    }
    private List<CustomerPayment> getList(List<Document> documentList) {
        List<CustomerPayment> customerPaymentList = new ArrayList<CustomerPayment>();
        for(Document customerPaymentDoc : documentList) {
            System.out.println("HERE you go::"+customerPaymentDoc.toJson());
            CustomerPayment customerPayment = gson.fromJson(customerPaymentDoc.toJson(), CustomerPayment.class);
            customerPaymentList.add(customerPayment);
        }
        return customerPaymentList;
    }

    @Override
    public List<CustomerPayment> list(String customerId) {
        Document customer = new Document();
        customer.append("customerId",customerId);
        List<Document> customerPaymentDocList = mongoCollection.find(customer).into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }

    @Override
    public void add(CustomerPayment customerPayment) {

        Document doc = mongoCollection.find(uniqueDocument(customerPayment)).first();
        if(doc==null) {
            mongoCollection.insertOne(Document.parse(gson.toJson(customerPayment)));
        } else {
            logger.error("It seems we are inserting same details again for the CustomerPayment: {}",gson.toJson(customerPayment));
            logger.error("Use forceAdd method incase you feel this is fine");
        }

    }

    @Override
    public void forceAdd(CustomerPayment customerPayment) {
        logger.info("Adding the transaction without any check for CustomerPayment:{}",gson.toJson(customerPayment));
        mongoCollection.insertOne(Document.parse(gson.toJson(customerPayment)));
    }

    @Override
    public void remove(CustomerPayment customerPayment) {
        mongoCollection.deleteOne(Document.parse(gson.toJson(customerPayment)));

    }

        @Override
    public List<CustomerPayment> list() {
        List<Document> customerPaymentDocList = mongoCollection.find().into(new ArrayList<Document>());
        return getList(customerPaymentDocList);
    }



*/

    public static void main(String[] args) {
        String nickName = "maloo1";


        CustomerDAO customerDAO = new CustomerDAOImpl();
        Customer customer = customerDAO.getBasedOnUniqueKey(nickName);

        if(customer==null) {
            logger.error("customer with {} does not exist, creating one",nickName);
            customer = new Customer(nickName);
            customerDAO.add(customer);

        }
        CustomerPaymentDAO customerPaymentDAO = new CustomerPaymentDAOImpl();
        CustomerPayment customerPayment = new CustomerPayment(customer.getUniqueKey(), ControllerUtilities.getCurrentDateStrInYYYY_MM_DD(),2000d);

        customerPaymentDAO.add(customerPayment);

        customerPayment = new CustomerPayment(customer.getUniqueKey(),ControllerUtilities.getCurrentDateStrInYYYY_MM_DD(),5000d);


        customerPaymentDAO.add(customerPayment);

        String targetDate="2016-06-18";
        Gson gson = new Gson();
        System.out.println("SUmm by Date:");
        System.out.println(gson.toJson(customerPaymentDAO.paymentSummaryBasedOnDate(targetDate)));

        System.out.println("SUmm by NickName:");
        System.out.println(gson.toJson(customerPaymentDAO.paymentSummaryBasedOnNickName(nickName)));

        System.out.println("SUmm by nickName date:");
        System.out.println(gson.toJson(customerPaymentDAO.dailyPaymentSummaryBasedOnNickName(nickName)));

        System.out.println("SUmm by all:");
        System.out.println(gson.toJson(customerPaymentDAO.paymentSummaryByNickName()));
    }


    public List<Document> getSummaryByEntity(String entity) {

        String docKey = "total";
        //String jsonGroupStr = "{"+docKey+":{$sum:\"$amount\"}}";
        //String jsonGroupStr = "{$group:{_id:\"$"+entity+".uniqueKey\", "+docKey+":{$sum:\"$amount\"}}}";
        String jsonGroupStr = "{$group:{_id:\"$"+entity+"\", "+docKey+":{$sum:\"$amount\"}}}";
        //String jsonGroupStr = "{total:{$sum:\"$amount\"}}";

        logger.debug(jsonGroupStr);
        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(BsonDocument.parse(jsonGroupStr));


        List<Document> documentList = mongoCollection.aggregate(bsonList).into(new ArrayList<Document>());
        logger.info(gson.toJson(documentList));
        return documentList;
    }



}
