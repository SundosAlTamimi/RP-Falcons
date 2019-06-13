package com.tamimi.sundos.restpos.BackOffice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.DatabaseHandler;
import com.tamimi.sundos.restpos.LayoutCategoryAdapter;
import com.tamimi.sundos.restpos.LayoutItemsAdapter;
import com.tamimi.sundos.restpos.Models.FamilyCategory;
import com.tamimi.sundos.restpos.Models.Items;
import com.tamimi.sundos.restpos.Models.UsedCategories;
import com.tamimi.sundos.restpos.Models.UsedItems;
import com.tamimi.sundos.restpos.R;
import com.tamimi.sundos.restpos.Settings;

import org.askerov.dynamicgrid.DynamicGridView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import yuku.ambilwarna.AmbilWarnaDialog;

public class OrderLayout extends AppCompatActivity {

    EditText backgroundColor, textColor;
    EditText itemBackgroundColor, itemTextColor;
    Button apply, delete, emptySquare, itemsSettings, apply2, delete2, emptySquare2, catSettings, save;
    LinearLayout setting, setting2;
    ListView categorieslist, itemsList;
    DynamicGridView catGridView, itemGridView;
    TextView catName;

    int currentColor;
    int focusedCatPosition = -1;
    int focusedItemPosition = -1;

    List<UsedCategories> categories;
    List<UsedItems> items;
    ArrayList<UsedCategories> usedCategoriesList;


    private static DatabaseHandler mDbHandler;

    List<String> categoriesArraylist = new ArrayList<>();
    List<String> itemsNamelist = new ArrayList<>();
    List<Items> allItems = new ArrayList<>();
    ArrayAdapter<String> categoriesAdapter, itemsNameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_layout);


        usedCategoriesList = new ArrayList<>();
        mDbHandler = new DatabaseHandler(OrderLayout.this);

        categories = mDbHandler.getUsedCategories();
        allItems = mDbHandler.getAllItems();

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

                case R.id.apply:
                    applyCatProperties();
                    break;

                case R.id.delete:
                    deleteCategory();
                    break;

                case R.id.emptyCat:
                    addEmptySquare();
                    break;

                case R.id.setItems:
                    showItemsSettings();
                    break;

                case R.id.background2:
                    showColorPickerDialog((EditText) view);
                    break;

                case R.id.text_color2:
                    showColorPickerDialog((EditText) view);
                    break;

                case R.id.apply2:
                    applyItemProperties();
                    break;

                case R.id.delete2:
                    deleteItem();
                    break;

                case R.id.emptyItem:
                    addEmptySquare2();
                    break;

                case R.id.set_cat:
                    showCatSettings();
                    break;

                case R.id.save:

                    if (focusedCatPosition != -1 && items != null)
                        freezeItems();

                    freezeCategories();
                    finish();
                    break;
            }
        }
    };

    void showCatSettings() {
        freezeItems();
        setting.setVisibility(View.VISIBLE);
        setting2.setVisibility(View.INVISIBLE);
        catGridView.setVisibility(View.VISIBLE);
        itemGridView.setVisibility(View.INVISIBLE);
    }

    void showItemsSettings() {

        if (focusedCatPosition != -1) {
            LinearLayout linearLayout = (LinearLayout) catGridView.getChildAt(focusedCatPosition);
            LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);
            TextView textView = (TextView) innerLinearLayout.getChildAt(1);

            if (!textView.getText().toString().equals("")) {
                fillItemListView();
                fillItems();

                setting.setVisibility(View.INVISIBLE);
                setting2.setVisibility(View.VISIBLE);
                catGridView.setVisibility(View.INVISIBLE);
                itemGridView.setVisibility(View.VISIBLE);
                catName.setText(getResources().getString(R.string.category) + " : " + categories.get(focusedCatPosition).getCategoryName());
            }
        } else
            new Settings().makeText(OrderLayout.this,getResources().getString(R.string.select_cat) );
    }

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

    void freezeItems() {
        mDbHandler.deleteUsedItems(categories.get(focusedCatPosition).getCategoryName());
        for (int i = 0; i < items.size(); i++) {
            mDbHandler.addUsedItems(items.get(i));
        }
    }

    void freezeCategories() {
        mDbHandler.deleteAllUsedCategories();
        for (int i = 0; i < categories.size(); i++) {
            mDbHandler.addUsedCategory(categories.get(i));
        }
    }

// _______________________________________________________ item methods

    void fillItemListView() {

        itemsNamelist.clear();
        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getMenuCategory().equals(categories.get(focusedCatPosition).getCategoryName()))
                itemsNamelist.add(allItems.get(i).getMenuName());
        }

        itemsNameAdapter = new ArrayAdapter<String>(OrderLayout.this, R.layout.categories_list_style, itemsNamelist);
        itemsList.setAdapter(itemsNameAdapter);

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bitmap img = null;
                List<Items> allItems = mDbHandler.getAllItems();
                for (int k = 0; k < allItems.size(); k++)
                    if (categories.get(focusedCatPosition).getCategoryName().equals(allItems.get(k).getMenuCategory()) &&
                            itemsNamelist.get(i).equals(allItems.get(k).getMenuName()))
                        img = StringToBitMap(allItems.get(k).getPic());

                items.add(new UsedItems(categories.get(focusedCatPosition).getCategoryName(), itemsNamelist.get(i),
                        getResources().getColor(R.color.layer2), getResources().getColor(R.color.text_color), items.size(), img));

                LayoutItemsAdapter adapter = new LayoutItemsAdapter(OrderLayout.this, items, 2);
                itemGridView.setAdapter(adapter);
            }
        });
    }

    void fillItems() {

        items = mDbHandler.getRequestedItems(categories.get(focusedCatPosition).getCategoryName());

        List<Items> allItems = mDbHandler.getAllItems();
        for (int i = 0; i < items.size(); i++) {
            for (int k = 0; k < allItems.size(); k++) {
                if (items.get(i).getCategoryName().equals(allItems.get(k).getMenuCategory()) &&
                        items.get(i).getitemName().equals(allItems.get(k).getMenuName())) {
                    items.get(i).setItemPic(StringToBitMap(allItems.get(k).getPic()));
                }
            }
        }

        LayoutItemsAdapter adapter = new LayoutItemsAdapter(OrderLayout.this, items, 2);
        itemGridView.setAdapter(adapter);

        new Settings().makeText(OrderLayout.this, "size " + items.size());

        itemGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d("Log1 ", "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                if (itemGridView.isEditMode()) {
//                    freezeCategories();
                    UsedItems obj = items.get(oldPosition);
                    items.remove(oldPosition);
                    items.add(newPosition, obj);
                    itemGridView.stopEditMode();

                }
                Log.d("Log 2", String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });

        itemGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemGridView.startEditMode(position);
                return true;
            }
        });

        itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(OrderLayout.this, parent.getAdapter().getItem(position).toString(),
//                        Toast.LENGTH_SHORT).show();

                new Settings().makeText(OrderLayout.this,"" + position );

                LinearLayout linearLayout = (LinearLayout) view;
                LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);

                if (innerLinearLayout.getBackground() == null) {

                    for (int i = 0; i < itemGridView.getChildCount(); i++) {
                        LinearLayout linear = (LinearLayout) itemGridView.getChildAt(i);
                        LinearLayout innerLinear = (LinearLayout) linear.getChildAt(0);
                        innerLinear.setBackgroundDrawable(null);
                    }

                    focusedItemPosition = position;
                    innerLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.focused_table));
                } else {
                    innerLinearLayout.setBackgroundDrawable(null);
                    focusedItemPosition = -1;
                }
            }
        });
    }

    void applyItemProperties() {
        if (focusedItemPosition != -1) {

            int backColor = itemBackgroundColor.getText().toString().equals("") ? getResources().getColor(R.color.dark_blue) : Integer.parseInt(itemBackgroundColor.getText().toString());
            int txtColor = itemTextColor.getText().toString().equals("") ? getResources().getColor(R.color.text_color) : Integer.parseInt(itemTextColor.getText().toString());

            LinearLayout linearLayout = (LinearLayout) itemGridView.getChildAt(focusedItemPosition);
            LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);
            LinearLayout textLinearLayout = (LinearLayout) innerLinearLayout.getChildAt(1);
            TextView nameTextView = (TextView) textLinearLayout.getChildAt(0);
            TextView descTextView = (TextView) textLinearLayout.getChildAt(1);

            linearLayout.setBackgroundColor(backColor);
            nameTextView.setTextColor(txtColor);
            descTextView.setTextColor(txtColor);

            items.get(focusedItemPosition).setBackground(backColor);
            items.get(focusedItemPosition).setTextColor(txtColor);
        }
    }

    void deleteItem() {

        if (focusedItemPosition != -1) {
            LinearLayout linearLayout = (LinearLayout) itemGridView.getChildAt(focusedItemPosition);
            LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);
            LinearLayout textLinearLayout = (LinearLayout) innerLinearLayout.getChildAt(1);
            TextView nameTextView = (TextView) textLinearLayout.getChildAt(0);
            TextView descTextView = (TextView) textLinearLayout.getChildAt(1);

            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getitemName().equals(nameTextView.getText().toString()) &&
                        items.get(i).getPosition() == focusedItemPosition)
                    items.remove(i);
            }
            LayoutItemsAdapter adapter = new LayoutItemsAdapter(OrderLayout.this, items, 2);
            itemGridView.setAdapter(adapter);

            focusedItemPosition = -1;
            clearSettings();
        }
    }

    void addEmptySquare2() {

        items.add(new UsedItems(categories.get(focusedCatPosition).getCategoryName(), "",
                getResources().getColor(R.color.layer2), getResources().getColor(R.color.text_color), items.size()));

        LayoutItemsAdapter adapter = new LayoutItemsAdapter(OrderLayout.this, items, 2);
        itemGridView.setAdapter(adapter);
    }

    // _______________________________________________________ category methods

    void fillListView() {
        categoriesArraylist = mDbHandler.getAllExistingCategories();

        for (int i = 0; i < catGridView.getCount(); i++) {
            UsedCategories obj = (UsedCategories) catGridView.getAdapter().getItem(i);
            for (int j = 0; j < categoriesArraylist.size(); j++) {
                if (obj.getCategoryName().equals(categoriesArraylist.get(j))) {
                    categoriesArraylist.remove(j);
                    j--;
                }
            }
        }

        categoriesAdapter = new ArrayAdapter<String>(OrderLayout.this, R.layout.categories_list_style, categoriesArraylist);
        categorieslist.setAdapter(categoriesAdapter);

        categorieslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                List<FamilyCategory> allCats = mDbHandler.getAllFamilyCategory();
                for (int k = 0; k < allCats.size(); k++) {
                    if (allCats.get(k).getType() != 2) {
                        allCats.remove(k);
                        k--;
                    }
                }
                Bitmap img = null;
                for (int k = 0; k < allCats.size(); k++) {
                    if (categoriesArraylist.get(i).equals(allCats.get(k).getName())) {
                        img = StringToBitMap(allCats.get(k).getCatPic());
                    }
                }

                categories.add(new UsedCategories(categoriesArraylist.get(i), 0, getResources().getColor(R.color.layer2),
                        getResources().getColor(R.color.text_color), categories.size(), img));

                LayoutCategoryAdapter adapter = new LayoutCategoryAdapter(OrderLayout.this, categories, 3);
                catGridView.setAdapter(adapter);

                fillListView();
            }
        });
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
                    categories.get(i).setCatPic(StringToBitMap(allCats.get(k).getCatPic()));
                }
            }
        }

        LayoutCategoryAdapter adapter = new LayoutCategoryAdapter(OrderLayout.this, categories, 3);
        catGridView.setAdapter(adapter);

        catGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d("Log1 ", "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {

                UsedCategories obj = categories.get(oldPosition);
                categories.remove(oldPosition);
                categories.add(newPosition, obj);

                if (catGridView.isEditMode()) {
                    catGridView.stopEditMode();
                }
            }
        });

        catGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                catGridView.startEditMode(position);
                return true;
            }
        });

        catGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(OrderLayout.this, parent.getAdapter().getItem(position).toString(),
//                        Toast.LENGTH_SHORT).show();

                new Settings().makeText(OrderLayout.this,"" + position );

                LinearLayout linearLayout = (LinearLayout) view;
                LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);

                if (innerLinearLayout.getBackground() == null) {

                    for (int i = 0; i < catGridView.getChildCount(); i++) {
                        LinearLayout linear = (LinearLayout) catGridView.getChildAt(i);
                        LinearLayout innerLinear = (LinearLayout) linear.getChildAt(0);
                        innerLinear.setBackgroundDrawable(null);
                    }

                    focusedCatPosition = position;
                    innerLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.focused_table));
                } else {
                    innerLinearLayout.setBackgroundDrawable(null);
                    focusedCatPosition = -1;
                }
            }
        });
    }

    void applyCatProperties() {
        if (focusedCatPosition != -1) {

            int backColor = backgroundColor.getText().toString().equals("") ? getResources().getColor(R.color.dark_blue) : Integer.parseInt(backgroundColor.getText().toString());
            int txtColor = textColor.getText().toString().equals("") ? getResources().getColor(R.color.text_color) : Integer.parseInt(textColor.getText().toString());

            LinearLayout linearLayout = (LinearLayout) catGridView.getChildAt(focusedCatPosition);
            LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);
            TextView textView = (TextView) innerLinearLayout.getChildAt(1);

            linearLayout.setBackgroundColor(backColor);
            textView.setTextColor(txtColor);

            categories.get(focusedCatPosition).setBackground(backColor);
            categories.get(focusedCatPosition).setTextColor(txtColor);
        }
    }

    void deleteCategory() {

        if (focusedCatPosition != -1) {
            mDbHandler.deleteUsedItems(categories.get(focusedCatPosition).getCategoryName());

            LinearLayout linearLayout = (LinearLayout) catGridView.getChildAt(focusedCatPosition);
            LinearLayout innerLinearLayout = (LinearLayout) linearLayout.getChildAt(0);
            TextView textView = (TextView) innerLinearLayout.getChildAt(1);

//            if (!textView.getText().toString().equals(""))
//                fillListView();

            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getCategoryName().equals(textView.getText().toString()))
                    categories.remove(i);
            }
            LayoutCategoryAdapter adapter = new LayoutCategoryAdapter(OrderLayout.this, categories, 3);
            catGridView.setAdapter(adapter);

            focusedCatPosition = -1;
            clearSettings();
            fillListView();
        }
    }

    void addEmptySquare() {

        categories.add(new UsedCategories("", 0, getResources().getColor(R.color.layer2),
                getResources().getColor(R.color.text_color), catGridView.getChildCount(), null));

        LayoutCategoryAdapter adapter = new LayoutCategoryAdapter(OrderLayout.this, categories, 3);
        catGridView.setAdapter(adapter);

    }

    void clearSettings() {
        backgroundColor.setText("");
        textColor.setText("");
        itemBackgroundColor.setText("");
        itemTextColor.setText("");
    }

    void initialize() {
        catName = (TextView) findViewById(R.id.cat_name);

        backgroundColor = (EditText) findViewById(R.id.background);
        textColor = (EditText) findViewById(R.id.text_color);

        itemBackgroundColor = (EditText) findViewById(R.id.background2);
        itemTextColor = (EditText) findViewById(R.id.text_color2);

        apply = (Button) findViewById(R.id.apply);
        delete = (Button) findViewById(R.id.delete);
        emptySquare = (Button) findViewById(R.id.emptyCat);
        itemsSettings = (Button) findViewById(R.id.setItems);
        apply2 = (Button) findViewById(R.id.apply2);
        delete2 = (Button) findViewById(R.id.delete2);
        emptySquare2 = (Button) findViewById(R.id.emptyItem);
        catSettings = (Button) findViewById(R.id.set_cat);
        save = (Button) findViewById(R.id.save);

        setting = (LinearLayout) findViewById(R.id.setting);
        setting2 = (LinearLayout) findViewById(R.id.setting2);
        categorieslist = (ListView) findViewById(R.id.categories_list);
        itemsList = (ListView) findViewById(R.id.items_list);
        itemGridView = (DynamicGridView) findViewById(R.id.itemsGridView);
        catGridView = (DynamicGridView) findViewById(R.id.categoriesGridView);

        setting.setVisibility(View.VISIBLE);
        setting2.setVisibility(View.INVISIBLE);
        catGridView.setVisibility(View.VISIBLE);
        itemGridView.setVisibility(View.INVISIBLE);

        backgroundColor.setOnClickListener(onClickListener);
        textColor.setOnClickListener(onClickListener);
        itemBackgroundColor.setOnClickListener(onClickListener);
        itemTextColor.setOnClickListener(onClickListener);
        apply.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);
        emptySquare.setOnClickListener(onClickListener);
        itemsSettings.setOnClickListener(onClickListener);
        apply2.setOnClickListener(onClickListener);
        delete2.setOnClickListener(onClickListener);
        emptySquare2.setOnClickListener(onClickListener);
        catSettings.setOnClickListener(onClickListener);
    }



    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result= Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }



    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
