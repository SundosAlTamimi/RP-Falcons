package com.tamimi.sundos.restpos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.Models.Tables;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;


public class DineInLayout extends AppCompatActivity {


    ArrayList<Tables> currentList, list0, list1, list2, list3, list4, list5;

    Dialog dialog;
    Button addIcon, addSquaredTable, addCircledTable;
    Button save;
    Button mainF, firstF, secondF, thirdF, fourthF, fifthF;
    LinearLayout add, rightBorder;
    ViewGroup land;
    int xDelta;
    int yDelta;
    GestureDetector gestureDetector;
    int visibleAdd = 0;
    int visibleEdit = 0;
    boolean clicked = false;

    LinearLayout focused;
    int tableNumber = 0;
    int current = 0;

    DatabaseHandler mHandler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dine_in);

        mHandler = new DatabaseHandler(DineInLayout.this);
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        focused = new LinearLayout(DineInLayout.this);

        addIcon = (Button) findViewById(R.id.add_icon);
        save = (Button) findViewById(R.id.save);
        mainF = (Button) findViewById(R.id.main);
        firstF = (Button) findViewById(R.id.first_f);
        secondF = (Button) findViewById(R.id.second_f);
        thirdF = (Button) findViewById(R.id.third_f);
        fourthF = (Button) findViewById(R.id.fourth_f);
        fifthF = (Button) findViewById(R.id.fifth_f);
        addSquaredTable = (Button) findViewById(R.id.add_squared_table);
        addCircledTable = (Button) findViewById(R.id.add_circled_table);
        add = (LinearLayout) findViewById(R.id.add_table_list);
        rightBorder = (LinearLayout) findViewById(R.id.rightBorder);
        land = (ViewGroup) findViewById(R.id.land);

        addIcon.bringToFront();
        add.setVisibility(View.INVISIBLE);
        showEditTableScreen();

        mainF.setOnClickListener(onFloorClickListener);
        firstF.setOnClickListener(onFloorClickListener);
        secondF.setOnClickListener(onFloorClickListener);
        thirdF.setOnClickListener(onFloorClickListener);
        fourthF.setOnClickListener(onFloorClickListener);
        fifthF.setOnClickListener(onFloorClickListener);

        addIcon.setOnClickListener(onAddTableClickListener);
        addSquaredTable.setOnClickListener(onAddTableClickListener);
        addCircledTable.setOnClickListener(onAddTableClickListener);

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


    OnClickListener onAddTableClickListener = new OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.add_icon:
                    if (visibleAdd == 0)
                        slideLeft(add);
                    else
                        slideRight(add);
                    break;
                case R.id.add_squared_table:
                    dialog = new Dialog(DineInLayout.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.add_squared_table_dialog);
                    dialog.setCanceledOnTouchOutside(true);
//                    Window window = dialog.getWindow();
//                    window.setLayout(900, 350);
                    ImageView eightC = (ImageView) dialog.findViewById(R.id.eight_c);
                    ImageView sixC = (ImageView) dialog.findViewById(R.id.six_c);
                    ImageView fourC = (ImageView) dialog.findViewById(R.id.four_c);
                    ImageView twoC = (ImageView) dialog.findViewById(R.id.two_c);

                    eightC.setOnClickListener(onClickListener);
                    sixC.setOnClickListener(onClickListener);
                    fourC.setOnClickListener(onClickListener);
                    twoC.setOnClickListener(onClickListener);

                    dialog.show();
                    break;
                case R.id.add_circled_table:
                    dialog = new Dialog(DineInLayout.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.add_cyrcled_table_dialog);
                    dialog.setCanceledOnTouchOutside(true);
//                    Window window1 = dialog.getWindow();
//                    window1.setLayout(900, 350);
                    ImageView eightC1 = (ImageView) dialog.findViewById(R.id.eight_c);
                    ImageView sixC1 = (ImageView) dialog.findViewById(R.id.six_c);
                    ImageView fourC1 = (ImageView) dialog.findViewById(R.id.four_c);
                    ImageView twoC1 = (ImageView) dialog.findViewById(R.id.two_c);

                    eightC1.setOnClickListener(onClickListener2);
                    sixC1.setOnClickListener(onClickListener2);
                    fourC1.setOnClickListener(onClickListener2);
                    twoC1.setOnClickListener(onClickListener2);

                    dialog.show();
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
                LinearLayout linearLayout = new LinearLayout(DineInLayout.this);
                ImageView imageView = new ImageView(DineInLayout.this);
                TextView textView = new TextView(DineInLayout.this);

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

                linearLayout.setOnTouchListener(new onTouchListener());
                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                imageView.setLayoutParams(param);
                linearLayout.setX(currentList.get(i).getMarginLeft());
                linearLayout.setY(currentList.get(i).getMarginTop());
                linearLayout.setLayoutParams(params);
                land.addView(linearLayout);
            }
        }
    };

    private final class onTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (gestureDetector.onTouchEvent(motionEvent)) { // onClick
                if (!clicked) {
                    focused = (LinearLayout) view;
                    view.setBackgroundResource(R.drawable.focused_table);
                    clicked = true;
                } else {
                    view.setBackgroundDrawable(null);

                    clicked = false;
                }


                return true;
            } else {
                LinearLayout v = (LinearLayout) view;
                final int x = (int) motionEvent.getRawX();
                final int y = (int) motionEvent.getRawY();
                TextView t = (TextView) v.getChildAt(1);

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        xDelta = x - params.leftMargin;
                        yDelta = y - params.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
                        for (int i = 0; i < currentList.size(); i++) {
                            if (Integer.parseInt(t.getText().toString()) == currentList.get(i).getTableNumber()) {
                                currentList.get(i).setMarginLeft(v.getLeft());
                                currentList.get(i).setMarginTop(v.getTop());
                                Log.i("***************current", "left" + v.getLeft());

                                setList();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;

                    case MotionEvent.ACTION_MOVE:

                        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        params2.leftMargin = x - xDelta;
                        params2.topMargin = y - yDelta;
                        view.setLayoutParams(params2);

                        break;
                }
                land.invalidate();
                return true;
            }
        }
    }

    OnClickListener onClickListener = new OnClickListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onClick(View view) {
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(150, 110);
            ImageView newTable = new ImageView(DineInLayout.this);
            newTable.setLayoutParams(imageParams);

            RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(170, 110);
            linearParams.leftMargin = 300;
            linearParams.topMargin = 100;
            LinearLayout newLinear = new LinearLayout(DineInLayout.this);
            newLinear.setOrientation(LinearLayout.HORIZONTAL);
            newLinear.setLayoutParams(linearParams);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(60, 20);
            TextView textView = new TextView(DineInLayout.this);
            textView.setLayoutParams(params2);
            textView.setText("" + tableNumber);
            textView.setTextColor(getResources().getColor(R.color.text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

            int imageResource = R.drawable.table_with_8;
            switch (view.getId()) {

                case R.id.eight_c:
                    newTable.setBackgroundResource(R.drawable.table_with_8);
                    newTable.setTag(R.drawable.table_with_8);
                    imageResource = R.drawable.table_with_8;
                    break;

                case R.id.six_c:
                    newTable.setBackgroundResource(R.drawable.table_with_6);
                    newTable.setTag(R.drawable.table_with_6);
                    imageResource = R.drawable.table_with_6;
                    break;

                case R.id.four_c:
                    newTable.setBackgroundResource(R.drawable.table_with_4);
                    newTable.setTag(R.drawable.table_with_4);
                    imageResource = R.drawable.table_with_4;
                    break;

                case R.id.two_c:
                    newTable.setBackgroundResource(R.drawable.table_with_2);
                    newTable.setTag(R.drawable.table_with_2);
                    imageResource = R.drawable.table_with_2;
                    break;

            }
            newLinear.setOnTouchListener(new onTouchListener());

            newLinear.addView(newTable);
            newLinear.addView(textView);
            land.addView(newLinear);

            Tables table = new Tables(170, 110, imageResource, 300, 100, tableNumber);
            currentList.add(table);
            tableNumber++;

            setList();

            slideRight(add);
            dialog.dismiss();
        }
    };

    OnClickListener onClickListener2 = new OnClickListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onClick(View view) {

            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(150, 110);
            ImageView newTable = new ImageView(DineInLayout.this);
            newTable.setLayoutParams(imageParams);


            RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(170, 110);
            linearParams.leftMargin = 300;
            linearParams.topMargin = 100;
            LinearLayout newLinear = new LinearLayout(DineInLayout.this);
            newLinear.setOrientation(LinearLayout.HORIZONTAL);
            newLinear.setLayoutParams(linearParams);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(60, 20);
            TextView textView = new TextView(DineInLayout.this);
            textView.setLayoutParams(params2);
            textView.setText("" + tableNumber);
            textView.setTextColor(getResources().getColor(R.color.text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

            int imageResource = R.drawable.table_with_8;
            switch (view.getId()) {

                case R.id.eight_c:
                    newTable.setBackgroundResource(R.drawable.circled_table_with_8);
                    newTable.setTag(R.drawable.circled_table_with_8);
                    imageResource = R.drawable.circled_table_with_8;
                    break;

                case R.id.six_c:
                    newTable.setBackgroundResource(R.drawable.circled_table_with_6);
                    newTable.setTag(R.drawable.circled_table_with_6);
                    imageResource = R.drawable.circled_table_with_6;
                    break;

                case R.id.four_c:
                    newTable.setBackgroundResource(R.drawable.circled_table_with_4);
                    newTable.setTag(R.drawable.circled_table_with_4);
                    imageResource = R.drawable.circled_table_with_4;
                    break;

                case R.id.two_c:
                    newTable.setBackgroundResource(R.drawable.circled_table_with_2);
                    newTable.setTag(R.drawable.circled_table_with_2);
                    imageResource = R.drawable.circled_table_with_2;
                    break;

            }
            newLinear.setOnTouchListener(new onTouchListener());

            newLinear.addView(newTable);
            newLinear.addView(textView);
            land.addView(newLinear);

            Tables table = new Tables(170, 110, imageResource, 300, 100, tableNumber);
            currentList.add(table);
            tableNumber++;

            setList();

            slideRight(add);
            dialog.dismiss();
        }

    };

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
            int tableNumber = i;

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

    public void showEditTableScreen() {
        rightBorder.removeAllViews();

        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80);
        Params.setMargins(17, 18, 17, 10);

        Button zoomIn = new Button(DineInLayout.this);
        zoomIn.setLayoutParams(Params);
        zoomIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zoom_in));
        zoomIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focused.getChildCount() != 0)
                    zoomIn();
            }
        });

        Button zoomOut = new Button(DineInLayout.this);
        zoomOut.setLayoutParams(Params);
        zoomOut.setBackgroundDrawable(getResources().getDrawable(R.drawable.zoom_out));
        zoomOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focused.getChildCount() != 0)
                    zoomOut();
            }
        });

        Button save = new Button(DineInLayout.this);
        save.setLayoutParams(Params);
        save.setBackgroundDrawable(getResources().getDrawable(R.drawable.save));
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                storeInDatabase();
            }
        });

        Button delete = new Button(DineInLayout.this);
        delete.setLayoutParams(Params);
        delete.setBackgroundDrawable(getResources().getDrawable(R.drawable.delete_table));
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focused.getChildCount() != 0) {
                    TextView textView = (TextView) focused.getChildAt(1);
                    land.removeView(focused);
                    for (int i = 0; i < currentList.size(); i++) {
                        if (Integer.parseInt(textView.getText().toString()) == currentList.get(i).getTableNumber())
                            currentList.remove(i);
                    }
                }
            }
        });

        rightBorder.addView(zoomIn);
        rightBorder.addView(zoomOut);
        rightBorder.addView(save);
        rightBorder.addView(delete);

    }

    public void storeInDatabase() {
        freeze();
        mHandler.deleteAllTables();
        if (list0.size() != 0)
            mHandler.addTables(list0, 0);

        if (list1.size() != 0)
            mHandler.addTables(list1, 1);

        if (list2.size() != 0)
            mHandler.addTables(list2, 2);

        if (list3.size() != 0)
            mHandler.addTables(list3, 3);

        if (list4.size() != 0)
            mHandler.addTables(list4, 4);

        if (list5.size() != 0)
            mHandler.addTables(list5, 5);
        new Settings().makeText(DineInLayout.this,getResources().getString(R.string.save) );
    }

    public void zoomIn() {
        ImageView imageView = (ImageView) focused.getChildAt(0);
        TextView textView = (TextView) focused.getChildAt(1);
        int x = (int) focused.getLeft();
        int y = (int) focused.getTop();

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams
                (focused.getLayoutParams().width + 5, focused.getLayoutParams().height + 5);
        params1.leftMargin = x;
        params1.topMargin = y;
        focused.setLayoutParams(params1);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams
                (imageView.getLayoutParams().width + 5, imageView.getLayoutParams().height + 5);
        imageView.setLayoutParams(params2);
        for (int i = 0; i < currentList.size(); i++) {
            if (Integer.parseInt(textView.getText().toString()) == currentList.get(i).getTableNumber()) {
                currentList.get(i).setWidth(focused.getLayoutParams().height + 5);
                currentList.get(i).setHeight(focused.getLayoutParams().width + 5);
            }
        }
    }

    public void zoomOut() {
        ImageView imageView = (ImageView) focused.getChildAt(0);
        TextView textView = (TextView) focused.getChildAt(1);
        int x = (int) focused.getLeft();
        int y = (int) focused.getTop();

        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams
                (focused.getLayoutParams().width - 5, focused.getLayoutParams().height - 4);
        params3.leftMargin = x;
        params3.topMargin = y;
        focused.setLayoutParams(params3);

        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams
                (imageView.getLayoutParams().width - 5, imageView.getLayoutParams().height - 4);
        imageView.setLayoutParams(params4);
        for (int i = 0; i < currentList.size(); i++) {
            if (Integer.parseInt(textView.getText().toString()) == currentList.get(i).getTableNumber()) {
                currentList.get(i).setWidth(focused.getLayoutParams().height - 4

                );
                currentList.get(i).setHeight(focused.getLayoutParams().width - 5);
            }
        }
    }

    public void slideLeft(View view) {
        visibleAdd = 1;
        view.setVisibility(View.VISIBLE);
        view.bringToFront();
        TranslateAnimation animate = new TranslateAnimation(
                view.getWidth(),                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideRight(View view) {
        visibleAdd = 0;
        view.setVisibility(View.INVISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                view.getWidth(),                 // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideUp(View view) {
        visibleEdit = 1;
        view.setVisibility(View.VISIBLE);
        view.bringToFront();
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(View view) {
        visibleEdit = 0;
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                view.getHeight() + 3);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);

    }

    public void fillMainFloor() {
        current = 0;
        currentList = list0;
        mainF.setBackgroundColor(getResources().getColor(R.color.dark_blue));

        for (int i = 0; i < currentList.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(DineInLayout.this);
            ImageView imageView = new ImageView(DineInLayout.this);
            TextView textView = new TextView(DineInLayout.this);

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

            linearLayout.setOnTouchListener(new onTouchListener());
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            imageView.setLayoutParams(param);
            linearLayout.setX(currentList.get(i).getMarginLeft());
            linearLayout.setY(currentList.get(i).getMarginTop());
            linearLayout.setLayoutParams(params);
            land.addView(linearLayout);

        }
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }


}
