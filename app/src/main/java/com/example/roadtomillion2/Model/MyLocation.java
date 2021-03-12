package com.example.roadtomillion2.Model;

import android.location.Location;

public class MyLocation extends Location {
    private static Location location;

    public MyLocation(Location l) {
        super(l);
        this.location = l;
    }

    static String getLat(){
        return String.valueOf(location.getLatitude());
    }

    static String getLng(){
        return String.valueOf(location.getLongitude());
    }
}
