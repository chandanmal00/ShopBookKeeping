package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;

/**
 * Created by chandanm on 7/9/16.
 */
public class ItemBuy extends ItemTransaction {

    private String company;
    //this constructor is for itemSold
    //We use negative quantity for sold Items to customers

    public ItemBuy(String barcode, int quantity, float price, String company, String eventDate) {
        super(barcode, quantity, price, eventDate);
        this.company = company;
        this.setItemTransactionType(ItemTransactionType.BUY);
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setCompany(company);
        this.setDetails(transactionDetails);
        this.setUniqueKey(barcode + Constants.UNIQUE_KEY_SEPARATOR + company + Constants.UNIQUE_KEY_SEPARATOR + this.getEventDate());
    }
}
