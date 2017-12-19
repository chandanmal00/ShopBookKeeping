package com.bookkeeping.model;

import com.google.gson.Gson;

/**
 * Created by chandan on 6/4/16.
 */
public class ShopKeeper extends Person {

    //Its in percentage, can be 0

    public ShopKeeper(String nickName) {
        super(nickName);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}


