package com.bookkeeping.DAO;

import com.bookkeeping.model.ItemInventory;
import org.bson.Document;

import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public interface ItemInventoryDAO extends MongoCollectionDAO<ItemInventory> {

    public List<Document> inventoryStatus();
    public List<Document> inventorySummaryOverall();
    public List<Document> inventorySummaryByProductType();

}
