package com.example.laptop.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;


public class DbCommands {

    Context context;
    MySqlOpenHelper helper;

    public DbCommands(Context c) {

        context = c;
        helper = new MySqlOpenHelper(context);
    }

    public  void addPlaceFavorites(Place place)
    {
        ContentValues cv= new ContentValues();
        cv.put(DbConstants.placeName, place.name);
        cv.put(DbConstants.placeAddress, place.address);
        cv.put(DbConstants.placeLat, place.latitude);
        cv.put(DbConstants.placeLng, place.longitude);
        cv.put(DbConstants.placePic, place.picToSaveByteArray);

        helper.getWritableDatabase().insert(DbConstants.favoritesTableName, null,cv );
    }



    public  void addPlaceSearch(Place place)
    {
        ContentValues cv= new ContentValues();
        cv.put(DbConstants.placeName, place.name);
        cv.put(DbConstants.placeAddress, place.address);
        cv.put(DbConstants.placeLat, place.latitude);
        cv.put(DbConstants.placeLng, place.longitude);
        cv.put(DbConstants.placePic, place.picToSaveByteArray);

        helper.getWritableDatabase().insert(DbConstants.searchTableName, null,cv );
    }

    public void addAllPlaces(ArrayList<Place> clonedList){

        for (Place place:clonedList){

            ContentValues cv= new ContentValues();
            cv.put(DbConstants.placeName, place.name);
            cv.put(DbConstants.placeAddress, place.address);
            cv.put(DbConstants.placeLat, place.latitude);
            cv.put(DbConstants.placeLng, place.longitude);
            cv.put(DbConstants.placePic, place.picToSaveByteArray);

            helper.getWritableDatabase().insert(DbConstants.searchTableName, null,cv );
        }


    }


    public Cursor getDataFromDBAsCursor()
    {
        Cursor tempTableDataCursor=   helper.getReadableDatabase().rawQuery("SELECT * FROM "+DbConstants.favoritesTableName, null);

        return  tempTableDataCursor;
    }

    public Cursor getDataFromDBAsCursor(int placeID)
    {
        Cursor tempTableDataCursor=   helper.getReadableDatabase().rawQuery("SELECT * FROM "+DbConstants.favoritesTableName+" WHERE _id="+placeID , null);

        return  tempTableDataCursor;
    }

    public Cursor getDataFromDBAsCursorSearch(String placeName)
    {
        placeName = placeName.replaceAll("'","''");
        Cursor tempTableDataCursor=   helper.getReadableDatabase().rawQuery("SELECT * FROM "+DbConstants.searchTableName+" WHERE "+DbConstants.placeName+"='"+placeName+"'" , null);

        return  tempTableDataCursor;
    }




    public void deletePlacesDB() {

        helper.getWritableDatabase().delete(DbConstants.favoritesTableName, null, null);
    }

    public void deleteSearchPlacesDB() {

        helper.getWritableDatabase().delete(DbConstants.searchTableName, null, null);
    }




    public ArrayList<Place> getAllPlaces()
    {
        ArrayList<Place> allPlaces= new ArrayList<>();

        Cursor cursor= helper.getReadableDatabase().query(DbConstants.favoritesTableName,null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            int nameColumn= cursor.getColumnIndex(DbConstants.placeName);
            String placeName= cursor.getString(nameColumn);

            int addressColumn= cursor.getColumnIndexOrThrow(DbConstants.placeAddress);
            String placeAddress= cursor.getString(addressColumn);

            int latColumn= cursor.getColumnIndexOrThrow(DbConstants.placeLat);
            String placeLat= cursor.getString(latColumn);

            int lngColumn= cursor.getColumnIndexOrThrow(DbConstants.placeLng);
            String placeLng= cursor.getString(lngColumn);

            int picColumn= cursor.getColumnIndexOrThrow(DbConstants.placePic);
            byte[] placePicByte= cursor.getBlob(picColumn);

            Bitmap placePic = BitmapFactory.decodeByteArray(placePicByte,0,placePicByte.length);  //preforms the decode from the blob that was stored - to bitmap

            Place placeTemp= new Place(placeName,placeAddress,placeLat,placeLng,placePic);
            allPlaces.add(placeTemp);

        }
        return allPlaces;
    }

    public ArrayList<Place> getAllPlacesSearch()
    {
        ArrayList<Place> allPlaces= new ArrayList<>();

        Cursor cursor= helper.getReadableDatabase().query(DbConstants.searchTableName,null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            int nameColumn= cursor.getColumnIndex(DbConstants.placeName);
            String placeName= cursor.getString(nameColumn);

            int addressColumn= cursor.getColumnIndexOrThrow(DbConstants.placeAddress);
            String placeAddress= cursor.getString(addressColumn);

            int latColumn= cursor.getColumnIndexOrThrow(DbConstants.placeLat);
            String placeLat= cursor.getString(latColumn);

            int lngColumn= cursor.getColumnIndexOrThrow(DbConstants.placeLng);
            String placeLng= cursor.getString(lngColumn);

            int picColumn= cursor.getColumnIndexOrThrow(DbConstants.placePic);
            byte[] placePicByte= cursor.getBlob(picColumn);

            Bitmap placePic = BitmapFactory.decodeByteArray(placePicByte,0,placePicByte.length);  //preforms the decode from the blob that was stored - to bitmap

            Place placeTemp= new Place(placeName,placeAddress,placeLat,placeLng,placePic);
            allPlaces.add(placeTemp);

        }
        return allPlaces;
    }
}
