package com.bookkeeping.TestData;

import com.bookkeeping.DAO.*;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.*;
import com.bookkeeping.persistence.MongoConnection;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by chandan on 8/7/16.
 */
public class AddSeedData {

    static final Logger logger = LoggerFactory.getLogger(ControllerUtilities.class);
    public static void main(String[] args) {


        MongoConnection.init(Constants.MONGO_PORT,Constants.MONGO_HOST,Constants.MONGO_INSTALLATION);
        int count = 300;

        CustomerDAO customerDAO = new CustomerDAOImpl();
        ItemDAO itemDAO = new ItemDAOImpl();
        CustomerPaymentDAO customerPaymentDAO = new CustomerPaymentDAOImpl();
        CustomerTransactionDAO customerTransactionDAO = new CustomerTransactionDAOImpl();
        ItemTransactionDAO itemTransactionDAO = null;
        itemTransactionDAO = new ItemTransactionDAOImpl();
      //  ItemTransactionDAO itemTransactionDAO = new ItemTransactionDAOImpl();
        CompanyDAO companyDAO = new CompanyDAOImpl();

        SalesmanDAO salesmanDAO = new SalesmanDAOImpl();

        Random random = new Random();
        Set<String> companySet = new TreeSet<String>();

        String currentDateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();

        for(int i=0;i<count;i++) {
            Date nDaysBack = ControllerUtilities.getNDaysDate(ControllerUtilities.getDateInFormat(currentDateStr),-i%31);
            String dateStr =  ControllerUtilities.formatDateInYYYY_MM_DD(nDaysBack);
            int r = random.nextInt(500);
            Item item = Item.createItem(i,r,dateStr);

            Company company = Company.create(r);

            if(!companySet.contains(company.getUniqueKey())) {
                companyDAO.forceAdd(company);
                companySet.add(company.getUniqueKey());
            }

            itemDAO.forceAdd(item);
            float price =  100 + (i%1000)+1;
            int  quantity =  (i%30)+1;
            ItemBuy itemBuy = new ItemBuy(item.getBarcode(),quantity,price,company.getNickName(),dateStr);
            ItemTransaction itemTransaction = itemBuy;
            itemTransactionDAO.forceAdd(itemTransaction);
        }

        logger.info("Items added:"+count);

        Set<String> salesManSet = new HashSet<String>();
        int customerId = 0;
        for(int cnt=400;cnt<500;cnt++) {
            Gson gson = new Gson();
            customerId = cnt;
            Customer customer = Customer.create(customerId);
            //System.out.println(String.format("Created customer:{} with value:{}",cnt,gson.toJson(customer)));
            customerDAO.forceAdd(customer);

            for(int j=0;j<500;j++) {


                double amount = Math.min(1000, (cnt + 1) * 100);
                CustomerPayment customerPayment = CustomerPayment.create(j,cnt,random.nextInt((int)amount));
                if(j%3==0) {
                    customerPayment.setPaymentType("cash");
                } else {
                    if(j%2==0) {
                        customerPayment.setPaymentType("credit");
                    }
                }
                customerPaymentDAO.forceAdd(customerPayment);
            }
            for(int j=0;j<500;j++) {
                int r = random.nextInt(count-3) % 3000;
                Salesman salesman1 = Salesman.create(r);
                if(!salesManSet.contains(salesman1.getUniqueKey())) {
                    //salesmanDAO.forceAdd(salesman1);
                    salesmanDAO.add(salesman1);
                    salesManSet.add(salesman1.getUniqueKey());
                }
                double amount = Math.min(1000, (cnt + 1) * 100);

                CustomerTransaction customerTransaction = CustomerTransaction.create(j,cnt,salesman1.getUniqueKey(),r,random.nextInt((int) amount), Math.max(random.nextInt((int)amount) -100, 50));
                customerTransactionDAO.forceAdd(customerTransaction);
            }
            logger.info("Done with Customer:{}",customerId);


        }


    }
}
