package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.controller.SingletonManagerDAO;
import com.bookkeeping.model.InventoryObj;
import com.bookkeeping.persistence.MongoConnection;
import com.bookkeeping.utilities.ControllerUtilities;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.Doc;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by chandan on 6/18/16.
 */
public abstract class MongoCollectionDAOImpl<T extends InventoryObj> {
    MongoCollection<Document> mongoCollection;
    Gson gson = new Gson();
    static final Logger logger = LoggerFactory.getLogger(MongoCollectionDAOImpl.class);


    public long count() {
        return mongoCollection.count();
    }
    public void add(T o) {

        if(!exists(o)) {
            //o.setCreatedBy(SingletonManagerDAO.getInstance().getSessionDAO().findUserNameBySessionId(ControllerUtilities.getSessionCookie(request)));
            mongoCollection.insertOne(getDocument(o));
            logger.info("Added document:{}",getJsonString(o));
        } else {
            logger.error("Document already exist, doc:{}",getJsonString(o));

        }
    }

    public void add(List<T> oList) {

        for(T o : oList) {
            if (!exists(o)) {
                mongoCollection.insertOne(getDocument(o));
                logger.info("Added document:{}", getJsonString(o));
            } else {
                logger.error("Document already exist, doc:{}", getJsonString(o));

            }
        }
    }


    public boolean exists(T o) {
        logger.info("Checking: {}", uniqueDocument(o).toJson());
        Document doc = mongoCollection.find(uniqueDocument(o)).first();
        return (doc!=null);
    }


    public void forceAdd(T o) {
        mongoCollection.insertOne(getDocument(o));
        //logger.info("Added document forcefully:{}",getJsonString(o));

    }


    public void remove(T o) {
        mongoCollection.deleteOne(uniqueDocument(o));
        logger.info("Removed document:{}", getJsonString(o));
    }

    public void remove(String uniqueKey) {
        mongoCollection.deleteOne(this.getUniqueDocument(uniqueKey));
        logger.info("Removed document if exists with uniqueKey:{}",uniqueKey);
    }


    public void remove(List<T> oList) {
        for(T o: oList) {
            mongoCollection.deleteOne(uniqueDocument(o));
            logger.info("Removed document:{}", getJsonString(o));
        }
    }

    protected Document getUniqueDocument(String uniqueKey) {
        return new Document().append(Constants.UNIQUE_KEY,uniqueKey);
    }

    protected Document getIdDocument(String id) {
        return new Document().append(Constants.ID_KEY, id);
    }


    public T getBasedOnUniqueKey(String uniqueKey) {
        Document doc = mongoCollection.find(this.getUniqueDocument(uniqueKey)).first();
        if(doc!=null) {
            return getTargetObjBasedOnUniqueKey(doc);
        }
        return null;
    }

    public T getBasedOnId(String id) {
        Document doc = mongoCollection.find(this.getIdDocument(id)).first();
        if(doc!=null) {
            return getTargetObjBasedOnUniqueKey(doc);
        }
        return null;
    }

    public List<Document> getDailySummaryNDatesEndingToday(int n) {

        return this.getDailySummaryNDatesEnding(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD(), n);

    }
    public List<Document> getDailySummaryForWeekEnding(String toDateStr) {
        return getDailySummaryNDatesEnding(toDateStr, -7);
    }

    public List<Document> getDailySummaryFor30daysEnding(String toDateStr) {
        return getDailySummaryNDatesEnding(toDateStr, -30);
    }


    public List<Document> getMonthlySummaryEndingToday(int n) {
        return this.getMonthlySummary(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD(), n);
    }
        public List<Document> getMonthlySummary(String toDateStr, int n) {


        DateFormat dateFormatInput = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM);
        Date toDate  = ControllerUtilities.getDateInFormat(toDateStr);
        if(toDate==null) {
            return new ArrayList<Document>();
        }

        Date fromDate = ControllerUtilities.getNMonthssDate(toDate, n);
        String fromDateStr = dateFormatInput.format(fromDate);

        logger.info("from:{}, to:{}",fromDateStr,toDateStr);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        //DateFormat dateFormatShort = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);

        try {
            //Bson filter = new Document("$gte", dateFormat.format(fromDate)).append("$lte", toDateStr+" 23:59:59");
            Bson filter = new Document("$gte", dateFormat.format(fromDate)).append("$lte", toDateStr);
            logger.info("Filter:{}", filter);

            Document matchDoc = new Document();
            //matchDoc.append("$match",new Document("creationTime", filter));
            matchDoc.append("$match",new Document("eventDate", filter));
            Document groupBy = new Document();
            groupBy.append("$group", Document.parse("{_id:{ $substr:[\"$eventDate\",0,7]},amountKhareeddar:{$sum:\"$amountKhareeddar\"} ,amount:{$sum:\"$amount\"}}"));
            //System.out.println(groupBy.toJson());
            Document sortBy = new Document().append("$sort",Document.parse("{_id:1}"));
            List<Document> list = new ArrayList<Document>();
            list.add(matchDoc);
            list.add(groupBy);
            list.add(sortBy);
            logger.info(gson.toJson(list));

            List<Document> documents = mongoCollection.aggregate(list).into(new ArrayList<Document>());

            /*
            Document doc = new Document("creationTime",filter);//new Document("creationTime",filter);
            System.out.println(doc.toJson());
            */
            //List<Document> documents = mongoCollection.find(doc).into(new ArrayList<Document>());
            return documents;

        }catch(Exception e) {
            logger.error("Error in parsing date:",e);
            return new ArrayList<Document>();
        }
    }


    public List<Document> getDailySummaryNDatesEnding(String toDateStr, int n) {


        DateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd");
        Date toDate  = ControllerUtilities.getDateInFormat(toDateStr);
        if(toDate==null) {
            return new ArrayList<Document>();
        }

        Date fromDate = ControllerUtilities.getNDaysDate(toDate, n);
        String fromDateStr = dateFormatInput.format(fromDate);

        logger.info("from:{}, to:{}",fromDateStr,toDateStr);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        //DateFormat dateFormatShort = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);

        try {
           // Bson filter = new Document("$gte", dateFormat.format(fromDate)).append("$lte", toDateStr+" 23:59:59");
            Bson filter = new Document("$gte", dateFormat.format(fromDate)).append("$lte", toDateStr);
            logger.info("Filter:{}", filter);

            Document matchDoc = new Document();
            matchDoc.append("$match", new Document("eventDate", filter));
            Document groupBy = new Document();
            groupBy.append("$group", Document.parse("{_id:\"$eventDate\",amountKhareeddar:{$sum:\"$amountKhareeddar\"} ,amount:{$sum:\"$amount\"}}"));
            Document sortBy = new Document().append("$sort", Document.parse("{_id:1}"));
            List<Document> list = new ArrayList<Document>();
            list.add(matchDoc);
            list.add(groupBy);
            list.add(sortBy);

            List<Document> documents = mongoCollection.aggregate(list).into(new ArrayList<Document>());

            /*
            Document doc = new Document("creationTime",filter);//new Document("creationTime",filter);
            System.out.println(doc.toJson());
            */
            //List<Document> documents = mongoCollection.find(doc).into(new ArrayList<Document>());
            return documents;

        }catch(Exception e) {
            logger.error("Error in parsing date:",e);
            return new ArrayList<Document>();
        }
    }



    public void removeBasedOnId(String id) {
        mongoCollection.deleteOne(this.getIdDocument(id));
    }


    public List<T> list() {
        return getList(mongoCollection.find().maxTime(5, TimeUnit.SECONDS).into(new ArrayList<Document>()));
    }

    public List<T> list(Document filter) {
        return getList(mongoCollection.find(filter).into(new ArrayList<Document>()));
    }

    public List<T> list(Document filter, int limit) {
        return getList(mongoCollection.find(filter).sort(new Document("creationTime", -1)).limit(limit).into(new ArrayList<Document>()));
    }

    public List<T> list(int limit) {

        return getList(mongoCollection.find().sort(new Document("creationTime", -1)).limit(limit).into(new ArrayList<Document>()));
    }

    public Map<String,T> keyMap() {
        List<T> list = getList(mongoCollection.find().into(new ArrayList<Document>()));
        Map<String,T> keyMap = new HashMap<String, T>();
        for(T o: list) {
            keyMap.put(o.getUniqueKey(), o);
        }
        return keyMap;

    }

    public List<T> search(String search) {
        String searchString = "{ $text: { $search:\""+search+"\" } }";
        logger.info("SearchString:"+searchString);
        return getList(mongoCollection.find(Document.parse(searchString)).into(new ArrayList<Document>()));

    }

    public List<T> search(String search, int limit) {
        String searchString = "{ $text: { $search:\""+search+"\" } }";
        logger.info("SearchString:"+searchString);
        return getList(mongoCollection.find(Document.parse(searchString)).limit(limit).into(new ArrayList<Document>()));

    }

    public List<String> listKeys() {

        List<String> keys = new ArrayList<String>();
        List<T> list = this.list();
        for(T inventoryObj : list) {
            keys.add(inventoryObj.getUniqueKey());
        }
        return keys;
    }


    public Document uniqueDocument(T o) {
        return new Document()
                .append(Constants.UNIQUE_KEY,
                        o.getUniqueKey());
    }


    public Document getDocument(T o) {
        return Document.parse(gson.toJson(o));
    }


    public String getJsonString(T o) {
        return gson.toJson(o);
    }


    protected abstract List<T> getList(List<Document> into);

    public abstract T getTargetObjBasedOnUniqueKey(Document doc);
    public void update(T o) {

        if(!exists(o)) {
            mongoCollection.insertOne(getDocument(o));
            logger.info("Added document as it does not exists, doc:{}",getJsonString(o));
        } else {
            logger.info(gson.toJson(o));
            mongoCollection.replaceOne(this.uniqueDocument(o),this.getDocument(o));
            logger.error("Document exists, so updating, doc:{}",getJsonString(o));

        }
    }

    public List<Document> getEntityDailySummaryNDatesEndingToday(int n,String uniqueKey, String entityType) {
        return this.getEntityDailySummaryNDatesEnding(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD(),n, uniqueKey, entityType);
    }

    public List<Document> getEntityDailySummaryNDatesEnding(String toDateStr, int n,String uniqueKey, String entityType) {


        DateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd");
        Date toDate  = ControllerUtilities.getDateInFormat(toDateStr);
        if(toDate==null) {
            return new ArrayList<Document>();
        }

        Date fromDate = ControllerUtilities.getNDaysDate(toDate, n);
        String fromDateStr = dateFormatInput.format(fromDate);

        logger.info("from:{}, to:{}",fromDateStr,toDateStr);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);

        try {
            //Bson filter = new Document("$gte", dateFormat.format(fromDate)).append("$lte", toDateStr+" 23:59:59");
            Bson filter = new Document("$gte", dateFormat.format(fromDate)).append("$lte", toDateStr);
            logger.info("Filter:{}", filter);


            Document matchDocFilter = new Document();
            //matchDocFilter.append("creationTime", filter);
            matchDocFilter.append("eventDate", filter);
            if(uniqueKey!=null) {
                // {"kisaan.nickName":""+uniqueKey+"\"}";
                //matchDocFilter.append(entityType+".uniqueKey",uniqueKey);
                //matchDocFilter.append(entityType+".uniqueKey",uniqueKey);
                matchDocFilter.append(entityType,uniqueKey);
            }

            logger.info("Match doc is:{}", matchDocFilter);
            Document matchDoc = new Document();
            //matchDoc.append("$match",new Document("creationTime", filter));
            matchDoc.append("$match",matchDocFilter);
            Document groupBy = new Document();
            groupBy.append("$group", Document.parse("{_id:\"$eventDate\",amountKhareeddar:{$sum:\"$amountKhareeddar\"} ,amount:{$sum:\"$amount\"}}"));
            Document sortBy = new Document().append("$sort", Document.parse("{_id:1}"));
            List<Document> list = new ArrayList<Document>();
            list.add(matchDoc);
            list.add(groupBy);
            list.add(sortBy);

            List<Document> documents = mongoCollection.aggregate(list).into(new ArrayList<Document>());

            /*
            Document doc = new Document("creationTime",filter);//new Document("creationTime",filter);
            System.out.println(doc.toJson());
            */
            //List<Document> documents = mongoCollection.find(doc).into(new ArrayList<Document>());
            logger.info(gson.toJson(documents));
            return documents;

        }catch(Exception e) {
            logger.error("Error in parsing date:",e);
            return new ArrayList<Document>();
        }
    }

    public List<Document> getEntityMonthlySummary(String toDateStr, int n,String uniqueKey, String entityType) {


        DateFormat dateFormatInput = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM);
        Date toDate  = ControllerUtilities.getDateInFormat(toDateStr);
        if(toDate==null) {
            return new ArrayList<Document>();
        }

        Date fromDate = ControllerUtilities.getNMonthssDate(toDate, n);
        String fromDateStr = dateFormatInput.format(fromDate);

        logger.info("from:{}, to:{}",fromDateStr,toDateStr);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD_FULL);
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        //DateFormat dateFormatShort = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);

        try {
            //Bson filter = new Document("$gte", dateFormat.format(fromDate)).append("$lte", toDateStr+" 23:59:59");
            Bson filter = new Document("$gte", dateFormat.format(fromDate)).append("$lte", toDateStr);

            Document matchDocFilter = new Document();
            //matchDocFilter.append("creationTime", filter);
            matchDocFilter.append("eventDate", filter);
            if(uniqueKey!=null) {
                // {"kisaan.nickName":""+uniqueKey+"\"}";
                //matchDocFilter.append(entityType+".uniqueKey",uniqueKey);
                matchDocFilter.append(entityType,uniqueKey);
            }

            logger.info("Match doc is:{}", matchDocFilter);

            Document matchDoc = new Document();
            matchDoc.append("$match",matchDocFilter);
            Document groupBy = new Document();
            groupBy.append("$group", Document.parse("{_id:{ $substr:[\"$eventDate\",0,7]},amountKhareeddar:{$sum:\"$amountKhareeddar\"} ,amount:{$sum:\"$amount\"}}"));
            Document sortBy = new Document().append("$sort", Document.parse("{_id:1}"));
            //System.out.println(groupBy.toJson());
            List<Document> list = new ArrayList<Document>();
            list.add(matchDoc);
            list.add(groupBy);
            list.add(sortBy);

            List<Document> documents = mongoCollection.aggregate(list).into(new ArrayList<Document>());

            /*
            Document doc = new Document("creationTime",filter);//new Document("creationTime",filter);
            System.out.println(doc.toJson());
            */
            //List<Document> documents = mongoCollection.find(doc).into(new ArrayList<Document>());
            logger.info(gson.toJson(documents));
            return documents;

        }catch(Exception e) {
            logger.error("Error in parsing date:",e);
            return new ArrayList<Document>();
        }
    }


    public List<Document> getEntityMonthlySummaryEndingToday(int n,String uniqueKey, String entityType) {
        return this.getEntityMonthlySummary(ControllerUtilities.getCurrentDateStrInYYYY_MM_DD(), n, uniqueKey, entityType);
    }

    public List<T> searchValueFieldWithLimit(String search, int limit) {
        if(StringUtils.isBlank(search)) {
            return new ArrayList<T>();
        }
        //limit safeguard
        if(limit <0 || limit >1000) {
            limit =20;
        }

        Document regexDoc = new Document();
        regexDoc.append("$regex",search).append("$options","i");

        Document searchDoc = new Document();
        searchDoc.append("value", regexDoc);

        //String searchString = new Document()"{ value: { $regex: '"+search+"', $options: 'i' } }";
        logger.info("SearchString:"+gson.toJson(searchDoc));

        return getList(mongoCollection.find(searchDoc).limit(limit).into(new ArrayList<Document>()));
    }

    public List<String> searchKeysWithLimit(String search, int limit) {
        if(StringUtils.isBlank(search)) {
            return new ArrayList<String>();
        }
        //limit safeguard
        if(limit <0 || limit >1000) {
            limit =20;
        }

        Document regexDoc = new Document();
        regexDoc.append("$regex",search).append("$options","i");

        Document searchDoc = new Document();
        searchDoc.append("uniqueKey", regexDoc);

        //String searchString = new Document()"{ value: { $regex: '"+search+"', $options: 'i' } }";
        logger.info("SearchString:" + gson.toJson(searchDoc));

        BasicDBObject  b = new BasicDBObject();
        b.append("uniqueKey",true);
        b.append("_id",false);

        List<Document> docs = mongoCollection.find(searchDoc).projection(Document.parse("{_id: false, uniqueKey: true}")).limit(limit).into(new ArrayList<Document>());
        List<String> objs = new ArrayList<String>();
        for(Document doc: docs) {
            objs.add(doc.getString("uniqueKey"));
        }
        return objs;
        //return getList(mongoCollection.find(searchDoc).projection().limit(limit).into(new ArrayList<Document>()));
    }


    public static void main(String[] args) {
        MongoConnection.init(Constants.MONGO_PORT, Constants.MONGO_HOST, Constants.MONGO_INSTALLATION);
        CustomerDAO customerDAO = SingletonManagerDAO.getInstance().getCustomerDAO();
        Gson gson = new Gson();
        logger.info(gson.toJson(customerDAO.searchKeysWithLimit("cust", 10)));

    }



}

