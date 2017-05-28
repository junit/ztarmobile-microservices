/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.ztarmobile.oauth2.model.PKCEAlgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of null-safe converters from common classes and JSON elements,
 * using GSON.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 *
 */
@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class JsonUtils {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static Gson gson = new Gson();

    /**
     * Translate a set of strings to a JSON array, empty array returned as null.
     * 
     * @param value
     *            The value.
     * @return The json element.
     */
    public static JsonElement getAsArray(Set<String> value) {
        return getAsArray(value, false);
    }

    /**
     * Translate a set of strings to a JSON array, optionally preserving the
     * empty array. Otherwise (default) empty array is returned as null.
     * 
     * @param value
     *            The value.
     * @param preserveEmpty
     *            The boolean flag.
     * @return The json element.
     */
    public static JsonElement getAsArray(Set<String> value, boolean preserveEmpty) {
        if (!preserveEmpty && value != null && value.isEmpty()) {
            // if we're not preserving empty arrays and the value is empty,
            // return null
            return JsonNull.INSTANCE;
        } else {
            return gson.toJsonTree(value, new TypeToken<Set<String>>() {
            }.getType());
        }
    }

    /**
     * Gets the value of the given member (expressed as integer seconds since
     * epoch) as a Date.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The date.
     */
    public static Date getAsDate(JsonObject o, String member) {
        if (o.has(member)) {
            JsonElement e = o.get(member);
            if (e != null && e.isJsonPrimitive()) {
                return new Date(e.getAsInt() * 1000L);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a JWE Algorithm, null if it doesn't
     * exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The JWEAlgorithm.
     */
    public static JWEAlgorithm getAsJweAlgorithm(JsonObject o, String member) {
        String s = getAsString(o, member);
        if (s != null) {
            return JWEAlgorithm.parse(s);
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a JWE Encryption Method, null if it
     * doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The EncryptionMethod.
     */
    public static EncryptionMethod getAsJweEncryptionMethod(JsonObject o, String member) {
        String s = getAsString(o, member);
        if (s != null) {
            return EncryptionMethod.parse(s);
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a JWS Algorithm, null if it doesn't
     * exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The JWSAlgorithm.
     */
    public static JWSAlgorithm getAsJwsAlgorithm(JsonObject o, String member) {
        String s = getAsString(o, member);
        if (s != null) {
            return JWSAlgorithm.parse(s);
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a PKCE Algorithm, null if it
     * doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The PKCEAlgorithm.
     */
    public static PKCEAlgorithm getAsPkceAlgorithm(JsonObject o, String member) {
        String s = getAsString(o, member);
        if (s != null) {
            return PKCEAlgorithm.parse(s);
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a string, null if it doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The string.
     */
    public static String getAsString(JsonObject o, String member) {
        if (o.has(member)) {
            JsonElement e = o.get(member);
            if (e != null && e.isJsonPrimitive()) {
                return e.getAsString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a boolean, null if it doesn't
     * exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return a boolean value.
     */
    public static Boolean getAsBoolean(JsonObject o, String member) {
        if (o.has(member)) {
            JsonElement e = o.get(member);
            if (e != null && e.isJsonPrimitive()) {
                return e.getAsBoolean();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a Long, null if it doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return A long number.
     */
    public static Long getAsLong(JsonObject o, String member) {
        if (o.has(member)) {
            JsonElement e = o.get(member);
            if (e != null && e.isJsonPrimitive()) {
                return e.getAsLong();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given given member as a set of strings, null if it
     * doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The list of member.
     * @throws JsonSyntaxException
     *             If there's an error in the json syntax.
     */
    public static Set<String> getAsStringSet(JsonObject o, String member) throws JsonSyntaxException {
        if (o.has(member)) {
            if (o.get(member).isJsonArray()) {
                return gson.fromJson(o.get(member), new TypeToken<Set<String>>() {
                }.getType());
            } else {
                return Sets.newHashSet(o.get(member).getAsString());
            }
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given given member as a set of strings, null if it
     * doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return List of tokens.
     * @throws JsonSyntaxException
     *             If there's an error.
     */
    public static List<String> getAsStringList(JsonObject o, String member) throws JsonSyntaxException {
        if (o.has(member)) {
            if (o.get(member).isJsonArray()) {
                return gson.fromJson(o.get(member), new TypeToken<List<String>>() {
                }.getType());
            } else {
                return Lists.newArrayList(o.get(member).getAsString());
            }
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a list of JWS Algorithms, null if
     * it doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The list of algorithms.
     */
    public static List<JWSAlgorithm> getAsJwsAlgorithmList(JsonObject o, String member) {
        List<String> strings = getAsStringList(o, member);
        if (strings != null) {
            List<JWSAlgorithm> algs = new ArrayList<>();
            for (String alg : strings) {
                algs.add(JWSAlgorithm.parse(alg));
            }
            return algs;
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a list of JWS Algorithms, null if
     * it doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The member.
     * @return The JWEAlgorithm.
     */
    public static List<JWEAlgorithm> getAsJweAlgorithmList(JsonObject o, String member) {
        List<String> strings = getAsStringList(o, member);
        if (strings != null) {
            List<JWEAlgorithm> algs = new ArrayList<>();
            for (String alg : strings) {
                algs.add(JWEAlgorithm.parse(alg));
            }
            return algs;
        } else {
            return null;
        }
    }

    /**
     * Gets the value of the given member as a list of JWS Algorithms, null if
     * it doesn't exist.
     * 
     * @param o
     *            The json object.
     * @param member
     *            The string member.
     * @return The encryption method.
     */
    public static List<EncryptionMethod> getAsEncryptionMethodList(JsonObject o, String member) {
        List<String> strings = getAsStringList(o, member);
        if (strings != null) {
            List<EncryptionMethod> algs = new ArrayList<>();
            for (String alg : strings) {
                algs.add(EncryptionMethod.parse(alg));
            }
            return algs;
        } else {
            return null;
        }
    }

    public static Map readMap(JsonReader reader) throws IOException {
        Map map = new HashMap<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Object value = null;
            switch (reader.peek()) {
            case STRING:
                value = reader.nextString();
                break;
            case BOOLEAN:
                value = reader.nextBoolean();
                break;
            case NUMBER:
                value = reader.nextLong();
                break;
            default:
                logger.debug("Found unexpected entry");
                reader.skipValue();
                continue;
            }
            map.put(name, value);
        }
        reader.endObject();
        return map;
    }

    public static Set readSet(JsonReader reader) throws IOException {
        Set arraySet = null;
        reader.beginArray();
        switch (reader.peek()) {
        case STRING:
            arraySet = new HashSet<>();
            while (reader.hasNext()) {
                arraySet.add(reader.nextString());
            }
            break;
        case NUMBER:
            arraySet = new HashSet<>();
            while (reader.hasNext()) {
                arraySet.add(reader.nextLong());
            }
            break;
        default:
            arraySet = new HashSet();
            break;
        }
        reader.endArray();
        return arraySet;
    }

    public static void writeNullSafeArray(JsonWriter writer, Set<String> items) throws IOException {
        if (items != null) {
            writer.beginArray();
            for (String s : items) {
                writer.value(s);
            }
            writer.endArray();
        } else {
            writer.nullValue();
        }
    }

}
