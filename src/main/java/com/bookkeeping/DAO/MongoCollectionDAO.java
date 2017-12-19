package com.bookkeeping.DAO;

import org.bson.Document;

import java.util.List;
import java.util.Map;

/**
 * Created by chandan on 6/18/16.
 */
public interface MongoCollectionDAO<T>{

    public long count();
    public void add(T o);
    public void update(T o);
    public void add(List<T> oList);
    public boolean exists(T o);
    public void forceAdd(T o);
    public void remove(T o);
    public void remove(String uniqueKey);
    public void remove(List<T> oList);
    public List<T> list();
    public List<T> list(int limit);
    public List<T> list(Document filter);
    public List<T> list(Document filter, int limit);
    public Map<String,T> keyMap();
    public List<String> listKeys();
    public T getBasedOnUniqueKey(String uniqueKey);
    public T getBasedOnId(String id);
    public void removeBasedOnId(String id);
    public List<T> search(String search);
    public List<T> search(String search,int limit);
    public List<T> searchValueFieldWithLimit(String search, int limit);
    public List<String> searchKeysWithLimit(String search, int limit);
    public List<Document> getDailySummaryForWeekEnding(String toDateStr);
    public List<Document> getDailySummaryFor30daysEnding(String toDateStr);
    public List<Document> getDailySummaryNDatesEndingToday(int n);
    public List<Document> getMonthlySummaryEndingToday(int n);
    public List<Document> getMonthlySummary(String toDateStr, int n);
    public List<Document> getEntityMonthlySummaryEndingToday(int n,String uniqueKey, String entity);
    public List<Document> getEntityDailySummaryNDatesEndingToday(int n,String uniqueKey, String entity);


}
