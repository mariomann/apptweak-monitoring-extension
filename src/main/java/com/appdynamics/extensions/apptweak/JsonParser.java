package com.appdynamics.extensions.apptweak;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mariomann on 23/08/16.
 */
public class JsonParser {

    public Integer parseInteger(String json, String element) {
        return Integer.parseInt(parseString(json, element));
    }

    public String parseString(String json, String element) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject content = (JSONObject) parser.parse(json);

            if(element.contains(".")) {
                List<String> elements = new ArrayList<>(Arrays.asList(element.split("\\.")));
                element = elements.remove(elements.size() - 1);

                for (String item : elements) {
                    content = (JSONObject) content.get(item);
                }
            }
            return String.valueOf(content.get(element));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
