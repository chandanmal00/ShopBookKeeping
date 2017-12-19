package com.bookkeeping.model;

import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;

/**
 * Created by chandan on 6/4/16.
 */
public class Company extends Person {
    private String tin;

    public Company(String nickName) {
        super(ControllerUtilities.formatUniqueKey(nickName));
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public static Company create(int i) {
        Company company = new Company("company_"+i%3000);
        return company;
    }
}


