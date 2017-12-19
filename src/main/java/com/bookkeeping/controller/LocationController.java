package com.bookkeeping.controller;

import com.bookkeeping.DAO.LocationDAO;
import com.bookkeeping.DAO.LocationDAOImpl;
import com.bookkeeping.model.Location;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.util.List;

import static spark.Spark.get;

/**
 * Created by chandan on 6/20/16.
 */
public class LocationController {

    LocationDAO locationDAO;
    static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    public LocationController() {
        locationDAO = new LocationDAOImpl();
    }

    public void initializeRoutes() throws IOException {
        // used to display Khareeddar Detail Page



        get(new Route("/listLocations") {
            @Override
            public Object handle(Request request, Response response) {
                List<Location> locations = locationDAO.list();
                Gson gson = new Gson();
                return gson.toJson(locations);
            }
        });

    }
}
