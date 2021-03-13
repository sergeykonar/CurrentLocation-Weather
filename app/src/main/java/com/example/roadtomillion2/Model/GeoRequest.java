package com.example.roadtomillion2.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GeoRequest {

    @SerializedName("results")
    @Expose
    private ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }
}
