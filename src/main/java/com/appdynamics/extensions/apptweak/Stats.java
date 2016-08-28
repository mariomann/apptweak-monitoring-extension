/**
 * Copyright 2013 AppDynamics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appdynamics.extensions.apptweak;

import com.appdynamics.extensions.apptweak.config.MobileApplication;
import com.google.common.collect.Maps;
import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mariomann on 23/08/16.
 */
public class Stats {
    private Logger logger;

    public Stats(Logger logger) {
        this.logger = logger;
    }

    public Map<String, Object> getAppRating(MobileApplication app, String token) {
        Map<String, Object> appRatingMap = new HashMap<>();
        HttpClient httpClient = new HttpClient();
        Map<String, String> body = new HashMap<>();
        JsonParser parser = new JsonParser();
        String breadcrumb = "content.";

        body.put("country", app.getCountry());
        body.put("language", app.getLanguage());
        Header[] header = new Header[1];
        header[0] = new Header("X-Apptweak-Key", token);
        HttpClient.Response<String> response = httpClient.get("https://api.apptweak.com/" + app.getReligion() + "/applications/" + app.getApplicationId() + "/ratings.json", body, header);

        if(response.statusCode != 200) {
            logger.error("Error: HTTP response code: " + response.statusCode + " | Message: " + response.getBody());
        }

        if (app.getReligion().toLowerCase().equals("android")) {
            appRatingMap.put("average", parser.parseString(response.getBody(), "content.average").replace(".", "").substring(0, 4));
            appRatingMap.put("count", parser.parseString(response.getBody(), "content.count"));
            appRatingMap.put("1", parser.parseString(response.getBody(), "content.star_count.1"));
            appRatingMap.put("2", parser.parseString(response.getBody(), "content.star_count.2"));
            appRatingMap.put("3", parser.parseString(response.getBody(), "content.star_count.3"));
            appRatingMap.put("4", parser.parseString(response.getBody(), "content.star_count.4"));
            appRatingMap.put("5", parser.parseString(response.getBody(), "content.star_count.5"));
        } else if (app.getReligion().toLowerCase().equals("ios")) {
            appRatingMap.put("average", parser.parseString(response.getBody(), "content.current_version.average").replace(".", "").substring(0, 4));
            appRatingMap.put("count", parser.parseString(response.getBody(), "content.current_version.count"));
            appRatingMap.put("1", parser.parseString(response.getBody(), "content.current_version.star_count.1"));
            appRatingMap.put("2", parser.parseString(response.getBody(), "content.current_version.star_count.2"));
            appRatingMap.put("3", parser.parseString(response.getBody(), "content.current_version.star_count.3"));
            appRatingMap.put("4", parser.parseString(response.getBody(), "content.current_version.star_count.4"));
            appRatingMap.put("5", parser.parseString(response.getBody(), "content.current_version.star_count.5"));
        }else {
            throw new RuntimeException();
        }

        return appRatingMap;
    }

}
