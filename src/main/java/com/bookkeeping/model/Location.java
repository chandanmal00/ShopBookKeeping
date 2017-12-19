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
public class Location extends InventoryObj {

    private String place;
    private  String taluka;
    private String district;
    private String address;
    private String state;

    public Location(String place) {
        super();
        this.place = place;
        super.setUniqueKey(ControllerUtilities.formatUniqueKey(place));

    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static void main(String[] args) {
        Location location = new Location("warora");
    }
}
