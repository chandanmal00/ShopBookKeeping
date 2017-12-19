package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chandanm on 7/9/16.
 */
public abstract class InventoryObj {
    private String _id = ObjectId.get().toHexString();
    private String creationDate;
    private String eventDate;
    private String creationTime;
    private String uniqueKey;
    private String createdBy;
    private String updatedBy;
    private String updateDate;

    public InventoryObj() {
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        SimpleDateFormat dtFull = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
        Date date = new Date();
        this.creationDate = dt.format(date);
        this.creationTime = dtFull.format(date);
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

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
