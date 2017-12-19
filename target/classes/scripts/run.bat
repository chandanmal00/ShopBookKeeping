

TITLE BookKeeper V0.16

set ROOT="C:\BrokerBookKeepingApp\"

set MONGO_HOME="C:\Program Files\MongoDB\Server\3.2"
set INTELLIJ_HOME=\Applications\IntelliJ\ IDEA\ 15\ CE\.app\
set MAVEN_HOME=\Applications\IntelliJ\ IDEA\ 15\ CE\.app\Contents\plugins\maven\lib\maven3\


set MAVEN_HOME=%ROOT%\apache-maven-3.3.9-bin
set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_60"

set PATH=%MONGO_HOME%\bin:%MAVEN_HOME%\bin:%JAVA_HOME%\bin:%PATH%
 
set JAR_LOCATION=%ROOT%\jars


md -p %ROOT%

status=$?

echo "Status is %status"

if (( %status==0 )); then
 
 echo "Created Root dir %ROOT%"

else
  echo "Failure in creation of %ROOT% dir, exiting..."
  
 #EXIT /B "exiting.."
fi


set PROJECT_HOME=%ROOT%\project


#Create dirs


set DB_BACKUP_DIR=%ROOT%\backups

set LOGS_DIR=%ROOT%\backups

set DB_DATA_DIR=%ROOT%\backups

md -p %DB_BACKUP_DIR%

md -p %LOGS_DIR%

md -p %DB_DATA_DIR%


set MONGO_PORT=27017
SET HOSTNAME=%COMPUTERNAME%
set MONGO_HOST=localhost

set SITE_PORT=8080


##Run mongod

%MONGO_HOME\bin\mongod --dbpath %DB_DATA_DIR% --port %MONGO_PORT%



status=$?

echo "Status is %status
"
if (( %status==0 )); then
 
 echo "Mongo Started, sleeping 5 secs..."

else

  echo "mongod did not start, please take a look"

  EXIT /B "exiting.."
fi

echo "Sleeping 5 sec.."

sleep 5



##Run jar

%JAVA_HOME%\bin\java -jar %PROJECT_HOME%\target\broker-bookkeeping-1.0-SNAPSHOT-jar-with-dependencies.jar %SITE_PORT% %MONGO_PORT% %MONGO_HOST% $DB_BACKUP_DIR% &



