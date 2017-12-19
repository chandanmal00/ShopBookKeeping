package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Customer;
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
public class CustomerDAOImpl extends MongoCollectionDAOImpl<Customer> implements CustomerDAO {

    Gson gson;
    static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);


    public CustomerDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_CUSTOMER_COLLECTION);
        gson = new Gson();
    }

    protected List<Customer> getList(List<Document> documentList) {
        List<Customer> CustomerList = new ArrayList<Customer>();
        for(Document itemDoc : documentList) {
            Customer item = gson.fromJson(itemDoc.toJson(), Customer.class);
            CustomerList.add(item);
        }
        return CustomerList;
    }



    @Override
    public Customer getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), Customer.class);
    }
}
