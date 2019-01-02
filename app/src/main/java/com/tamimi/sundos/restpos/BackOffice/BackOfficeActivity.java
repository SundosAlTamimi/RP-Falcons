package com.tamimi.sundos.restpos.BackOffice;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tamimi.sundos.restpos.DatabaseHandler;
import com.tamimi.sundos.restpos.Models.CategoryWithModifier;
import com.tamimi.sundos.restpos.Models.CustomerPayment;
import com.tamimi.sundos.restpos.Models.ForceQuestions;
import com.tamimi.sundos.restpos.Models.ItemWithFq;
import com.tamimi.sundos.restpos.Models.ItemWithModifier;
import com.tamimi.sundos.restpos.Models.Items;
import com.tamimi.sundos.restpos.Models.JobGroup;
import com.tamimi.sundos.restpos.Models.MemberShipGroup;
import com.tamimi.sundos.restpos.Models.Modifier;
import com.tamimi.sundos.restpos.Models.Money;
import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.PayMethod;
import com.tamimi.sundos.restpos.Models.Shift;
import com.tamimi.sundos.restpos.Order;
import com.tamimi.sundos.restpos.R;
import com.tamimi.sundos.restpos.Settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BackOfficeActivity extends AppCompatActivity {

    LinearLayout lManagement, lSales, lCustomers, lEmployees, lMenu, lSettings;

    TableLayout jobTable;
    Button butManagement, butSales, butCustomers, butEmployees, butMenu, butSettings;
    LinearLayout announcement, giftCard, employeeClockInOut, menuSearch;
    LinearLayout membershipGroup, membership, customerRegistration;
    LinearLayout jobGroup, employeeRegistration, employeeSchedule, payroll, vacation, editTables;
    LinearLayout menuCategory, menuRegistration, modifier, forceQuestion, menuLayout;
    LinearLayout store, storeOperation, users, moneyCategory;
    LinearLayout salesTotal, cashierInOut, canceledOrderHistory, dailyCashOut, salesByEmployee, salesByServers,
            salesReportForDay, salesByHours, salesVolumeByItem, topSalesItemReport, topGroupSalesReport, topFamilySalesReport,
            salesReportByCustomer, profitLossReport, detailSalesReport;

    private DatePickerDialog.OnDateSetListener mdate;
    int count, count2 , nextSerial;
    TextView test = null, fromDate, toDate;
    Dialog dialog;
    String today;
    DatabaseHandler mDHandler;
    Bitmap imageBitmap = null;
    ImageView moneyPicImageView = null;

    ArrayList<OrderHeader> headerData;

    TableRow focusedRaw = null;
    int rawPosition = 0;

    Calendar myCalendar ;

    ArrayList<ItemWithFq> itemWithFqsList;
    ArrayList<ItemWithModifier> itemWithModifiersList;
    ArrayList<CategoryWithModifier> categoryWithModifiersList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.back_office_activity);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today = df.format(currentTimeAndDate);

        itemWithFqsList = new ArrayList<>();
        itemWithModifiersList = new ArrayList<>();
        categoryWithModifiersList = new ArrayList<>();

        initialize();
        currentLinear(lManagement);
        mDHandler = new DatabaseHandler(BackOfficeActivity.this);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.b_management:
                    currentLinear(lManagement);
                    break;

                case R.id.b_sales:
                    currentLinear(lSales);
                    break;

                case R.id.b_customers:
                    currentLinear(lCustomers);
                    break;

                case R.id.b_employees:
                    currentLinear(lEmployees);
                    break;

                case R.id.b_menu:
                    currentLinear(lMenu);
                    break;

                case R.id.b_settings:
                    currentLinear(lSettings);
                    break;

            }
        }
    };

    View.OnClickListener onClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.announcement:
                    break;

                case R.id.gift_card:
                    break;

                case R.id.employee_click_out:
                    showAddShiftDialog();
                    break;

                case R.id.menu_search:
                    break;

                case R.id.membership_group:
                    showMemberShipGroupDialog();
                    break;
                case R.id.customer_reg:
                    Intent intentCustomerRegistration = new Intent(BackOfficeActivity.this, CustomerRegistration.class);
                    startActivity(intentCustomerRegistration);
                    break;

                case R.id.membership:
                    showCustomerPaymentDialog();
                    break;

                case R.id.job_group:
                    showJobGroupDialog();
                    break;

                case R.id.employee_registration:
                    Intent intentEmployeeRegistration = new Intent(BackOfficeActivity.this, EmployeeRegistration.class);
                    startActivity(intentEmployeeRegistration);
                    break;

                case R.id.employee_schedule:
                    break;

                case R.id.payroll:

                    break;

                case R.id.vacation:
                    break;

                case R.id.edit_tables_outhorization:
                    showEditTablesAuthorizationDialog();
                    break;

                case R.id.menu_category:

                    break;

                case R.id.menu_registration:
                    Intent intentMenuRegistration = new Intent(BackOfficeActivity.this, MenuRegistration.class);
                    startActivity(intentMenuRegistration);
                    break;

                case R.id.menu_layout:
                    Intent intentOrderLayout = new Intent(BackOfficeActivity.this, OrderLayout.class);
                    startActivity(intentOrderLayout);
                    break;

                case R.id.modifier:
                    showModifierDialog();
                    break;

                case R.id.force_question:
                    showForceQuestionDialog();
                    break;

                case R.id.store:
                    break;

                case R.id.store_operation:
                    break;

                case R.id.users:

                    break;

                case R.id.money_category:
                    showMoneyCategoryDialog();
                    break;

                case R.id.sales_total:

                    salesTotalDialog();

                    break;

                case R.id.cashier_in_out:
                    break;

                case R.id.canceled_order_history:
                    break;

                case R.id.daily_cash_out:
                    break;

                case R.id.sales_by_employee:
                    break;

                case R.id.sales_by_servers:
                    break;

                case R.id.sales_report_for_day:
                    break;

                case R.id.sales_by_houres:
                    break;

                case R.id.sales_volume_by_item:
                    break;

                case R.id.top_sales_item_report:
                    break;

                case R.id.top_group_sales_report:
                    break;

                case R.id.top_family_sales_report:
                    break;

                case R.id.sales_report_by_cusromer:
                    break;

                case R.id.profit_loss_report:
                    break;

                case R.id.detail_sales_report:
                    break;

            }
        }
    };

    void showCustomerPaymentDialog() {

        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.customer_payment_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(880, 510);

        Button save, exit;
        final EditText customerNo, customerName, customerBalance, value;
        TextView transDate, transNo;
        final Spinner paymentType;

        paymentType = (Spinner) dialog.findViewById(R.id.spinner1);
        save = (Button) dialog.findViewById(R.id.save);
        exit = (Button) dialog.findViewById(R.id.exit);
        customerNo = (EditText) dialog.findViewById(R.id.customerno);
        customerName = (EditText) dialog.findViewById(R.id.customername);
        customerBalance = (EditText) dialog.findViewById(R.id.customerbal);
        transNo = (TextView) dialog.findViewById(R.id.transno);
        transDate = (TextView) dialog.findViewById(R.id.transndate);
        value = (EditText) dialog.findViewById(R.id.values);

        ArrayList<PayMethod> payMethods = mDHandler.getAllExistingPay();
        ArrayList<String> payType = new ArrayList<>();

        for (int i = 0; i < payMethods.size(); i++) {
            payType.add(payMethods.get(i).getPayType());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, payType);
        paymentType.setAdapter(adapter);

        transDate.setText(today);
        final int transNumber = mDHandler.getAllCustomerPayment().size();
        transNo.setText(" " + transNumber);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerPayment customerPayment = new CustomerPayment();

                if (!value.getText().toString().equals("") && !customerBalance.getText().toString().equals("") && !customerName.getText().toString().equals("") && !customerNo.getText().toString().equals("")) {

                    customerPayment.setPointOfSaleNumber(Settings.POS_number);
                    customerPayment.setUserNO(Settings.password);
                    customerPayment.setUserName(Settings.user_name);
                    customerPayment.setCustomerNo(1);
                    customerPayment.setCustomerName("name1");
                    customerPayment.setCustomerBalance(Double.parseDouble(customerBalance.getText().toString()));
                    customerPayment.setTransNo(transNumber);
                    customerPayment.setTransDate(today);
                    customerPayment.setPayMentType(paymentType.getSelectedItem().toString());
                    customerPayment.setValue(Double.parseDouble(value.getText().toString()));
                    customerPayment.setShiftNo(Settings.shift_number);
                    customerPayment.setShiftName(Settings.shift_name);

                    mDHandler.addCustomerPayment(customerPayment);
                    Toast.makeText(BackOfficeActivity.this, "Saved Successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } else {
                    Toast.makeText(BackOfficeActivity.this, " Please fill out all required information ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void showMoneyCategoryDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.monay_category_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(860, 430);

        nextSerial = mDHandler.getAllMoneyCategory().size();
        final ArrayList<Money> money = new ArrayList<>();

        final EditText serial = (EditText) dialog.findViewById(R.id.serial);
        final EditText catName = (EditText) dialog.findViewById(R.id.cat_name);
        final EditText catValue = (EditText) dialog.findViewById(R.id.cat_value);
        moneyPicImageView = (ImageView) dialog.findViewById(R.id.money_pic);
        final CheckBox show = (CheckBox) dialog.findViewById(R.id.show);
        Button add = (Button) dialog.findViewById(R.id.add);
        Button save = (Button) dialog.findViewById(R.id.save);
        Button exit = (Button) dialog.findViewById(R.id.exit);
        serial.setText("" + nextSerial);

        moneyPicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddPictureDialog();

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkMoneyInputs(serial.getText().toString(), catName.getText().toString(),
                        catValue.getText().toString(), imageBitmap)) {

                    Money m = new Money();
                    m.setSerial(Integer.parseInt(serial.getText().toString()));
                    m.setCatName(catName.getText().toString());
                    m.setCatValue(Integer.parseInt(catValue.getText().toString()));
                    m.setPicture(imageBitmap);
                    if (show.isChecked())
                        m.setShow(1);
                    else
                        m.setShow(0);
                    money.add(m);

                    serial.setText("" + (nextSerial + 1));
                    catName.setText("");
                    catValue.setText("");
                    moneyPicImageView.setImageDrawable(getResources().getDrawable(R.drawable.focused_table));
                    imageBitmap = null;
                    show.setChecked(true);
                    nextSerial++;
                    Toast.makeText(BackOfficeActivity.this, "Added to list", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(BackOfficeActivity.this, "Please insure your inputs", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!catName.getText().toString().equals("") && !catValue.getText().toString().equals("")) {
                    mDHandler.addMoneyCategory(money);
                    dialog.dismiss();
                } else
                    Toast.makeText(BackOfficeActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(BackOfficeActivity.this);
                builder1.setMessage("Your inputs will be lost, are you sure you want to dismiss ?");
                builder1.setCancelable(false);

                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                dialog1.cancel();
                                dialog.dismiss();
                            }
                        });

                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                dialog1.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        dialog.show();
    }

    void showAddPictureDialog() {

        final Dialog dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_picture_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(650, 410);

        final ArrayList<Money> money = mDHandler.getAllMoneyCategory();

        LinearLayout pics = (LinearLayout) dialog.findViewById(R.id.usedPictures);

        for (int i = money.size() - 1; i >= 0; i--) {

            final Bitmap pic = money.get(i).getPicture();
            if (pic != null) {
                RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                imageParams.setMargins(5, 0, 5, 0);

                ImageView newPic = new ImageView(BackOfficeActivity.this);
                newPic.setLayoutParams(imageParams);
                newPic.setImageDrawable(new BitmapDrawable(getResources(), pic));

                newPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moneyPicImageView.setImageDrawable(new BitmapDrawable(getResources(), pic));
                        imageBitmap = pic;
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
                intent.putExtra("crop", "false");
//                intent.putExtra("scale", true);
                intent.putExtra("outputX", 456);
                intent.putExtra("outputY", 256);
//                intent.putExtra("aspectX", 1);
//                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 1);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void showModifierDialog() {

        final Dialog dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.modifier_dialog);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout addMod = (LinearLayout) dialog.findViewById(R.id.add_modifier);
        LinearLayout itemWithMod = (LinearLayout) dialog.findViewById(R.id.item_with_mod);
        LinearLayout catWithMod = (LinearLayout) dialog.findViewById(R.id.cat_with_mod);

        addMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddModifierDialog();
            }
        });
        itemWithMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showJoinItemWithModifierDialog();
            }
        });
        catWithMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showJoinCategoryWithModifier();
            }
        });

        dialog.show();
    }

    void showForceQuestionDialog() {

        final Dialog dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.force_question_dialog);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout addfq = (LinearLayout) dialog.findViewById(R.id.add_question);
        LinearLayout itemWithFq = (LinearLayout) dialog.findViewById(R.id.item_with_fq);

        addfq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddQuestionDialog();
            }
        });
        itemWithFq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showJoinItemWithForceQuestionDialog();
            }
        });
        dialog.show();
    }

    void showAddModifierDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_modifier_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final int Serial = mDHandler.getAllModifiers().size();

        TextView serial = (TextView) dialog.findViewById(R.id.modifierNumber);
        final EditText modifierName = (EditText) dialog.findViewById(R.id.modifier_name);
        final CheckBox active = (CheckBox) dialog.findViewById(R.id.active);
        Button addModifier = (Button) dialog.findViewById(R.id.add_modifier);
        serial.setText(String.valueOf(Serial));

        addModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!modifierName.getText().toString().equals("")) {
                    Modifier modifier1 = new Modifier();

                    modifier1.setModifierActive(active.isChecked() ? 1 : 0);
                    modifier1.setModifierName(modifierName.getText().toString());
                    modifier1.setModifierNumber(Serial);

                    mDHandler.addModifierItem(modifier1);
                    dialog.dismiss();
                } else
                    Toast.makeText(BackOfficeActivity.this, "Please add modifier", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

    void showAddQuestionDialog() {

        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_force_question_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(950, 500);

        final EditText serialText = (EditText) dialog.findViewById(R.id.question_no);
        final EditText questionText = (EditText) dialog.findViewById(R.id.question_text);
        final RadioButton multipleRadioButton = (RadioButton) dialog.findViewById(R.id.multiple_answer);
        final Button add = (Button) dialog.findViewById(R.id.add_question);
        final Button delete = (Button) dialog.findViewById(R.id.delete_question);
        final Button save = (Button) dialog.findViewById(R.id.save);
        final Button exit = (Button) dialog.findViewById(R.id.exit);
        final LinearLayout answers = (LinearLayout) dialog.findViewById(R.id.answers);
        final LinearLayout selectedAnswers = (LinearLayout) dialog.findViewById(R.id.selected_answers);

        ArrayList<ForceQuestions> forceQuestions = mDHandler.getAllForceQuestions();
        if (forceQuestions.size() != 0)
            serialText.setText("" + (forceQuestions.get(forceQuestions.size() - 1).getQuestionNo() + 1));
        else
            serialText.setText("0");

        ArrayList<Modifier> modifiers = mDHandler.getAllModifiers();
        Log.e("fff", "" + modifiers.size());

        for (int i = 0; i < modifiers.size(); i++) {
            CheckBox checkBox = new CheckBox(BackOfficeActivity.this);
            checkBox.setText(modifiers.get(i).getModifierName());
            checkBox.setTextColor(getResources().getColor(R.color.text_color));
            checkBox.setTextSize(20);

            answers.addView(checkBox);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answers.getChildCount() != 0) {
                    int childCount = answers.getChildCount();
                    for (int i = childCount - 1; i >= 0; i--) {
                        CheckBox checkBox = (CheckBox) answers.getChildAt(i);
                        if (checkBox.isChecked()) {
                            answers.removeView(checkBox);
                            selectedAnswers.addView(checkBox);
                            checkBox.setChecked(false);

                        }
                    }
                } else
                    Toast.makeText(BackOfficeActivity.this, "No answers to be added !", Toast.LENGTH_LONG).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAnswers.getChildCount() != 0) {
                    int childCount = selectedAnswers.getChildCount();
                    for (int i = childCount - 1; i >= 0; i--) {
                        CheckBox checkBox = (CheckBox) selectedAnswers.getChildAt(i);
                        if (checkBox.isChecked()) {
                            selectedAnswers.removeView(checkBox);
                            answers.addView(checkBox);
                            checkBox.setChecked(false);
                        }
                    }
                } else
                    Toast.makeText(BackOfficeActivity.this, "No selected answers to be removed !", Toast.LENGTH_LONG).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int multipleAnswer = multipleRadioButton.isChecked() ? 1 : 0;
                if (!questionText.getText().toString().equals("") && selectedAnswers.getChildCount() != 0) {

                    for (int i = 0; i < selectedAnswers.getChildCount(); i++) {
                        CheckBox checkBox = (CheckBox) selectedAnswers.getChildAt(i);
                        mDHandler.addForceQuestion(new ForceQuestions(Integer.parseInt(serialText.getText().toString()),
                                questionText.getText().toString(), multipleAnswer, checkBox.getText().toString()));
                        dialog.dismiss();
                    }
                } else
                    Toast.makeText(BackOfficeActivity.this, "Please insure your inputs", Toast.LENGTH_LONG).show();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void showJoinCategoryWithModifier() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.join_category_with_modifier_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(950, 500);

        final Spinner paperSpinner = (Spinner) dialog.findViewById(R.id.modifier_list);
        final LinearLayout modifiersLinearLayout = (LinearLayout) dialog.findViewById(R.id.categories);
        final TableLayout itemsTableLayout = (TableLayout) dialog.findViewById(R.id.items);
        final Button add = (Button) dialog.findViewById(R.id.add_category);
        final Button save = (Button) dialog.findViewById(R.id.save);
        final Button exit = (Button) dialog.findViewById(R.id.exit);


        final List<Modifier> items = mDHandler.getAllModifiers();
        final ArrayList<String> modifierName = new ArrayList<>();
        modifierName.add("");
        for (int i = 0; i < items.size(); i++) {
            modifierName.add(items.get(i).getModifierName());
        }
        ArrayAdapter<String> paperAdapter = new ArrayAdapter<String>(BackOfficeActivity.this, R.layout.spinner_style, modifierName);
        paperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paperSpinner.setAdapter(paperAdapter);
        paperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!paperSpinner.getSelectedItem().toString().equals("")) {
                    insertRaw2(items.get(i - 1), itemsTableLayout, "category");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> category = mDHandler.getAllExistingCategories();
        for (int i = 0; i < category.size(); i++) {
            CheckBox checkBox = new CheckBox(BackOfficeActivity.this);
            checkBox.setText("- " + category.get(i));
            checkBox.setTextColor(getResources().getColor(R.color.text_color));
            checkBox.setTextSize(20);

            modifiersLinearLayout.addView(checkBox);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedRaw != null) {
                    if (modifiersLinearLayout.getChildCount() != 0) {
                        int childCount = modifiersLinearLayout.getChildCount();
                        for (int i = childCount - 1; i >= 0; i--) {
                            CheckBox checkBox = (CheckBox) modifiersLinearLayout.getChildAt(i);
                            if (checkBox.isChecked()) {
                                checkBox.setChecked(false);

                                TextView ModNa = (TextView) focusedRaw.getChildAt(0);
                                TextView category = (TextView) focusedRaw.getChildAt(1);

                                if (category.getText().toString().equals("no category")) {
                                    category.setText(checkBox.getText().toString());
                                } else {
                                    category.setText(category.getText().toString() + "\n" + checkBox.getText().toString());
                                }
                                categoryWithModifiersList.add(new CategoryWithModifier(ModNa.getText().toString(),
                                        checkBox.getText().toString()));
                            }
                        }
                    } else
                        Toast.makeText(BackOfficeActivity.this, "No category to be added !", Toast.LENGTH_LONG).show();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < categoryWithModifiersList.size(); i++) {
                    if (!categoryWithModifiersList.get(i).getCategoryName().equals("no category")) {
                        mDHandler.addCategoriesModifier(categoryWithModifiersList.get(i));

                    }
                }
                focusedRaw = null;
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedRaw = null;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void showJoinItemWithModifierDialog() {

        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.join_item_with_modifier_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final Spinner paperSpinner = (Spinner) dialog.findViewById(R.id.items_list);
        final LinearLayout modifiersLinearLayout = (LinearLayout) dialog.findViewById(R.id.modifiers);
        final TableLayout itemsTableLayout = (TableLayout) dialog.findViewById(R.id.items);
        final Button add = (Button) dialog.findViewById(R.id.add_modifier);
        final Button save = (Button) dialog.findViewById(R.id.save);
        final Button exit = (Button) dialog.findViewById(R.id.exit);


        final List<Items> items = mDHandler.getAllItems();
        final ArrayList<String> itemName = new ArrayList<>();
        itemName.add("");
        for (int i = 0; i < items.size(); i++) {
            itemName.add(items.get(i).getMenuName());
        }
        ArrayAdapter<String> paperAdapter = new ArrayAdapter<String>(BackOfficeActivity.this, R.layout.spinner_style, itemName);
        paperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paperSpinner.setAdapter(paperAdapter);
        paperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!paperSpinner.getSelectedItem().toString().equals("")) {
                    insertRaw(items.get(i - 1), itemsTableLayout, "modifier");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<Modifier> modifiers = mDHandler.getAllModifiers();
        for (int i = 0; i < modifiers.size(); i++) {
            CheckBox checkBox = new CheckBox(BackOfficeActivity.this);
            checkBox.setText("- " + modifiers.get(i).getModifierName());
            checkBox.setTag(modifiers.get(i).getModifierNumber());
            checkBox.setTextColor(getResources().getColor(R.color.text_color));
            checkBox.setTextSize(20);

            modifiersLinearLayout.addView(checkBox);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedRaw != null) {
                    if (modifiersLinearLayout.getChildCount() != 0) {
                        int childCount = modifiersLinearLayout.getChildCount();
                        for (int i = childCount - 1; i >= 0; i--) {
                            CheckBox checkBox = (CheckBox) modifiersLinearLayout.getChildAt(i);
                            if (checkBox.isChecked()) {
                                checkBox.setChecked(false);

                                TextView itemBa = (TextView) focusedRaw.getChildAt(0);
                                TextView itemNa = (TextView) focusedRaw.getChildAt(1);
                                TextView ModNo = (TextView) focusedRaw.getChildAt(2);
                                TextView ModTe = (TextView) focusedRaw.getChildAt(3);

                                if (ModTe.getText().toString().equals("no modifier")) {
                                    ModNo.setText(checkBox.getTag().toString());
                                    ModTe.setText(checkBox.getText().toString());
                                } else {
                                    ModNo.setText(ModNo.getText().toString() + " , " + checkBox.getTag().toString());
                                    ModTe.setText(ModTe.getText().toString() + "\n" + checkBox.getText().toString());
                                }
                                itemWithModifiersList.add(new ItemWithModifier(Integer.parseInt(itemBa.getText().toString()),
                                        Integer.parseInt(checkBox.getTag().toString()), checkBox.getText().toString()));
                            }
                        }
                    } else
                        Toast.makeText(BackOfficeActivity.this, "No answers to be added !", Toast.LENGTH_LONG).show();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < itemWithModifiersList.size(); i++) {
                    if (itemWithModifiersList.get(i).getModifierNo() != -1) {
                        mDHandler.addItemWithModifier(itemWithModifiersList.get(i));
                    }
                }
                focusedRaw = null;
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedRaw = null;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void showJoinItemWithForceQuestionDialog() {

        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.join_item_with_fq_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(950, 500);

        final Spinner paperSpinner = (Spinner) dialog.findViewById(R.id.items_list);
        final LinearLayout questionsLinearLayout = (LinearLayout) dialog.findViewById(R.id.questions);
        final TableLayout itemsTableLayout = (TableLayout) dialog.findViewById(R.id.items);
        final Button add = (Button) dialog.findViewById(R.id.add_question);
        final Button save = (Button) dialog.findViewById(R.id.save);
        final Button exit = (Button) dialog.findViewById(R.id.exit);


        final List<Items> items = mDHandler.getAllItems();
        final ArrayList<String> itemName = new ArrayList<>();
        itemName.add("");
        for (int i = 0; i < items.size(); i++) {
            itemName.add(items.get(i).getMenuName());
        }
        ArrayAdapter<String> paperAdapter = new ArrayAdapter<String>(BackOfficeActivity.this, R.layout.spinner_style, itemName);
        paperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paperSpinner.setAdapter(paperAdapter);
        paperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!paperSpinner.getSelectedItem().toString().equals("")) {
                    insertRaw(items.get(i - 1), itemsTableLayout, "question");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<ForceQuestions> questions = mDHandler.getSomeForceQuestions();
        for (int i = 0; i < questions.size(); i++) {
            CheckBox checkBox = new CheckBox(BackOfficeActivity.this);
            checkBox.setText("- " + questions.get(i).getQuestionText());
            checkBox.setTag(questions.get(i).getQuestionNo());
            checkBox.setTextColor(getResources().getColor(R.color.text_color));
            checkBox.setTextSize(20);

            questionsLinearLayout.addView(checkBox);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedRaw != null) {
                    if (questionsLinearLayout.getChildCount() != 0) {
                        int childCount = questionsLinearLayout.getChildCount();
                        for (int i = childCount - 1; i >= 0; i--) {
                            CheckBox checkBox = (CheckBox) questionsLinearLayout.getChildAt(i);
                            if (checkBox.isChecked()) {
                                checkBox.setChecked(false);

                                TextView itemBa = (TextView) focusedRaw.getChildAt(0);
                                TextView itemNa = (TextView) focusedRaw.getChildAt(1);
                                TextView QusNo = (TextView) focusedRaw.getChildAt(2);
                                TextView QusTe = (TextView) focusedRaw.getChildAt(3);

                                if (QusTe.getText().toString().equals("no question")) {
                                    QusNo.setText(checkBox.getTag().toString());
                                    QusTe.setText(checkBox.getText().toString());
                                } else {
                                    QusNo.setText(QusNo.getText().toString() + " , " + checkBox.getTag().toString());
                                    QusTe.setText(QusTe.getText().toString() + "\n" + checkBox.getText().toString());
                                }
                                itemWithFqsList.add(new ItemWithFq(Integer.parseInt(itemBa.getText().toString()),
                                        Integer.parseInt(checkBox.getTag().toString()), checkBox.getText().toString()));
                            }
                        }
                    } else
                        Toast.makeText(BackOfficeActivity.this, "No answers to be added !", Toast.LENGTH_LONG).show();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < itemWithFqsList.size(); i++) {
                    if (itemWithFqsList.get(i).getQuestionNo() != -1) {
                        mDHandler.addItemWithFQ(itemWithFqsList.get(i));
                    }
                }
                focusedRaw = null;
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedRaw = null;
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    void salesTotalDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sales_total_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final int[] posNoString = {-1};
        final String[] shiftNameString = {"All"};
        final String[] userString = {"All"};

        final TextView salesText, returnsText, netSalesText, salesDiscountText, returnsDiscountText, netDiscountText, salesServiceText, returnsServiceText, netServiceText, cashText,
                visaText, masterText, chequeText, netPayMethodText, pointText, giftText, creditText;
        final Spinner shiftName, posNo, users;
        Button done, exit;

        done = (Button) dialog.findViewById(R.id.doneReport);
        exit = (Button) dialog.findViewById(R.id.exitReport);

        shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        posNo = (Spinner) dialog.findViewById(R.id.posNo);
        users = (Spinner) dialog.findViewById(R.id.user);

        fromDate = (TextView) dialog.findViewById(R.id.fDate);
        toDate = (TextView) dialog.findViewById(R.id.tDate);
        salesText = (TextView) dialog.findViewById(R.id.sales);
        returnsText = (TextView) dialog.findViewById(R.id.returns);
        netSalesText = (TextView) dialog.findViewById(R.id.netSales);
        salesDiscountText = (TextView) dialog.findViewById(R.id.salesDiscount);
        returnsDiscountText = (TextView) dialog.findViewById(R.id.returnDiscount);
        netDiscountText = (TextView) dialog.findViewById(R.id.netDiscount);
        salesServiceText = (TextView) dialog.findViewById(R.id.salesService);
        returnsServiceText = (TextView) dialog.findViewById(R.id.returnServis);
        netServiceText = (TextView) dialog.findViewById(R.id.netService);
        cashText = (TextView) dialog.findViewById(R.id.cash);
        visaText = (TextView) dialog.findViewById(R.id.visa);
        masterText = (TextView) dialog.findViewById(R.id.master);
        chequeText = (TextView) dialog.findViewById(R.id.cheque);
        netPayMethodText = (TextView) dialog.findViewById(R.id.totalPay);
        pointText = (TextView) dialog.findViewById(R.id.points);
        giftText = (TextView) dialog.findViewById(R.id.gifts);
        creditText = (TextView) dialog.findViewById(R.id.credits);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today = df.format(currentTimeAndDate);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for(int i=0;i<mDHandler.getAllShifts().size();i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0,"All");

        for(int i=0;i<mDHandler.getAllEmployeeRegistration().size();i++) {
            if(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType()==0)
            {userArray.add(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeName());}
        }
        userArray.add(0,"All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        users.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        posNo.setAdapter(adapterPosNo);

        headerData = new ArrayList<OrderHeader>();

        headerData = mDHandler.getAllOrderHeader();

        fromDate.setOnClickListener(dateClick);
        toDate.setOnClickListener(dateClick);

        mdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                test.setText(dayOfMonth + "-" + month + "-" + year);
                Log.e("date ",""+dayOfMonth + "-" + month + "-" + year);
            }
        };

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double sales = 0.0, returns = 0.0, allDiscountSales = 0.0, allDiscountReturn = 0.0,
                        totalServiceSales = 0.0, totalServiceReturn = 0.0, cashValue = 0.0, pointValue = 0.0, visaValue = 0.0, masterValue = 0.0, giftValue = 0.0, creditValue = 0.0, chequeValue = 0.0, netSales = 0.0, netPayMethod = 0.0, netDiscount = 0.0, netService = 0.0;

                userString[0] = users.getSelectedItem().toString();
                shiftNameString[0] = shiftName.getSelectedItem().toString();

               if(posNo.getSelectedItem().toString().equals("All")){
                   posNoString[0]=-1;
               }else{ posNoString[0]=Integer.parseInt(posNo.getSelectedItem().toString());}

                for (int i = 0; i < headerData.size(); i++) {
                    if (filters(i)) {//1
                        if (headerData.get(i).getShiftName().equals(shiftNameString[0]) || shiftNameString[0].equals("All")) {
                            if ( headerData.get(i).getWaiter().equals(userString[0]) ||userString[0].equals("All")) {
                                if (headerData.get(i).getPointOfSaleNumber()==posNoString[0]|| posNoString[0]==-1) {
                                    if (headerData.get(i).getOrderKind() == 0) {
                                        sales += headerData.get(i).getAmountDue();
                                        allDiscountSales += headerData.get(i).getAllDiscount();
                                        totalServiceSales += headerData.get(i).getTotalService();
                                    } else if (headerData.get(i).getOrderKind() == 998) {
                                        returns += headerData.get(i).getAmountDue();
                                        allDiscountReturn += headerData.get(i).getAllDiscount();
                                        totalServiceReturn += headerData.get(i).getTotalService();
                                    }

//                        if(headerData.get(i).) {     ///in this side we must
                                    visaValue += headerData.get(i).getCardsValue();
                                    masterValue += headerData.get(i).getCardsValue();
//                        }
                                    cashValue += headerData.get(i).getCashValue();
                                    pointValue += headerData.get(i).getPointValue();
                                    giftValue += headerData.get(i).getGiftValue();
                                    creditValue += headerData.get(i).getCouponValue();  /////???? replace coupon to credit """"
                                    chequeValue += headerData.get(i).getChequeValue();

                                }
                            }
                        }
                    }//else 1
                }
                netSales = sales - returns;
                netDiscount = allDiscountSales - allDiscountReturn;
                netService = totalServiceSales - totalServiceReturn;
                netPayMethod = cashValue + pointValue + visaValue + masterValue + giftValue + creditValue + chequeValue;

                salesText.setText("" + sales);
                returnsText.setText("" + returns);
                netSalesText.setText("" + netSales);

                cashText.setText("" + cashValue);
                pointText.setText("" + pointValue);
                creditText.setText("" + creditValue);
                giftText.setText("" + giftValue);
                visaText.setText("" + visaValue);
                masterText.setText("" + masterValue);
                chequeText.setText("" + chequeValue);
                netPayMethodText.setText("" + netPayMethod);

                salesDiscountText.setText("" + allDiscountSales);
                returnsDiscountText.setText("" + allDiscountReturn);
                netDiscountText.setText("" + netDiscount);


                salesServiceText.setText("" + totalServiceSales);
                returnsServiceText.setText("" + totalServiceReturn);
                netServiceText.setText("" + netService);

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    View.OnClickListener dateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.fDate:
                    test = fromDate;
                    break;

                case R.id.tDate:
                    test = toDate;
                    break;
            }

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_WEEK);

            DatePickerDialog dialogs = new DatePickerDialog(BackOfficeActivity.this, android.R.style.Theme_DeviceDefault_DialogWhenLarge, mdate, year, month, day);
            dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Theme_Holo_Dialog_MinWidth
            dialogs.show();

        }
    };

    public boolean filters(int n) {


        String fromDate1 = fromDate.getText().toString().trim();
        String toDate1 = toDate.getText().toString();

        String date = headerData.get(n).getVoucherDate();

        try {

            if ((formatDate(date).after(formatDate(fromDate1)) || formatDate(date).equals(formatDate(fromDate1))) &&
                    (formatDate(date).before(formatDate(toDate1)) || formatDate(date).equals(formatDate(toDate1))))
                return true;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Date formatDate(String date) throws ParseException {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d;
    }

    void showJobGroupDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.job_group_dialog);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();


        count = 0;
        final LinearLayout linearLayoutNo, linearLayoutJob;
        final EditText jobGroupText;
        Button newButton, saveButton, deleteButton;
        final CheckBox jobCheck;
        jobTable = (TableLayout) dialog.findViewById(R.id.job_group);
        final ArrayList<String> jobGroup = new ArrayList<>();
        final ArrayList<Integer> jobActive = new ArrayList<>();
        jobGroupText = (EditText) dialog.findViewById(R.id.jobGroup);

        newButton = (Button) dialog.findViewById(R.id.newButton1);
        saveButton = (Button) dialog.findViewById(R.id.saveButton1);
        deleteButton = (Button) dialog.findViewById(R.id.deleteButton1);
        jobCheck = (CheckBox) dialog.findViewById(R.id.jobCheck);
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String today1 = df.format(currentTimeAndDate);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!jobGroup.isEmpty()) {
                    for (int i = 0; i < jobGroup.size(); i++) {
                        JobGroup jobGroups = new JobGroup();
                        String text = jobGroup.get(i).toString();
                        jobGroups.setJobGroup(text);
                        jobGroups.setInDate(today1);
                        jobGroups.setUserName(Settings.user_name);
                        jobGroups.setUserNo(Settings.password);
                        jobGroups.setShiftName(Settings.shift_name);
                        jobGroups.setShiftNo(Settings.shift_number);
                        jobGroups.setActive(jobActive.get(i));

                        mDHandler.addJobGroup(jobGroups);
                    }
                    Toast.makeText(BackOfficeActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(BackOfficeActivity.this, "  Please Add Job Group Filled   ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String job = jobGroupText.getText().toString();
                if (!job.equals("")) {
                    jobGroup.add(job);
                    if (jobCheck.isChecked()) {
                        jobActive.add(1);
                    } else {
                        jobActive.add(0);
                    }
                    insertRaw3(count, job, jobTable);
                    count++;
                    jobGroupText.setText("");
                } else {
                    Toast.makeText(BackOfficeActivity.this, "  Please Enter Job Group Filled   ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    void showAddShiftDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_shift_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final TableLayout tableLayout = dialog.findViewById(R.id.tableLayout1);
        final EditText shiftNo = dialog.findViewById(R.id.shift_no);
        final EditText shiftName = dialog.findViewById(R.id.shift_name);
        final EditText fromTime = dialog.findViewById(R.id.from);
        final EditText toTime = dialog.findViewById(R.id.to);
        Button add = dialog.findViewById(R.id.add);
        Button save = dialog.findViewById(R.id.save);
        Button exit = dialog.findViewById(R.id.exit);

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(BackOfficeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = "AM";

                                } else {
                                    AM_PM = "PM";
                                    hourOfDay -= 12;
                                }
                                fromTime.setText(hourOfDay + ":" + minute + " " + AM_PM);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(BackOfficeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = "AM";

                                } else {
                                    AM_PM = "PM";
                                    hourOfDay -= 12;
                                }
                                toTime.setText(hourOfDay + ":" + minute + " " + AM_PM);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shiftNo.getText().toString().equals("") && !shiftName.getText().toString().equals("") &&
                        !fromTime.getText().toString().equals("") && !toTime.getText().toString().equals("")) {

                    final TableRow row = new TableRow(BackOfficeActivity.this);

                    TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
                    lp.setMargins(2, 0, 2, 0);
                    row.setLayoutParams(lp);

                    for (int i = 0; i < 4; i++) {
                        TextView textView = new TextView(BackOfficeActivity.this);

                        switch (i) {
                            case 0:
                                textView.setText(shiftNo.getText().toString());
                                break;
                            case 1:
                                textView.setText(shiftName.getText().toString());
                                break;
                            case 2:
                                textView.setText(fromTime.getText().toString());
                                break;
                            case 3:
                                textView.setText(toTime.getText().toString());
                                break;
                        }

                        textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                        textView.setGravity(Gravity.CENTER);

                        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                        textView.setLayoutParams(lp1);

                        row.addView(textView);
                    }
                    tableLayout.addView(row);

                    shiftNo.setText("");
                    shiftName.setText("");
                    fromTime.setText("");
                    toTime.setText("");

                } else
                    Toast.makeText(BackOfficeActivity.this, "please fill requested fields", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableLayout.getChildCount() != 0) {
                    for (int i = 0; i < tableLayout.getChildCount(); i++) {
                        TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                        TextView shNo = (TextView) tableRow.getChildAt(0);
                        TextView shName = (TextView) tableRow.getChildAt(1);
                        TextView from = (TextView) tableRow.getChildAt(2);
                        TextView to = (TextView) tableRow.getChildAt(3);

                        mDHandler.addShift(new Shift(Integer.parseInt(shNo.getText().toString()), shName.getText().toString(),
                                from.getText().toString(), to.getText().toString()));

                        Toast.makeText(BackOfficeActivity.this, "saved !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } else
                    Toast.makeText(BackOfficeActivity.this, "No shifts to be saved !", Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void showMemberShipGroupDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.member_ship_group_managment_dialog);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();

        count2 = 0;
        final EditText memberGroupText;
        Button newButton, saveButton, deleteButton;
        final CheckBox active;
        final ArrayList<String> memberGroup = new ArrayList<>();
        final ArrayList<Integer> memberActive = new ArrayList<>();
        final TableLayout tableLayoutMember;

        memberGroupText = (EditText) dialog.findViewById(R.id.jobGroup2);
        newButton = (Button) dialog.findViewById(R.id.newButton2);
        saveButton = (Button) dialog.findViewById(R.id.saveButton2);
        deleteButton = (Button) dialog.findViewById(R.id.deleteButton2);
        active = (CheckBox) dialog.findViewById(R.id.memberCheck);
        tableLayoutMember = (TableLayout) dialog.findViewById(R.id.member_table);


        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String today1 = df.format(currentTimeAndDate);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!memberGroup.isEmpty()) {
                    for (int i = 0; i < memberGroup.size(); i++) {
                        MemberShipGroup memberShipGroup = new MemberShipGroup();
                        String text = memberGroup.get(i).toString();
                        memberShipGroup.setMemberShipGroup(text);
                        memberShipGroup.setInDate(today1);
                        memberShipGroup.setUserName(Settings.user_name);
                        memberShipGroup.setUserNo(Settings.password);
                        memberShipGroup.setShiftName(Settings.shift_name);
                        memberShipGroup.setShiftNo(Settings.shift_number);
                        memberShipGroup.setActive(memberActive.get(i));

                        mDHandler.addMemberShipGroup(memberShipGroup);
                    }
                    Toast.makeText(BackOfficeActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(BackOfficeActivity.this, "  Please Add Member Ship Group   ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String job = memberGroupText.getText().toString();
                if (!job.equals("")) {
                    memberGroup.add(job);
                    if (active.isChecked()) {
                        memberActive.add(1);
                    } else {
                        memberActive.add(0);
                    }
                    insertRaw3(count2, job, tableLayoutMember);
                    count2++;
                    memberGroupText.setText("");
                } else {
                    Toast.makeText(BackOfficeActivity.this, "  Please Enter Member Ship Group Filled   ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    void showEditTablesAuthorizationDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.table_edit_outhorization_dialog);
        dialog.setCanceledOnTouchOutside(true);

        final EditText editText = (EditText) dialog.findViewById(R.id.password);
        Button buttonDone = (Button) dialog.findViewById(R.id.b_done);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().equals("")) {
                    if (Integer.parseInt(editText.getText().toString()) == 4444) {
                        Settings settings = new Settings();
                        settings.table_edit_authorized = true;
                        Toast.makeText(BackOfficeActivity.this, "Your'r authorized to edit tables ", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Settings settings = new Settings();
                        settings.table_edit_authorized = false;
                        Toast.makeText(BackOfficeActivity.this, "Your authorization number is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();

    }

    void insertRaw(Items items, final TableLayout itemsTableLayout, String text) {
        final TableRow row = new TableRow(BackOfficeActivity.this);

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
        lp.setMargins(2, 2, 2, 0);
        row.setLayoutParams(lp);
        row.setTag(rawPosition);
        for (int k = 0; k < 4; k++) {
            TextView textView = new TextView(BackOfficeActivity.this);

            switch (k) {
                case 0:
                    textView.setText("" + items.getItemBarcode());
                    break;
                case 1:
                    textView.setText(items.getMenuName());
                    break;
                case 2:
                    textView.setText("" + -1);
                    break;
                case 3:
                    textView.setText("no " + text);
                    break;
            }

            textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
            textView.setGravity(Gravity.CENTER);

            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            textView.setLayoutParams(lp2);

            row.addView(textView);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // remove focused rows
                    for (int k = 0; k < itemsTableLayout.getChildCount(); k++) {
                        TableRow tableRow = (TableRow) itemsTableLayout.getChildAt(k);
                        tableRow.setBackgroundColor(getResources().getColor(R.color.layer3));
                    }
                    focusedRaw = row;
                    focusedRaw.setBackgroundColor(getResources().getColor(R.color.layer4));
                }
            });
        }
        itemsTableLayout.addView(row);
        rawPosition += 1;
    }

    void insertRaw2(Modifier items, final TableLayout itemsTableLayout, String text) {
        final TableRow row = new TableRow(BackOfficeActivity.this);

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams();
        lp.setMargins(2, 2, 2, 0);
        row.setLayoutParams(lp);
        row.setTag(rawPosition);
        for (int k = 0; k < 2; k++) {
            TextView textView = new TextView(BackOfficeActivity.this);

            switch (k) {
                case 0:
                    textView.setText("" + items.getModifierName());
                    break;
                case 1:
                    textView.setText("no " + text);
                    break;
            }

            textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
            textView.setGravity(Gravity.CENTER);

            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            textView.setLayoutParams(lp2);

            row.addView(textView);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // remove focused rows
                    for (int k = 0; k < itemsTableLayout.getChildCount(); k++) {
                        TableRow tableRow = (TableRow) itemsTableLayout.getChildAt(k);
                        tableRow.setBackgroundColor(getResources().getColor(R.color.layer3));
                    }
                    focusedRaw = row;
                    focusedRaw.setBackgroundColor(getResources().getColor(R.color.layer4));
                }
            });
        }
        itemsTableLayout.addView(row);
        rawPosition += 1;
    }

    void insertRaw3(int number, String string, TableLayout tableLayout) {

        if (true) {
            final TableRow row = new TableRow(BackOfficeActivity.this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            for (int i = 0; i < 2; i++) {
                TextView textView = new TextView(BackOfficeActivity.this);

                switch (i) {
                    case 0:
                        textView.setText("" + number);
                        break;
                    case 1:
                        textView.setText(string);
                        break;
                }

                textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                textView.setGravity(Gravity.CENTER);

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                textView.setLayoutParams(lp2);

                row.addView(textView);

            }

            tableLayout.addView(row);
        }
    }

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
                imageBitmap = extras.getParcelable("data");
                moneyPicImageView.setImageDrawable(new BitmapDrawable(getResources(), imageBitmap));
            }
        }
    }

    boolean checkMoneyInputs(String s, String cName, String cValue, Bitmap pic) {
        return !s.equals("") && !cName.equals("") && !cValue.equals("") ;
    }

    void currentLinear(LinearLayout linearLayout) {

        lManagement.setVisibility(View.INVISIBLE);
        lSales.setVisibility(View.INVISIBLE);
        lCustomers.setVisibility(View.INVISIBLE);
        lEmployees.setVisibility(View.INVISIBLE);
        lMenu.setVisibility(View.INVISIBLE);
        lSettings.setVisibility(View.INVISIBLE);

        linearLayout.setVisibility(View.VISIBLE);
    }

    void initialize() {
        butManagement = (Button) findViewById(R.id.b_management);
        butSales = (Button) findViewById(R.id.b_sales);
        butCustomers = (Button) findViewById(R.id.b_customers);
        butEmployees = (Button) findViewById(R.id.b_employees);
        butMenu = (Button) findViewById(R.id.b_menu);
        butSettings = (Button) findViewById(R.id.b_settings);

        lManagement = (LinearLayout) findViewById(R.id.l_management);
        lSales = (LinearLayout) findViewById(R.id.l_sales);
        lCustomers = (LinearLayout) findViewById(R.id.l_customers);
        lEmployees = (LinearLayout) findViewById(R.id.l_employees);
        lMenu = (LinearLayout) findViewById(R.id.l_menu);
        lSettings = (LinearLayout) findViewById(R.id.l_settings);

        announcement = (LinearLayout) findViewById(R.id.announcement);
        giftCard = (LinearLayout) findViewById(R.id.gift_card);
        employeeClockInOut = (LinearLayout) findViewById(R.id.employee_click_out);
        menuSearch = (LinearLayout) findViewById(R.id.menu_search);
        membershipGroup = (LinearLayout) findViewById(R.id.membership_group);
        membership = (LinearLayout) findViewById(R.id.membership);
        customerRegistration = (LinearLayout) findViewById(R.id.customer_reg);
        jobGroup = (LinearLayout) findViewById(R.id.job_group);
        employeeRegistration = (LinearLayout) findViewById(R.id.employee_registration);
        employeeSchedule = (LinearLayout) findViewById(R.id.employee_schedule);
        payroll = (LinearLayout) findViewById(R.id.payroll);
        vacation = (LinearLayout) findViewById(R.id.vacation);
        editTables = (LinearLayout) findViewById(R.id.edit_tables_outhorization);
        menuCategory = (LinearLayout) findViewById(R.id.menu_category);
        menuRegistration = (LinearLayout) findViewById(R.id.menu_registration);
        modifier = (LinearLayout) findViewById(R.id.modifier);
        forceQuestion = (LinearLayout) findViewById(R.id.force_question);
        menuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        store = (LinearLayout) findViewById(R.id.store);
        storeOperation = (LinearLayout) findViewById(R.id.store_operation);
        users = (LinearLayout) findViewById(R.id.users);
        moneyCategory = (LinearLayout) findViewById(R.id.money_category);
        salesTotal = (LinearLayout) findViewById(R.id.sales_total);
        cashierInOut = (LinearLayout) findViewById(R.id.cashier_in_out);
        canceledOrderHistory = (LinearLayout) findViewById(R.id.canceled_order_history);
        dailyCashOut = (LinearLayout) findViewById(R.id.daily_cash_out);
        salesByEmployee = (LinearLayout) findViewById(R.id.sales_by_employee);
        salesByServers = (LinearLayout) findViewById(R.id.sales_by_servers);
        salesReportForDay = (LinearLayout) findViewById(R.id.sales_report_for_day);
        salesByHours = (LinearLayout) findViewById(R.id.sales_by_houres);
        salesVolumeByItem = (LinearLayout) findViewById(R.id.sales_volume_by_item);
        topSalesItemReport = (LinearLayout) findViewById(R.id.top_sales_item_report);
        topGroupSalesReport = (LinearLayout) findViewById(R.id.top_group_sales_report);
        topFamilySalesReport = (LinearLayout) findViewById(R.id.top_family_sales_report);
        salesReportByCustomer = (LinearLayout) findViewById(R.id.sales_report_by_cusromer);
        profitLossReport = (LinearLayout) findViewById(R.id.profit_loss_report);
        detailSalesReport = (LinearLayout) findViewById(R.id.detail_sales_report);


        butManagement.setOnClickListener(onClickListener);
        butSales.setOnClickListener(onClickListener);
        butCustomers.setOnClickListener(onClickListener);
        butEmployees.setOnClickListener(onClickListener);
        butMenu.setOnClickListener(onClickListener);
        butSettings.setOnClickListener(onClickListener);

        announcement.setOnClickListener(onClickListener2);
        giftCard.setOnClickListener(onClickListener2);
        employeeClockInOut.setOnClickListener(onClickListener2);
        menuSearch.setOnClickListener(onClickListener2);
        membershipGroup.setOnClickListener(onClickListener2);
        membership.setOnClickListener(onClickListener2);
        customerRegistration.setOnClickListener(onClickListener2);
        jobGroup.setOnClickListener(onClickListener2);
        employeeRegistration.setOnClickListener(onClickListener2);
        employeeSchedule.setOnClickListener(onClickListener2);
        payroll.setOnClickListener(onClickListener2);
        vacation.setOnClickListener(onClickListener2);
        editTables.setOnClickListener(onClickListener2);
        menuCategory.setOnClickListener(onClickListener2);
        menuRegistration.setOnClickListener(onClickListener2);
        modifier.setOnClickListener(onClickListener2);
        forceQuestion.setOnClickListener(onClickListener2);
        menuLayout.setOnClickListener(onClickListener2);
        store.setOnClickListener(onClickListener2);
        storeOperation.setOnClickListener(onClickListener2);
        users.setOnClickListener(onClickListener2);
        moneyCategory.setOnClickListener(onClickListener2);
        salesTotal.setOnClickListener(onClickListener2);
        cashierInOut.setOnClickListener(onClickListener2);
        canceledOrderHistory.setOnClickListener(onClickListener2);
        dailyCashOut.setOnClickListener(onClickListener2);
        salesByEmployee.setOnClickListener(onClickListener2);
        salesByServers.setOnClickListener(onClickListener2);
        salesReportForDay.setOnClickListener(onClickListener2);
        salesByHours.setOnClickListener(onClickListener2);
        salesVolumeByItem.setOnClickListener(onClickListener2);
        topSalesItemReport.setOnClickListener(onClickListener2);
        topGroupSalesReport.setOnClickListener(onClickListener2);
        topFamilySalesReport.setOnClickListener(onClickListener2);
        salesReportByCustomer.setOnClickListener(onClickListener2);
        profitLossReport.setOnClickListener(onClickListener2);
        detailSalesReport.setOnClickListener(onClickListener2);

    }
}
