package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;

/**
 * Created by chandanm on 7/9/16.
 */
public class ItemInventory extends InventoryObj {
    private Item item;
    private int quantity;
    private String company;
    private String barcode;
    private float price; //itemSold fields
    private double amount; //itemSold fields
    private ItemTransactionType itemTransactionType;
    private String transactionId; //only incase of sold Inventory during CustomerTransactions, this is added

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    //this constructor is for itemSold
    /*
    public ItemInventory(String barcode,int quantity) {
        this.barcode = barcode;
        this.quantity = quantity;
        this.setUniqueKey(this.barcode + Constants.UNIQUE_KEY_SEPARATOR + this.get_id());
        itemTransactionType = ItemTransactionType.SELL;
    }

*/
    //this constructor is for itemSold
    //We use negative quantity for sold Items to customers
    public ItemInventory(String barcode,int quantity,float price, String eventDate) {
        this.barcode = barcode;
        this.quantity = quantity;
        if(this.quantity>0) {
            this.quantity = -1 * this.quantity;
        }
        this.price = price;
        this.amount = this.quantity * this.price;
        this.setEventDate(eventDate);
        this.setUniqueKey(this.barcode + Constants.UNIQUE_KEY_SEPARATOR + this.get_id());
        itemTransactionType = ItemTransactionType.SELL;
    }

    //this constructor is strictly for incoming stock
    public ItemInventory(String barcode,int quantity,float price, String eventDate, String company) {
        super();
        this.company = company;
        this.barcode = barcode;
        this.price = price;
        this.quantity = quantity;

        this.amount = this.quantity * this.price;
        this.setEventDate(eventDate);
        this.setUniqueKey(barcode + Constants.UNIQUE_KEY_SEPARATOR + company +Constants.UNIQUE_KEY_SEPARATOR + this.getEventDate());
        itemTransactionType = ItemTransactionType.BUY;
    }

    public ItemTransactionType getItemTransactionType() {
        return itemTransactionType;
    }

    public void setItemTransactionType(ItemTransactionType itemTransactionType) {
        this.itemTransactionType = itemTransactionType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static void main(String[] args) {
     //   ItemInventory itemInventory = new ItemInventory();
       // itemInventory
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
}
