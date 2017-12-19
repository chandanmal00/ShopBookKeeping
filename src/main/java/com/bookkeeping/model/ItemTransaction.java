package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;

/**
 * Created by chandanm on 8/12/16.
 */
public class ItemTransaction extends InventoryObj{
    private int quantity;
    private String barcode;
    private float price;
    private double amount;
    private ItemTransactionType itemTransactionType;
    private Item item;
    private TransactionDetails details;


    public ItemTransaction(String barcode, int quantity, float price,String eventDate) {

        this.quantity = quantity;
        this.barcode = barcode;
        this.price = ControllerUtilities.formatDecimalValue(price);
        this.amount = ControllerUtilities.formatDecimalValue(price * quantity);
        this.setEventDate(eventDate);
        this.setUniqueKey(barcode+ Constants.UNIQUE_KEY_SEPARATOR+eventDate);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemTransactionType getItemTransactionType() {
        return itemTransactionType;
    }

    public void setItemTransactionType(ItemTransactionType itemTransactionType) {
        this.itemTransactionType = itemTransactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionDetails getDetails() {
        return details;
    }

    public void setDetails(TransactionDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
