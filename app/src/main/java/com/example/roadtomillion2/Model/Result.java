package com.example.roadtomillion2.Model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("address_components")
    @Expose
    private ArrayList<AddressComponent> addressComponents = null;

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;





    @SerializedName("types")
    @Expose
    private ArrayList<String> types = null;

    public ArrayList<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(ArrayList<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }



    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

}