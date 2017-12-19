package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;

/**
 * Created by chandan on 8/7/16.
 */
public class Backup extends InventoryObj {
    String path;

    public Backup(String dateStr,String path) {
        this.path = path;
        this.setEventDate(dateStr);
        this.setUniqueKey(dateStr + Constants.UNIQUE_KEY_SEPARATOR + path);
    }

    public String getPath() {
        return path;
    }

}
