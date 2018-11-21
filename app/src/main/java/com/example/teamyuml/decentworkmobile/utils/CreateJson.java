package com.example.teamyuml.decentworkmobile.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Let us create easier json body in volley requests.
 * For now we only have {@link Integer} and {@link String} types as parameters.
 */
public class CreateJson {

    /**
     * Integer parameters.
     */
    private Map<String, Integer> intParams;
    /**
     * String parameters.
     */
    private Map<String, String> strParams;

    /**
     * Add Integer parameter to Integer Hash Map
     * @param name Parameter name i.e key
     * @param value Parameter value
     */
    public void addInt(String name, Integer value) {
        if (intParams == null) {
            intParams = new HashMap<>();
        }

        intParams.put(name, value);
    }

    /**
     * Add String parameter to String Hash Map
     * @param name Parameter name i.e. key
     * @param value Parameter value
     */
    public void addStr(String name, String value) {
        if (strParams == null) {
            strParams = new HashMap<>();
        }

        strParams.put(name, value);
    }

    /**
     * Makes Valid json to send as data with requests.
     * @return valid json object.
     * @throws JSONException
     */
    public JSONObject makeJSON() throws JSONException {
        String jsonString = "{";
        List<String> params = new ArrayList<>();

        if (intParams != null) {
            params.add(iterateMap(intParams));
        }

        if (strParams != null) {
            params.add(iterateMap(strParams));
        }

        String jsonParams = TextUtils.join(", ", params);
        jsonString += jsonParams + "}";

        return new JSONObject(jsonString);
    }

    /**
     * Iterate through String Map and joins params with comma.
     * @param map with params
     * @return Joined params
     */
    private String iterateMap(Map map) {
        List<String> params = new ArrayList<>();
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry param = (Map.Entry)it.next();

            if (param.getValue() instanceof Integer) {
                params.add(intJson(param));
            } else {
                params.add(strJson(param));
            }

            it.remove();
        }

        return TextUtils.join(", ", params);
    }

    /**
     * Creates String containing valid json for integer param.
     * Single param consists of:
     * 'name': value
     * @param param Single map entry.
     * @return String containing valid json.
     */
    private String intJson(Map.Entry param) {
        return "'" + param.getKey() + "': " + param.getValue();
    }

    /**
     * Creates String containing valid json for string param.
     * Single param consist of:
     * 'name': 'value'
     * @param param Sing map entry.
     * @return String containing valid json.
     */
    private String strJson(Map.Entry param) {
        return "'" + param.getKey() + "': '" + param.getValue() + "'";
    }
}