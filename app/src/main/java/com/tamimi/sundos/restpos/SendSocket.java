package com.tamimi.sundos.restpos;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendSocket {

    Context context;
    JSONObject obj;

    public SendSocket(Context context, JSONObject obj) {
        this.obj = obj;
        this.context = context;

    }

    public void sendMessage() {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {//"10.10.100.10" // 10.10.100.17 //F_A_wifi_ip
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    Socket s = new Socket("10.10.100.22", 9002);
//                    if(Integer.parseInt(IP.getText().toString())==1)
//                     s = new Socket("10.10.100.10", 9002);
//                    else if(Integer.parseInt(IP.getText().toString())==2)
//                        s = new Socket("10.10.100.22", 9002);

                    OutputStream out = s.getOutputStream();
                    PrintWriter output = new PrintWriter(out);

                    output.println(obj);
                    output.flush();

                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String st = input.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            String s = mTextViewReplyFromServer.getText().toString();
                            if (st.trim().length() != 0)
//                                mTextViewReplyFromServer.setText(s + "\nFrom Server : " + st);
                                Toast.makeText(context, "From Server : successful Socket " + st, Toast.LENGTH_SHORT).show();
                        }
                    });

                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


}
