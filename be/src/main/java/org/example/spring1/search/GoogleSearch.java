package org.example.spring1.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;

public class GoogleSearch {
    private Map<String, String> parameters;

    public GoogleSearch(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonObject getJson() throws Exception {
        StringBuilder urlBuilder = new StringBuilder("https://serpapi.com/search.json?");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            urlBuilder.append(encodeValue(entry.getKey()))
                    .append("=")
                    .append(encodeValue(entry.getValue()))
                    .append("&");
        }
        urlBuilder.append("api_key=")
                .append(encodeValue("7d653d7d8aa9e6e9cb5a71e4bc92d8aaee1517916319c2272bbef3fab4dcbfd7"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();
        return json;
    }
}
