package com.bookkeeping.DAO;

import com.bookkeeping.model.ItemInventory;
import com.bookkeeping.model.ItemTransaction;
import org.bson.Document;

import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public interface ItemTransactionDAO extends MongoCollectionDAO<ItemTransaction> {

    public List<Document> inventoryStatus();
    public List<Document> inventoryStatus(int page);
    public List<Document> inventoryStatus(String entity, String value);
    public List<ItemTransaction> inventoryDetails(String entity, String key);
    public List<ItemTransaction> inventoryDetailsWithLimit(String entity, String key);
    public List<Document> inventorySummaryOverall();
    public Document itemTransactionSummary();
    public List<Document> inventorySummaryOverallMoney();
    public List<Document> inventorySummaryByProductType();
    //get inventory which has items more than x days, so we can do markdowns
    public List<Document> inventoryListSummaryGt(int n);
    public List<Document> inventoryListSummaryGt(int n, int limit);

}
