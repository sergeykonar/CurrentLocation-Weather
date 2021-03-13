package com.example.roadtomillion2.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {


    @SerializedName("icon")
    @Expose
    private String icon;



    public String getIcon() {
        return icon;
    }



}
