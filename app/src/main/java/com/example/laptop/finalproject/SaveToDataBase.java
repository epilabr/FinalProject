package com.example.laptop.finalproject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Laptop on 02-Feb-17.
 */

public class SaveToDataBase extends AsyncTask<ArrayList<Place>,Void,Void>{

    private Context c;

    public SaveToDataBase (Context context){
        c = context;
    }

    @Override
    protected Void doInBackground(ArrayList<Place>... params) {
        ImageView imageView;
        ArrayList<Place> jasonPlaces = params[0];
        ArrayList<Place> clonedList = new ArrayList<>(jasonPlaces.size());
        for (Place place : jasonPlaces) {
           // imageView = (R.id.place_image);
          //  clonedList.add(new Place(place,imageView));
        }

        DbCommands commands = new DbCommands(c);
        commands.addAllPlaces(clonedList);

        return null;
    }
}
