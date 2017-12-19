package com.bookkeeping.DAO;

import com.bookkeeping.model.CustomerPayment;
import org.bson.Document;

import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public interface CustomerPaymentDAO extends MongoCollectionDAO<CustomerPayment> {


    /*
    public void add(CustomerPayment customerPayment);
    public void forceAdd(CustomerPayment customerPayment);
    public void remove(CustomerPayment customerPayment);
    public List<CustomerPayment> list();


    */
    

    //public List<CustomerPayment> getBasedOnCustomerId(String customerId);
    //public List<CustomerPayment> getBasedOnCustomerNickName(String nickName);
    public List<CustomerPayment> getBasedOnCustomer(String customerUniqueKey);
    public List<CustomerPayment> getBasedOnCustomerWithLimit(String customerUniqueKey, int rowCount);
    public List<CustomerPayment> getBasedOnCustomer(String customerUniqueKey, String targetDate);
    public double paymentSumBasedOnCustomer(String customerUniqueKey);
    public double paymentSumBasedOnCustomer(String customerUniqueKey, String targetDate);
    //public List<CustomerPayment> paymentSumBasedOnCustomer(String uniqueKey, String targetDate);
    //public List<CustomerPayment> getBasedOnCustomerFirstName(String firstName);
    //public List<CustomerPayment> getBasedOnCustomerLastName(String lastName);
    //public List<CustomerPayment> getBasedOnCustomerFullName(String firstName, String lastName);
    public List<Document> paymentSummaryBasedOnDate(String targetDate);
    public List<Document> dailyPaymentSummaryBasedOnNickName(String nickName);
    public Document paymentSummaryBasedOnNickName(String nickName);
    public List<Document> paymentSummaryByNickName();
    public List<Document> getSummaryByEntity(String entity);
}
