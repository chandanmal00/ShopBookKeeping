package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by chandan on 6/4/16.
 */
public class CustomerPayment extends InventoryObj {

    private double amount;
    private String customer;
    private String tag="NO_TAG_PROVIDED";
    private String paymentType;
    private String transactionId; //this is the one associated with the transaction done for the first time.

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public CustomerPayment(String customer, String dateStr, double amount) {
        this.customer = customer;
        this.amount=ControllerUtilities.formatDecimalValue(amount);
        this.setEventDate(dateStr);
        this.setUniqueKey(this.customer
                + Constants.UNIQUE_KEY_SEPARATOR
                + this.amount
                + Constants.UNIQUE_KEY_SEPARATOR
                + this.getCreationDate());
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static CustomerPayment create(int i, int customer, double payment) {
        String dateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
        Date nDaysBack = ControllerUtilities.getNDaysDate(ControllerUtilities.getDateInFormat(dateStr), -i % 31);

        return new CustomerPayment("customer_"+customer, ControllerUtilities.formatDateInYYYY_MM_DD(nDaysBack),payment);
    }
}
