package com.example.laptop.finalproject;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SearchIntentService extends IntentService {

    String searchText;
    String ifChecked;
    String searchRadius;
    String units;

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.example.laptop.finalproject.action.FOO";
    public static final String ACTION_BAZ = "com.example.laptop.finalproject.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.example.laptop.finalproject.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.laptop.finalproject.extra.PARAM2";

    public SearchIntentService() {
        super("SearchIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            searchText = intent.getStringExtra("searchText");
            ifChecked = intent.getStringExtra("ifChecked");
            searchRadius = intent.getStringExtra("searchRadius");
            units = intent.getStringExtra("units");

            String json = downloadJson(searchText.replaceAll(" ", "%20"));
            Log.d("text", json);
            Intent broadcastMessage = new Intent("finalProject.FINISHED");
            broadcastMessage.putExtra("json", json);
            LocalBroadcastManager.getInstance(SearchIntentService.this).sendBroadcast(broadcastMessage);
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
    }

    private String downloadJson(String searchkey) {


        BufferedReader input = null;
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();

        try {
            if (ifChecked.equals("1")) {

                URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + "lat" + "," + "lng" + "&radius=" + searchRadius + "&name=" + searchkey + "&key=AIzaSyDmDuBBI1JVwkKp8VtmyIwzhz4Nujl_Xvo");

                connection = (HttpURLConnection) url.openConnection();
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = input.readLine()) != null) {
                    response.append(line + "\n");
                }
            } else {

                URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + searchkey + "&key=AIzaSyDmDuBBI1JVwkKp8VtmyIwzhz4Nujl_Xvo");

                connection = (HttpURLConnection) url.openConnection();
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = input.readLine()) != null) {
                    response.append(line + "\n");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }
}
