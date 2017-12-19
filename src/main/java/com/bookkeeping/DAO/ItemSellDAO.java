package com.bookkeeping.DAO;

import com.bookkeeping.model.ItemSell;
import com.bookkeeping.model.ItemTransaction;
import org.bson.Document;

import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public interface ItemSellDAO extends MongoCollectionDAO<ItemSell> {

    public List<Document> inventoryStatus();
    public List<Document> inventorySummaryOverall();
    public List<Document> inventorySummaryByProductType();

}
