package com.bookkeeping.constants;

/**
 * Created by chandan on 9/14/2015.
 */
public class Constants {

    //has privilege to upload pics for listings
    public final static String ADMIN_USER="master@test.com";
    public final static String ADMIN_PASS="admin123";
    public final static String DEFAULT_PASS="test1234";

    public final static int SEARCH_RESULTS_SIZE = 100;

    public final static String APP_TITLE="Inventory BookKeeper";
    public final static String APP_TITLE_HI="माल बहीखाता";
    public final static String DOMAIN_NAME="businss.xyz";
    public final static String APP_LINK="InventoryBookKeeper";
    public final static String APP_WEBSITE="www.inventory-bookkeeper.xyz";
    public final static String APP_WEBSITE_BIZ="Inventory BookKeeper";
    public final static String APP_ADDRESS="Sunnyvale, CA";
    public final static String APP_MESSAGE="Helping make business easier!!";
    public final static String APP_MESSAGE_HI="कारोबार को आसान बनाने में मदद!";



    public final static String MONGO_HOST="localhost";
    public final static int MONGO_PORT=27017;
    public final static String MONGO_URI="mongodb://"+MONGO_HOST;
    public static final String MONGO_DATABASE = "inventory";
    public static final String MONGO_DATABASE_BACKUP_DIR_ROOT = "/local/backup";
    public static final String MONGO_USERS_COLLECTION = "users";
    public static final String MONGO_INSTALLATION = "/Users/chandanm/Downloads/mongodb-osx-x86_64-3.2.8";
    public static final String MONGO_SESSIONS_COLLECTION = "sessions";

    /*
    public final static String ELASTIC_CLUSTER_NAME="elasticsearch";
    public final static String ELASTIC_INDEX_NAME="bookkeeping";
    public final static String ELASTIC_TYPE_NAME="entities";


    public final static int ELASTIC_SEARCH_RESULTS_SIZE = 10;
    public final static int ELASTIC_SEARCH_SUGGEST_TIMEOUT = 3000;

*/
    public final static String INFO_EMAIL = "test@test.com";

    public final static int SITE_PORT = 8080;

    public static final String MONGO_SALESMAN_COLLECTION = "salesmans";
    public static final String MONGO_BACKUP_COLLECTION = "backups";
    public static final String MONGO_ITEM_COLLECTION = "items";
    public static final String MONGO_COMPANY_COLLECTION = "companies";
    public static final String MONGO_NATIONAL_IDENTITY_COLLECTION = "national_identity";
    public static final String MONGO_CUSTOMER_TRANSACTIONS_COLLECTION = "customer_transaction";
    public static final String MONGO_ITEM_SOLD_COLLECTION = "item_sold";
    public static final String MONGO_ITEM_TRANSACTION_COLLECTION = "item_transaction";
    public static final String MONGO_CUSTOMER_COLLECTION = "customer";
    public static final String MONGO_CUSTOMER_PAYMENT_COLLECTION = "customer_payment";

    public static final String MONGO_LOCATION_COLLECTION = "location";



    //unique keys for each collection
    public static final String kisaanUniqueKey = "nickName";
    public static final String khareeddarUniqueKey = "firmName";
    public static final String locationUniqueKey = "place";
    public static final String transactionItemUniqueKey = "name";
    public static final String nationalIdentityUniqueKey = "aadhar";
    public static final String nationalIdentityPanUniqueKey = "pan";

    public static final String UNIQUE_KEY_SEPARATOR = "__";
    public static final String UNIQUE_KEY = "uniqueKey";
    public static final String ID_KEY = "_id";

    public static final String NO_KEY = "NO_KEY_EXISTS";
    public static final String FAILURE = "failure";

    public static final String ENTITY_NAME = "ENTITY_NAME";
    public static final String ENTITY = "entity";
    public static final String PAYMENTS = "payments";
    public static final String TRANSACTIONS = "transactions";
    public static final String ENTITY_KEY_NAME = "entityKey";
    public static final String ENTITY_KEY_VALUE = "entityValue";

    public static final String ENTITY_ITEM = "item";
    public static final String ENTITY_BACKUP = "backup";
    public static final String ENTITY_ITEM_SOLD = "itemSold";
    public static final String ENTITY_ITEM_INVENTORY = "itemInventory";
    public static final String ENTITY_ITEM_TRANSACTION = "itemTransaction";
    public static final String ENTITY_ITEM_SELL = "itemSell";
    public static final String ENTITY_ITEM_BUY = "itemBuy";
    public static final String ENTITY_COMPANY = "company";
    public static final String ENTITY_CUSTOMER_TRANSACTION = "transaction";
    public static final String ENTITY_CUSTOMER = "customer";
    public static final String ENTITY_CUSTOMER_PAYMENT = "payment";
    public static final String ENTITY_SALESMAN = "salesman";
    public static final String ERROR = "errors";
    public static final String SUCCESS = "success";


    public static final String TOTAL_PAYMENT_AMOUNT = "totalPaymentAmount";
    public static final String TOTAL_TRANSACTION_AMOUNT = "totalTransactionAmount";

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";
    public static final String DATE_FORMAT_YYYY_MM_DD_FULL = "yyyy-MM-dd HH:mm:ss";


    public static final String ERROR_KEY = "errors";
    public static final int LIMIT_ROWS = 100;
    public final static int TRIAL_EDITION_LIMIT = 999999999;
    public final static int MAX_ROWS = 1000;



/*
db.item_sold.aggregate({$group:{_id: '$barcode',total:{$sum:1},'stock':{$sum:{$cond:[{ $gt:['$quantity',0]},'$quantity',0]}}, 'sold':{$sum:{$cond:[{ $lt:['$quantity',0]},'$quantity',0]}}}});
 */

    //drop: db.runCommand({ dropDatabase: 1 })

    //delete all indexes
    /*
    db.getCollectionNames().forEach(function(collName) {
        db.runCommand({dropIndexes: collName, index: "*"});
    });

    //like delete: db.customer_transaction.remove({customer:/customer_200/});
    db.customer_2.drop()
    */


}
