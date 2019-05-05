package com.tamimi.sundos.restpos;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Main extends AppCompatActivity {

    Button back, exit;
    Button takeAway, dineIn;
    TextView userName, shift, date, payIn, payOut, timeCard, safeMode, refund, cashDrawer, annText;

    DatabaseHandler mDHandler;
    Dialog dialog;
    String today;
    TextView focusedTextView;
    TableLayout categories;
    TableLayout AnnouncementTable;
    ArrayList<Double> lineDiscount;
    ArrayList<Double> DiscountArray;
    TableLayout refundTables, table;
    ArrayList<OrderTransactions> orderTransactions;
    ArrayList<OrderTransactions> rowRefund;
    TextView text;
    int idGeneral = 0;
    String data;
    boolean CheckTrue = true;
    double netTotals = 0.0;
    double balance = 0.0;
    int textId = 0;
    double totalAdd = 0.0;
    double cashValues, creditValues, chequeVales, pointValues, giftCardValues, cardValues;
    double discountAdd = 0.0;
    TableRow rows;

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
        today = convertToEnglish(df.format(currentTimeAndDate));

        date.setText(today);
        userName.setText(mDHandler.getOpenedShifts(today, 1).getUserName());
        shift.setText(getResources().getString(R.string.shift) + " : " + mDHandler.getOpenedShifts(today, 1).getShiftName());

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
                    moveTaskToBack(true);
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

                case R.id.refund:
                    openRefundDialog();
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


    void showAnnouncement() {
        ArrayList<Announcemet> announcemets = new ArrayList<>();
        announcemets = mDHandler.getAllTableAnnouncement();
        int count = 0;
        for (int i = 0; i < announcemets.size(); i++) {
            if (announcemets.get(i).getAnnouncementDate().equals(today)) {
                if (announcemets.get(i).getUserName().equals(Settings.user_name) || announcemets.get(i).getUserName().equals(getResources().getString(R.string.all))) {
                    if (announcemets.get(i).getPosNo() == (Settings.POS_number) || announcemets.get(i).getPosNo() == (-1)) {
                        if (announcemets.get(i).getShiftName().equals(Settings.shift_name) || announcemets.get(i).getShiftName().equals(getResources().getString(R.string.all))) {
                            count++;
                            final TableRow row = new TableRow(Main.this);

                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                            lp.setMargins(0, 5, 0, 5);
                            row.setLayoutParams(lp);


                            TextView textView = new TextView(Main.this);
                            textView.setText("" + count + ") " + announcemets.get(i).getMessage());


                            textView.setTextColor(ContextCompat.getColor(Main.this, R.color.text_color));
                            textView.setGravity(Gravity.START);

                            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                            textView.setLayoutParams(lp2);
                            textView.setTextSize(16);

                            row.addView(textView);

                            AnnouncementTable.addView(row);
                            mDHandler.updateAnnounementIsShow(announcemets.get(i).getMessage(), announcemets.get(i).getAnnouncementDate());
                            blinkAnnouncement(annText);

                        }
                    }


                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    void blinkAnnouncement(TextView text) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(text, "textColor", Color.WHITE, Color.RED, Color.WHITE);
        objectAnimator.setDuration(2000);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.setRepeatMode(Animation.REVERSE);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.start();

    }


    void showCashierInDialog(String times, String dates, ClockInClockOut clockInClockOut) {
        Dialog dialogCashierIn = new Dialog(Main.this);
        dialogCashierIn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCashierIn.setCancelable(false);
        dialogCashierIn.setContentView(R.layout.cashier_in_dialog);
        dialogCashierIn.setCanceledOnTouchOutside(false);

//        Window window = dialogCashierIn.getWindow();
//        window.setLayout(920, 470);

        final ArrayList<Money> money = mDHandler.getAllMoneyCategory();

        categories = (TableLayout) dialogCashierIn.findViewById(R.id.money_categories);
        final TextView mainTotal = (TextView) dialogCashierIn.findViewById(R.id.mainTotal);
        final TextView user = (TextView) dialogCashierIn.findViewById(R.id.user);
        final TextView date = (TextView) dialogCashierIn.findViewById(R.id.date);
        user.setText(Settings.user_name);

        date.setText(today);

        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, clear, save;
        b1 = (Button) dialogCashierIn.findViewById(R.id.b1);
        b2 = (Button) dialogCashierIn.findViewById(R.id.b2);
        b3 = (Button) dialogCashierIn.findViewById(R.id.b3);
        b4 = (Button) dialogCashierIn.findViewById(R.id.b4);
        b5 = (Button) dialogCashierIn.findViewById(R.id.b5);
        b6 = (Button) dialogCashierIn.findViewById(R.id.b6);
        b7 = (Button) dialogCashierIn.findViewById(R.id.b7);
        b8 = (Button) dialogCashierIn.findViewById(R.id.b8);
        b9 = (Button) dialogCashierIn.findViewById(R.id.b9);
        b0 = (Button) dialogCashierIn.findViewById(R.id.b0);
        clear = (Button) dialogCashierIn.findViewById(R.id.b_clear);
        save = (Button) dialogCashierIn.findViewById(R.id.save);

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
                if (money.size() > 0) {
                    if (!checkIsCashierInOutZero(money)) {

                        for (int i = 0; i < money.size(); i++) {
                            Cashier cash = new Cashier();
                            TableRow tableRow = (TableRow) categories.getChildAt(i);
                            TextView text = (TextView) tableRow.getChildAt(0);
                            TextView text1 = (TextView) tableRow.getChildAt(1);

                            if (!convertToEnglish(text1.getText().toString()).equals("")) {

                                cash.setCashierName(convertToEnglish(user.getText().toString()));
                                cash.setCheckInDate(convertToEnglish(date.getText().toString()));
                                cash.setCategoryName(convertToEnglish(text.getText().toString()));
                                cash.setCategoryValue(Double.parseDouble(convertToEnglish(text.getTag().toString())));
                                cash.setCategoryQty(Integer.parseInt(convertToEnglish(text1.getText().toString())));
                                cash.setOrderKind(0);
                                cashier.add(cash);
                            } else {
                                Toast.makeText(Main.this, getResources().getString(R.string.some_qty_not), Toast.LENGTH_SHORT).show();
                            }
                        }
                        mDHandler.addCashierInOut(cashier);
                        mDHandler.addClockInClockOut(clockInClockOut);
                        clockInSuccessful(times, dates); //this for Successful clockIn

                        dialogCashierIn.dismiss();
                    } else {
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(Main.this);
                        builderInner.setTitle(R.string.cashier_in_zero);
                        builderInner.setCancelable(false);
                        builderInner.setPositiveButton(getResources().getString(R.string.yes), (dialog1, which1) -> {
                            for (int i = 0; i < money.size(); i++) {
                                Cashier cash = new Cashier();
                                TableRow tableRow = (TableRow) categories.getChildAt(i);
                                TextView text = (TextView) tableRow.getChildAt(0);
                                TextView text1 = (TextView) tableRow.getChildAt(1);

                                if (!text1.getText().toString().equals("")) {

                                    cash.setCashierName(convertToEnglish(user.getText().toString()));
                                    cash.setCheckInDate(convertToEnglish(date.getText().toString()));
                                    cash.setCategoryName(convertToEnglish(text.getText().toString()));
                                    cash.setCategoryValue(Double.parseDouble(text.getTag().toString()));
                                    cash.setCategoryQty(Integer.parseInt(convertToEnglish(text1.getText().toString())));
                                    cash.setOrderKind(0);
                                    cashier.add(cash);
                                } else {
                                    Toast.makeText(Main.this, getResources().getString(R.string.some_qty_not), Toast.LENGTH_SHORT).show();
                                }
                            }
                            mDHandler.addCashierInOut(cashier);
                            dialogCashierIn.dismiss();

                            mDHandler.addClockInClockOut(clockInClockOut);
                            Toast.makeText(Main.this, getResources().getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
                            clockInSuccessful(times, dates); //this for Successful clockIn
                        });
                        builderInner.setNegativeButton(getResources().getString(R.string.no), (dialog1, i) -> {
                            dialog1.dismiss();
                        });
                        builderInner.show();
                    }


                } else {
                    Toast.makeText(Main.this, getResources().getString(R.string.add_money_category), Toast.LENGTH_SHORT).show();
                    dialogCashierIn.dismiss();

                }
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

                            double total = Double.parseDouble(convertToEnglish(text.getTag().toString())) * Double.parseDouble(convertToEnglish(focusedTextView.getText().toString()));
                            text2.setText("" + total);
                        }

                        mainTotal.setText("0.000");
                        for (int i = 0; i < money.size(); i++) {
                            TableRow tRow = (TableRow) categories.getChildAt(i);
                            TextView t = (TextView) tRow.getChildAt(2);
                            mainTotal.setText("" + (Double.parseDouble(convertToEnglish(mainTotal.getText().toString())) + Double.parseDouble(convertToEnglish(t.getText().toString()))));
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


        dialogCashierIn.show();

    }

    @SuppressLint("ClickableViewAccessibility")
    void showCashierOutDialog() {
        Dialog dialogCashierOut = new Dialog(Main.this);
        dialogCashierOut.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCashierOut.setCancelable(false);
        dialogCashierOut.setContentView(R.layout.cashier_out_dialog);
        dialogCashierOut.setCanceledOnTouchOutside(false);

//        Window window = dialogCashierOut.getWindow();
//        window.setLayout(920, 490);

        final ArrayList<Money> money = mDHandler.getAllMoneyCategory();

        categories = (TableLayout) dialogCashierOut.findViewById(R.id.money_categories);
        RadioGroup transType = dialogCashierOut.findViewById(R.id.transType);
        RadioButton finalClose = dialogCashierOut.findViewById(R.id.finalClose);
        RadioButton changeOver = dialogCashierOut.findViewById(R.id.changeOver);
        EditText toUser = dialogCashierOut.findViewById(R.id.toUser);
        final TextView cashTotals = (TextView) dialogCashierOut.findViewById(R.id.cashTotal);
        final TextView creditCard = (TextView) dialogCashierOut.findViewById(R.id.creditCard);
        final TextView cheque = (TextView) dialogCashierOut.findViewById(R.id.cheque);
        final TextView giftCard = (TextView) dialogCashierOut.findViewById(R.id.giftCard);
        final TextView credit = (TextView) dialogCashierOut.findViewById(R.id.credit);
        final TextView point = (TextView) dialogCashierOut.findViewById(R.id.point);
        final TextView otherPaymentTotal = (TextView) dialogCashierOut.findViewById(R.id.otherPaymentTotal);
        final TextView mainTotal = (TextView) dialogCashierOut.findViewById(R.id.mainTotal);
        final TextView user = (TextView) dialogCashierOut.findViewById(R.id.user);
        final TextView date = (TextView) dialogCashierOut.findViewById(R.id.date);
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
        b1 = (Button) dialogCashierOut.findViewById(R.id.b1);
        b2 = (Button) dialogCashierOut.findViewById(R.id.b2);
        b3 = (Button) dialogCashierOut.findViewById(R.id.b3);
        b4 = (Button) dialogCashierOut.findViewById(R.id.b4);
        b5 = (Button) dialogCashierOut.findViewById(R.id.b5);
        b6 = (Button) dialogCashierOut.findViewById(R.id.b6);
        b7 = (Button) dialogCashierOut.findViewById(R.id.b7);
        b8 = (Button) dialogCashierOut.findViewById(R.id.b8);
        b9 = (Button) dialogCashierOut.findViewById(R.id.b9);
        b0 = (Button) dialogCashierOut.findViewById(R.id.b0);
        clear = (Button) dialogCashierOut.findViewById(R.id.b_clear);
        save = (Button) dialogCashierOut.findViewById(R.id.save);

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

                if (!checkIsCashierInOutZero(money)) {
                    int tranType = 0;
                    if (finalClose.isChecked()) {
                        tranType = 0;
                    }
                    if (changeOver.isChecked()) {
                        tranType = 1;
                    }
                    saveCashierOutBase(categories, tranType, finalClose, changeOver, toUser, cashTotals, creditCard, cheque, giftCard,
                            credit, point, otherPaymentTotal, mainTotal, money, dialogCashierOut);

                    Toast.makeText(Main.this, getResources().getString(R.string.save_successful), Toast.LENGTH_SHORT).show();

                } else {

                    AlertDialog.Builder builderInner = new AlertDialog.Builder(Main.this);
                    builderInner.setTitle(getResources().getString(R.string.zero_cashier_q));
                    builderInner.setCancelable(false);
                    builderInner.setPositiveButton(getResources().getString(R.string.yes), (dialog1, which1) -> {

                        int tranType = 0;
                        if (finalClose.isChecked()) {
                            tranType = 0;
                        }
                        if (changeOver.isChecked()) {
                            tranType = 1;
                        }

                        saveCashierOutBase(categories, tranType, finalClose, changeOver, toUser, cashTotals, creditCard, cheque, giftCard,
                                credit, point, otherPaymentTotal, mainTotal, money, dialogCashierOut);

                        Toast.makeText(Main.this, getResources().getString(R.string.save_successful), Toast.LENGTH_SHORT).show();

                    });
                    builderInner.setNegativeButton(getResources().getString(R.string.no), (dialog1, i) -> {
                        dialog1.dismiss();
                    });
                    builderInner.show();

                }
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

        dialogCashierOut.show();


    }


    void showPayInDialog(final int transType) {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pay_in_out_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(920, 490);

        final TextView tranType = (TextView) dialog.findViewById(R.id.trans_type);
        final TextView date = (TextView) dialog.findViewById(R.id.date);
        final TextView serial = (TextView) dialog.findViewById(R.id.trans);
        final TextView value = (TextView) dialog.findViewById(R.id.value);
        final TextView remark = (TextView) dialog.findViewById(R.id.remark);
        final Button save = (Button) dialog.findViewById(R.id.save);
        final Button exit = (Button) dialog.findViewById(R.id.exit);

        String signal = "";
        tranType.setText(transType == 0 ? getResources().getString(R.string.pay_in) : getResources().getString(R.string.pay_out));
        if (transType == 0) {
            signal = "";
        } else signal = "-";
        date.setText(today);

        ArrayList<Pay> pays = mDHandler.getAllPayInOut();
        serial.setText(pays.size() == 0 ? getResources().getString(R.string.trans_no) + " : " + "1" : getResources().getString(R.string.trans_no) + " : " + (pays.size() + 1));

        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        final ArrayList<Money> money = mDHandler.getAllMoneyCategory();

        categories = (TableLayout) dialog.findViewById(R.id.money_categories);
        final TextView mainTotal = (TextView) dialog.findViewById(R.id.mainTotal);

        value.setText("0");
        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, clear;
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
        value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                focusedTextView = value;
                focusedTextView.setText("");
                focusedTextView.setTag("*");

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
                value.setText("0");
            }
        });
        String finalSignal = signal;
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Date currentTimeAndDate = Calendar.getInstance().getTime();
                SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
                String time = convertToEnglish(dfTime.format(currentTimeAndDate));
                Log.e("time123", "" + time);
                if (!value.getText().toString().equals("") && !mainTotal.getText().toString().equals("") && (Double.parseDouble(value.getText().toString()) != 0)) {
                    if (Double.parseDouble(value.getText().toString()) == Double.parseDouble(mainTotal.getText().toString())) {

                        //SAVE IN PAY_IN_OUT TABLE ...
                        if (!value.getText().toString().equals("")) {
                            mDHandler.addPayInOut(new Pay(transType, Settings.POS_number, Settings.user_no, Settings.user_name, today,
                                    Double.parseDouble(value.getText().toString()), remark.getText().toString(), Settings.shift_number,
                                    Settings.shift_name, time));
                            Toast.makeText(Main.this, getResources().getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        //SAVE IN CASHIER_IN_OUT TABLE ...

                        ArrayList<Cashier> cashier = new ArrayList<>();
                        for (int i = 0; i < money.size(); i++) {
                            Cashier cash = new Cashier();
                            TableRow tableRow = (TableRow) categories.getChildAt(i);
                            TextView text = (TextView) tableRow.getChildAt(0);
                            TextView text1 = (TextView) tableRow.getChildAt(1);

                            if (!text1.getText().toString().equals("")) {

                                cash.setCashierName(Settings.user_name);
                                cash.setCheckInDate(date.getText().toString());
                                cash.setCategoryName(text.getText().toString());
                                cash.setCategoryValue(Double.parseDouble(finalSignal + text.getTag().toString()));
                                cash.setCategoryQty(Integer.parseInt(text1.getText().toString()));
                                cash.setOrderKind(2);/// 2 --> pay in / out   1 --> trans (order - refund ) / 0 --> cashier iN
                                cashier.add(cash);
                            } else {
                                Toast.makeText(Main.this, getResources().getString(R.string.some_qty_not), Toast.LENGTH_SHORT).show();
                            }
                        }
                        mDHandler.addCashierInOut(cashier);
                        dialog.dismiss();
                    } else
                        Toast.makeText(Main.this, getResources().getString(R.string.total_from_cash_not_equal_value), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(Main.this, getResources().getString(R.string.ensure_your_input), Toast.LENGTH_SHORT).show();
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
                        if (!focusedTextView.getText().toString().equals("") && !focusedTextView.getTag().toString().equals("*")) {

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


    boolean checkIsCashierInOutZero(ArrayList<Money> money) {
        boolean isAllZero = true;
        for (int i = 0; i < money.size(); i++) {

            TableRow tableRow = (TableRow) categories.getChildAt(i);
            TextView text = (TextView) tableRow.getChildAt(1);
            TextView text1 = (TextView) tableRow.getChildAt(2);
//&& !text.getText().toString().equals("")// && !text1.getText().toString().equals("")
            if ((!text.getText().toString().equals("0")) && (!text1.getText().toString().equals("0"))) {
                isAllZero = false;
                break;
            }
        }

        return isAllZero;
    }


    void saveCashierOutBase(TableLayout categories, int tranType, RadioButton finalClose, RadioButton changeOver,
                            EditText toUser, TextView cashTotals, TextView creditCard, TextView cheque, TextView giftCard, TextView credit,
                            TextView point, TextView otherPaymentTotal, TextView mainTotal, ArrayList<Money> money, Dialog dialogCashierOut) {


        if (finalClose.isChecked() || (changeOver.isChecked() && !toUser.getText().toString().equals(""))) {
            ArrayList<OrderHeader> orderHeaders = mDHandler.getAllOrderHeader();
            ArrayList<PayMethod> payMethods = mDHandler.getAllExistingPay();

            int transNo = mDHandler.getAllBlindClose().size();

            Date currentTimeAndDate = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String time = convertToEnglish(df.format(currentTimeAndDate));

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
                    Settings.shift_name, Settings.user_no, Settings.user_name, sysSales, userSales, userSales - sysSales,
                    sysCash, userCash, userCash - sysCash, sysOthers, userOthers, userOthers - sysOthers, 0, tranType,
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
                        Settings.shift_name, Settings.user_no, Settings.user_name, catName, catQty, catValue, catQty * catValue,
                        "Cash", "", "", -1, "no-user"));
            }

            double creditCardValue = 0.0;
            double chequeValue = 0.0;
            double giftCardValue = 0.0;
            double creditValue = 0.0;
            double pointValue = 0.0;
            if (!creditCard.getText().toString().equals("")) {
                creditCardValue = Double.parseDouble(creditCard.getText().toString());
            }
            if (!cheque.getText().toString().equals("")) {
                chequeValue = Double.parseDouble(cheque.getText().toString());
            }
            if (!giftCard.getText().toString().equals("")) {
                giftCardValue = Double.parseDouble(giftCard.getText().toString());
            }
            if (!credit.getText().toString().equals("")) {
                creditValue = Double.parseDouble(credit.getText().toString());
            }
            if (!point.getText().toString().equals("")) {
                pointValue = Double.parseDouble(point.getText().toString());
            }

            if (creditCardValue != 0)
                mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                        Settings.shift_name, Settings.user_no, Settings.user_name, "Credit Card", 1, creditCardValue,
                        creditCardValue, "Credit Card", "", "", -1, "no-user"));

            if (chequeValue != 0)
                mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                        Settings.shift_name, Settings.user_no, Settings.user_name, "Cheque", 1, chequeValue,
                        chequeValue, "Cheque", "", "", -1, "no-user"));

            if (giftCardValue != 0)
                mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                        Settings.shift_name, Settings.user_no, Settings.user_name, "Gift Card", 1, giftCardValue,
                        giftCardValue, "Gift Card", "", "", -1, "no-user"));

            if (creditValue != 0)
                mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                        Settings.shift_name, Settings.user_no, Settings.user_name, "Credit", 1, creditValue,
                        creditValue, "Credit", "", "", -1, "no-user"));

            if (pointValue != 0)
                mDHandler.addBlindCloseDetails(new BlindCloseDetails(transNo, today, time, Settings.POS_number, Settings.shift_number,
                        Settings.shift_name, Settings.user_no, Settings.user_name, "Point", 1, pointValue,
                        pointValue, "Point", "", "", -1, "no-user"));

            dialogCashierOut.dismiss();
        } else
            Toast.makeText(Main.this, getResources().getString(R.string.to_user_field), Toast.LENGTH_LONG).show();


        mDHandler.updateStatusInBlindShiftIn(Settings.user_name, today);
        dialogCashierOut.dismiss();
        finish();
        Intent logInActivate = new Intent(Main.this, LogIn.class);
        startActivity(logInActivate);

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
                    if (String.valueOf(Settings.password).equals(focusedTextView.getText().toString())) {

                        int Size = mDHandler.getAllExistingClockInClockOut().size() - 1;
                        String TransType;
                        if (mDHandler.getAllExistingClockInClockOut().size() > 0)
                            TransType = mDHandler.getAllExistingClockInClockOut().get(Size).getTranstype();
                        else
                            TransType = "ClockOut";

                        switch (TransType) {
                            case "ClockOut":
                                dialog.dismiss();
                                showTimeCardDialog();
                                break;
                            case "ClockIN":
                                dialog.dismiss();
                                clockTimeOut();
                                break;
                            case "BreakOut":
                                dialog.dismiss();
                                clockTimeOut();
                                break;
                            case "BreakIN":
                                dialog.dismiss();
                                showBreakTimeOut();
                                break;
                        }
                    } else {
                        Toast.makeText(Main.this, getResources().getString(R.string.insert_correct_password), Toast.LENGTH_SHORT).show();
                        focusedTextView.setText("");
                    }
                } else {
                    Toast.makeText(Main.this, getResources().getString(R.string.enter_your_password), Toast.LENGTH_SHORT).show();
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

//        final Date currentTimeAndDate = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        final String today = df.format(currentTimeAndDate);
        date.setText(today);
        username.setText(Settings.user_name);

        clockIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final String times = convertToEnglish(time.getText().toString());

                Settings.time_card = 1;

                ClockInClockOut clockInClockOut = new ClockInClockOut();

                clockInClockOut.setPointOfSaleNumber(Settings.POS_number);
                clockInClockOut.setDate(today);
                clockInClockOut.setUserNO(Settings.user_no);
                clockInClockOut.setUserName(Settings.user_name);
                clockInClockOut.setTranstype("ClockIN");
                clockInClockOut.setDateCard(today);
                clockInClockOut.setTimeCard(times);
                clockInClockOut.setRemark((remark.getText().toString()));
                clockInClockOut.setShiftNo(Settings.shift_number);
                clockInClockOut.setShiftName(Settings.shift_name);


//                mDHandler.addClockInClockOut(clockInClockOut); // this in cashierInDialog ...

                showCashierInDialog(times, today, clockInClockOut);
            }
        });

        dialog.show();
    }

    void clockInSuccessful(String times, String dates) {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.clockin_successful_dialog);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        // window.setLayout(590, 290);

        TextView masege, time, date;

        masege = (TextView) dialog.findViewById(R.id.clockinsuccessfull);
        time = (TextView) dialog.findViewById(R.id.time2);
        date = (TextView) dialog.findViewById(R.id.date2);
        masege.setText(getResources().getString(R.string.clockinsuccessful) + "(" + Settings.user_name + ")");
        Button ok = (Button) dialog.findViewById(R.id.ok1);

        time.setText(convertToEnglish(times));
        date.setText(convertToEnglish(dates));

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

//        final Date currentTimeAndDate = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        final String today = df.format(currentTimeAndDate);
        date.setText(today);


        clockOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.time_card = 0;
                final String times = convertToEnglish(time.getText().toString());
                ClockInClockOut clockInClockOut = new ClockInClockOut();

                clockInClockOut.setPointOfSaleNumber(Settings.POS_number);
                clockInClockOut.setDate(today);
                clockInClockOut.setUserNO(Settings.user_no);
                clockInClockOut.setUserName(Settings.user_name);
                clockInClockOut.setTranstype("ClockOut");
                clockInClockOut.setDateCard(today);
                clockInClockOut.setTimeCard(times);
                clockInClockOut.setRemark((remarkOut.getText().toString()));
                clockInClockOut.setShiftNo(Settings.shift_number);
                clockInClockOut.setShiftName(Settings.shift_name);


                mDHandler.addClockInClockOut(clockInClockOut);
                showCashierOutDialog();

                dialog.dismiss();

            }
        });
        breakIN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.time_card = 2;
                final String times = convertToEnglish(time.getText().toString());
                ClockInClockOut clockInClockOut = new ClockInClockOut();

                clockInClockOut.setPointOfSaleNumber(Settings.POS_number);
                clockInClockOut.setDate(today);
                clockInClockOut.setUserNO(Settings.user_no);
                clockInClockOut.setUserName(Settings.user_name);
                clockInClockOut.setTranstype("BreakIN");
                clockInClockOut.setDateCard(today);
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

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }


    public void openRefundDialog() {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.refund_invoice);
        dialog.setCanceledOnTouchOutside(false);

        final EditText vhfNo;
        final TextView posNo, originalDate, originalTime, tableNo, customer;
        Button show, done, exit;
        lineDiscount = new ArrayList<>();
        DiscountArray = new ArrayList<>();

        final boolean[] flag = {true};
        final ArrayList<String> inVoucher = new ArrayList<>();
        orderTransactions = new ArrayList<>();
        rowRefund = new ArrayList<>();

        final boolean[] check = {false};

        refundTables = (TableLayout) dialog.findViewById(R.id.Table);
        table = (TableLayout) dialog.findViewById(R.id.table);
        vhfNo = (EditText) dialog.findViewById(R.id.VHF_NO);
        final String[] VHF_NO = new String[1];
        posNo = (TextView) dialog.findViewById(R.id.pos_NO);
        originalDate = (TextView) dialog.findViewById(R.id.VhfDate);
        originalTime = (TextView) dialog.findViewById(R.id.vhfTime);
        tableNo = (TextView) dialog.findViewById(R.id.tableNO);
        customer = (TextView) dialog.findViewById(R.id.customer);

        show = (Button) dialog.findViewById(R.id.bu_show);
        done = (Button) dialog.findViewById(R.id.bu_ok);
        exit = (Button) dialog.findViewById(R.id.bu_exit);


        posNo.setText(String.valueOf(Settings.POS_number));


        show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                VHF_NO[0] = convertToEnglish(vhfNo.getText().toString());

                for (int i = 0; i < inVoucher.size(); i++) {
                    if (!inVoucher.get(i).equals(VHF_NO[0])) {
                        check[0] = false;
                    } else {
                        check[0] = true;
                        break;
                    }
                }
                if (!check[0] && flag[0]) {
                    inVoucher.add(VHF_NO[0]);
                    orderTransactions = mDHandler.getAllRequestVoucher(VHF_NO[0]);
                    ArrayList<OrderTransactions> orderTransactions1 = new ArrayList<>();
                    List<String> item_ = new ArrayList<>();

                    if (!orderTransactions.isEmpty()) {
                        originalDate.setText(orderTransactions.get(0).getVoucherDate());
                        originalTime.setText(orderTransactions.get(0).getTime());
                        if (orderTransactions.get(0).getTableNo() != -1) {
                            tableNo.setText(String.valueOf(orderTransactions.get(0).getTableNo()));
                        } else {
                            tableNo.setText("-");
                        }
                        customer.setText("customer");

                        for (int q = 0; q < orderTransactions.size(); q++) {
                            item_.add(orderTransactions.get(q).getItemBarcode());
                        }
                        Set<String> sets = new HashSet<String>(item_);
                        item_.clear();
                        item_.addAll(sets);

                        int ind = -1, cou = 0;
                        for (int x = 0; x < item_.size(); x++) {
                            int qtyR_ = 0, qty_ = 0;
                            double total_R = 0.0, total = 0.0;
                            double liDec = 0.0, Dec = 0.0, liDec_r = 0.0, Dec_r = 0.0;
                            for (int y = 0; y < orderTransactions.size(); y++) {

                                if (item_.get(x).equals(orderTransactions.get(y).getItemBarcode())) {
                                    ind = y;
                                    if (orderTransactions.get(y).getQty() != 0) {
                                        if (orderTransactions.get(y).getOrderKind() == 998) {
                                            total_R += orderTransactions.get(y).getTotal();
                                            qtyR_ += orderTransactions.get(y).getQty();
                                            liDec_r += orderTransactions.get(y).getlDiscount();
                                            Dec_r += orderTransactions.get(y).getDiscount();
                                        } else if (orderTransactions.get(y).getOrderKind() == 0) {
                                            total += orderTransactions.get(y).getTotal();
                                            qty_ += orderTransactions.get(y).getQty();
                                            liDec += orderTransactions.get(y).getlDiscount();
                                            Dec += orderTransactions.get(y).getDiscount();
                                        }
                                    }
                                }

                            }
                            int cv = qty_ - qtyR_;
                            if (ind != -1 && cv > 0) {

                                orderTransactions1.add(orderTransactions.get(ind));
                                orderTransactions1.get(cou).setQty(qty_ - qtyR_);
                                orderTransactions1.get(cou).setTotal(total - total_R);
                                orderTransactions1.get(cou).setDiscount(Dec - Dec_r);
                                orderTransactions1.get(cou).setlDiscount(liDec - liDec_r);
                                cou++;
                            }
                        }
                        orderTransactions.clear();
                        orderTransactions = orderTransactions1;

                        if (orderTransactions.size() == 0)
                            notCorrectValueDialog(getString(R.string.cannot_return));

                        for (int i = 0; i < orderTransactions.size(); i++) {//if
                            if (!(orderTransactions.get(i).getQty() <= 0)) {

                                insertRow(orderTransactions.get(i).getVoucherSerial(), orderTransactions.get(i).getItemName(), orderTransactions.get(i).getQty(), orderTransactions, refundTables);


                            }
                        }
                        flag[0] = false;
                    } else {
                        Toast.makeText(Main.this, getResources().getString(R.string.invoice_no_not_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Main.this, getResources().getString(R.string.invoice_no_insert_bottom), Toast.LENGTH_SHORT).show();

                }
                vhfNo.setText("");
            }
        });

        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int textData;

                for (int i = 0; i < orderTransactions.size(); i++) {
                    text = dialog.findViewById(Integer.parseInt(i + "" + 5));
                    String textCheak = text.getText().toString();
                    if (textCheak.equals("-1")) {
                        CheckTrue = false;
                        break;
                    }
                }
                if (netTotals != 0.0 && CheckTrue) {
                    int index = 0;
                    for (int i = 0; i < orderTransactions.size(); i++) {
                        text = dialog.findViewById(Integer.parseInt(i + "3"));
                        if (!text.getText().toString().equals("")) {
                            textData = Integer.parseInt(text.getText().toString());
                            rowRefund.add(orderTransactions.get(i));
                            rowRefund.get(index).setQty(Integer.parseInt(text.getText().toString()));
                            index++;
                        } else {
                            textData = 0;
                        }

                        double lDiscon = orderTransactions.get(i).getlDiscount();
                        int q = orderTransactions.get(i).getQty();
                        lineDiscount.add(textData * (lDiscon / q));
                        DiscountArray.add(textData * (orderTransactions.get(i).getDiscount() / q));
                    }
                    textId = 0;
                    CheckTrue = true;
                    dialog.dismiss();
                    payMethodRefund(orderTransactions, VHF_NO[0]);

                }

            }
        });

        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textId = 0;
                netTotals = 0.0;
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    void insertRow(int serial, String itemName, final int qty, final ArrayList<OrderTransactions> list, final TableLayout recipeTable) {

        final TableRow row = new TableRow(Main.this);
        final double[] rTotal = {0.0};
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(lp);
        data = "0";

        for (int i = 0; i < 6; i++) {
            final EditText editText = new EditText(Main.this);
            final TextView textView = new TextView(Main.this);
            if (i == 3) {
                editText.setTextColor(ContextCompat.getColor(Main.this, R.color.text_color));
                editText.setGravity(Gravity.CENTER);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                editText.setLayoutParams(lp2);
                editText.setId(Integer.parseInt(textId + "3"));
//                row.setId(textId);
                row.addView(editText);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        idGeneral = Integer.parseInt(row.getId() + "4");
                        data = convertToEnglish(editText.getText().toString());
                        int id = row.getId();

                        if (!data.equals("")) {
                            if (Integer.parseInt(data) <= (list.get(id).getQty()) && Integer.parseInt(data) > 0) {
                                rows = row;
                                rows.setBackgroundColor(getResources().getColor(R.color.layer3));
                                rTotal[0] = ((Integer.parseInt(data)) * list.get(id).getPrice());
                                text = (TextView) dialog.findViewById(idGeneral);
                                text.setText(String.valueOf(rTotal[0]));
                                text = (TextView) dialog.findViewById(Integer.parseInt(row.getId() + "" + 5));
                                text.setText("0");
                                CheckTrue = true;
                            } else {
                                text = (TextView) dialog.findViewById(idGeneral);

                                notCorrectValueDialog(getResources().getString(R.string.this_value_not_correct));
                                rows = row;
                                rows.setBackgroundColor(getResources().getColor(R.color.exit_hover));
                                text.setText("0.0");
                                text = (TextView) dialog.findViewById(Integer.parseInt(row.getId() + "" + 5));
                                text.setText("-1");

                            }
                        } else {
                            text = (TextView) dialog.findViewById(idGeneral);
                            text.setText("0.0");
                        }


                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        totalAdd = 0.0;
                        discountAdd = 0.0;
                        netTotals = 0.0;

                        for (int i = 0; i < list.size(); i++) {
                            text = dialog.findViewById(Integer.parseInt(i + "4"));
                            Log.e("i=", "" + i + "4");
                            String da = convertToEnglish(text.getText().toString());
                            totalAdd += Double.parseDouble(da);
                            text = dialog.findViewById(Integer.parseInt(i + "3"));
                            String dataTest = convertToEnglish(text.getText().toString());
                            if (da.equals("0.0") || dataTest.equals("")) {
                                discountAdd += 0.0;
                            } else {
                                discountAdd += ((list.get(i).getDiscount() / list.get(i).getQty()) + (list.get(i).getlDiscount() / list.get(i).getQty())) * Integer.parseInt(dataTest);
                            }
                            netTotals = totalAdd - discountAdd;
                            balance = netTotals;
                            text = dialog.findViewById(R.id.total_);
                            text.setText(String.valueOf(totalAdd));

                            text = dialog.findViewById(R.id.discount);
                            text.setText(String.valueOf(discountAdd));

                            text = dialog.findViewById(R.id.net_total);
                            text.setText(String.valueOf(netTotals));


                        }


                    }
                });

            } else if (i == 5) {
                textView.setId(Integer.parseInt(textId + "" + i));
                textView.setText("0");
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(lp2);
                row.addView(textView);
            } else {
                switch (i) {
                    case 0:
                        textView.setText("" + serial);
                        break;
                    case 1:
                        textView.setText(itemName);
                        break;
                    case 2:
                        textView.setText("" + qty);
                        break;
                    case 4:
                        textView.setText("0.0");
                        break;

                }

                textView.setTextColor(ContextCompat.getColor(Main.this, R.color.text_color));
                textView.setGravity(Gravity.CENTER);

                textView.setId(Integer.parseInt(textId + "" + i));

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                textView.setLayoutParams(lp2);


                row.addView(textView);

            }

        }
        row.setId(textId);
        recipeTable.addView(row);
        textId++;

    }

    public void payMethodRefund(final ArrayList<OrderTransactions> list, final String VHF_NO) {
        dialog = new Dialog(Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pay_method_refund);
        dialog.setCanceledOnTouchOutside(false);

        ///
        final TextView cashValue, chequeValue, CreditValue, netTotalText, point, card, gift;
        cashValue = (TextView) dialog.findViewById(R.id.cashValue);
        chequeValue = (TextView) dialog.findViewById(R.id.chequeValue);
        CreditValue = (TextView) dialog.findViewById(R.id.creditValue);
        point = (TextView) dialog.findViewById(R.id.point);
        card = (TextView) dialog.findViewById(R.id.credit);
        gift = (TextView) dialog.findViewById(R.id.gift);

        categories = (TableLayout) dialog.findViewById(R.id.money_categorie);
        final ArrayList<Money> money = mDHandler.getAllMoneyCategory();

        String categoryName;
        double categoryValue = 0.0;
        int categoryQty = 0;
        final boolean[] flag = {true};

        netTotalText = (TextView) dialog.findViewById(R.id.nettotal);
        netTotalText.setText("" + netTotals);


        point.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                point.setText("");
                focusedTextView = point;
                flag[0] = true;

            }
        });
        card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                card.setText("");
                focusedTextView = card;
                flag[0] = true;

            }
        });
        gift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gift.setText("");
                focusedTextView = gift;
                flag[0] = true;

            }
        });

        cashValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cashValue.setText("");
                focusedTextView = cashValue;
                flag[0] = true;

            }
        });
        chequeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chequeValue.setText("");
                focusedTextView = chequeValue;
                flag[0] = true;

            }
        });
        CreditValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditValue.setText("");
                focusedTextView = CreditValue;
                flag[0] = true;

            }
        });


        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, dot, save, exit;
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
        dot = (Button) dialog.findViewById(R.id.dot);
        save = (Button) dialog.findViewById(R.id.save);
        exit = (Button) dialog.findViewById(R.id.exits);

        focusedTextView = cashValue;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag[0])
                    focusedTextView.setText(focusedTextView.getText().toString() + ".");
                flag[0] = false;

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTimeAndDate = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat Tf = new SimpleDateFormat("HH:mm:ss");
                String today = df.format(currentTimeAndDate);
                String times = Tf.format(currentTimeAndDate);
                ArrayList<String> listForPay = new ArrayList<>();
                ArrayList<Double> listValuePay = new ArrayList<>();

                if (!cashValue.getText().toString().equals("0") && !cashValue.getText().toString().equals("")) {
                    listForPay.add("cash");
                    listValuePay.add(Double.parseDouble(cashValue.getText().toString()));
                    cashValues = Double.parseDouble(cashValue.getText().toString());
                }
                if (!CreditValue.getText().toString().equals("0") && !CreditValue.getText().toString().equals("")) {
                    listForPay.add("Credit Card");
                    listValuePay.add(Double.parseDouble(CreditValue.getText().toString()));
                    creditValues = Double.parseDouble(CreditValue.getText().toString());
                }
                if (!chequeValue.getText().toString().equals("0") && !chequeValue.getText().toString().equals("")) {
                    listForPay.add("Cheque");
                    listValuePay.add(Double.parseDouble(chequeValue.getText().toString()));
                    chequeVales = Double.parseDouble(chequeValue.getText().toString());
                }
                if (!point.getText().toString().equals("0") && !point.getText().toString().equals("")) {
                    listForPay.add("Point");
                    listValuePay.add(Double.parseDouble(point.getText().toString()));
                    pointValues = Double.parseDouble(point.getText().toString());
                }
                if (!gift.getText().toString().equals("0") && !gift.getText().toString().equals("")) {
                    listForPay.add("Gift Card");
                    listValuePay.add(Double.parseDouble(gift.getText().toString()));
                    giftCardValues = Double.parseDouble(gift.getText().toString());
                }
                if (!card.getText().toString().equals("0") && !card.getText().toString().equals("")) {
                    listForPay.add("Coupon");
                    listValuePay.add(Double.parseDouble(card.getText().toString()));
                    cardValues = Double.parseDouble(card.getText().toString());
                }

                if (netTotalText.getText().toString().equals("0.0")) {

                    OrderHeader orderHeader = new OrderHeader(rowRefund.get(0).getOrderType(), 998, convertToEnglish(today), Settings.POS_number, Settings.store_number,
                            rowRefund.get(0).getVoucherNo(), rowRefund.get(0).getVoucherSerial(), totalAdd, lineDiscount.get(0), DiscountArray.get(0), lineDiscount.get(0) + DiscountArray.get(0),
                            Settings.service_value, rowRefund.get(0).getTaxValue(), rowRefund.get(0).getServiceTax(), netTotals,
                            netTotals, 1, rowRefund.get(0).getTableNo(),
                            rowRefund.get(0).getSectionNo(), cashValues, creditValues, chequeVales, cardValues,
                            giftCardValues, pointValues, Settings.shift_name, Settings.shift_number, orderTransactions.get(0).getUserName(), 0, Settings.user_name, Settings.user_no, convertToEnglish(times));

                    mDHandler.addOrderHeader(orderHeader);

                    for (int i = 0; i < rowRefund.size(); i++) {

                        OrderTransactions orderTransactions = new OrderTransactions(rowRefund.get(i).getOrderType(), 998, convertToEnglish(today), Settings.POS_number, Settings.store_number,
                                rowRefund.get(i).getVoucherNo(), rowRefund.get(i).getVoucherSerial(), "" + rowRefund.get(i).getItemBarcode(), rowRefund.get(i).getItemName(),
                                rowRefund.get(i).getSecondaryName(), rowRefund.get(i).getKitchenAlias(), rowRefund.get(i).getItemCategory(),
                                rowRefund.get(i).getItemFamily(), rowRefund.get(i).getQty(), rowRefund.get(i).getPrice(),
                                rowRefund.get(i).getQty() * rowRefund.get(i).getPrice(), DiscountArray.get(i), lineDiscount.get(i), lineDiscount.get(i) + DiscountArray.get(i), rowRefund.get(i).getTaxValue(),
                                rowRefund.get(i).getTaxPerc(), 0, rowRefund.get(i).getService(), rowRefund.get(i).getServiceTax(),
                                rowRefund.get(i).getTableNo(), rowRefund.get(i).getSectionNo(), Settings.shift_number, Settings.shift_name, Settings.user_no, Settings.user_name, convertToEnglish(times));


                        mDHandler.addOrderTransaction(orderTransactions);

                    }

//                    for (int i = 0; i < money.size(); i++) {
//                        TableRow tRow = (TableRow) categories.getChildAt(i);
//                        TextView t = (TextView) tRow.getChildAt(2);
//                        TextView t0 = (TextView) tRow.getChildAt(0);
//                        TextView t1 = (TextView) tRow.getChildAt(1);
//                        if (!t1.getText().toString().equals("0") && !t1.getText().toString().equals("")) {
//                            Cashier cashier = new Cashier();
//                            ArrayList<Cashier> cashiersList = new ArrayList<Cashier>();
//                            cashier.setCashierName(Settings.user_name);
//                            cashier.setCategoryName(t0.getText().toString());
//                            cashier.setCategoryQty(Integer.parseInt(t1.getText().toString()));
//                            cashier.setCategoryValue(Double.parseDouble("-" + t.getText().toString()));
//                            cashier.setCheckInDate(today);
//                            cashier.setOrderKind(1);
//
//                            cashiersList.add(cashier);
//
//                            mDHandler.addCashierInOut(cashiersList);
//                        }
//                    }


                    if (Double.parseDouble(cashValue.getText().toString()) != 0 && !cashValue.getText().toString().equals("")) {
                        Cashier cashier = new Cashier();
                        ArrayList<Cashier> cashiersList = new ArrayList<Cashier>();
                        cashier.setCashierName(Settings.user_name);
                        cashier.setCategoryName("null");
                        cashier.setCategoryQty(-1);
                        cashier.setCategoryValue(Double.parseDouble(cashValue.getText().toString()));
                        cashier.setCheckInDate(today);
                        cashier.setOrderKind(1);

                        cashiersList.add(cashier);

                        mDHandler.addCashierInOut(cashiersList);
                    }

                    ArrayList<PayMethod> listOrder = new ArrayList();
                    listOrder = mDHandler.getAllRequestPayMethod(VHF_NO);
                    String payNumber = "0", payName = "0";
                    for (int x = 0; x < listForPay.size(); x++) {

                        for (int i = 0; i < listOrder.size(); i++) {
                            if (listForPay.get(x).equals(listOrder.get(i).getPayType())) {
                                payNumber = listOrder.get(i).getPayNumber();

                                payName = listOrder.get(i).getPayName();
                                Log.e("paynum : ", payNumber + "     --> " + payName);
                                break;
                            }
                            Log.e("paynum1 : ", payNumber + "     --> " + payName);
                        }

                        PayMethod payMethod = new PayMethod(list.get(0).getOrderType(),
                                998,
                                convertToEnglish(today),
                                Settings.POS_number,
                                Settings.store_number, list.get(0).getVoucherNo(), list.get(0).getVoucherSerial(), listForPay.get(x),
                                listValuePay.get(x), payNumber, payName, Settings.shift_name, Settings.shift_number, Settings.user_name, Settings.user_no, convertToEnglish(times));

                        mDHandler.addAllPayMethodItem(payMethod);
                    }
                    netTotals = 0.0;
                    dialog.dismiss();
                    rowRefund.clear();

                } else {
                    Toast.makeText(Main.this, getResources().getString(R.string.total_not_allow), Toast.LENGTH_SHORT).show();
                }

            }

        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                netTotals = 0.0;
                dialog.dismiss();
                rowRefund.clear();

            }
        });

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                balance = netTotals;
                String pointV, cashV, CreditV, chequeV, giftV, cardV;

                if (!point.getText().toString().equals("")) {
                    pointV = point.getText().toString();
                } else {
                    pointV = "0";
                }
                if (!cashValue.getText().toString().equals("")) {
                    cashV = cashValue.getText().toString();
                } else {
                    cashV = "0";
                }
                if (!CreditValue.getText().toString().equals("")) {
                    CreditV = CreditValue.getText().toString();
                } else {
                    CreditV = "0";
                }
                if (!chequeValue.getText().toString().equals("")) {
                    chequeV = chequeValue.getText().toString();
                } else {
                    chequeV = "0";
                }
                if (!gift.getText().toString().equals("")) {
                    giftV = gift.getText().toString();
                } else {
                    giftV = "0";
                }
                if (!card.getText().toString().equals("")) {
                    cardV = card.getText().toString();
                } else {
                    cardV = "0";
                }

                balance = netTotals - (Double.parseDouble(cashV) + Double.parseDouble(chequeV) +
                        Double.parseDouble(CreditV) + Double.parseDouble(pointV) +
                        Double.parseDouble(giftV) + Double.parseDouble(cardV));

                netTotalText.setText("" + balance);

            }
        };
        cashValue.addTextChangedListener(textWatcher);
        chequeValue.addTextChangedListener(textWatcher);
        CreditValue.addTextChangedListener(textWatcher);
        point.addTextChangedListener(textWatcher);
        gift.addTextChangedListener(textWatcher);
        card.addTextChangedListener(textWatcher);


        ///"""""""""""""""""""""""""""""""""""""""
//
//        for (int i = 0; i < money.size(); i++) {
//            final int position = i;
//            TableRow row = new TableRow(Main.this);
//            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(0, 10, 0, 5);
//            row.setLayoutParams(lp);
//
//            TextView textView = new TextView(Main.this);
//            textView.setText(money.get(i).getCatName() + "   ");
//            textView.setTag(money.get(i).getCatValue());
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextColor(getResources().getColor(R.color.text_color));
//
//            final TextView textView1 = new TextView(Main.this);
//            textView1.setBackgroundColor(getResources().getColor(R.color.layer1));
//            textView1.setHeight(26);
//            textView1.setPadding(10, 0, 0, 0);
//            textView1.setTextColor(getResources().getColor(R.color.text_color));
//            textView1.setText("0");
//            textView1.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (focusedTextView != null && convertToEnglish(focusedTextView.getText().toString()).equals("")) {
//                        focusedTextView.setText("0");
//                    }
//
//                    focusedTextView = textView1;
//                    focusedTextView.setTag("" + position);
//                    focusedTextView.setText("");
//                }
//            });
//
//            textView1.addTextChangedListener(new TextWatcher() {
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (focusedTextView != null) {
//                        if (!convertToEnglish(focusedTextView.getText().toString()).equals("")) {
//
//                            TableRow tableRow = (TableRow) categories.getChildAt(Integer.parseInt(convertToEnglish(focusedTextView.getTag().toString())));
//                            TextView text = (TextView) tableRow.getChildAt(0);
//                            TextView text2 = (TextView) tableRow.getChildAt(2);
//
//                            double total = Double.parseDouble(convertToEnglish(text.getTag().toString())) * Double.parseDouble(convertToEnglish(focusedTextView.getText().toString()));
//                            text2.setText("" + total);
//                        }
//
//                        cashValue.setText("0.000");
//                        for (int i = 0; i < money.size(); i++) {
//                            TableRow tRow = (TableRow) categories.getChildAt(i);
//                            TextView t = (TextView) tRow.getChildAt(2);
//                            cashValue.setText("" + (Double.parseDouble(cashValue.getText().toString()) + Double.parseDouble(t.getText().toString())));
//                        }
//
//                    }
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count,
//                                              int after) {
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });
//
//
//            TextView textView2 = new TextView(Main.this);
//            textView2.setText("0");
//
//            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(130, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
//            lp2.setMargins(15, 0, 15, 0);
//            textView.setLayoutParams(lp2);
//            textView1.setLayoutParams(lp2);
//            textView2.setLayoutParams(lp2);
//            textView2.setGravity(Gravity.CENTER);
//            textView2.setTextColor(getResources().getColor(R.color.text_color));
//
//            row.addView(textView);
//            row.addView(textView1);
//            row.addView(textView2);
//
//            categories.addView(row);
//        }

        ///"""""""""""""""""""""""""""""""""""""""


        dialog.show();
    }


    public void notCorrectValueDialog(String mass) {
        Dialog dialog1 = new Dialog(Main.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.not_correct_dialog);
        dialog1.setCanceledOnTouchOutside(true);
        TextView text = (TextView) dialog1.findViewById(R.id.not);

        text.setText(mass);

        dialog1.show();

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
                clockInClockOut.setUserNO(Settings.user_no);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    void initialize() {

        back = (Button) findViewById(R.id.back);
        exit = (Button) findViewById(R.id.exit);
        takeAway = (Button) findViewById(R.id.tack_away);
        dineIn = (Button) findViewById(R.id.dine_in);
        refund = (TextView) findViewById(R.id.refund);
        userName = (TextView) findViewById(R.id.user_name);
        shift = (TextView) findViewById(R.id.shift);
        date = (TextView) findViewById(R.id.date);

        payIn = (TextView) findViewById(R.id.pay_in);
        payOut = (TextView) findViewById(R.id.pay_out);
        timeCard = (TextView) findViewById(R.id.time_card);
        safeMode = (TextView) findViewById(R.id.safe_mode);
        cashDrawer = (TextView) findViewById(R.id.cash_drawer);

        annText = (TextView) findViewById(R.id.annText);
        AnnouncementTable = (TableLayout) findViewById(R.id.AnnouncmentTable);

        back.setOnClickListener(onClickListener);
        exit.setOnClickListener(onClickListener);
        takeAway.setOnClickListener(onClickListener);
        dineIn.setOnClickListener(onClickListener);

        payIn.setOnClickListener(onClickListener);
        payOut.setOnClickListener(onClickListener);
        timeCard.setOnClickListener(onClickListener);
        safeMode.setOnClickListener(onClickListener);
        cashDrawer.setOnClickListener(onClickListener);
        refund.setOnClickListener(onClickListener);

        back.setOnTouchListener(onTouchListener);
        exit.setOnTouchListener(onTouchListener);
        takeAway.setOnTouchListener(onTouchListener);
        dineIn.setOnTouchListener(onTouchListener);
    }
}
