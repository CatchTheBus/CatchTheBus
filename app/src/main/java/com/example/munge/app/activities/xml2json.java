package com.example.munge.app.activities;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class xml2json {

    private String xmlString;

    public xml2json(String s) {
        xmlString = s;
    }

    public JSONObject convert() {
        try {

            return XML.toJSONObject(xmlString);

        } catch (JSONException e) {
            System.out.println(e.toString());
        }

        return null;
    }
}
