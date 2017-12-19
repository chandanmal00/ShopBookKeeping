package com.bookkeeping.DAO;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.model.ItemSell;
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
public class ItemSoldDAOImpl extends MongoCollectionDAOImpl<ItemSell> implements ItemSoldDAO {

    Gson gson;
    static final Logger logger = LoggerFactory.getLogger(ItemSoldDAOImpl.class);


    public ItemSoldDAOImpl() {
        mongoCollection = MongoConnection.getInstance().getCollection(Constants.MONGO_ITEM_SOLD_COLLECTION);
        gson = new Gson();
    }

    protected List<ItemSell> getList(List<Document> documentList) {
        List<ItemSell> itemSellList = new ArrayList<ItemSell>();
        for(Document itemDoc : documentList) {
            ItemSell item = gson.fromJson(itemDoc.toJson(), ItemSell.class);
            itemSellList.add(item);
        }
        return itemSellList;
    }



    @Override
    public ItemSell getTargetObjBasedOnUniqueKey(Document doc) {
        return gson.fromJson(doc.toJson(), ItemSell.class);
    }
}
