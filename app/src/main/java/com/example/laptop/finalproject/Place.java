package com.example.laptop.finalproject;

/*===this class shows the Place object and its different fields===*/

public class Place {

    String name;
    String address;
    String latitude;
    String longitude;
    String image;

    public Place(String name, String latitude, String longitude, String address, String image) {  //constructor...
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.image = image;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
