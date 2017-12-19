/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bookkeeping.controller;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.persistence.MongoConnection;
import com.google.gson.Gson;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

public class UserDAO {
    private final MongoCollection<Document> usersCollection;

    private Random random = new SecureRandom();
    static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public UserDAO() {
        this.usersCollection= MongoConnection.getInstance().getCollection(Constants.MONGO_USERS_COLLECTION);
    }

    // validates that username is unique and insert into db
    public boolean exists(String username) {
        Document user = new Document();
        user.append("_id", username);
        Document document = usersCollection.find(user).first();
        if(document!=null) {
            return true;
        }
        return false;
    }

    public boolean addUser(String username, String password, String email) {

        String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));

        Document user = new Document();

        user.append("_id", username).append("password", passwordHash);

        if (email != null && !email.equals("")) {
            // the provided email address
            user.append("email", email);
        }

        try {
            usersCollection.insertOne(user);
            return true;
        } catch (MongoWriteException e) {
            logger.error("Username already in use: {}", username, e);
            return false;
        }
    }

    /**
     * TODO: write a better with with no default password
     * @param email
     * @return
     */
    public boolean updatePassword(String email) {

        Gson gson = new Gson();
        String passwordHash = makePasswordHash(Constants.DEFAULT_PASS, Integer.toString(random.nextInt()));

        BsonDocument id = new BsonDocument();
        id.put("_id",new BsonString(email));
        Document user = new Document();
        user.append("_id", email).append("password", passwordHash);

        if (StringUtils.isNotBlank(email)) {
            // the provided email address
            user.append("email", email);
        }

        try {
            usersCollection.replaceOne(id, user);
            return true;
        } catch (MongoWriteException e) {
            logger.error("Username:{} does not exist, so not updating!!", email, e);
            return false;
        }
    }

    // validates that username is unique and insert into db
    public boolean addUser(Map<String,String> rootMap) {

        String password = rootMap.get("password");
        String firstname = rootMap.get("firstname");
        String lastname = rootMap.get("lastname");
        String email = rootMap.get("email");
        String phonenumber = rootMap.get("phonenumber");
        String username = rootMap.get("username");

        String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));

        Document user = new Document();

        user.append("_id", username).append("password", passwordHash);

        if (email != null && !email.equals("")) {
            // the provided email address
            user.append("email", email);
        }
        user.append("firstname",firstname)
                .append("lastname",lastname)
                .append("phonenumber",phonenumber);

        try {
            usersCollection.insertOne(user);
            return true;
        } catch (MongoWriteException e) {
            logger.error("Username already in use: {}", username, e);
            return false;
        }
    }



    public Document validateLogin(String username, String password) {


        Document user = usersCollection.find(new Document("_id", username)).first();

        if (user == null) {
            logger.error("User not in database");
            return null;
        }

        String hashedAndSalted = user.get("password").toString();

        String salt = hashedAndSalted.split(",")[1];

        if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
            logger.error("Submitted password is not a match");
            return null;
        }

        return user;
    }


    private String makePasswordHash(String password, String salt) {
        try {
            String saltedAndHashed = password + "," + salt;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(saltedAndHashed.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
            return encoder.encode(hashedBytes) + "," + salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 is not available", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
        }
    }

    public static void main(String[] args) {

            long time = System.currentTimeMillis();
        System.out.println(time);
        String orig = time + "";

            //encoding  byte array into base 64
            byte[] encoded = Base64.encodeBase64(orig.getBytes());

            System.out.println("Original String: " + orig );
            System.out.println("Base64 Encoded String : " + new String(encoded) + ":"+ new String(encoded).length());

            //decoding byte array into base64
            byte[] decoded = Base64.decodeBase64(encoded);
            System.out.println("Base 64 Decoded  String : " + new String(decoded));

    }
}
