package com.example.roadtomillion2.Client;

import com.example.roadtomillion2.Model.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("weather?units=metric")
    Call<WeatherRequest> getMyJson(@Query("lat") String lat, @Query("lon") String lng, @Query("appid") String apiKey);
}
