package com.bookkeeping.model;

import com.bookkeeping.DAO.*;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public class CustomerTransaction extends InventoryObj {
    private String customer;
    private List<ItemSell> itemSolds;
    private double amount;
    private float discountPercent;
    private float discount;
    private float additionalCharges;
    private double paidAmount;
    private double actualAmount;
    private String salesman;
    private String paymentType;


    public CustomerTransaction(String customer,
                               List<ItemSell> itemSolds,
                               double amount,
                               double paidAmount,
                               String salesman,
                               String paymentType,
                               String dateStr
    ) {
        super();
        this.setUniqueKey(this.get_id());
        this.customer = customer;
        this.amount = ControllerUtilities.formatDecimalValue(amount);
        this.paymentType = paymentType;
        this.salesman = salesman;
        this.paidAmount = ControllerUtilities.formatDecimalValue(paidAmount);
        this.itemSolds = itemSolds;
        this.setEventDate(dateStr);
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public List<ItemSell> getItemSolds() {
        return itemSolds;
    }

    public void setItemSolds(List<ItemSell> itemSolds) {
        this.itemSolds = itemSolds;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = ControllerUtilities.formatDecimalValue(discountPercent);
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = ControllerUtilities.formatDecimalValue(discount);
    }

    public float getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(float additionalCharges) {
        this.additionalCharges = ControllerUtilities.formatDecimalValue(additionalCharges);
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = ControllerUtilities.formatDecimalValue(paidAmount);
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public static CustomerTransaction create(int i, int customer, String salesman, int itemCount, double amount, double paidAmount) {
        String currentDateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
        Date nDaysBack = ControllerUtilities.getNDaysDate(ControllerUtilities.getDateInFormat(currentDateStr), -i % 31);
        String dateStr =  ControllerUtilities.formatDateInYYYY_MM_DD(nDaysBack);
        float price = 100 + (float) ((float)((i%1000)+1)*1.3);
        int  quantity =  (i%10)+1;

        List<ItemSell> itemTransactions = new ArrayList<ItemSell>();
        ItemSell itemSell = new ItemSell("barcode_"+itemCount,quantity,price,dateStr);

        itemCount++;
        if(itemCount>10000) {
          itemCount = itemCount - 5;
        }
        ItemSell itemSell2 = new ItemSell("barcode_"+itemCount,quantity,price,dateStr);



        ItemSellDAO itemSellDAO = new ItemSellDAOImpl();

        itemTransactions.add(itemSell);
        itemTransactions.add(itemSell2);
        amount = amount + 0.2;

        CustomerTransaction customerTransaction= new CustomerTransaction("customer_"+customer, itemTransactions,amount,paidAmount, salesman,"cash",dateStr);
        itemSell.setTransactionId(customerTransaction.get_id());
        itemSell2.setTransactionId(customerTransaction.get_id());
        itemSellDAO.forceAdd(itemSell);
        itemSellDAO.forceAdd(itemSell2);

        return customerTransaction;
    }
}


