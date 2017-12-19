package com.bookkeeping.factory;

import com.bookkeeping.DAO.*;
import com.bookkeeping.constants.Constants;
import com.bookkeeping.controller.SingletonManagerDAO;
import com.bookkeeping.model.MongoCollectionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chandan on 6/21/16.
 */
public class factoryDAO {

    static final Logger logger = LoggerFactory.getLogger(factoryDAO.class);

    public static MongoCollectionResponse getDAO(String entity) {


        MongoCollectionResponse mongoCollectionResponse = new MongoCollectionResponse();
        mongoCollectionResponse.setMongoCollectionDAO(null);
        mongoCollectionResponse.setEntity(entity);

        if (entity.equals("customer")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getCustomerDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_CUSTOMER);

        } else if (entity.equals(Constants.ENTITY_ITEM)) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getItemDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_ITEM);

        } else if (entity.equals("itemSold")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getItemSoldDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_ITEM_SOLD);

        } else if (entity.equals("itemInventory")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getItemInventoryDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_ITEM_INVENTORY);

        } else if (entity.equals(Constants.ENTITY_CUSTOMER_TRANSACTION)) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getCustomerTransactionDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_CUSTOMER_TRANSACTION);

        } else if (entity.equals("salesman")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getSalesmanDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_SALESMAN);

        } else if (entity.equals("company")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getCompanyDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_COMPANY);

        } else if (entity.equals("payment")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getCustomerPaymentDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_CUSTOMER_PAYMENT);

        } else if (entity.equals("backup")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getBackupDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_BACKUP);

        }  else if (entity.equals("itemTransaction")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getItemTransactionDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_ITEM_TRANSACTION);

        }  else if (entity.equals("itemSell")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getItemTransactionDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_ITEM_SELL);

        }  else if (entity.equals("itemBuy")) {
            mongoCollectionResponse.setMongoCollectionDAO(SingletonManagerDAO.getInstance().getItemTransactionDAO());
            mongoCollectionResponse.setEntity(Constants.ENTITY_ITEM_BUY);

        }else {
            logger.error("This should not happen, input entity:{}", entity);

        }
        return mongoCollectionResponse;
    }

}
