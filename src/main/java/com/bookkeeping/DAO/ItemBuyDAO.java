package com.bookkeeping.DAO;

import com.bookkeeping.model.ItemBuy;
import com.bookkeeping.model.ItemTransaction;
import org.bson.Document;

import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public interface ItemBuyDAO extends MongoCollectionDAO<ItemBuy> {

    public List<Document> inventoryStatus(String entity, String key);
    public List<Document> inventorySummaryOverall(String entity, String key);
    public List<Document> inventorySummaryByProductType(String entity, String key);

}
