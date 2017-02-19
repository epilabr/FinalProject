package com.example.laptop.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static java.lang.Double.parseDouble;

/*==this class is custom made to show the list of places==*/

public class MyListAdapter extends ArrayAdapter{


    List<Place> allPlaces; //array of places
    Context c;
    private LayoutInflater inflater;

    public MyListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);

        allPlaces=objects;
        c=context;
        inflater = ((Activity)c).getLayoutInflater();

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
        final ImageView pictureView= (ImageView) v.findViewById(R.id.place_image);

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
        double distanceInMiles = 0.6214*distanceInKilometers;

        String distanceFromYou = c.getString(R.string.distanceFromYou);

        nameTV.setText(""+tempPlace.name);
        addressTV.setText(""+tempPlace.address);

        if (unites.equals(c.getString(R.string.metric))){
            distanceTV.setText(distanceFromYou+" "+(float)((int)distanceInKilometers)/1000 + " "+unites);
        }else {
            distanceTV.setText(distanceFromYou+" "+(float)((int)distanceInMiles)/1000 + " "+unites);
        }

        if (tempPlace.photoImage.equals("no_picture")) {
            DbCommands dbCommands = new DbCommands(c);
            Cursor cursor = dbCommands.getDataFromDBAsCursorSearch(tempPlace.name);
            if (cursor.getCount() == 0) {
                Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.ic_menu_gallery);
                byte[] pic = changeToByteCode(bm);
                Place place = new Place(tempPlace.name, tempPlace.address, tempPlace.latitude, tempPlace.longitude, pic);
                dbCommands.addPlaceSearch(place);
            }
        }


        Picasso.with(getContext()).load(tempPlace.photoImage)
                .placeholder(android.R.drawable.ic_menu_gallery).fit()
                .into(pictureView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        new Thread(new Runnable() {

                            public void run() {
                                DbCommands dbCommands = new DbCommands(c);
                                Cursor cursor = dbCommands.getDataFromDBAsCursorSearch(tempPlace.name);
                                if (cursor.getCount() == 0){
                                    BitmapDrawable drawable = (BitmapDrawable) pictureView.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();
                                    byte[] bytePicture = changeToByteCode(bitmap);
                                    Place place = new Place(tempPlace.name, tempPlace.address, tempPlace.latitude, tempPlace.longitude, bytePicture);
                                    dbCommands.addPlaceSearch(place);
                                }
                            }
                        }).start();

                    }

                    @Override
                    public void onError() {
                        pictureView.setImageResource(android.R.drawable.ic_menu_gallery);

                    }
                });

        return v;


    }

    public  byte[]  changeToByteCode(Bitmap bitmap){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap .compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] saveByteArray = bos.toByteArray();

        return saveByteArray;

    }
}
