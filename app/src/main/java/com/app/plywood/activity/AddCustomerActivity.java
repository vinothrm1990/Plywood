package com.app.plywood.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.plywood.R;
import com.app.plywood.helper.Constants;
import com.libizo.CustomEditText;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import spencerstudios.com.bungeelib.Bungee;
import thebat.lib.validutil.ValidUtils;

public class AddCustomerActivity extends AppCompatActivity {

    CustomEditText etCompany, etName, etAddress, etPhone, etCity, etState, etPincode, etBAddress, etBCity, etBState,
            etBPincode, etSAddress, etSCity, etSState, etSPincode;
    CheckBox cbBill, cbShip;
    ImageView ivBAdd, ivBRemove, ivSAdd, ivSRemove;
    Button btnAdd;
    LinearLayout billLayout, shipLayout;
    String name, address, city, state, phone, company, pincode,
            baddress, bcity, bstate, bpincode,
            saddress, scity, sstate, spincode;
    ValidUtils validUtils;
    String ADD_CUSTOMER_URL = Constants.BASE_URL + Constants.ADD_CUSTOMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("ADD CUSTOMER");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        etCompany = findViewById(R.id.cus_add_et_cname);
        etName = findViewById(R.id.cus_add_et_name);
        etAddress = findViewById(R.id.cus_add_et_address);
        etPhone = findViewById(R.id.cus_add_et_phone);
        etCity= findViewById(R.id.cus_add_et_city);
        etState= findViewById(R.id.cus_add_et_state);
        etPincode= findViewById(R.id.cus_add_et_pincode);
        etBAddress = findViewById(R.id.cus_bill_et_address);
        etBCity= findViewById(R.id.cus_bill_et_city);
        etBState= findViewById(R.id.cus_bill_et_state);
        etBPincode= findViewById(R.id.cus_bill_et_pincode);
        etSAddress = findViewById(R.id.cus_ship_et_address);
        etSCity= findViewById(R.id.cus_ship_et_city);
        etSState= findViewById(R.id.cus_ship_et_state);
        etSPincode= findViewById(R.id.cus_ship_et_pincode);
        cbBill = findViewById(R.id.cus_cb_bill);
        cbShip = findViewById(R.id.cus_cb_ship);
        ivBAdd = findViewById(R.id.cus_bill_iv_add);
        ivBRemove = findViewById(R.id.cus_bill_iv_remove);
        ivSAdd = findViewById(R.id.cus_ship_iv_add);
        ivSRemove = findViewById(R.id.cus_ship_iv_remove);
        btnAdd = findViewById(R.id.cus_add_btn_update);
        billLayout = findViewById(R.id.cus_bill_layout);
        shipLayout = findViewById(R.id.cus_ship_layout);

        ivBAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                billLayout.setVisibility(View.VISIBLE);
                ivBRemove.setVisibility(View.VISIBLE);
                ivBAdd.setVisibility(View.GONE);
            }
        });

        ivBRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                billLayout.setVisibility(View.GONE);
                ivBRemove.setVisibility(View.GONE);
                ivBAdd.setVisibility(View.VISIBLE);
            }
        });

        ivSAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shipLayout.setVisibility(View.VISIBLE);
                ivSRemove.setVisibility(View.VISIBLE);
                ivSAdd.setVisibility(View.GONE);
            }
        });

        ivSRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shipLayout.setVisibility(View.GONE);
                ivSRemove.setVisibility(View.GONE);
                ivSAdd.setVisibility(View.VISIBLE);
            }
        });

        cbBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cbBill.isChecked()){
                    String address = etAddress.getText().toString().trim();
                    String city = etCity.getText().toString().trim();
                    String state = etState.getText().toString().trim();
                    String pincode = etPincode.getText().toString().trim();

                    etBAddress.setText(address);
                    etBCity.setText(city);
                    etBState.setText(state);
                    etBPincode.setText(pincode);
                }else {
                    etBAddress.setText("");
                    etBCity.setText("");
                    etBState.setText("");
                    etBPincode.setText("");
                }
            }
        });

        cbShip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cbShip.isChecked()){
                    String address = etBAddress.getText().toString().trim();
                    String city = etBCity.getText().toString().trim();
                    String state = etBState.getText().toString().trim();
                    String pincode = etBPincode.getText().toString().trim();

                    etSAddress.setText(address);
                    etSCity.setText(city);
                    etSState.setText(state);
                    etSPincode.setText(pincode);
                }else {
                    etSAddress.setText("");
                    etSCity.setText("");
                    etSState.setText("");
                    etSPincode.setText("");
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = false;

                if (!validUtils.validateEditTexts(etCompany)){
                    validation =  true;
                    validUtils.showToast(AddCustomerActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etName)){
                    validation =  true;
                    validUtils.showToast(AddCustomerActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etPhone)){
                    validation =  true;
                    validUtils.showToast(AddCustomerActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etAddress)){
                    validation =  true;
                    validUtils.showToast(AddCustomerActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etCity)){
                    validation =  true;
                    validUtils.showToast(AddCustomerActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etState)){
                    validation =  true;
                    validUtils.showToast(AddCustomerActivity.this, "Feilds are Empty");

                }

                if (!validUtils.validateEditTexts(etPincode)){
                    validation =  true;
                    validUtils.showToast(AddCustomerActivity.this, "Feilds are Empty");
                }
                if (validation == false){
                    addCustomer();
                }

            }
        });
    }

    private void addCustomer() {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.POST, ADD_CUSTOMER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                                validUtils.hideProgressDialog();
                                startActivity(new Intent(AddCustomerActivity.this, CustomerActivity.class));
                                Bungee.split(AddCustomerActivity.this);

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(AddCustomerActivity.this, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(AddCustomerActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(AddCustomerActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(AddCustomerActivity.this, error.getMessage());
                    }
                })
        {

            @Override
            protected Map<String, String> getParams()
            {
                name = etName.getText().toString().trim();
                address = etAddress.getText().toString().trim();
                city = etCity.getText().toString().trim();
                state = etState.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                company = etCompany.getText().toString().trim();
                pincode = etPincode.getText().toString().trim();

                if (cbBill.isChecked()){
                    baddress = etBAddress.getText().toString().trim();
                    bcity = etBCity.getText().toString().trim();
                    bstate = etBState.getText().toString().trim();
                    bpincode = etBPincode.getText().toString().trim();
                }else {
                    baddress = etBAddress.getText().toString().trim();
                    bcity = etBCity.getText().toString().trim();
                    bstate = etBState.getText().toString().trim();
                    bpincode = etBPincode.getText().toString().trim();
                }

                if (cbShip.isChecked()){
                    saddress = etSAddress.getText().toString().trim();
                    scity = etSCity.getText().toString().trim();
                    sstate = etSState.getText().toString().trim();
                    spincode = etSPincode.getText().toString().trim();
                }else {
                    saddress = etSAddress.getText().toString().trim();
                    scity = etSCity.getText().toString().trim();
                    sstate = etSState.getText().toString().trim();
                    spincode = etSPincode.getText().toString().trim();
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("c_name", name);
                params.put("address", address);
                params.put("city", city);
                params.put("state", state);
                params.put("mobile", phone);
                params.put("company", company);
                params.put("pincode", pincode);
                params.put("b_address", baddress);
                params.put("b_city", bcity);
                params.put("b_state", bstate);
                params.put("b_pincode", bpincode);
                params.put("s_address", saddress);
                params.put("s_city", scity);
                params.put("s_state", sstate);
                params.put("s_pincode", spincode);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AddCustomerActivity.this);
        queue.add(request);
    }
}
