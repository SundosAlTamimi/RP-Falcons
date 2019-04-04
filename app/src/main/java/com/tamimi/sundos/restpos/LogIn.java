package com.tamimi.sundos.restpos;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.BackOffice.EmployeeRegistration;
import com.tamimi.sundos.restpos.Models.BlindClose;
import com.tamimi.sundos.restpos.Models.BlindShift;
import com.tamimi.sundos.restpos.Models.EmployeeRegistrationModle;
import com.tamimi.sundos.restpos.Models.Shift;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;


public class LogIn extends AppCompatActivity {

    ImageView lock;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0;
    Button clear, logIn;
    TextView t1, t2, t3, t4;
    TextView[] arrayOfText;
    int index = 0;
    MediaPlayer mp;

    String date, time, shiftName = "A";
    int shiftNo = 0;
    boolean isActive;
    int userPassword;

    Dialog dialog;
    DatabaseHandler mDHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.log_in);

        mDHandler = new DatabaseHandler(this);
        mp = MediaPlayer.create(this, R.raw.unlock_sound);
        initialize();
        setShift();
        arrayOfText = new TextView[]{t1, t2, t3, t4};

        showUserNameDialog();

    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (index < 4) {
                Button button = (Button) view;
                arrayOfText[index].setText(button.getText().toString());
                index++;
            }
        }

    };

    OnClickListener onClickListener2 = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.b_clear:
                    t1.setText("");
                    t2.setText("");
                    t3.setText("");
                    t4.setText("");
                    index = 0;
                    break;

                case R.id.b_login:
                    if (index == 4) {
                        String password = t1.getText().toString() + t2.getText().toString() + t3.getText().toString() + t4.getText().toString();

                        if (isCorrect(Integer.parseInt(password))) {

                            Date currentTimeAndDate = Calendar.getInstance().getTime();
                            SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                            time = tf.format(currentTimeAndDate);
                            if (!isActive) {
                                mDHandler.addBlindShiftInOut(new BlindShift(date, time, 1, shiftNo, shiftName,
                                        Integer.parseInt(password), Settings.user_name, 1));

                                Settings.shift_name = shiftName;
                                Settings.shift_number = shiftNo;
                            } else {
                                Settings.shift_name = mDHandler.getOpenedShifts(date, 1).getShiftName();
                                Settings.shift_number = mDHandler.getOpenedShifts(date, 1).getShiftNo();
                            }
                            Settings.password = Integer.parseInt(password);
                            Settings.POS_number = 1;
                            Settings.store_number = 7;

                            logIn();
                        } else
                            Toast.makeText(LogIn.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

    };

    void showUserNameDialog() {

        dialog = new Dialog(LogIn.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.user_name_dialog);
        dialog.setCanceledOnTouchOutside(false);

        final EditText userNameEditText = (EditText) dialog.findViewById(R.id.user_name);
        Button buttonDone = (Button) dialog.findViewById(R.id.b_done);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userText = userNameEditText.getText().toString();
                if (!userText.equals("")) {

                    switch (openedShift(userText)) {
                        case "":
                            if (userText.equals("master")) {
                                isActive = false;
                                Settings.user_name = userText;
                                dialog.dismiss();
                            } else {
                                isActive = false;
                                ArrayList<EmployeeRegistrationModle> employees = mDHandler.getAllEmployeeRegistration();
                                boolean isExist = false;
                                for (int i = 0; i < employees.size(); i++) {
                                    if (userText.equals(employees.get(i).getEmployeeName())) {
                                        Settings.user_name = userText;
                                        userPassword = employees.get(i).getUserPassword();
                                        isExist = true;
                                        break;
                                    }
                                }
                                if (isExist) {
                                    dialog.dismiss();
                                } else
                                    Toast.makeText(LogIn.this, "user not found", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "another user is logged":
                            Toast.makeText(LogIn.this, "another user is logged", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Settings.user_name = userText;
                            isActive = true;
                            dialog.dismiss();
                            break;
                    }

                } else {
                    Toast.makeText(LogIn.this, "Please Enter your user name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public boolean isCorrect(int password) {
        if (Settings.user_name.equals("master"))
            return password == 5555;
        else {
            Log.e("***", "" + password + "-" + userPassword);
            return password == userPassword;
        }
    }

    public void logIn() {
        lock.setBackgroundResource(R.drawable.unlock);
        mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(LogIn.this, Main.class);
                startActivity(mainIntent);
                finish();
            }
        }, 500);
    }

    String openedShift(String userName) {
        BlindShift openShift = mDHandler.getOpenedShifts(date, 1);
        if (openShift.getUserName() != null) {
            if (openShift.getUserName().equals(userName)) {
                userPassword = openShift.getUserNo();
                return userName;
            } else
                return "another user is logged";
        } else
            return "";
    }

    @TargetApi(Build.VERSION_CODES.O)
    void setShift() {

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        date = df.format(currentTimeAndDate);

        ArrayList<Shift> shifts = mDHandler.getAllShifts();

//        if (shifts.size() == 0) {
//            shiftNo = 0;
//            shiftName = "A";
//        }

//        try {
        for (int i = 0; i < shifts.size(); i++) {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
            time = tf.format(currentTime);

//                Date time1 = new SimpleDateFormat("hh:mm").parse(shifts.get(i).getFromTime());
//                Calendar calendar1 = Calendar.getInstance();
//                calendar1.setTime(time1);
//
//                Date time2 = new SimpleDateFormat("hh:mm").parse(shifts.get(i).getToTime());
//                Calendar calendar2 = Calendar.getInstance();
//                calendar2.setTime(time2);
//                calendar2.add(Calendar.DATE, 1);
//
//                Date d = new SimpleDateFormat("hh:mm").parse(time);
//                Calendar calendar3 = Calendar.getInstance();
//                calendar3.setTime(d);
//                calendar3.add(Calendar.DATE, 1);
//
//                Date x = calendar3.getTime();
//                Log.e("time :" , calendar1.getTime().toString() + " " + x.toString() + " " + calendar2.getTime().toString());
//                if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

            int time1 = Integer.parseInt(shifts.get(i).getFromTime().substring(0, shifts.get(i).getFromTime().indexOf(":")));
            int time2 = Integer.parseInt(shifts.get(i).getToTime().substring(0, shifts.get(i).getToTime().indexOf(":")));
            int current = Integer.parseInt(time.substring(0, time.indexOf(":")));
            if (current >= time1 && current < time2) {

                shiftNo = shifts.get(i).getShiftNo();
                shiftName = shifts.get(i).getShiftName();

                // test if the previous user closed before his ending shift
                List<BlindClose> blindCloseList = mDHandler.getAllBlindClose();
                for (int k = 0; k < blindCloseList.size(); k++) {

                    int timeClose = Integer.parseInt(blindCloseList.get(k).getTime().substring(0, blindCloseList.get(k).getTime().indexOf(":")));

                    if (blindCloseList.get(k).getDate().equals(date) &&
                            timeClose >= time1 && current < time2    &&
                            blindCloseList.get(k).getTransType() == 0) {

                        if(i+1 > shifts.size()-1){
                            shiftNo = shifts.get(0).getShiftNo();
                            shiftName = shifts.get(0).getShiftName();
                        } else {
                            shiftNo = shifts.get(i+1).getShiftNo();
                            shiftName = shifts.get(i+1).getShiftName();
                        }
                    }

                }
            }
        }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    void initialize() {
        lock = (ImageView) findViewById(R.id.lock);
        t1 = (TextView) findViewById(R.id.num1);
        t2 = (TextView) findViewById(R.id.num2);
        t3 = (TextView) findViewById(R.id.num3);
        t4 = (TextView) findViewById(R.id.num4);

        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b5 = (Button) findViewById(R.id.b5);
        b6 = (Button) findViewById(R.id.b6);
        b7 = (Button) findViewById(R.id.b7);
        b8 = (Button) findViewById(R.id.b8);
        b9 = (Button) findViewById(R.id.b9);
        b0 = (Button) findViewById(R.id.b0);

        clear = (Button) findViewById(R.id.b_clear);
        logIn = (Button) findViewById(R.id.b_login);

        b1.setOnClickListener(onClickListener);
        b2.setOnClickListener(onClickListener);
        b3.setOnClickListener(onClickListener);
        b4.setOnClickListener(onClickListener);
        b5.setOnClickListener(onClickListener);
        b6.setOnClickListener(onClickListener);
        b7.setOnClickListener(onClickListener);
        b8.setOnClickListener(onClickListener);
        b9.setOnClickListener(onClickListener);
        b0.setOnClickListener(onClickListener);

        clear.setOnClickListener(onClickListener2);
        logIn.setOnClickListener(onClickListener2);
    }
}
