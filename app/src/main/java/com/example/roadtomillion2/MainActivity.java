package com.example.roadtomillion2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadtomillion2.Client.ApiService;
import com.example.roadtomillion2.Client.RetroClient;
import com.example.roadtomillion2.Model.AddressComponent;
import com.example.roadtomillion2.Model.GeoRequest;
import com.example.roadtomillion2.Model.MyLocation;
import com.example.roadtomillion2.Model.Result;
import com.example.roadtomillion2.Model.WeatherRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private InternetBroadcast internetBroadcast;
    private IntentFilter filter;
    private static String TAG = "Main";
    private static TextView text;
    private static String apiKey = "fae986984f570eebcecab44a59a07640";
    private static String apiKeyLocation = "AIzaSyD_rPLmE9W32frK6P3d0ZDQeNnmX_FIyRc";

    static private MyLocationListener locationListener;
    static private String locality = "";

    private static TextView localityText;
    private static ImageView iconWeather;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text);
        localityText = (TextView) findViewById(R.id.locality);
        // Broadcast receiver for looking when the Wifi was disabled
        internetBroadcast = new InternetBroadcast(findViewById(R.id.mainLayout));
        filter = new IntentFilter();

        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); // Adding an action that I want to follow
        registerReceiver(internetBroadcast, filter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Weather");
        toolbar.setTitleTextColor(R.color.black);
        setSupportActionBar(toolbar);



        iconWeather = (ImageView) findViewById(R.id.weatherIcon);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_info){
            Toast.makeText(this, "HI", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);



        registerReceiver(internetBroadcast, filter);
        Log.d(TAG, "onResume()");

    }

    public static void getFromInternet(){
        try {
            getLocationName();
            getWeatherApi();

        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    private static void getLocationName() {
        ApiService apiServiceGeo = RetroClient.getApiServiceGeo();
        String latlng = MyLocation.getLat() +","+MyLocation.getLng();
        Call<GeoRequest> callGeo = apiServiceGeo.getMyGeo(latlng, apiKeyLocation);
        callGeo.enqueue(new Callback<GeoRequest>() {
            @Override
            public void onResponse(Call<GeoRequest> call, Response<GeoRequest> response) {
                if (response.isSuccessful() ){
                    GeoRequest geoRequest = response.body();
//                        String s = geoRequest.getResults().get(0).getAddressComponents().get(0).getTypes().get(0).toString();

                    for (int i=0; i < response.body().getResults().size(); i++){
                        int addressComponentsSize = geoRequest.getResults().get(0).getAddressComponents().size();
                        for(AddressComponent addressComponent: geoRequest.getResults().get(0).getAddressComponents()){
                            ArrayList<String> types = addressComponent.getTypes();
                            for (String string : types){
                                if (string.equals("locality")){
                                    locality = addressComponent.getLongName();
                                    Log.d(TAG, "Place: " + locality);
                                    localityText.setText(locality);
                                }
                            }
                        }

                    }

                    Log.d(TAG, "Locality received successfully");
                }else {
                    Log.e(TAG, String.valueOf(response.errorBody()));
                    Log.e(TAG, "Error occurred during request: " + response.message());
                }


            }

            @Override
            public void onFailure(Call<GeoRequest> call, Throwable t) {
                Log.e(TAG, "Error occurred during connection:  " + t.toString());
            }
        });
    }

    // http://openweathermap.org/img/wn/  01d @2x.png
    private static void getWeatherApi(){
        ApiService api = RetroClient.getApiService();
        Call<WeatherRequest> call = api.getMyJson(MyLocation.getLat(), MyLocation.getLng(), apiKey);
        call.enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response.isSuccessful()){
                    WeatherRequest list = response.body();
                    String temp = list.getMain().getTemp().toString();
                    String icon = list.getWeather().get(0).getIcon();

                    String url = "http://openweathermap.org/img/wn/"+icon+"@2x.png";
                    Picasso.get().load(url).resize(300,300).into(iconWeather);
                    Log.d(TAG, "Weather icon loaded successfully: " + url);

                   double mTemp = Double.parseDouble(temp);

                    String s2 = String.valueOf(Math.round(mTemp));
                    String s;

                    if (mTemp >= 0){
                        s = "+"+ s2;
                    }
                    else {
                        s = "" +s2;
                    }

                    text.setText(s);
                    Log.d(TAG, "Weather received successfully + ");
                }else{
                    Log.e(TAG, String.valueOf(response.errorBody()));
                    Log.e(TAG, "Error occurred during request: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                Log.e(TAG, "Error occurred during connection:1 " + t.toString());

            }
        });
    }
}