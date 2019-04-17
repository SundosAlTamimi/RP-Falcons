package com.tamimi.sundos.restpos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.BackOffice.BackOfficeActivity;
import com.tamimi.sundos.restpos.BackOffice.MenuRegistration;
import com.tamimi.sundos.restpos.BackOffice.OrderLayout;
import com.tamimi.sundos.restpos.Models.CancleOrder;
import com.tamimi.sundos.restpos.Models.FamilyCategory;
import com.tamimi.sundos.restpos.Models.ForceQuestions;
import com.tamimi.sundos.restpos.Models.ItemWithFq;
import com.tamimi.sundos.restpos.Models.ItemWithModifier;
import com.tamimi.sundos.restpos.Models.Items;
import com.tamimi.sundos.restpos.Models.Modifier;
import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.PayMethod;
import com.tamimi.sundos.restpos.Models.UsedCategories;
import com.tamimi.sundos.restpos.Models.UsedItems;
import com.tamimi.sundos.restpos.Models.VoidResons;

import org.askerov.dynamicgrid.DynamicGridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Order extends AppCompatActivity {

    Button modifier, void_, delivery, discount, lDiscount, split, priceChange;
    TextView total, lineDisCount, disCount, deliveryCharge, subTotal, service, tax, amountDue, vhSerial;
    Button pay, order;
    TextView orderType, tableNo, check, date, user, seats;
    TableLayout tableLayout;
    GridView catGridView, itemGridView;
    CheckBox discPerc;
    Button back;

    private ProgressDialog progressDialog;
    String json_getString;
    List<UsedCategories> categories;

    int orderTypeFlag;
    int currentColor;
    FoodAdapter1 foodAdapter;
    int tableLayoutPosition;
    Double lineDiscountValue = 0.0;
    Double discountValue = 0.0;
    static double balance;
    double totalItemsWithDiscount = 0.0;
    double voucherDiscount;
    boolean discChanged = false;

    static ArrayList<OrderTransactions> OrderTransactionsObj;
    static OrderHeader OrderHeaderObj;

    int voucherSerial;
    public static String OrderType, today, time, yearMonth, voucherNo;

    View v = null;
    String waiter = "No Waiter";
    String waiterNo = "-1";
    int tableNumber, sectionNumber, seatNo;
    ArrayList<Items> wantedItems;
    List<Items> items = new ArrayList<>();
    ArrayList<Double> lineDiscount;
    ArrayList<Items> requestedItems;

    TableRow focused = null;
    int selectedModifier = -1;

    Dialog dialog;
    private DatabaseHandler mDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order);

        initialize();

        mDbHandler = new DatabaseHandler(Order.this);
        OrderTransactionsObj = new ArrayList<>();
        items = mDbHandler.getAllItems();
        wantedItems = new ArrayList<>();
        lineDiscount = new ArrayList<Double>();
        categories = mDbHandler.getUsedCategories();

        fillCategories();
        showCats();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orderTypeFlag = Integer.parseInt(extras.getString("flag"));
            tableNumber = extras.getInt("tableNo");
            sectionNumber = extras.getInt("sectionNo");
            waiter = extras.getString("waiter");
            waiterNo = extras.getString("waiterNo");
            seatNo = extras.getInt("seatNo");

        }

        setDateAndVoucherNumber();
        setOrder(orderTypeFlag);


        tableLayoutPosition = 0;
        currentColor = ContextCompat.getColor(this, R.color.layer2);

        OrderType = convertToEnglish(orderType.getText().toString());

        if (mDbHandler.getOrderTransactionsTemp("" + sectionNumber, "" + tableNumber).size() != 0)
            fillPreviousOrder();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Date currentTimeAndDate = Calendar.getInstance().getTime();
            SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
            time = convertToEnglish(dfTime.format(currentTimeAndDate));

            switch (view.getId()) {
                case R.id.pay:
                    if (orderTypeFlag == 0) {
                        if (!(Double.parseDouble(amountDue.getText().toString()) == 0)) {
                            saveInOrderTransactionObj();
                            saveInOrderHeaderObj();
                            sendToKitchen();

                            Intent intentPay = new Intent(Order.this, PayMethods.class);
                            startActivity(intentPay);
                        } else
                            Toast.makeText(Order.this, getResources().getString(R.string.amountdue_oo), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.order:
                    if (orderTypeFlag == 1) {
                        if (!(Double.parseDouble(amountDue.getText().toString()) == 0)) {
                            saveInOrderTransactionTemp();
                            saveInOrderHeaderTemp();

                            Intent intent = new Intent(Order.this, DineIn.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(Order.this, getResources().getString(R.string.amountdue_oo), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.modifier:
                    showModifierDialog();
                    break;

                case R.id.void_:
                    deleteRaw(focused);
                    break;

                case R.id.delivery_b:
                    showDeliveryChangeDialog();
                    break;

                case R.id.discount_b:
                    showDiscountDialog();
                    break;

                case R.id.line_discount_b:
                    showLineDiscountDialog();
                    break;

                case R.id.back:
                    showCats();
                    break;
            }
        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.pay:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        pay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        pay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight));
                    }
                    break;

                case R.id.order:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        order.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.exit));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        order.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.exit_hover));
                    }
                    break;

                case R.id.modifier:
                case R.id.void_:
                    break;

                case R.id.delivery_b:
                    break;

                case R.id.split:
                case R.id.discount:
                    break;

                case R.id.line_discount_b:
                    break;

                case R.id.price_change:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                    }
                    break;

            }
            return false;
        }
    };

    void showCats() {
        catGridView.setVisibility(View.VISIBLE);
        itemGridView.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
    }

    void showItems() {
        catGridView.setVisibility(View.INVISIBLE);
        itemGridView.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    void setOrder(int flag) {
        if (flag == 0) {
            orderType.setText(getResources().getString(R.string.take_away));
            tableNo.setText(getResources().getString(R.string.table_no) + " : -");
            check.setText(getResources().getString(R.string.check) + "-");
            user.setText("no waiter");
            seats.setText("0");
            tableNumber = -1;
            sectionNumber = -1;
            vhSerial.setText(voucherNo);
        } else {
            orderType.setText(getResources().getString(R.string.dine_in));
            tableNo.setText(getResources().getString(R.string.table_no) + " :  " + tableNumber);
            check.setText(getResources().getString(R.string.check) + sectionNumber);
            user.setText(waiter);
            seats.setText("" + seatNo);
            vhSerial.setText(voucherNo);
        }
    }

    void setDateAndVoucherNumber() {
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today = convertToEnglish(df.format(currentTimeAndDate));

        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");
        time = convertToEnglish(dfTime.format(currentTimeAndDate));

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMM");
        yearMonth = convertToEnglish(df2.format(currentTimeAndDate));

        List<OrderHeader> transactions = mDbHandler.getAllOrderHeader();
        List<OrderHeader> transactionsTemp = mDbHandler.getAllOrderHeaderTemp();

        int transactionsSize = 0, transactionsTempSize = 0;

        if (transactions.size() != 0)
            transactionsSize = transactions.get(transactions.size() - 1).getVoucherSerial();

        if (transactionsTemp.size() != 0)
            transactionsTempSize = transactionsTemp.get(transactionsTemp.size() - 1).getVoucherSerial();

        if (transactionsSize > transactionsTempSize) {
            voucherSerial = transactionsSize + 1;
        } else {
            voucherSerial = transactionsTempSize + 1;
        }
        date.setText(today);
        voucherNo = yearMonth + "-" + voucherSerial;
    }

    void fillCategories() {

        List<FamilyCategory> allCats = mDbHandler.getAllFamilyCategory();
        for (int i = 0; i < allCats.size(); i++)
            if (allCats.get(i).getType() != 2) {
                allCats.remove(i);
                i--;
            }
        for (int i = 0; i < categories.size(); i++) {
            for (int k = 0; k < allCats.size(); k++) {
                if (categories.get(i).getCategoryName().equals(allCats.get(k).getName())) {
                    categories.get(i).setCatPic(allCats.get(k).getCatPic());
                }
            }
        }

        LayoutCategoryAdapter adapter = new LayoutCategoryAdapter(Order.this, categories, 3);
        catGridView.setAdapter(adapter);

        catGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout linearLayout = (LinearLayout) view;
                LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);
                TextView textView = (TextView) innerLinearLayout.getChildAt(1);
                if (!textView.getText().toString().equals("")) {
                    fillItems(textView.getText().toString());
                    showItems();
                }
            }
        });
    }

    void fillItems(String categoryName) {

        if (!categoryName.equals("")) {
            ArrayList<UsedItems> subList = mDbHandler.getRequestedItems(categoryName);

            if (subList.size() != 0) {
                List<Items> items = mDbHandler.getAllItems();
                requestedItems = new ArrayList<>();

                for (int i = 0; i < subList.size(); i++) {
                    if (Character.isDigit(subList.get(i).getitemName().charAt(0)) || subList.get(i).getitemName().equals("")) { // no data in this position
                        requestedItems.add(new Items("", "", "", 0, 0, "", "",
                                0, 0, 0, "", 0, 0, 0, 0,
                                "", "", 0, 0, 0, null, subList.get(i).getBackground(),
                                subList.get(i).getBackground(), 0));
                    } else {
                        for (int j = 0; j < items.size(); j++) {
                            if (subList.get(i).getitemName().equals(items.get(j).getMenuName()))
                                requestedItems.add(new Items(categoryName, items.get(j).getMenuName(), items.get(j).getFamilyName(),
                                        items.get(j).getTax(), items.get(j).getTaxType(), items.get(j).getSecondaryName(), items.get(j).getKitchenAlias(),
                                        items.get(j).getItemBarcode(), items.get(j).getStatus(), items.get(j).getItemType(), items.get(j).getInventoryUnit(),
                                        items.get(j).getWastagePercent(), items.get(j).getDiscountAvailable(), items.get(j).getPointAvailable(),
                                        items.get(j).getOpenPrice(), items.get(j).getKitchenPrinter(), items.get(j).getDescription(), items.get(j).getPrice(),
                                        items.get(j).getUsed(), items.get(j).getShowInMenu(), items.get(j).getPic(), subList.get(i).getBackground(),
                                        subList.get(i).getTextColor(), subList.get(i).getPosition()));
                        }
                    }
                }
                foodAdapter = new FoodAdapter1(Order.this, requestedItems);
                itemGridView.setAdapter(foodAdapter);

                itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!requestedItems.get(i).getMenuName().equals("")) {
                            boolean exist = false;
                            int index = 0;
                            for (int k = 0; k < tableLayout.getChildCount(); k++) {
                                TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
                                TextView textViewName = (TextView) tableRow.getChildAt(1);
                                if (textViewName.getText().toString().equals(requestedItems.get(i).getMenuName())) {
                                    exist = true;
                                    index = k;
                                    break;
                                }
                            }

                            if (!exist) {
                                ArrayList<ItemWithFq> questions = mDbHandler.getItemWithFqs(requestedItems.get(i).itemBarcode);
                                if (questions.size() == 0) {
                                    wantedItems.add(requestedItems.get(i));
                                    lineDiscount.add(0.0);
                                    insertItemRaw(requestedItems.get(i));
                                } else {
                                    wantedItems.add(requestedItems.get(i));
                                    lineDiscount.add(0.0);
                                    insertItemRaw(requestedItems.get(i));
                                    showForceQuestionDialog(requestedItems.get(i).itemBarcode, 0);
                                }
                            } else {
                                TableRow tableRow = (TableRow) tableLayout.getChildAt(index);
                                TextView textViewQty = (TextView) tableRow.getChildAt(0);
                                TextView textViewPrice = (TextView) tableRow.getChildAt(2);
                                TextView textViewTotal = (TextView) tableRow.getChildAt(3);
                                TextView textViewLineDiscount = (TextView) tableRow.getChildAt(4);

                                int qty = Integer.parseInt(convertToEnglish(textViewQty.getText().toString()));
                                double price = Double.parseDouble(convertToEnglish(textViewPrice.getText().toString()));
                                double newTotal = price * (qty + 1);

                                double originalDisc = lineDiscount.get(index) * 100 / Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
                                double newDiscountValue = originalDisc * newTotal / 100;
                                lineDiscount.set(index, newDiscountValue);

                                textViewQty.setText("" + (qty + 1));
                                textViewTotal.setText("" + newTotal);
                                textViewLineDiscount.setText("" + newDiscountValue);
                                calculateTotal();
                            }
                        } else
                            Toast.makeText(Order.this, getResources().getString(R.string.no_item), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else
            itemGridView.setAdapter(null);
    }

    void insertItemRaw(Items item) {
        final TableRow row = new TableRow(Order.this);

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
        lp.setMargins(2, 0, 2, 0);
        row.setLayoutParams(lp);

        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(Order.this);

            switch (i) {
                case 0:
                    textView.setText("1");
                    break;
                case 1:
                    textView.setText(item.getMenuName());
                    break;
                case 2:
                    textView.setText("" + item.getPrice());
                    break;
                case 3:
                    textView.setText("" + item.getPrice());
                    break;
                case 4:
                    textView.setText("0"); // line discount
                    break;
            }

            textView.setTextColor(ContextCompat.getColor(Order.this, R.color.text_color));
            textView.setGravity(Gravity.CENTER);

            if (i != 4) {
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                textView.setLayoutParams(lp1);
            } else {
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.00001f);
                textView.setLayoutParams(lp2);
            }

            row.addView(textView);
            row.setTag(tableLayoutPosition);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    focused = row;
                    setRawFocused(row);
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    focused = row;
                    setRawFocused(row);
                    return true;
                }
            });
        }
        tableLayout.addView(row);
        tableLayoutPosition++;

        calculateTotal();
    }

    void insertModifierRaw(String modifierText) {
        final TableRow row = new TableRow(Order.this);

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
        lp.setMargins(2, 0, 2, 0);
        row.setLayoutParams(lp);

        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(Order.this);

            switch (i) {
                case 0:
                    textView.setText("0");
                    break;
                case 1:
                    textView.setText(modifierText);
                    break;
                case 2:
                    textView.setText("0");
                    break;
                case 3:
                    textView.setText("0");
                    break;
                case 4:
                    textView.setText("0"); // line discount
                    break;
            }

            textView.setTextColor(ContextCompat.getColor(Order.this, R.color.exit));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

            if (i != 4) {
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                textView.setLayoutParams(lp1);
            } else {
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.00001f);
                textView.setLayoutParams(lp2);
            }

            row.addView(textView);
            row.setTag(tableLayoutPosition);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    focused = row;
                    setRawFocused(row);
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    focused = row;
                    setRawFocused(row);
                    return true;
                }
            });
        }
        tableLayout.addView(row, Integer.parseInt(focused.getTag().toString()) + 1);
        tableLayoutPosition++;
        resetPosition();

    }

    void insertForceQuestionRaw(String forceQuestionText) {
        final TableRow row = new TableRow(Order.this);

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
        lp.setMargins(2, 0, 2, 0);
        row.setLayoutParams(lp);

        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(Order.this);

            switch (i) {
                case 0:
                    textView.setText("0");
                    break;
                case 1:
                    textView.setText(forceQuestionText);
                    break;
                case 2:
                    textView.setText("0");
                    break;
                case 3:
                    textView.setText("0");
                    break;
                case 4:
                    textView.setText("0"); // line discount
                    break;
            }

            textView.setTextColor(ContextCompat.getColor(Order.this, R.color.exit));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

            if (i != 4) {
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                textView.setLayoutParams(lp1);
            } else {
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.00001f);
                textView.setLayoutParams(lp2);
            }

            row.addView(textView);
            row.setTag(tableLayoutPosition);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    focused = row;
                    setRawFocused(row);
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    focused = row;
                    setRawFocused(row);
                    return true;
                }
            });
        }
        tableLayout.addView(row);
        tableLayoutPosition++;
        resetPosition();

    }

    void setRawFocused(TableRow raw) {
        for (int k = 0; k < tableLayout.getChildCount(); k++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
            tableRow.setBackgroundDrawable(null);
        }
        raw.setBackgroundColor(getResources().getColor(R.color.layer4));
    }

    void deleteRaw(final TableRow row) {
        if (wantedItems.size() != 0) {
            if (focused != null) {
                row.setBackgroundColor(getResources().getColor(R.color.layer4));

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(Order.this);
                builderSingle.setCancelable(false);
                builderSingle.setTitle(getResources().getString(R.string.what_kind));

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Order.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add(getResources().getString(R.string.select_item));
                arrayAdapter.add(getResources().getString(R.string.curent_order));

                builderSingle.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (strName.equals(getResources().getString(R.string.select_item))) {
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(Order.this);
                            builderInner.setTitle(getResources().getString(R.string.delete_this_item));
                            builderInner.setCancelable(false);
                            builderInner.setPositiveButton(getResources().getString(R.string.yes), (dialog1, which1) -> {

                                showVoidReasonDialog(row);
                            });
                            builderInner.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> {
                                row.setBackgroundDrawable(null);
                            });
                            builderInner.show();

                        } else {
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(Order.this);
                            builderInner.setTitle(getResources().getString(R.string.delete_curent_order));
                            builderInner.setCancelable(false);
                            builderInner.setPositiveButton(getResources().getString(R.string.yes), (dialog1, which1) -> {

                                showVoidReasonDialog2();
                            });
                            builderInner.setNegativeButton(getResources().getString(R.string.no), (dialog1, i) -> {
//                            row.setBackgroundDrawable(null);
                                dialog1.dismiss();
                            });
                            builderInner.show();
                        }
                    }
                });
                builderSingle.show();

//            AlertDialog.Builder builder = new AlertDialog.Builder(Order.this);
//            builder.setTitle("Do you want to delete this recipe ?");
//            builder.setCancelable(false);
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    tableLayout.removeView(row);
//                    wantedItems.remove(Integer.parseInt(row.getTag().toString()));
//                    lineDiscount.remove(Integer.parseInt(row.getTag().toString()));
//                    tableLayoutPosition--;
//                    resetPosition();
//                    calculateTotal();
//                    showVoidReasonDialog(row);
//                }
//            });
//
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    row.setBackgroundDrawable(null);
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
            } else
                Toast.makeText(Order.this, getResources().getString(R.string.chooes_item_delete), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Order.this, getResources().getString(R.string.no_item), Toast.LENGTH_SHORT).show();
        }
    }

    void showVoidReasonDialog(TableRow raw) {

        TextView textViewQty = (TextView) raw.getChildAt(0);
        TextView textViewPrice = (TextView) raw.getChildAt(2);
        TextView textViewTotal = (TextView) raw.getChildAt(3);

        dialog = new Dialog(Order.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.void_qty_dialog);
        dialog.setCanceledOnTouchOutside(false);

        EditText voidQty = (EditText) dialog.findViewById(R.id.void_qty);
        Button done = (Button) dialog.findViewById(R.id.b_done);

        voidQty.setText(convertToEnglish(textViewQty.getText().toString()));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!convertToEnglish(voidQty.getText().toString()).equals("")) {
                    if (Integer.parseInt(convertToEnglish(voidQty.getText().toString())) <= Integer.parseInt(convertToEnglish(textViewQty.getText().toString()))) {
//
                        Dialog dialog1 = new Dialog(Order.this);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setCancelable(false);
                        dialog1.setContentView(R.layout.void_reason_dialog);
                        dialog1.setCanceledOnTouchOutside(false);

                        TableLayout reasons = (TableLayout) dialog1.findViewById(R.id.tableOfReasons);
                        Button save = (Button) dialog1.findViewById(R.id.done);
                        EditText newReason = (EditText) dialog1.findViewById(R.id.newReason);

                        ArrayList<VoidResons> resons = mDbHandler.getAllVoidReasons();

                        reasons.removeAllViews();
                        RadioGroup radioGroup = new RadioGroup(Order.this);
                        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                        lp1.setMargins(0, 2, 2, 6);
                        radioGroup.setLayoutParams(lp1);

                        final TableRow row = new TableRow(Order.this);
                        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                        lp.setMargins(0, 2, 2, 6);
                        row.setLayoutParams(lp);

                        final String[] selectedReason = {""};
                        for (int k = 0; k < resons.size(); k++) {
                            if (resons.get(k).getActiveated() == 1) {
                                RadioButton radioButton = new RadioButton(Order.this);
                                radioButton.setText(resons.get(k).getVoidReason());
                                radioButton.setTextSize(20);
                                radioButton.setTextColor(ContextCompat.getColor(Order.this, R.color.text_color));
                                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        selectedReason[0] = radioButton.getText().toString();
                                    }
                                });
                                radioGroup.addView(radioButton);
                            }
                        }
                        row.addView(radioGroup);
                        reasons.addView(row);

                        save.setOnClickListener(view1 -> {

                            if (!selectedReason[0].equals("") || !newReason.getText().toString().equals("")) {
                                String reasonText;
                                if (newReason.getText().toString().equals("")) {
                                    reasonText = selectedReason[0];
                                } else {
                                    reasonText = newReason.getText().toString();
                                    mDbHandler.addVoidReason(new VoidResons(Settings.shift_number, Settings.shift_name,
                                            Settings.password, Settings.user_name, reasonText, today, 1));
                                }

                                int index = Integer.parseInt(convertToEnglish(raw.getTag().toString()));

                                mDbHandler.addCancleOrder(new CancleOrder(voucherNo, today, Settings.user_name, Settings.password, Settings.shift_name,
                                        Settings.shift_number, waiter, Integer.parseInt(waiterNo), "" + wantedItems.get(index).getItemBarcode(),
                                        wantedItems.get(index).getMenuName(), Integer.parseInt(textViewQty.getText().toString()),
                                        wantedItems.get(index).getPrice(), Double.parseDouble(textViewTotal.getText().toString()),
                                        reasonText, 0, time, Settings.POS_number));

                                if (convertToEnglish(voidQty.getText().toString()).equals(convertToEnglish(textViewQty.getText().toString()))) {

                                    tableLayout.removeView(raw);
                                    wantedItems.remove(index);
                                    lineDiscount.remove(index);
                                    tableLayoutPosition--;
                                    resetPosition();
                                    calculateTotal();

                                    for (int i = index; i < tableLayout.getChildCount(); i++) {
                                        TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                                        TextView qty = (TextView) tableRow.getChildAt(0);

                                        if (convertToEnglish(qty.getText().toString()).equals("0")) {
                                            tableLayout.removeView(tableRow);
                                            wantedItems.remove(i);
                                            lineDiscount.remove(i);
                                            tableLayoutPosition--;
                                            i--;
                                            resetPosition();
                                        } else
                                            break;
                                    }

                                    if (orderTypeFlag == 0 && wantedItems.size() == 0)
                                        deliveryCharge.setText("0.0");

                                    if (orderTypeFlag == 1 && tableLayout.getChildCount() == 0) {
                                        mDbHandler.deleteFromOrderHeaderTemp("" + sectionNumber, "" + tableNumber);
                                        mDbHandler.deleteFromOrderTransactionTemp("" + sectionNumber, "" + tableNumber);

                                        Intent intent = new Intent(Order.this, DineIn.class);
                                        startActivity(intent);
                                    }

                                } else {

                                    int newQty = Integer.parseInt(convertToEnglish(textViewQty.getText().toString())) - Integer.parseInt(convertToEnglish(voidQty.getText().toString()));
                                    double newTotal = newQty * Double.parseDouble(convertToEnglish(textViewPrice.getText().toString()));
                                    double originalDisc = lineDiscount.get(index) * 100 / Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
                                    double newDiscountValue = originalDisc * newTotal / 100;

                                    textViewQty.setText("" + newQty);
                                    textViewTotal.setText("" + newTotal);
                                    lineDiscount.set(index, newDiscountValue);
                                    calculateTotal();
                                }
                                dialog1.dismiss();
                                dialog.dismiss();
                            } else
                                Toast.makeText(Order.this, getResources().getString(R.string.select_reson_cancele), Toast.LENGTH_LONG).show();
                        });


                        dialog1.show();
                    } else
                        Toast.makeText(Order.this, getResources().getString(R.string.curent_qty_less_than) + voidQty.getText().toString(), Toast.LENGTH_LONG).show();

                } else
                    Toast.makeText(Order.this, getResources().getString(R.string.enter_qty), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    void showVoidReasonDialog2() {

        dialog = new Dialog(Order.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.void_reason_dialog);
        dialog.setCanceledOnTouchOutside(false);

        TableLayout reasons = (TableLayout) dialog.findViewById(R.id.tableOfReasons);
        Button save = (Button) dialog.findViewById(R.id.done);
        EditText newReason = (EditText) dialog.findViewById(R.id.newReason);

        ArrayList<VoidResons> resons = mDbHandler.getAllVoidReasons();

        reasons.removeAllViews();
        RadioGroup radioGroup = new RadioGroup(Order.this);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
        lp1.setMargins(0, 2, 2, 6);
        radioGroup.setLayoutParams(lp1);

        final TableRow row = new TableRow(Order.this);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp.setMargins(0, 2, 2, 6);
        row.setLayoutParams(lp);

        final String[] selectedReason = {""};
        for (int k = 0; k < resons.size(); k++) {

            if (resons.get(k).getActiveated() == 1) {
                RadioButton radioButton = new RadioButton(Order.this);
                radioButton.setText(resons.get(k).getVoidReason());
                radioButton.setTextSize(20);
                radioButton.setTextColor(ContextCompat.getColor(Order.this, R.color.text_color));
                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        selectedReason[0] = radioButton.getText().toString();
                    }
                });

                radioGroup.addView(radioButton);
            }
        }
        row.addView(radioGroup);
        reasons.addView(row);

        save.setOnClickListener(view -> {
            if (!selectedReason[0].equals("") || !convertToEnglish(newReason.getText().toString()).equals("")) {
                String reasonText;
                if (convertToEnglish(newReason.getText().toString()).equals("")) {
                    reasonText = selectedReason[0];
                } else {
                    reasonText = convertToEnglish(newReason.getText().toString());
                    mDbHandler.addVoidReason(new VoidResons(Settings.shift_number, Settings.shift_name,
                            Settings.password, Settings.user_name, reasonText, today, 1));
                }

                for (int k = 0; k < tableLayout.getChildCount(); k++) {
                    TableRow raw = (TableRow) tableLayout.getChildAt(k);
                    TextView textViewQty = (TextView) raw.getChildAt(0);
                    TextView textViewTotal = (TextView) raw.getChildAt(3);

                    mDbHandler.addCancleOrder(new CancleOrder(voucherNo, today, Settings.user_name, Settings.password, Settings.shift_name,
                            Settings.shift_number, waiter, Integer.parseInt(waiterNo), "" + wantedItems.get(k).getItemBarcode(),
                            wantedItems.get(k).getMenuName(), Integer.parseInt(convertToEnglish(textViewQty.getText().toString())),
                            wantedItems.get(k).getPrice(), Double.parseDouble(convertToEnglish(textViewTotal.getText().toString())),
                            reasonText, 1, time, Settings.POS_number));
                }
                if (orderTypeFlag == 0) {
                    tableLayout.removeAllViews();
                    wantedItems.clear();
                    lineDiscount.clear();
                    tableLayoutPosition = 0;
                    deliveryCharge.setText("0.0");

                    resetPosition();
                    calculateTotal();
                } else {
                    tableLayout.removeAllViews();
                    wantedItems.clear();
                    lineDiscount.clear();
                    tableLayoutPosition = 0;
                    resetPosition();
                    calculateTotal();
                    mDbHandler.deleteFromOrderHeaderTemp("" + sectionNumber, "" + tableNumber);
                    mDbHandler.deleteFromOrderTransactionTemp("" + sectionNumber, "" + tableNumber);

                    Intent intent = new Intent(Order.this, DineIn.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            } else {
                Toast.makeText(Order.this, getResources().getString(R.string.select_reson_cancele), Toast.LENGTH_LONG).show();

            }
        });
        dialog.show();
    }

    void resetPosition() {
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            tableRow.setTag("" + i);
        }
    }

    void showForceQuestionDialog(final int itemBarcode, final int questionNo) {
        dialog = new Dialog(Order.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.answer_force_question_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final TextView qus = dialog.findViewById(R.id.question);
        final Button extra = dialog.findViewById(R.id.extra);
        final Button no = dialog.findViewById(R.id.no);
        final Button little = dialog.findViewById(R.id.little);
        final Button half = dialog.findViewById(R.id.half);
        final Button save = dialog.findViewById(R.id.save);
        final Button exit = dialog.findViewById(R.id.exit);
        final Button previous = dialog.findViewById(R.id.previous);
        final LinearLayout answersLinear = dialog.findViewById(R.id.answer);

        ArrayList<ItemWithFq> ItemWithFqs = mDbHandler.getItemWithFqs(itemBarcode);
        qus.setText(ItemWithFqs.get(questionNo).getQuestionText());

        ArrayList<ForceQuestions> questions = mDbHandler.getRequestedForceQuestions(ItemWithFqs.get(questionNo).getQuestionNo());

        if (questions.get(0).getMultipleAnswer() == 0) {
            RadioGroup radioGroup = new RadioGroup(Order.this);
            for (int i = 0; i < questions.size(); i++) {
                final RadioButton radioButton = new RadioButton(Order.this);
                radioButton.setText(questions.get(i).getAnswer());
                radioButton.setTextColor(getResources().getColor(R.color.text_color));
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        v = radioButton;
                    }
                });
                radioGroup.addView(radioButton);
            }
            answersLinear.addView(radioGroup);
        } else
            for (int i = 0; i < questions.size(); i++) {
                final CheckBox checkBox = new CheckBox(Order.this);
                checkBox.setText(questions.get(i).getAnswer());
                checkBox.setTextColor(getResources().getColor(R.color.text_color));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        v = checkBox;
                    }
                });
                answersLinear.addView(checkBox);
            }


        extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v != null) {
                    if (v instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) v;
                        if (!radioButton.getText().toString().contains("*  Extra")) { // if contain the same string
                            if (!radioButton.getText().toString().contains("*")) { // if contain another string
                                radioButton.setText(radioButton.getText().toString() + "  *  Extra");
                            } else { // if it has another string it will extract it and add the new one
                                radioButton.setText(radioButton.getText().toString().substring(0, radioButton.getText().toString().indexOf('*') - 1) + " *  Extra");
                            }
                        }
                    } else {
                        CheckBox checkBox = (CheckBox) v;
                        if (!checkBox.getText().toString().contains("*  Extra")) { // if contain the same string
                            if (!checkBox.getText().toString().contains("*")) { // if contain another string
                                checkBox.setText(checkBox.getText().toString() + "  *  Extra");
                            } else { // if it has another string it will extract it and add the new one
                                checkBox.setText(checkBox.getText().toString().substring(0, checkBox.getText().toString().indexOf('*') - 1) + " *  Extra");
                            }
                        }
                    }
                } else
                    Toast.makeText(Order.this, getResources().getString(R.string.selected_answer), Toast.LENGTH_SHORT).show();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v != null) {
                    if (v instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) v;
                        if (!radioButton.getText().toString().contains("*  no")) { // if contain the same string
                            if (!radioButton.getText().toString().contains("*")) { // if contain another string
                                radioButton.setText(radioButton.getText().toString() + "  *  no");
                            } else { // if it has another string it will extract it and add the new one
                                radioButton.setText(radioButton.getText().toString().substring(0, radioButton.getText().toString().indexOf('*') - 1) + " *  no");
                            }
                        }
                    } else {
                        CheckBox checkBox = (CheckBox) v;
                        if (!checkBox.getText().toString().contains("*  no")) { // if contain the same string
                            if (!checkBox.getText().toString().contains("*")) { // if contain another string
                                checkBox.setText(checkBox.getText().toString() + "  *  no");
                            } else { // if it has another string it will extract it and add the new one
                                checkBox.setText(checkBox.getText().toString().substring(0, checkBox.getText().toString().indexOf('*') - 1) + " *  no");
                            }
                        }
                    }
                } else
                    Toast.makeText(Order.this, getResources().getString(R.string.selected_answer), Toast.LENGTH_SHORT).show();
            }
        });

        little.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v != null) {
                    if (v instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) v;
                        if (!radioButton.getText().toString().contains("*  little")) { // if contain the same string
                            if (!radioButton.getText().toString().contains("*")) { // if contain another string
                                radioButton.setText(radioButton.getText().toString() + "  *  little");
                            } else { // if it has another string it will extract it and add the new one
                                radioButton.setText(radioButton.getText().toString().substring(0, radioButton.getText().toString().indexOf('*') - 1) + " *  little");
                            }
                        }
                    } else {
                        CheckBox checkBox = (CheckBox) v;
                        if (!checkBox.getText().toString().contains("*  little")) { // if contain the same string
                            if (!checkBox.getText().toString().contains("*")) { // if contain another string
                                checkBox.setText(checkBox.getText().toString() + "  *  little");
                            } else { // if it has another string it will extract it and add the new one
                                checkBox.setText(checkBox.getText().toString().substring(0, checkBox.getText().toString().indexOf('*') - 1) + " *  little");
                            }
                        }
                    }
                } else
                    Toast.makeText(Order.this, getResources().getString(R.string.selected_answer), Toast.LENGTH_SHORT).show();
            }
        });
        half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v != null) {
                    if (v instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) v;
                        if (!radioButton.getText().toString().contains("*  half")) { // if contain the same string
                            if (!radioButton.getText().toString().contains("*")) { // if contain another string
                                radioButton.setText(radioButton.getText().toString() + "  *  half");
                            } else { // if it has another string it will extract it and add the new one
                                radioButton.setText(radioButton.getText().toString().substring(0, radioButton.getText().toString().indexOf('*') - 1) + " *  half");
                            }
                        }
                    } else {
                        CheckBox checkBox = (CheckBox) v;
                        if (!checkBox.getText().toString().contains("*  half")) { // if contain the same string
                            if (!checkBox.getText().toString().contains("*")) { // if contain another string
                                checkBox.setText(checkBox.getText().toString() + "  *  half");
                            } else { // if it has another string it will extract it and add the new one
                                checkBox.setText(checkBox.getText().toString().substring(0, checkBox.getText().toString().indexOf('*') - 1) + " *  half");
                            }
                        }
                    }
                } else
                    Toast.makeText(Order.this, getResources().getString(R.string.selected_answer), Toast.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v != null) {
                    if (v instanceof RadioButton) {
                        RadioButton answer = (RadioButton) v;
                        if (answer.getText().toString().contains("*")) {
                            insertForceQuestionRaw(answer.getText().toString().substring(0, 10) + "..");
                            wantedItems.add(new Items("force question", answer.getText().toString(), "", 0,
                                    0, "", "", 0, 0, 0, "", 0,
                                    0, 0, 0, "", "", 0, 0, 0, null));
                            lineDiscount.add(0.0);

                            v = null;
                            dialog.dismiss();
                            int nextQu = questionNo;
                            ArrayList<ItemWithFq> questions = mDbHandler.getItemWithFqs(itemBarcode);
                            if (questionNo < questions.size() - 1) {
                                nextQu = questionNo + 1;
                                showForceQuestionDialog(itemBarcode, nextQu);
                            }

                        } else {
                            Toast.makeText(Order.this, getResources().getString(R.string.select_qty), Toast.LENGTH_SHORT).show();
                        }
                        Log.e("here", "******" + answersLinear.getChildCount());
                    } else {
                        for (int i = 0; i < answersLinear.getChildCount(); i++) {
                            CheckBox checkBox = (CheckBox) answersLinear.getChildAt(i);
                            if (checkBox.isChecked()) {
                                if (checkBox.getText().toString().contains("*")) {
                                    insertForceQuestionRaw(checkBox.getText().toString().substring(0, 10) + "..");
                                    wantedItems.add(new Items("force question", checkBox.getText().toString(), "", 0,
                                            0, "", "", 0, 0, 0, "", 0,
                                            0, 0, 0, "", "", 0, 0, 0, null));
                                    lineDiscount.add(0.0);

                                    v = null;
                                    dialog.dismiss();
                                    int nextQu = questionNo;
                                    ArrayList<ItemWithFq> questions = mDbHandler.getItemWithFqs(itemBarcode);
                                    if (questionNo < questions.size() - 1) {
                                        nextQu = questionNo + 1;
                                        showForceQuestionDialog(itemBarcode, nextQu);
                                    }
                                } else {
                                    Toast.makeText(Order.this, getResources().getString(R.string.select_qty), Toast.LENGTH_SHORT).show();
                                }
                                Log.e("here", "******" + answersLinear.getChildCount());
                            }
                        }
                    }
                }


            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v = null;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void showModifierDialog() {
        if (wantedItems.size() != 0) {
            if (focused != null) {
                dialog = new Dialog(Order.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.pick_modifier_dialog);
                dialog.setCanceledOnTouchOutside(true);

                final Button extra = dialog.findViewById(R.id.extra);
                final Button no = dialog.findViewById(R.id.no);
                final Button little = dialog.findViewById(R.id.little);
                final Button half = dialog.findViewById(R.id.half);
                final Button save = dialog.findViewById(R.id.save);
                final Button exit = dialog.findViewById(R.id.exit);
                final GridView gridView = dialog.findViewById(R.id.modifiers);

                int itemBarcode = wantedItems.get(Integer.parseInt(focused.getTag().toString())).getItemBarcode();
                Log.e("hi", "********" + itemBarcode);
                final ArrayList<ItemWithModifier> modifiers = mDbHandler.getItemWithModifiers(itemBarcode);
                final ArrayList<String> modifiersName = new ArrayList<>();

                for (int i = 0; i < modifiers.size(); i++) {
                    modifiersName.add("(" + modifiers.get(i).getModifierNo() + ") " + modifiers.get(i).getModifierText());
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(Order.this, R.layout.grid_style, modifiersName);
                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        for (int j = 0; j < gridView.getChildCount(); j++) {
                            gridView.getChildAt(j).setBackgroundDrawable(null);
                        }
                        gridView.getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.focused_table));
                        selectedModifier = i;
                    }
                });

                extra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedModifier != -1) {
                            if (!modifiersName.get(selectedModifier).contains("*  Extra")) { // if contain the same string
                                if (!modifiersName.get(selectedModifier).contains("*")) { // if contain another string
                                    modifiersName.set(selectedModifier, modifiersName.get(selectedModifier) + "  *  Extra" + " \n ");
                                    adapter.notifyDataSetChanged();
                                } else { // if it has another string it will extract it and add the new one
                                    modifiersName.set(selectedModifier, modifiersName.get(selectedModifier).substring(0, modifiersName.get(selectedModifier).indexOf('*') - 1) + " *  Extra");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else
                            Toast.makeText(Order.this, getResources().getString(R.string.select_modifer), Toast.LENGTH_SHORT).show();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedModifier != -1) {
                            if (!modifiersName.get(selectedModifier).contains("*  No")) { // if contain the same string
                                if (!modifiersName.get(selectedModifier).contains("*")) { // if contain another string
                                    modifiersName.set(selectedModifier, modifiersName.get(selectedModifier) + "  *  No" + " \n ");
                                    adapter.notifyDataSetChanged();
                                } else { // if it has another string it will extract it and add the new one
                                    modifiersName.set(selectedModifier, modifiersName.get(selectedModifier).substring(0, modifiersName.get(selectedModifier).indexOf('*') - 1) + " *  No");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else
                            Toast.makeText(Order.this, getResources().getString(R.string.select_modifer), Toast.LENGTH_SHORT).show();
                    }
                });
                little.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedModifier != -1) {
                            if (!modifiersName.get(selectedModifier).contains("*  Little")) { // if contain the same string
                                if (!modifiersName.get(selectedModifier).contains("*")) { // if contain another string
                                    modifiersName.set(selectedModifier, modifiersName.get(selectedModifier) + "  *  Little" + " \n ");
                                    adapter.notifyDataSetChanged();
                                } else { // if it has another string it will extract it and add the new one
                                    modifiersName.set(selectedModifier, modifiersName.get(selectedModifier).substring(0, modifiersName.get(selectedModifier).indexOf('*') - 1) + " *  Little");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else
                            Toast.makeText(Order.this, getResources().getString(R.string.select_modifer), Toast.LENGTH_SHORT).show();
                    }
                });
                half.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedModifier != -1) {
                            if (!modifiersName.get(selectedModifier).contains("*  Half")) { // if contain the same string
                                if (!modifiersName.get(selectedModifier).contains("*")) { // if contain another string
                                    modifiersName.set(selectedModifier, modifiersName.get(selectedModifier) + "  *  Half" + " \n ");
                                    adapter.notifyDataSetChanged();
                                } else { // if it has another string it will extract it and add the new one
                                    modifiersName.set(selectedModifier, modifiersName.get(selectedModifier).substring(0, modifiersName.get(selectedModifier).indexOf('*') - 1) + " *  Half");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else
                            Toast.makeText(Order.this, getResources().getString(R.string.select_modifer), Toast.LENGTH_SHORT).show();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < modifiersName.size(); i++) {
                            if (modifiersName.get(i).contains("*")) {
                                insertModifierRaw(modifiersName.get(i).substring(modifiersName.get(i).indexOf('-') + 1,
                                        modifiersName.get(i).indexOf('-') + 10) + "..");

                                String modName = modifiersName.get(i).substring(modifiersName.get(i).indexOf('-') + 1, modifiersName.get(i).length()- modifiersName.get(i).indexOf('-')+1) ;
                                wantedItems.add(Integer.parseInt(focused.getTag().toString()) + 1,
                                        new Items("modifier", modName , "", 0, 0, "",
                                                "", 0, 0, 0, "", 0,
                                                0, 0, 0, "", "", 0, 0, 0, null));
                                lineDiscount.add(0.0);
                                focused.setBackgroundDrawable(null);
                            }
                        }
                        selectedModifier = -1;
                        dialog.dismiss();
                    }
                });
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedModifier = -1;
                        focused.setBackgroundDrawable(null);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            } else {
                Toast.makeText(Order.this, getResources().getString(R.string.chooes_item_to_modifier), Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, getResources().getString(R.string.no_item), Toast.LENGTH_SHORT).show();

    }

    void showDeliveryChangeDialog() {
        if (wantedItems.size() != 0) {
            dialog = new Dialog(Order.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.delivery_change_dialog);
            dialog.setCanceledOnTouchOutside(true);

            Window window = dialog.getWindow();
            window.setLayout(460, 220);

            final EditText addDeliveryEditText = (EditText) dialog.findViewById(R.id.add_delivery);
            Button buttonDone = (Button) dialog.findViewById(R.id.b_done);

            buttonDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!convertToEnglish(addDeliveryEditText.getText().toString()).equals("")) {
                        deliveryCharge.setText(convertToEnglish(addDeliveryEditText.getText().toString()));
                        calculateTotal();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(Order.this, getResources().getString(R.string.enter_dekivery), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.delivary_is_not_avilable), Toast.LENGTH_SHORT).show();
        }
    }

    void showLineDiscountDialog() {

        if (wantedItems.size() != 0) {
            if (focused != null) {
                if (wantedItems.get(Integer.parseInt(focused.getTag().toString())).discountAvailable == 1) {
                    dialog = new Dialog(Order.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.line_discount_dialog);
                    dialog.setCanceledOnTouchOutside(true);

//                    Window window = dialog.getWindow();
//                    window.setLayout(470, 280);

                    final EditText addLineDiscountEditText = (EditText) dialog.findViewById(R.id.add_line_discount);
                    Button buttonDone = (Button) dialog.findViewById(R.id.b_done);
                    final CheckBox radioButton = (CheckBox) dialog.findViewById(R.id.discount_perc);

                    buttonDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!addLineDiscountEditText.getText().toString().equals("")) {
                                lineDiscountValue = Double.parseDouble(addLineDiscountEditText.getText().toString());

                                if (radioButton.isChecked()) {
                                    TableRow tableRow = (TableRow) tableLayout.getChildAt(Integer.parseInt(focused.getTag().toString()));
                                    TextView textViewTotal = (TextView) tableRow.getChildAt(3);

                                    lineDiscountValue = (Double.parseDouble(convertToEnglish(addLineDiscountEditText.getText().toString()))) *
                                            (Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()))) / 100;
                                }
                                lineDiscount.set(Integer.parseInt(convertToEnglish(focused.getTag().toString())), lineDiscountValue);
                                calculateTotal();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(Order.this, getResources().getString(R.string.enter_line_discount), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                } else
                    Toast.makeText(Order.this, getResources().getString(R.string.discount_not_avilable), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(Order.this, getResources().getString(R.string.chooes_item_to_add_linediscount), Toast.LENGTH_SHORT).show();
        }
    }

    void showDiscountDialog() {

        boolean discAvailable = false;
        for (int i = 0; i < wantedItems.size(); i++) {
            if (wantedItems.get(i).discountAvailable == 1) {
                discAvailable = true;
                break;
            }
        }
        if (discAvailable) {
            dialog = new Dialog(Order.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.discount_dialog);
            dialog.setCanceledOnTouchOutside(true);

//            Window window = dialog.getWindow();
//            window.setLayout(470, 280);

            final EditText addDiscountEditText = (EditText) dialog.findViewById(R.id.add_discount);
            Button buttonDone = (Button) dialog.findViewById(R.id.b_done);
            discPerc = (CheckBox) dialog.findViewById(R.id.discount_perc);

            buttonDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!convertToEnglish(addDiscountEditText.getText().toString()).equals("")) {
                        discountValue = Double.parseDouble(convertToEnglish(addDiscountEditText.getText().toString()));
                        voucherDiscount = Double.parseDouble(convertToEnglish(addDiscountEditText.getText().toString()));

                        if (discPerc.isChecked()) {
                            discountValue = (Double.parseDouble(convertToEnglish(addDiscountEditText.getText().toString()))) *
                                    totalItemsWithDiscount / 100;
                            Log.e("sum ", "" + (Double.parseDouble(convertToEnglish(addDiscountEditText.getText().toString()))) + "*" +
                                    totalItemsWithDiscount + "/" + "100");
                        }
                        discChanged = true;
                        disCount.setText(convertToEnglish(discountValue + ""));
                        calculateTotal();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(Order.this, getResources().getString(R.string.enter_discount), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        } else
            Toast.makeText(Order.this, getResources().getString(R.string.discount_not_avilable_for_curent_item), Toast.LENGTH_SHORT).show();
    }

    void calculateTotal() {

        totalItemsWithDiscount = 0.0;
        double lineDisCountValue = 0.0;
        Log.e("111", "dd --> " + convertToEnglish(deliveryCharge.getText().toString()));
        double deliveryChargeValue = Double.parseDouble(convertToEnglish(deliveryCharge.getText().toString()));

        double sum = 0;
        double totalItemsTaxInclude = 0;
        double totalItemsTaxExclude = 0;
        for (int k = 0; k < tableLayout.getChildCount(); k++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
            TextView textViewTotal = (TextView) tableRow.getChildAt(3);
            TextView firstText = (TextView) tableRow.getChildAt(0);

            if (!firstText.getText().toString().contains("*")) {
                sum += Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
                lineDisCountValue += lineDiscount.get(k);

                double totalAfterDisc = Double.parseDouble(convertToEnglish(textViewTotal.getText().toString())) - lineDiscount.get(k);

                if (wantedItems.get(k).getDiscountAvailable() == 1) // items have discount available
                    totalItemsWithDiscount += totalAfterDisc;
            }
        }

        // _______________________________________________ calculate discount
        boolean discNotAvailableForAll = true;
        for (int i = 0; i < wantedItems.size(); i++) {
            if (wantedItems.get(i).discountAvailable == 1) {
                discNotAvailableForAll = false;
                break;
            }
        }
        if (mDbHandler.getOrderTransactionsTemp("" + sectionNumber, "" + tableNumber).size() == 0) { // Takeaway In Discount
            if (discPerc != null) {
                if (discPerc.isChecked()) {
                    discountValue = Double.parseDouble(convertToEnglish((voucherDiscount * totalItemsWithDiscount / 100) + ""));
                    disCount.setText((discNotAvailableForAll ? "0.0" : "" + convertToEnglish(String.format("%.3f", (double) discountValue))));
                } else
                    disCount.setText((discNotAvailableForAll ? "0.0" : "" + convertToEnglish(String.format("%.3f", (double) discountValue))));
            }
        } else { // Dine In Discount
            if (!discChanged) {
                ArrayList<OrderHeader> orderHeaders = mDbHandler.getOrderHeaderTemp("" + sectionNumber, "" + tableNumber);
                disCount.setText("" + convertToEnglish((orderHeaders.get(0).getTotalDiscount()) + ""));
                discountValue = orderHeaders.get(0).getTotalDiscount();
                // I can generate the original discount = (disc * 100 / total ) / 100 but how if it was a value ?

            } else { // if we entered another discount
                if (discPerc != null) {
                    if (discPerc.isChecked()) {
                        Log.e("disc   ", "" + voucherDiscount + "*" + totalItemsWithDiscount + "/100");
                        discountValue = Double.parseDouble(convertToEnglish((voucherDiscount * totalItemsWithDiscount / 100) + ""));
                        disCount.setText((discNotAvailableForAll ? "0.0" : "" + convertToEnglish(String.format("%.3f", (double) discountValue))));
                    } else
                        disCount.setText((discNotAvailableForAll ? "0.0" : "" + convertToEnglish(String.format("%.3f", (double) discountValue))));
                }
            }
        }
        // _______________________________________________ calculate tax

        for (int k = 0; k < tableLayout.getChildCount(); k++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
            TextView textViewTotal = (TextView) tableRow.getChildAt(3);
            TextView firstText = (TextView) tableRow.getChildAt(0);

            if (!firstText.getText().toString().contains("*")) {
                double totalAfterDisc = Double.parseDouble(convertToEnglish(textViewTotal.getText().toString())) - lineDiscount.get(k);

                if (wantedItems.get(k).getTaxType() == 0) {

                    double itemDiscount = 0.0;
                    if (wantedItems.get(k).getDiscountAvailable() == 1)
                        itemDiscount = (discountValue / totalItemsWithDiscount) * totalAfterDisc;

//                    Log.e("************", "discount (" + discountValue + "/" + totalItemsWithDiscount + ")*(" +totalAfterDisc +")");

                    double totalLineAfterDisc = totalAfterDisc - itemDiscount;
                    if (Settings.tax_type == 0) {
                        totalItemsTaxInclude += totalLineAfterDisc * wantedItems.get(k).getTax() / 100;
//                        Log.e("**********", "totalItemsTaxInclude = " + totalLineAfterDisc +"*"+ wantedItems.get(k).getTax()+"/100 = " + totalAfterDisc * wantedItems.get(k).getTax() / 100);
                    } else
                        totalItemsTaxExclude += (totalAfterDisc * wantedItems.get(k).getTax() / 100) / (1 + (wantedItems.get(k).getTax() / 100));
                }
            }
        }


        double subTotalValue = Double.parseDouble(convertToEnglish((sum - (lineDisCountValue + discountValue) + deliveryChargeValue) + ""));
        double serviceValue = Double.parseDouble(convertToEnglish((sum * (Settings.service_value / 100)) + ""));
        double serviceTax = Double.parseDouble(convertToEnglish((serviceValue * (Settings.service_tax / 100)) + ""));
        double taxValue = Double.parseDouble(convertToEnglish((totalItemsTaxInclude + totalItemsTaxExclude + serviceTax) + ""));
        double amountDueValue = Double.parseDouble(convertToEnglish((subTotalValue + serviceValue + serviceTax + totalItemsTaxInclude) + ""));

//        System.out.println(Math.round(d));
        total.setText("" + sum);
        lineDisCount.setText(convertToEnglish("" + String.format("%.3f", (double) lineDisCountValue)));
//        disCount.setText("" + discountValue);
        deliveryCharge.setText(convertToEnglish("" + String.format("%.2f", (double) deliveryChargeValue)));
        subTotal.setText(convertToEnglish("" + String.format("%.2f", (double) subTotalValue)));
        service.setText(convertToEnglish("" + String.format("%.2f", (double) serviceValue)));
        tax.setText(convertToEnglish("" + String.format("%.2f", (double) taxValue)));
        amountDue.setText(convertToEnglish("" + String.format("%.2f", (double) amountDueValue)));
        balance = amountDueValue;
    }

    void saveInOrderTransactionObj() {
        OrderTransactionsObj.clear();
        for (int k = 0; k < tableLayout.getChildCount(); k++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
            TextView textViewQty = (TextView) tableRow.getChildAt(0);
            TextView textViewTotal = (TextView) tableRow.getChildAt(3);
            TextView textLineDiscount = (TextView) tableRow.getChildAt(4);

            double totalLine = Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
            double lineDiscount_ = lineDiscount.get(k);
            double disc = Double.parseDouble(convertToEnglish(disCount.getText().toString()));
            double serviceTax = Double.parseDouble(convertToEnglish(service.getText().toString())) * Settings.service_tax;

            double discount = 0.0;
            if (wantedItems.get(k).getDiscountAvailable() == 1)
                discount = (disc / totalItemsWithDiscount) * (totalLine - lineDiscount_);

//            Log.e("here", "******" + disc + "/" + totalItemsWithDiscount + "*" + totalLine + "-" + lineDiscount_);

            OrderTransactionsObj.add(new OrderTransactions(orderTypeFlag, 0, today, Settings.POS_number, Settings.store_number,
                    voucherNo, voucherSerial, "" + wantedItems.get(k).getItemBarcode(), wantedItems.get(k).getMenuName(),
                    wantedItems.get(k).getSecondaryName(), wantedItems.get(k).getKitchenAlias(), wantedItems.get(k).getMenuCategory(),
                    wantedItems.get(k).getFamilyName(), Integer.parseInt(convertToEnglish(textViewQty.getText().toString())), wantedItems.get(k).getPrice(),
                    totalLine, discount, lineDiscount_, discount + lineDiscount_, wantedItems.get(k).getTax(),
                    wantedItems.get(k).getTax(), 0, Double.parseDouble(service.getText().toString()), serviceTax,
                    tableNumber, sectionNumber, Settings.shift_number, Settings.shift_name, Settings.password, Settings.user_name, time));
        }
    }

    void saveInOrderHeaderObj() {

        double disc = Double.parseDouble(convertToEnglish(disCount.getText().toString()));
        double ldisc = Double.parseDouble(convertToEnglish(lineDisCount.getText().toString()));
        double serviceTax = Double.parseDouble(convertToEnglish(service.getText().toString())) * Settings.service_tax;

        OrderHeaderObj = new OrderHeader(orderTypeFlag, 0, today, Settings.POS_number, Settings.store_number,
                voucherNo, voucherSerial, Double.parseDouble(convertToEnglish(total.getText().toString())), ldisc, disc, disc + ldisc,
                Settings.service_value, Double.parseDouble((convertToEnglish(tax.getText().toString()))), serviceTax, Double.parseDouble((convertToEnglish(subTotal.getText().toString()))),
                Double.parseDouble(convertToEnglish(amountDue.getText().toString())), Double.parseDouble(convertToEnglish(deliveryCharge.getText().toString())), tableNumber,
                sectionNumber, PayMethods.cashValue1, PayMethods.creditCardValue1, PayMethods.chequeValue1, PayMethods.creditValue1,
                PayMethods.giftCardValue1, PayMethods.pointValue1, Settings.shift_name, Settings.shift_number, "No Waiter", 0, Settings.user_name, Settings.password, time);


    }

    void saveInOrderTransactionTemp() {

        calculateTotal();

        if (mDbHandler.getOrderTransactionsTemp("" + sectionNumber, "" + tableNumber).size() != 0)
            mDbHandler.deleteFromOrderTransactionTemp("" + sectionNumber, "" + tableNumber);

        for (int k = 0; k < tableLayout.getChildCount(); k++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
            TextView textViewQty = (TextView) tableRow.getChildAt(0);
            TextView textViewTotal = (TextView) tableRow.getChildAt(3);
            TextView textLineDiscount = (TextView) tableRow.getChildAt(4);

            double totalLine = Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
            double lineDiscount_ = lineDiscount.get(k);
            double disc = Double.parseDouble(convertToEnglish(disCount.getText().toString()));
            double serviceTax = Double.parseDouble(convertToEnglish(service.getText().toString())) * Settings.service_tax;

            double discount = 0.0;
            if (wantedItems.get(k).getDiscountAvailable() == 1)
                discount = (disc / totalItemsWithDiscount) * (totalLine - lineDiscount_);

            mDbHandler.addOrderTransactionTemp(new OrderTransactions(orderTypeFlag, 0, today, Settings.POS_number, Settings.store_number,
                    voucherNo, voucherSerial, "" + wantedItems.get(k).getItemBarcode(), wantedItems.get(k).getMenuName(),
                    wantedItems.get(k).getSecondaryName(), wantedItems.get(k).getKitchenAlias(), wantedItems.get(k).getMenuCategory(),
                    wantedItems.get(k).getFamilyName(), Integer.parseInt(convertToEnglish(textViewQty.getText().toString())), wantedItems.get(k).getPrice(),
                    totalLine, discount, lineDiscount_, discount + lineDiscount_, wantedItems.get(k).getTax(),
                    wantedItems.get(k).getTax(), 0, Double.parseDouble(convertToEnglish(service.getText().toString())), serviceTax,
                    tableNumber, sectionNumber, Settings.shift_number, Settings.shift_name, Settings.password, Settings.user_name, time));
        }
    }

    void saveInOrderHeaderTemp() {

        if (mDbHandler.getOrderHeaderTemp("" + sectionNumber, "" + tableNumber).size() != 0)
            mDbHandler.deleteFromOrderHeaderTemp("" + sectionNumber, "" + tableNumber);

        double disc = Double.parseDouble(convertToEnglish(disCount.getText().toString()));
        double ldisc = Double.parseDouble(convertToEnglish(lineDisCount.getText().toString()));
        double serviceTax = Double.parseDouble(convertToEnglish(service.getText().toString())) * Settings.service_tax;

        mDbHandler.addOrderHeaderTemp(new OrderHeader(orderTypeFlag, 0, today, Settings.POS_number, Settings.store_number,
                voucherNo, voucherSerial, Double.parseDouble(convertToEnglish(total.getText().toString())), ldisc, disc, disc + ldisc,
                Settings.service_value, Double.parseDouble(convertToEnglish(tax.getText().toString())), serviceTax, Double.parseDouble(convertToEnglish(subTotal.getText().toString())),
                Double.parseDouble(convertToEnglish(amountDue.getText().toString())), Double.parseDouble(convertToEnglish(deliveryCharge.getText().toString())), sectionNumber,
                tableNumber, 0.00, 0.00, 0.00, 0.00,
                0.00, 0.00, Settings.shift_name, Settings.shift_number, waiter, seatNo, Settings.user_name, Settings.password, time));
    }

    void sendToKitchen() {

        try {
            JSONObject obj1 = OrderHeaderObj.getJSONObject();

            JSONArray obj2 = new JSONArray();
            for (int i = 0; i < OrderTransactionsObj.size(); i++)
                obj2.put(OrderTransactionsObj.get(i).getJSONObject());

            JSONObject obj = new JSONObject();
            obj.put("Header", obj1);
            obj.put("Items", obj2);

            SendCloud sendCloud = new SendCloud(Order.this, obj);
            sendCloud.startSending();
//            new JSONTask2().execute();

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }

    }

    private class JSONTask2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Order.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                JSONObject obj = new JSONObject();

                try {
                    JSONObject obj1 = new JSONObject();
                    obj1.put("POSNO", 1);
                    obj1.put("ORDERNO", 13);
                    obj1.put("ORDERTYPE", 2);
                    obj1.put("TABLENO", 1);
                    obj1.put("SECTIONNO", "main");

                    JSONObject obj2 = new JSONObject();
                    obj2.put("ITEMCODE", 1);
                    obj2.put("ITEMNAME", "مادة 1");
                    obj2.put("QTY", 3);
                    obj2.put("PRICE", 5.25);
                    obj2.put("NOTE", "any");
                    obj2.put("ISUPDATE", 0);

                    JSONObject obj3 = new JSONObject();
                    obj3.put("ITEMCODE", 1);
                    obj3.put("ITEMNAME", "مادة 2");
                    obj3.put("QTY", 3);
                    obj3.put("PRICE", 5.25);
                    obj3.put("NOTE", "any");
                    obj3.put("ISUPDATE", 1);

                    JSONArray arr = new JSONArray();
                    arr.put(obj2);
                    arr.put(obj3);

                    obj.put("Header", obj1);
                    obj.put("Items", arr);

                } catch (JSONException e) {
                    Log.e("Tag" , "JSONException");
                }

                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/RestSaveKitchenScreen?";

                String data =
                        "compno=" + URLEncoder.encode("302", "UTF-8") + "&" +
                                "compyear=" + URLEncoder.encode("2018", "UTF-8") + "&" +
                                "voucher=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data );

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();


                while ((json_getString = bufferedReader.readLine()) != null) {
                    stringBuffer.append(json_getString + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

//                JsonResponse = sb.toString();
                Log.e("tag", "" + stringBuffer.toString());
                return stringBuffer.toString();
//
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("Voucher Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

    public ArrayList<OrderTransactions> getOrderTransactionObj() {
        return OrderTransactionsObj;
    }

    public OrderHeader getOrderHeaderObj() {
        return OrderHeaderObj;
    }

    @SuppressLint("SetTextI18n")
    void fillPreviousOrder() {
        ArrayList<OrderHeader> orderHeaders = mDbHandler.getOrderHeaderTemp("" + sectionNumber, "" + tableNumber);
        List<OrderTransactions> orderTransactions = mDbHandler.getOrderTransactionsTemp("" + sectionNumber, "" + tableNumber);

        for (int k = 0; k < orderTransactions.size(); k++) {

            lineDiscount.add(orderTransactions.get(k).getlDiscount());
            wantedItems.add(new Items(orderTransactions.get(k).getItemCategory(), orderTransactions.get(k).getItemName(),
                    orderTransactions.get(k).getItemFamily(), orderTransactions.get(k).getTaxValue(),
                    orderTransactions.get(k).getTaxKind(), orderTransactions.get(k).getSecondaryName(),
                    orderTransactions.get(k).getKitchenAlias(), Integer.parseInt(orderTransactions.get(k).getItemBarcode()),
                    1, 0, "", 0, 1, 0, 0, "",
                    "", orderTransactions.get(k).getPrice(), 1, 1, null, 0, 0, 0));

            vhSerial.setText(orderTransactions.get(k).getVoucherNo());

            final TableRow row = new TableRow(Order.this);

            TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
            lp.setMargins(2, 0, 2, 0);
            row.setLayoutParams(lp);

            for (int i = 0; i < 5; i++) {
                TextView textView = new TextView(Order.this);

                switch (i) {
                    case 0:
                        textView.setText("" + orderTransactions.get(k).getQty());
                        break;
                    case 1:
                        textView.setText(orderTransactions.get(k).getItemName());
                        break;
                    case 2:
                        textView.setText("" + orderTransactions.get(k).getPrice());
                        break;
                    case 3:
                        textView.setText("" + orderTransactions.get(k).getTotal());
                        break;
                    case 4:
                        textView.setText("" + orderTransactions.get(k).getlDiscount()); // line discount
                        break;
                }

                if (orderTransactions.get(k).getQty() == 0) {
                    textView.setTextColor(ContextCompat.getColor(Order.this, R.color.exit));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    textView.setGravity(Gravity.CENTER);
                } else {
                    textView.setTextColor(ContextCompat.getColor(Order.this, R.color.text_color));
                    textView.setGravity(Gravity.CENTER);
                }

                if (i != 4) {
                    TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                    textView.setLayoutParams(lp1);
                } else {
                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.00001f);
                    textView.setLayoutParams(lp2);
                }

                row.addView(textView);
                row.setTag(tableLayoutPosition);

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        focused = row;
                        setRawFocused(row);
                    }
                });

                row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        focused = row;
                        setRawFocused(row);
                        return true;
                    }
                });
            }
            tableLayout.addView(row);
            tableLayoutPosition++;
        }

        //_________________________________________________________________________

        user.setText(orderHeaders.get(0).getWaiter());
        seats.setText(convertToEnglish(("" + orderHeaders.get(0).getSeatsNumber())));
        total.setText(convertToEnglish(("" + orderHeaders.get(0).getTotal())));
        disCount.setText(convertToEnglish("" + orderHeaders.get(0).getTotalDiscount()));
        lineDisCount.setText(convertToEnglish(("" + orderHeaders.get(0).getTotalLineDiscount()))); // here
        deliveryCharge.setText(convertToEnglish(("" + orderHeaders.get(0).getDeliveryCharge())));
        subTotal.setText(convertToEnglish(("" + orderHeaders.get(0).getSubTotal())));
        tax.setText(convertToEnglish(("" + orderHeaders.get(0).getTotalTax())));
        service.setText(convertToEnglish(("" + orderHeaders.get(0).getTotalService())));
        amountDue.setText(convertToEnglish(("" + orderHeaders.get(0).getAmountDue())));

        voucherSerial = orderHeaders.get(0).getVoucherSerial();
        voucherNo = orderHeaders.get(0).getVoucherNumber();
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (orderTypeFlag == 0) {
            Intent intent = new Intent(Order.this, Main.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Order.this, DineIn.class);
            startActivity(intent);
        }
    }


    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    @SuppressLint("ClickableViewAccessibility")
    void initialize() {

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        itemGridView = (GridView) findViewById(R.id.GridViewItems);
        catGridView = (GridView) findViewById(R.id.GridViewCats);
        orderType = (TextView) findViewById(R.id.orderType);
        tableNo = (TextView) findViewById(R.id.tableNumber);
        check = (TextView) findViewById(R.id.checkNumber);
        date = (TextView) findViewById(R.id.date);
        user = (TextView) findViewById(R.id.user);
        seats = (TextView) findViewById(R.id.seat_number);

        pay = (Button) findViewById(R.id.pay);
        order = (Button) findViewById(R.id.order);
        modifier = (Button) findViewById(R.id.modifier);
        void_ = (Button) findViewById(R.id.void_);
        delivery = (Button) findViewById(R.id.delivery_b);
        split = (Button) findViewById(R.id.split);
        discount = (Button) findViewById(R.id.discount_b);
        lDiscount = (Button) findViewById(R.id.line_discount_b);
        priceChange = (Button) findViewById(R.id.price_change);
        back = (Button) findViewById(R.id.back);

        total = (TextView) findViewById(R.id.total);
        disCount = (TextView) findViewById(R.id.discount);
        lineDisCount = (TextView) findViewById(R.id.line_discount);
        deliveryCharge = (TextView) findViewById(R.id.delivery);
        subTotal = (TextView) findViewById(R.id.sub_total);
        tax = (TextView) findViewById(R.id.tax);
        service = (TextView) findViewById(R.id.service);
        amountDue = (TextView) findViewById(R.id.amount_due);
        vhSerial = (TextView) findViewById(R.id.vhSerial);

        pay.setOnTouchListener(onTouchListener);
        order.setOnTouchListener(onTouchListener);
        modifier.setOnTouchListener(onTouchListener);
        void_.setOnTouchListener(onTouchListener);
        deliveryCharge.setOnTouchListener(onTouchListener);
        split.setOnTouchListener(onTouchListener);
        discount.setOnTouchListener(onTouchListener);
        priceChange.setOnTouchListener(onTouchListener);

        pay.setOnClickListener(onClickListener);
        order.setOnClickListener(onClickListener);
        modifier.setOnClickListener(onClickListener);
        void_.setOnClickListener(onClickListener);
        delivery.setOnClickListener(onClickListener);
        discount.setOnClickListener(onClickListener);
        lDiscount.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);

    }
}
