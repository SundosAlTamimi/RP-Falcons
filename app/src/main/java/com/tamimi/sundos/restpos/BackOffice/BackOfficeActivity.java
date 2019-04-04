package com.tamimi.sundos.restpos.BackOffice;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tamimi.sundos.restpos.DatabaseHandler;
import com.tamimi.sundos.restpos.DineInLayout;
import com.tamimi.sundos.restpos.Models.BlindClose;
import com.tamimi.sundos.restpos.Models.BlindCloseDetails;
import com.tamimi.sundos.restpos.Models.Announcemet;
import com.tamimi.sundos.restpos.Models.CancleOrder;
import com.tamimi.sundos.restpos.Models.CategoryWithModifier;
import com.tamimi.sundos.restpos.Models.Cheque;
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
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.Pay;
import com.tamimi.sundos.restpos.Models.PayMethod;
import com.tamimi.sundos.restpos.Models.Shift;
import com.tamimi.sundos.restpos.Models.TableActions;
import com.tamimi.sundos.restpos.Models.VoidResons;
import com.tamimi.sundos.restpos.Models.ZReport;
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
    LinearLayout announcement, giftCard, employeeClockInOut, menuSearch, reCancellationSupervisor;
    LinearLayout membershipGroup, membership, customerRegistration;
    LinearLayout jobGroup, employeeRegistration, employeeSchedule, payroll, vacation, editTables;
    LinearLayout menuCategory, menuRegistration, modifier, forceQuestion, voiding_reasons, menuLayout;
    LinearLayout store, storeOperation, users, moneyCategory;
    LinearLayout salesTotal, cashierInOut, canceledOrderHistory, x_report, z_report, market_report_,
            salesReportForDay, salesByHours, salesVolumeByItem, topSalesItemReport, topGroupSalesReport, topFamilySalesReport,
            salesReportByCustomer, salesReportByCardType, waiterSalesReport, tableActionReport, profitLossReport, detailSalesReport,
            simpleSalesTotalReport, SoldQtyReport, userOrderCountReport, reCancellationReport, reCancellationSupervisorReport;

    int count, count2, nextSerial;
    Dialog dialog;
    String today;
    DatabaseHandler mDHandler;
    Bitmap imageBitmap = null;
    ImageView moneyPicImageView = null;

    ArrayList<OrderHeader> headerData, headerDataMarket;
    ArrayList<PayMethod> payData, OrderPayMData;
    List<OrderTransactions> orderTransactionData;
    ArrayList<Pay> payInData;
    ArrayList<Announcemet> Announcement;
    TableRow focusedRaw = null;
    int rawPosition = 0;
    Calendar myCalendar;
    List<BlindCloseDetails> focusedRowData = null;
    TableRow focusedRowReCancellation = null;
    int visible = 0;
    boolean clicked = false;

    ArrayList<ItemWithFq> itemWithFqsList;
    ArrayList<ItemWithModifier> itemWithModifiersList;
    ArrayList<CategoryWithModifier> categoryWithModifiersList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.back_office_activity);

        myCalendar = Calendar.getInstance();

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today = df.format(currentTimeAndDate);

        itemWithFqsList = new ArrayList<>();
        itemWithModifiersList = new ArrayList<>();
        categoryWithModifiersList = new ArrayList<>();
        headerData = new ArrayList<OrderHeader>();

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
                    addAnnouncementDialog();
                    break;
                case R.id.gift_card:
                    break;
                case R.id.employee_click_out:
                    showAddShiftDialog();
                    break;
                case R.id.menu_search:
                    break;
                case R.id.re_cancellation_supervisor:
                    showReCancellationSupervisor();
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
                    Intent intentOrderLayout = new Intent(BackOfficeActivity.this, OrderLayout2.class);
                    startActivity(intentOrderLayout);
                    break;
                case R.id.modifier:
                    showModifierDialog();
                    break;
                case R.id.force_question:
                    showForceQuestionDialog();
                    break;
                case R.id.voiding_reasons:
                    showAddVoidReasonsDialog();
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
                    salesTotalReportDialog();
                    break;
                case R.id.cashier_in_out:
                    cashierInOutReportDialog();
                    break;
                case R.id.canceled_order_history:
                    showCanceledOrdersHistory();
                    break;
                case R.id.x_report:
                    X_ReportDialog();
                    break;
                case R.id.z_report:
                    Z_ReportDialog();
                    break;
                case R.id.market_report_:
                    ShowMarketReport();
                    break;
                case R.id.sales_report_for_day:
                    announcementReportDialog();

                    break;
                case R.id.sales_by_houres:
                    showSalesPerHour();
                    break;
                case R.id.sales_volume_by_item:
                    showSalesVolumeByItemType();
                    break;
                case R.id.top_sales_item_report:
                    showTopSalesItemReport();
                    break;
                case R.id.top_group_sales_report:
                    break;
                case R.id.top_family_sales_report:
                    break;
                case R.id.sales_report_by_cusromer:
                    break;
                case R.id.sales_report_by_card_type:
                    ShowSalesReportByCardTypes();
                    break;
                case R.id.waiter_sales_report:
                    waiterSalesReportDialog();
                    break;
                case R.id.table_action_report:
                    showTablesActionReport();
                    break;
                case R.id.profit_loss_report:
                    break;
                case R.id.detail_sales_report:
                    break;
                case R.id.simple_sales_total_report:
                    showSimpleSalesTotal();
                    break;
                case R.id.sold_qty_report:
                    showSoldQtyReport();
                    break;
                case R.id.user_order_count_report:
                    userOrderCountReportDialog();
                    break;
                case R.id.re_cancellation_report:
                    ShowReCancellationReport();
                    break;
            }
        }
    };

    void showReCancellationSupervisor() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.re_cancellationt_supervisor_dialog);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout hiddenLinear = (LinearLayout) dialog.findViewById(R.id.hiddenLinear);
        TableLayout table = (TableLayout) dialog.findViewById(R.id.canceledTable);
        TableLayout catTable = (TableLayout) dialog.findViewById(R.id.categoryTable);
        EditText creditCard = (EditText) dialog.findViewById(R.id.creditCard);
        EditText cheque = (EditText) dialog.findViewById(R.id.cheque);
        EditText giftCard = (EditText) dialog.findViewById(R.id.giftCard);
        EditText credit = (EditText) dialog.findViewById(R.id.credit);
        EditText point = (EditText) dialog.findViewById(R.id.point);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        Button updateBlind = (Button) dialog.findViewById(R.id.updateBlindClose);
        Button tillOk = (Button) dialog.findViewById(R.id.tillerOk);
        Button printDenom = (Button) dialog.findViewById(R.id.print_denomination);
        Button moveChangeOver = (Button) dialog.findViewById(R.id.move_change_over_to_another_user);

        TextView date = (TextView) dialog.findViewById(R.id.frDate);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        CheckBox adminOkCheck = dialog.findViewById(R.id.admin_ok);

        date.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        date.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(date), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        List<BlindClose> blindCloseList = mDHandler.getAllBlindClose();
        List<BlindCloseDetails> blindCloseDetailsList = mDHandler.getAllBlindCloseDetails();

        preview.setOnClickListener(v -> {

            table.removeAllViews();

            int adminOk = adminOkCheck.isChecked() ? 1 : 0;

            int CashierNo = -1;
            if (cashierNo.getSelectedItem().toString().equals("All"))
                CashierNo = -1;
            else
                CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

            String ShiftName = shiftName.getSelectedItem().toString();

            int posNoString = -1;
            if (PosNo.getSelectedItem().toString().equals("All"))
                posNoString = -1;
            else
                posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

            for (int i = 0; i < blindCloseList.size(); i++) {
                try {
                    if (formatDate(date.getText().toString()).equals(formatDate(blindCloseList.get(i).getDate()))) {
                        if (blindCloseList.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                            if (blindCloseList.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                                if (blindCloseList.get(i).getPOSNo() == posNoString || posNoString == -1) {
                                    if (adminOk == blindCloseList.get(i).getTillOk() || adminOk == 0) {
//
                                        String remark = "-";
                                        if (blindCloseList.get(i).getSalesDiff() < 0)
                                            remark = "short";
                                        if (blindCloseList.get(i).getSalesDiff() > 0)
                                            remark = "over";

                                        String type = "close";
                                        if (blindCloseList.get(i).getTransType() == 1)
                                            type = "change over";

                                        double changeOverValue = 0;
                                        for (int k = 0; k < blindCloseList.size(); k++) {
                                            if (blindCloseList.get(i).getUserName().equals(blindCloseList.get(k).getToUser())) {
                                                changeOverValue = blindCloseList.get(k).getUserSales();
                                                break;
                                            }
                                        }

                                        String updateName = "no-user";
                                        for (int k = 0; k < blindCloseDetailsList.size(); k++) {
                                            if (blindCloseList.get(i).getTransNo() == blindCloseDetailsList.get(k).getTransNo()
                                                    && !blindCloseDetailsList.get(k).getUpdateUserName().equals("no-user")) {

                                                updateName = blindCloseDetailsList.get(k).getUpdateUserName();
                                                break;
                                            }
                                        }

                                        TableRow row = new TableRow(BackOfficeActivity.this);

                                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                        row.setLayoutParams(lp);

                                        for (int k = 0; k < 13; k++) {
                                            TextView textView = new TextView(BackOfficeActivity.this);

                                            switch (k) {
                                                case 0:
                                                    textView.setText(blindCloseList.get(i).getShiftName());
                                                    break;
                                                case 1:
                                                    textView.setText(blindCloseList.get(i).getUserName());
                                                    break;
                                                case 2:
                                                    textView.setText("" + blindCloseList.get(i).getPOSNo());
                                                    break;
                                                case 3:
                                                    textView.setText("" + changeOverValue);
                                                    break;
                                                case 4:
                                                    textView.setText("" + blindCloseList.get(i).getSysSales());
                                                    break;
                                                case 5:
                                                    textView.setText("" + blindCloseList.get(i).getUserSales());
                                                    break;
                                                case 6:
                                                    textView.setText("" + blindCloseList.get(i).getSalesDiff());
                                                    break;
                                                case 7:
                                                    textView.setText("" + blindCloseList.get(i).getReason()); //reason
                                                    break;
                                                case 8:
                                                    textView.setText("" + blindCloseList.get(i).getToUser());
                                                    break;
                                                case 9:
                                                    textView.setText(remark);
                                                    break;
                                                case 10:
                                                    textView.setText(updateName); //updated by
                                                    break;
                                                case 11:
                                                    textView.setText("" + (blindCloseList.get(i).getTillOk() == 0 ? "no" : "yes"));
                                                    break;
                                                case 12:
                                                    textView.setText(type);
                                                    break;
                                            }

                                            textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                                            textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                                            textView.setGravity(Gravity.CENTER);
                                            textView.setTextSize(16);

                                            int transNom = blindCloseList.get(i).getTransNo();
                                            if (k == 7) {
                                                textView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Dialog dialog1 = new Dialog(BackOfficeActivity.this);
                                                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        dialog1.setCancelable(true);
                                                        dialog1.setContentView(R.layout.reason_dialog);
                                                        dialog1.setCanceledOnTouchOutside(true);

                                                        EditText editText = dialog1.findViewById(R.id.reasonBox);
                                                        Button done = dialog1.findViewById(R.id.done);
                                                        done.setOnClickListener(view1 -> {
                                                            textView.setText(editText.getText().toString());
                                                            mDHandler.updateBlindCloseReason(transNom, editText.getText().toString());
                                                            dialog1.dismiss();
                                                        });
                                                        dialog1.show();
                                                    }
                                                });
                                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                                                lp2.setMargins(1, 1, 1, 1);
                                                textView.setLayoutParams(lp2);
                                            } else {
                                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                                lp2.setMargins(1, 1, 1, 1);
                                                textView.setLayoutParams(lp2);
                                            }

                                            row.addView(textView);
                                            row.setOnClickListener(view -> {
                                                for (int j = 0; j < table.getChildCount(); j++) {
                                                    TableRow tableRow = (TableRow) table.getChildAt(j);
                                                    tableRow.setBackgroundDrawable(null);
                                                }
                                                if (!clicked) {
                                                    row.setBackgroundResource(R.drawable.focused_table);
                                                    focusedRowData = mDHandler.getBlindCloseDetails(transNom);
                                                    focusedRowReCancellation = row;
                                                    fillRowDate(catTable, creditCard, cheque, giftCard, credit, point);
                                                    clicked = true;
                                                    if (visible == 0)
                                                        slideUp(hiddenLinear);
                                                } else {
                                                    row.setBackgroundDrawable(null);
                                                    clicked = false;
                                                    slideDown(hiddenLinear);
                                                }
                                            });
                                        }
                                        table.addView(row);
                                    }
                                }
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        updateBlind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (focusedRowData != null) {

                    for (int i = 0; i < catTable.getChildCount(); i++) {
                        TableRow tableRow = (TableRow) catTable.getChildAt(i);
                        EditText qty = (EditText) tableRow.getChildAt(1);
                        if (qty.getText().toString().equals(""))
                            qty.setText("0");
                    }
                    if (creditCard.getText().toString().equals(""))
                        creditCard.setText("0.00");
                    if (cheque.getText().toString().equals(""))
                        cheque.setText("0.00");
                    if (giftCard.getText().toString().equals(""))
                        giftCard.setText("0.00");
                    if (credit.getText().toString().equals(""))
                        credit.setText("0.00");
                    if (point.getText().toString().equals(""))
                        point.setText("0.00");

                    TextView textView4 = (TextView) focusedRowReCancellation.getChildAt(4);
                    TextView textView5 = (TextView) focusedRowReCancellation.getChildAt(5);
                    TextView textView6 = (TextView) focusedRowReCancellation.getChildAt(6);
                    TextView textView10 = (TextView) focusedRowReCancellation.getChildAt(10);

                    Date currentTimeAndDate = Calendar.getInstance().getTime();
                    SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                    String time = tf.format(currentTimeAndDate);

                    double newPhysical = 0;
                    double newOtherPayments = 0;
                    double sysOtherPayments = 0;
                    for (int i = 0; i < blindCloseList.size(); i++) {
                        if (blindCloseList.get(i).getTransNo() == focusedRowData.get(0).getTransNo())
                            sysOtherPayments = blindCloseList.get(i).getSysOthers();
                    }

                    for (int i = 0; i < catTable.getChildCount(); i++) {
                        TableRow tableRow = (TableRow) catTable.getChildAt(i);
                        TextView cat = (TextView) tableRow.getChildAt(0);
                        EditText qty = (EditText) tableRow.getChildAt(1);
                        TextView total = (TextView) tableRow.getChildAt(2);
                        double catValue = focusedRowData.get(i).getCatValue();

                        mDHandler.updateBlindCloseDetails(focusedRowData.get(0).getTransNo(), cat.getText().toString(),
                                Integer.parseInt(qty.getText().toString()), catValue, Double.parseDouble(total.getText().toString()),
                                today, time, Settings.password, Settings.user_name);

                        newPhysical += Double.parseDouble(total.getText().toString());
                    }
                    mDHandler.updateBlindCloseDetails(focusedRowData.get(0).getTransNo(), "Credit Card", 1,
                            Double.parseDouble(creditCard.getText().toString()), Double.parseDouble(creditCard.getText().toString()),
                            today, time, Settings.password, Settings.user_name);
                    mDHandler.updateBlindCloseDetails(focusedRowData.get(0).getTransNo(), "Cheque", 1,
                            Double.parseDouble(cheque.getText().toString()), Double.parseDouble(cheque.getText().toString()),
                            today, time, Settings.password, Settings.user_name);
                    mDHandler.updateBlindCloseDetails(focusedRowData.get(0).getTransNo(), "Gift Card", 1,
                            Double.parseDouble(giftCard.getText().toString()), Double.parseDouble(giftCard.getText().toString()),
                            today, time, Settings.password, Settings.user_name);
                    mDHandler.updateBlindCloseDetails(focusedRowData.get(0).getTransNo(), "Credit", 1,
                            Double.parseDouble(credit.getText().toString()), Double.parseDouble(credit.getText().toString()),
                            today, time, Settings.password, Settings.user_name);
                    mDHandler.updateBlindCloseDetails(focusedRowData.get(0).getTransNo(), "Point", 1,
                            Double.parseDouble(point.getText().toString()), Double.parseDouble(point.getText().toString()),
                            today, time, Settings.password, Settings.user_name);

                    newOtherPayments += Double.parseDouble(creditCard.getText().toString());
                    newOtherPayments += Double.parseDouble(cheque.getText().toString());
                    newOtherPayments += Double.parseDouble(giftCard.getText().toString());
                    newOtherPayments += Double.parseDouble(credit.getText().toString());
                    newOtherPayments += Double.parseDouble(point.getText().toString());

                    mDHandler.updateBlindClose(focusedRowData.get(0).getTransNo(), newPhysical,
                            newPhysical - Double.parseDouble(textView4.getText().toString()),
                            newOtherPayments, newOtherPayments - sysOtherPayments);

                    textView5.setText("" + newPhysical);
                    textView6.setText("" + (newPhysical - Double.parseDouble(textView4.getText().toString())));
                    textView10.setText(Settings.user_name);

                } else // it will never be -_-
                    Toast.makeText(BackOfficeActivity.this, "Please select user", Toast.LENGTH_LONG).show();

            }
        });

        tillOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDHandler.updateBlindCloseTillOk(focusedRowData.get(0).getTransNo());
                TextView textView11 = (TextView) focusedRowReCancellation.getChildAt(11);
                textView11.setText("yes");
            }
        });

        printDenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        moveChangeOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    void ShowReCancellationReport() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.re_cancellationt_report_dialog);
        dialog.setCanceledOnTouchOutside(true);

        TableLayout table = (TableLayout) dialog.findViewById(R.id.canceledTable);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        List<BlindClose> blindClose = mDHandler.getAllBlindClose();
        List<BlindCloseDetails> blindCloseDetails = mDHandler.getAllBlindCloseDetails();
        preview.setOnClickListener(v -> {
            table.removeAllViews();
            int CashierNo = -1;
            if (cashierNo.getSelectedItem().toString().equals("All"))
                CashierNo = -1;
            else
                CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

            String ShiftName = shiftName.getSelectedItem().toString();

            int posNoString = -1;
            if (PosNo.getSelectedItem().toString().equals("All"))
                posNoString = -1;
            else
                posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

            for (int i = 0; i < blindClose.size(); i++) {
                if (filters(fromDate.getText().toString(), toDate.getText().toString(), blindClose.get(i).getDate())) {

                    if (blindClose.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                        if (blindClose.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                            if (blindClose.get(i).getPOSNo() == posNoString || posNoString == -1) {

                                double changeOverValue = 0;
                                for (int k = 0; k < blindClose.size(); k++) {
                                    if (blindClose.get(i).getUserName().equals(blindClose.get(k).getToUser())) {
                                        changeOverValue = blindClose.get(k).getUserSales();
                                        break;
                                    }
                                }

                                String updateName = "no-user";
                                for (int k = 0; k < blindCloseDetails.size(); k++) {
                                    if (blindClose.get(i).getTransNo() == blindCloseDetails.get(k).getTransNo()) {
                                        updateName = blindCloseDetails.get(k).getUpdateUserName();
                                        break;
                                    }
                                }

                                TableRow row = new TableRow(BackOfficeActivity.this);
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                row.setLayoutParams(lp);

                                for (int k = 0; k < 10; k++) {
                                    TextView textView = new TextView(BackOfficeActivity.this);

                                    switch (k) {
                                        case 0:
                                            textView.setText(blindClose.get(i).getDate());
                                            break;
                                        case 1:
                                            textView.setText("" + blindClose.get(i).getPOSNo());
                                            break;
                                        case 2:
                                            textView.setText(blindClose.get(i).getShiftName());
                                            break;
                                        case 3:
                                            textView.setText(blindClose.get(i).getUserName());
                                            break;
                                        case 4:
                                            textView.setText("" + changeOverValue);
                                            break;
                                        case 5:
                                            textView.setText("" + blindClose.get(i).getSysSales());
                                            break;
                                        case 6:
                                            textView.setText("" + blindClose.get(i).getUserSales());
                                            break;
                                        case 7:
                                            textView.setText("" + blindClose.get(i).getSalesDiff());
                                            break;
                                        case 8:
                                            textView.setText(updateName);
                                            break;
                                        case 9:
                                            textView.setText("" + (blindClose.get(i).getTillOk() == 0 ? "no" : "yes"));
                                            break;

                                    }

                                    textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                                    textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setTextSize(16);

                                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(70, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                    lp2.setMargins(1, 1, 1, 1);
                                    textView.setLayoutParams(lp2);

                                    row.addView(textView);
                                }
                                table.addView(row);
                            }
                        }
                    }
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    void fillRowDate(TableLayout catTable, EditText creditCard, EditText cheque, EditText giftCard,
                     EditText credit, EditText point) {

        catTable.removeAllViews();
        for (int s = 0; s < focusedRowData.size(); s++) {
            if (focusedRowData.get(s).getType().equals("Cash")) {
                TableRow row1 = new TableRow(BackOfficeActivity.this);

                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row1.setLayoutParams(lp1);

                for (int r = 0; r < 3; r++) {
                    TextView textV = new TextView(BackOfficeActivity.this);
                    EditText editT = new EditText(BackOfficeActivity.this);

                    switch (r) {
                        case 0:
                            textV.setText(focusedRowData.get(s).getCatName());
                            break;
                        case 1:
                            editT.setText("" + focusedRowData.get(s).getCatQty());
                            break;
                        case 2:
                            textV.setText("" + focusedRowData.get(s).getCatTotal());
                            break;
                    }

                    textV.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                    textV.setGravity(Gravity.CENTER);
                    textV.setTextSize(13);

                    int index = s;
                    editT.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                    editT.setGravity(Gravity.CENTER);
                    editT.setTextSize(13);
                    editT.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editT.addTextChangedListener(new TextWatcher() {

                        // the user's changes are saved here
                        public void onTextChanged(CharSequence c, int start, int before, int count) {
                            TextView tot = (TextView) row1.getChildAt(2);
                            if (!editT.getText().toString().equals("")) {
                                double total = Integer.parseInt(editT.getText().toString()) * focusedRowData.get(index).getCatValue();
                                tot.setText("" + total);
                            } else
                                tot.setText("0.0");
                        }

                        public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                            // this space intentionally left blank
                        }

                        public void afterTextChanged(Editable c) {
                            // this one too
                        }
                    });

                    TableRow.LayoutParams lp3 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp3.setMargins(1, 1, 1, 1);
                    textV.setLayoutParams(lp3);
                    editT.setLayoutParams(lp3);

                    if (r == 1)
                        row1.addView(editT);
                    else
                        row1.addView(textV);
                }
                catTable.addView(row1);

            } else if (focusedRowData.get(s).getType().equals("Credit Card")) {
                creditCard.setText("" + focusedRowData.get(s).getCatTotal());
            } else if (focusedRowData.get(s).getType().equals("Cheque")) {
                cheque.setText("" + focusedRowData.get(s).getCatTotal());
            } else if (focusedRowData.get(s).getType().equals("Gift Card")) {
                giftCard.setText("" + focusedRowData.get(s).getCatTotal());
            } else if (focusedRowData.get(s).getType().equals("Credit")) {
                credit.setText("" + focusedRowData.get(s).getCatTotal());
            } else if (focusedRowData.get(s).getType().equals("Point")) {
                point.setText("" + focusedRowData.get(s).getCatTotal());
            }
        }
    }

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

    void X_ReportDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.x_report_dialog);
        dialog.setCanceledOnTouchOutside(true);


        TextView totalBeforTax, tax, totalAfterTax, services, servicesTax, totalTax, net;

        totalBeforTax = (TextView) dialog.findViewById(R.id.total);
        tax = (TextView) dialog.findViewById(R.id.tax);
        totalAfterTax = (TextView) dialog.findViewById(R.id.totalAfterTax);
        services = (TextView) dialog.findViewById(R.id.services);
        servicesTax = (TextView) dialog.findViewById(R.id.servicesTax);
        totalTax = (TextView) dialog.findViewById(R.id.totalTax);
        net = (TextView) dialog.findViewById(R.id.net);


        TextView toDate = (TextView) dialog.findViewById(R.id.toDateX);
        TextView fromDate = (TextView) dialog.findViewById(R.id.fromDateX);

        Button preview, exit, export, print;
        Spinner ShiftName, PosNo;

        ShiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        preview = (Button) dialog.findViewById(R.id.doneReport);
        exit = (Button) dialog.findViewById(R.id.exitReport);
        export = (Button) dialog.findViewById(R.id.exportReport);
        print = (Button) dialog.findViewById(R.id.printReport);

        TableLayout tableXreportTax = (TableLayout) dialog.findViewById(R.id.TAXPer);

        TableLayout tableXreport = (TableLayout) dialog.findViewById(R.id.taxTable);
        ArrayList<OrderTransactions> orderTransactionsTax = new ArrayList<>();
        orderTransactionData = new ArrayList<>();
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        ShiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);

        fromDate.setText(today);
        toDate.setText(today);

        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        orderTransactionData = mDHandler.getAllOrderTransactions();

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableXreport.removeAllViews();
                tableXreportTax.removeAllViews();

                String ShiftNa = "SHIFT_NAME";
                String fromDat = fromDate.getText().toString();
                String toDat = toDate.getText().toString();
                double totalText = 0.0, tatText = 0.0, netText = 0.0;

                String posNoString = "POS_NO";

                if (ShiftName.getSelectedItem().toString().equals("All")) {
                    ShiftNa = "SHIFT_NAME";

                } else {
                    ShiftNa = "'" + ShiftName.getSelectedItem().toString() + "'";
                }

                if (PosNo.getSelectedItem().toString().equals("All")) {
                    posNoString = "POS_NO";

                } else {
                    posNoString = "'" + PosNo.getSelectedItem().toString() + "'";
                }

                orderTransactionData = mDHandler.getXReport(ShiftNa, posNoString, fromDat, toDat);

                for (int i = 0; i < orderTransactionData.size(); i++) {

                    insertCashierInOutReport(tableXreport, orderTransactionData.get(i).getItemName(), String.valueOf(orderTransactionData.get(i).getTaxValue()),
                            "", String.valueOf(orderTransactionData.get(i).getTotal())
                            , "", "", String.valueOf(orderTransactionData.get(i).getTotal() + orderTransactionData.get(i).getTaxValue()), 4);
                }

                for (int a = 0; a < tableXreport.getChildCount(); a++) {

                    TableRow rows = (TableRow) tableXreport.getChildAt(a);
                    TextView textTotal = (TextView) rows.getChildAt(1);
                    TextView textTax = (TextView) rows.getChildAt(2);
                    TextView textNet = (TextView) rows.getChildAt(3);

                    totalText += Double.parseDouble(textTotal.getText().toString());
                    tatText += Double.parseDouble(textTax.getText().toString());
                    netText += Double.parseDouble(textNet.getText().toString());

                }

                totalBeforTax.setText("" + totalText);
                tax.setText("" + tatText);
                totalAfterTax.setText("" + netText);
//                services.setText("" + totalText);
//                servicesTax.setText("" + totalText);
                totalTax.setText("" + totalText);
                net.setText("" + netText);

                orderTransactionData = mDHandler.getXReportPercent(ShiftNa, posNoString, fromDat, toDat);
                for (int i = 0; i < orderTransactionData.size(); i++) {

                    insertCashierInOutReport(tableXreportTax, String.valueOf(orderTransactionData.get(i).getTaxPerc()),
                            String.valueOf(orderTransactionData.get(i).getTaxValue()), "",
                            String.valueOf(orderTransactionData.get(i).getTotal()), "", "", "", 3);

                }


            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

    void Z_ReportDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.z_report_dialog);
        dialog.setCanceledOnTouchOutside(true);


        TextView totalBeforTax, tax, totalAfterTax, services, servicesTax, totalTax, net, serial, PosNo;

        totalBeforTax = (TextView) dialog.findViewById(R.id.total);
        tax = (TextView) dialog.findViewById(R.id.tax);
        totalAfterTax = (TextView) dialog.findViewById(R.id.totalAfterTax);
        services = (TextView) dialog.findViewById(R.id.services);
        servicesTax = (TextView) dialog.findViewById(R.id.servicesTax);
        totalTax = (TextView) dialog.findViewById(R.id.totalTax);
        net = (TextView) dialog.findViewById(R.id.net);
        serial = (TextView) dialog.findViewById(R.id.serial);
        TextView fromDate = (TextView) dialog.findViewById(R.id.fromDateX);

        Button preview, exit, export, print;

        PosNo = (TextView) dialog.findViewById(R.id.posNo);

        preview = (Button) dialog.findViewById(R.id.doneReport);
        exit = (Button) dialog.findViewById(R.id.exitReport);
        export = (Button) dialog.findViewById(R.id.exportReport);
        print = (Button) dialog.findViewById(R.id.printReport);

        TableLayout tableXreportTax = (TableLayout) dialog.findViewById(R.id.TAXPer);

        TableLayout tableXreport = (TableLayout) dialog.findViewById(R.id.taxTable);
        ArrayList<OrderTransactions> orderTransactionsTax = new ArrayList<>();
        orderTransactionData = new ArrayList<>();

//        ArrayList<String> posNoArray = new ArrayList<>();
//
//        posNoArray.add("All");
//        posNoArray.add("4");
//        posNoArray.add("7");
//        posNoArray.add("1");
//        posNoArray.add("0");
//
//
//
//
//        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
//        PosNo.setAdapter(adapterPosNo);

        PosNo.setText("" + Settings.POS_number);
        fromDate.setText(today);
        int serials = mDHandler.getMaxZReportSerial(String.valueOf(Settings.POS_number)) + 1;
        serial.setText("" + serials);

        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        orderTransactionData = mDHandler.getAllOrderTransactions();

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tableXreport.removeAllViews();
                tableXreportTax.removeAllViews();


                String fromDat = fromDate.getText().toString();
                double totalText = 0.0, tatText = 0.0, netText = 0.0;
                String posNoString = "POS_NO";

//                if (PosNo.getSelectedItem().toString().equals("All")) {
//                    posNoString = "POS_NO";
//
//                } else {
                posNoString = "'" + PosNo.getText().toString() + "'";
//                }

                int serials = mDHandler.getMaxZReportSerial(posNoString) + 1;

                serial.setText("" + serials);
                Log.e("testSerial", "" + mDHandler.getMaxZReportSerial(posNoString));

                orderTransactionData = mDHandler.getXReport("SHIFT_NAME", posNoString, fromDat, fromDat);

                for (int i = 0; i < orderTransactionData.size(); i++) {

                    insertCashierInOutReport(tableXreport, orderTransactionData.get(i).getItemName(), String.valueOf(orderTransactionData.get(i).getTaxValue()),
                            "", String.valueOf(orderTransactionData.get(i).getTotal())
                            , "", "", String.valueOf(orderTransactionData.get(i).getTotal() + orderTransactionData.get(i).getTaxValue()), 4);
                }

                for (int a = 0; a < tableXreport.getChildCount(); a++) {

                    TableRow rows = (TableRow) tableXreport.getChildAt(a);
                    TextView textTotal = (TextView) rows.getChildAt(1);
                    TextView textTax = (TextView) rows.getChildAt(2);
                    TextView textNet = (TextView) rows.getChildAt(3);

                    totalText += Double.parseDouble(textTotal.getText().toString());
                    tatText += Double.parseDouble(textTax.getText().toString());
                    netText += Double.parseDouble(textNet.getText().toString());

                }

                totalBeforTax.setText("" + totalText);
                tax.setText("" + tatText);
                totalAfterTax.setText("" + netText);
//                services.setText("" + totalText);
//                servicesTax.setText("" + totalText);
                totalTax.setText("" + totalText);
                net.setText("" + netText);

                orderTransactionData = mDHandler.getXReportPercent("SHIFT_NAME", posNoString, fromDat, fromDat);
                for (int i = 0; i < orderTransactionData.size(); i++) {

                    insertCashierInOutReport(tableXreportTax, String.valueOf(orderTransactionData.get(i).getTaxPerc()),
                            String.valueOf(orderTransactionData.get(i).getTaxValue()), "",
                            String.valueOf(orderTransactionData.get(i).getTotal()), "", "", "", 3);

                }


            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isZReportPrinting(fromDate.getText().toString())) {
                    ZReport zReport = new ZReport(fromDate.getText().toString(), Settings.POS_number
                            , Settings.password, Settings.user_name, (Integer.parseInt(serial.getText().toString())));

                    mDHandler.addZReportTable(zReport);

                    Toast.makeText(BackOfficeActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BackOfficeActivity.this, "this report printed before this time", Toast.LENGTH_SHORT).show();
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

    boolean isZReportPrinting(String date) {
        ArrayList<ZReport> ZReport = new ArrayList<>();
        boolean isFound = false;
        ZReport = mDHandler.getAllZReport();
        for (int i = 0; i < ZReport.size(); i++) {
            if (date.equals(ZReport.get(i).getDate())) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    void userOrderCountReportDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.user_order_count);
        dialog.setCanceledOnTouchOutside(true);


        TextView toDate = (TextView) dialog.findViewById(R.id.toDateX);
        TextView fromDate = (TextView) dialog.findViewById(R.id.fromDateX);

        Button preview, exit, export, print;
        Spinner userName, PosNo;

        userName = (Spinner) dialog.findViewById(R.id.userN);
        PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        preview = (Button) dialog.findViewById(R.id.doneReport);
        exit = (Button) dialog.findViewById(R.id.exitReport);
        export = (Button) dialog.findViewById(R.id.exportReport);
        print = (Button) dialog.findViewById(R.id.printReport);

        TableLayout userTable = (TableLayout) dialog.findViewById(R.id.userTable);


        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();


        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeName()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        userName.setAdapter(adapter);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);

        fromDate.setText(today);
        toDate.setText(today);

        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userTable.removeAllViews();

                String ShiftNa = "USER_NAME";
                String fromDat = fromDate.getText().toString();
                String toDat = toDate.getText().toString();
                double totalText = 0.0, tatText = 0.0, netText = 0.0;

                String posNoString = "POINT_OF_SALE_NUMBER";

                if (userName.getSelectedItem().toString().equals("All")) {
                    ShiftNa = "USER_NAME";

                } else {
                    ShiftNa = "'" + userName.getSelectedItem().toString() + "'";
                }

                if (PosNo.getSelectedItem().toString().equals("All")) {
                    posNoString = "POINT_OF_SALE_NUMBER";

                } else {
                    posNoString = "'" + PosNo.getSelectedItem().toString() + "'";
                }

                headerData = mDHandler.getUserNameReport(ShiftNa, posNoString, fromDat, toDat);
                for (int i = 0; i < headerData.size(); i++) {

                    insertCashierInOutReport(userTable, String.valueOf(headerData.get(i).getUserName()),
                            String.valueOf(headerData.get(i).getAmountDue()), "",
                            String.valueOf(headerData.get(i).getTime()), "", "", "", 3);

                }
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

    void announcementReportDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.announcement_report);
        dialog.setCanceledOnTouchOutside(true);


        TextView toDate = (TextView) dialog.findViewById(R.id.toDateX);
        TextView fromDate = (TextView) dialog.findViewById(R.id.fromDateX);

        Button preview, exit, export, print;
        Spinner userName, PosNo;

        userName = (Spinner) dialog.findViewById(R.id.userN);
        PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        preview = (Button) dialog.findViewById(R.id.doneReport);
        exit = (Button) dialog.findViewById(R.id.exitReport);
        export = (Button) dialog.findViewById(R.id.exportReport);
        print = (Button) dialog.findViewById(R.id.printReport);

        TableLayout userTable = (TableLayout) dialog.findViewById(R.id.userTable);
        Announcement = new ArrayList<>();

        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();


        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeName()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        userName.setAdapter(adapter);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);

        fromDate.setText(today);
        toDate.setText(today);

        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userTable.removeAllViews();
                String posNoAll = "All";

                String fromDat = fromDate.getText().toString();
                String toDat = toDate.getText().toString();


                int posNoString = -1;
                if (PosNo.getSelectedItem().toString().equals("All")) {
                    posNoString = -1;

                } else {
                    posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

                }

                Announcement = mDHandler.getAllTableAnnouncement();
                for (int i = 0; i < Announcement.size(); i++) {
                    if (filters(fromDat, toDat, Announcement.get(i).getAnnouncementDate()) &&
                            (Announcement.get(i).getUserName().equals(userName.getSelectedItem().toString()) || userName.getSelectedItem().toString().equals("All")) &&
                            (Announcement.get(i).getPosNo() == posNoString || posNoString == -1)) {
                        if (Announcement.get(i).getPosNo() == -1) {
                            posNoAll = "All";
                        } else {
                            posNoAll = String.valueOf(Announcement.get(i).getPosNo());
                        }
                        insertCashierInOutReport(userTable, Announcement.get(i).getShiftName(), posNoAll,
                                Announcement.get(i).getMessage(), Announcement.get(i).getUserName(), String.valueOf(Announcement.get(i).getIsShow()), "", Announcement.get(i).getAnnouncementDate(), 6);
                    }
                }
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

        nextSerial = mDHandler.getAllMoneyCategory().size()+1;
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
                    m.setCatValue(Double.parseDouble(catValue.getText().toString()));
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
//                if (!catName.getText().toString().equals("") && !catValue.getText().toString().equals("")) {
                if (money.size() != 0) {
                    mDHandler.addMoneyCategory(money);
                    dialog.dismiss();
                } else
                    Toast.makeText(BackOfficeActivity.this, "Please insure your inputs", Toast.LENGTH_SHORT).show();
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

    void showAddVoidReasonsDialog() {
        final Dialog dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_void_reason_dialog);

        LinearLayout addR = (LinearLayout) dialog.findViewById(R.id.add_reason);
        EditText reason = (EditText) dialog.findViewById(R.id.reason);
        CheckBox isActive = (CheckBox) dialog.findViewById(R.id.isActive);
        TableLayout reasons = (TableLayout) dialog.findViewById(R.id.tableOfReasons);
        Button save = (Button) dialog.findViewById(R.id.done);
        Button exit = (Button) dialog.findViewById(R.id.exit);

        ArrayList<VoidResons> resons = mDHandler.getAllVoidReasons();

        reasons.removeAllViews();
        for (int k = 0; k < resons.size(); k++) {

            final TableRow row = new TableRow(BackOfficeActivity.this);
            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp.setMargins(2, 2, 2, 4);
            row.setLayoutParams(lp);

            TextView textView = new TextView(BackOfficeActivity.this);
            textView.setText(resons.get(k).getVoidReason());
            textView.setTextSize(20);
            textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
            TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            textView.setLayoutParams(lp1);

            CheckBox checkBox = new CheckBox(BackOfficeActivity.this);
            checkBox.setChecked(resons.get(k).getActiveated() == 1);
            checkBox.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.15f);
            checkBox.setLayoutParams(lp2);

            Button button = new Button(BackOfficeActivity.this);
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.delete_raw));
            TableRow.LayoutParams lp3 = new TableRow.LayoutParams(0, 20, 0.05f);
            button.setLayoutParams(lp3);
            button.setOnClickListener(view1 -> {
                reasons.removeView(row);
            });

            row.addView(textView);
            row.addView(checkBox);
            row.addView(button);

            reasons.addView(row);
        }

        addR.setOnClickListener(view -> {

            if (!reason.getText().toString().equals("")) {

                final TableRow row1 = new TableRow(BackOfficeActivity.this);

                TableLayout.LayoutParams lp12 = new TableLayout.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                lp12.setMargins(2, 2, 2, 4);
                row1.setLayoutParams(lp12);

                TextView textView = new TextView(BackOfficeActivity.this);
                textView.setText(reason.getText().toString());
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                textView.setLayoutParams(lp1);

                CheckBox checkBox = new CheckBox(BackOfficeActivity.this);
                checkBox.setChecked(isActive.isChecked());
                checkBox.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.15f);
                checkBox.setLayoutParams(lp2);

                Button button = new Button(BackOfficeActivity.this);
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.delete_raw));
                TableRow.LayoutParams lp3 = new TableRow.LayoutParams(0, 20, 0.05f);
                button.setLayoutParams(lp3);
                button.setOnClickListener(view1 -> {
                    reasons.removeView(row1);
                });

                row1.addView(textView);
                row1.addView(checkBox);
                row1.addView(button);

                reasons.addView(row1);
                reason.setText("");
            } else
                Toast.makeText(BackOfficeActivity.this, "No text to be added ! ", Toast.LENGTH_LONG).show();
        });

        save.setOnClickListener(view -> {

            mDHandler.deleteAllVoidReasons();

            for (int k = 0; k < reasons.getChildCount(); k++) {
                TableRow tableRow = (TableRow) reasons.getChildAt(k);
                TextView textView = (TextView) tableRow.getChildAt(0);
                CheckBox checkBox = (CheckBox) tableRow.getChildAt(1);
                int active = checkBox.isChecked() ? 1 : 0;

                mDHandler.addVoidReason(new VoidResons(Settings.shift_number, Settings.shift_name, Settings.password,
                        Settings.user_name, textView.getText().toString(), today, active));
            }
            dialog.dismiss();
        });

        exit.setOnClickListener(view -> dialog.dismiss());

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

    void salesTotalReportDialog() {
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

        ImageView printingReport;

        done = (Button) dialog.findViewById(R.id.doneReport);
        exit = (Button) dialog.findViewById(R.id.exitReport);

        shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        posNo = (Spinner) dialog.findViewById(R.id.posNo);
        users = (Spinner) dialog.findViewById(R.id.user);

        TextView fromDate = (TextView) dialog.findViewById(R.id.fDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.tDate);
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

        printingReport = (ImageView) dialog.findViewById(R.id.printing);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today = df.format(currentTimeAndDate);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeName());
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        users.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        posNo.setAdapter(adapterPosNo);


        headerData = mDHandler.getAllOrderHeader();
        payData = mDHandler.getAllExistingPay();

        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double sales = 0.0, returns = 0.0, allDiscountSales = 0.0, allDiscountReturn = 0.0,
                        totalServiceSales = 0.0, totalServiceReturn = 0.0, cashValue = 0.0, pointValue = 0.0, visaValue = 0.0, masterValue = 0.0, giftValue = 0.0, creditValue = 0.0, chequeValue = 0.0, netSales = 0.0, netPayMethod = 0.0, netDiscount = 0.0, netService = 0.0;

                userString[0] = users.getSelectedItem().toString();
                shiftNameString[0] = shiftName.getSelectedItem().toString();

                if (posNo.getSelectedItem().toString().equals("All")) {
                    posNoString[0] = -1;
                } else {
                    posNoString[0] = Integer.parseInt(posNo.getSelectedItem().toString());
                }

                for (int i = 0; i < headerData.size(); i++) {
                    if (filters(fromDate.getText().toString(), toDate.getText().toString(), headerData.get(i).getVoucherDate())) {
                        if (headerData.get(i).getShiftName().equals(shiftNameString[0]) || shiftNameString[0].equals("All")) {
                            if (headerData.get(i).getUserName().equals(userString[0]) || userString[0].equals("All")) {
                                if (headerData.get(i).getPointOfSaleNumber() == posNoString[0] || posNoString[0] == -1) {
                                    if (headerData.get(i).getOrderKind() == 0) {
                                        sales += headerData.get(i).getAmountDue();
                                        allDiscountSales += headerData.get(i).getAllDiscount();
                                        totalServiceSales += headerData.get(i).getTotalService();
                                    } else if (headerData.get(i).getOrderKind() == 998) {
                                        returns += headerData.get(i).getAmountDue();
                                        allDiscountReturn += headerData.get(i).getAllDiscount();
                                        totalServiceReturn += headerData.get(i).getTotalService();
                                    }

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

                for (int i = 0; i < payData.size(); i++) {
                    if (filters(fromDate.getText().toString(), toDate.getText().toString(), payData.get(i).getVoucherDate())) {
                        if (payData.get(i).getShiftName().equals(shiftNameString[0]) || shiftNameString[0].equals("All")) {
                            if (payData.get(i).getUserName().equals(userString[0]) || userString[0].equals("All")) {
                                if (payData.get(i).getPointOfSaleNumber() == posNoString[0] || posNoString[0] == -1) {
                                    if (payData.get(i).getOrderKind() == 0) {
                                        if (payData.get(i).getPayName().equals("visa"))
                                            visaValue += payData.get(i).getPayValue();
                                        else if (payData.get(i).getPayName().equals("master"))
                                            masterValue += payData.get(i).getPayValue();

                                    } else if (payData.get(i).getOrderKind() == 998) {
                                        if (payData.get(i).getPayName().equals("visa"))
                                            visaValue -= payData.get(i).getPayValue();
                                        else if (payData.get(i).getPayName().equals("master"))
                                            masterValue -= payData.get(i).getPayValue();
                                    }


                                }
                            }
                        }
                    }
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

        printingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BackOfficeActivity.this, "printing", Toast.LENGTH_SHORT).show();
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

    void cashierInOutReportDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cashier_in_out_dialog);
        dialog.setCanceledOnTouchOutside(true);

        RadioButton All, In, Out;

        Button exit, preview, export, print;
        TableLayout cashierTable = (TableLayout) dialog.findViewById(R.id.cashierTable);


        Spinner shiftName, cashierNo, PosNo;

        exit = (Button) dialog.findViewById(R.id.exitReport);
        preview = (Button) dialog.findViewById(R.id.doneReport);
        export = (Button) dialog.findViewById(R.id.exportReport);
        print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        All = (RadioButton) dialog.findViewById(R.id.All);
        In = (RadioButton) dialog.findViewById(R.id.cashierIN);
        Out = (RadioButton) dialog.findViewById(R.id.cashierOut);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        payInData = new ArrayList<Pay>();
        payInData = mDHandler.getAllPayInOut();
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                cashierTable.removeAllViews();

                int cashierType = 0;
                if (All.isChecked()) {
                    cashierType = -1;
                } else if (In.isChecked()) {
                    cashierType = 0;
                } else if (Out.isChecked()) {
                    cashierType = 1;
                }
                int CashierNo = -1;

                if (cashierNo.getSelectedItem().toString().equals("All")) {
                    CashierNo = -1;
                } else {
                    CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());
                }


                String ShiftName = shiftName.getSelectedItem().toString();
                int posNoString = -1;

                if (PosNo.getSelectedItem().toString().equals("All")) {
                    posNoString = -1;
                } else {
                    posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());
                }

                for (int i = 0; i < payInData.size(); i++) {
                    if (filters(fromDate.getText().toString(), toDate.getText().toString(), payInData.get(i).getTransDate())) {
                        if (payInData.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                            if (payInData.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                                if (payInData.get(i).getPosNo() == posNoString || posNoString == -1) {
                                    if (cashierType == payInData.get(i).getTransType() || cashierType == -1) {
                                        count++;
                                        insertCashierInOutReport(cashierTable, String.valueOf(count), payInData.get(i).getTransDate()
                                                , String.valueOf(payInData.get(i).getPosNo()), payInData.get(i).getUserName(), String.valueOf(payInData.get(i).getTransType())
                                                , String.valueOf(payInData.get(i).getValue()), "2-2-2000", 7);
                                    }

                                }
                            }
                        }
                    }//else 1
                }


            }
        });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    void showCanceledOrdersHistory() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.canceled_orders_history_dialog);
        dialog.setCanceledOnTouchOutside(true);

        TableLayout canceledTable = (TableLayout) dialog.findViewById(R.id.canceledTable);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        RadioGroup voidType = dialog.findViewById(R.id.voidType);
        RadioButton All = (RadioButton) dialog.findViewById(R.id.All);
        RadioButton In = (RadioButton) dialog.findViewById(R.id.canceled);
        RadioButton Out = (RadioButton) dialog.findViewById(R.id.void_);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        List<CancleOrder> canceledOrders = mDHandler.getAllCanselOrder();
        preview.setOnClickListener(v -> {

            canceledTable.removeAllViews();

            int voidingType = -1;
            switch (voidType.getCheckedRadioButtonId()) {
                case R.id.All:
                    voidingType = -1;
                    break;

                case R.id.canceled:
                    voidingType = 0;
                    break;

                case R.id.void_:
                    voidingType = 1;
                    break;
            }

            int CashierNo = -1;
            if (cashierNo.getSelectedItem().toString().equals("All"))
                CashierNo = -1;
            else
                CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

            String ShiftName = shiftName.getSelectedItem().toString();

            int posNoString = -1;
            if (PosNo.getSelectedItem().toString().equals("All"))
                posNoString = -1;
            else
                posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

            int serial = 0;
            for (int i = 0; i < canceledOrders.size(); i++) {
                if (filters(fromDate.getText().toString(), toDate.getText().toString(), canceledOrders.get(i).getTransDate())) {

                    if (canceledOrders.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                        if (canceledOrders.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                            if (canceledOrders.get(i).getPosNO() == posNoString || posNoString == -1) {
                                if (voidingType == canceledOrders.get(i).getIsAllCancel() || voidingType == -1) {

                                    TableRow row = new TableRow(BackOfficeActivity.this);

                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                    row.setLayoutParams(lp);

                                    for (int k = 0; k < 8; k++) {
                                        TextView textView = new TextView(BackOfficeActivity.this);

                                        switch (k) {
                                            case 0:
                                                textView.setText("" + serial);
                                                break;
                                            case 1:
                                                textView.setText(canceledOrders.get(i).getTransDate());
                                                break;
                                            case 2:
                                                textView.setText(canceledOrders.get(i).getTime());
                                                break;
                                            case 3:
                                                textView.setText(canceledOrders.get(i).getItemCode());
                                                break;
                                            case 4:
                                                textView.setText(canceledOrders.get(i).getItemName());
                                                break;
                                            case 5:
                                                textView.setText("" + canceledOrders.get(i).getQty());
                                                break;
                                            case 6:
                                                textView.setText("" + canceledOrders.get(i).getTotal());
                                                break;
                                            case 7:
                                                textView.setText(canceledOrders.get(i).getReason());
                                                break;
                                        }

                                        textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                                        textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setTextSize(16);

                                        if (k == 0) {
                                            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(70, TableRow.LayoutParams.MATCH_PARENT, 0.5f);
                                            lp2.setMargins(1, 1, 1, 1);
                                            textView.setLayoutParams(lp2);
                                        } else if (k == 7) {
                                            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(150, TableRow.LayoutParams.WRAP_CONTENT, 4.0f);
                                            lp2.setMargins(1, 1, 1, 1);
                                            textView.setLayoutParams(lp2);
                                        } else {
                                            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 2.0f);
                                            lp2.setMargins(1, 1, 1, 1);
                                            textView.setLayoutParams(lp2);
                                        }
                                        row.addView(textView);
                                    }
                                    canceledTable.addView(row);
                                    serial++;
                                }
                            }
                        }
                    }
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    void showTablesActionReport() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.table_action_report);
        dialog.setCanceledOnTouchOutside(true);

        TableLayout canceledTable = (TableLayout) dialog.findViewById(R.id.table);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        RadioGroup actionType = dialog.findViewById(R.id.actionType);
        RadioButton All = (RadioButton) dialog.findViewById(R.id.All);
        RadioButton move = (RadioButton) dialog.findViewById(R.id.move);
        RadioButton merge = (RadioButton) dialog.findViewById(R.id.merge);
        RadioButton split = (RadioButton) dialog.findViewById(R.id.split);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        List<TableActions> actions = mDHandler.getAllTableActions();
        preview.setOnClickListener(v -> {

            canceledTable.removeAllViews();

            int actionTyp = -1;
            switch (actionType.getCheckedRadioButtonId()) {
                case R.id.All:
                    actionTyp = -1;
                    break;
                case R.id.move:
                    actionTyp = 0;
                    break;
                case R.id.merge:
                    actionTyp = 1;
                    break;
                case R.id.split:
                    actionTyp = 2;
                    break;
            }

            int CashierNo = -1;
            if (cashierNo.getSelectedItem().toString().equals("All"))
                CashierNo = -1;
            else
                CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

            String ShiftName = shiftName.getSelectedItem().toString();

            int posNoString = -1;
            if (PosNo.getSelectedItem().toString().equals("All"))
                posNoString = -1;
            else
                posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

            for (int i = 0; i < actions.size(); i++) {
                if (filters(fromDate.getText().toString(), toDate.getText().toString(), actions.get(i).getActionDate())) {

                    if (actions.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                        if (actions.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                            if (actions.get(i).getPOSNumber() == posNoString || posNoString == -1) {
                                if (actionTyp == actions.get(i).getActionType() || actionTyp == -1) {

                                    String type = "";
                                    switch (actions.get(i).getActionType()) {
                                        case 0:
                                            type = "Move";
                                            break;
                                        case 1:
                                            type = "Merge";
                                            break;
                                        case 2:
                                            type = "Split";
                                            break;
                                    }

                                    TableRow row = new TableRow(BackOfficeActivity.this);

                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                    row.setLayoutParams(lp);

                                    for (int k = 0; k < 8; k++) {
                                        TextView textView = new TextView(BackOfficeActivity.this);

                                        switch (k) {
                                            case 0:
                                                textView.setText(actions.get(i).getActionDate());
                                                break;
                                            case 1:
                                                textView.setText(actions.get(i).getActionTime());
                                                break;
                                            case 2:
                                                textView.setText("" + actions.get(i).getTableNo());
                                                break;
                                            case 3:
                                                textView.setText("" + actions.get(i).getSectionNo());
                                                break;
                                            case 4:
                                                textView.setText(type);
                                                break;
                                            case 5:
                                                textView.setText("" + actions.get(i).getToTable());
                                                break;
                                            case 6:
                                                textView.setText("" + actions.get(i).getToSection());
                                                break;
                                            case 7:
                                                textView.setText("" + actions.get(i).getUserName());
                                                break;
                                        }

                                        textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                                        textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setTextSize(16);

                                        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                        lp2.setMargins(1, 1, 1, 1);
                                        textView.setLayoutParams(lp2);

                                        row.addView(textView);
                                    }
                                    canceledTable.addView(row);
                                }
                            }
                        }
                    }
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    void showSimpleSalesTotal() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.simple_sales_total_dialog);
        dialog.setCanceledOnTouchOutside(true);

        TableLayout table = (TableLayout) dialog.findViewById(R.id.canceledTable);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        RadioGroup orderType = dialog.findViewById(R.id.orderType);
        RadioButton All = (RadioButton) dialog.findViewById(R.id.All);
        RadioButton dineIn = (RadioButton) dialog.findViewById(R.id.dineIn);
        RadioButton takeAway = (RadioButton) dialog.findViewById(R.id.takeAway);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        List<OrderHeader> orderHeaders = mDHandler.getAllOrderHeader();
        preview.setOnClickListener(v -> {

            table.removeAllViews();

            int orderTyp = -1;
            switch (orderType.getCheckedRadioButtonId()) {
                case R.id.All:
                    orderTyp = -1;
                    break;

                case R.id.takeAway:
                    orderTyp = 0;
                    break;

                case R.id.dineIn:
                    orderTyp = 1;
                    break;


            }

            int CashierNo = -1;
            if (cashierNo.getSelectedItem().toString().equals("All"))
                CashierNo = -1;
            else
                CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

            String ShiftName = shiftName.getSelectedItem().toString();

            int posNoString = -1;
            if (PosNo.getSelectedItem().toString().equals("All"))
                posNoString = -1;
            else
                posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

            int serial = 0;
            double totalTotal = 0;
            double totalDiscount = 0;
            double totalTax = 0;
            double totalNet = 0;
            for (int i = 0; i < orderHeaders.size(); i++) {
                if (filters(fromDate.getText().toString(), toDate.getText().toString(), orderHeaders.get(i).getVoucherDate())) {

                    if (orderHeaders.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                        if (orderHeaders.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                            if (orderHeaders.get(i).getPointOfSaleNumber() == posNoString || posNoString == -1) {
                                if (orderTyp == orderHeaders.get(i).getOrderType() || orderTyp == -1) {

                                    totalTotal += orderHeaders.get(i).getTotal();
                                    totalTax += orderHeaders.get(i).getTotalTax();
                                    totalDiscount += orderHeaders.get(i).getAllDiscount();
                                    totalNet += orderHeaders.get(i).getAmountDue();

                                    String payMethods = "";
                                    if (orderHeaders.get(i).getCashValue() > 0)
                                        payMethods = payMethods + "- cash";
                                    if (orderHeaders.get(i).getCardsValue() > 0)
                                        payMethods = payMethods + "- card";
                                    if (orderHeaders.get(i).getChequeValue() > 0)
                                        payMethods = payMethods + "- cheque";
                                    if (orderHeaders.get(i).getCouponValue() > 0)
                                        payMethods = payMethods + "- coupon";
                                    if (orderHeaders.get(i).getGiftValue() > 0)
                                        payMethods = payMethods + "- gift";
                                    if (orderHeaders.get(i).getPointValue() > 0)
                                        payMethods = payMethods + "- point";

                                    TableRow row = new TableRow(BackOfficeActivity.this);

                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                    row.setLayoutParams(lp);

                                    for (int k = 0; k < 8; k++) {
                                        TextView textView = new TextView(BackOfficeActivity.this);

                                        switch (k) {
                                            case 0:
                                                textView.setText(orderHeaders.get(i).getVoucherDate());
                                                break;
                                            case 1:
                                                textView.setText("" + posNoString);
                                                break;
                                            case 2:
                                                textView.setText("" + orderHeaders.get(i).getVoucherNumber());
                                                break;
                                            case 3:
                                                textView.setText("" + orderHeaders.get(i).getTotal());
                                                break;
                                            case 4:
                                                textView.setText("" + orderHeaders.get(i).getAllDiscount());
                                                break;
                                            case 5:
                                                textView.setText("" + orderHeaders.get(i).getTotalTax());
                                                break;
                                            case 6:
                                                textView.setText("" + orderHeaders.get(i).getAmountDue());
                                                break;
                                            case 7:
                                                textView.setText(payMethods);
                                                break;
                                        }

                                        textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                                        textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setTextSize(16);

                                        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 2.0f);
                                        lp2.setMargins(1, 1, 1, 1);
                                        textView.setLayoutParams(lp2);

                                        row.addView(textView);
                                    }
                                    table.addView(row);
                                    serial++;
                                }
                            }
                        }
                    }
                }
            }

            TableRow row = new TableRow(BackOfficeActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            for (int k = 0; k < 8; k++) {
                TextView textView = new TextView(BackOfficeActivity.this);
                switch (k) {
                    case 0:
                        textView.setText(" ");
                        break;
                    case 1:
                        textView.setText(" ");
                        break;
                    case 2:
                        textView.setText(getResources().getString(R.string.totals));
                        break;
                    case 3:
                        textView.setText("" + totalTotal);
                        break;
                    case 4:
                        textView.setText("" + totalDiscount);
                        break;
                    case 5:
                        textView.setText("" + totalTax);
                        break;
                    case 6:
                        textView.setText("" + totalNet);
                        break;
                    case 7:
                        textView.setText(" ");
                        break;
                }

                textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.light_blue_over));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 2.0f);
                lp2.setMargins(1, 1, 1, 1);
                textView.setLayoutParams(lp2);

                row.addView(textView);
            }
            table.addView(row);

        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    void showSalesPerHour() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sales_per_hours_dialog);
        dialog.setCanceledOnTouchOutside(true);

        TableLayout table = (TableLayout) dialog.findViewById(R.id.canceledTable);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        RadioGroup type = dialog.findViewById(R.id.type);
        RadioButton qty = (RadioButton) dialog.findViewById(R.id.qty);
        RadioButton money = (RadioButton) dialog.findViewById(R.id.money);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        List<OrderHeader> orderHeaders = mDHandler.getAllOrderHeader();
        preview.setOnClickListener(v -> {

            table.removeAllViews();

            int typ = 0;
            switch (type.getCheckedRadioButtonId()) {
                case R.id.qty:
                    typ = 0;
                    break;

                case R.id.money:
                    typ = 1;
                    break;
            }

            int CashierNo = -1;
            if (cashierNo.getSelectedItem().toString().equals("All"))
                CashierNo = -1;
            else
                CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

            String ShiftName = shiftName.getSelectedItem().toString();

            int posNoString = -1;
            if (PosNo.getSelectedItem().toString().equals("All"))
                posNoString = -1;
            else
                posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

            for (int i = 0; i < 24; i++) {

                List<OrderHeader> filteredList = getFilteredArrayByHour(orderHeaders, i, fromDate, toDate, ShiftName, CashierNo, posNoString);

                int totalGusts = 0;
                int numOfTrans = 0;
                double totalSales = 0;

                for (int s = 0; s < filteredList.size(); s++) {
                    if (filteredList.get(s).getOrderType() == 0)
                        totalGusts += 1;
                    else
                        totalGusts += filteredList.get(s).getSeatsNumber();

                    numOfTrans += 1;
                    totalSales += filteredList.get(s).getAmountDue();
                }

                TableRow row = new TableRow(BackOfficeActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                for (int k = 0; k < 6; k++) {
                    TextView textView = new TextView(BackOfficeActivity.this);

                    switch (k) {
                        case 0:
                            textView.setText("" + i + ":00 - " + (i + 1) + ":00");
                            break;
                        case 1:
                            textView.setText("" + totalGusts);
                            break;
                        case 2:
                            textView.setText("" + numOfTrans);
                            break;
                        case 3:
                            textView.setText("" + totalSales);
                            break;
                        case 4:
                            textView.setText("" + (totalSales / numOfTrans));
                            break;
                        case 5:
                            textView.setText("" + (totalSales / totalGusts));
                            break;
                    }

                    textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                    textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(16);

                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 2.0f);
                    lp2.setMargins(1, 1, 1, 1);
                    textView.setLayoutParams(lp2);

                    if (textView.getText().toString().equals("NaN"))
                        textView.setText("-");

                    row.addView(textView);
                }
                table.addView(row);
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    public List<OrderHeader> getFilteredArrayByHour(List<OrderHeader> orderHeaders, int hour, TextView fromDate, TextView toDate, String ShiftName,
                                                    int CashierNo, int posNoString) {
        List<OrderHeader> filteredOrderHeaders = new ArrayList<>();
        for (int i = 0; i < orderHeaders.size(); i++) {
            int orderHour = Integer.parseInt(orderHeaders.get(i).getTime().substring(0, 2));

            if (filters(fromDate.getText().toString(), toDate.getText().toString(), orderHeaders.get(i).getVoucherDate())) {
                if (orderHeaders.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                    if (orderHeaders.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                        if (orderHeaders.get(i).getPointOfSaleNumber() == posNoString || posNoString == -1) {
                            if (orderHour == hour) {
                                filteredOrderHeaders.add(orderHeaders.get(i));
                            }
                        }
                    }
                }
            }
        }
        return filteredOrderHeaders;
    }

    void ShowSalesReportByCardTypes() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sale_report_by_card_dialog);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();

        Button exit, preview, export, print;
        TableLayout cardTypeTable = (TableLayout) dialog.findViewById(R.id.cardTypeTable);

        Spinner shiftName, cardType, PosNo;

        exit = (Button) dialog.findViewById(R.id.exitReport);
        preview = (Button) dialog.findViewById(R.id.doneReport);
        export = (Button) dialog.findViewById(R.id.exportReport);
        print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate2 = (TextView) dialog.findViewById(R.id.frDateCard);
        TextView toDate2 = (TextView) dialog.findViewById(R.id.toDateCard);
        TextView totalTextCard = (TextView) dialog.findViewById(R.id.total);


        shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        cardType = (Spinner) dialog.findViewById(R.id.cardType);
        PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        fromDate2.setText(today);
        toDate2.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllCreditCards().size(); i++) {
            userArray.add(String.valueOf(mDHandler.getAllCreditCards().get(i).getCardName()));
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cardType.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate2.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate2), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate2.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate2), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        OrderPayMData = new ArrayList<>();

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=0;
                double totalText = 0;
                cardTypeTable.removeAllViews();

                int posNoString = -1;

                if (PosNo.getSelectedItem().toString().equals("All")) {
                    posNoString = -1;
                } else {
                    posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());
                }
                String cardTypes = cardType.getSelectedItem().toString();
                String ShiftNames = shiftName.getSelectedItem().toString();

                OrderPayMData = mDHandler.getAllExistingPay();
                for (int i = 0; i < OrderPayMData.size(); i++) {
                    if (filters(fromDate2.getText().toString(), toDate2.getText().toString(), OrderPayMData.get(i).getVoucherDate()) &&
                            (OrderPayMData.get(i).getPayName().equals(cardTypes) || cardTypes.equals("All")) &&
                            (OrderPayMData.get(i).getShiftName().equals(ShiftNames) || ShiftNames.equals("All")) &&
                            (OrderPayMData.get(i).getPayType().equals("Credit Card")) &&
                            (posNoString == -1 || posNoString == OrderPayMData.get(i).getPointOfSaleNumber())) {
                        count++;
                        insertCashierInOutReport(cardTypeTable, String.valueOf(count), "11:32",
                                OrderPayMData.get(i).getVoucherNumber(), OrderPayMData.get(i).getVoucherDate(),
                                String.valueOf(OrderPayMData.get(i).getPayValue()), OrderPayMData.get(i).getUserName(),
                                String.valueOf(OrderPayMData.get(i).getPointOfSaleNumber()), 7);

                    }
                }

                for (int a = 0; a < cardTypeTable.getChildCount(); a++) {

                    TableRow rows = (TableRow) cardTypeTable.getChildAt(a);
                    TextView textTotal = (TextView) rows.getChildAt(5);

                    totalText += Double.parseDouble(textTotal.getText().toString());

                }
                totalTextCard.setText(": " + totalText);

            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    void ShowMarketReport() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.market_report);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();

        Button exit, preview, export, print;
        TableLayout marketTable = (TableLayout) dialog.findViewById(R.id.marketTable);


        exit = (Button) dialog.findViewById(R.id.exitReport);
        preview = (Button) dialog.findViewById(R.id.doneReport);
        export = (Button) dialog.findViewById(R.id.exportReport);
        print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate2 = (TextView) dialog.findViewById(R.id.frDateMarket);
        TextView toDate2 = (TextView) dialog.findViewById(R.id.toDateMarket);


        fromDate2.setText(today);
        toDate2.setText(today);


        fromDate2.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate2), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate2.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate2), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        headerDataMarket = new ArrayList<>();

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalText = 0;
                marketTable.removeAllViews();

                int posNoString = -1;

                headerDataMarket = mDHandler.getMarketReport(fromDate2.getText().toString(), toDate2.getText().toString());
                for (int i = 0; i < headerDataMarket.size(); i++) {
                    if (filters(fromDate2.getText().toString(), toDate2.getText().toString(), headerDataMarket.get(i).getVoucherDate())) {
                        insertCashierInOutReport(marketTable, String.valueOf(headerDataMarket.get(i).getPointOfSaleNumber()),
                                String.valueOf(headerDataMarket.get(i).getTotalTax()), headerDataMarket.get(i).getTime(),
                                String.valueOf(headerDataMarket.get(i).getTotal()), String.valueOf(headerDataMarket.get(i).getAmountDue() / Integer.parseInt(headerDataMarket.get(i).getTime())), "",
                                String.valueOf(headerDataMarket.get(i).getAmountDue()), 6);
                    }
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    void waiterSalesReportDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.waiter_report);
        dialog.setCanceledOnTouchOutside(true);


        Button exit, preview, export, print;
        TableLayout waiterTable = (TableLayout) dialog.findViewById(R.id.waiterTable);

        Spinner shiftName, waiterName, PosNo;

        exit = (Button) dialog.findViewById(R.id.exitReport);
        preview = (Button) dialog.findViewById(R.id.doneReport);
        export = (Button) dialog.findViewById(R.id.exportReport);
        print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        waiterName = (Spinner) dialog.findViewById(R.id.casherNo);
        PosNo = (Spinner) dialog.findViewById(R.id.posNo);


        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 1) {
                userArray.add(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeName());
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");
        posNoArray.add("1");
        posNoArray.add("0");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        waiterName.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        headerData = mDHandler.getAllOrderHeader();
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                waiterTable.removeAllViews();

                String CashierNo = "All";

                CashierNo = waiterName.getSelectedItem().toString();

                String ShiftName = shiftName.getSelectedItem().toString();
                int posNoString = -1;

                if (PosNo.getSelectedItem().toString().equals("All")) {
                    posNoString = -1;
                } else {
                    posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());
                }

                for (int i = 0; i < headerData.size(); i++) {
                    if (filters(fromDate.getText().toString(), toDate.getText().toString(), headerData.get(i).getVoucherDate())) {
                        if (headerData.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                            if (headerData.get(i).getWaiter().equals(CashierNo) || CashierNo.equals("All")) {
                                if (headerData.get(i).getPointOfSaleNumber() == posNoString || posNoString == -1) {

                                    insertCashierInOutReport(waiterTable, headerData.get(i).getWaiter(), String.valueOf(headerData.get(i).getTotalDiscount()),
                                            String.valueOf(headerData.get(i).getAmountDue()),
                                            String.valueOf(headerData.get(i).getTotal()), String.valueOf(headerData.get(i).getTotalService()),
                                            String.valueOf(headerData.get(i).getTotalServiceTax()), String.valueOf(headerData.get(i).getTotalTax()), 7);
                                }
                            }
                        }
                    }
                }//else 1
            }


        });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    void showSalesVolumeByItemType() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sales_volume_by_item_type_dialog);
        dialog.setCanceledOnTouchOutside(true);

        TableLayout table = (TableLayout) dialog.findViewById(R.id.table);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        List<String> categories = mDHandler.getAllOrderedCategories();
        preview.setOnClickListener(v -> {
            table.removeAllViews();
            for (int i = 0; i < categories.size(); i++) {
                List<OrderTransactions> transactions = mDHandler.getOrdersTransactionsByCategory(categories.get(i));
                insertSalesVolumeByItem(table, transactions, cashierNo, shiftName, PosNo, fromDate, toDate);
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    void showSoldQtyReport() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sold_qty_report);
        dialog.setCanceledOnTouchOutside(true);

        TableLayout table = (TableLayout) dialog.findViewById(R.id.table);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);
        Spinner category = (Spinner) dialog.findViewById(R.id.category);
        Spinner family = (Spinner) dialog.findViewById(R.id.family);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();
        ArrayList<String> categoryArray = new ArrayList<>();
        ArrayList<String> familyArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllFamilyCategory().size(); i++) {
            if (mDHandler.getAllFamilyCategory().get(i).getType() == 2) { // 1 for family , 2 for category
                categoryArray.add(String.valueOf(mDHandler.getAllFamilyCategory().get(i).getName()));
            } else
                familyArray.add(String.valueOf(mDHandler.getAllFamilyCategory().get(i).getName()));
        }
        categoryArray.add(0, "All");
        familyArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, categoryArray);
        category.setAdapter(adapterCategory);

        ArrayAdapter<String> adapterFamily = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, familyArray);
        family.setAdapter(adapterFamily);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        List<OrderTransactions> transactions = mDHandler.getAllOrderTransactions();
        preview.setOnClickListener(v -> {
            table.removeAllViews();
            int CashierNo = -1;
            if (cashierNo.getSelectedItem().toString().equals("All"))
                CashierNo = -1;
            else
                CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

            String ShiftName = shiftName.getSelectedItem().toString();
            String categoryName = category.getSelectedItem().toString();
            String familyName = family.getSelectedItem().toString();

            int posNoString = -1;
            if (PosNo.getSelectedItem().toString().equals("All"))
                posNoString = -1;
            else
                posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

            for (int i = 0; i < transactions.size(); i++) {
                if (filters(fromDate.getText().toString(), toDate.getText().toString(), transactions.get(i).getVoucherDate())) {

                    if (transactions.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                        if (transactions.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                            if (transactions.get(i).getPosNo() == posNoString || posNoString == -1) {
                                if (transactions.get(i).getItemCategory().equals(categoryName) || categoryName.equals("All")) {
                                    if (transactions.get(i).getItemFamily().equals(familyName) || familyName.equals("All")) {

                                        TableRow row = new TableRow(BackOfficeActivity.this);

                                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                        row.setLayoutParams(lp);

                                        for (int k = 0; k < 10; k++) {
                                            TextView textView = new TextView(BackOfficeActivity.this);

                                            switch (k) {
                                                case 0:
                                                    textView.setText(transactions.get(i).getItemFamily());
                                                    break;
                                                case 1:
                                                    textView.setText(transactions.get(i).getItemCategory());
                                                    break;
                                                case 2:
                                                    textView.setText(transactions.get(i).getItemBarcode());
                                                    break;
                                                case 3:
                                                    textView.setText(transactions.get(i).getItemName());
                                                    break;
                                                case 4:
                                                    textView.setText("" + (transactions.get(i).getQty()));
                                                    break;
                                                case 5:
                                                    textView.setText("" + (transactions.get(i).getPrice()));
                                                    break;
                                                case 6:
                                                    textView.setText("" + (transactions.get(i).getQty() * (transactions.get(i).getPrice())));
                                                    break;
                                                case 7:
                                                    textView.setText("" + (transactions.get(i).getDiscount()));
                                                    break;
                                                case 8:
                                                    textView.setText("" + (transactions.get(i).getTaxValue()));
                                                    break;
                                                case 9:
                                                    textView.setText("" + (transactions.get(i).getTotal()));
                                                    break;
                                            }

                                            textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                                            textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                                            textView.setGravity(Gravity.CENTER);
                                            textView.setTextSize(16);

                                            if (k == 4 || k == 5 || k == 6 || k == 8) {
                                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(70, TableRow.LayoutParams.MATCH_PARENT, 0.7f);
                                                lp2.setMargins(1, 1, 1, 1);
                                                textView.setLayoutParams(lp2);
                                            } else {
                                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                                lp2.setMargins(1, 1, 1, 1);
                                                textView.setLayoutParams(lp2);
                                            }

                                            row.addView(textView);
                                        }
                                        table.addView(row);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    void showTopSalesItemReport() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.top_sales_item_report_dialog);
        dialog.setCanceledOnTouchOutside(true);

        TableLayout table = (TableLayout) dialog.findViewById(R.id.table);

        Button exit = (Button) dialog.findViewById(R.id.exitReport);
        Button preview = (Button) dialog.findViewById(R.id.doneReport);
        Button export = (Button) dialog.findViewById(R.id.exportReport);
        Button print = (Button) dialog.findViewById(R.id.printReport);

        TextView fromDate = (TextView) dialog.findViewById(R.id.frDate);
        TextView toDate = (TextView) dialog.findViewById(R.id.toDate);

        RadioGroup orderBy = (RadioGroup) dialog.findViewById(R.id.orderBy);
        RadioButton qty = (RadioButton) dialog.findViewById(R.id.qty);
        RadioButton total = (RadioButton) dialog.findViewById(R.id.total);

        Spinner shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        Spinner cashierNo = (Spinner) dialog.findViewById(R.id.casherNo);
        Spinner PosNo = (Spinner) dialog.findViewById(R.id.posNo);

        fromDate.setText(today);
        toDate.setText(today);
        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeNO()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        cashierNo.setAdapter(adapterUser);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        PosNo.setAdapter(adapterPosNo);


        fromDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(fromDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(toDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        preview.setOnClickListener(v -> {

            List<OrderTransactions> transactions = new ArrayList<>();
            table.removeAllViews();

            switch (orderBy.getCheckedRadioButtonId()) {
                case R.id.qty:
                    transactions = mDHandler.getTopSalesItemsByQty();
                    break;
                case R.id.total:
                    transactions = mDHandler.getTopSalesItemsByTotal();
                    break;
            }

            Log.e("transactions", "-->" + transactions.size());

            int CashierNo = -1;
            if (cashierNo.getSelectedItem().toString().equals("All"))
                CashierNo = -1;
            else
                CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

            String ShiftName = shiftName.getSelectedItem().toString();

            int posNoString = -1;
            if (PosNo.getSelectedItem().toString().equals("All"))
                posNoString = -1;
            else
                posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

            for (int i = 0; i < transactions.size(); i++) {
                if (filters(fromDate.getText().toString(), toDate.getText().toString(), transactions.get(i).getVoucherDate())) {

                    if (transactions.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                        if (transactions.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                            if (transactions.get(i).getPosNo() == posNoString || posNoString == -1) {

                                TableRow row = new TableRow(BackOfficeActivity.this);

                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                row.setLayoutParams(lp);

                                for (int k = 0; k < 4; k++) {
                                    TextView textView = new TextView(BackOfficeActivity.this);

                                    switch (k) {
                                        case 0:
                                            textView.setText(transactions.get(i).getItemBarcode());
                                            break;
                                        case 1:
                                            textView.setText(transactions.get(i).getItemName());
                                            break;
                                        case 2:
                                            textView.setText("" + transactions.get(i).getQty());
                                            break;
                                        case 3:
                                            textView.setText("" + (transactions.get(i).getTotal()));
                                            break;
                                    }

                                    textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                                    textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setTextSize(16);

                                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(70, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                    lp2.setMargins(1, 1, 1, 1);
                                    textView.setLayoutParams(lp2);

                                    row.addView(textView);
                                }
                                table.addView(row);
                            }
                        }
                    }
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    public DatePickerDialog.OnDateSetListener dateListener(TextView textView) {
        final DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {

            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            textView.setText(sdf.format(myCalendar.getTime()));
        };
        return date;
    }

    public boolean filters(String fromDate, String toDate, String date) {
        try {
            if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                    (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate))))
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
//                                String AM_PM;
//                                if (hourOfDay < 12) {
//                                    AM_PM = "AM";
//
//                                } else {
//                                    AM_PM = "PM";
//                                    hourOfDay -= 12;
//                                }
                                fromTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePickerDialog = new TimePickerDialog(BackOfficeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                String AM_PM;
//                                if (hourOfDay < 12) {
//                                    AM_PM = "AM";
//
//                                } else {
//                                    AM_PM = "PM";
//                                    hourOfDay -= 12;
//                                }
                                toTime.setText(hourOfDay + ":" + minute);
                            }
                        }, Calendar.HOUR_OF_DAY, Calendar.HOUR_OF_DAY, true);
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
                if (tableLayout.getChildCount() != 0) {
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


    void addAnnouncementDialog() {
        dialog = new Dialog(BackOfficeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_annoouncemet_dialog);
        dialog.setCanceledOnTouchOutside(true);

        TextView date = (TextView) dialog.findViewById(R.id.Date);
        EditText message = (EditText) dialog.findViewById(R.id.message);
        Spinner posNo, shiftName, userName;
        Button save, exit;

        posNo = (Spinner) dialog.findViewById(R.id.posNo);
        shiftName = (Spinner) dialog.findViewById(R.id.shiftName);
        userName = (Spinner) dialog.findViewById(R.id.userName);


        save = (Button) dialog.findViewById(R.id.save);
        exit = (Button) dialog.findViewById(R.id.exit);

        date.setText(today);
        date.setOnClickListener(v -> new DatePickerDialog(BackOfficeActivity.this, dateListener(date), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        ArrayList<String> shiftNameArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        ArrayList<String> posNoArray = new ArrayList<>();

        for (int i = 0; i < mDHandler.getAllShifts().size(); i++) {
            shiftNameArray.add(mDHandler.getAllShifts().get(i).getShiftName());
        }
        shiftNameArray.add(0, "All");

        for (int i = 0; i < mDHandler.getAllEmployeeRegistration().size(); i++) {
            if (mDHandler.getAllEmployeeRegistration().get(i).getEmployeeType() == 0) {
                userArray.add(String.valueOf(mDHandler.getAllEmployeeRegistration().get(i).getEmployeeName()));
            }
        }
        userArray.add(0, "All");

        posNoArray.add("All");
        posNoArray.add("4");
        posNoArray.add("7");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, shiftNameArray);
        shiftName.setAdapter(adapter);

        ArrayAdapter<String> adapterPosNo = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, posNoArray);
        posNo.setAdapter(adapterPosNo);

        ArrayAdapter<String> adapterUserName = new ArrayAdapter<>(BackOfficeActivity.this, R.layout.spinner_style, userArray);
        userName.setAdapter(adapterUserName);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ShiftNameString = shiftName.getSelectedItem().toString();
                String userNameString = userName.getSelectedItem().toString();
                String messageString = message.getText().toString();
                String DateString = date.getText().toString();
                int posNoString = -1;
                if (posNo.getSelectedItem().toString().equals("All")) {
                    posNoString = -1;
                } else {
                    posNoString = Integer.parseInt(posNo.getSelectedItem().toString());
                }

                if (!messageString.equals("")) {

                    Announcemet announcemet = new Announcemet(ShiftNameString,
                            DateString, userNameString, posNoString, messageString, 0);

                    mDHandler.addAnnouncement(announcemet);

                    Toast.makeText(BackOfficeActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    message.setText("");
                } else {
                    Toast.makeText(BackOfficeActivity.this, "Please add Message ", Toast.LENGTH_SHORT).show();
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
                        Settings.table_edit_authorized = true;
//                        Toast.makeText(BackOfficeActivity.this, "Your'r authorized to edit tables ", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent = new Intent(BackOfficeActivity.this, DineInLayout.class);
                        startActivity(intent);
                    } else {
                        Settings.table_edit_authorized = false;
                        Toast.makeText(BackOfficeActivity.this, "Your authorization number is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();

    }


    void insertCashierInOutReport(TableLayout tableLayout, String num, String Date, String pos, String cashierName,
                                  String transType, String Amount, String Times, int switchCount) {
        final TableRow row = new TableRow(BackOfficeActivity.this);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
//        lp.setMargins(2, 2, 2, 2);
        row.setLayoutParams(lp);

        for (int i = 0; i < switchCount; i++) {
            TextView textView = new TextView(BackOfficeActivity.this);

            switch (i) {
                case 0:
                    textView.setText(num);
                    break;
                case 1:
                    textView.setText(cashierName);
                    break;
                case 2:
                    textView.setText(Date);
                    break;
                case 3:
                    textView.setText(Times);
                    break;
                case 4:
                    textView.setText(pos);
                    break;
                case 5:
                    textView.setText(transType);
                    break;
                case 6:
                    textView.setText(Amount);
                    break;


            }

            textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
            textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);

            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp2.setMargins(2, 2, 2, 2);
            textView.setLayoutParams(lp2);

            row.addView(textView);

        }

        tableLayout.addView(row);

    }

    void insertSalesVolumeByItem(TableLayout table, List<OrderTransactions> transactions, Spinner cashierNo, Spinner shiftName,
                                 Spinner PosNo, TextView fromDate, TextView toDate) {

        int CashierNo = -1;
        if (cashierNo.getSelectedItem().toString().equals("All"))
            CashierNo = -1;
        else
            CashierNo = Integer.parseInt(cashierNo.getSelectedItem().toString());

        String ShiftName = shiftName.getSelectedItem().toString();

        int posNoString = -1;
        if (PosNo.getSelectedItem().toString().equals("All"))
            posNoString = -1;
        else
            posNoString = Integer.parseInt(PosNo.getSelectedItem().toString());

        double totalQty = 0;
        double totalTotal = 0;
        for (int i = 0; i < transactions.size(); i++) {
            if (filters(fromDate.getText().toString(), toDate.getText().toString(), transactions.get(i).getVoucherDate())) {

                if (transactions.get(i).getShiftName().equals(ShiftName) || ShiftName.equals("All")) {
                    if (transactions.get(i).getUserNo() == CashierNo || CashierNo == -1) {
                        if (transactions.get(i).getPosNo() == posNoString || posNoString == -1) {

                            totalQty += transactions.get(i).getQty();
                            totalTotal += transactions.get(i).getTotal();

                            TableRow row = new TableRow(BackOfficeActivity.this);

                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                            row.setLayoutParams(lp);

                            for (int k = 0; k < 5; k++) {
                                TextView textView = new TextView(BackOfficeActivity.this);

                                switch (k) {
                                    case 0:
                                        textView.setText(transactions.get(i).getItemBarcode());
                                        break;
                                    case 1:
                                        textView.setText(transactions.get(i).getItemName());
                                        break;
                                    case 2:
                                        textView.setText("" + transactions.get(i).getQty());
                                        break;
                                    case 3:
                                        textView.setText("" + transactions.get(i).getPrice());
                                        break;
                                    case 4:
                                        textView.setText("" + (transactions.get(i).getQty() * transactions.get(i).getPrice()));
                                        break;
                                }

                                textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                                textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.jeans_blue));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextSize(16);

                                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(70, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                lp2.setMargins(1, 1, 1, 1);
                                textView.setLayoutParams(lp2);

                                row.addView(textView);
                            }
                            table.addView(row);
                        }
                    }
                }
            }
        }

        if (totalQty != 0 && totalTotal != 0) {

            TableRow row = new TableRow(BackOfficeActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            for (int k = 0; k < 5; k++) {

                TextView textView = new TextView(BackOfficeActivity.this);
                switch (k) {
                    case 0:
                        textView.setText(transactions.get(0).getItemCategory());
                        break;
                    case 1:
                        textView.setText(" ");
                        break;
                    case 2:
                        textView.setText("" + totalQty);
                        break;
                    case 3:
                        textView.setText(" ");
                        break;
                    case 4:
                        textView.setText("" + totalTotal);
                        break;
                }
                textView.setTextColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.text_color));
                textView.setBackgroundColor(ContextCompat.getColor(BackOfficeActivity.this, R.color.light_blue_over));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(70, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                lp2.setMargins(1, 1, 1, 1);
                textView.setLayoutParams(lp2);

                row.addView(textView);
            }
            table.addView(row);
        }
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
        return !s.equals("") && !cName.equals("") && !cValue.equals("");
    }

    public void slideUp(View view) {
        visible = 1;
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
        visible = 0;
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                view.getHeight() + 3);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);

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
        reCancellationSupervisor = (LinearLayout) findViewById(R.id.re_cancellation_supervisor);
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
        voiding_reasons = (LinearLayout) findViewById(R.id.voiding_reasons);
        menuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        store = (LinearLayout) findViewById(R.id.store);
        storeOperation = (LinearLayout) findViewById(R.id.store_operation);
        users = (LinearLayout) findViewById(R.id.users);
        moneyCategory = (LinearLayout) findViewById(R.id.money_category);
        salesTotal = (LinearLayout) findViewById(R.id.sales_total);
        cashierInOut = (LinearLayout) findViewById(R.id.cashier_in_out);
        canceledOrderHistory = (LinearLayout) findViewById(R.id.canceled_order_history);
        x_report = (LinearLayout) findViewById(R.id.x_report);
        z_report = (LinearLayout) findViewById(R.id.z_report);
        market_report_ = (LinearLayout) findViewById(R.id.market_report_);
        salesReportForDay = (LinearLayout) findViewById(R.id.sales_report_for_day);
        salesByHours = (LinearLayout) findViewById(R.id.sales_by_houres);
        salesVolumeByItem = (LinearLayout) findViewById(R.id.sales_volume_by_item);
        topSalesItemReport = (LinearLayout) findViewById(R.id.top_sales_item_report);
        topGroupSalesReport = (LinearLayout) findViewById(R.id.top_group_sales_report);
        topFamilySalesReport = (LinearLayout) findViewById(R.id.top_family_sales_report);
        salesReportByCustomer = (LinearLayout) findViewById(R.id.sales_report_by_cusromer);
        salesReportByCardType = (LinearLayout) findViewById(R.id.sales_report_by_card_type);
        waiterSalesReport = (LinearLayout) findViewById(R.id.waiter_sales_report);
        tableActionReport = (LinearLayout) findViewById(R.id.table_action_report);
        profitLossReport = (LinearLayout) findViewById(R.id.profit_loss_report);
        detailSalesReport = (LinearLayout) findViewById(R.id.detail_sales_report);
        simpleSalesTotalReport = (LinearLayout) findViewById(R.id.simple_sales_total_report);
        SoldQtyReport = (LinearLayout) findViewById(R.id.sold_qty_report);
        userOrderCountReport = (LinearLayout) findViewById(R.id.user_order_count_report);
        reCancellationReport = (LinearLayout) findViewById(R.id.re_cancellation_report);

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
        reCancellationSupervisor.setOnClickListener(onClickListener2);
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
        voiding_reasons.setOnClickListener(onClickListener2);
        menuLayout.setOnClickListener(onClickListener2);
        store.setOnClickListener(onClickListener2);
        storeOperation.setOnClickListener(onClickListener2);
        users.setOnClickListener(onClickListener2);
        moneyCategory.setOnClickListener(onClickListener2);
        salesTotal.setOnClickListener(onClickListener2);
        cashierInOut.setOnClickListener(onClickListener2);
        canceledOrderHistory.setOnClickListener(onClickListener2);
        x_report.setOnClickListener(onClickListener2);
        z_report.setOnClickListener(onClickListener2);
        market_report_.setOnClickListener(onClickListener2);
        salesReportForDay.setOnClickListener(onClickListener2);
        salesByHours.setOnClickListener(onClickListener2);
        salesVolumeByItem.setOnClickListener(onClickListener2);
        topSalesItemReport.setOnClickListener(onClickListener2);
        topGroupSalesReport.setOnClickListener(onClickListener2);
        topFamilySalesReport.setOnClickListener(onClickListener2);
        salesReportByCustomer.setOnClickListener(onClickListener2);
        salesReportByCardType.setOnClickListener(onClickListener2);
        waiterSalesReport.setOnClickListener(onClickListener2);
        tableActionReport.setOnClickListener(onClickListener2);
        profitLossReport.setOnClickListener(onClickListener2);
        detailSalesReport.setOnClickListener(onClickListener2);
        simpleSalesTotalReport.setOnClickListener(onClickListener2);
        SoldQtyReport.setOnClickListener(onClickListener2);
        userOrderCountReport.setOnClickListener(onClickListener2);
        reCancellationReport.setOnClickListener(onClickListener2);

    }
}
