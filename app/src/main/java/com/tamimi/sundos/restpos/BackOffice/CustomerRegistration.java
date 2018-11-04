package com.tamimi.sundos.restpos.BackOffice;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.tamimi.sundos.restpos.DatabaseHandler;
import com.tamimi.sundos.restpos.Models.CustomerRegistrationModel;
import com.tamimi.sundos.restpos.Models.MemberShipGroup;
import com.tamimi.sundos.restpos.R;
import com.tamimi.sundos.restpos.Settings;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerRegistration extends AppCompatActivity {

    Spinner memberShipGroup;
    Button saveButton, exitButton, newButton;
    EditText customerName, customerCode, memberShipCardNo, streetNoName, city, phoneNo, MobileNo, NameShow, birthday, anniversary, occupation, email, totalPoint, redeemedPoint, remaining, remark;
    RadioGroup gender;
    RadioButton mal, female;
    ArrayAdapter<String> memberShipAdapter;

    private static DatabaseHandler mDHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customer_registration);


        final ArrayList<String> memberShipGroupList = new ArrayList<>();
        final ArrayList<String> customerNameList = new ArrayList<>();
        final ArrayList<String> customerCodeList = new ArrayList<>();
        final ArrayList<Integer> memberShipCardList = new ArrayList<>();
        final ArrayList<String> genderList = new ArrayList<>();
        final ArrayList<String> streetNoNameList = new ArrayList<>();
        final ArrayList<String> cityList = new ArrayList<>();
        final ArrayList<Integer> phoneList = new ArrayList<>();
        final ArrayList<Integer> mobileList = new ArrayList<>();
        final ArrayList<String> nameShowList = new ArrayList<>();
        final ArrayList<String> birthdayList = new ArrayList<>();
        final ArrayList<String> anniversaryList = new ArrayList<>();
        final ArrayList<String> occupationList = new ArrayList<>();
        final ArrayList<String> emailList = new ArrayList<>();
        final ArrayList<Integer> totalPointList = new ArrayList<>();
        final ArrayList<Integer> redeemedPointList = new ArrayList<>();
        final ArrayList<Integer> remainingPointList = new ArrayList<>();
        final ArrayList<String> remarkList = new ArrayList<>();

        initialize();
//        NameShow.setText(customerName.getText());

        customerName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NameShow.setText(customerName.getText() + "");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        gender.check(R.id.radio_mal);

        mDHandler = new DatabaseHandler(CustomerRegistration.this);
        ArrayList<MemberShipGroup> memberShipArray = new ArrayList<MemberShipGroup>();
        final ArrayList<String> memberShipList = new ArrayList<String>();

        memberShipArray = mDHandler.getAllMemberShipGroup();

        for (int i = 0; i < memberShipArray.size(); i++) {
            memberShipList.add(memberShipArray.get(i).getMemberShipGroup());
        }

        memberShipAdapter = new ArrayAdapter<String>(CustomerRegistration.this, R.layout.spinner_style, memberShipList);
        memberShipGroup.setAdapter(memberShipAdapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!customerNameList.isEmpty()) {
                    for (int i = 0; i < customerNameList.size(); i++) {
                        CustomerRegistrationModel customerRegistrationModel = new CustomerRegistrationModel();

                        customerRegistrationModel.setMemberShipGroup(memberShipGroupList.get(i));
                        customerRegistrationModel.setCustomerName(customerNameList.get(i));
                        customerRegistrationModel.setCustomerCode(customerCodeList.get(i));
                        customerRegistrationModel.setMemberShipCardNO(memberShipCardList.get(i));
                        customerRegistrationModel.setCoender(genderList.get(i));
                        customerRegistrationModel.setStreetNoName(streetNoNameList.get(i));
                        customerRegistrationModel.setCity(cityList.get(i));
                        customerRegistrationModel.setPhoneNo(phoneList.get(i));
                        customerRegistrationModel.setRemark(remarkList.get(i));
                        customerRegistrationModel.setMobileNo(mobileList.get(i));
                        customerRegistrationModel.setNameShow(nameShowList.get(i));
                        customerRegistrationModel.setBirthday(birthdayList.get(i));
                        customerRegistrationModel.setAnniversary(anniversaryList.get(i));
                        customerRegistrationModel.setOccupation(occupationList.get(i));
                        customerRegistrationModel.setEmail(emailList.get(i));
//                        customerRegistrationModel.setTotalPoint(totalPointList.get(i));
//                        customerRegistrationModel.setRedeemedPoint(redeemedPointList.get(i));
//                        customerRegistrationModel.setRemainingPoint(remainingPointList.get(i));
                        customerRegistrationModel.setShiftNO(Settings.shift_number);
                        customerRegistrationModel.setShiftName(Settings.shift_name);

                        mDHandler.addCustomerRegistration(customerRegistrationModel);
                        Toast.makeText(CustomerRegistration.this, "Saved Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(CustomerRegistration.this, " Not Have Any Data In Table ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!memberShipList.isEmpty()) {
                    if (!customerName.getText().toString().equals("") && !customerCode.getText().toString().equals("") &&
                            !NameShow.getText().toString().equals("") && !birthday.getText().toString().equals("")) {

                        memberShipGroupList.add(memberShipGroup.getSelectedItem().toString());

                        customerNameList.add(customerName.getText().toString());

                        customerCodeList.add(customerCode.getText().toString());
                        nameShowList.add(NameShow.getText().toString());
                        birthdayList.add(birthday.getText().toString());
                        if (mal.isChecked()) {
                            genderList.add("male");
                            Log.e("+++", "male");
                        } else {
                            genderList.add("female");
                            Log.e("+++", "female");
                        }

                        if (!memberShipCardNo.getText().toString().equals("")) {
                            memberShipCardList.add(Integer.parseInt(memberShipCardNo.getText().toString()));
                        } else {
                            memberShipCardList.add(-1);
                        }
                        if (!streetNoName.getText().toString().equals("")) {
                            streetNoNameList.add(streetNoName.getText().toString());
                        } else {
                            streetNoNameList.add("");
                        }
                        if (!city.getText().toString().equals("")) {
                            cityList.add(city.getText().toString());
                        } else {
                            cityList.add("");
                        }

                        if (!phoneNo.getText().toString().equals("")) {
                            phoneList.add(Integer.parseInt(phoneNo.getText().toString()));
                        } else {
                            phoneList.add(-1);
                        }

                        if (!MobileNo.getText().toString().equals("")) {
                            mobileList.add(Integer.parseInt(MobileNo.getText().toString()));
                        } else {
                            mobileList.add(-1);
                        }


                        if (!anniversary.getText().toString().equals("")) {
                            anniversaryList.add(anniversary.getText().toString());
                        } else {
                            anniversaryList.add("");
                        }

                        if (!occupation.getText().toString().equals("")) {
                            occupationList.add(occupation.getText().toString());
                        } else {
                            occupationList.add("");
                        }


                        if (!email.getText().toString().equals("")) {
                            emailList.add(email.getText().toString());
                        } else {
                            emailList.add("");
                        }

                        if (!remark.getText().toString().equals("")) {
                            remarkList.add(remark.getText().toString());
                        } else {
                            remarkList.add("");
                        }

//                    totalPointList.add(Integer.parseInt(totalPoint.getText().toString()));
//                    redeemedPointList.add(Integer.parseInt(redeemedPoint.getText().toString()));
//                    remainingPointList.add(Integer.parseInt(remaining.getText().toString()));

                        Toast.makeText(CustomerRegistration.this, "OK", Toast.LENGTH_SHORT).show();

                        customerName.setText("");
                        customerCode.setText("");
                        MobileNo.setText("");
                        phoneNo.setText("");
                        city.setText("");
                        memberShipCardNo.setText("");
                        streetNoName.setText("");
                        NameShow.setText("");
                        birthday.setText("");
                        anniversary.setText("");
                        occupation.setText("");
                        email.setText("");
                        totalPoint.setText("");
                        redeemedPoint.setText("");
                        remaining.setText("");
                        remark.setText("");
                    } else {
                        Toast.makeText(CustomerRegistration.this, "Please Insert data ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CustomerRegistration.this, "please Add Member Ship Group ", Toast.LENGTH_SHORT).show();
                }


            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    void initialize() {
        saveButton = (Button) findViewById(R.id.saveButton3);
        exitButton = (Button) findViewById(R.id.deleteButton3);
        newButton = (Button) findViewById(R.id.newButton3);

        customerName = (EditText) findViewById(R.id.customer_name);
        customerCode = (EditText) findViewById(R.id.customer_code);
        memberShipCardNo = (EditText) findViewById(R.id.member_ship_card);
        streetNoName = (EditText) findViewById(R.id.streetNOEdit);
        city = (EditText) findViewById(R.id.city);
        phoneNo = (EditText) findViewById(R.id.phone_no);
        MobileNo = (EditText) findViewById(R.id.mobile_number3);
        NameShow = (EditText) findViewById(R.id.name_show);
        birthday = (EditText) findViewById(R.id.birthday);
        anniversary = (EditText) findViewById(R.id.anniversary);
        occupation = (EditText) findViewById(R.id.occupation);
        email = (EditText) findViewById(R.id.email);
        totalPoint = (EditText) findViewById(R.id.total_point);
        redeemedPoint = (EditText) findViewById(R.id.total_red);
        remark = (EditText) findViewById(R.id.remark3);
        remaining = (EditText) findViewById(R.id.total_rem);

        gender = (RadioGroup) findViewById(R.id.radio_mal_female);
        mal = (RadioButton) findViewById(R.id.radio_mal);
        female = (RadioButton) findViewById(R.id.radio_female);

        memberShipGroup = (Spinner) findViewById(R.id.spinner_member);
    }
}
