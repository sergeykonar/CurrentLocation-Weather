package com.example.roadtomillion2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.roadtomillion2.Client.ApiService;
import com.example.roadtomillion2.Client.RetroClient;
import com.example.roadtomillion2.Model.MyLocation;
import com.example.roadtomillion2.Model.WeatherRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private InternetBroadcast internetBroadcast;
    private IntentFilter filter;
    private static String TAG = "Main";
    private static TextView text;
    private static String apiKey = "fae986984f570eebcecab44a59a07640";

    static private MyLocationListener locationListener;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text);
        // Broadcast receiver for looking when the Wifi was disabled
        internetBroadcast = new InternetBroadcast(findViewById(R.id.mainLayout));
        filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); // Adding an action that I want to follow
        registerReceiver(internetBroadcast, filter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(internetBroadcast, filter);

        Log.d(TAG, "onResume()");

    }

        static void getFromInternet(){
        try {
            ApiService api = RetroClient.getApiService();
            Call<WeatherRequest> call = api.getMyJson(MyLocation.getLat(), MyLocation.getLng(), apiKey);
            call.enqueue(new Callback<WeatherRequest>() {
                @Override
                public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                    if (response.isSuccessful()){
                        WeatherRequest list = response.body();
                        String temp = list.getMain().getTemp().toString();
                        text.setText(temp);
                        Log.d(TAG, call.toString());
                    }else{
                        Log.e(TAG, "No response");
                        Log.e(TAG, response.message());
                    }
                }

                @Override
                public void onFailure(Call<WeatherRequest> call, Throwable t) {
                    Log.e(TAG, String.valueOf(t));
                    Log.e(TAG, call.toString());
                }
            });
        }catch (Exception e){

        }


    }
}