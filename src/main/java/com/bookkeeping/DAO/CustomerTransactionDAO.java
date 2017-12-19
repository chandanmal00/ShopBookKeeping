package com.bookkeeping.DAO;

import com.bookkeeping.model.CustomerTransaction;
import org.bson.Document;

import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public interface CustomerTransactionDAO extends MongoCollectionDAO<CustomerTransaction> {
    public double transactionSumForCustomer(String customerUniqueKey,String targetDate);
    public double transactionSum(String targetDate);
    public List<CustomerTransaction> getBasedOnCustomerKeyWithLimit(String customerUniqueKey,int rowCount);
    public List<CustomerTransaction> getBasedOnCustomerKey(String customerUniqueKey, String targetDate);
    public List<CustomerTransaction> getBasedOnDate(String targetDate);
    public double transactionSumForCustomer(String customerUniqueKey);
    public double transactionSumForEntity(String entity, String uniqueKey,String targetDate);
    public double transactionSumForEntity(String entity,String uniqueKey);
    public List<CustomerTransaction> getBasedOnEntityKeyWithLimit(String entity, String customerUniqueKey,int rowCount);
    public List<Document> getSummaryByEntity(String entity);

}
