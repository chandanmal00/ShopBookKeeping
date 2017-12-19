package com.bookkeeping.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.util.List;

import static spark.Spark.get;

/**
 * Created by chandan on 6/20/16.
 */
public class TransactionItemController {

    /*
    TransactionItemDAO transactionItemDAO;
    static final Logger logger = LoggerFactory.getLogger(TransactionItemController.class);
    public TransactionItemController() {
        transactionItemDAO = new TransactionItemDAOImpl();
    }

    public void initializeRoutes() throws IOException {
        // used to display Khareeddar Detail Page



        get(new Route("/listItems") {
            @Override
            public Object handle(Request request, Response response) {
                List<TransactionItem> transactionItemList = transactionItemDAO.list();
                Gson gson = new Gson();
                return gson.toJson(transactionItemList);
            }
        });

        get(new Route("/listItemKeys") {
            @Override
            public Object handle(Request request, Response response) {
                List<String> list = transactionItemDAO.listKeys();
                Gson gson = new Gson();
                return gson.toJson(list);
            }
        });

    }
*/
}
