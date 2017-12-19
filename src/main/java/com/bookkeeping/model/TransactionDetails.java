package com.bookkeeping.model;

/**
 * Created by chandanm on 8/14/16.
 */

public class TransactionDetails {
String company;
    String invoiceId;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCompany() {

        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}

