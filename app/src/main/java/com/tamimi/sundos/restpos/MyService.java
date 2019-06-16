package com.tamimi.sundos.restpos;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.PayMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Belal on 12/30/2016.
 */

public class MyService extends Service {
    //creating a mediaplayer object
    DatabaseHandler db = new DatabaseHandler(MyService.this);
    Timer T;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        T = new Timer();

        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<OrderHeader> OrderHeaderObj = new ArrayList<>();
                List<OrderTransactions> OrderTransactionsObj = new ArrayList<>();
                List<PayMethod> PayMethodObj = new ArrayList<>();

                OrderHeaderObj = db.getAllOrderHeader();
                OrderTransactionsObj = db.getAllOrderTransactions();
                PayMethodObj = db.getAllExistingPay();

                for (int i = 0; i < OrderHeaderObj.size(); i++) {
                    if(OrderHeaderObj.get(i).getIsPost()!=1){
                    sendToServer(OrderHeaderObj.get(i), OrderTransactionsObj, PayMethodObj);
                    }
                }
//                message();

            }
        }, 10000, 3000);

        //START_REDELIVER_INTENT

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed

    }


    void sendToServer(OrderHeader OrderHeaderObj, List<OrderTransactions> OrderTransactionsObj, List<PayMethod> PayMethodObj) {
        try {
            JSONArray obj2 = new JSONArray();
            for (int i = 0; i < OrderTransactionsObj.size(); i++) {
                if (OrderHeaderObj.getVoucherNumber().equals(OrderTransactionsObj.get(i).getVoucherNo()))
                    obj2.put(OrderTransactionsObj.get(i).getJSONObject2());
            }
            JSONObject obj1 = OrderHeaderObj.getJSONObject2();

            JSONArray obj3 = new JSONArray();
            for (int i = 0; i < PayMethodObj.size(); i++) {
                if (OrderHeaderObj.getVoucherNumber().equals(PayMethodObj.get(i).getVoucherNumber()))
                    obj3.put(PayMethodObj.get(i).getJSONObject2());
            }
            JSONObject obj = new JSONObject();
            obj.put("ORDERHEADER", obj1);
            obj.put("ORDERTRANSACTIONS", obj2);
            obj.put("PAYMETHOD", obj3);

            Log.e("log trans =", obj2.toString());

            Log.e("save successful =", obj.toString());

            SendCloud sendCloud = new SendCloud(MyService.this, obj);
            sendCloud.startSending("Order");

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
    }

    public void message() {
        Toast.makeText(MyService.this, "is send succ", Toast.LENGTH_SHORT).show();

    }


}