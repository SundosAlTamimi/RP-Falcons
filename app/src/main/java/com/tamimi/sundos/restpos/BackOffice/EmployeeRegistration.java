package com.tamimi.sundos.restpos.BackOffice;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.DatabaseHandler;
import com.tamimi.sundos.restpos.Models.EmployeeRegistrationModle;
import com.tamimi.sundos.restpos.Models.JobGroup;
import com.tamimi.sundos.restpos.R;
import com.tamimi.sundos.restpos.Settings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class EmployeeRegistration extends AppCompatActivity {

    TableLayout tableEmployee;
    EditText  empName, mobileNo, userPassword, payRate;
    TextView empNo, hireDate, termination;
    Button newButton, saveButton, exitButton;
    Spinner securityLevel, payBasic, holidayPay, jobGroup, employeeType;
    CheckBox active;
    DatabaseHandler mDHandler;
    ArrayAdapter<String> jobSpinner, holidayPaySpinner, securityLevelSpinner, payBasicSpinner, employeeTypeSpinner;
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.employee_registration);

        myCalendar = Calendar.getInstance();

        final ArrayList<String> jobList = new ArrayList<>();
        final ArrayList<String> empNameList = new ArrayList<>();
        final ArrayList<Integer> empNOList = new ArrayList<>();
        final ArrayList<Integer> mobileList = new ArrayList<>();
        final ArrayList<String> securityList = new ArrayList<>();
        final ArrayList<Integer> userPassList = new ArrayList<>();
        final ArrayList<Integer> ActiveList = new ArrayList<>();
        final ArrayList<String> hireDateList = new ArrayList<>();
        final ArrayList<String> terminationList = new ArrayList<>();
        final ArrayList<String> payBasicList = new ArrayList<>();
        final ArrayList<String> payRateList = new ArrayList<>();
        final ArrayList<String> holidayList = new ArrayList<>();
        final ArrayList<Integer> empTypeList = new ArrayList<>();
        ArrayList<JobGroup> jopGroupListForSpinner = new ArrayList<>();
        final ArrayList<String> jopGroupSpinner = new ArrayList<>();
        ArrayList<String> payBasicListSpinner = new ArrayList<>();
        ArrayList<String> holidayPayListSpinner = new ArrayList<>();
        ArrayList<String> securityLevelListSpinner = new ArrayList<>();
        ArrayList<String> employeeTypeListSpinner = new ArrayList<>();

        payBasicListSpinner.add("hourly pau");
        holidayPayListSpinner.add("over Time");
        securityLevelListSpinner.add("ip");
        securityLevelListSpinner.add("https");

        employeeTypeListSpinner.add("Cashier");
        employeeTypeListSpinner.add("waiter");
        employeeTypeListSpinner.add("Employee");

        Initialization();
        mDHandler = new DatabaseHandler(EmployeeRegistration.this);

        jopGroupListForSpinner = mDHandler.getAllJobGroup();
        final int[] serial = {mDHandler.getAllEmployeeRegistration().size()};
        empNo.setText(""+ serial[0]);



        hireDate.setOnClickListener(v -> new DatePickerDialog(EmployeeRegistration.this, dateListener(hireDate), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        termination.setOnClickListener(v -> new DatePickerDialog(EmployeeRegistration.this, dateListener(termination), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        for (int i = 0; i < jopGroupListForSpinner.size(); i++) {
            jopGroupSpinner.add(jopGroupListForSpinner.get(i).getJobGroup());
        }

        jobSpinner = new ArrayAdapter<String>(EmployeeRegistration.this, R.layout.spinner_style, jopGroupSpinner);
        jobGroup.setAdapter(jobSpinner);

        securityLevelSpinner = new ArrayAdapter<String>(EmployeeRegistration.this, R.layout.spinner_style, securityLevelListSpinner);
        securityLevel.setAdapter(securityLevelSpinner);

        payBasicSpinner = new ArrayAdapter<String>(EmployeeRegistration.this, R.layout.spinner_style, payBasicListSpinner);
        payBasic.setAdapter(payBasicSpinner);

        holidayPaySpinner = new ArrayAdapter<String>(EmployeeRegistration.this, R.layout.spinner_style, holidayPayListSpinner);
        holidayPay.setAdapter(holidayPaySpinner);

        employeeTypeSpinner = new ArrayAdapter<String>(EmployeeRegistration.this, R.layout.spinner_style, employeeTypeListSpinner);
        employeeType.setAdapter(employeeTypeSpinner);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!empNameList.isEmpty()) {
                    for (int i = 0; i < empNameList.size(); i++) {
                        EmployeeRegistrationModle employeeRegistrationModle = new EmployeeRegistrationModle();

                        employeeRegistrationModle.setJobGroup(jobList.get(i).toString());
                        employeeRegistrationModle.setEmployeeName(empNameList.get(i).toString());
                        employeeRegistrationModle.setEmployeeNO(empNOList.get(i));
                        employeeRegistrationModle.setMobileNo(mobileList.get(i));
                        employeeRegistrationModle.setSecurityLevel(securityList.get(i).toString());
                        employeeRegistrationModle.setUserPassword(userPassList.get(i));
                        employeeRegistrationModle.setActive(ActiveList.get(i));
                        employeeRegistrationModle.setHireDate(hireDateList.get(i).toString());
                        employeeRegistrationModle.setTerminationDate(terminationList.get(i).toString());
                        employeeRegistrationModle.setPayBasic(payBasicList.get(i).toString());
                        employeeRegistrationModle.setPayRate(payRateList.get(i).toString());
                        employeeRegistrationModle.setHolidayPay(holidayList.get(i).toString());
                        employeeRegistrationModle.setEmployeeType(empTypeList.get(i));
                        employeeRegistrationModle.setShiftNo(Settings.shift_number);
                        employeeRegistrationModle.setShiftName(Settings.shift_name);

                        mDHandler.addEmployeeRegistration(employeeRegistrationModle);
                        Toast.makeText(EmployeeRegistration.this, "Saved Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(EmployeeRegistration.this, " Not Have Any Data In Table ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!jopGroupSpinner.isEmpty()) {
                    if (!empName.getText().toString().equals("") && !empNo.getText().toString().equals("") && !mobileNo.getText().toString().equals("") && !hireDate.getText().toString().equals("") &&
                            !termination.getText().toString().equals("") && !payRate.getText().toString().equals("")) {
                        if(userPassword.getText().toString().length()==4){
                        String pass = userPassword.getText().toString();
                        jobList.add(jobGroup.getSelectedItem().toString());
                        empNameList.add(empName.getText().toString());
//                        empNOList.add(Integer.parseInt(empNo.getText().toString()));
                        empNOList.add(serial[0]);
                        mobileList.add(Integer.parseInt(mobileNo.getText().toString()));
                        securityList.add(securityLevel.getSelectedItem().toString());
                        userPassList.add(Integer.parseInt(pass));
                        if (active.isChecked()) {
                            ActiveList.add(1);
                        } else {
                            ActiveList.add(0);
                        }
                        hireDateList.add(hireDate.getText().toString());
                        terminationList.add(termination.getText().toString());
                        payBasicList.add(payBasic.getSelectedItem().toString());
                        payRateList.add(payRate.getText().toString());
                        holidayList.add(holidayPay.getSelectedItem().toString());
                        switch (employeeType.getSelectedItem().toString()) {
                            case "Cashier":
                                empTypeList.add(0);
                                break;
                            case "waiter":
                                empTypeList.add(1);
                                break;
                            case "Employee":
                                empTypeList.add(2);
                                break;
                        }

                        Toast.makeText(EmployeeRegistration.this, "OK ", Toast.LENGTH_SHORT).show();
                        insertRaw3(empName.getText().toString(), serial[0], Integer.parseInt(mobileNo.getText().toString()),
                                securityLevel.getSelectedItem().toString(), Integer.parseInt(pass), hireDate.getText().toString(), termination.getText().toString()
                                , payBasic.getSelectedItem().toString(), payRate.getText().toString(), holidayPay.getSelectedItem().toString(), employeeType.getSelectedItem().toString(), tableEmployee);

                        empName.setText("");
                        empNo.setText("");
                        mobileNo.setText("");
                        userPassword.setText("");
                        hireDate.setText("");
                        termination.setText("");
                        payRate.setText("");


                    }else {
                            Toast.makeText(EmployeeRegistration.this, "Length of user password must 4 number Please Edit password  ", Toast.LENGTH_SHORT).show();
                        }
                } else {
                        Toast.makeText(EmployeeRegistration.this, "Please Insert data ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EmployeeRegistration.this, "Please Add Job Group Before Adding new Employee ", Toast.LENGTH_SHORT).show();
                }
                serial[0]++;
                empNo.setText(""+ serial[0]);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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

    void insertRaw3(String EmpName, int empNo, int mobileNo, String
            securityLevel, int userPassword, String hireDate
            , String terminationDate, String payBasic, String payRate, String holidayPay, String EmployeeType, TableLayout tableLayout) {

        if (true) {
            final TableRow row = new TableRow(EmployeeRegistration.this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            for (int i = 0; i < 11; i++) {
                TextView textView = new TextView(EmployeeRegistration.this);
                switch (i) {
                    case 0:

                        textView.setText(EmpName);

                        break;
                    case 1:
                        textView.setText("  " + empNo);
                        break;
                    case 2:
                        textView.setText("  " + mobileNo);
                        break;
                    case 3:
                        textView.setText("  " + securityLevel);
                        break;
                    case 4:
                        textView.setText("  " + userPassword);
                        break;
                    case 5:
                        textView.setText("  " + hireDate);
                        break;
                    case 6:
                        textView.setText("  " + terminationDate);
                        break;
                    case 7:
                        textView.setText("  " + payBasic);
                        break;
                    case 8:
                        textView.setText("  " + payRate);
                        break;
                    case 9:
                        textView.setText("  " + holidayPay);
                        break;
                    case 10:
                        textView.setText("  " + EmployeeType);
                        break;
                }

                textView.setTextColor(ContextCompat.getColor(EmployeeRegistration.this, R.color.text_color));
                textView.setGravity(Gravity.CENTER);

                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                textView.setLayoutParams(lp2);

                row.addView(textView);

            }

            tableLayout.addView(row);
        }
    }


    void Initialization() {
        {
            saveButton = (Button) findViewById(R.id.saveButton5);
            exitButton = (Button) findViewById(R.id.deleteButton5);
            newButton = (Button) findViewById(R.id.newButton5);

            empNo = (TextView) findViewById(R.id.empNo);
            empName = (EditText) findViewById(R.id.empName);
            mobileNo = (EditText) findViewById(R.id.mobileNo);
            userPassword = (EditText) findViewById(R.id.user_password8);
            hireDate = (TextView) findViewById(R.id.hire_date2);
            termination = (TextView) findViewById(R.id.termination);
            payRate = (EditText) findViewById(R.id.pay_rate);

            securityLevel = (Spinner) findViewById(R.id.spinner_security);
            payBasic = (Spinner) findViewById(R.id.pay_basic);
            holidayPay = (Spinner) findViewById(R.id.holiday_pay);
            jobGroup = (Spinner) findViewById(R.id.job_spinner);
            employeeType = (Spinner) findViewById(R.id.spinner_employee_type);
            active = (CheckBox) findViewById(R.id.employeeCheck);

            tableEmployee = (TableLayout) findViewById(R.id.employee_reg);
        }
    }

}
