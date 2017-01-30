package com.example.laptop.finalproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/*==this class is custom made to show the list of places==*/

public class MyListAdapter extends ArrayAdapter{

    List<Place> allPlaces; //array of places
    Context c;

    public MyListAdapter(Context context, int resource, List objects) {
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
        String thisImage= "https://maps.googleapis.com/maps/api/place/photo?maxheight=110&maxwidth=110&photoreference="+tempPlace.image+"&key=AIzaSyDmDuBBI1JVwkKp8VtmyIwzhz4Nujl_Xvo";
        nameTV.setText(""+tempPlace.name);
        addressTV.setText(""+tempPlace.address);
        distanceTV.setText(""+tempPlace.address);
        Picasso.with(c).load(thisImage).fit().into(pictureView);  // loads the picture using picasso

        return v;
    }
}
