package com.sameer.WordFinder.ServiceGateway;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class OxfordServiceGateway {

    public Map<String, List<String>> getDefinitionsOfWords(List<String> words) {
        Map<String, List<String>> wordDefMap = new HashMap<>();
        for(String word : words) {
            List<String> definitions = isEnglishWord(word);
            if (definitions != null) {
                wordDefMap.put(word, definitions);
            }
        }

        return wordDefMap;
    }

    private List<String> isEnglishWord(String word) {
        List<String> wordDefList = new ArrayList<>();
        StringBuilder def;

        try {
            Properties props = new Properties();
            props.load(new FileInputStream(new File("app.properties")));

            String url = props.getProperty("oxford_service_url");
            String appId = props.getProperty("app_id");
            String appKey = props.getProperty("app_key");

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(url + word);
            String str = target.request(MediaType.APPLICATION_JSON).header("app_id", appId)
                    .header("app_key", appKey).get(String.class);


            try {
                //System.out.println("Found: " + word);
                //System.out.println(str);
                JSONObject jsonObj = new JSONObject(str);
                JSONArray resultsArray = jsonObj.getJSONArray("results");
                JSONObject resultsArrayFirstObject = resultsArray.getJSONObject(0);
                JSONArray lexicalEntriesArray = resultsArrayFirstObject.getJSONArray("lexicalEntries");
                for (int i = 0; i < lexicalEntriesArray.length(); i++) {
                    def = new StringBuilder();
                    JSONObject lexicalEntriesObject = lexicalEntriesArray.getJSONObject(i);
                    String lexicalCategory = lexicalEntriesObject.getString("lexicalCategory");
                    def.append("(").append(lexicalCategory).append(") ");
                    JSONArray entriesArray = lexicalEntriesObject.getJSONArray("entries");
                    JSONObject entriesFirstObject = entriesArray.getJSONObject(0);
                    JSONArray sensesArray = entriesFirstObject.getJSONArray("senses");
                    for (int j = 0; j < sensesArray.length(); j++) {
                        JSONObject sensesFirstObject = sensesArray.getJSONObject(j);
                        if (sensesFirstObject.has("definitions")) {
                            JSONArray definitionsArray = sensesFirstObject.getJSONArray("definitions");
                            //System.out.println(i + 1 + "." + j + ". " + definitionsArray.getString(0));
                            if (definitionsArray.length() > 0) {
                                String lexicalDef = definitionsArray.getString(0);
                                def.append(lexicalDef).append(" ");
                                int removeIndex = -1;
                                int defCount = 0;
                                for(String defn : wordDefList) {
                                    if(defn.contains(lexicalDef)) {
                                        break;
                                    }
                                    if(def.toString().contains(defn)) {
                                        removeIndex = defCount;
                                        break;
                                    }
                                    defCount++;
                                }
                                if(removeIndex >= 0) {
                                    wordDefList.remove(removeIndex);
                                }
                                wordDefList.add(def.toString());
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (NotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordDefList;
    }
}
