package com.tamimi.sundos.restpos;


import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.BackOffice.BackOfficeActivity;

public class InternetConnection extends Application {

    public static boolean isConnected = false;
    /**
     * To receive change in network state
     */
    private BroadcastReceiver NetworkStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

//            connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            networkInfo.isCo();

            isConnected = networkInfo != null && networkInfo.isConnected();


            if (!isConnected) {
                Toast noInternetToast = Toast.makeText(getApplicationContext(),
                       "work Off line ", Toast.LENGTH_LONG);
                noInternetToast.show();
                Settings.onOFF=false;
                new Settings().blinkAnnouncement(false);

            }else {
                Toast noInternetToast = Toast.makeText(getApplicationContext(),
                        "work On Line ", Toast.LENGTH_LONG);
                noInternetToast.show();
                Settings.onOFF=true;
//                new Settings().makeText(context, "internet_msg");
                new Settings().blinkAnnouncement( true);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        /** Register for CONNECTIVITY_ACTION broadcasts */
        registerReceiver(NetworkStatusReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(NetworkStatusReceiver);
    }
}