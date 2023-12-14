package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private static final String API_BASE_URL = "https://api.worldoftanks.eu/wot/";

    private static final String APPLICATION_ID = System.getenv("WOT_APPLICATION_ID");
    private static final String ACCESS_TOKEN = System.getenv("WOT_ACCESS_TOKEN");

    private static final String CLAN_ID = "500138842";

    public static void main(String[] args) throws Exception {
        printNames();
    }

    public static void printNames() throws Exception {
        String onlineJson = getRequestOnline("clans/info/?application_id=" + APPLICATION_ID +
                "&access_token=" + ACCESS_TOKEN + "&clan_id=" + CLAN_ID + "&extra=private.online_members&fields=private.online_members");
        JSONArray online = formatOnline(onlineJson);

        for (int i = 0; i < online.length(); i++) {
            System.out.println(formatNickname(getRequestNickname("account/info/?application_id=" + APPLICATION_ID +
                    "&account_id=" + online.get(i) + "&fields=nickname"), online.getInt(i)));
        }
    }

    public static String getRequestOnline(String endpoint) throws Exception {
        URL url = new URL(API_BASE_URL + endpoint);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    public static JSONArray formatOnline(String text) throws Exception {
        JSONObject jsonObject = new JSONObject(text);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject privateObject = data.getJSONObject(CLAN_ID + "").getJSONObject("private");
        return privateObject.getJSONArray("online_members");
    }

    public static String getRequestNickname(String endpoint) throws Exception {
        URL url = new URL(API_BASE_URL + endpoint);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    public static JSONObject formatNickname(String text, int id) throws Exception {
        JSONObject jsonObject = new JSONObject(text);
        JSONObject data = jsonObject.getJSONObject("data");
        return data.getJSONObject(String.valueOf(id));
    }
}
