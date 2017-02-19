package com.example.laptop.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Laptop on 31-Jan-17.
 */

public class MyCursorAdapter extends CursorAdapter{


    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_favorites, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView placeNameTV = (TextView) view.findViewById(R.id.place_name);
        TextView placeAddressTV = (TextView) view.findViewById(R.id.place_address);
        ImageView placeImageIV = (ImageView) view.findViewById(R.id.place_image);
        // Extract properties from cursor
        String placeName = cursor.getString(cursor.getColumnIndexOrThrow("placename"));
        String placeAddress = cursor.getString(cursor.getColumnIndexOrThrow("placeaddress"));
        String placeImage = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
        // Populate fields with extracted properties



        placeNameTV.setText(placeName);
        placeAddressTV.setText(placeAddress);
        Picasso.with(context).load(placeImage).fit().into(placeImageIV);
    }


}
