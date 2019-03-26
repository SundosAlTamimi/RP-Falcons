package com.tamimi.sundos.restpos;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.BackOffice.BackOfficeActivity;
import com.tamimi.sundos.restpos.Models.Announcemet;
import com.tamimi.sundos.restpos.Models.BlindClose;
import com.tamimi.sundos.restpos.Models.BlindCloseDetails;
import com.tamimi.sundos.restpos.Models.Cashier;
import com.tamimi.sundos.restpos.Models.ClockInClockOut;
import com.tamimi.sundos.restpos.Models.Money;
import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.Pay;
import com.tamimi.sundos.restpos.Models.PayMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Main extends AppCompatActivity {

    Button back, exit;
    Button takeAway, dineIn;
    TextView userName, shift, date, cashierIn, cashierOut, payIn, payOut, timeCard, safeMode, cashDrawer;

    DatabaseHandler mDHandler;
    Dialog dialog;
    String today;
    TextView focusedTextView;
    TableLayout categories;
    TableLayout AnnouncementTable;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        mDHandler = new DatabaseHandler(Main.this);
        focusedTextView = null;
        initialize();


        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today = df.format(currentTimeAndDate);

        date.setText(today);
        userName.setText(Settings.user_name);
        shift.setText("Shift: " + mDHandler.getOpenedShifts(today, 1).getShiftName());

        showAnnouncement();


    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back:
//                    showDialog();
                    Intent intent0 = new Intent(Main.this, BackOfficeActivity.class);
                    startActivity(intent0);
                    break;

                case R.id.exit:
                    finish();
                    System.exit(0);
                    break;

                case R.id.tack_away:
                    Intent intent1 = new Intent(Main.this, Order.class);
                    startActivity(intent1);
                    break;

                case R.id.dine_in:
//                    if (Settings.table_edit_authorized) {
//                        Intent intent = new Intent(Main.this, DineInLayout.class);
////                        intent.putExtra("flag", "0");
//                        startActivity(intent);
//                    } else {
                    Intent intent = new Intent(Main.this, DineIn.class);
                    startActivity(intent);
//                    }
                    break;

                case R.id.cashier_in:
                    showCashierInDialog();
                    break;

                case R.id.cashier_out:
                    showCashierOutDialog();
                    break;

                case R.id.pay_in:
                    showPayInDialog(0);
                    break;

                case R.id.pay_out:
                    showPayInDialog(1);
                    break;

                case R.id.time_card:
                    showClockInClockOutDialog();
                    break;

                case R.id.safe_mode:
                    showSafeModeDialog();
                    break;

                case R.id.cash_drawer:
                    showCashDrawerDialog();
                    break;
            }
        }
    };

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.back:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight));
                    }
                    break;

                case R.id.exit:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        exit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.exit));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        exit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.exit_hover));
                    }
                    break;

                case R.id.tack_away:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        takeAway.setBackgroundResource(R.drawable.take_away);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        takeAway.setBackgroundResource(R.drawable.take_away_hover);
                    }
                    break;

                case R.id.dine_in:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        dineIn.setBackgroundResource(R.drawable.dine_in);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        dineIn.setBackgroundResource(R.drawable.dine_in_hover);
                    }
                    break;
            }
            return false;
        }
    };


    void showAnnouncement(){
        ArrayList<Announcemet> announcemets=new ArrayList<>();
        announcemets=mDHandler.getAllTableAnnouncement();
        int count=0;
        for(int i=0;i<announcemets.size();i++){
            if(announcemets.get(i).getIsShow()== 0){
                if(announcemets.get(i).getUserName().equals(Settings.user_name)||announcemets.get(i).getUserName().equals("All")) {
                    if (announcemets.get(i).getPosNo() == (Settings.POS_number) || announcemets.get(i).getPosNo() == (-1)) {
                        if(announcemets.get(i).getShiftName().equals(Settings.shift_name)||announcemets.get(i).getShiftName().equals("All")) {
                        count++;
                        final TableRow row = new TableRow(Main.this);

                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, 5, 0, 5);
                        row.setLayoutParams(lp);


                        TextView textView = new TextView(Main.this);
                        textView.setText("" + count + ") " + announcemets.get(i).getMessage());


                        textView.setTextColor(ContextCompat.getColor(Main.this, R.color.exit_hover));
                        textView.setGravity(Gravity.START);

                        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                        textView.setLayoutParams(lp2);
                        textView.setTextSize(16);

                        row.addView(textView);

                        AnnouncementTable.addView(row);
                        mDHandler.updateAnnounementIsShow(announcemets.get(i).getMessage(), announcemets.get(i).getAnnouncementDate());
                            blinkAnnouncement(textView);
                    }
                }


                    }
                }
            }
        }

        @SuppressLint("WrongConstant")
        void blinkAnnouncement(TextView text){

            ObjectAnimator objectAnimator= ObjectAnimator.ofInt(text,"textColor",Color.WHITE,Color.RED,Color.WHITE);
            objectAnimator.setDuration(2000);
            objectAnimator.setEvaluator(new ArgbEvaluator());
            objectAnimator.setRepeatMode(Animation.REVERSE);
            objectAnimator.setRepeatCount(Animation.INFINITE);
            objectAnimator.start();

        }


    @SuppressLint("ClickableViewAccessibility")
    void showCashierInDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cashier_in_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(920, 470);

        final ArrayList<Money> money = mDHandler.getAllMoneyCategory();

        categories = (TableLayout) dialog.findViewById(R.id.money_categories);
        final TextView mainTotal = (TextView) dialog.findViewById(R.id.mainTotal);
        final TextView user = (TextView) dialog.findViewById(R.id.user);
        final TextView date = (TextView) dialog.findViewById(R.id.date);
        user.setText(Settings.user_name);

        date.setText(today);

        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, clear, save;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        clear = (Button) dialog.findViewById(R.id.b_clear);
        save = (Button) dialog.findViewById(R.id.save);

        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < money.size(); i++) {
                    TableRow tableRow = (TableRow) categories.getChildAt(i);
                    TextView text1 = (TextView) tableRow.getChildAt(1);
                    TextView text2 = (TextView) tableRow.getChildAt(2);
                    text1.setText("0");
                    text2.setText("0");
                    mainTotal.setText("0.00");
                }
            }
        });
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Cashier> cashier = new ArrayList<>();
                for (int i = 0; i < money.size(); i++) {
                    Cashier cash = new Cashier();
                    TableRow tableRow = (TableRow) categories.getChildAt(i);
                    TextView text = (TextView) tableRow.getChildAt(0);
                    TextView text1 = (TextView) tableRow.getChildAt(1);

                    cash.setCashierName(user.getText().toString());
                    cash.setCheckInDate(date.getText().toString());
                    cash.setCategoryName(text.getText().toString());
                    cash.setCategoryValue(Double.parseDouble(text.getTag().toString()));
                    cash.setCategoryQty(Integer.parseInt(text1.getText().toString()));
                    cash.setOrderKind(0);
                    cashier.add(cash);
                }
                mDHandler.addCashierInOut(cashier);
                dialog.dismiss();
            }
        });


        for (int i = 0; i < money.size(); i++) {
            final int position = i;
            TableRow row = new TableRow(Main.this);
            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 5);
            row.setLayoutParams(lp);

            TextView textView = new TextView(Main.this);
            textView.setText(money.get(i).getCatName() + "   ");
            textView.setTag(money.get(i).getCatValue());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.text_color));

            final TextView textView1 = new TextView(Main.this);
            textView1.setBackgroundColor(getResources().getColor(R.color.layer1));
            textView1.setHeight(26);
            textView1.setPadding(10, 0, 0, 0);
            textView1.setTextColor(getResources().getColor(R.color.text_color));
            textView1.setText("0");
            textView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (focusedTextView != null) {
//                        if (focusedTextView.getText().toString().equals("")) {
//                            focusedTextView.setText("1");
//                        }
//                        TableRow tableRow = (TableRow) categories.getChildAt(Integer.parseInt(focusedTextView.getTag().toString()));
//                        TextView text = (TextView) tableRow.getChildAt(0);
//                        TextView text2 = (TextView) tableRow.getChildAt(2);
//                        int total = Integer.parseInt(text.getTag().toString()) * Integer.parseInt(focusedTextView.getText().toString());
//                        text2.setText("" + total);
//
//                        mainTotal.setText("0.000");
//                        for (int i = 0; i < money.size(); i++) {
//                            TableRow tRow = (TableRow) categories.getChildAt(i);
//                            TextView t = (TextView) tRow.getChildAt(2);
//                            mainTotal.setText("" +(Double.parseDouble(mainTotal.getText().toString())+ Double.parseDouble(t.getText().toString())));
//                        }
//
//                    }
                    if (focusedTextView != null && focusedTextView.getText().toString().equals("")) {
                        focusedTextView.setText("0");
                    }

                    focusedTextView = textView1;
                    focusedTextView.setTag("" + position);
                    focusedTextView.setText("");
                }
            });

            textView1.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (focusedTextView != null) {
                        if (!focusedTextView.getText().toString().equals("")) {

                            TableRow tableRow = (TableRow) categories.getChildAt(Integer.parseInt(focusedTextView.getTag().toString()));
                            TextView text = (TextView) tableRow.getChildAt(0);
                            TextView text2 = (TextView) tableRow.getChildAt(2);

                            double total = Double.parseDouble(text.getTag().toString()) * Double.parseDouble(focusedTextView.getText().toString());
                            text2.setText("" + total);
                        }

                        mainTotal.setText("0.000");
                        for (int i = 0; i < money.size(); i++) {
                            TableRow tRow = (TableRow) categories.getChildAt(i);
                            TextView t = (TextView) tRow.getChildAt(2);
                            mainTotal.setText("" + (Double.parseDouble(mainTotal.getText().toString()) + Double.parseDouble(t.getText().toString())));
                        }

                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            TextView textView2 = new TextView(Main.this);
            textView2.setText("0");

            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(130, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            lp2.setMargins(15, 0, 15, 0);
            textView.setLayoutParams(lp2);
            textView1.setLayoutParams(lp2);
            textView2.setLayoutParams(lp2);
            textView2.setGravity(Gravity.CENTER);
            textView2.setTextColor(getResources().getColor(R.color.text_color));

            row.addView(textView);
            row.addView(textView1);
            row.addView(textView2);

            categories.addView(row);
        }
        double totals = 0;
//        for (int i = 0; i < money.size(); i++) {
//            totals += money.get(i).getCatValue();
        mainTotal.setText("0.00");
//        }


        dialog.show();

    }

    @SuppressLint("ClickableViewAccessibility")
    void showCashierOutDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cashier_out_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(920, 490);

        final ArrayList<Money> money = mDHandler.getAllMoneyCategory();

        categories = (TableLayout) dialog.findViewById(R.id.money_categories);
        RadioGroup transType = dialog.findViewById(R.id.transType);
        RadioButton finalClose = dialog.findViewById(R.id.finalClose);
        RadioButton changeOver = dialog.findViewById(R.id.changeOver);
        EditText toUser = dialog.findViewById(R.id.toUser);
        final TextView cashTotals = (TextView) dialog.findViewById(R.id.cashTotal);
        final TextView creditCard = (TextView) dialog.findViewById(R.id.creditCard);
        final TextView cheque = (TextView) dialog.findViewById(R.id.cheque);
        final TextView giftCard = (TextView) dialog.findViewById(R.id.giftCard);
        final TextView credit = (TextView) dialog.findViewById(R.id.credit);
        final TextView point = (TextView) dialog.findViewById(R.id.point);
        final TextView otherPaymentTotal = (TextView) dialog.findViewById(R.id.otherPaymentTotal);
        final TextView mainTotal = (TextView) dialog.findViewById(R.id.mainTotal);
        final TextView user = (TextView) dialog.findViewById(R.id.user);
        final TextView date = (TextView) dialog.findViewById(R.id.date);
        user.setText(Settings.user_name);

        date.setText(today);

        final int[] tranType = {0};
        finalClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                tranType[0] = 0;
                toUser.setText("");
                toUser.setEnabled(true);
            }
        });
        changeOver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                tranType[0] = 1;
                toUser.setEnabled(false);
            }
        });

        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, clear, save;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        clear = (Button) dialog.findViewById(R.id.b_clear);
        save = (Button) dialog.findViewById(R.id.save);

        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < money.size(); i++) {
                    TableRow tableRow = (TableRow) categories.getChildAt(i);
                    TextView text1 = (TextView) tableRow.getChildAt(1);
                    TextView text2 = (TextView) tableRow.getChildAt(2);
                    text1.setText("0");
                    text2.setText("0");
                }
//                cashTotals.setText("0.000");
                creditCard.setText("0.00");
                cheque.setText("0.00");
                giftCard.setText("0.00");
                credit.setText("0.00");
                point.setText("0.00");
                otherPaymentTotal.setText("0.00");
                mainTotal.setText("0.00");

                toUser.setText("");
                toUser.setEnabled(false);
                finalClose.setChecked(true);
                changeOver.setChecked(false);
                tranType[0] = 0;

            }
        });
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (finalClose.isChecked() || (changeOver.isChecked() && !toUser.getText().toString().equals(""))) {
                    ArrayList<OrderHeader> orderHeaders = mDHandler.getAllOrderHeader();
                    ArrayList<PayMethod> payMethods = mDHandler.getAllExistingPay();

                    int transNo = mDHandler.getAllBlindClose().size();

                    Date currentTimeAndDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                    String time = df.format(currentTimeAndDate);

                    double userSales = Double.parseDouble(mainTotal.getText().toString());
                    double sysSales = 0;
//                Log.e("tag***", "" + orderHeaders.get(0).getVoucherDate() + " == " + today + " && " + orderHeaders.get(0).getShiftName() + Settings.shift_name);

                    for (int i = 0; i < orderHeaders.size(); i++)
                        if (orderHeaders.get(i).getVoucherDate().equals(today) && orderHeaders.get(i).getShiftName().equals(Settings.shift_name))
                            sysSales += orderHeaders.get(i).getAmountDue();

                    double userCash = Double.parseDouble(cashTotals.getText().toString());
                    double sysCash = 0;
                    for (int i = 0; i < payMethods.size(); i++)
                        if (payMethods.get(i).getVoucherDate().equals(today) && payMethods.get(i).getShiftName().equals(Settings.shift_name)
                                && payMethods.get(i).getPayType().equals("Cash"))
                            sysCash += payMethods.get(i).getPayValue();

                    double userOthers = Double.parseDouble(otherPaymentTotal.getText().toString());
                    double sysOthers = 0;
                    for (int i = 0; i < payMethods.size(); i++)
                        if (payMethods.get(i).getVoucherDate().equals(today) && payMethods.get(i).getShiftName().equals(Settings.shift_name)
                                && !payMethods.get(i).getPayType().equals("Cash"))
                            sysOthers += payMethods.get(i).getPayValue();

                    mDHandler.addBlindClose(new BlindClose(transNo, today, time, Settings.POS_number, Settings.shift_number,
                            Settings.shift_name, Settings.password, Settings.user_name, sysSales, userSales, userSales - sysSales,
                            sysCash, userCash, userCash - sysCash, sysOthers, userOthers, userOthers - sysOthers, 0, tranType[0],
                            "", toUser.getText().toString()));


                    for (int i = 0; i < money.size(); i++) {
                        TableRow tableRow = (TableRow) categories.getChildAt(i);
                        TextView text = (TextView) tableRow.getChildAt(0);
                        TextView text1 = (TextView) tableRow.getChildAt(1);

                        String catName = text.getText().toString();
                        Double catValue = Double.parseDouble(text.getTag().toString());
                        int catQty;
                        if (text1.getText().toString().equals(""))
                            catQty = 0;
                        else
                            catQty = Integer.parseInt(text1.getText().toString());

                        mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                                Settings.shift_name, Settings.password, Settings.user_name, catName, catQty, catValue, catQty * catValue,
                                "Cash", "", "", -1, "no-user"));
                    }

                    double creditCardValue = Double.parseDouble(creditCard.getText().toString());
                    double chequeValue = Double.parseDouble(cheque.getText().toString());
                    double giftCardValue = Double.parseDouble(giftCard.getText().toString());
                    double creditValue = Double.parseDouble(credit.getText().toString());
                    double pointValue = Double.parseDouble(point.getText().toString());

                    if (creditCardValue != 0)
                        mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                                Settings.shift_name, Settings.password, Settings.user_name, "Credit Card", 1, creditCardValue,
                                creditCardValue, "Credit Card", "", "", -1, "no-user"));

                    if (chequeValue != 0)
                        mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                                Settings.shift_name, Settings.password, Settings.user_name, "Cheque", 1, chequeValue,
                                chequeValue, "Cheque", "", "", -1, "no-user"));

                    if (giftCardValue != 0)
                        mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                                Settings.shift_name, Settings.password, Settings.user_name, "Gift Card", 1, giftCardValue,
                                giftCardValue, "Gift Card", "", "", -1, "no-user"));

                    if (creditValue != 0)
                        mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                                Settings.shift_name, Settings.password, Settings.user_name, "Credit", 1, creditValue,
                                creditValue, "Credit", "", "", -1, "no-user"));

                    if (pointValue != 0)
                        mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                                Settings.shift_name, Settings.password, Settings.user_name, "Point", 1, pointValue,
                                pointValue, "Point", "", "", -1, "no-user"));

                    dialog.dismiss();
                } else
                    Toast.makeText(Main.this, "Please enter 'to user' field", Toast.LENGTH_LONG).show();
            }
        });


        for (int i = 0; i < money.size(); i++) {
            final int position = i;
            TableRow row = new TableRow(Main.this);
            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 5, 0, 5);
            row.setLayoutParams(lp);

            TextView textView = new TextView(Main.this);
            textView.setText(money.get(i).getCatName() + "   ");
            textView.setTag(money.get(i).getCatValue());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.text_color));

            final TextView textView1 = new TextView(Main.this);
            textView1.setBackgroundColor(getResources().getColor(R.color.layer1));
            textView1.setHeight(26);
            textView1.setPadding(10, 0, 0, 0);
            textView1.setTextColor(getResources().getColor(R.color.text_color));
            textView1.setText("0");
            textView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (focusedTextView != null && focusedTextView.getText().toString().equals("")) {
                        focusedTextView.setText("0");
                    }

                    focusedTextView = textView1;
                    focusedTextView.setTag("" + position);
                    focusedTextView.setText("");
                }
            });

            textView1.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (focusedTextView != null) {
                        if (!focusedTextView.getText().toString().equals("")) {

                            TableRow tableRow = (TableRow) categories.getChildAt(Integer.parseInt(focusedTextView.getTag().toString()));
                            TextView text = (TextView) tableRow.getChildAt(0);
                            TextView text2 = (TextView) tableRow.getChildAt(2);

                            double total = Double.parseDouble(text.getTag().toString()) * Double.parseDouble(focusedTextView.getText().toString());
                            text2.setText("" + total);
                        }

                        cashTotals.setText("0.000");
                        for (int i = 0; i < money.size(); i++) {
                            TableRow tRow = (TableRow) categories.getChildAt(i);
                            TextView t = (TextView) tRow.getChildAt(2);
                            cashTotals.setText("" + (Double.parseDouble(cashTotals.getText().toString()) + Double.parseDouble(t.getText().toString())));
                        }
                        mainTotal.setText("" + (Double.parseDouble(cashTotals.getText().toString()) +
                                Double.parseDouble(otherPaymentTotal.getText().toString())));

                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            TextView textView2 = new TextView(Main.this);
            textView2.setText("0");

            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(130, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            lp2.setMargins(15, 0, 15, 0);
            textView.setLayoutParams(lp2);
            textView1.setLayoutParams(lp2);
            textView2.setLayoutParams(lp2);
            textView2.setGravity(Gravity.CENTER);
            textView2.setTextColor(getResources().getColor(R.color.text_color));

            row.addView(textView);
            row.addView(textView1);
            row.addView(textView2);

            categories.addView(row);
        }
        double totals = 0;
//        for (int i = 0; i < money.size(); i++) {
//            totals += money.get(i).getCatValue();
        cashTotals.setText("0.00");
//        }

        //------------------------------------

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null && focusedTextView.getText().toString().equals("")) {
                    focusedTextView.setText("0");
                }
                focusedTextView = (TextView) view;
                focusedTextView.setText("");
            }
        };

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (focusedTextView != null) {
                    if (!focusedTextView.getText().toString().equals("")) {

                        double sum =
                                Double.parseDouble(creditCard.getText().toString()) +
                                        Double.parseDouble(cheque.getText().toString()) +
                                        Double.parseDouble(giftCard.getText().toString()) +
                                        Double.parseDouble(credit.getText().toString()) +
                                        Double.parseDouble(point.getText().toString());

                        otherPaymentTotal.setText("" + sum);
                        mainTotal.setText("" + (Double.parseDouble(cashTotals.getText().toString()) +
                                Double.parseDouble(otherPaymentTotal.getText().toString())));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        creditCard.setOnClickListener(onClickListener);
        cheque.setOnClickListener(onClickListener);
        giftCard.setOnClickListener(onClickListener);
        credit.setOnClickListener(onClickListener);
        point.setOnClickListener(onClickListener);

        creditCard.addTextChangedListener(textWatcher);
        cheque.addTextChangedListener(textWatcher);
        giftCard.addTextChangedListener(textWatcher);
        credit.addTextChangedListener(textWatcher);
        point.addTextChangedListener(textWatcher);

        dialog.show();

    }

    void showPayInDialog(final int transType) {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pay_in_out_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(920, 470);

        final TextView tranType = (TextView) dialog.findViewById(R.id.trans_type);
        final TextView date = (TextView) dialog.findViewById(R.id.date);
        final TextView serial = (TextView) dialog.findViewById(R.id.trans);
        final TextView value = (TextView) dialog.findViewById(R.id.value);
        final TextView remark = (TextView) dialog.findViewById(R.id.remark);
        final Button save = (Button) dialog.findViewById(R.id.save);
        final Button exit = (Button) dialog.findViewById(R.id.exit);

        tranType.setText(transType == 0 ? "Pay In" : "Pay Out");
        date.setText("Date:  " + today);

        ArrayList<Pay> pays = mDHandler.getAllPayInOut();
        serial.setText(pays.size() == 0 ? "Transaction Number:  0" : "Transaction Number:  " + pays.size());

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!value.getText().toString().equals("")) {
                    mDHandler.addPayInOut(new Pay(transType, Settings.POS_number, Settings.password, Settings.user_name, today,
                            Double.parseDouble(value.getText().toString()), remark.getText().toString(), Settings.shift_number,
                            Settings.shift_name));
                    Toast.makeText(Main.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else
                    Toast.makeText(Main.this, "Please ensure your inputs", Toast.LENGTH_SHORT).show();
            }
        });
        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    void showPayOutDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.table_edit_outhorization_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(920, 470);


        dialog.show();

    }

    void showSafeModeDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.table_edit_outhorization_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(920, 470);


        dialog.show();

    }

    void showCashDrawerDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.table_edit_outhorization_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(920, 470);


        dialog.show();

    }


    void showClockInClockOutDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.clockin_clockout_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        // window.setLayout(410, 510);

        final TextView value = (TextView) dialog.findViewById(R.id.text);

        focusedTextView = value;

        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, clear, ok;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        clear = (Button) dialog.findViewById(R.id.b_clear);
        ok = (Button) dialog.findViewById(R.id.okay);


        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText("");
            }
        });

        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!focusedTextView.getText().toString().equals("")) {
                    if (Settings.password == Integer.parseInt(focusedTextView.getText().toString())) {
                        switch (Settings.time_card) {
                            case 0:
                                dialog.dismiss();
                                showTimeCardDialog();
                                break;
                            case 1:
                                dialog.dismiss();
                                clockTimeOut();
                                break;
                            case 2:
                                dialog.dismiss();
                                showBreakTimeOut();
                                break;
                        }
                    } else {
                        Toast.makeText(Main.this, " Please Insert Correct Password ", Toast.LENGTH_SHORT).show();
                        focusedTextView.setText("");
                    }
                } else {
                    Toast.makeText(Main.this, " Please Enter Your Password ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void showTimeCardDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.time_card_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        // window.setLayout(590, 390);


        Button clockIn;
        final TextView date, username;
        final EditText remark;
        clockIn = (Button) dialog.findViewById(R.id.clockin);

        final TextClock time = (TextClock) dialog.findViewById(R.id.horas);
        date = (TextView) dialog.findViewById(R.id.date1);
        username = (TextView) dialog.findViewById(R.id.username);

        remark = (EditText) dialog.findViewById(R.id.remark);

        SystemClock.elapsedRealtime();

        final Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String dates = df.format(currentTimeAndDate);
        date.setText(dates);
        username.setText(Settings.user_name);

        clockIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final String times = time.getText().toString();
                clockInSuccessful(times, dates);
                Settings.time_card = 1;

                ClockInClockOut clockInClockOut = new ClockInClockOut();

                clockInClockOut.setPointOfSaleNumber(Settings.POS_number);
                clockInClockOut.setDate(dates);
                clockInClockOut.setUserNO(Settings.password);
                clockInClockOut.setUserName(Settings.user_name);
                clockInClockOut.setTranstype("ClockIN");
                clockInClockOut.setDateCard(dates);
                clockInClockOut.setTimeCard(times);
                clockInClockOut.setRemark((remark.getText().toString()));
                clockInClockOut.setShiftNo(Settings.shift_number);
                clockInClockOut.setShiftName(Settings.shift_name);


                mDHandler.addClockInClockOut(clockInClockOut);
            }
        });

        dialog.show();
    }

    void clockInSuccessful(String times, String dates) {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.clockin_successful_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        // window.setLayout(590, 290);

        TextView masege, time, date;

        masege = (TextView) dialog.findViewById(R.id.clockinsuccessfull);
        time = (TextView) dialog.findViewById(R.id.time2);
        date = (TextView) dialog.findViewById(R.id.date2);
        masege.setText("Clock IN Successful   (" + Settings.user_name + ")");
        Button ok = (Button) dialog.findViewById(R.id.ok1);

        time.setText(times);
        date.setText(dates);

        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void clockTimeOut() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.time_card_out_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        // window.setLayout(590, 390);

        Button clockOut, breakIN;
        TextView userNameOut, date;
        final TextClock time;
        final EditText remarkOut;

        clockOut = (Button) dialog.findViewById(R.id.clock_out);
        breakIN = (Button) dialog.findViewById(R.id.break_in);
        remarkOut = (EditText) dialog.findViewById(R.id.remark3);
        userNameOut = (TextView) dialog.findViewById(R.id.username1);
        userNameOut.setText(Settings.user_name);

        time = (TextClock) dialog.findViewById(R.id.horas1);
        date = (TextView) dialog.findViewById(R.id.date3);

        final Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String dates = df.format(currentTimeAndDate);
        date.setText(dates);


        clockOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.time_card = 0;
                final String times = time.getText().toString();
                ClockInClockOut clockInClockOut = new ClockInClockOut();

                clockInClockOut.setPointOfSaleNumber(Settings.POS_number);
                clockInClockOut.setDate(dates);
                clockInClockOut.setUserNO(Settings.password);
                clockInClockOut.setUserName(Settings.user_name);
                clockInClockOut.setTranstype("ClockOut");
                clockInClockOut.setDateCard(dates);
                clockInClockOut.setTimeCard(times);
                clockInClockOut.setRemark((remarkOut.getText().toString()));
                clockInClockOut.setShiftNo(Settings.shift_number);
                clockInClockOut.setShiftName(Settings.shift_name);


                mDHandler.addClockInClockOut(clockInClockOut);
                dialog.dismiss();

            }
        });
        breakIN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.time_card = 2;
                final String times = time.getText().toString();
                ClockInClockOut clockInClockOut = new ClockInClockOut();

                clockInClockOut.setPointOfSaleNumber(Settings.POS_number);
                clockInClockOut.setDate(dates);
                clockInClockOut.setUserNO(Settings.password);
                clockInClockOut.setUserName(Settings.user_name);
                clockInClockOut.setTranstype("BreakIN");
                clockInClockOut.setDateCard(dates);
                clockInClockOut.setTimeCard(times);
                clockInClockOut.setRemark((remarkOut.getText().toString()));
                clockInClockOut.setShiftNo(Settings.shift_number);
                clockInClockOut.setShiftName(Settings.shift_name);

                mDHandler.addClockInClockOut(clockInClockOut);

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void showBreakTimeOut() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.time_break_out_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        //window.setLayout(590, 390);

        Button breakOut;
        TextView date, username;
        final TextClock time;
        final EditText remark;
        breakOut = (Button) dialog.findViewById(R.id.breaks_out);

        time = (TextClock) dialog.findViewById(R.id.horas2);
        date = (TextView) dialog.findViewById(R.id.date4);
        username = (TextView) dialog.findViewById(R.id.username4);

        remark = (EditText) dialog.findViewById(R.id.remark4);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String dates = df.format(currentTimeAndDate);
        date.setText(dates);

        username.setText(Settings.user_name);

        breakOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final String times = time.getText().toString();
                ClockInClockOut clockInClockOut = new ClockInClockOut();

                clockInClockOut.setPointOfSaleNumber(Settings.POS_number);
                clockInClockOut.setDate(dates);
                clockInClockOut.setUserNO(Settings.password);
                clockInClockOut.setUserName(Settings.user_name);
                clockInClockOut.setTranstype("BreakOut");
                clockInClockOut.setDateCard(dates);
                clockInClockOut.setTimeCard(times);
                clockInClockOut.setRemark((remark.getText().toString()));
                clockInClockOut.setShiftNo(Settings.shift_number);
                clockInClockOut.setShiftName(Settings.shift_name);

                mDHandler.addClockInClockOut(clockInClockOut);

                Settings.time_card = 1;
            }
        });


        dialog.show();
    }


    void showAouthorizingDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.table_edit_outhorization_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(610, 270);

        final EditText editText = (EditText) dialog.findViewById(R.id.password);
        Button buttonDone = (Button) dialog.findViewById(R.id.b_done);

        buttonDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().equals("")) {
                    if (Integer.parseInt(editText.getText().toString()) == 4444) {
                        Settings settings = new Settings();
                        settings.table_edit_authorized = true;
                        Toast.makeText(Main.this, "Your'r authorized to edit tables ", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(Main.this, "Your authorization number is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();

    }

    void initialize() {

        back = (Button) findViewById(R.id.back);
        exit = (Button) findViewById(R.id.exit);
        takeAway = (Button) findViewById(R.id.tack_away);
        dineIn = (Button) findViewById(R.id.dine_in);

        userName = (TextView) findViewById(R.id.user_name);
        shift = (TextView) findViewById(R.id.shift);
        date = (TextView) findViewById(R.id.date);
        cashierIn = (TextView) findViewById(R.id.cashier_in);
        cashierOut = (TextView) findViewById(R.id.cashier_out);
        payIn = (TextView) findViewById(R.id.pay_in);
        payOut = (TextView) findViewById(R.id.pay_out);
        timeCard = (TextView) findViewById(R.id.time_card);
        safeMode = (TextView) findViewById(R.id.safe_mode);
        cashDrawer = (TextView) findViewById(R.id.cash_drawer);
        AnnouncementTable=(TableLayout)findViewById(R.id.AnnouncmentTable);

        back.setOnClickListener(onClickListener);
        exit.setOnClickListener(onClickListener);
        takeAway.setOnClickListener(onClickListener);
        dineIn.setOnClickListener(onClickListener);
        cashierIn.setOnClickListener(onClickListener);
        cashierOut.setOnClickListener(onClickListener);
        payIn.setOnClickListener(onClickListener);
        payOut.setOnClickListener(onClickListener);
        timeCard.setOnClickListener(onClickListener);
        safeMode.setOnClickListener(onClickListener);
        cashDrawer.setOnClickListener(onClickListener);

        back.setOnTouchListener(onTouchListener);
        exit.setOnTouchListener(onTouchListener);
        takeAway.setOnTouchListener(onTouchListener);
        dineIn.setOnTouchListener(onTouchListener);
    }
}
