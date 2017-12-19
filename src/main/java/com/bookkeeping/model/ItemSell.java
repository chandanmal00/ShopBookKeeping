package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;

/**
 * Created by chandanm on 7/9/16.
 */
public class ItemSell extends ItemTransaction {
    private String transactionId; //only incase of sold Inventory during CustomerTransactions, this is added

    public ItemSell(String barcode, int quantity, float price, String eventDate) {
        super(barcode, quantity, price,eventDate);
        //Do amount calculation again in case we do negative quantity
        if(quantity<0) {
            this.setAmount(-1*quantity*price);
        }

        //Sets the quantity to negative as as it helps keeping info in the same table and avoid joining cose
        //for getting inventory Status
        if(quantity>0) {
            quantity=-1*quantity;
        }
        this.setItemTransactionType(ItemTransactionType.SELL);
        this.setQuantity(quantity);
        this.setAmount(ControllerUtilities.formatDecimalValue(this.getAmount()));
        this.setUniqueKey(barcode + Constants.UNIQUE_KEY_SEPARATOR + this.get_id());

    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setInvoiceId(transactionId);
        this.setDetails(transactionDetails);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static void main(String[] args) {
        ItemSell itemSell = new ItemSell("d",10,300,"2312");
        System.out.println(itemSell.toString());
    }
}
