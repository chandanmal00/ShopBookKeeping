#!/bin/bash

MONGO_HOME="/Users/chandan/mongodb/"
INTELLIJ_HOME=/Applications/IntelliJ\ IDEA\ 15\ CE\.app/
export MAVEN_HOME=/Applications/IntelliJ\ IDEA\ 15\ CE\.app/Contents/plugins/maven/lib/maven3/
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home
export PATH=$MONGO_HOME/bin:$MAVEN_HOME/bin:$JAVA_HOME/bin:$PATH
ROOT="/Users/chandan/app"
mkdir -p $ROOT
status=$?
echo "Status is $status"
if (( $status==0 )); then
  echo "Created Root dir $ROOT"
else
  echo "Failure in creation of $ROOT dir, exiting..."
  #exit -2
fi

PROJECT_HOME=/Users/chandan/Desktop/Data/NewFolder/BrokerBookeeping
#Create dirs

DB_BACKUP_DIR=$ROOT/backups
LOGS_DIR=$ROOT/backups
DB_DATA_DIR=$ROOT/backups
mkdir -p $DB_BACKUP_DIR
mkdir -p $LOGS_DIR
mkdir -p $DB_DATA_DIR

MONGO_PORT=27018
MONGO_HOST=localhost
SITE_PORT=8080
##Run mongod
$MONGO_HOME/bin/mongod --dbpath $DB_DATA_DIR --port $MONGO_PORT&

status=$?
echo "Status is $status"
if (( $status==0 )); then
  echo "Mongo Started, sleeping 5 secs..."
else
  echo "mongod did not start, please take a look"
  exit -2
fi
echo "Sleeping 5 sec.."
sleep 5

##Run jar
$JAVA_HOME/bin/java -jar $PROJECT_HOME/target/broker-bookkeeping-1.0-SNAPSHOT-jar-with-dependencies.jar $SITE_PORT $MONGO_PORT $MONGO_HOST $DB_BACKUP_DIR $MONGO_HOME &



