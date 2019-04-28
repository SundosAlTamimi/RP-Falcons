package com.tamimi.sundos.restpos;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.tamimi.sundos.restpos.Models.KitchenScreen;
import com.tamimi.sundos.restpos.Models.OrderTransactions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendSocket {

    Context context;
    JSONObject obj;
    List<OrderTransactions> orderTransactions;
    DatabaseHandler db;

    public SendSocket(Context context, JSONObject obj, List<OrderTransactions> orderTransactions) {
        this.obj = obj;
        this.context = context;
        this.orderTransactions = orderTransactions;
        db = new DatabaseHandler(context);
    }

    public void sendMessage() {
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket s = null;
                    OutputStream out = null;
                    PrintWriter output = null;
                    List<KitchenScreen> kitchenScreens = new ArrayList<>();
                    JSONArray obj3 = new JSONArray();

                    //this for get all kitchen ...
                    kitchenScreens = db.getAllKitchenScreen();

                    //this for_loop for filter and send all data to target kitchen has same kitchen no and have ip
                    for (int i = 0; i < kitchenScreens.size(); i++) {
                        if (!kitchenScreens.get(i).getKitchenIP().equals("")) {
                            obj3 = getObjectForKitchenNo(kitchenScreens.get(i).getKitchenNo());
                            if (obj3.toString().length() > 2) {
                                s = new Socket(kitchenScreens.get(i).getKitchenIP(), 9002);
                                out = s.getOutputStream();
                                output = new PrintWriter(out);
                                output.println(obj3.toString());
                                output.flush();
                                Log.e("obj3 " + i + " sec " + kitchenScreens.get(i).getKitchenNo(), "obj3.toString().length()" + obj3.toString());
                                output.close();
                                out.close();
                                s.close();
                            }
                        }
                    }

                    //tis for read data send from server ...

//                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
//                    final String st = input.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            String s = mTextViewReplyFromServer.getText().toString();
//                            if (st.trim().length() != 0)
//                                mTextViewReplyFromServer.setText(s + "\nFrom Server : " + st);
//                                Toast.makeText(context, "From Server : successful Socket " + st, Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    // this for get all object  send to target kitchen by kitchen no ...

    JSONArray getObjectForKitchenNo(int KitchenNo) {

        JSONArray objNo = new JSONArray();

        for (int i = 0; i < orderTransactions.size(); i++) {
            if (orderTransactions.get(i).getScreenNo() == KitchenNo) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("ITEMCODE", orderTransactions.get(i).getItemBarcode());
                    obj.put("ITEMNAME", orderTransactions.get(i).getItemName());
                    obj.put("QTY", orderTransactions.get(i).getQty());
                    obj.put("PRICE", orderTransactions.get(i).getPrice());
                    obj.put("NOTE", orderTransactions.get(i).getNote());
                    obj.put("ISUPDATE", 0);
                    obj.put("SCREENNO", orderTransactions.get(i).getScreenNo());
                    obj.put("POSNO", orderTransactions.get(i).getPosNo());
                    obj.put("ORDERNO", orderTransactions.get(i).getVoucherNo());
                    obj.put("ORDERTYPE", orderTransactions.get(i).getOrderType());
                    obj.put("TABLENO", orderTransactions.get(i).getTableNo());
                    obj.put("SECTIONNO", orderTransactions.get(i).getSectionNo());
                    objNo.put(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("getobj " + KitchenNo, "" + objNo.toString());

        return objNo;
    }


}
