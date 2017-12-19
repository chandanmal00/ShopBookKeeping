package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.ItemTransaction;
import com.bookkeeping.persistence.MongoConnection;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by chandan on 6/4/16.
 */
public class ItemTransactionDAOImpl extends MongoCollectionDAOImpl<ItemTransaction> implements ItemTransactionDAO {

    Gson gson;
    static final Logger logger = LoggerFactory.getLogger(ItemTransactionDAOImpl.class);


    public ItemTransactionDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_ITEM_TRANSACTION_COLLECTION);
        gson = new Gson();
    }

    protected List<ItemTransaction> getList(List<Document> documentList) {
        List<ItemTransaction> ItemTransactionList = new ArrayList<ItemTransaction>();
        for(Document itemDoc : documentList) {
            ItemTransaction item = gson.fromJson(itemDoc.toJson(), ItemTransaction.class);
            ItemTransactionList.add(item);
        }
        return ItemTransactionList;
    }



    @Override
    public ItemTransaction getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), ItemTransaction.class);
    }

    @Override
    public List<Document> inventoryStatus() {
        List<Document> documents = new ArrayList<Document>();
        Document doc = Document.parse("{$group:{_id: '$barcode',total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}}}})");
        Document doc1= Document.parse("{}");
        Document doc2= Document.parse("{}");
        documents.add(doc);
        documents.add(doc2);
        return mongoCollection.aggregate(documents).into(new ArrayList<Document>());

    }

    public List<Document> inventoryStatus(int page) {
        List<Document> documents = new ArrayList<Document>();
        int count =  page*Constants.MAX_ROWS;
        Gson gson = new Gson();
        Document doc = Document.parse("{$group:{_id: '$barcode',total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}},'totalCost':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'totalRevenue':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}},'avgPriceSold':{$avg:{$cond:[{ $lt:['$quantity',0]},'$price',0]}}}})");
        Document doc1= Document.parse("{$skip : "+count+"}");
        Document doc2= Document.parse("{$limit:"+Constants.MAX_ROWS+"}");
        documents.add(doc);
        documents.add(doc1);
        documents.add(doc2);
        //logger.info("doc:" + gson.toJson(documents));
        return mongoCollection.aggregate(documents).into(new ArrayList<Document>());

    }

    @Override
    public List<Document> inventoryStatus(String entity, String key) {
        List<Document> documents = new ArrayList<Document>();

        Document matchDocFilter = new Document();
        if(entity.equals("item")) {
            matchDocFilter.append("barcode", key);
        } else {
            matchDocFilter.append(entity, key);
        }

        logger.info("Match doc is:{}", matchDocFilter);
        Document matchDoc = new Document();
        //matchDoc.append("$match",new Document("creationTime", filter));
        matchDoc.append("$match", matchDocFilter);
        Document doc = Document.parse("{$group:{_id: '$barcode',total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}},'totalCost':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'totalRevenue':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}},'avgPriceSold':{$avg:{$cond:[{ $lt:['$quantity',0]},'$price',0]}}}})");

        documents.add(matchDoc);
        documents.add(doc);
        logger.info(gson.toJson(documents));
        return mongoCollection.aggregate(documents).into(new ArrayList<Document>());

    }

    @Override
    public List<ItemTransaction> inventoryDetails(String entity, String key) {
        List<Document> documents = new ArrayList<Document>();

        Document matchDocFilter = new Document();
        if(entity.equals("item")) {
            matchDocFilter.append("barcode", key);
        } else {
            matchDocFilter.append(entity, key);
        }

        logger.info("Match doc is:{}", matchDocFilter);

        return getList(mongoCollection.find(matchDocFilter).into(new ArrayList<Document>()));

    }

    @Override
    public List<ItemTransaction> inventoryDetailsWithLimit(String entity, String key) {
        List<Document> documents = new ArrayList<Document>();

        Document matchDocFilter = new Document();
        if(entity.equals("item")) {
            matchDocFilter.append("barcode", key);
        } else {
            matchDocFilter.append(entity, key);
        }

        logger.info("Match doc is:{}", matchDocFilter);

        return getList(mongoCollection.find(matchDocFilter).limit(Constants.LIMIT_ROWS).into(new ArrayList<Document>()));

    }
    /**
     * We do not need this as the above method does it
     * @param entity
     * @param key
     * @param limit
     * @return
     */
    public List<ItemTransaction> rawTransactions(String entity, String key, int limit) {
        return new ArrayList<ItemTransaction>();

    }
    public List<Document> inventorySummaryOverallMoney() {

        List<Document> documents = new ArrayList<Document>();
        //Document doc = Document.parse("{$group:{'_id': \"null\",total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}}}})");
        Document doc = Document.parse("{$group:{'_id': \"null\",totalItemTransactions:{$sum:1}, 'totalItemBuyQuantity':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'totalItemSellQuantity':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}}, 'totalItemBuyAmount':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'totalItemSellAmount':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}}}})");
        documents.add(doc);
        logger.info(gson.toJson(doc));
        return mongoCollection.aggregate(documents).into(new ArrayList<Document>());

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


    public Document itemTransactionSummary() {
        List<Document> documents = new ArrayList<Document>();
        //Document doc = Document.parse("{$group:{'_id': \"null\",total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}}}})");
        Document doc = Document.parse("{$group:{'_id': \"null\",totalItemTransactions:{$sum:1}, 'totalItemBuyQuantity':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'totalItemSellQuantity':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}}, 'totalItemBuyAmount':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'totalItemSellAmount':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}}}})");
        documents.add(doc);
        return mongoCollection.aggregate(documents).first();
    }

    public List<Document> itemSellSummary() {
        List<Document> documents = new ArrayList<Document>();
        Document doc = Document.parse("{$group:{'_id': \"null\",totalItemTransactions:{$sum:1}, 'totalItemBuys':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'totalItemSells':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}}, 'totalItemBuyAmount':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'totalItemSellAmount':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}}}})");
        documents.add(doc);
        return mongoCollection.aggregate(documents).into(new ArrayList<Document>());
    }

    public List<Document> itemBuySummary() {
        List<Document> documents = new ArrayList<Document>();
        Document doc = Document.parse("{$group:{'_id': \"null\",total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}}}})");
        documents.add(doc);
        return mongoCollection.aggregate(documents).into(new ArrayList<Document>());
    }

    public List<Document> inventorySummaryByProductType() {
        return null;
    }

    public List<Document> inventoryListSummaryGt(int n, int limit) {


        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        Date fromDate = ControllerUtilities.getNDaysDateFromCurrentDate(-n);
        String toDateStr = dateFormat.format(fromDate);

        logger.info("to date:{}",toDateStr);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
        //DateFormat dateFormatShort = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);

        try {
            //Bson filter = new Document("$lte", toDateStr+" 23:59:59");
            Bson filter = new Document("$lte", toDateStr);
            logger.info("Filter:{}", filter);


            Document matchDocFilter = new Document();
            matchDocFilter.append("eventDate", filter).append("itemTransactionType", "BUY");

            logger.info("Match doc is:{}", matchDocFilter);
            Document matchDoc = new Document();
            //matchDoc.append("$match",new Document("creationTime", filter));
            matchDoc.append("$match",matchDocFilter);

            List<Document> documents = new ArrayList<Document>();
            //  int count =  page*Constants.MAX_ROWS;
            Gson gson = new Gson();
            Document doc = Document.parse("{$group:{_id: '$barcode',total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}},'totalCost':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'totalRevenue':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}},'avgPriceSold':{$avg:{$cond:[{ $lt:['$quantity',0]},'$price',0]}}}})");
            //Document doc1= Document.parse("{$skip : "+count+"}");
            // Document doc2= Document.parse("{$limit:"+Constants.MAX_ROWS+"}");
            documents.add(matchDoc);
            documents.add(doc);
            documents.add(Document.parse("{\"$project\":{stock:1, total:1, totalCost: 1, totalRevenue:1, avgPriceSold:1, sold:1,diff:{$subtract:[\"$stock\",\"$sold\"]}}},{\"$match\":{diff:{\"$gt\":0}}}"));
            //Document filterGroupByDoc = Document.parse( "{if:{$gt :[\"$stock\",\"$sold\"]}}");
            //Document filterGroupByDoc = Document.parse( "{stock gt sold }");
            // documents.add(doc1);
            //documents.add(doc2);
            // Document filterGroupMatchDoc = new Document().append("$match", filterGroupByDoc);
            //documents.add(filterGroupMatchDoc);


            //project: {"$project":{stock:1, sold:1,diff:{$subtract:["$stock","$sold"]}}}
            //project: {"$project":{stock:1, totalCost: 1, totalRevenue:1, avgPriceSold:1, sold:1,diff:{$subtract:["$stock","$sold"]}}}
            //{"$match":{stock:{"$gt":27}}}
            documents.add(Document.parse("{$limit:"+limit+"}"));
            logger.info(gson.toJson(documents));
            List<Document> documents_returned = mongoCollection.aggregate(documents).into(new ArrayList<Document>());

            /*
            Document doc = new Document("creationTime",filter);//new Document("creationTime",filter);
            System.out.println(doc.toJson());
            */
            //List<Document> documents = mongoCollection.find(doc).into(new ArrayList<Document>());
            //logger.info(gson.toJson(documents_returned));
            return documents_returned;

        }catch(Exception e) {
            logger.error("Error in parsing date:",e);
            return new ArrayList<Document>();
        }

    }

    public List<Document> inventoryListSummaryGt(int n) {


        DateFormat dateFormatInput = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        Date fromDate = ControllerUtilities.getNDaysDateFromCurrentDate(-n);
        String toDateStr = dateFormatInput.format(fromDate);

        logger.info("to date:{}",toDateStr);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
        //DateFormat dateFormatShort = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);

        try {
            //Bson filter = new Document("$lte", toDateStr+" 23:59:59");
            Bson filter = new Document("$lte", toDateStr);
            logger.info("Filter:{}", filter);


            Document matchDocFilter = new Document();
            matchDocFilter.append("eventDate", filter).append("itemTransactionType", "BUY");

            logger.info("Match doc is:{}", matchDocFilter);
            Document matchDoc = new Document();
            //matchDoc.append("$match",new Document("creationTime", filter));
            matchDoc.append("$match",matchDocFilter);

            List<Document> documents = new ArrayList<Document>();
          //  int count =  page*Constants.MAX_ROWS;
            Gson gson = new Gson();
            Document doc = Document.parse("{$group:{_id: '$barcode',total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}},'totalCost':{$sum:{$cond:[{ $gt:['$quantity',0]},'$amount',0]}}, 'totalRevenue':{$sum:{$cond:[{ $lt:['$quantity',0]},'$amount',0]}},'avgPriceSold':{$avg:{$cond:[{ $lt:['$quantity',0]},'$price',0]}}}})");
            //Document doc1= Document.parse("{$skip : "+count+"}");
           // Document doc2= Document.parse("{$limit:"+Constants.MAX_ROWS+"}");
            documents.add(matchDoc);
            documents.add(doc);
            documents.add(Document.parse("{\"$project\":{stock:1, total:1, totalCost: 1, totalRevenue:1, avgPriceSold:1, sold:1,diff:{$subtract:[\"$stock\",\"$sold\"]}}},{\"$match\":{diff:{\"$gt\":0}}}"));
            //Document filterGroupByDoc = Document.parse( "{if:{$gt :[\"$stock\",\"$sold\"]}}");
            //Document filterGroupByDoc = Document.parse( "{stock gt sold }");
           // documents.add(doc1);
            //documents.add(doc2);
           // Document filterGroupMatchDoc = new Document().append("$match", filterGroupByDoc);
            //documents.add(filterGroupMatchDoc);


            //project: {"$project":{stock:1, sold:1,diff:{$subtract:["$stock","$sold"]}}}
            //project: {"$project":{stock:1, totalCost: 1, totalRevenue:1, avgPriceSold:1, sold:1,diff:{$subtract:["$stock","$sold"]}}}
            //{"$match":{stock:{"$gt":27}}}
            logger.info(gson.toJson(documents));
            List<Document> documents_returned = mongoCollection.aggregate(documents).into(new ArrayList<Document>());

            /*
            Document doc = new Document("creationTime",filter);//new Document("creationTime",filter);
            System.out.println(doc.toJson());
            */
            //List<Document> documents = mongoCollection.find(doc).into(new ArrayList<Document>());
            //logger.info(gson.toJson(documents_returned));
            return documents_returned;

        }catch(Exception e) {
            logger.error("Error in parsing date:",e);
            return new ArrayList<Document>();
        }

    }

    public static void main(String[] args) {
        MongoConnection.init(Constants.MONGO_PORT,Constants.MONGO_HOST,Constants.MONGO_INSTALLATION);
        //MongoDatabase db = MongoConnection.getInstance();
        ItemTransactionDAO itemInventoryDAO = new ItemTransactionDAOImpl();
        Gson gson = new Gson();
        logger.info(gson.toJson(itemInventoryDAO.inventoryDetails("item", "barcode_0")));
    }
}
