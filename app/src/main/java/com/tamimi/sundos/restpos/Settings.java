package com.tamimi.sundos.restpos;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

public class Settings {
    public static final String URL = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/";
    public static String user_name = "no user";
    public static int password = -1;
    public static int user_no = -1;
    public static int POS_number = 1;
    public static int store_number = 0;
    public static int shift_number = 0;
    public static String shift_name = "no shift";
    public static double service_tax = 0.0;
    public static double service_value = 0.0;
    public static int tax_type = 1;
    public static int time_card = 0;
    public static int cash_no = 1;
    public static TextView focas=null;
    public static boolean onOFF=true;
    public static String datatest="";


    public Settings() {
    }

    public void makeText(Context context, String msg) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(17);
        messageTextView.setTextColor(context.getResources().getColor(R.color.text_color));
        group.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.toast));
        toast.show();
    }


    public void text(String msg) {

        focas.setText(msg);

    }

    @SuppressLint("WrongConstant")
    public void blinkAnnouncement( boolean onOff) {
//        Log.e("msg", ""+msg);
        try {

//        focas.setText(msg);
        ObjectAnimator objectAnimator;
        if (onOff) {
            objectAnimator = ObjectAnimator.ofInt(focas, "backgroundColor", Color.WHITE, Color.GREEN, Color.WHITE);
        } else {
            objectAnimator = ObjectAnimator.ofInt(focas, "backgroundColor", Color.WHITE, Color.RED, Color.WHITE);

        }
        objectAnimator.setDuration(2000);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.setRepeatMode(Animation.REVERSE);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.start();
        }catch (NullPointerException U){
            Log.e("null ....",""+U.getMessage());
        }


    }


}
