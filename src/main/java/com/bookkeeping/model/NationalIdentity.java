package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;
import com.google.gson.Gson;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chandan on 6/4/16.
 */
public class NationalIdentity {
    private String pan;
    private String aadhar;
    private String _id = ObjectId.get().toHexString();
    private String creationDate;
    private String creationTime;
    private String uniqueKey;
    private String createdBy;

    public NationalIdentity(String pan) {
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        SimpleDateFormat dtFull = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
        Date date = new Date();
        this.creationDate = dt.format(date);
        this.creationTime = dtFull.format(date);
        this.pan = pan;
        this.uniqueKey=pan;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public NationalIdentity(String pan,String aadhar) {
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        SimpleDateFormat dtFull = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
        Date date = new Date();
        this.creationDate = dt.format(date);
        this.creationTime = dtFull.format(date);
        this.pan = pan;
        this.aadhar=aadhar;
        this.uniqueKey= this.pan
                + Constants.UNIQUE_KEY_SEPARATOR
                + this.aadhar;
        this.createdBy = "root";
    }




    public String get_id() {
        return _id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }


    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
