package com.bookkeeping.model;

import com.bookkeeping.DAO.MongoCollectionDAO;

/**
 * Created by chandanm on 7/10/16.
 */
public class MongoCollectionResponse {
    MongoCollectionDAO mongoCollectionDAO;
    String entity;

    public MongoCollectionDAO getMongoCollectionDAO() {
        return mongoCollectionDAO;
    }

    public void setMongoCollectionDAO(MongoCollectionDAO mongoCollectionDAO) {
        this.mongoCollectionDAO = mongoCollectionDAO;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
