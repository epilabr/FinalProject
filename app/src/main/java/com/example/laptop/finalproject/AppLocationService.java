package com.example.laptop.finalproject;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AppLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    static int searchRadius = 500;
    String lat;
    String lng;


    public AppLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("AppLocationServiceStart","AppLocationServiceStart");
        buildGoogleApiClient();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Intent broadcastMessage0 = new Intent("finalProject.STARTED"); //sends the appropriate broadcast message
        LocalBroadcastManager.getInstance(AppLocationService.this).sendBroadcast(broadcastMessage0);


        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        startLocationUpdate();

        lat = String.valueOf(mLastLocation.getLatitude());
        lng = String.valueOf(mLastLocation.getLongitude());


        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=" + searchRadius + "&language=iw" + "&key=AIzaSyDmDuBBI1JVwkKp8VtmyIwzhz4Nujl_Xvo";
        DownloadWebsite downloadWebsite= new DownloadWebsite();
        downloadWebsite.execute(url);


    }



    private void startLocationUpdate() {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(9000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.e("LocationChanged","LocationChanged");
        String myLocationLat = String.valueOf(location.getLatitude());
        String myLocationLng = String.valueOf(location.getLongitude());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("myLocationLat",myLocationLat);
        editor.putString("myLocationLng",myLocationLng);

        editor.apply();
    }

    public  class DownloadWebsite extends AsyncTask<String, Integer, String > {


        @Override
        protected String doInBackground(String... params) {


            int lineConut = 0;
            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                }
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = "";
                while ((line = input.readLine()) != null) {
                    response.append(line + "\n");
                    lineConut++;
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

        @Override
        protected void onPostExecute(String json) {
            Log.d("jason trace", json);
            Intent broadcastMessage = new Intent("finalProject.FINISHED");  //sends the appropriate broadcast message
            broadcastMessage.putExtra("json", json);  //sends the json to the SearchFragment with putExtra(intent) and the "finished" announcement
            broadcastMessage.putExtra("lat", lat);
            broadcastMessage.putExtra("lng", lng);
            LocalBroadcastManager.getInstance(AppLocationService.this).sendBroadcast(broadcastMessage);
        }

    }


    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();

    }
}
