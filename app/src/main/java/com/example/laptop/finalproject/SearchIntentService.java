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

/*======this service preforms in it's one thread the search action ===========*/

public class SearchIntentService extends IntentService {

    String searchText;  // the place we want to search
    String ifChecked;   //if the user chose to use proximity search
    String searchRadius;  //how close to user location to search
    String units;      // should we use kilometers or miles

    public SearchIntentService() {
        super("SearchIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {  //the hard working thread that does most of the work
        if (intent != null) {     //receives the intent with all the extras
            searchText = intent.getStringExtra("searchText");
            ifChecked = intent.getStringExtra("ifChecked");
            searchRadius = intent.getStringExtra("searchRadius");
            units = intent.getStringExtra("units");

            Intent broadcastMessage0 = new Intent("finalProject.STARTED"); //sends the appropriate broadcast message
            LocalBroadcastManager.getInstance(SearchIntentService.this).sendBroadcast(broadcastMessage0);

            String json = downloadJson(searchText.replaceAll(" ", "%20")); //calls for downloading the json
            Log.d("jason trace", json);  //logs the json for debug purposes only
            Intent broadcastMessage = new Intent("finalProject.FINISHED");  //sends the appropriate broadcast message
            broadcastMessage.putExtra("json", json);  //sends the json to the SearchFragment with putExtra(intent) and the "finished" announcement
            LocalBroadcastManager.getInstance(SearchIntentService.this).sendBroadcast(broadcastMessage);
        }
    }


    private String downloadJson(String searchKey) {  //preforms the api call and downloads the response json

        BufferedReader input = null;
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();

        try {
            if (ifChecked.equals("1")) {
                //search by keywords and search radius that the user chose
                URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + "lat" + "," + "lng" + "&radius=" + searchRadius + "&name=" + searchKey + "&key=AIzaSyDmDuBBI1JVwkKp8VtmyIwzhz4Nujl_Xvo");

                connection = (HttpURLConnection) url.openConnection();
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = input.readLine()) != null) {
                    response.append(line + "\n");
                }
            } else {
                //search by keywords only
                URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + searchKey + "&key=AIzaSyDmDuBBI1JVwkKp8VtmyIwzhz4Nujl_Xvo");

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
        return response.toString();   //returns the response to the
    }
}
