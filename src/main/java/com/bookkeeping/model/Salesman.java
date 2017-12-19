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
public class Salesman extends Person {

    //Its in percentage, can be 0
    private float commission = 0;

    public Salesman(String nickName) {
        super(ControllerUtilities.formatUniqueKey(nickName));
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Salesman create(int i) {
        Salesman salesman = new Salesman("salesman_"+i);
        if(i%7==0) {
            salesman.setCommission(0.5f);
        }
        if(i%5==0) {
            salesman.setCommission(1);
        }
        return salesman;
    }

    public static void main(String[] args) {
        String entity="chand";
        String limit = "10";
        System.out.println(String.format("Empty entity:%s or limit field:%s",entity,limit));
    }
}


