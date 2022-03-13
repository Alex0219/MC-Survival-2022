package de.fileinputstream.mytraz.worldmanagement.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;



public class UUIDFetcher {

    public static String getUUID(String username) {
        try {
            URL url = new URL("https://control.mc-survival.de/uuid?username=" + username);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = reader.readLine();

            final JSONParser parser = new JSONParser();

            try {
                final JSONObject json = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));

                if (json.get("uuid") != null) {
                    String uuid = json.get("uuid").toString();
                    return uuid;
                }


            } catch (ParseException e) {

            }


        } catch (IOException e) {
        }

        return "";


    }

    public static String getName(String uuid) {
        try {
            URL url = new URL("https://control.mc-survival.de/name?uuid=" + uuid);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = reader.readLine();

            final JSONParser parser = new JSONParser();

            try {
                final JSONObject json = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));

                if (json.get("username") != null) {
                    String username = json.get("username").toString();
                    return username;
                }


            } catch (ParseException e) {

            }


        } catch (IOException e) {
        }
        return "";
    }
}

