package com.tamimi.sundos.restpos;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
import java.util.List;

public class SendSocket {

    Context context;
    JSONObject obj;
    List<OrderTransactions> orderTransactions;

    public SendSocket(Context context, JSONObject obj, List<OrderTransactions> orderTransactions) {
        this.obj = obj;
        this.context = context;
        this.orderTransactions = orderTransactions;

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

                    JSONArray obj2 = new JSONArray();
                    JSONArray obj3 = new JSONArray();

                    for (int i = 0; i < orderTransactions.size(); i++) {
                        if (orderTransactions.get(i).getScreenNo() == 1) {
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
                            obj2.put(obj);
                        } else if (orderTransactions.get(i).getScreenNo() == 2) {
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
                            obj3.put(obj);
                        }
                    }

                    JSONObject objI1 = new JSONObject();
                    objI1.put("Items", obj2);
                    objI1.put("Header", obj);
                    JSONObject objI2 = new JSONObject();
                    objI2.put("Items", obj3);
                    objI2.put("Header", obj);

                    //"10.10.100.10" // 10.10.100.17 //F_A_wifi_ip
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.

                    if (obj3.toString().length() >2) {//192.168.2.6
                        s = new Socket("192.168.2.6", 9002);
                        out = s.getOutputStream();
                        output = new PrintWriter(out);
                        output.println(obj3.toString());
                        output.flush();
                        Log.e("obj3 sec2 ", "obj3.toString().length()" + obj3.toString());
                        s.close();
                    }

                    if (obj2.toString().length() >2) {
                        s = new Socket("192.168.2.5", 9002);
                        out = s.getOutputStream();
                        output = new PrintWriter(out);
                        output.println(obj2.toString());
                        output.flush();
                        Log.e("obj2 sec1 ", "obj2.toString().length()" + obj2.toString());
                        s.close();
                    }

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

                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


}
