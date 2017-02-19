package com.example.laptop.finalproject;

/*===this class shows the Place object and its different fields===*/

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.RequestCreator;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class Place {

    String name;
    String address;
    String latitude;
    String longitude;
    String  photoImage;
    byte[]  picToSaveByteArray;
    Bitmap  picToLoadByteArray;
    ImageView v;


    public Place(String name, String address, String latitude, String longitude, String photoImage) {  //constructor...

        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoImage = photoImage;
    }

    public Place(String name, String address, String latitude, String longitude, byte[] picToSaveByteArray) {  //constructor...

        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.picToSaveByteArray = picToSaveByteArray;
    }

    public Place(String name, String address, String latitude, String longitude, Bitmap picToLoadByteArray) {  //constructor...

        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.picToLoadByteArray = picToLoadByteArray;
    }

    public Place(Place place,ImageView imageViewToSave){
          name = place.name;
          address = place.address;
          latitude = place.latitude;
          longitude = place.longitude;
          picToSaveByteArray = changeToByteCode(imageViewToSave);

    }

    public  byte[]  changeToByteCode(ImageView imageViewToSave){

        Bitmap bitmapToSave = ((BitmapDrawable)imageViewToSave.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapToSave .compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] saveByteArray = bos.toByteArray();

        return saveByteArray;

    }



    @Override
    public String toString() {
        return this.name;
    }
}
