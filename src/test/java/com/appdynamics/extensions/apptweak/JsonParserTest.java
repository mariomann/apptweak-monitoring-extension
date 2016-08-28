package com.appdynamics.extensions.apptweak;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonParserTest {

    JsonParser jsonParser = new JsonParser();
    String json = "{\n" +
            "\t\"content\": {" +
            "\t\t\"average\": 3.7971904277801514," +
            "\t\t\"count\": 11390,\n" +
            "\t\t\"star_count\": {\n" +
            "\t\t\t\"1\": 1799,\n" +
            "\t\t\t\"2\": 705,\n" +
            "\t\t\t\"3\": 1033,\n" +
            "\t\t\t\"4\": 2323,\n" +
            "\t\t\t\"5\": 5530\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"metadata\": {\n" +
            "\t\t\"request\": {\n" +
            "\t\t\t\"path\": \"/applications/com.db.mm.deutschebank/ratings.json\",\n" +
            "\t\t\t\"store\": \"android\",\n" +
            "\t\t\t\"params\": {\n" +
            "\t\t\t\t\"country\": \"de\",\n" +
            "\t\t\t\t\"language\": \"en\",\n" +
            "\t\t\t\t\"id\": \"com.db.mm.deutschebank\",\n" +
            "\t\t\t\t\"format\": \"json\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"performed_at\": \"2016-08-18 13:08:02 UTC\"\n" +
            "\t\t},\n" +
            "\t\t\"content\": {}\n" +
            "\t}\n" +
            "}";

    @Test
    public void parseAverageValue() {
        String averageValue = jsonParser.parseString(json, "content.average");

        assertThat(averageValue, is(equalTo("3.7971904277801514")));
    }

    @Test
    public void parseCountValue() {
        int count = jsonParser.parseInteger(json, "content.count");
        assertThat(count, is(11390));
    }

    @Test
    public void parseStarCount() {
        int starCountOne = jsonParser.parseInteger(json,"content.star_count.1");

        assertThat(starCountOne, is(1799));
    }

    @Test
    public void parseDecimalValue() {
        int value = Integer.valueOf(jsonParser.parseString(json, "content.average").replace(".", "").substring(0, 4));

        assertThat(value, is(3797));
    }
}

