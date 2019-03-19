package com.tamimi.sundos.restpos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.BackOffice.BackOfficeActivity;
import com.tamimi.sundos.restpos.BackOffice.EmployeeRegistration;
import com.tamimi.sundos.restpos.BackOffice.MenuRegistration;
import com.tamimi.sundos.restpos.Models.Cashier;
import com.tamimi.sundos.restpos.Models.EmployeeRegistrationModle;
import com.tamimi.sundos.restpos.Models.Money;
import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.PayMethod;
import com.tamimi.sundos.restpos.Models.Tables;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class DineIn extends AppCompatActivity {


    ArrayList<Tables> currentList, list0, list1, list2, list3, list4, list5;

    Dialog dialog;
    Button addIcon, save;
    Button mainF, firstF, secondF, thirdF, fourthF, fifthF;
    TextView move, merge, reservation, takeAway, close, cashDrawer, refund, checkOut, reprint;
    LinearLayout add, rightBorder;
    ViewGroup land;
    TextView focusedTextView;
    LinearLayout focused = null;
    GestureDetector gestureDetector;
    boolean CheckTrue = true;
    int tableNumber;
    int current = 0;
    String waiter , waiterNo;

    int fromSection, toSection;
    List<String> tablesNoLeft, tablesNoRight;
    int focusedLeft = -1, focusedRight = -1;
    ImageView movingTable;

    TextView text;
    TableRow rows;
    double totalAdd = 0.0;
    double cashValues, creditValues, chequeVales, pointValues, giftCardValues, cardValues;
    double discountAdd = 0.0;
    ArrayList<Double> lineDiscount;
    ArrayList<Double> DiscountArray;
    double netTotals = 0.0;
    double balance = 0.0;
    int textId = 0;
    int idGeneral = 0;
    String data;
    List<OrderTransactions> greenTables;
    DatabaseHandler mHandler;
    TableLayout refundTables, table;
    ArrayList<OrderTransactions> orderTransactions;
    ArrayList<OrderTransactions> rowRefund;
    TableLayout categories;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dine_in);

        mHandler = new DatabaseHandler(DineIn.this);
        greenTables = mHandler.getAllOrderTransactionsTemp();
//        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        initialize();
        focusedTextView = null;
        //_________________________________________________________________________________

        currentList = new ArrayList<Tables>();
        list0 = mHandler.getRequestedTables(0);
        list1 = mHandler.getRequestedTables(1);
        list2 = mHandler.getRequestedTables(2);
        list3 = mHandler.getRequestedTables(3);
        list4 = mHandler.getRequestedTables(4);
        list5 = mHandler.getRequestedTables(5);

        switch (current) {
            case 0:
                currentList = list0;
            case 1:
                currentList = list1;
            case 2:
                currentList = list2;
            case 3:
                currentList = list3;
            case 4:
                currentList = list4;
            case 5:
                currentList = list5;
        }

        fillMainFloor(); // when open the activity the main floor is active by default
    }

    OnClickListener onRightListListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.move:
                    openMoveDialog();
                    break;

                case R.id.merge:
                    openMergeDialog();
                    break;

                case R.id.reservation:
                    break;

                case R.id.takeAway:
                    break;

                case R.id.close:
                    break;

                case R.id.cash_drawer:
                    break;

                case R.id.refund:
                    openRefundDialog();
                    break;

                case R.id.checkOut:
                    openCheckOutDialog();
                    break;

                case R.id.reprint:
                    mHandler.deleteAllOrders();
                    break;
            }
        }
    };

    OnClickListener onFloorClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            freeze();
            unfocusFloors();
            removeTables();

            switch (view.getId()) {

                case R.id.main:
                    current = 0;
                    currentList = list0;
                    mainF.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                    break;

                case R.id.first_f:
                    current = 1;
                    currentList = list1;
                    firstF.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                    break;

                case R.id.second_f:
                    current = 2;
                    currentList = list2;
                    secondF.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                    break;

                case R.id.third_f:
                    current = 3;
                    currentList = list3;
                    thirdF.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                    break;

                case R.id.fourth_f:
                    current = 4;
                    currentList = list4;
                    fourthF.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                    break;

                case R.id.fifth_f:
                    current = 5;
                    currentList = list5;
                    fifthF.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                    break;

            }

            for (int i = 0; i < currentList.size(); i++) {
                LinearLayout linearLayout = new LinearLayout(DineIn.this);
                ImageView imageView = new ImageView(DineIn.this);
                TextView textView = new TextView(DineIn.this);

                imageView.setBackgroundResource(currentList.get(i).getImageResource());
                imageView.setTag(currentList.get(i).getImageResource());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(currentList.get(i).getHeight() - 20, currentList.get(i).getWidth());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(currentList.get(i).getHeight(), currentList.get(i).getWidth());

                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(50, 15);
                textView.setLayoutParams(params2);
                textView.setText("" + i);
                textView.setTextColor(getResources().getColor(R.color.text_color));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

                linearLayout.setOnClickListener(onTableClickListener);
                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                imageView.setLayoutParams(param);
                linearLayout.setX(currentList.get(i).getMarginLeft());
                linearLayout.setY(currentList.get(i).getMarginTop());
                linearLayout.setLayoutParams(params);
                if (checkGreen(textView.getText().toString(), current)) {
                    linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_light));
                }
                land.addView(linearLayout);

            }

        }

    };

    boolean checkGreen(String tableNo, int section) {
        for (int i = 0; i < greenTables.size(); i++)
            if (greenTables.get(i).getSectionNo() == section && greenTables.get(i).getTableNo() == Integer.parseInt(tableNo))
                return true;

        return false;
    }

    OnClickListener onTableClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view.getBackground() == null) { // not green
                dialog = new Dialog(DineIn.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.pick_waiter_dialog);
                dialog.setCanceledOnTouchOutside(true);

                final LinearLayout linearLayout = dialog.findViewById(R.id.linear);
                Button done = dialog.findViewById(R.id.b_done);

                ArrayList<EmployeeRegistrationModle> employees = mHandler.getAllEmployeeRegistration();

                for (int i = 0; i < employees.size(); i++) {
                    if (employees.get(i).getEmployeeType() == 1) {

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);

                        final TextView textView = new TextView(DineIn.this);
                        textView.setText("" + employees.get(i).getEmployeeName());
                        textView.setTextColor(getResources().getColor(R.color.text_color));
                        textView.setTextSize(22);
                        textView.setGravity(Gravity.BOTTOM);
                        textView.setLayoutParams(lp);

                        final TextView textView2 = new TextView(DineIn.this);
                        textView2.setText("" + employees.get(i).getEmployeeNO());

                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(50, 40);
                        lp2.setMargins(3, 5, 7, 5);

                        final ImageView imageView = new ImageView(DineIn.this);
                        imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.waiter));
                        imageView.setLayoutParams(lp2);

                        final LinearLayout linearLayout1 = new LinearLayout(DineIn.this);
                        linearLayout1.addView(imageView);
                        linearLayout1.addView(textView);
                        linearLayout1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                waiter = textView.getText().toString();
                                waiterNo = textView2.getText().toString();
                                setTableBackground(linearLayout, linearLayout1);
                            }
                        });

                        linearLayout.addView(linearLayout1);
                    }
                }
                done.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        openSeatsNumberDialog();
                    }
                });
                dialog.show();

                focused = (LinearLayout) view;
                TextView textView = (TextView) focused.getChildAt(1);
                tableNumber = Integer.parseInt(textView.getText().toString());

            } else {
                focused = (LinearLayout) view;
                TextView textView = (TextView) focused.getChildAt(1);
                tableNumber = Integer.parseInt(textView.getText().toString());

                String waiterName = "";
                ArrayList<OrderHeader> orderHeader = mHandler.getOrderHeaderTemp("" + current, "" + tableNumber);
                for(int i=0 ; i<orderHeader.size() ; i++){
                    if(orderHeader.get(i).getTableNO() == tableNumber) {
                        waiterName = orderHeader.get(i).getWaiter();
                        break;
                    }
                }

                String waiterNumber = "";
                ArrayList<EmployeeRegistrationModle> employees = mHandler.getAllEmployeeRegistration();
                for(int i=0 ; i<employees.size() ; i++){
                    if(employees.get(i).getEmployeeName().equals(waiterName)) {
                        waiterNumber = "" + employees.get(i).getEmployeeNO();
                        break;
                    }
                }
                Log.e("test " , "" + waiterName );
                Log.e("test " , "" + waiterNumber );

                Intent intent = new Intent(DineIn.this, Order.class);
                intent.putExtra("flag", "1");
                intent.putExtra("tableNo", tableNumber);
                intent.putExtra("sectionNo", current);
                intent.putExtra("waiter", waiterName);
                intent.putExtra("waiterNo", waiterNumber);
                startActivity(intent);
            }
        }
    };

    public void openRefundDialog() {
        dialog = new Dialog(DineIn.this);
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


                VHF_NO[0] = vhfNo.getText().toString();

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
                    orderTransactions = mHandler.getAllRequestVoucher(VHF_NO[0]);
                    if (!orderTransactions.isEmpty()) {
                        originalDate.setText(orderTransactions.get(0).getVoucherDate());
                        originalTime.setText(orderTransactions.get(0).getVoucherDate());
                        if (orderTransactions.get(0).getTableNo() != -1) {
                            tableNo.setText(String.valueOf(orderTransactions.get(0).getTableNo()));
                        } else {
                            tableNo.setText("-");
                        }
                        customer.setText("customer");


                        for (int i = 0; i < orderTransactions.size(); i++) {
//                            if(!(orderTransactions.get(i).getOrderKind()==998)) {
                            insertRow(orderTransactions.get(i).getVoucherSerial(), orderTransactions.get(i).getItemName(), orderTransactions.get(i).getQty(), orderTransactions, refundTables);
//                            }
                        }
                        flag[0] = false;
                    } else {
                        Toast.makeText(DineIn.this, "This InVoice Number not found ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DineIn.this, "This InVoice Number insert in bottom table  ", Toast.LENGTH_SHORT).show();

                }
                vhfNo.setText("");
            }
        });

        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                double textData;

                for (int i = 0; i < orderTransactions.size(); i++) {
                    text = dialog.findViewById(Integer.parseInt(i + "" + 5));
                    String textCheak = text.getText().toString();
                    if (textCheak.equals("-1")) {
                        CheckTrue = false;
                        break;
                    }
                }
                if (netTotals != 0.0 && CheckTrue) {
                    for (int i = 0; i < orderTransactions.size(); i++) {
                        text = dialog.findViewById(Integer.parseInt(i + "3"));
                        if (!text.getText().toString().equals("")) {
                            textData = Double.parseDouble(text.getText().toString());
                            rowRefund.add(orderTransactions.get(i));

                        } else {
                            textData = 0.0;
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

        final TableRow row = new TableRow(DineIn.this);
        final double[] rTotal = {0.0};
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(lp);
        data = "0";

        for (int i = 0; i < 6; i++) {
            final EditText editText = new EditText(DineIn.this);
            final TextView textView = new TextView(DineIn.this);
            if (i == 3) {
                editText.setTextColor(ContextCompat.getColor(DineIn.this, R.color.text_color));
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
                        data = editText.getText().toString();
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

                                notCorrectValueDialog();
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
                            String da = text.getText().toString();
                            totalAdd += Double.parseDouble(da);
                            text = dialog.findViewById(Integer.parseInt(i + "3"));
                            String dataTest = text.getText().toString();
                            if (da == "0.0" || dataTest.equals("")) {
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

                textView.setTextColor(ContextCompat.getColor(DineIn.this, R.color.text_color));
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
        dialog = new Dialog(DineIn.this);
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
        final ArrayList<Money> money = mHandler.getAllMoneyCategory();

        String categoryName;
        double categoryValue = 0.0;
        int categoryQty = 0;


        netTotalText = (TextView) dialog.findViewById(R.id.nettotal);
        netTotalText.setText("" + netTotals);


        point.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                point.setText("");
                focusedTextView = point;

            }
        });
        card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                card.setText("");
                focusedTextView = card;

            }
        });
        gift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gift.setText("");
                focusedTextView = gift;

            }
        });

        cashValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cashValue.setText("");
                focusedTextView = cashValue;

            }
        });
        chequeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chequeValue.setText("");
                focusedTextView = chequeValue;

            }
        });
        CreditValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditValue.setText("");
                focusedTextView = CreditValue;

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

                focusedTextView.setText(focusedTextView.getText().toString() + ".");


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTimeAndDate = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String today = df.format(currentTimeAndDate);
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

                    OrderHeader orderHeader = new OrderHeader(rowRefund.get(0).getOrderType(), 998, today, Settings.POS_number, Settings.store_number,
                            rowRefund.get(0).getVoucherNo(), rowRefund.get(0).getVoucherSerial(), totalAdd, lineDiscount.get(0), DiscountArray.get(0), lineDiscount.get(0) + DiscountArray.get(0),
                            Settings.service_value, rowRefund.get(0).getTaxValue(), rowRefund.get(0).getServiceTax(), netTotals,
                            netTotals, 1, rowRefund.get(0).getTableNo(),
                            rowRefund.get(0).getSectionNo(), cashValues, creditValues, chequeVales, cardValues,
                            giftCardValues, pointValues, Settings.shift_name, Settings.shift_number, "No Waiter", 0, Settings.user_name, Settings.password);

                    mHandler.addOrderHeader(orderHeader);

                    for (int i = 0; i < rowRefund.size(); i++) {

                        OrderTransactions orderTransactions = new OrderTransactions(rowRefund.get(i).getOrderType(), 998, today, Settings.POS_number, Settings.store_number,
                                rowRefund.get(i).getVoucherNo(), rowRefund.get(i).getVoucherSerial(), "" + rowRefund.get(i).getItemBarcode(), rowRefund.get(i).getItemName(),
                                rowRefund.get(i).getSecondaryName(), rowRefund.get(i).getKitchenAlias(), rowRefund.get(i).getItemCategory(),
                                rowRefund.get(i).getItemFamily(), rowRefund.get(i).getQty(), rowRefund.get(i).getPrice(),
                                totalAdd, DiscountArray.get(i), lineDiscount.get(i), lineDiscount.get(i) + DiscountArray.get(i), rowRefund.get(i).getTaxValue(),
                                rowRefund.get(i).getTaxPerc(), 0, rowRefund.get(i).getService(), rowRefund.get(i).getServiceTax(),
                                rowRefund.get(i).getTableNo(), rowRefund.get(i).getSectionNo(), Settings.shift_number, Settings.shift_name, Settings.password, Settings.user_name);


                        mHandler.addOrderTransaction(orderTransactions);

                    }

                    for (int i = 0; i < money.size(); i++) {
                        TableRow tRow = (TableRow) categories.getChildAt(i);
                        TextView t = (TextView) tRow.getChildAt(2);
                        TextView t0 = (TextView) tRow.getChildAt(0);
                        TextView t1 = (TextView) tRow.getChildAt(1);
                        if (!t1.getText().toString().equals("0") && !t1.getText().toString().equals("")) {
                            Cashier cashier = new Cashier();
                            ArrayList<Cashier> cashiersList = new ArrayList<Cashier>();
                            cashier.setCashierName(Settings.user_name);
                            cashier.setCategoryName(t0.getText().toString());
                            cashier.setCategoryQty(Integer.parseInt(t1.getText().toString()));
                            cashier.setCategoryValue(Double.parseDouble("-" + t.getText().toString()));
                            cashier.setCheckInDate(today);
                            cashier.setOrderKind(1);

                            cashiersList.add(cashier);

                            mHandler.addCashierInOut(cashiersList);
                        }
                    }

                    ArrayList<PayMethod> listOrder = new ArrayList();
                    listOrder = mHandler.getAllRequestPayMethod(VHF_NO);
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
                                today,
                                Settings.POS_number,
                                Settings.store_number, list.get(0).getVoucherNo(), list.get(0).getVoucherSerial(), listForPay.get(x),
                                listValuePay.get(x), payNumber, payName, Settings.shift_number, Settings.shift_name, Settings.user_name, Settings.password);

                        mHandler.addAllPayMethodItem(payMethod);
                    }
                    netTotals = 0.0;
                    dialog.dismiss();
                    rowRefund.clear();

                } else {
                    Toast.makeText(DineIn.this, "The Net Total not allow to 0.0", Toast.LENGTH_SHORT).show();
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

        for (int i = 0; i < money.size(); i++) {
            final int position = i;
            TableRow row = new TableRow(DineIn.this);
            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 5);
            row.setLayoutParams(lp);

            TextView textView = new TextView(DineIn.this);
            textView.setText(money.get(i).getCatName() + "   ");
            textView.setTag(money.get(i).getCatValue());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.text_color));

            final TextView textView1 = new TextView(DineIn.this);
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

                        cashValue.setText("0.000");
                        for (int i = 0; i < money.size(); i++) {
                            TableRow tRow = (TableRow) categories.getChildAt(i);
                            TextView t = (TextView) tRow.getChildAt(2);
                            cashValue.setText("" + (Double.parseDouble(cashValue.getText().toString()) + Double.parseDouble(t.getText().toString())));
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


            TextView textView2 = new TextView(DineIn.this);
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

        ///"""""""""""""""""""""""""""""""""""""""


        dialog.show();
    }

    public void notCorrectValueDialog() {
        Dialog dialog1 = new Dialog(DineIn.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.not_correct_dialog);
        dialog1.setCanceledOnTouchOutside(true);

        dialog1.show();

    }

    public void openSeatsNumberDialog() {
        dialog = new Dialog(DineIn.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.seats_number_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final EditText seatsNo = dialog.findViewById(R.id.seatsNo);
        final EditText male = dialog.findViewById(R.id.male);
        final EditText female = dialog.findViewById(R.id.female);
        final EditText children = dialog.findViewById(R.id.children);
        Button ok = dialog.findViewById(R.id.okay);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String males = ((male.getText().toString().equals("")) ? "0" : male.getText().toString());
                String females = ((female.getText().toString().equals("")) ? "0" : female.getText().toString());
                String childrens = ((children.getText().toString().equals("")) ? "0" : children.getText().toString());

                int sum = (int) Double.parseDouble(males) +
                        (int) Double.parseDouble(females) +
                        (int) Double.parseDouble(childrens);
                seatsNo.setText("" + sum);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        male.addTextChangedListener(textWatcher);
        female.addTextChangedListener(textWatcher);
        children.addTextChangedListener(textWatcher);

        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatsNo.getText().toString() != "" && male.getText().toString() != "" && female.getText().toString() != "" && children.getText().toString() != "") {
                    int sum = (int) Double.parseDouble(male.getText().toString()) +
                            (int) Double.parseDouble(female.getText().toString()) +
                            (int) Double.parseDouble(children.getText().toString());

                    if ((int) Double.parseDouble(seatsNo.getText().toString()) == sum) {

                        dialog.dismiss();

                        Intent intent = new Intent(DineIn.this, Order.class);
                        intent.putExtra("flag", "1");
                        intent.putExtra("tableNo", tableNumber);
                        intent.putExtra("sectionNo", current);
                        intent.putExtra("waiter", waiter);

                        intent.putExtra("waiterNo", waiterNo);
                        intent.putExtra("seatNo", sum);
                        startActivity(intent);

                    } else {
                        Toast.makeText(DineIn.this, "Number of seats doesn't machs number of people", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DineIn.this, "Please fill all requested fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void openMoveDialog() {
        dialog = new Dialog(DineIn.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.move_table_dialog);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout linearLayoutLeft = dialog.findViewById(R.id.linearButtonsLeft);
        LinearLayout linearLayoutRight = dialog.findViewById(R.id.linearButtonsRight);
        GridView gridViewLeft = dialog.findViewById(R.id.GridViewLeft);
        final GridView gridViewRight = dialog.findViewById(R.id.GridViewRight);
        final Button mainL = dialog.findViewById(R.id.b_main1);
        final Button firstL = dialog.findViewById(R.id.b_f1);
        final Button secondL = dialog.findViewById(R.id.b_s1);
        final Button thirdL = dialog.findViewById(R.id.b_t1);
        final Button fourthL = dialog.findViewById(R.id.b_fo1);
        final Button fifthL = dialog.findViewById(R.id.b_fi1);
        final Button mainR = dialog.findViewById(R.id.b_main2);
        final Button firstR = dialog.findViewById(R.id.b_f2);
        final Button secondR = dialog.findViewById(R.id.b_s2);
        final Button thirdR = dialog.findViewById(R.id.b_t2);
        final Button fourthR = dialog.findViewById(R.id.b_fo2);
        final Button fifthR = dialog.findViewById(R.id.b_fi2);

        Button move = dialog.findViewById(R.id.move);
        tablesNoRight = new ArrayList<>();

        for (int i = 0; i < linearLayoutLeft.getChildCount(); i++) {
            if (current == i) {
                Button buttonL = (Button) linearLayoutLeft.getChildAt(i);
                Button buttonR = (Button) linearLayoutRight.getChildAt(i);
                buttonL.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                buttonR.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                fromSection = i;
                toSection = i;
            }
        }
        tablesNoLeft = mHandler.getAllOrderedTables(fromSection);
        final TablesMoveMergeAdapter adapter = new TablesMoveMergeAdapter(DineIn.this, tablesNoLeft);
        gridViewLeft.setAdapter(adapter);
        gridViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int n = 0; n < adapterView.getChildCount(); n++) {
                    adapterView.getChildAt(n).setBackgroundDrawable(null);
                }
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.focused_table));
                focusedLeft = Integer.parseInt(tablesNoLeft.get(i));
            }
        });
        final ArrayList<Tables> currentListTemp = mHandler.getRequestedTables(toSection);
        List<String> orderedTables = mHandler.getAllOrderedTables(toSection);
        for (int i = 0; i < currentListTemp.size(); i++) {
            boolean exist = false;
            for (int k = 0; k < orderedTables.size(); k++) {
                if (currentListTemp.get(i).getTableNumber() == Integer.parseInt(orderedTables.get(k)))
                    exist = true;
            }
            if (!exist)
                tablesNoRight.add("" + currentListTemp.get(i).getTableNumber());
        }
        final TablesMoveMergeAdapter adapter2 = new TablesMoveMergeAdapter(DineIn.this, tablesNoRight);
        gridViewRight.setAdapter(adapter2);
        gridViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (int n = 0; n < adapterView.getChildCount(); n++) {
                    adapterView.getChildAt(n).setBackgroundDrawable(null);
                }
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.focused_table));
                focusedRight = Integer.parseInt(tablesNoRight.get(i));
            }
        });

        OnClickListener onClickListenerLeft = new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainL.setBackgroundColor(getResources().getColor(R.color.gray));
                firstL.setBackgroundColor(getResources().getColor(R.color.gray));
                secondL.setBackgroundColor(getResources().getColor(R.color.gray));
                thirdL.setBackgroundColor(getResources().getColor(R.color.gray));
                fourthL.setBackgroundColor(getResources().getColor(R.color.gray));
                fifthL.setBackgroundColor(getResources().getColor(R.color.gray));
                view.setBackgroundColor(getResources().getColor(R.color.dark_blue));

                focusedLeft = -1;
                switch (view.getId()) {
                    case R.id.b_main1:
                        fromSection = 0;
                        break;
                    case R.id.b_f1:
                        fromSection = 1;
                        break;
                    case R.id.b_s1:
                        fromSection = 2;
                        break;
                    case R.id.b_t1:
                        fromSection = 3;
                        break;
                    case R.id.b_fo1:
                        fromSection = 4;
                        break;
                    case R.id.b_fi1:
                        fromSection = 5;
                        break;
                }
                tablesNoLeft.clear();
                tablesNoLeft.addAll(mHandler.getAllOrderedTables(fromSection));
                adapter.notifyDataSetChanged();
            }
        };

        mainL.setOnClickListener(onClickListenerLeft);
        firstL.setOnClickListener(onClickListenerLeft);
        secondL.setOnClickListener(onClickListenerLeft);
        thirdL.setOnClickListener(onClickListenerLeft);
        fourthL.setOnClickListener(onClickListenerLeft);
        fifthL.setOnClickListener(onClickListenerLeft);

        OnClickListener onClickListenerRight = new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainR.setBackgroundColor(getResources().getColor(R.color.gray));
                firstR.setBackgroundColor(getResources().getColor(R.color.gray));
                secondR.setBackgroundColor(getResources().getColor(R.color.gray));
                thirdR.setBackgroundColor(getResources().getColor(R.color.gray));
                fourthR.setBackgroundColor(getResources().getColor(R.color.gray));
                fifthR.setBackgroundColor(getResources().getColor(R.color.gray));
                view.setBackgroundColor(getResources().getColor(R.color.dark_blue));

                focusedRight = -1;
                currentListTemp.clear();

                switch (view.getId()) {
                    case R.id.b_main2:
                        toSection = 0;
                        break;
                    case R.id.b_f2:
                        toSection = 1;
                        break;
                    case R.id.b_s2:
                        toSection = 2;
                        break;
                    case R.id.b_t2:
                        toSection = 3;
                        break;
                    case R.id.b_fo2:
                        toSection = 4;
                        break;
                    case R.id.b_fi2:
                        toSection = 5;
                        break;
                }
                currentListTemp.addAll(mHandler.getRequestedTables(toSection));
                List<String> orderedTables = new ArrayList<>(mHandler.getAllOrderedTables(toSection));

                tablesNoRight.clear();
                for (int i = 0; i < currentListTemp.size(); i++) {
                    boolean exist = false;
                    for (int k = 0; k < orderedTables.size(); k++) {
                        if (currentListTemp.get(i).getTableNumber() == Integer.parseInt(orderedTables.get(k)))
                            exist = true;
                    }
                    if (!exist)
                        tablesNoRight.add("" + currentListTemp.get(i).getTableNumber());
                }
                adapter2.notifyDataSetChanged();
            }
        };

        mainR.setOnClickListener(onClickListenerRight);
        firstR.setOnClickListener(onClickListenerRight);
        secondR.setOnClickListener(onClickListenerRight);
        thirdR.setOnClickListener(onClickListenerRight);
        fourthR.setOnClickListener(onClickListenerRight);
        fifthR.setOnClickListener(onClickListenerRight);

        move.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("here", "*****" + focusedLeft + "" + focusedRight + "" + fromSection + "" + toSection);
                if (focusedLeft != -1 && focusedRight != -1) {
                    if (focusedLeft == focusedRight && fromSection == toSection) {
                        Toast.makeText(DineIn.this, "Ops, it's the same table !", Toast.LENGTH_SHORT).show();
                    } else {
                        // update on tables temp
                        mHandler.moveTablesTemp(fromSection, focusedLeft, toSection, focusedRight);
                        dialog.dismiss();

                        final Dialog dialog2 = new Dialog(DineIn.this);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.setCancelable(false);
                        dialog2.setContentView(R.layout.move_table_dialog_smal);
                        dialog2.setCanceledOnTouchOutside(true);

                        final ImageView movingTable = dialog2.findViewById(R.id.imageViewt);

                        slideRight(movingTable);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog2.dismiss();
                                DineIn.this.recreate();
                            }
                        }, 2000);

                        dialog2.show();
                    }
                } else
                    Toast.makeText(DineIn.this, "Please choose tables from both two lists", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void openMergeDialog() {
        dialog = new Dialog(DineIn.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.merge_table_dialog);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout linearLayoutLeft = dialog.findViewById(R.id.linearButtonsLeft);
        LinearLayout linearLayoutRight = dialog.findViewById(R.id.linearButtonsRight);
        GridView gridViewLeft = dialog.findViewById(R.id.GridViewLeft);
        final GridView gridViewRight = dialog.findViewById(R.id.GridViewRight);
        final Button mainL = dialog.findViewById(R.id.b_main1);
        final Button firstL = dialog.findViewById(R.id.b_f1);
        final Button secondL = dialog.findViewById(R.id.b_s1);
        final Button thirdL = dialog.findViewById(R.id.b_t1);
        final Button fourthL = dialog.findViewById(R.id.b_fo1);
        final Button fifthL = dialog.findViewById(R.id.b_fi1);
        final Button mainR = dialog.findViewById(R.id.b_main2);
        final Button firstR = dialog.findViewById(R.id.b_f2);
        final Button secondR = dialog.findViewById(R.id.b_s2);
        final Button thirdR = dialog.findViewById(R.id.b_t2);
        final Button fourthR = dialog.findViewById(R.id.b_fo2);
        final Button fifthR = dialog.findViewById(R.id.b_fi2);

        Button merge = dialog.findViewById(R.id.merge);

        for (int i = 0; i < linearLayoutLeft.getChildCount(); i++) {
            if (current == i) {
                Button buttonL = (Button) linearLayoutLeft.getChildAt(i);
                Button buttonR = (Button) linearLayoutRight.getChildAt(i);
                buttonL.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                buttonR.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                fromSection = i;
                toSection = i;
            }
        }
        tablesNoLeft = mHandler.getAllOrderedTables(fromSection);
        tablesNoRight = mHandler.getAllOrderedTables(fromSection);
        final TablesMoveMergeAdapter adapter = new TablesMoveMergeAdapter(DineIn.this, tablesNoLeft);
        gridViewLeft.setAdapter(adapter);
        gridViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int n = 0; n < adapterView.getChildCount(); n++) {
                    adapterView.getChildAt(n).setBackgroundDrawable(null);
                }
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.focused_table));
                focusedLeft = Integer.parseInt(tablesNoLeft.get(i));
            }
        });

        final TablesMoveMergeAdapter adapter2 = new TablesMoveMergeAdapter(DineIn.this, tablesNoRight);
        gridViewRight.setAdapter(adapter2);
        gridViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (int n = 0; n < adapterView.getChildCount(); n++) {
                    adapterView.getChildAt(n).setBackgroundDrawable(null);
                }
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.focused_table));
                focusedRight = Integer.parseInt(tablesNoRight.get(i));
            }
        });

        OnClickListener onClickListenerLeft = new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainL.setBackgroundColor(getResources().getColor(R.color.gray));
                firstL.setBackgroundColor(getResources().getColor(R.color.gray));
                secondL.setBackgroundColor(getResources().getColor(R.color.gray));
                thirdL.setBackgroundColor(getResources().getColor(R.color.gray));
                fourthL.setBackgroundColor(getResources().getColor(R.color.gray));
                fifthL.setBackgroundColor(getResources().getColor(R.color.gray));
                view.setBackgroundColor(getResources().getColor(R.color.dark_blue));

                focusedLeft = -1;
                switch (view.getId()) {
                    case R.id.b_main1:
                        fromSection = 0;
                        break;
                    case R.id.b_f1:
                        fromSection = 1;
                        break;
                    case R.id.b_s1:
                        fromSection = 2;
                        break;
                    case R.id.b_t1:
                        fromSection = 3;
                        break;
                    case R.id.b_fo1:
                        fromSection = 4;
                        break;
                    case R.id.b_fi1:
                        fromSection = 5;
                        break;
                }
                tablesNoLeft.clear();
                tablesNoLeft.addAll(mHandler.getAllOrderedTables(fromSection));
                adapter.notifyDataSetChanged();
            }
        };

        mainL.setOnClickListener(onClickListenerLeft);
        firstL.setOnClickListener(onClickListenerLeft);
        secondL.setOnClickListener(onClickListenerLeft);
        thirdL.setOnClickListener(onClickListenerLeft);
        fourthL.setOnClickListener(onClickListenerLeft);
        fifthL.setOnClickListener(onClickListenerLeft);

        OnClickListener onClickListenerRight = new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainR.setBackgroundColor(getResources().getColor(R.color.gray));
                firstR.setBackgroundColor(getResources().getColor(R.color.gray));
                secondR.setBackgroundColor(getResources().getColor(R.color.gray));
                thirdR.setBackgroundColor(getResources().getColor(R.color.gray));
                fourthR.setBackgroundColor(getResources().getColor(R.color.gray));
                fifthR.setBackgroundColor(getResources().getColor(R.color.gray));
                view.setBackgroundColor(getResources().getColor(R.color.dark_blue));

                focusedRight = -1;
                switch (view.getId()) {
                    case R.id.b_main2:
                        toSection = 0;
                        break;
                    case R.id.b_f2:
                        toSection = 1;
                        break;
                    case R.id.b_s2:
                        toSection = 2;
                        break;
                    case R.id.b_t2:
                        toSection = 3;
                        break;
                    case R.id.b_fo2:
                        toSection = 4;
                        break;
                    case R.id.b_fi2:
                        toSection = 5;
                        break;
                }
                tablesNoRight.clear();
                tablesNoRight.addAll(mHandler.getAllOrderedTables(toSection));
                adapter2.notifyDataSetChanged();
            }
        };

        mainR.setOnClickListener(onClickListenerRight);
        firstR.setOnClickListener(onClickListenerRight);
        secondR.setOnClickListener(onClickListenerRight);
        thirdR.setOnClickListener(onClickListenerRight);
        fourthR.setOnClickListener(onClickListenerRight);
        fifthR.setOnClickListener(onClickListenerRight);

        merge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("here", "*****" + focusedLeft + "" + focusedRight + "" + fromSection + "" + toSection);
                if (focusedLeft != -1 && focusedRight != -1) {
                    if (focusedLeft == focusedRight && fromSection == toSection) {
                        Toast.makeText(DineIn.this, "Ops, it's the same table !", Toast.LENGTH_SHORT).show();
                    } else {
                        // update on tables temp
                        mHandler.mergeTablesTemp(fromSection, focusedLeft, toSection, focusedRight);
                        dialog.dismiss();

                        final Dialog dialog2 = new Dialog(DineIn.this);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.setCancelable(false);
                        dialog2.setContentView(R.layout.merge_table_dialog_smal);
                        dialog2.setCanceledOnTouchOutside(true);

                        final ImageView movingTable = dialog2.findViewById(R.id.imageViewt);
                        final ImageView movingTable2 = dialog2.findViewById(R.id.imageViewt2);

                        slideRight(movingTable);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog2.dismiss();
                                DineIn.this.recreate();
                            }
                        }, 2000);

                        dialog2.show();
                    }
                } else
                    Toast.makeText(DineIn.this, "Please choose tables from both two lists", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void openCheckOutDialog() {
        dialog = new Dialog(DineIn.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.check_out_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final TextView sectionNo = dialog.findViewById(R.id.section);
        final Spinner tableNo = dialog.findViewById(R.id.tableNo);
        Button done = dialog.findViewById(R.id.b_done);

        switch (current) {
            case 0:
                sectionNo.setText("Main Floor");
                break;
            case 1:
                sectionNo.setText("1st Floor");
                break;
            case 2:
                sectionNo.setText("2nd Floor");
                break;
            case 3:
                sectionNo.setText("3rd Floor");
                break;
            case 4:
                sectionNo.setText("4th Floor");
                break;
            case 5:
                sectionNo.setText("5th Floor");
                break;
        }

        List<String> tablesNo = mHandler.getAllOrderedTables(current);
        ArrayAdapter<String> tableNoAdapter = new ArrayAdapter<>(DineIn.this, R.layout.spinner_style, tablesNo);
        tableNo.setAdapter(tableNoAdapter);

        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                List<OrderTransactions> orderTransTemp = mHandler.getOrderTransactionsTemp("" + current, tableNo.getSelectedItem().toString());
                if (orderTransTemp.size() != 0) {
                    Intent intent = new Intent(DineIn.this, PayMethods.class);
                    intent.putExtra("sectionNo", "" + current);
                    intent.putExtra("tableNo", tableNo.getSelectedItem().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(DineIn.this, "This table has no order !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void setTableBackground(LinearLayout linearLayout, LinearLayout linearLayout1) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            LinearLayout linear = (LinearLayout) linearLayout.getChildAt(i);
            linear.setBackgroundColor(getResources().getColor(R.color.layer3));
        }
        linearLayout1.setBackgroundColor(getResources().getColor(R.color.layer4));
    }

    public void freeze() {
        currentList.clear();
        for (int i = 0; i < land.getChildCount(); i++) {

            LinearLayout linearLayout = (LinearLayout) land.getChildAt(i);
            ImageView imageView1 = (ImageView) linearLayout.getChildAt(0);
            TextView textView1 = (TextView) linearLayout.getChildAt(1);

            int width = linearLayout.getHeight();
            int height = linearLayout.getWidth();
            float left = linearLayout.getX();
            float top = linearLayout.getY();
            int resource = (int) imageView1.getTag();
            int tableNumber = Integer.parseInt(textView1.getText().toString());

            Tables table = new Tables(height, width, resource, left, top, tableNumber);
            currentList.add(table);
        }
        setList();
    }

    public void removeTables() {
        land.removeAllViews();
    }

    public void setList() {

        switch (current) {
            case 0:
                list0 = currentList;
                break;
            case 1:
                list1 = currentList;
                break;
            case 2:
                list2 = currentList;
                break;
            case 3:
                list3 = currentList;
                break;
            case 4:
                list4 = currentList;
                break;
            case 5:
                list5 = currentList;
                break;
        }
    }

    public void unfocusFloors() {
        mainF.setBackgroundColor(getResources().getColor(R.color.gray));
        firstF.setBackgroundColor(getResources().getColor(R.color.gray));
        secondF.setBackgroundColor(getResources().getColor(R.color.gray));
        thirdF.setBackgroundColor(getResources().getColor(R.color.gray));
        fourthF.setBackgroundColor(getResources().getColor(R.color.gray));
        fifthF.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    public void fillMainFloor() {
        current = 0;
        currentList = list0;
        mainF.setBackgroundColor(getResources().getColor(R.color.dark_blue));

        for (int i = 0; i < currentList.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(DineIn.this);
            ImageView imageView = new ImageView(DineIn.this);
            TextView textView = new TextView(DineIn.this);

            imageView.setBackgroundResource(currentList.get(i).getImageResource());
            imageView.setTag(currentList.get(i).getImageResource());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(currentList.get(i).getHeight() - 20, currentList.get(i).getWidth());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(currentList.get(i).getHeight(), currentList.get(i).getWidth());

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(50, 15);
            textView.setLayoutParams(params2);
            textView.setText("" + i);
            textView.setTextColor(getResources().getColor(R.color.text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

            linearLayout.setOnClickListener(onTableClickListener);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            imageView.setLayoutParams(param);
            linearLayout.setX(currentList.get(i).getMarginLeft());
            linearLayout.setY(currentList.get(i).getMarginTop());
            linearLayout.setLayoutParams(params);
            if (checkGreen(textView.getText().toString(), current)) {
                linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_light));
            }
            land.addView(linearLayout);

        }
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    public void slideRight(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                view.getWidth() + 300,                 // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(1500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        greenTables = mHandler.getAllOrderTransactionsTemp();
        if (focused != null)
            focused.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_light));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DineIn.this, Main.class);
        startActivity(intent);
    }

    void initialize() {
        save = (Button) findViewById(R.id.save);
        mainF = (Button) findViewById(R.id.main);
        firstF = (Button) findViewById(R.id.first_f);
        secondF = (Button) findViewById(R.id.second_f);
        thirdF = (Button) findViewById(R.id.third_f);
        fourthF = (Button) findViewById(R.id.fourth_f);
        fifthF = (Button) findViewById(R.id.fifth_f);
        addIcon = (Button) findViewById(R.id.add_icon);
        add = (LinearLayout) findViewById(R.id.add_table_list);
        rightBorder = (LinearLayout) findViewById(R.id.rightBorder);
        land = (ViewGroup) findViewById(R.id.land);

        move = (TextView) findViewById(R.id.move);
        merge = (TextView) findViewById(R.id.merge);
        reservation = (TextView) findViewById(R.id.reservation);
        takeAway = (TextView) findViewById(R.id.takeAway);
        close = (TextView) findViewById(R.id.close);
        cashDrawer = (TextView) findViewById(R.id.cashDrawer);
        refund = (TextView) findViewById(R.id.refund);
        checkOut = (TextView) findViewById(R.id.checkOut);
        reprint = (TextView) findViewById(R.id.reprint);

        addIcon.setVisibility(View.INVISIBLE);
        add.setVisibility(View.INVISIBLE);

        mainF.setOnClickListener(onFloorClickListener);
        firstF.setOnClickListener(onFloorClickListener);
        secondF.setOnClickListener(onFloorClickListener);
        thirdF.setOnClickListener(onFloorClickListener);
        fourthF.setOnClickListener(onFloorClickListener);
        fifthF.setOnClickListener(onFloorClickListener);

        move.setOnClickListener(onRightListListener);
        merge.setOnClickListener(onRightListListener);
        reservation.setOnClickListener(onRightListListener);
        takeAway.setOnClickListener(onRightListListener);
        close.setOnClickListener(onRightListListener);
        cashDrawer.setOnClickListener(onRightListListener);
        refund.setOnClickListener(onRightListListener);
        checkOut.setOnClickListener(onRightListListener);
        reprint.setOnClickListener(onRightListListener);
    }
}
