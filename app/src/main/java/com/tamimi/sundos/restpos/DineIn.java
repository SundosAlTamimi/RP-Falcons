package com.tamimi.sundos.restpos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.tamimi.sundos.restpos.Models.Cashier;
import com.tamimi.sundos.restpos.Models.EmployeeRegistrationModle;
import com.tamimi.sundos.restpos.Models.Money;
import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.PayMethod;
import com.tamimi.sundos.restpos.Models.TableActions;
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
    TextView move, merge, reservation, takeAway, close, cashDrawer,  checkOut, reprint;
    LinearLayout add, rightBorder;
    ViewGroup land;
    TextView focusedTextView;
    LinearLayout focused = null;
    boolean CheckTrue = true;
    int tableNumber;
    int current = 0;
    String waiter, waiterNo;
    boolean waiterClick = false;
    int fromSection, toSection;
    List<String> tablesNoLeft, tablesNoRight;
    int focusedLeft = -1, focusedRight = -1;

    TableRow rows;
    public String today, time;
    List<OrderTransactions> greenTables;
    DatabaseHandler mHandler;

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

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today = convertToEnglish(df.format(currentTimeAndDate));

        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");
        time = convertToEnglish(dfTime.format(currentTimeAndDate));

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

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(60, 20);
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
                waiterClick = false;
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
                                waiter =  convertToEnglish(textView.getText().toString());
                                waiterNo = convertToEnglish(textView2.getText().toString());
                                waiterClick = true;
                                setTableBackground(linearLayout, linearLayout1);
                            }
                        });

                        linearLayout.addView(linearLayout1);
                    }
                }
                done.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (linearLayout.getChildCount() != 0 && waiterClick) {
                            dialog.dismiss();
                            openSeatsNumberDialog();
                            waiterClick = false;
                        } else {
                            new Settings().makeText(DineIn.this, getResources().getString(R.string.add_waiter_befor));
                        }
                    }
                });
                dialog.show();

                focused = (LinearLayout) view;
                TextView textView = (TextView) focused.getChildAt(1);
                tableNumber = Integer.parseInt( convertToEnglish(textView.getText().toString()));

            } else {
                focused = (LinearLayout) view;
                TextView textView = (TextView) focused.getChildAt(1);
                tableNumber = Integer.parseInt( convertToEnglish(textView.getText().toString()));

                String waiterName = "";
                ArrayList<OrderHeader> orderHeader = mHandler.getOrderHeaderTemp("" + current, "" + tableNumber);
                for (int i = 0; i < orderHeader.size(); i++) {
                    if (orderHeader.get(i).getTableNO() == tableNumber) {
                        waiterName = orderHeader.get(i).getWaiter();
                        break;
                    }
                }

                String waiterNumber = "";
                ArrayList<EmployeeRegistrationModle> employees = mHandler.getAllEmployeeRegistration();
                for (int i = 0; i < employees.size(); i++) {
                    if (employees.get(i).getEmployeeName().equals(waiterName)) {
                        waiterNumber =  convertToEnglish("" + employees.get(i).getEmployeeNO());
                        break;
                    }
                }
                Log.e("test ", "" + waiterName);
                Log.e("test ", "" + waiterNumber);

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
                seatsNo.setText("" +  convertToEnglish(sum+""));
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
                if (! convertToEnglish(seatsNo.getText().toString()).equals("") && ! convertToEnglish(male.getText().toString()).equals("") && ! convertToEnglish(female.getText().toString()).equals("") && ! convertToEnglish(children.getText().toString()).equals("")) {
                    int sum = (int) Double.parseDouble(male.getText().toString()) +
                            (int) Double.parseDouble(female.getText().toString()) +
                            (int) Double.parseDouble(children.getText().toString());

                    if ((int) Double.parseDouble( convertToEnglish(seatsNo.getText().toString())) == sum) {

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
                        new Settings().makeText(DineIn.this, getResources().getString(R.string.no_of_seats_doesn_match_no_pepole));
                    }
                } else {
                    new Settings().makeText(DineIn.this,getResources().getString(R.string.fill_request_filed) );
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
                        Toast.makeText(DineIn.this, getResources().getString(R.string.ops_same_table), Toast.LENGTH_SHORT).show();
                    } else {
                        // update on tables temp
                        mHandler.moveTablesTemp(fromSection, focusedLeft, toSection, focusedRight);

                        mHandler.addTableAction(new TableActions(Settings.POS_number, Settings.user_name, Settings.user_no,
                                Settings.shift_name, Settings.shift_number, 0, today, time, focusedLeft, fromSection,
                                focusedRight, toSection));

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
                    new Settings().makeText(DineIn.this, getResources().getString(R.string.chooes_table_from_to_list));
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
                        new Settings().makeText(DineIn.this,getResources().getString(R.string.ops_same_table) );
                    } else {
                        // update on tables temp
                        mHandler.mergeTablesTemp(fromSection, focusedLeft, toSection, focusedRight);

                        mHandler.addTableAction(new TableActions(Settings.POS_number, Settings.user_name, Settings.user_no,
                                Settings.shift_name, Settings.shift_number, 1, today, time, focusedLeft, fromSection,
                                focusedRight, toSection));

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
                new Settings().makeText(DineIn.this,getResources().getString(R.string.chooes_table_from_to_list) );
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

                if (tableNo.getSelectedItem() != null) {
                    List<OrderTransactions> orderTransTemp = mHandler.getOrderTransactionsTemp("" + current, tableNo.getSelectedItem().toString());
                    if (orderTransTemp.size() != 0) {
                        Intent intent = new Intent(DineIn.this, PayMethods.class);
                        intent.putExtra("sectionNo", "" + current);
                        intent.putExtra("tableNo", tableNo.getSelectedItem().toString());
                        startActivity(intent);
                    } else {
                        new Settings().makeText(DineIn.this,getResources().getString(R.string.this_table_no_order) );
                    }
                } else
                new Settings().makeText(DineIn.this,getResources().getString(R.string.no_table_select) );
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

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(60, 20);
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


    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
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

        checkOut.setOnClickListener(onRightListListener);
        reprint.setOnClickListener(onRightListListener);
    }
}
