package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Location;
import com.bookkeeping.persistence.MongoConnection;
import com.google.gson.Gson;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public class LocationDAOImpl extends MongoCollectionDAOImpl<Location> implements LocationDAO {
    
    //Client client;
    static final Logger logger = LoggerFactory.getLogger(LocationDAOImpl.class);
    Gson gson;


    public LocationDAOImpl() {
        //this.client = ElasticCacheConnection.getInstance();
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_LOCATION_COLLECTION);
        gson = new Gson();
    }

    @Override
    public Location getBasedOnPlace(String place) {
        Document doc = new Document();
        doc.append("place",place);
        Document locationDoc = mongoCollection.find(doc).first();
        return gson.fromJson(locationDoc.toJson(),Location.class);
    }

    @Override
    public List<Location> getBasedOnTaluka(String taluka) {
        Document doc = new Document();
        doc.append("taluka",taluka);
        List<Document> locationDocList = mongoCollection.find(doc).into(new ArrayList<Document>());
        return getList(locationDocList);

    }
    @Override
    public List<String> listKeys() {
        List<String> keys = new ArrayList<String>();
        List<Location> list = super.list();
        for(Location k : list) {
            keys.add(k.getUniqueKey());
        }
        return keys;
    }



    @Override
    public Location getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), Location.class);
    }

    @Override
    protected List<Location> getList(List<Document> documentList) {
        List<Location> locationList = new ArrayList<Location>();
        for (Document locationDoc : documentList) {
            // System.out.println("HERE you go::" + locationDoc.toJson());
            Location location = gson.fromJson(locationDoc.toJson(), Location.class);
            locationList.add(location);
        }
        return locationList;
    }

    public static void main(String[] args) {
        Location location = new Location("wrr");
        LocationDAO locationDAO = new LocationDAOImpl();
        locationDAO.add(location);
    }

}
