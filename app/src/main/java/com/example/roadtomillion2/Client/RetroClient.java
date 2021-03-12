package com.example.roadtomillion2.Client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    static String URL = "https://api.openweathermap.org/data/2.5/";

    public static Retrofit getInstance(){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService(){
        return getInstance().create(ApiService.class);
    }
}
