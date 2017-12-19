use inventory

db.items.drop()
db.item_inventory.drop()
db.customer_transactions.drop()
db.customer_payment.drop()
db.companies.drop()
db.salesmans.drop()
db.customer.drop()

db.customer_transaction.drop()
db.sessions.drop()
db.users.drop()
db.backups.drop()
db.getCollectionNames()

db.runCommand({ dropDatabase: 1 })

//get index: db.items.getIndexes()
//To get all indexes in mongo for all collections
db.getCollectionNames().forEach(function(collection) {
     indexes = db[collection].getIndexes();
     print("Indexes for " + collection + ":");
     printjson(indexes);
  });

