package com.tamimi.sundos.restpos;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Settings {
    public static String user_name = "no user";
    public static int password = -1;
    public static int user_no = -1;
    public static int POS_number = 1;
    public static int store_number = 0;
    public static int shift_number =0;
    public static String shift_name = "no shift";
    public static double service_tax = 0.0;
    public static double service_value = 0.0;
    public static int tax_type = 1;
    public static int time_card = 0;

    public Settings(){
    }

    public void makeText(Context context , String msg){

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(17);
        messageTextView.setTextColor(context.getResources().getColor(R.color.text_color));
        group.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.toast));
        toast.show();
    }
}
