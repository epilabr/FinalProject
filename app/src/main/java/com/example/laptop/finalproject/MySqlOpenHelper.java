package com.example.laptop.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MySqlOpenHelper extends SQLiteOpenHelper {


    public MySqlOpenHelper(Context context) {
        super(context, DbConstants.databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String searchCommand="CREATE TABLE  "+DbConstants.searchTableName+" ( "+DbConstants.idColumn+"  INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+ DbConstants.placeName +" TEXT," +
                " "+ DbConstants.placeAddress +" TEXT," +
                " "+ DbConstants.placeLat +" TEXT," +
                " "+ DbConstants.placeLng+" TEXT," +
                " "+ DbConstants.placePic+" BLOB);";

        String favoritesCommand="CREATE TABLE  "+DbConstants.favoritesTableName+" ( "+DbConstants.idColumn+"  INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+ DbConstants.placeName +" TEXT," +
                " "+ DbConstants.placeAddress +" TEXT," +
                " "+ DbConstants.placeLat +" TEXT," +
                " "+ DbConstants.placeLng+" TEXT," +
                " "+ DbConstants.placePic+" BLOB);";

        db.execSQL(searchCommand);
        db.execSQL(favoritesCommand);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
