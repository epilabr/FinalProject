package com.example.laptop.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * Created by Laptop on 01-Feb-17.
 */

public class MyFavoritesAdapter extends ArrayAdapter {

    List<Place> allPlaces; //array of places
    Context c;

    public MyFavoritesAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);

        allPlaces=objects;
        c=context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v= convertView;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(c);
            v= inflater.inflate(R.layout.place_layout, null);
        }

        TextView nameTV= (TextView) v.findViewById(R.id.place_name);
        TextView addressTV= (TextView) v.findViewById(R.id.place_address);
        TextView distanceTV= (TextView) v.findViewById(R.id.place_distance);
        ImageView pictureView= (ImageView) v.findViewById(R.id.place_image);

        final Place tempPlace= allPlaces.get(position);

        Location loc1 = new Location("");
        loc1.setLatitude(parseDouble(tempPlace.latitude));
        loc1.setLongitude(parseDouble(tempPlace.longitude));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        double myLocationLat = parseDouble(preferences.getString("myLocationLat", ""));
        double myLocationLng = parseDouble(preferences.getString("myLocationLng", ""));
        String unites = preferences.getString("unites", "");

        Location loc2 = new Location("");
        loc2.setLatitude(myLocationLat);
        loc2.setLongitude(myLocationLng);

        float distanceInKilometers = loc1.distanceTo(loc2);
        String distanceFromYou = c.getString(R.string.distanceFromYou);
        double distanceInMiles = 0.6214*distanceInKilometers;

        nameTV.setText(""+tempPlace.name);
        addressTV.setText(""+tempPlace.address);

        if (unites.equals(c.getString(R.string.metric))){
            distanceTV.setText(distanceFromYou+" "+(float)((int)distanceInKilometers)/1000 + " "+unites);
        }else {
            distanceTV.setText(distanceFromYou+" "+(float)((int)distanceInMiles)/1000 + " "+unites);
        }

        pictureView.setImageBitmap(tempPlace.picToLoadByteArray);

        return v;
    }
}
