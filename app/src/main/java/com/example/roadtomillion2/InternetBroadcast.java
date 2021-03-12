package com.example.roadtomillion2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class InternetBroadcast extends BroadcastReceiver {

    private final View view;

    public InternetBroadcast(View view) {
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int state = intent.getIntExtra("wifi_state", 0);
        if(state == WifiManager.WIFI_STATE_DISABLED){
            Snackbar.make(view, "Wifi disabled", Snackbar.LENGTH_LONG).setAction("HIDE", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Tag", "SNACKBAR CLICK");
                }
            }).show();
            Log.d("TAG", "STATE DISABLED");
        }else{
            MainActivity.getFromInternet();
        }
    }


}
