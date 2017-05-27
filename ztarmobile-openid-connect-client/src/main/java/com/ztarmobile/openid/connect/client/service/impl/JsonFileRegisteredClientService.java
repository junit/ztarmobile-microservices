/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.client.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ztarmobile.oauth2.model.RegisteredClient;
import com.ztarmobile.openid.connect.ClientDetailsEntityJsonProcessor;
import com.ztarmobile.openid.connect.config.service.RegisteredClientService;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The client holds its data in a JSON file.
 * 
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Component
public class JsonFileRegisteredClientService implements RegisteredClientService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(JsonFileRegisteredClientService.class);

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(RegisteredClient.class, new JsonSerializer<RegisteredClient>() {
                @Override
                public JsonElement serialize(RegisteredClient src, Type typeOfSrc, JsonSerializationContext context) {
                    return ClientDetailsEntityJsonProcessor.serialize(src);
                }
            }).registerTypeAdapter(RegisteredClient.class, new JsonDeserializer<RegisteredClient>() {
                @Override
                public RegisteredClient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    return ClientDetailsEntityJsonProcessor.parseRegistered(json);
                }
            }).setPrettyPrinting().create();

    private File file;

    private Map<String, RegisteredClient> clients = new HashMap<>();

    public JsonFileRegisteredClientService(@Value("${account.openid.client.config}") String filename) {
        this.file = new File(filename);
        load();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ztarmobile.openid.connect.client.service.RegisteredClientService#
     * getByIssuer(java.lang.String)
     */
    @Override
    public RegisteredClient getByIssuer(String issuer) {
        return clients.get(issuer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ztarmobile.openid.connect.client.service.RegisteredClientService#save
     * (java.lang.String, org.mitre.oauth2.model.RegisteredClient)
     */
    @Override

    public void save(String issuer, RegisteredClient client) {
        clients.put(issuer, client);
        write();
    }

    /**
     * Sync the map of clients out to disk.
     */
    @SuppressWarnings("serial")
    private void write() {
        try {
            if (!file.exists()) {
                // create a new file
                log.info("Creating saved clients list in " + file);
                file.createNewFile();
            }
            FileWriter out = new FileWriter(file);
            gson.toJson(clients, new TypeToken<Map<String, RegisteredClient>>() {
            }.getType(), out);

            out.close();
        } catch (IOException e) {
            log.error("Could not write to output file", e);
        }
    }

    /**
     * Load the map in from disk.
     */
    @SuppressWarnings("serial")
    private void load() {
        try {
            if (!file.exists()) {
                log.info("No sved clients file found in " + file);
                return;
            }
            FileReader in = new FileReader(file);
            clients = gson.fromJson(in, new TypeToken<Map<String, RegisteredClient>>() {
            }.getType());

            in.close();
        } catch (IOException e) {
            log.error("Could not read from input file", e);
        }
    }
}
