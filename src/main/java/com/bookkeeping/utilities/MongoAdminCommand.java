package com.bookkeeping.utilities;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.persistence.MongoConnection;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;


/**
 * Created by chandan on 6/24/16.
 * Class used to import/export mongo database, it also has a utility to run Commands
 */
public class MongoAdminCommand {
    static final Logger logger = LoggerFactory.getLogger(MongoAdminCommand.class);


    public static void main(String[] args) {
        String command = String.format("%s/bin/mongodump -h %s:%d -d %s -o %s",
                Constants.MONGO_INSTALLATION,
                Constants.MONGO_HOST,MongoConnection.getMongoPort(),Constants.MONGO_DATABASE,"uDIr");

        System.out.println(command);
    }
    /*
    public static void main(String[] args) {
        String command = "ls -l /tmp";
        try {
            //exportMongoDatabaseToDir();
            importMongoDatabaseFromDir("2016-06-24");
            Process p = Runtime.getRuntime().exec(command);
            InputStream is = p.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            logger.info("Input Stream for command:{}"+writer.toString());
            IOUtils.copy(p.getErrorStream(), writer, "UTF-8");
            logger.info("Error Stream for command:{}"+writer.toString());

        } catch (IOException e) {
            // e.printStackTrace();
            logger.error("Error in importing mongo database", e);
        } catch (BookKeepingException e) {
            e.printStackTrace();
        }

    }
    */

    public static boolean exportMongoDatabaseToDir() throws BookKeepingException {
        //create dir here
        String currentDateStr = ControllerUtilities.getCurrentDateStrInYYYY_MM_DD();
        return exportMongoDatabaseToDir(currentDateStr,Constants.MONGO_DATABASE_BACKUP_DIR_ROOT);
    }

    public static boolean exportMongoDatabaseToDir(String inputDateStr,String backupDir) throws BookKeepingException {
        //create dir here
        if(!ControllerUtilities.verifyDateInFormat(inputDateStr)) {
            logger.error("Date:{} not in yyyy-MM-dd format", inputDateStr);
            throw new BookKeepingException("Date:"+inputDateStr+" not in yyyy-MM-dd format");
        }
        String inputDir =  backupDir +"/"+inputDateStr;
        File f = new File(inputDir);
        try {

            if(f.exists() && f.isDirectory()) {
                f.delete();
                logger.info(("Deleting dir as it exists for a new backup"));
            }
            f.mkdirs();
        } catch(Exception e) {
            logger.error("Exception in creation input dir:{}", inputDir,e);
            throw new BookKeepingException("Exception in creation input dir:"+inputDir);
        }
        String command = String.format("%s/bin/mongodump -h %s:%d -d %s -o %s",
                MongoConnection.getMongoInstallationDir(),
                Constants.MONGO_HOST,MongoConnection.getMongoPort(),Constants.MONGO_DATABASE,inputDir);

        logger.info("Command for export: {}",command);
        return runCommand(command);


    }

    public static boolean importMongoDatabaseFromDir(String inputDateStr) throws BookKeepingException {
        return importMongoDatabaseFromDir(Constants.MONGO_DATABASE_BACKUP_DIR_ROOT,inputDateStr);
    }

    public static boolean importMongoDatabaseFromDir(String backupDir, String inputDateStr) throws BookKeepingException {
        //create dir here
        if (ControllerUtilities.verifyDateInFormat(inputDateStr)) {
            String inputDir = backupDir + "/" + inputDateStr + "/"+Constants.MONGO_DATABASE;

            File f = new File(inputDir);
            try {
                if (f.exists() && f.isDirectory()) {
                    //f.delete();
                    String command = String.format("%s/bin/mongorestore -h %s:%d --drop -d %s --dir %s",
                            MongoConnection.getMongoInstallationDir(),
                            Constants.MONGO_HOST,MongoConnection.getMongoPort(), Constants.MONGO_DATABASE, inputDir);
                    logger.info("Command for import: {}", command);

                    //logger.info(("dir exists, trying to load the backup, but before we need to delete existing db"));
                    runCommand(command);
                    return true;

                } else {
                    logger.error("It seems the backup mongodb you are looking for date:{}, path:{} is missing, exiting...",inputDateStr,inputDir);
                    throw new BookKeepingException("It seems the backup mongodb you are looking for date:"
                            +inputDateStr
                            +", path:"+inputDir
                            +" is missing");
                    //System.exit(-2);
                }
            } catch (Exception e) {
                logger.error("Exception in creation input dir:{}", inputDir, e);
                throw new BookKeepingException("Most likely we do not have a backup with this input date:"+inputDateStr+", path:"+ inputDir);
            }

        } else {
            logger.error("Bad date format for importing mongoDB backsup, we need in yyyy-mm-dd format, input:{}",inputDateStr);
            throw new BookKeepingException("Bad date format for importing mongoDB backsup"
                    +"we need in yyyy-mm-dd format, input:"+inputDateStr);
        }

    }

    private static boolean runCommand(String command) throws BookKeepingException {
        try {
            Process p = Runtime.getRuntime().exec(command);
            StringWriter writer = new StringWriter();
            IOUtils.copy(p.getInputStream(), writer, "UTF-8");
            logger.info("Input Stream for command:{}"+writer.toString());
            IOUtils.copy(p.getErrorStream(), writer, "UTF-8");
            logger.info("Error Stream for command:{}"+writer.toString());
            return true;
        } catch (Exception e) {
            logger.error("Error in executing command:{}",command,e);
            throw new BookKeepingException("Error in executing command:"+command);
        }


    }


}
