package com.bookkeeping.controller;

import com.bookkeeping.DAO.*;
import com.bookkeeping.model.*;

/**
 * Created by chandan on 6/21/16.
 */
public class SingletonManagerDAO {

    private static volatile SingletonManagerDAO instance;
    private SessionDAO sessionDAO;
    private CustomerDAO customerDAO;
    private ItemSoldDAO itemSoldDAO;
    private ItemDAO itemDAO;
    private ItemInventoryDAO itemInventoryDAO;
    private CustomerTransactionDAO customerTransactionDAO;
    private SalesmanDAO salesmanDAO;
    private CompanyDAO companyDAO;
    private UserDAO userDAO;
    private CustomerPaymentDAO customerPaymentDAO;
    private BackupDAO backupDAO;
    private ItemTransactionDAO itemTransactionDAO;
    private ItemSellDAO itemSellDAO;
    private ItemBuyDAO itemBuyDAO;

    private SingletonManagerDAO() {

        sessionDAO = new SessionDAO();
        customerDAO = new CustomerDAOImpl();
        itemDAO = new ItemDAOImpl();
        itemInventoryDAO = new ItemInventoryDAOImpl();
        customerTransactionDAO = new CustomerTransactionDAOImpl();
        companyDAO = new CompanyDAOImpl();
        salesmanDAO = new SalesmanDAOImpl();
        userDAO = new UserDAO();
        itemSoldDAO = new ItemSoldDAOImpl();
        customerPaymentDAO = new CustomerPaymentDAOImpl();
        backupDAO = new BackupDAOImpl();
        itemTransactionDAO = new ItemTransactionDAOImpl();
        itemSellDAO = new ItemSellDAOImpl();
        itemBuyDAO = new ItemBuyDAOImpl();
    }

    public static SingletonManagerDAO getInstance() {
        if(instance==null) {
            synchronized (SingletonManagerDAO.class) {
                if(instance==null) {
                    instance = new SingletonManagerDAO();
                }
            }
        }
        return instance;

    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public ItemSoldDAO getItemSoldDAO() {
        return itemSoldDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public ItemInventoryDAO getItemInventoryDAO() {
        return itemInventoryDAO;
    }

    public CustomerTransactionDAO getCustomerTransactionDAO() {
        return customerTransactionDAO;
    }

    public SalesmanDAO getSalesmanDAO() {
        return salesmanDAO;
    }

    public CompanyDAO getCompanyDAO() {
        return companyDAO;
    }

    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public CustomerPaymentDAO getCustomerPaymentDAO() {
        return customerPaymentDAO;
    }

    public BackupDAO getBackupDAO() {
        return backupDAO;
    }

    public ItemTransactionDAO getItemTransactionDAO() {
        return itemTransactionDAO;
    }

    public ItemSellDAO getItemSellDAO() {
        return itemSellDAO;
    }

    public ItemBuyDAO getItemBuyDAO() {
        return itemBuyDAO;
    }
}
