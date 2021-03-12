package com.example.roadtomillion2;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.roadtomillion2.Model.MyLocation;

class MyLocationListener implements LocationListener {

    private static final String TAG = "LocationListener";
    private Context context;



    public MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(
                context,
                "Location changed: Lat: " + location.getLatitude() + " Lng: "
                        + location.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + location.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + location.getLatitude();
        Log.v(TAG, latitude);
        MyLocation myLocation = new MyLocation(location);


        MainActivity.getFromInternet();
    }



}