package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.Item;
import com.bookkeeping.persistence.MongoConnection;
import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandan on 6/4/16.
 */
public class ItemDAOImpl extends MongoCollectionDAOImpl<Item> implements ItemDAO {

    Gson gson;
    static final Logger logger = LoggerFactory.getLogger(ItemDAOImpl.class);


    public ItemDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_ITEM_COLLECTION);
        gson = new Gson();
    }

    protected List<Item> getList(List<Document> documentList) {
        List<Item> ItemList = new ArrayList<Item>();
        for(Document itemDoc : documentList) {
            Item item = gson.fromJson(itemDoc.toJson(), Item.class);
            ItemList.add(item);
        }
        return ItemList;
    }



    @Override
    public Item getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), Item.class);
    }

    public static void main(String[] args) {
        MongoConnection.init(Constants.MONGO_PORT,Constants.MONGO_HOST,Constants.MONGO_INSTALLATION);
        MongoDatabase db = MongoConnection.getInstance();
        ItemDAO itemDAO = new ItemDAOImpl();
        for(int i=0;i<120;i++) {
            Item item = new Item("barcode_"+i,"productType_"+i,"brand_"+i,"itemType_"+i,800);

            item.setListPrice(1000);
            item.setPurchasePrice(800);
            item.setEventDate(item.getCreationDate());
            itemDAO.add(item);
        }

    }
}
