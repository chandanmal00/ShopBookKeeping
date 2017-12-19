package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chandan on 6/4/16.
 */
public class Customer extends Person {

    private Loyalty loyalty;

    public Customer(String nickName) {
        super(ControllerUtilities.formatUniqueKey(nickName));
    }


    public Loyalty getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(Loyalty loyalty) {
        this.loyalty = loyalty;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Customer create(int i) {
        Customer customer =  new Customer("customer_"+i);
        if(i%10==0) {
            Loyalty loyalty = new Loyalty();
            loyalty.setNumber("11221_"+i);
            if(i%2==0) {
                loyalty.setType("Gold");
            } else {
                loyalty.setType("Platinum");
            }
            customer.setLoyalty(loyalty);
        }
        return customer;
    }
}


