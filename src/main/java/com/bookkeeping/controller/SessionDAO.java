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
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import sun.misc.BASE64Encoder;

import java.security.SecureRandom;

public class SessionDAO {
    private final MongoCollection<Document> sessionsCollection;

    public SessionDAO() {
        this.sessionsCollection= MongoConnection.getInstance().getCollection(Constants.MONGO_SESSIONS_COLLECTION);
    }


    public String findUserNameBySessionId(String sessionId) {
        Document session = getSession(sessionId);

        if (session == null) {
            return null;
        }
        else {
            return session.get("username").toString();
        }
    }


    // starts a new session in the sessions table
    public String startSession(String username) {

        // get 32 byte random number. that's a lot of bits.
        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes(randomBytes);

        BASE64Encoder encoder = new BASE64Encoder();

        String sessionID = encoder.encode(randomBytes);

        // build the BSON object
        Document session = new Document("username", username);

        session.append("_id", sessionID);

        sessionsCollection.deleteMany(
                new Document("username",username));

        sessionsCollection.insertOne(session);

        return session.getString("_id");
    }

    public String startVanillaSession() {

        // get 32 byte random number. that's a lot of bits.
        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes(randomBytes);

        BASE64Encoder encoder = new BASE64Encoder();

        String sessionID = encoder.encode(randomBytes);
        return sessionID;
    }


    // ends the session by deleting it from the sesisons table
    public void endSession(String sessionID) {
        sessionsCollection.deleteOne
                (new Document("_id", sessionID));
    }

    // retrieves the session from the sessions table
    public Document getSession(String sessionID) {
        return sessionsCollection.find(
                new Document("_id", sessionID)).first();
    }
}
