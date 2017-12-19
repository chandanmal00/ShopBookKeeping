package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.ItemInventory;
import com.bookkeeping.persistence.MongoConnection;
import com.google.gson.Gson;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chandan on 6/4/16.
 */
public class ItemInventoryDAOImpl extends MongoCollectionDAOImpl<ItemInventory> implements ItemInventoryDAO {

    Gson gson;
    static final Logger logger = LoggerFactory.getLogger(ItemInventoryDAOImpl.class);


    public ItemInventoryDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_ITEM_TRANSACTION_COLLECTION);

        /*
        List<WriteModel<Document>> updates = Arrays.<WriteModel<Document>>asList(
                new InsertOneModel<Document>(
                        new Document(),                   // find part
                )
        );
        mongoCollection.bulkWrite(updates,new BulkWriteOptions().ordered(false));
        */
        gson = new Gson();
    }

    protected List<ItemInventory> getList(List<Document> documentList) {
        List<ItemInventory> ItemInventoryList = new ArrayList<ItemInventory>();
        for(Document itemDoc : documentList) {
            ItemInventory item = gson.fromJson(itemDoc.toJson(), ItemInventory.class);
            ItemInventoryList.add(item);
        }
        return ItemInventoryList;
    }



    @Override
    public ItemInventory getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), ItemInventory.class);
    }

    @Override
    public List<Document> inventoryStatus() {
        List<Document> documents = new ArrayList<Document>();
        Document doc = Document.parse("{$group:{_id: '$barcode',total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}}}})");
        documents.add(doc);
        return mongoCollection.aggregate(documents).into(new ArrayList<Document>());

    }

    public static List<Document> inventorySummaryOverallMoney() {
        Random r = new Random();
        Gson gson = new Gson();

        List<Document> documents = new ArrayList<Document>();
        Document doc = Document.parse("{$group:{'_id': \"null\",total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}}}})");
        documents.add(doc);
        logger.info(gson.toJson(doc));
       // return mongoCollection.aggregate(documents).into(new ArrayList<Document>());


        return null;

    }


    public List<Document> inventorySummaryOverall() {
        Random r = new Random();
        Gson gson = new Gson();

        String inventorySnapshot = "inventory_snapshot_"+r.nextInt(10000);
        //db.item_inventory.aggregate([{$lookup:{from:"items",localField:"barcode",foreignField:"barcode",as:"embeddedData"}},{$out:""}])

        Document doc = Document.parse("{$lookup:{from:\"items\",localField:\"barcode\",foreignField:\"barcode\",as:\"embeddedData\"}}");
        Document doc1 = Document.parse("{$out:\""+inventorySnapshot+"\"}");
        List<Document> docList = new ArrayList<Document>();
        docList.add(doc);
        docList.add(doc1);
        logger.info(gson.toJson(docList));
        mongoCollection.aggregate(docList);


logger.info("HERE");
        //{}
         String query="{$group:{_id: \"$embedded.productType\",total:{$sum:1},\"stock\":{$sum:{$cond:[{ $gt:[\"$amount\",0]},\"$amount\",0]}}, \"sold\":{$sum:{$cond:[{ $lt:[\"$amount\",0]},\"$amount\",0]}}}})";

        docList = new ArrayList<Document>();
        docList.add(Document.parse(query));

        logger.info(gson.toJson("2nd:"+docList));
        //logger.info(gson.toJson(MongoConnection.getInstance().getCollection(inventorySnapshot).aggregate(docList)));
        return null;

    }

    public List<Document> inventorySummaryByProductType() {
        return null;
    }

    public static void main(String[] args) {
       // MongoConnection.init(Constants.MONGO_PORT,Constants.MONGO_HOST,Constants.MONGO_INSTALLATION);
       // MongoDatabase db = MongoConnection.getInstance();
        //ItemInventoryDAOImpl itemInventoryDAO = new ItemInventoryDAOImpl();
        Gson gson = new Gson();
        logger.info(gson.toJson(ItemInventoryDAOImpl.inventorySummaryOverallMoney()));
    }
}
