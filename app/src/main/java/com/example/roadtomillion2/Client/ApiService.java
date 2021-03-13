package com.example.roadtomillion2.Client;

import com.example.roadtomillion2.Model.GeoRequest;
import com.example.roadtomillion2.Model.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("weather?units=metric")
    Call<WeatherRequest> getMyJson(@Query("lat") String lat, @Query("lon") String lng, @Query("appid") String apiKey);

    @GET("json?")
    Call<GeoRequest> getMyGeo(@Query("latlng") String latlng, @Query("key") String apiKey);
}
//latlng=42.33871166666667,-7.864115&key=AIzaSyD_rPLmE9W32frK6P3d0ZDQeNnmX_FIyRc
