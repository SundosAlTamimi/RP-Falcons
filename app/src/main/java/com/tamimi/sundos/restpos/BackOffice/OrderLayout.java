package com.tamimi.sundos.restpos.BackOffice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.DatabaseHandler;
import com.tamimi.sundos.restpos.LayoutFoodAdapter;
import com.tamimi.sundos.restpos.Models.Items;
import com.tamimi.sundos.restpos.Models.UsedCategories;
import com.tamimi.sundos.restpos.Models.UsedItems;
import com.tamimi.sundos.restpos.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import yuku.ambilwarna.AmbilWarnaDialog;

public class OrderLayout extends AppCompatActivity {

    EditText categoryName, numOfItems, backgroundColor, textColor;
    EditText itemName, itemPosition, itemBackgroundColor, itemTextColor;
    Button addCategory, apply, delete, add, save;
    LinearLayout categoriesLinearLayout, setting, setting2;
    ListView categorieslist, itemsList;
    GridView gv;

    LayoutFoodAdapter adapter;
    Button focused;
    int currentColor;


    ArrayList<UsedCategories> usedCategoriesList;

    private static DatabaseHandler mDbHandler;

    List<String> categoriesArraylist = new ArrayList<>();
    List<String> itemsNamelist = new ArrayList<>();
    List<Items> items = new ArrayList<>();
    ArrayList<UsedItems> subList;
    ArrayAdapter<String> categoriesAdapter, itemsNameAdapter;

    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_layout);

        usedCategoriesList = new ArrayList<>();
        mDbHandler = new DatabaseHandler(OrderLayout.this);

        items = mDbHandler.getAllItems();

        focused = new Button(OrderLayout.this);

        currentColor = ContextCompat.getColor(this, R.color.layer2);

        initialize();

        fillCategories();

        fillListView();

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.item_name:
                    break;

                case R.id.num_of_items:
                    break;

                case R.id.background:
                    showColorPickerDialog((EditText) view);
                    break;

                case R.id.text_color:
                    showColorPickerDialog((EditText) view);
                    break;

                case R.id.category1:
                    addCategory();
                    break;

                case R.id.apply:
                    setButtonAttributes(focused);
                    clearSettings();
                    fillGridView(0);
                    storeCategories();
                    break;

                case R.id.delete:
                    deleteCategory();
                    break;

                case R.id.background2:
                    showColorPickerDialog((EditText) view);
                    break;

                case R.id.text_color2:
                    showColorPickerDialog((EditText) view);
                    break;

                case R.id.add:
                    addItem();
                    break;

                case R.id.save:
                    storeItems();
                    finish();
                    break;

            }
        }
    };

    void showColorPickerDialog(final EditText editText) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, currentColor, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                editText.setText("" + color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                editText.setText("");
            }
        });
        dialog.show();
    }

    void addCategory() {

        setting.setVisibility(View.INVISIBLE);
        setting2.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        param.setMargins(0, 1, 0, 1);

        final Button button = new Button(OrderLayout.this);
        button.setLayoutParams(param);
        button.setText("");
        button.setBackgroundColor(getResources().getColor(R.color.dark_blue));
        button.setTextColor(getResources().getColor(R.color.text_color));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        button.setTag("0");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                freezeItems();
                storeItems();
                setting.setVisibility(View.INVISIBLE);
                setting2.setVisibility(View.VISIBLE);
                focused = (Button) view;
                fillItemListView();
                fillGridView(2);
                position = -1;
                clearSettings();
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
//                freezeItems();
                setting.setVisibility(View.VISIBLE);
                setting2.setVisibility(View.INVISIBLE);
                focused = (Button) view;
                position = -1;
                clearSettings();
                fillGridView(0);
                return true;
            }

        });

        categoriesLinearLayout.addView(button);

        freezeCategories();

    }

    void addItem() {

        if (itemPosition.getText().toString().equals(""))
            Toast.makeText(OrderLayout.this, "Please set position", Toast.LENGTH_SHORT).show();
        else {
            int item_position = Integer.parseInt(itemPosition.getText().toString());
            int tag = Integer.parseInt(focused.getTag().toString());

            if (item_position > tag - 1)
                Toast.makeText(OrderLayout.this, "Position is greater than the number of items in category " +
                        focused.getText().toString(), Toast.LENGTH_LONG).show();
            else {
                if (position != -1) { // position of setting itemList
                    int backColor = itemBackgroundColor.getText().toString().equals("") ? getResources().getColor(R.color.layer2) : Integer.parseInt(itemBackgroundColor.getText().toString());
                    int txtColor = itemTextColor.getText().toString().equals("") ? getResources().getColor(R.color.text_color) : Integer.parseInt(itemTextColor
                            .getText().toString());

                    subList.set(item_position, new UsedItems(focused.getText().toString(), itemName.getText().toString(),
                            backColor, txtColor, Integer.parseInt(itemPosition.getText().toString())));

//                    gv.setAdapter(null);
                    adapter = new LayoutFoodAdapter(OrderLayout.this, subList, 1);
                    gv.setAdapter(adapter);
                    storeItems();

                } else {
                    Toast.makeText(OrderLayout.this, "Please choose item", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    void setButtonAttributes(Button button) {
        if (position != -1) {
            int backColor = backgroundColor.getText().toString().equals("") ? getResources().getColor(R.color.dark_blue) : Integer.parseInt(backgroundColor.getText().toString());
            int txtColor = textColor.getText().toString().equals("") ? getResources().getColor(R.color.text_color) : Integer.parseInt(textColor.getText().toString());

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
            param.setMargins(0, 1, 0, 1);

            button.setLayoutParams(param);
            button.setText(categoryName.getText().toString());
            button.setBackgroundColor(backColor);
            button.setTextColor(txtColor);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            button.setTag(numOfItems.getText().toString().equals("") ? 0 : numOfItems.getText().toString());

            for (int i = 0; i < Integer.parseInt(numOfItems.getText().toString()); i++) {

                UsedItems usedItems = new UsedItems(categoryName.getText().toString(), "item" + i,
                        getResources().getColor(R.color.layer2), getResources().getColor(R.color.text_color), i);

                mDbHandler.addUsedItems(usedItems);
            }

            freezeCategories();

            if (categoriesArraylist.size() > 0) {
                categoriesArraylist.remove(position);
                categoriesAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(OrderLayout.this, "Please choose Category", Toast.LENGTH_SHORT).show();
        }
    }

    void freezeCategories() {

        usedCategoriesList.clear();
        for (int i = 0; i < categoriesLinearLayout.getChildCount(); i++) {
            Button button = (Button) categoriesLinearLayout.getChildAt(i);
            int backColor = ((ColorDrawable) button.getBackground()).getColor();

            usedCategoriesList.add(new UsedCategories(button.getText().toString(), Integer.parseInt(button.getTag().toString()),
                    backColor, button.getCurrentTextColor(), i));
        }
    }

    void storeCategories() {

        mDbHandler.deleteAllUsedCategories();
        for (int i = 0; i < usedCategoriesList.size(); i++) {

            UsedCategories usedCategory = new UsedCategories(usedCategoriesList.get(i).getCategoryName(),
                    usedCategoriesList.get(i).getNumberOfItems(), usedCategoriesList.get(i).getBackground(),
                    usedCategoriesList.get(i).getTextColor(), usedCategoriesList.get(i).getPosition());

            mDbHandler.addUsedCategory(usedCategory);

        }

    }

    void storeItems() {
        mDbHandler.deleteUsedItems(focused.getText().toString());

        final int size = gv.getChildCount();
        for (int i = 0; i < size; i++) {
            ViewGroup gridChild = (ViewGroup) gv.getChildAt(i);
            int backColor = ((ColorDrawable) gridChild.getBackground()).getColor();

            LinearLayout linearLayout = (LinearLayout) gridChild.getChildAt(1);
            TextView textView = (TextView) linearLayout.getChildAt(0);
            int textColor = textView.getCurrentTextColor();

            mDbHandler.addUsedItems(new UsedItems(focused.getText().toString(), textView.getText().toString(),
                    backColor, textColor, i));
        }

//        Toast.makeText(OrderLayout.this , "Menu Saved" , Toast.LENGTH_LONG).show();
    }

    void fillGridView(int flag) {

        String categoryName = focused.getText().toString();
        int noi = Integer.parseInt(focused.getTag().toString());

        if (flag == 0) {
            ArrayList<UsedItems> emptyList = new ArrayList<>();
            for (int i = 0; i < noi; i++) {
                emptyList.add(new UsedItems(categoryName, "item" + i, getResources().getColor(R.color.layer2), getResources().getColor(R.color.text_color), i));
            }

            Toast.makeText(OrderLayout.this, "tag " + focused.getTag().toString(), Toast.LENGTH_SHORT).show();
            gv.setAdapter(null);
            adapter = new LayoutFoodAdapter(OrderLayout.this, emptyList, 0);
            gv.setAdapter(adapter);

        } else if (flag == 1) {

        } else { // flag == 2
            subList = mDbHandler.getRequestedItems(categoryName);

            if (subList.size() != 0) {
                gv.setAdapter(null);
                adapter = new LayoutFoodAdapter(OrderLayout.this, subList, 1);
                Toast.makeText(OrderLayout.this, "size " + subList.size(), Toast.LENGTH_SHORT).show();
                gv.setAdapter(adapter);
            }
        }
    }

    void fillListView() {
        categoriesArraylist = mDbHandler.getAllExistingCategories();
        for (int i = 0; i < categoriesLinearLayout.getChildCount(); i++) {
            Button button = (Button) categoriesLinearLayout.getChildAt(i);

            for (int j = 0; j < categoriesArraylist.size(); j++) {
                if (button.getText().toString().equals(categoriesArraylist.get(j)))
                    categoriesArraylist.remove(j);
            }
        }

        categoriesAdapter = new ArrayAdapter<String>(OrderLayout.this, R.layout.categories_list_style, categoriesArraylist);
        categorieslist.setAdapter(categoriesAdapter);

        categorieslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                categoryName.setText(textView.getText().toString());
                position = i;
            }
        });
    }

    void fillItemListView() {

        itemsNamelist.clear();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getMenuCategory().equals(focused.getText().toString()))
                itemsNamelist.add(items.get(i).getMenuName());
        }

        itemsNameAdapter = new ArrayAdapter<String>(OrderLayout.this, R.layout.categories_list_style, itemsNamelist);
        itemsList.setAdapter(itemsNameAdapter);

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                itemName.setText(textView.getText().toString());
                position = i;
            }
        });
    }

    void fillCategories() {
        List<UsedCategories> categories = mDbHandler.getUsedCategories();
        categoriesLinearLayout.removeAllViews();

        for (int i = 0; i < categories.size(); i++) {

            final Button button = new Button(OrderLayout.this);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
            param.setMargins(0, 1, 0, 1);

            button.setLayoutParams(param);
            button.setText(categories.get(i).getCategoryName());
            button.setBackgroundColor(categories.get(i).getBackground());
            button.setTextColor(categories.get(i).getTextColor());
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            button.setTag(categories.get(i).getNumberOfItems());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storeItems();
                    setting.setVisibility(View.INVISIBLE);
                    setting2.setVisibility(View.VISIBLE);
                    focused = (Button) view;
                    fillItemListView();
                    fillGridView(2);
                    clearSettings();
                    position = -1;
                }
            });

            button.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    setting.setVisibility(View.VISIBLE);
                    setting2.setVisibility(View.INVISIBLE);
                    focused = (Button) view;
                    position = -1;
                    clearSettings();
                    fillGridView(0);
                    return true;
                }

            });

            categoriesLinearLayout.addView(button);
        }
    }

    void deleteCategory() {
        for (int i = 0; i < usedCategoriesList.size(); i++) {
            if (usedCategoriesList.get(i).getCategoryName().equals(focused.getText().toString()))
                usedCategoriesList.remove(i);
        }
        categoriesLinearLayout.removeView(focused);

        if (!focused.getText().toString().equals(""))
            categoriesArraylist.add(focused.getText().toString());

        focused = null;
        setting.setVisibility(View.INVISIBLE);
        setting2.setVisibility(View.INVISIBLE);
        clearSettings();
        fillListView();
        freezeCategories();
        gv.setAdapter(null);
    }

    void clearSettings() {
        categoryName.setText("");
        numOfItems.setText("");
        backgroundColor.setText("");
        textColor.setText("");

        itemName.setText("");
        itemPosition.setText("");
        itemBackgroundColor.setText("");
        itemTextColor.setText("");
    }

    void initialize() {
        categoryName = (EditText) findViewById(R.id.item_name);
        numOfItems = (EditText) findViewById(R.id.num_of_items);
        backgroundColor = (EditText) findViewById(R.id.background);
        textColor = (EditText) findViewById(R.id.text_color);

        itemName = (EditText) findViewById(R.id.item_name2);
        itemPosition = (EditText) findViewById(R.id.item_position);
        itemBackgroundColor = (EditText) findViewById(R.id.background2);
        itemTextColor = (EditText) findViewById(R.id.text_color2);

        addCategory = (Button) findViewById(R.id.category1);
        apply = (Button) findViewById(R.id.apply);
        delete = (Button) findViewById(R.id.delete);
        add = (Button) findViewById(R.id.add);
        save = (Button) findViewById(R.id.save);

        categoriesLinearLayout = (LinearLayout) findViewById(R.id.categories);
        setting = (LinearLayout) findViewById(R.id.setting);
        setting2 = (LinearLayout) findViewById(R.id.setting2);
        categorieslist = (ListView) findViewById(R.id.categories_list);
        itemsList = (ListView) findViewById(R.id.items_list);
        gv = (GridView) findViewById(R.id.GridView);

        setting.setVisibility(View.INVISIBLE);
        setting2.setVisibility(View.INVISIBLE);

        categoryName.setOnClickListener(onClickListener);
        numOfItems.setOnClickListener(onClickListener);
        backgroundColor.setOnClickListener(onClickListener);
        textColor.setOnClickListener(onClickListener);
        itemBackgroundColor.setOnClickListener(onClickListener);
        itemTextColor.setOnClickListener(onClickListener);
        addCategory.setOnClickListener(onClickListener);
        apply.setOnClickListener(onClickListener);
        add.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderLayout.this);
                builder.setTitle("Are you sure, you want delete this item ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {

//                        UsedItems usedItems = new UsedItems(categoryName.getText().toString(), "item" + i,
//                                getResources().getColor(R.color.layer2), getResources().getColor(R.color.text_color), i);

                        String itemName = "";
                        ArrayList<UsedItems> subList = mDbHandler.getRequestedItems(focused.getText().toString());
                        for (int k = 0; k < subList.size(); k++) {
                            if(subList.get(k).getCategoryName().equals(focused.getText().toString()) && subList.get(k).getPosition()==i){
                                itemName = subList.get(k).getitemName();
                                break;
                            }
                        }
                        int itemCode = -1;
                        List<Items> items = mDbHandler.getAllItems();
                        for (int i = 0; i < items.size(); i++) {
                            if(items.get(i).getMenuName().equals(itemName)){
                                itemCode = items.get(i).getItemBarcode();
                                break;
                            }
                        }
                        Log.e("delete " , " " + itemName + " " + itemCode);
                        mDbHandler.deleteModifierAndForce(itemCode);

                        mDbHandler.updateUsedItems(focused.getText().toString(), "item" + i,
                                getResources().getColor(R.color.layer2), getResources().getColor(R.color.text_color), i);

                        subList = mDbHandler.getRequestedItems(focused.getText().toString());

                        gv.setAdapter(null);
                        adapter = new LayoutFoodAdapter(OrderLayout.this, subList, 1);
                        gv.setAdapter(adapter);

                        storeItems();

                        Toast.makeText(OrderLayout.this , "Item deleted !" , Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }
}
