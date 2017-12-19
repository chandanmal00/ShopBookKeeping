package com.bookkeeping.DAO;

import com.bookkeeping.model.Location;

import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public interface LocationDAO extends MongoCollectionDAO<Location>{

    /*
    public  void add(Location location);
    public  void remove(Location location);
    public List<Location> list();

    public Location get(String locationId);
    */
    public Location getBasedOnPlace(String place);
    public List<Location> getBasedOnTaluka(String taluka);
}
