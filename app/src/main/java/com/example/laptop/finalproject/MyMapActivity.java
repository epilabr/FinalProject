package com.example.laptop.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static java.lang.Double.parseDouble;


public class MyMapActivity extends AppCompatActivity {

    String name;
    String address;
    double lat;
    double lng;
    double myLocationLat;
    double myLocationLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);

        Intent mapIntent = getIntent();

        name = mapIntent.getStringExtra("name");
        address = mapIntent.getStringExtra("address");
        lat = parseDouble(mapIntent.getStringExtra("lat"));
        lng = parseDouble(mapIntent.getStringExtra("lng"));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        myLocationLat = parseDouble(preferences.getString("myLocationLat", ""));
        myLocationLng = parseDouble(preferences.getString("myLocationLng", ""));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                LatLng targetPosition = new LatLng(lat, lng);
                LatLng myPosition = new LatLng(myLocationLat, myLocationLng);

                googleMap.addMarker(new MarkerOptions().position(targetPosition).title(name)).showInfoWindow();
                googleMap.addMarker(new MarkerOptions().position(myPosition).title(getString(R.string.youAreHere)));

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(targetPosition, 15);
                googleMap.moveCamera(update);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
