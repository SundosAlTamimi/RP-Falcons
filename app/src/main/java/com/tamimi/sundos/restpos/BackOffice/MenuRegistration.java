package com.tamimi.sundos.restpos.BackOffice;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.DatabaseHandler;
import com.tamimi.sundos.restpos.Models.FamilyCategory;
import com.tamimi.sundos.restpos.Models.Items;
import com.tamimi.sundos.restpos.Models.Recipes;
import com.tamimi.sundos.restpos.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MenuRegistration extends AppCompatActivity {

    TableLayout recipeTable;
    Spinner categoriesSpinner, unitSpinner, printersSpinner;
    RadioGroup taxTypRadioGroup, statusRadioGroup, itemTypeRadioGroup;
    CheckBox notUsedCheckBox, discountAvailableCheckBox, pointAvailableCheckBox, openPriceCheckBox;
    EditText menuNameEditText, priceEditText, taxPercentEditText, secondaryNameEditText,
            kitchenAliasEditText, itemBarcodeEditText, descriptionEditText, wastagePercentEditText;
    Button newButton, saveButton, exitButton, addMenuCategory, addInventoryUnit, addRecipe;
    ImageView itemPic;

    String familyName = "Baverage";
    int showInMenuVariavle = 0;
    Bitmap itemBitmapPic;

    Dialog dialog, dialog2;
    static final int SELECTED_PICTURE = 1;
    boolean itemBarcodeFound = false;
    List<Items> items;

    ArrayAdapter<String> categoriesAdapter, unitAdapter, printersAdapter, familiesAdapter, menuNameAdapter;

    List<String> categories = new ArrayList<>();
    List<String> unit = new ArrayList<>();
    List<String> printers = new ArrayList<>();
    List<String> families;

    private static DatabaseHandler mDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_registration);

        initialize();

        taxTypRadioGroup.check(R.id.tax_);
        statusRadioGroup.check(R.id.available);
        itemTypeRadioGroup.check(R.id.ready);

        addMenuCategory.setOnClickListener(onClickListener);
        addInventoryUnit.setOnClickListener(onClickListener);
        addRecipe.setOnClickListener(onClickListener);
        itemPic.setOnClickListener(onClickListener);

        itemBitmapPic = null;

        mDbHandler = new DatabaseHandler(MenuRegistration.this);
        fillSpinners();

        taxTypRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tax_:
                        taxPercentEditText.setEnabled(true);
                        taxPercentEditText.setText("");
                        break;
                    case R.id.no_tax:
                        taxPercentEditText.setEnabled(false);
                        taxPercentEditText.setText("0.00");
                        break;
                }
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuRegistration.this);
                builder.setTitle("Are you sure, you want to clear the form ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearForm();
                    }
                });

                builder.setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items = mDbHandler.getAllItems();
                String itemBarcode = itemBarcodeEditText.getText().toString();
                for (int i = 0; i < items.size(); i++) {
                    if (!itemBarcode.equals("") && itemBarcode.equals(String.valueOf(items.get(i).getItemBarcode()))) {
                        itemBarcodeFound = true;
                        break;
                    }
                }
                if (!itemBarcodeFound) {
                    if (check()) {
                        int taxType = 0;
                        switch (taxTypRadioGroup.getCheckedRadioButtonId()) {
                            case R.id.tax_:
                                taxType = 0;
                                break;
                            case R.id.no_tax:
                                taxType = 1;
                                break;
                        }
                        int status = 0;
                        switch (statusRadioGroup.getCheckedRadioButtonId()) {
                            case R.id.available:
                                status = 0;
                                break;
                            case R.id.out_of_stock:
                                status = 1;
                                break;
                        }
                        int itemType = 0;
                        switch (itemTypeRadioGroup.getCheckedRadioButtonId()) {
                            case R.id.ready:
                                itemType = 0;
                                break;
                            case R.id.row_material:
                                itemType = 1;
                                break;
                        }

                        storeInDatabase(
                                categoriesSpinner.getSelectedItem().toString(),
                                menuNameEditText.getText().toString(),
                                familyName,
                                ifEmptyDouble(taxPercentEditText.getText().toString()),
                                taxType,
                                ifEmptyString(secondaryNameEditText.getText().toString()),
                                ifEmptyString(kitchenAliasEditText.getText().toString()),
                                Integer.parseInt(itemBarcodeEditText.getText().toString()),
                                status,
                                itemType,
                                unitSpinner.getSelectedItem().toString(),
                                ifEmptyDouble(wastagePercentEditText.getText().toString()),
                                discountAvailableCheckBox.isChecked() ? 1 : 0,
                                pointAvailableCheckBox.isChecked() ? 1 : 0,
                                openPriceCheckBox.isChecked() ? 1 : 0,
                                printersSpinner.getSelectedItem().toString(),
                                ifEmptyString(descriptionEditText.getText().toString()),
                                ifEmptyDouble(priceEditText.getText().toString()),
                                notUsedCheckBox.isChecked() ? 1 : 0,
                                showInMenuVariavle,
                                itemBitmapPic);

                        Toast.makeText(MenuRegistration.this, "Saved", Toast.LENGTH_SHORT).show();
                        clearForm();
                        itemBarcodeFound = false;
                    } else
                        Toast.makeText(MenuRegistration.this, "Please input the requested fields", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(MenuRegistration.this, "Please change itemBarcode - another item have this itemBarcode -", Toast.LENGTH_SHORT).show();
                itemBarcodeFound = false;
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.addMenuCategory:
                    showAddCategoryDialog();
                    break;

                case R.id.addInventoryUnit:
                    showAddUnitDialog();
                    break;

                case R.id.addRecipe:
                    showAddRecipeDialog();
                    break;

                case R.id.itemPicture:
                    showAddPictureDialog();
                    break;

            }

        }

    };


    void showAddCategoryDialog() {

        dialog = new Dialog(MenuRegistration.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_new_category_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final EditText catName = (EditText) dialog.findViewById(R.id.cat_name);
        final Spinner familyNameSpinner = (Spinner) dialog.findViewById(R.id.family_name);
        Button buttonAdd = (Button) dialog.findViewById(R.id.addFamily);
        Button buttonDone = (Button) dialog.findViewById(R.id.b_done);
        CheckBox showInMenu = (CheckBox) dialog.findViewById(R.id.showInMenu);

        showInMenuVariavle = showInMenu.isChecked() ? 1 : 0;
        families = new ArrayList<>();

//        families = mDbHandler.getAllExistingFamilies();
        for (int i = 0; i < mDbHandler.getAllFamilyCategory().size(); i++) {
            if (mDbHandler.getAllFamilyCategory().get(i).getType() == 1) {
                families.add(mDbHandler.getAllFamilyCategory().get(i).getName());
            }

        }
        familiesAdapter = new ArrayAdapter<String>(MenuRegistration.this, R.layout.spinner_style, families);
        familyNameSpinner.setAdapter(familiesAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFamilyDialog();
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!catName.getText().toString().equals("") && familyNameSpinner.getCount() != 0) {

                    FamilyCategory familyCategory = new FamilyCategory();

                    categories.add(0, catName.getText().toString());
                    categoriesAdapter.notifyDataSetChanged();
                    familyName = familyNameSpinner.getSelectedItem().toString();
                    int serial = mDbHandler.getAllFamilyCategory().size();
                    familyCategory.setSerial(serial);
                    familyCategory.setType(2);
                    // 1--> family type // 2--> category type
                    familyCategory.setName(catName.getText().toString());

                    mDbHandler.addFamilyCategory(familyCategory);

                    dialog.dismiss();

                } else {
                    Toast.makeText(MenuRegistration.this, "Please input category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    void showAddUnitDialog() {

        dialog = new Dialog(MenuRegistration.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_new_unit_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final EditText unitEditText = (EditText) dialog.findViewById(R.id.unit);
        Button buttonDone = (Button) dialog.findViewById(R.id.b_done);


        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!unitEditText.getText().toString().equals("")) {

                    unit.add(0, unitEditText.getText().toString());
                    unitAdapter.notifyDataSetChanged();
                    dialog.dismiss();

                } else {
                    Toast.makeText(MenuRegistration.this, "Please input unit name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    void showAddFamilyDialog() {

        dialog2 = new Dialog(MenuRegistration.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setCancelable(true);
        dialog2.setContentView(R.layout.add_new_family_dialog);
        dialog2.setCanceledOnTouchOutside(true);

        final EditText familyEditText = (EditText) dialog2.findViewById(R.id.family);
        Button buttonDone = (Button) dialog2.findViewById(R.id.b_done);


        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!familyEditText.getText().toString().equals("")) {

                    FamilyCategory familyCategory = new FamilyCategory();

                    families.add(0, familyEditText.getText().toString());
                    familiesAdapter.notifyDataSetChanged();

                    int serial = mDbHandler.getAllFamilyCategory().size();
                    familyCategory.setSerial(serial);
                    familyCategory.setType(1);
                    // 1--> family type // 2--> category type
                    familyCategory.setName(familyEditText.getText().toString());

                    mDbHandler.addFamilyCategory(familyCategory);

                    dialog2.dismiss();

                } else {
                    Toast.makeText(MenuRegistration.this, "Please input family name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog2.show();
    }

    void showAddRecipeDialog() {

        dialog = new Dialog(MenuRegistration.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_recipe_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final EditText unit = (EditText) dialog.findViewById(R.id.unit);
        final EditText qty = (EditText) dialog.findViewById(R.id.qty);
        final Spinner recipeSpinner = (Spinner) dialog.findViewById(R.id.recipe_name);
        Button buttonDone = (Button) dialog.findViewById(R.id.b_done);

        items = mDbHandler.getAllItems();
        List<String> categoryName = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            categoryName.add(items.get(i).getMenuName());
        }

        menuNameAdapter = new ArrayAdapter<>(MenuRegistration.this, R.layout.spinner_style, categoryName);
        recipeSpinner.setAdapter(menuNameAdapter);

        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit.setText(items.get(i).getInventoryUnit());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!qty.getText().toString().equals("") && recipeSpinner.getCount() != 0) {

                    int position = recipeSpinner.getSelectedItemPosition();
                    insertRow(Integer.parseInt(itemBarcodeEditText.getText().toString()), items.get(position).getMenuName(),
                            items.get(position).getInventoryUnit(), Integer.parseInt(qty.getText().toString()),
                            items.get(position).getPrice());

                    dialog.dismiss();

                } else {
                    Toast.makeText(MenuRegistration.this, "Please input requested fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    void showAddPictureDialog() {

        dialog = new Dialog(MenuRegistration.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_picture_dialog);
        dialog.setCanceledOnTouchOutside(true);

        items = mDbHandler.getAllItems();

        LinearLayout pics = (LinearLayout) dialog.findViewById(R.id.usedPictures);

        for (int i = items.size() - 1; i >= 0; i--) {

            final Bitmap pic = items.get(i).getPic();
            if (pic != null) {
                RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(150, 150);
                imageParams.setMargins(5, 0, 5, 0);

                ImageView newPic = new ImageView(MenuRegistration.this);
                newPic.setLayoutParams(imageParams);
                newPic.setImageDrawable(new BitmapDrawable(getResources(), pic));

                newPic.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemPic.setImageDrawable(new BitmapDrawable(getResources(), pic));
                        itemBitmapPic = pic;
                        dialog.dismiss();
                    }
                });

                pics.addView(newPic);
            }
        }
        Button buttonAddFromGallery = (Button) dialog.findViewById(R.id.buttonAddFromGallery);

        buttonAddFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 256);
                intent.putExtra("outputY", 256);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, SELECTED_PICTURE);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                itemBitmapPic = extras.getParcelable("data");
                itemPic.setImageDrawable(new BitmapDrawable(getResources(), itemBitmapPic));
                Log.e("******************1", itemPic.getDrawable().toString());

            }
        }
    }

    void storeInDatabase(String categoryName, String menuName, String familyName, double taxPercent, int taxType, String secondaryName,
                         String kitchenAlias, int itemBarcode, int status, int itemType, String inventoryUnit,
                         double wastagePercent, int discountAvailable, int pointAvailable, int openPrice, String printer,
                         String description, double price, int used, int showInMenu, Bitmap img) {

        mDbHandler.addItem(new Items(
                categoryName, menuName, familyName, taxPercent, taxType, secondaryName, kitchenAlias, itemBarcode, status, itemType,
                inventoryUnit, wastagePercent, discountAvailable, pointAvailable, openPrice, printer, description, price, used, showInMenu, img));

        for (int i = 0; i < recipeTable.getChildCount(); i++) {
            TableRow tableRow = (TableRow) recipeTable.getChildAt(i);

            TextView textView1 = (TextView) tableRow.getChildAt(0);
            TextView textView2 = (TextView) tableRow.getChildAt(1);
            TextView textView3 = (TextView) tableRow.getChildAt(2);
            TextView textView4 = (TextView) tableRow.getChildAt(3);
            TextView textView5 = (TextView) tableRow.getChildAt(4);

            mDbHandler.addRecipe(new Recipes(Integer.parseInt(textView1.getText().toString()), textView2.getText().toString(),
                    textView3.getText().toString(), Integer.parseInt(textView4.getText().toString()),
                    Double.parseDouble(textView5.getText().toString())));
        }
    }

    void insertRow(int barCode, String item, String unit, int qty, double price) {

        if (check()) {
            final TableRow row = new TableRow(MenuRegistration.this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            for (int i = 0; i < 5; i++) {
                TextView textView = new TextView(MenuRegistration.this);

                switch (i) {
                    case 0:
                        textView.setText("" + barCode);
                        break;
                    case 1:
                        textView.setText(item);
                        break;
                    case 2:
                        textView.setText(unit);
                        break;
                    case 3:
                        textView.setText("" + qty);
                        break;
                    case 4:
                        textView.setText("" + qty * price);
                        break;
                }

                textView.setTextColor(ContextCompat.getColor(MenuRegistration.this, R.color.text_color));
                textView.setGravity(Gravity.CENTER);

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                textView.setLayoutParams(lp2);

                row.addView(textView);

                row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuRegistration.this);
                        builder.setTitle("Do you want to delete this recipe ?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                recipeTable.removeView(row);
                            }
                        });

                        builder.setNegativeButton("No", null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        return true;
                    }
                });
            }

            recipeTable.addView(row);
        }
    }

    boolean check() {
        if (!menuNameEditText.getText().toString().equals("") &&
                !priceEditText.getText().toString().equals("") &&
                !taxPercentEditText.getText().toString().equals("") &&
                !itemBarcodeEditText.getText().toString().equals("") &&
                categoriesSpinner.getCount() != 0 &&
                unitSpinner.getCount() != 0 &&
                printersSpinner.getCount() != 0)

            return true;
        else
            return false;
    }

    String ifEmptyString(String editText) {
        if (editText.equals(""))
            return menuNameEditText.getText().toString();
        else
            return editText;
    }

    double ifEmptyDouble(String editText) {
        if (editText.equals(""))
            return 0.00;
        else
            return Double.parseDouble(editText);
    }

    void fillSpinners() {

//        categories = mDbHandler.getAllExistingCategories();
        for (int i = 0; i < mDbHandler.getAllFamilyCategory().size(); i++) {
            if (mDbHandler.getAllFamilyCategory().get(i).getType() == 2) {
                categories.add(mDbHandler.getAllFamilyCategory().get(i).getName());
            }
        }
        unit = mDbHandler.getAllExistingUnits();

        printers.add("printer 1");
        printers.add("printer 2");
        printers.add("printer 3");

        categoriesAdapter = new ArrayAdapter<String>(MenuRegistration.this, R.layout.spinner_style, categories);
        categoriesSpinner.setAdapter(categoriesAdapter);

        unitAdapter = new ArrayAdapter<String>(MenuRegistration.this, R.layout.spinner_style, unit);
        unitSpinner.setAdapter(unitAdapter);

        printersAdapter = new ArrayAdapter<String>(MenuRegistration.this, R.layout.spinner_style, printers);
        printersSpinner.setAdapter(printersAdapter);
    }

    void clearForm() {
        menuNameEditText.setText("");
        priceEditText.setText("");
        taxPercentEditText.setText("");
        secondaryNameEditText.setText("");
        kitchenAliasEditText.setText("");
        itemBarcodeEditText.setText("");
        descriptionEditText.setText("");
        wastagePercentEditText.setText("");

        notUsedCheckBox.setChecked(false);
        discountAvailableCheckBox.setChecked(false);
        pointAvailableCheckBox.setChecked(false);
        openPriceCheckBox.setChecked(false);

        taxTypRadioGroup.check(R.id.tax_);
        statusRadioGroup.check(R.id.available);
        itemTypeRadioGroup.check(R.id.ready);

        itemPic.setImageDrawable(getResources().getDrawable(R.drawable.item_pic_icon));
        itemBitmapPic = null;

        recipeTable.removeAllViews();
        taxPercentEditText.setEnabled(true);
    }

    void initialize() {
        recipeTable = (TableLayout) findViewById(R.id.recipeTable);
        itemPic = (ImageView) findViewById(R.id.itemPicture);

        categoriesSpinner = (Spinner) findViewById(R.id.categoriesSpinner);
        unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        printersSpinner = (Spinner) findViewById(R.id.printersSpinner);

        taxTypRadioGroup = (RadioGroup) findViewById(R.id.taxTypRadioGroup);
        statusRadioGroup = (RadioGroup) findViewById(R.id.statusRadioGroup);
        itemTypeRadioGroup = (RadioGroup) findViewById(R.id.itemTypeRadioGroup);

        notUsedCheckBox = (CheckBox) findViewById(R.id.notUsedCheckBox);
        discountAvailableCheckBox = (CheckBox) findViewById(R.id.discountAvailableCheckBox);
        pointAvailableCheckBox = (CheckBox) findViewById(R.id.pointAvailableCheckBox);
        openPriceCheckBox = (CheckBox) findViewById(R.id.openPriceCheckBox);

        menuNameEditText = (EditText) findViewById(R.id.menuNameEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        taxPercentEditText = (EditText) findViewById(R.id.taxPercentEditText);
        secondaryNameEditText = (EditText) findViewById(R.id.secondaryNameEditText);
        kitchenAliasEditText = (EditText) findViewById(R.id.kitchenAliasEditText);
        itemBarcodeEditText = (EditText) findViewById(R.id.itemBarcodeEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        wastagePercentEditText = (EditText) findViewById(R.id.wastagePercentEditText);

        newButton = (Button) findViewById(R.id.newButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        exitButton = (Button) findViewById(R.id.exitButton);
        addMenuCategory = (Button) findViewById(R.id.addMenuCategory);
        addInventoryUnit = (Button) findViewById(R.id.addInventoryUnit);
        addRecipe = (Button) findViewById(R.id.addRecipe);
    }
}
