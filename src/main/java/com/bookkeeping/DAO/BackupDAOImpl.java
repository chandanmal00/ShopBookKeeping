package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Backup;
import com.bookkeeping.persistence.MongoConnection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public class BackupDAOImpl extends MongoCollectionDAOImpl<Backup> implements BackupDAO {

    static final Logger logger = LoggerFactory.getLogger(BackupDAOImpl.class);


    public BackupDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_BACKUP_COLLECTION);
    }


    /*

    @Override
    public Backup getBasedOnId(String companyId) {

        Document query = new Document();
        query.append("_id",companyId);
        Document companyDocument = mongoCollection.find(query).first();
        Backup company = gson.fromJson(companyDocument.toJson(), Backup.class);
        return company;
    }

    @Override
    public Backup getBasedOnUniqueKey(String uniqueKey) {
        Document doc = mongoCollection.find(this.getUniqueDocument(uniqueKey)).first();
        if(doc!=null) {
            return gson.fromJson(doc.toJson(),Backup.class);
        }
        return null;
    }

    /*
    @Override
    public Backup getBasedOnFirstName(String companyName) {
        Document query = new Document();
        query.append("firstName",companyName);
        Document companyDocument = mongoCollection.find(query).first();
        return gson.fromJson(companyDocument.toJson(), Backup.class);
    }
    */
/*
    public List<Backup> getBasedOnBackupFirstName(String company) {
        Document query = new Document();
        query.append("firstName",company);
        List<Document> companyDocumentList = mongoCollection.find(query).into(new ArrayList<Document>());
        return getList(companyDocumentList);
    }

    */
    /*
    @Override
    public Backup getBasedOnFirmName(String firmName) {
        return getBasedOnUniqueKey(firmName);

        Document query = new Document();
        query.append("firmName",firmName);
        Document companyDocument = mongoCollection.find(query).first();
        return gson.fromJson(companyDocument.toJson(), Backup.class);



    }

*/
    /*
    @Override
    public List<String> listKeys() {
        List<String> keys = new ArrayList<String>();
        List<Backup> list = super.list();
        for(Backup k : list) {
            keys.add(k.getUniqueKey());
        }
        return keys;
    }

*/
    protected List<Backup> getList(List<Document> documentList) {
        List<Backup> BackupList = new ArrayList<Backup>();
        for(Document companyDoc : documentList) {
            Backup company = gson.fromJson(companyDoc.toJson(), Backup.class);
            BackupList.add(company);
        }
        return BackupList;
    }

    /*
    @Override
    protected Document uniqueDocument(Backup company) {
        return new Document()
                .append(Constants.UNIQUE_KEY,
                        company.getUniqueKey());
    }

    @Override
    protected Document getDocument(Backup company) {
        return Document.parse(gson.toJson(company));
    }

    @Override
    protected String getJsonString(Backup company) {
        return gson.toJson(company);
    }

*/


    @Override
    public Backup getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), Backup.class);
    }
}
