package com.tamimi.sundos.restpos;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.tamimi.sundos.restpos.BackOffice.MenuRegistration;
//import com.tamimi.sundos.restpos.BackOffice.OrderLayout; //update8
import com.tamimi.sundos.restpos.Models.CancleOrder;
import com.tamimi.sundos.restpos.Models.FamilyCategory;
import com.tamimi.sundos.restpos.Models.ForceQuestions;
import com.tamimi.sundos.restpos.Models.ItemWithFq;
import com.tamimi.sundos.restpos.Models.ItemWithModifier;
import com.tamimi.sundos.restpos.Models.ItemWithScreen;
import com.tamimi.sundos.restpos.Models.Items;
import com.tamimi.sundos.restpos.Models.MaxSerial;
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
import java.io.ByteArrayOutputStream;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Order extends AppCompatActivity {

    Button modifier, void_, delivery, discount, lDiscount, split, priceChange;
    TextView total, lineDisCount, disCount, deliveryCharge, subTotal, service, tax, amountDue, vhSerial, details, onOffLine;
    Button pay, order;
    TextView orderType, tableNo, check, date, user, seats;
    TableLayout tableLayout, tableItem, tableDetail;
    GridView catGridView, itemGridView;
    CheckBox discPerc;
    Button back;
    LinearLayout baLiner;
    boolean showChek = false;
    ImageView mo1,mo2;
ImageView  views;
    RelativeLayout imageMove ;

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
    int index = 0;
    int index2 = 0;

    static ArrayList<OrderTransactions> OrderTransactionsObj;
    static OrderHeader OrderHeaderObj;

    static ArrayList<OrderTransactions> OrderTransactionsSplit;
    static OrderHeader orderHeaderSplit;


    public static int voucherSerial;
    public static String OrderType, today, time, yearMonth, voucherNo;

    View v = null;
    String waiter = "No Waiter";
    String waiterNo = "-1";
    int tableNumber, sectionNumber, seatNo;
    ArrayList<Items> wantedItems;
    List<Items> items = new ArrayList<>();
    ArrayList<Double> lineDiscount;
    ArrayList<Items> requestedItems;
    ArrayList<OrderTransactions> requestedItemsSplit;
    ArrayList<OrderTransactions> requestedItemsSplitTemp;

    TableRow focused = null;
    int selectedModifier = -1;
    boolean showdetal = false;
    Dialog dialog;
    private DatabaseHandler mDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order);
        requestedItemsSplit = new ArrayList<>();
        requestedItemsSplitTemp = new ArrayList<>();
//        mo1=(ImageView)findViewById(R.id.mo1);
//        mo2=(ImageView)findViewById(R.id.mo2);
        Log.e("Order ", "in 12345");
        imageMove = (RelativeLayout) findViewById(R.id.imageMove);
        initialize();

        mDbHandler = new DatabaseHandler(Order.this);
        OrderTransactionsObj = new ArrayList<>();
        items = mDbHandler.getAllItems();
        wantedItems = new ArrayList<>();
        lineDiscount = new ArrayList<Double>();
//        categories = mDbHandler.getUsedCategories();//update 10

       OrderTransactionsSplit = new ArrayList<>();

        fillCategories();
        showCats();
//        blinkAnnouncement(onOffLine);
        Settings.focas = onOffLine;
        if (Settings.onOFF) {
            new Settings().blinkAnnouncement(true);
        } else {
            new Settings().blinkAnnouncement(false);
        }

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
//                            sendToKitchen();

                            Intent intentPay = new Intent(Order.this, PayMethods.class);
                            startActivity(intentPay);
                            setSlideAnimation();

                        } else
                            new Settings().makeText(Order.this, getResources().getString(R.string.amountdue_oo));
                    }
                    break;

                case R.id.order:
                    if (orderTypeFlag == 1) {
                        if (!(Double.parseDouble(amountDue.getText().toString()) == 0)) {
                            saveInOrderTransactionTemp();
                            saveInOrderHeaderTemp();
                            List<MaxSerial> max = new ArrayList<>();
                            max = mDbHandler.getMaxSerialForVhf();
                            if (max.size() != 0) {
                                String vhNO = max.get(0).getMaxSerial();
                                if (Integer.parseInt(voucherNo) > Integer.parseInt(vhNO)) {
                                    mDbHandler.updateMaxVhf(voucherNo);
                                }
                            }
                            Intent intent = new Intent(Order.this, DineIn.class);
                            startActivity(intent);
                            setSlideAnimation();
//                            finish();

                        } else
                            new Settings().makeText(Order.this, getResources().getString(R.string.amountdue_oo));
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
                case R.id.split:
                    if (orderTypeFlag != 0) {
                        showSplitDialog();
                    } else {
                        Toast.makeText(Order.this, "Can not make split from Takeaway ", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.orderType:
                    if (showChek) {
                        showCats();
                    }
                    break;
                case R.id.details:
                    if (!showdetal) {
                        notShowDetails();
                        showdetal = true;
                    } else {
                        ShowDetails();
                        showdetal = false;
                    }
                    break;
            }
        }
    };


    void notShowDetails() {

        tableDetail.setVisibility(View.GONE);
        details.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrowup));

    }

    void ShowDetails() {

        tableDetail.setVisibility(View.VISIBLE);
        details.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrowdown));

    }

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
        back.setVisibility(View.GONE);
        baLiner.setVisibility(View.GONE);
        showChek = false;

    }

    void showItems() {
        catGridView.setVisibility(View.INVISIBLE);
        itemGridView.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        baLiner.setVisibility(View.VISIBLE);
        showChek = true;
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

//        int transactionsSize = mDbHandler.getMaxSerial("ORDER_HEADER","0")+1;
//        int transactionsTempSize =  mDbHandler.getMaxSerial("ORDER_HEADER_TEMP","0")+1;
        //        int transactionsSize = 0, transactionsTempSize = 0;

//        if (transactions.size() != 0)
//            transactionsSize = transactions.get(transactions.size() - 1).getVoucherSerial();
//
//        if (transactionsTemp.size() != 0)
//            transactionsTempSize = transactionsTemp.get(transactionsTemp.size() - 1).getVoucherSerial();

//        if (transactionsSize > transactionsTempSize) {
//            voucherSerial = transactionsSize ;
//        } else {
//            voucherSerial = transactionsTempSize;
//        }
        List<MaxSerial> max = new ArrayList<>();

        max = mDbHandler.getMaxSerialForVhf();
        if (max.size() != 0) {
            voucherSerial = Integer.parseInt(max.get(0).getMaxSerial()) + 1;
        } else {
            MaxSerial maxN = new MaxSerial("0", "0");
            mDbHandler.addMAXSerial(maxN);
            voucherSerial = 1;
        }
        Log.e("maxSerial = ", "" + voucherSerial);

        date.setText(today);
        voucherNo = "" + voucherSerial;

    }

    void fillCategories() {
        Log.e("Order ", "in fill 12345");
        List<FamilyCategory> allCats = mDbHandler.getAllFamilyCategory();
        for (int i = 0; i < allCats.size(); i++) {
            if (allCats.get(i).getType() != 2) {
                allCats.remove(i);
                i--;
            }
        }
        Log.e("cat size", "=" + allCats.size());

//        for (int i = 0; i < categories.size(); i++) {
//            for (int k = 0; k < allCats.size(); k++) {
//                if (categories.get(i).getCategoryName().equals(allCats.get(k).getName())) {
//                    categories.get(i).setCatPic(StringToBitMap(allCats.get(k).getCatPic()));
//                }
//            }
//        }//update 9

//        LayoutCategoryAdapter adapter = new LayoutCategoryAdapter(Order.this, categories, 3);
//        catGridView.setAdapter(adapter); //update 1

        LayoutCategoryAdapter adapter = new LayoutCategoryAdapter(Order.this, allCats, 3);
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ImageView insertImage(View view,String picItem){


        ImageView imageView = new ImageView(Order.this);
        imageView.setImageBitmap(StringToBitMap(picItem));
        int p1[] = new int[2];
        int p2[] = new int[2];
        tableLayout.getLocationInWindow(p2);
        view.getLocationInWindow(p1);
        imageView.setVisibility(View.VISIBLE);
        Log.e("location ",""+p1[0]+"    "+p1[1]);        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(70,
                70);
        imageView.setLayoutParams(params);
        imageView.setX(p1[0]+10);
        imageView.setY(p1[1]);

        imageMove.addView(imageView);
        imageView.animate()
                .x(p2[0])
                .y(p2[1])
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        imageView.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }


                })
                .start();





return imageView;
    }

    @SuppressLint("ClickableViewAccessibility")
    void fillItems(String categoryName) {
//        ArrayList<UsedItems> subList = mDbHandler.getRequestedItems(categoryName);//update 11
        ArrayList<Items> subList = mDbHandler.getRequestedItems2(categoryName);


        if (!categoryName.equals("") && subList.size() != 0) {

            if (subList.size() != 0) {
                List<Items> items = mDbHandler.getAllItems();
                requestedItems = new ArrayList<>();

/*first
//                for (int i = 0; i < subList.size(); i++) {
//                    if (Character.isDigit(subList.get(i).getitemName().charAt(0)) || subList.get(i).getitemName().equals("")) { // no data in this position
//                        requestedItems.add(new Items("", "", "", 0, 0, "", "",
//                                0, 0, 0, "", 0, 0, 0, 0,
//                                "", "", 0, 0, 0, null, subList.get(i).getBackground(),
//                                subList.get(i).getBackground(), 0));
//                    } else {
//                        for (int j = 0; j < items.size(); j++) {
//                            if (subList.get(i).getitemName().equals(items.get(j).getMenuName()))
//                                requestedItems.add(new Items(categoryName, items.get(j).getMenuName(), items.get(j).getFamilyName(),
//                                        items.get(j).getTax(), items.get(j).getTaxType(), items.get(j).getSecondaryName(), items.get(j).getKitchenAlias(),
//                                        items.get(j).getItemBarcode(), items.get(j).getStatus(), items.get(j).getItemType(), items.get(j).getInventoryUnit(),
//                                        items.get(j).getWastagePercent(), items.get(j).getDiscountAvailable(), items.get(j).getPointAvailable(),
//                                        items.get(j).getOpenPrice(), items.get(j).getKitchenPrinter(), items.get(j).getDescription(), items.get(j).getPrice(),
//                                        items.get(j).getUsed(), items.get(j).getShowInMenu(), items.get(j).getPic(), subList.get(i).getBackground(),
//                                        subList.get(i).getTextColor(), subList.get(i).getPosition()));
//                        }
//                    }
//                }//update 12
end*/

                for (int i = 0; i < subList.size(); i++) {
                    if (Character.isDigit(subList.get(i).getMenuName().charAt(0)) || subList.get(i).getMenuName().equals("")) { // no data in this position
                        requestedItems.add(new Items("", "", "", 0, 0, "", "",
                                0, 0, 0, "", 0, 0, 0, 0,
                                "", "", 0, 0, 0, null, subList.get(i).getBackground(),
                                subList.get(i).getBackground(), 0));
                    } else {
                        for (int j = 0; j < items.size(); j++) {
                            if (subList.get(i).getMenuName().equals(items.get(j).getMenuName()))
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

                Log.e("menu name ", "" + requestedItems.get(0).getMenuName());

                foodAdapter = new FoodAdapter1(Order.this, requestedItems);
                itemGridView.setAdapter(foodAdapter);

                itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @SuppressLint("ResourceType")
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        LinearLayout linearLayout = (LinearLayout) view;
                        LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);
                        views = (ImageView) innerLinearLayout.getChildAt(0);

                        insertImage(view,requestedItems.get(i).getPic());

                        if (!requestedItems.get(i).getMenuName().equals("")) {
                            if (requestedItems.get(i).getOpenPrice() != 0) {
//
                                showOpenPriceDilaog(i);

                            } else {
//                            boolean exist = false;
//                            int index = 0;
//                            for (int k = 0; k < tableLayout.getChildCount(); k++) {
//                                TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
//                                TextView textViewName = (TextView) tableRow.getChildAt(1);
//                                if (textViewName.getText().toString().equals(requestedItems.get(i).getMenuName())) {
//                                    exist = true;
//                                    index = k;
//                                    break;
//                                }
//                            }
//
//
//                            if (!exist) {
//                                ArrayList<ItemWithFq> questions = mDbHandler.getItemWithFqs(requestedItems.get(i).itemBarcode);
//                                if (questions.size() == 0) {
//                                    wantedItems.add(requestedItems.get(i));
//                                    lineDiscount.add(0.0);
//                                    insertItemRaw(requestedItems.get(i));
//                                } else {
//                                    wantedItems.add(requestedItems.get(i));
//                                    lineDiscount.add(0.0);
//                                    insertItemRaw(requestedItems.get(i));
//                                    showForceQuestionDialog(requestedItems.get(i).itemBarcode, 0);
//                                }
//                            } else {
//                                TableRow tableRow = (TableRow) tableLayout.getChildAt(index);
//                                TextView textViewQty = (TextView) tableRow.getChildAt(0);
//                                TextView textViewPrice = (TextView) tableRow.getChildAt(2);
//                                TextView textViewTotal = (TextView) tableRow.getChildAt(3);
//                                TextView textViewLineDiscount = (TextView) tableRow.getChildAt(4);
//
//                                double qty = Double.parseDouble(convertToEnglish(textViewQty.getText().toString()));
//                                double price = Double.parseDouble(convertToEnglish(textViewPrice.getText().toString()));
//                                double newTotal = price * (qty + 1);
//
//                                double originalDisc = 0;
//                                if (Double.parseDouble(convertToEnglish(textViewTotal.getText().toString())) != 0) {
//                                    originalDisc = lineDiscount.get(index) * 100 / Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
//                                } else {
//                                    originalDisc = 0;
//                                }
//                                double newDiscountValue = originalDisc * newTotal / 100;
//                                lineDiscount.set(index, newDiscountValue);
//
//                                textViewQty.setText("" + (qty + 1));
//                                textViewTotal.setText("" + newTotal);
//                                textViewLineDiscount.setText("" + newDiscountValue);
//                                calculateTotal();
//                            }
                                insertAndCheackItem(i);
                            }

                        } else
                            new Settings().makeText(Order.this, getResources().getString(R.string.no_item));

                    }
                });
            }
        } else
            itemGridView.setAdapter(null);
    }

    void insertItemRaw(Items item) {


//        motionEvent(item.getPic(),views,mo1,mo2);

        final TableRow row = new TableRow(Order.this);
        TextView textView=null;
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
        lp.setMargins(2, 0, 2, 0);
        row.setLayoutParams(lp);

        for (int i = 0; i < 5; i++) {
          textView = new TextView(Order.this);

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
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, 30, 1.0f);
                textView.setLayoutParams(lp1);
            } else {
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, 30, 0.00001f);
                textView.setLayoutParams(lp2);
            }

            if (i == 1) {
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                textView.setLayoutParams(lp1);
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

    void insertItemRawSplit(OrderTransactions item, TableLayout tableLayout, int origNSplit) {
        final TableRow row = new TableRow(Order.this);

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
        lp.setMargins(2, 0, 2, 0);
        row.setLayoutParams(lp);

        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(Order.this);
            EditText editText = new EditText(Order.this);


            switch (i) {
                case 0:
                    if (origNSplit == 0) {
                        textView.setText(" " + item.getQty());
                    } else if(origNSplit == 1) {
                        textView.setText("1");
                    }else {

                        textView.setText("0.0");
                    }

                    break;
                case 1:
                    textView.setText(item.getItemName());
                    break;
                case 2:
                    textView.setText("" + item.getPrice());
                    break;
                case 3:
                    if (origNSplit == 0) {
                        textView.setText("" + item.getTotal());
                    } else {
                        textView.setText("" + item.getPrice());

                    }
                    break;

                case 4:
                    textView.setText("0");

                    break;

//                case 4:
//                    if(origNSplit==0) {
//                         editText.setTextColor(ContextCompat.getColor(Order.this, R.color.text_color));
//                        editText.setGravity(Gravity.CENTER);
//                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//                        TableRow.LayoutParams lp2 =  new  TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//                        editText.setLayoutParams(lp2);
//                        editText.setText("0");
//                        row.addView(editText);
//                    }
//                    break;


            }

            textView.setTextColor(ContextCompat.getColor(Order.this, R.color.text_color));
            textView.setGravity(Gravity.CENTER);

            if (i != 4) {
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, 30, 1.0f);
                textView.setLayoutParams(lp1);
            } else {
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, 30, 0.00001f);
                textView.setLayoutParams(lp2);
            }

            if (i == 1) {
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                textView.setLayoutParams(lp1);
            }

            row.addView(textView);
            row.setTag("0");

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    focused = row;
                    setRawFocusedSplit(row, tableLayout);
                }
            });

        }
        tableLayout.addView(row);
//        tableLayoutPosition++;

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

    void setRawFocusedSplit(TableRow raw, TableLayout tableLayout) {
//        for (int k = 0; k < tableLayout.getChildCount(); k++) {
//            TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
//            tableRow.setBackgroundDrawable(null);
//        }

        if (!raw.getTag().toString().equals("1")) {
            raw.setTag("1");
            raw.setBackgroundColor(getResources().getColor(R.color.layer4));
        } else {
            raw.setTag("0");
            raw.setBackgroundDrawable(null);

        }


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
                new Settings().makeText(Order.this, getResources().getString(R.string.chooes_item_delete));
        } else {
            new Settings().makeText(Order.this, getResources().getString(R.string.no_item));
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
                    if (Double.parseDouble(convertToEnglish(voidQty.getText().toString())) <= Double.parseDouble(convertToEnglish(textViewQty.getText().toString()))) {
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
                                            Settings.user_no, Settings.user_name, reasonText, today, 1));
                                }

                                int index = Integer.parseInt(convertToEnglish(raw.getTag().toString()));

                                mDbHandler.addCancleOrder(new CancleOrder(voucherNo, today, Settings.user_name, Settings.user_no, Settings.shift_name,
                                        Settings.shift_number, waiter, Integer.parseInt(waiterNo), "" + wantedItems.get(index).getItemBarcode(),
                                        wantedItems.get(index).getMenuName(), Double.parseDouble(textViewQty.getText().toString()),
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

                                    double newQty = Double.parseDouble(convertToEnglish(textViewQty.getText().toString())) - Double.parseDouble(convertToEnglish(voidQty.getText().toString()));
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
                                new Settings().makeText(Order.this, getResources().getString(R.string.select_reson_cancele));
                        });


                        dialog1.show();
                    } else
                        new Settings().makeText(Order.this, getResources().getString(R.string.curent_qty_less_than) + voidQty.getText().toString());

                } else
                    new Settings().makeText(Order.this, getResources().getString(R.string.enter_qty));

                focused = null;
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
                            Settings.user_no, Settings.user_name, reasonText, today, 1));
                }

                for (int k = 0; k < tableLayout.getChildCount(); k++) {
                    TableRow raw = (TableRow) tableLayout.getChildAt(k);
                    TextView textViewQty = (TextView) raw.getChildAt(0);
                    TextView textViewTotal = (TextView) raw.getChildAt(3);

                    mDbHandler.addCancleOrder(new CancleOrder(voucherNo, today, Settings.user_name, Settings.user_no, Settings.shift_name,
                            Settings.shift_number, waiter, Integer.parseInt(waiterNo), "" + wantedItems.get(k).getItemBarcode(),
                            wantedItems.get(k).getMenuName(), Double.parseDouble(convertToEnglish(textViewQty.getText().toString())),
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
                new Settings().makeText(Order.this, getResources().getString(R.string.select_reson_cancele));

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

    void showSplitDialog() {

        Dialog dialogSplit = new Dialog(Order.this);
        dialogSplit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSplit.setCancelable(false);
        dialogSplit.setContentView(R.layout.split_dialog);
        dialogSplit.setCanceledOnTouchOutside(false);

        Button split, pay, cancel,noSplit_2;
        TableLayout originalTLayout, splitTLayout;

        split = (Button) dialogSplit.findViewById(R.id.split_);
        noSplit_2 = (Button) dialogSplit.findViewById(R.id.split_2);
        pay = (Button) dialogSplit.findViewById(R.id.pay_split);
        cancel = (Button) dialogSplit.findViewById(R.id.cancel_split);
        originalTLayout = (TableLayout) dialogSplit.findViewById(R.id.originalTLayout);
        splitTLayout = (TableLayout) dialogSplit.findViewById(R.id.splitTLayout);

        TextView total_original, delivery_original, lineDiscount_original, discount_original, subTotal_original, service_original, tax_original, amountDue_original;
        TextView total_split, delivery_split, lineDiscount_split, discount_split, subTotal_split, service_split, tax_split, amountDue_split;

        total_original = (TextView) dialogSplit.findViewById(R.id.total);
        delivery_original = (TextView) dialogSplit.findViewById(R.id.delivery);
        lineDiscount_original = (TextView) dialogSplit.findViewById(R.id.line_discount);
        discount_original = (TextView) dialogSplit.findViewById(R.id.discount);
        subTotal_original = (TextView) dialogSplit.findViewById(R.id.sub_total);
        service_original = (TextView) dialogSplit.findViewById(R.id.service);
        tax_original = (TextView) dialogSplit.findViewById(R.id.tax);
        amountDue_original = (TextView) dialogSplit.findViewById(R.id.amount_due);

        total_split = (TextView) dialogSplit.findViewById(R.id.total2);
        delivery_split = (TextView) dialogSplit.findViewById(R.id.delivery2);
        lineDiscount_split = (TextView) dialogSplit.findViewById(R.id.line_discount2);
        discount_split = (TextView) dialogSplit.findViewById(R.id.discount2);
        subTotal_split = (TextView) dialogSplit.findViewById(R.id.sub_total2);
        service_split = (TextView) dialogSplit.findViewById(R.id.service2);
        tax_split = (TextView) dialogSplit.findViewById(R.id.tax2);
        amountDue_split = (TextView) dialogSplit.findViewById(R.id.amount_due2);

        total_original.setText(total.getText().toString());
        delivery_original.setText(deliveryCharge.getText().toString());
        lineDiscount_original.setText(lineDisCount.getText().toString());
        discount_original.setText(disCount.getText().toString());
        subTotal_original.setText(subTotal.getText().toString());
        service_original.setText(service.getText().toString());
        tax_original.setText(tax.getText().toString());
        amountDue_original.setText(amountDue.getText().toString());

        index=0;
        index2=0;

        requestedItemsSplit.clear();
        requestedItemsSplit = mDbHandler.getAllRequestVoucherOrderTemp(voucherNo, "" + Settings.POS_number);
        String vouchersNo="-1";
        if(requestedItemsSplit.size()!=0) {
             vouchersNo = requestedItemsSplit.get(0).getVoucherNo();
        }

        for (int i = 0; i < requestedItemsSplit.size(); i++) {
            insertItemRawSplit(requestedItemsSplit.get(i), originalTLayout, 0);

        }


        split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findAndAddQty(originalTLayout, splitTLayout);
                List<String> originaltData = calculateSplit(originalTLayout, requestedItemsSplit);
                List<String> splitData = calculateSplit(splitTLayout, requestedItemsSplitTemp);

                total_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(0)))));
                delivery_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(1)))));
                lineDiscount_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(2)))));
                discount_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(3)))));
                subTotal_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(4)))));
                service_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(5)))));
                tax_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(6)))));
                amountDue_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(7)))));


                total_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(0)))));
                delivery_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(1)))));
                lineDiscount_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(2)))));
                discount_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(3)))));
                subTotal_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(4)))));
                service_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(5)))));
                tax_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(6)))));
                amountDue_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(7)))));


                Toast.makeText(Order.this, "split ...", Toast.LENGTH_SHORT).show();
            }
        });

        noSplit_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAndAddQtySplit(splitTLayout, originalTLayout);
                List<String> originaltData = calculateSplit(originalTLayout, requestedItemsSplit);
                List<String> splitData = calculateSplit(splitTLayout, requestedItemsSplitTemp);

                total_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(0)))));
                delivery_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(1)))));
                lineDiscount_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(2)))));
                discount_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(3)))));
                subTotal_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(4)))));
                service_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(5)))));
                tax_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(6)))));
                amountDue_original.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(originaltData.get(7)))));


                total_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(0)))));
                delivery_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(1)))));
                lineDiscount_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(2)))));
                discount_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(3)))));
                subTotal_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(4)))));
                service_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(5)))));
                tax_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(6)))));
                amountDue_split.setText(convertToEnglish(String.format("%.3f",Double.parseDouble(splitData.get(7)))));


                Toast.makeText(Order.this, "split ...", Toast.LENGTH_SHORT).show();
            }
        });


        String finalVouchersNo = vouchersNo;
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<OrderTransactions> original = new ArrayList<>();
                List<OrderTransactions> split = new ArrayList<>();
                List<MaxSerial> vhSerial = mDbHandler.getMaxSerialForVhf();
                OrderTransactionsSplit.clear();
                OrderTransactionsObj.clear();

                if (requestedItemsSplit.size() != 0) {
                if (requestedItemsSplitTemp.size() != 0& Double.parseDouble(amountDue_split.getText().toString())!=0& Double.parseDouble(amountDue_original.getText().toString())!=0) {

                    OrderHeaderObj = new OrderHeader(orderTypeFlag, 0,requestedItemsSplit.get(0).getVoucherDate(), requestedItemsSplit.get(0).getPosNo(), requestedItemsSplit.get(0).getStoreNo(),
                            "" + (Integer.parseInt(vhSerial.get(0).getMaxSerial()) + 1), 1, Double.parseDouble(convertToEnglish(total_split.getText().toString())), Double.parseDouble(convertToEnglish(lineDiscount_split.getText().toString())), Double.parseDouble(convertToEnglish(discount_split.getText().toString())), Double.parseDouble(convertToEnglish(lineDiscount_split.getText().toString())) + Double.parseDouble(convertToEnglish(discount_split.getText().toString())),
                            Settings.service_value, Double.parseDouble((convertToEnglish(tax_split.getText().toString()))), Double.parseDouble(convertToEnglish(service_split.getText().toString())), Double.parseDouble((convertToEnglish(subTotal_split.getText().toString()))),
                            Double.parseDouble(convertToEnglish(amountDue_split.getText().toString())), Double.parseDouble(convertToEnglish(delivery_split.getText().toString())),sectionNumber, tableNumber,
                             PayMethods.cashValue1, PayMethods.creditCardValue1, PayMethods.chequeValue1, PayMethods.creditValue1,
                            PayMethods.giftCardValue1, PayMethods.pointValue1, Settings.shift_name, Settings.shift_number, waiter, 0, Settings.user_name, Settings.user_no, time, "0", -1, Settings.cash_no, "noAdd");


                    for (int i = 0; i < splitTLayout.getChildCount(); i++) {
                        TableRow tableRow = (TableRow) splitTLayout.getChildAt(i);
                        TextView textViewQty = (TextView) tableRow.getChildAt(0);
                        TextView textViewItemName = (TextView) tableRow.getChildAt(1);
                        TextView textViewPrice = (TextView) tableRow.getChildAt(2);
                        TextView textViewTotal = (TextView) tableRow.getChildAt(3);
                        TextView textViewLineDiscount = (TextView) tableRow.getChildAt(4);

                        OrderTransactionsObj.add(requestedItemsSplitTemp.get(i));
                        OrderTransactionsObj.get(i).setItemName(textViewItemName.getText().toString());
                        OrderTransactionsObj.get(i).setTotal(Double.parseDouble(convertToEnglish(textViewTotal.getText().toString())));
                        OrderTransactionsObj.get(i).setQty(Double.parseDouble(convertToEnglish(textViewQty.getText().toString())));
                        OrderTransactionsObj.get(i).setPrice(Double.parseDouble(convertToEnglish(textViewPrice.getText().toString())));
                        OrderTransactionsObj.get(i).setlDiscount(Double.parseDouble(convertToEnglish(textViewLineDiscount.getText().toString())));

                        OrderTransactionsObj.get(i).setVoucherNo("" + (Integer.parseInt(vhSerial.get(0).getMaxSerial()) + 1));
                        OrderTransactionsObj.get(i).setVoucherSerial(i+1);
                        OrderTransactionsObj.get(i).setServiceTax(Double.parseDouble(convertToEnglish(service_split.getText().toString())));
                        OrderTransactionsObj.get(i).setService(Double.parseDouble(convertToEnglish(service_split.getText().toString())));
                        OrderTransactionsObj.get(i).setTotalDiscount(Double.parseDouble(convertToEnglish(textViewLineDiscount.getText().toString())));
                        OrderTransactionsObj.get(i).setTaxValue(Double.parseDouble((convertToEnglish(tax_split.getText().toString()))));

                    }


                    orderHeaderSplit = new OrderHeader(orderTypeFlag, 0,  requestedItemsSplit.get(0).getVoucherDate(), requestedItemsSplit.get(0).getPosNo(), requestedItemsSplit.get(0).getStoreNo(),
                            finalVouchersNo, requestedItemsSplit.get(0).getVoucherSerial(), Double.parseDouble(convertToEnglish(total_original.getText().toString())), Double.parseDouble(convertToEnglish(lineDiscount_original.getText().toString())), Double.parseDouble(convertToEnglish(discount_original.getText().toString())), Double.parseDouble(convertToEnglish(lineDiscount_original.getText().toString())) + Double.parseDouble(convertToEnglish(discount_original.getText().toString())),
                            Settings.service_value, Double.parseDouble((convertToEnglish(tax_original.getText().toString()))), Double.parseDouble(convertToEnglish(service_original.getText().toString())), Double.parseDouble((convertToEnglish(subTotal_original.getText().toString()))),
                            Double.parseDouble(convertToEnglish(amountDue_original.getText().toString())), Double.parseDouble(convertToEnglish(delivery_original.getText().toString())), requestedItemsSplit.get(0).getSectionNo(), requestedItemsSplit.get(0).getTableNo(),
                            PayMethods.cashValue1, PayMethods.creditCardValue1, PayMethods.chequeValue1, PayMethods.creditValue1,
                            PayMethods.giftCardValue1, PayMethods.pointValue1, Settings.shift_name, Settings.shift_number, waiter, 0, Settings.user_name, Settings.user_no, time, "0", -1, Settings.cash_no, "noAdd");


                    for (int i = 0; i < originalTLayout.getChildCount(); i++) {
                        TableRow tableRow = (TableRow) originalTLayout.getChildAt(i);
                        TextView textViewQty = (TextView) tableRow.getChildAt(0);
                        TextView textViewItemName = (TextView) tableRow.getChildAt(1);
                        TextView textViewPrice = (TextView) tableRow.getChildAt(2);
                        TextView textViewTotal = (TextView) tableRow.getChildAt(3);
                        TextView textViewLineDiscount = (TextView) tableRow.getChildAt(4);


                        OrderTransactionsSplit.add(requestedItemsSplit.get(i));
                        Log.e("OrderTransactions1","Splittemp "+requestedItemsSplit.get(i).getVoucherNo());
                        OrderTransactionsSplit.get(i).setVoucherNo(finalVouchersNo);

                        Log.e("OrderTransactions2","Splittemp "+ OrderTransactionsSplit.get(i).getVoucherNo());
                        OrderTransactionsSplit.get(i).setItemName(textViewItemName.getText().toString());
                        OrderTransactionsSplit.get(i).setTotal(Double.parseDouble(convertToEnglish(textViewTotal.getText().toString())));
                        OrderTransactionsSplit.get(i).setQty(Double.parseDouble(convertToEnglish(textViewQty.getText().toString())));
                        OrderTransactionsSplit.get(i).setPrice(Double.parseDouble(convertToEnglish(textViewPrice.getText().toString())));
                        OrderTransactionsSplit.get(i).setlDiscount(Double.parseDouble(convertToEnglish(textViewLineDiscount.getText().toString())));
                        OrderTransactionsSplit.get(i).setServiceTax(Double.parseDouble(convertToEnglish(service_original.getText().toString())));
                        OrderTransactionsSplit.get(i).setService(Double.parseDouble(convertToEnglish(service_original.getText().toString())));
                        OrderTransactionsSplit.get(i).setTotalDiscount(Double.parseDouble(convertToEnglish(textViewLineDiscount.getText().toString())));
                        OrderTransactionsSplit.get(i).setTaxValue(Double.parseDouble((convertToEnglish(tax_original.getText().toString()))));

                    }

                    Intent intentPay = new Intent(Order.this, PayMethods.class);
                    startActivity(intentPay);

                } else {
                    Toast.makeText(Order.this, "Amount Due = 0.0 ", Toast.LENGTH_SHORT).show();
                }
            }else{
                    Toast.makeText(Order.this, "Please Check out this table can not split  ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSplit.dismiss();
                requestedItemsSplit.clear();
                requestedItemsSplitTemp.clear();
            }
        });


        dialogSplit.show();

    }


    public OrderHeader updateOrderHeaderTempSplit(){

        return orderHeaderSplit;

    }
   public List<OrderTransactions> updateOrderTransactionTempSplit(){

        return OrderTransactionsSplit;
    }


    void findAndAddQty(TableLayout tableLayout1, TableLayout tableLayout2) {


        for (int i = 0; i < tableLayout1.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout1.getChildAt(i);
            TextView textViewORGqTY = (TextView) tableRow.getChildAt(0);
            TextView textViewOrgItemName = (TextView) tableRow.getChildAt(1);
            TextView textViewPrices = (TextView) tableRow.getChildAt(2);

            TextView textViewTotal = (TextView) tableRow.getChildAt(3);

            if(!textViewOrgItemName.getText().toString().contains("*")){
            if (tableLayout2.getChildCount() != 0) {
                if (tableRow.getTag().toString().equals("1")) {
                    boolean isFound = false;
                    TextView QtyText = null;
                    TextView textPrice = null;
                    TextView textTotal = null;

                    for (int q = 0; q < tableLayout2.getChildCount(); q++) {
                        TableRow tableRows = (TableRow) tableLayout2.getChildAt(q);
                        TextView textViewQty = (TextView) tableRows.getChildAt(0);
                        TextView textViewItemName = (TextView) tableRows.getChildAt(1);
                        TextView textViewPrice = (TextView) tableRows.getChildAt(2);
                        TextView textViewTotalS = (TextView) tableRows.getChildAt(3);

                        isFound = false;

                        if (textViewItemName.getText().toString().equals(textViewOrgItemName.getText().toString())) {
                            isFound = true;
                            QtyText = textViewQty;
                            textPrice = textViewPrice;
                            textTotal = textViewTotalS;
                            break;
                        }
                    }

                    if (isFound) {

                        double orgQty = Double.parseDouble(convertToEnglish(textViewORGqTY.getText().toString())) - 1;
                        double splitQty = Double.parseDouble(convertToEnglish(QtyText.getText().toString())) + 1;
                        QtyText.setText("" + (splitQty));
                        textViewORGqTY.setText("" + (orgQty));

                        double orgTotal = orgQty * Double.parseDouble(convertToEnglish(textViewPrices.getText().toString()));
                        double splitTotal = splitQty * Double.parseDouble(convertToEnglish(textPrice.getText().toString()));

                        textTotal.setText("" + splitTotal);
                        textViewTotal.setText("" + orgTotal);

                        if (orgQty == 0) {

                            if(tableLayout1.getChildCount()-1>i){
                                TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                                TextView textViewQtyq = (TextView) table.getChildAt(0);
                                TextView textViewItemNameq = (TextView) table.getChildAt(1);
                                if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                                    requestedItemsSplit.remove(i+1);
                                    tableLayout1.removeView(table);
                                }
                            }

                            tableLayout1.removeView(tableRow);
                            requestedItemsSplit.remove(i);

                        }

                    } else {

                        requestedItemsSplitTemp.add(requestedItemsSplit.get(i));
                        insertItemRawSplit(requestedItemsSplitTemp.get(requestedItemsSplitTemp.size()-1), tableLayout2, 1);

                        if(tableLayout1.getChildCount()-1>i){
                            TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                            TextView textViewQtyq = (TextView) table.getChildAt(0);
                            TextView textViewItemNameq = (TextView) table.getChildAt(1);
                            if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                                requestedItemsSplitTemp.add(requestedItemsSplit.get(i+1));
                                insertItemRawSplit(requestedItemsSplitTemp.get(requestedItemsSplitTemp.size() - 1), tableLayout2, 2);
                            }
                        }


                        double orgQty = Double.parseDouble(convertToEnglish(textViewORGqTY.getText().toString())) - 1;
                        textViewORGqTY.setText("" + (orgQty));

                        double orgTotal = orgQty * Double.parseDouble(convertToEnglish(textViewPrices.getText().toString()));

                        textViewTotal.setText("" + orgTotal);

                        if (orgQty == 0) {
                            if(tableLayout1.getChildCount()-1>i){
                                TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                                TextView textViewQtyq = (TextView) table.getChildAt(0);
                                TextView textViewItemNameq = (TextView) table.getChildAt(1);
                                if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                                    requestedItemsSplit.remove(i+1);
                                    tableLayout1.removeView(table);
                                }
                            }
                            tableLayout1.removeView(tableRow);
                            requestedItemsSplit.remove(i);

                        }


                    }


                }

            } else {

                if (tableRow.getTag().toString().equals("1")) {
                    requestedItemsSplitTemp.add(requestedItemsSplit.get(i));
                    insertItemRawSplit(requestedItemsSplitTemp.get(requestedItemsSplitTemp.size()-1), tableLayout2, 1);

                    if(tableLayout1.getChildCount()-1>i){
                        TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                        TextView textViewQtyq = (TextView) table.getChildAt(0);
                        TextView textViewItemNameq = (TextView) table.getChildAt(1);
                        if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                            requestedItemsSplitTemp.add(requestedItemsSplit.get(i+1));
                            insertItemRawSplit(requestedItemsSplitTemp.get(requestedItemsSplitTemp.size() - 1), tableLayout2, 2);
                        }
                    }


                    double orgQty = Double.parseDouble(convertToEnglish(textViewORGqTY.getText().toString())) - 1;
                    textViewORGqTY.setText("" + (orgQty));

                    double orgTotal = orgQty * Double.parseDouble(convertToEnglish(textViewPrices.getText().toString()));
                    double splitTotal = 1 * (requestedItemsSplitTemp.get(index).getPrice());

                    textViewTotal.setText("" + orgTotal);

                    if (orgQty == 0) {

                        if(tableLayout1.getChildCount()-1>i){
                            TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                            TextView textViewQtyq = (TextView) table.getChildAt(0);
                            TextView textViewItemNameq = (TextView) table.getChildAt(1);
                            if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                                requestedItemsSplit.remove(i+1);
                                tableLayout1.removeView(table);
                            }
                        }

                        tableLayout1.removeView(tableRow);

                        requestedItemsSplit.remove(i);



                    }
                }
            }
            }else{

//                if (tableRow.getTag().toString().equals("1")) {
//                    requestedItemsSplitTemp.add(requestedItemsSplit.get(i));
//                    insertItemRawSplit(requestedItemsSplitTemp.get(requestedItemsSplitTemp.size()-1), tableLayout2, 0);
//                        tableLayout1.removeView(tableRow);
//                        requestedItemsSplit.remove(i);
//
//                }

            }

        }

    }

    void findAndAddQtySplit(TableLayout tableLayout1, TableLayout tableLayout2) {


        for (int i = 0; i < tableLayout1.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout1.getChildAt(i);
            TextView textViewORGqTY = (TextView) tableRow.getChildAt(0);
            TextView textViewOrgItemName = (TextView) tableRow.getChildAt(1);
            TextView textViewPrices = (TextView) tableRow.getChildAt(2);

            TextView textViewTotal = (TextView) tableRow.getChildAt(3);
            if(!textViewOrgItemName.getText().toString().contains("*")){
            if (tableLayout2.getChildCount() != 0) {
                if (tableRow.getTag().toString().equals("1")) {
                    boolean isFound = false;
                    TextView QtyText = null;
                    TextView textPrice = null;
                    TextView textTotal = null;

                    for (int q = 0; q < tableLayout2.getChildCount(); q++) {
                        TableRow tableRows = (TableRow) tableLayout2.getChildAt(q);
                        TextView textViewQty = (TextView) tableRows.getChildAt(0);
                        TextView textViewItemName = (TextView) tableRows.getChildAt(1);
                        TextView textViewPrice = (TextView) tableRows.getChildAt(2);
                        TextView textViewTotalS = (TextView) tableRows.getChildAt(3);

                        isFound = false;

                        if (textViewItemName.getText().toString().equals(textViewOrgItemName.getText().toString())) {
                            isFound = true;
                            QtyText = textViewQty;
                            textPrice = textViewPrice;
                            textTotal = textViewTotalS;
                            break;
                        }
                    }

                    if (isFound) {

                        double orgQty = Double.parseDouble(convertToEnglish(textViewORGqTY.getText().toString())) - 1;
                        double splitQty = Double.parseDouble(convertToEnglish(QtyText.getText().toString())) + 1;
                        QtyText.setText("" + (splitQty));
                        textViewORGqTY.setText("" + (orgQty));

                        double orgTotal = orgQty * Double.parseDouble(convertToEnglish(textViewPrices.getText().toString()));
                        double splitTotal = splitQty * Double.parseDouble(convertToEnglish(textPrice.getText().toString()));

                        textTotal.setText("" + splitTotal);
                        textViewTotal.setText("" + orgTotal);

                        if (orgQty == 0) {

                            if(tableLayout1.getChildCount()-1>i){
                                TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                                TextView textViewQtyq = (TextView) table.getChildAt(0);
                                TextView textViewItemNameq = (TextView) table.getChildAt(1);
                                if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                                requestedItemsSplitTemp.remove(i+1);
                                tableLayout1.removeView(table);
                                }
                            }

                            tableLayout1.removeView(tableRow);
                            requestedItemsSplitTemp.remove(i);

                        }

                    } else {


                        requestedItemsSplit.add(requestedItemsSplitTemp.get(i));
                        insertItemRawSplit(requestedItemsSplit.get(requestedItemsSplit.size() - 1), tableLayout2, 1);

                        if(tableLayout1.getChildCount()-1>i){
                            TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                            TextView textViewQtyq = (TextView) table.getChildAt(0);
                            TextView textViewItemNameq = (TextView) table.getChildAt(1);
                            if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                                requestedItemsSplit.add(requestedItemsSplitTemp.get(i+1));
                                insertItemRawSplit(requestedItemsSplit.get(requestedItemsSplit.size() - 1), tableLayout2, 2);
                            }
                        }

                        double orgQty = Double.parseDouble(convertToEnglish(textViewORGqTY.getText().toString())) - 1;
                        textViewORGqTY.setText("" + (orgQty));

                        double orgTotal = orgQty * Double.parseDouble(convertToEnglish(textViewPrices.getText().toString()));

                        textViewTotal.setText("" + orgTotal);

                        if (orgQty == 0) {

                            if(tableLayout1.getChildCount()-1>i){
                                TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                                TextView textViewQtyq = (TextView) table.getChildAt(0);
                                TextView textViewItemNameq = (TextView) table.getChildAt(1);
                                if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                                    requestedItemsSplitTemp.remove(i+1);
                                    tableLayout1.removeView(table);
                                }
                            }

                            tableLayout1.removeView(tableRow);
                            requestedItemsSplitTemp.remove(i);


                        }

                    }


                }

            } else {

                if (tableRow.getTag().toString().equals("1")) {
                    requestedItemsSplit.add(requestedItemsSplitTemp.get(i));
                    insertItemRawSplit(requestedItemsSplit.get(requestedItemsSplit.size() - 1), tableLayout2, 1);

                    if(tableLayout1.getChildCount()-1>i){
                        TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                        TextView textViewQtyq = (TextView) table.getChildAt(0);
                        TextView textViewItemNameq = (TextView) table.getChildAt(1);
                        if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                            requestedItemsSplit.add(requestedItemsSplitTemp.get(i+1));
                            insertItemRawSplit(requestedItemsSplit.get(requestedItemsSplit.size() - 1), tableLayout2, 2);
                        }
                    }

                    double orgQty = Double.parseDouble(convertToEnglish(textViewORGqTY.getText().toString())) - 1;
                    textViewORGqTY.setText("" + (orgQty));

                    double orgTotal = orgQty * Double.parseDouble(convertToEnglish(textViewPrices.getText().toString()));
                    double splitTotal = 1 * (requestedItemsSplit.get(index2).getPrice());

                    textViewTotal.setText("" + orgTotal);

                    if (orgQty == 0) {

                        if(tableLayout1.getChildCount()-1>i){
                            TableRow table= (TableRow) tableLayout1.getChildAt(i+1);
                            TextView textViewQtyq = (TextView) table.getChildAt(0);
                            TextView textViewItemNameq = (TextView) table.getChildAt(1);
                            if(Double.parseDouble(textViewQtyq.getText().toString())==0&textViewItemNameq.getText().toString().contains("*")){
                                requestedItemsSplitTemp.remove(i+1);
                                tableLayout1.removeView(table);
                            }
                        }
                        tableLayout1.removeView(tableRow);
                        requestedItemsSplitTemp.remove(i);
                    }


                }
            }
        }else{

//            if (tableRow.getTag().toString().equals("1")) {
//                requestedItemsSplit.add(requestedItemsSplitTemp.get(i));
//                insertItemRawSplit(requestedItemsSplit.get(requestedItemsSplit.size()-1), tableLayout2, 0);
//                tableLayout1.removeView(tableRow);
//                requestedItemsSplitTemp.remove(i);
//
//            }

        }

        }

    }


    List<String> calculateSplit(TableLayout originalTableLayout, List<OrderTransactions> orderTransactions) {

        double total = 0, delivery = 0, lDiscount = 0, discount = 0, subTotal = 0, service = 0, tax = 0, amountDue = 0,totalItemsTaxInclude=0,totalItemsTaxExclude=0;
        List<String> originalData = new ArrayList<>();
        List<String> SplitData = new ArrayList<>();
        for (int i = 0; i < originalTableLayout.getChildCount(); i++) {

            TableRow tableRow = (TableRow) originalTableLayout.getChildAt(i);
            TextView textViewORGqTY = (TextView) tableRow.getChildAt(0);
            TextView textViewOrgItemName = (TextView) tableRow.getChildAt(1);
            TextView textViewPrices = (TextView) tableRow.getChildAt(2);
            TextView textViewTotal = (TextView) tableRow.getChildAt(3);
            TextView textViewlDisc = (TextView) tableRow.getChildAt(4);

            if(orderTransactions.get(i).getQty()!=0) {
                double lDisc = 0;
                lDisc = (orderTransactions.get(i).getlDiscount() / orderTransactions.get(i).getQty()) * Double.parseDouble(convertToEnglish(textViewORGqTY.getText().toString()));
                textViewlDisc.setText("" + lDisc);

                double Disc = 0;
                Disc = (orderTransactions.get(i).getDiscount() / orderTransactions.get(i).getQty()) * Double.parseDouble(convertToEnglish(textViewORGqTY.getText().toString()));


                total += Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
//            delivery+=Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
                lDiscount += lDisc;
                discount += Disc;

               double totalLineAfterDisc=total-(Disc+lDisc);

//            service+=Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));

                if((orderTransactions.get(i).getTotal()!=0)) {
                    tax += (orderTransactions.get(i).getTaxValue() / (orderTransactions.get(i).getTotal() - (orderTransactions.get(i).getTotalDiscount()))) * totalLineAfterDisc;
                }
            }
        }

        subTotal = total + delivery - (discount + lDiscount);

        if (Settings.tax_type != 0) {
            amountDue += subTotal  + service;
                } else {
            amountDue += subTotal + tax + service;
                  }




        originalData.add("" + total);
        originalData.add("" + delivery);
        originalData.add("" + lDiscount);
        originalData.add("" + discount);
        originalData.add("" + subTotal);
        originalData.add("" + service);
        originalData.add("" + tax);
        originalData.add("" + amountDue);


        return originalData;

    }


    void showOpenPriceDilaog(int mainIndex) {

        dialog = new Dialog(Order.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.open_price_dialog);
        dialog.setCanceledOnTouchOutside(false);

        EditText price, newName;
        Button save, cancel;

        price = (EditText) dialog.findViewById(R.id.price);
        newName = (EditText) dialog.findViewById(R.id.newName);

        save = (Button) dialog.findViewById(R.id.save);
        cancel = (Button) dialog.findViewById(R.id.cancel);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!price.getText().toString().equals("")) {

                    requestedItems.get(mainIndex).setPrice(Double.parseDouble(price.getText().toString()));
                    if (!newName.getText().toString().equals("")) {
                        requestedItems.get(mainIndex).setMenuName(newName.getText().toString());
                    }

                    insertAndCheackItem(mainIndex);
                    dialog.dismiss();

                } else {
                    Toast.makeText(Order.this, " please Enter Price ", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    void insertAndCheackItem(int mainIndex) {


        Log.e("openprice sar", "" + "    " + requestedItems.get(mainIndex).getPrice() + "///////////" + requestedItems.get(mainIndex).getMenuName());

        boolean exist = false;
        int index = 0;
        for (int k = 0; k < tableLayout.getChildCount(); k++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(k);
            TextView textViewName = (TextView) tableRow.getChildAt(1);
            if (textViewName.getText().toString().equals(requestedItems.get(mainIndex).getMenuName())) {
                exist = true;
                index = k;
                break;
            }
        }


        if (!exist) {
            ArrayList<ItemWithFq> questions = mDbHandler.getItemWithFqs(requestedItems.get(mainIndex).itemBarcode);
            if (questions.size() == 0) {
                wantedItems.add(requestedItems.get(mainIndex));
                lineDiscount.add(0.0);
                insertItemRaw(requestedItems.get(mainIndex));
            } else {
                wantedItems.add(requestedItems.get(mainIndex));
                lineDiscount.add(0.0);
                insertItemRaw(requestedItems.get(mainIndex));
                showForceQuestionDialog(requestedItems.get(mainIndex).itemBarcode, 0);
            }
        } else {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(index);
            TextView textViewQty = (TextView) tableRow.getChildAt(0);
            TextView textViewPrice = (TextView) tableRow.getChildAt(2);
            TextView textViewTotal = (TextView) tableRow.getChildAt(3);
            TextView textViewLineDiscount = (TextView) tableRow.getChildAt(4);

            double qty = Double.parseDouble(convertToEnglish(textViewQty.getText().toString()));
            double price = Double.parseDouble(convertToEnglish(textViewPrice.getText().toString()));
            double newTotal = price * (qty + 1);

            double originalDisc = 0;
            if (Double.parseDouble(convertToEnglish(textViewTotal.getText().toString())) != 0) {
                originalDisc = lineDiscount.get(index) * 100 / Double.parseDouble(convertToEnglish(textViewTotal.getText().toString()));
            } else {
                originalDisc = 0;
            }
            double newDiscountValue = originalDisc * newTotal / 100;
            lineDiscount.set(index, newDiscountValue);

            textViewQty.setText("" + (qty + 1));
            textViewTotal.setText("" + newTotal);
            textViewLineDiscount.setText("" + newDiscountValue);
            calculateTotal();



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
if(questions.size()!=0) {
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
    } else {
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
    }
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
                    new Settings().makeText(Order.this, getResources().getString(R.string.selected_answer));
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
                    new Settings().makeText(Order.this, getResources().getString(R.string.selected_answer));
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
                    new Settings().makeText(Order.this, getResources().getString(R.string.selected_answer));
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
                    new Settings().makeText(Order.this, getResources().getString(R.string.selected_answer));
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
                            new Settings().makeText(Order.this, getResources().getString(R.string.select_qty));
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
                                    new Settings().makeText(Order.this, getResources().getString(R.string.select_qty));
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
                            new Settings().makeText(Order.this, getResources().getString(R.string.select_modifer));
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
                            new Settings().makeText(Order.this, getResources().getString(R.string.select_modifer));
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
                            new Settings().makeText(Order.this, getResources().getString(R.string.select_modifer));
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
                            new Settings().makeText(Order.this, getResources().getString(R.string.select_modifer));
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < modifiersName.size(); i++) {
                            if (modifiersName.get(i).contains("*")) {
                                insertModifierRaw(modifiersName.get(i).substring(modifiersName.get(i).indexOf('-') + 1,
                                        modifiersName.get(i).indexOf('-') + 10) + "..");

                                String modName = modifiersName.get(i).substring(modifiersName.get(i).indexOf('-') + 1, modifiersName.get(i).length() - modifiersName.get(i).indexOf('-') + 1);
                                wantedItems.add(Integer.parseInt(focused.getTag().toString()) + 1,
                                        new Items("modifier", modName, "", 0, 0, "",
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

                new Settings().makeText(Order.this, getResources().getString(R.string.chooes_item_to_modifier));
            }
        } else

            new Settings().makeText(Order.this, getResources().getString(R.string.no_item));

    }

    void showDeliveryChangeDialog() {
        if (wantedItems.size() != 0) {
            dialog = new Dialog(Order.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.delivery_change_dialog);
            dialog.setCanceledOnTouchOutside(true);

//            Window window = dialog.getWindow();
//            window.setLayout(460, 220);

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
                        new Settings().makeText(Order.this, getResources().getString(R.string.enter_dekivery));
                    }
                }
            });
            dialog.show();
        } else {
            new Settings().makeText(Order.this, getResources().getString(R.string.delivary_is_not_avilable));
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

                                new Settings().makeText(Order.this, getResources().getString(R.string.enter_line_discount));
                            }
                        }
                    });
                    dialog.show();
                } else

                    new Settings().makeText(Order.this, getResources().getString(R.string.discount_not_avilable));
            } else

                new Settings().makeText(Order.this, getResources().getString(R.string.chooes_item_to_add_linediscount));
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
                        new Settings().makeText(Order.this, getResources().getString(R.string.enter_discount));
                    }
                }
            });
            dialog.show();
        } else
            new Settings().makeText(Order.this, getResources().getString(R.string.discount_not_avilable_for_curent_item));
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
                    if (Settings.tax_type != 0) {
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

            double taxValue = 0;
            if (wantedItems.get(k).getTaxType() == 0) {
                if (Settings.tax_type != 0) {
                    taxValue = (totalLine - lineDiscount_ - discount) * wantedItems.get(k).getTax() / 100;
                    Log.e("tax__1", "((" + totalLine + "-" + discount + ")*" + wantedItems.get(k).getTax() + "/100)");
                } else {
                    taxValue = ((totalLine - lineDiscount_ - discount) * wantedItems.get(k).getTax() / 100) / (1 + (wantedItems.get(k).getTax() / 100));

                    Log.e("tax__2", "((" + totalLine + "-" + discount + ")*" + wantedItems.get(k).getTax() + "/100)/(1+" + wantedItems.get(k).getTax() + "/100)");
                }
            }
            Log.e("tax ", " = " + taxValue);
            OrderTransactionsObj.add(new OrderTransactions(orderTypeFlag, 0, today, Settings.POS_number, Settings.store_number,
                    voucherNo, k, "" + wantedItems.get(k).getItemBarcode(), wantedItems.get(k).getMenuName(),
                    wantedItems.get(k).getSecondaryName(), wantedItems.get(k).getKitchenAlias(), wantedItems.get(k).getMenuCategory(),
                    wantedItems.get(k).getFamilyName(), Double.parseDouble(convertToEnglish(textViewQty.getText().toString())), wantedItems.get(k).getPrice(),
                    totalLine, discount, lineDiscount_, discount + lineDiscount_, taxValue,
                    wantedItems.get(k).getTax(), 0, Double.parseDouble(service.getText().toString()), serviceTax,
                    tableNumber, sectionNumber, Settings.shift_number, Settings.shift_name, Settings.user_no, Settings.user_name, time, "0", -1, 0, Settings.cash_no));
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
                PayMethods.giftCardValue1, PayMethods.pointValue1, Settings.shift_name, Settings.shift_number, "No Waiter", 0, Settings.user_name, Settings.user_no, time, "0", -1, Settings.cash_no, "noAdd");


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

            double taxValue = 0;
            if (wantedItems.get(k).getTaxType() == 0) {
                if (Settings.tax_type != 0) {
                    taxValue = (totalLine - discount) * wantedItems.get(k).getTax() / 100;
                } else
                    taxValue = ((totalLine - discount) * wantedItems.get(k).getTax() / 100) / (1 + (wantedItems.get(k).getTax() / 100));
            }

            mDbHandler.addOrderTransactionTemp(new OrderTransactions(orderTypeFlag, 0, today, Settings.POS_number, Settings.store_number,
                    voucherNo, k, "" + wantedItems.get(k).getItemBarcode(), wantedItems.get(k).getMenuName(),
                    wantedItems.get(k).getSecondaryName(), wantedItems.get(k).getKitchenAlias(), wantedItems.get(k).getMenuCategory(),
                    wantedItems.get(k).getFamilyName(), Double.parseDouble(convertToEnglish(textViewQty.getText().toString())), wantedItems.get(k).getPrice(),
                    totalLine, discount, lineDiscount_, discount + lineDiscount_, taxValue,
                    wantedItems.get(k).getTax(), 0, Double.parseDouble(convertToEnglish(service.getText().toString())), serviceTax,
                    tableNumber, sectionNumber, Settings.shift_number, Settings.shift_name, Settings.user_no, Settings.user_name, time, "0", -1, 0, Settings.cash_no));
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
                0.00, 0.00, Settings.shift_name, Settings.shift_number, waiter, seatNo, Settings.user_name, Settings.user_no, time, "0", -1, Settings.cash_no, "noAdd"));
    }

//    void sendToKitchen() {
//        try {
//            JSONObject obj1 = OrderHeaderObj.getJSONObject();
//
//            List<ItemWithScreen> itemWithScreens = mDbHandler.getAllItemsWithScreen();
//            for (int i = 0; i < OrderTransactionsObj.size(); i++){
//                for (int j = 0; j < itemWithScreens.size(); j++){
//                    if(OrderTransactionsObj.get(i).getItemBarcode().equals(""+itemWithScreens.get(j).getItemCode()))
//                        OrderTransactionsObj.get(i).setScreenNo(itemWithScreens.get(j).getScreenNo());
//                }
//                OrderTransactionsObj.get(i).setNote("");
//            }
//
//            for (int i = 0; i < OrderTransactionsObj.size(); i++) {
//                if(OrderTransactionsObj.get(i).getQty() == 0) {
//                    OrderTransactionsObj.get(i - 1).setNote((OrderTransactionsObj.get(i-1).getNote()) + "\n" + OrderTransactionsObj.get(i).getItemName());
//                    OrderTransactionsObj.remove(i);
//                    i--;
//                }
//            }
//
//            JSONArray obj2 = new JSONArray();
//            for (int i = 0; i < OrderTransactionsObj.size(); i++)
//                obj2.put(i ,OrderTransactionsObj.get(i).getJSONObject());
//
//            JSONObject obj = new JSONObject();
//            obj.put("Items", obj2);
//            obj.put("Header", obj1);
//
//            Log.e("socket", "J");
//            SendSocket sendSocket = new SendSocket(Order.this, obj1,OrderTransactionsObj);
//            sendSocket.sendMessage();
//
//
//            Log.e("sendCloud", "J");
//            SendCloud sendCloud = new SendCloud(Order.this, obj);
//            sendCloud.startSending("kitchen");
//
//
//        } catch (JSONException e) {
//            Log.e("Tag", "JSONException");
//        }
//    }

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
                    TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, 30, 1.0f);
                    textView.setLayoutParams(lp1);
                } else {
                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, 30, 0.00001f);
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
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void setSlideAnimation() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    @SuppressLint("ClickableViewAccessibility")
    void initialize() {

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        tableItem = (TableLayout) findViewById(R.id.tableItem);
        tableDetail = (TableLayout) findViewById(R.id.tableDetal);

        itemGridView = (GridView) findViewById(R.id.GridViewItems);
        catGridView = (GridView) findViewById(R.id.GridViewCats);
        orderType = (TextView) findViewById(R.id.orderType);
        tableNo = (TextView) findViewById(R.id.tableNumber);
        check = (TextView) findViewById(R.id.checkNumber);
        date = (TextView) findViewById(R.id.date);
        user = (TextView) findViewById(R.id.user);
        seats = (TextView) findViewById(R.id.seat_number);
        details = (TextView) findViewById(R.id.details);
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
        baLiner = (LinearLayout) findViewById(R.id.baLiner);

        total = (TextView) findViewById(R.id.total);
        disCount = (TextView) findViewById(R.id.discount);
        lineDisCount = (TextView) findViewById(R.id.line_discount);
        deliveryCharge = (TextView) findViewById(R.id.delivery);
        subTotal = (TextView) findViewById(R.id.sub_total);
        tax = (TextView) findViewById(R.id.tax);
        service = (TextView) findViewById(R.id.service);
        amountDue = (TextView) findViewById(R.id.amount_due);
        vhSerial = (TextView) findViewById(R.id.vhSerial);
        onOffLine = (TextView) findViewById(R.id.onOffLine);
        pay.setOnTouchListener(onTouchListener);
        order.setOnTouchListener(onTouchListener);
        modifier.setOnTouchListener(onTouchListener);
        void_.setOnTouchListener(onTouchListener);
        deliveryCharge.setOnTouchListener(onTouchListener);
        split.setOnTouchListener(onTouchListener);
        discount.setOnTouchListener(onTouchListener);
        priceChange.setOnTouchListener(onTouchListener);

        split.setOnClickListener(onClickListener);
        details.setOnClickListener(onClickListener);
        pay.setOnClickListener(onClickListener);
        order.setOnClickListener(onClickListener);
        modifier.setOnClickListener(onClickListener);
        void_.setOnClickListener(onClickListener);
        delivery.setOnClickListener(onClickListener);
        discount.setOnClickListener(onClickListener);
        lDiscount.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        orderType.setOnClickListener(onClickListener);
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        String result = Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }


    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


}
