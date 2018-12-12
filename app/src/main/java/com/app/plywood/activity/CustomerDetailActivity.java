package com.app.plywood.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import spencerstudios.com.bungeelib.Bungee;
import thebat.lib.validutil.ValidUtils;

public class CustomerDetailActivity extends AppCompatActivity {


    CustomEditText etCompany, etName, etAddress, etPhone, etCity, etState, etPincode, etBAddress, etBCity, etBState,
            etBPincode, etSAddress, etSCity, etSState, etSPincode;
    TextView tvCompany, tvName, tvAddress, tvPhone, tvCity, tvState, tvPincode;
    Button btnEdit, btnUpdate;
    String id, name, phone, address, city, state, company, pincode, baddress, bcity, bstate, bpincode, saddress,
            scity, sstate, spincode;
    LinearLayout layoutNonEdit, layoutEdit;
    ValidUtils validUtils;
    String EDIT_CUSTOMER_URL = Constants.BASE_URL + Constants.EDIT_CUSTOMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("CUSTOMER DETAILS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        tvCompany = findViewById(R.id.cus_det_tv_cname);
        etCompany = findViewById(R.id.cus_det_et_cname);
        tvName = findViewById(R.id.cus_det_tv_name);
        etName = findViewById(R.id.cus_det_et_name);
        tvAddress = findViewById(R.id.cus_det_tv_address);
        etAddress = findViewById(R.id.cus_det_et_address);
        tvPhone = findViewById(R.id.cus_det_tv_phone);
        etPhone = findViewById(R.id.cus_det_et_phone);
        tvCity= findViewById(R.id.cus_det_tv_city);
        etCity= findViewById(R.id.cus_det_et_city);
        tvState= findViewById(R.id.cus_det_tv_state);
        tvPincode= findViewById(R.id.cus_det_tv_pincode);
        etState= findViewById(R.id.cus_det_et_state);
        etPincode= findViewById(R.id.cus_det_et_pincode);
        etBAddress = findViewById(R.id.cus_det_et_baddress);
        etBCity= findViewById(R.id.cus_det_et_bcity);
        etBState= findViewById(R.id.cus_det_et_bstate);
        etBPincode= findViewById(R.id.cus_det_et_bpincode);
        etSAddress = findViewById(R.id.cus_det_et_saddress);
        etSCity= findViewById(R.id.cus_det_et_scity);
        etSState= findViewById(R.id.cus_det_et_sstate);
        etSPincode= findViewById(R.id.cus_det_et_spincode);
        btnEdit = findViewById(R.id.cus_det_btn_edit);
        btnUpdate = findViewById(R.id.cus_det_btn_update);
        layoutEdit = findViewById(R.id.edit_layout);
        layoutNonEdit = findViewById(R.id.nonedit_layout);

        validUtils = new ValidUtils();

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            id = bundle.getString("id");
            name = bundle.getString("name");
            address = bundle.getString("address");
            city = bundle.getString("city");
            state = bundle.getString("state");
            phone = bundle.getString("mobile");
            company = bundle.getString("company");
            pincode = bundle.getString("pincode");

            tvName.setText(name);
            tvState.setText(state);
            tvCity.setText(city);
            tvPhone.setText(phone);
            tvCompany.setText(company);
            tvAddress.setText(address);
            tvPincode.setText(pincode);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutNonEdit.setVisibility(View.GONE);
                layoutEdit.setVisibility(View.VISIBLE);

                if (bundle != null){

                    id = bundle.getString("id");
                    name = bundle.getString("name");
                    address = bundle.getString("address");
                    city = bundle.getString("city");
                    state = bundle.getString("state");
                    phone = bundle.getString("mobile");
                    company = bundle.getString("company");
                    pincode = bundle.getString("pincode");
                    baddress = bundle.getString("baddress");
                    bcity = bundle.getString("bcity");
                    bstate = bundle.getString("bstate");
                    bpincode = bundle.getString("bpincode");
                    saddress = bundle.getString("saddress");
                    scity = bundle.getString("scity");
                    sstate = bundle.getString("sstate");
                    spincode = bundle.getString("spincode");

                    etName.setText(name);
                    etState.setText(state);
                    etCity.setText(city);
                    etPhone.setText(phone);
                    etCompany.setText(company);
                    etAddress.setText(address);
                    etPincode.setText(pincode);
                    etBAddress.setText(baddress);
                    etBCity.setText(bcity);
                    etBState.setText(bstate);
                    etBPincode.setText(bpincode);
                    etSAddress.setText(saddress);
                    etSCity.setText(scity);
                    etSState.setText(sstate);
                    etSPincode.setText(spincode);
                }

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        validUtils.showProgressDialog(CustomerDetailActivity.this, CustomerDetailActivity.this);
                        StringRequest request = new StringRequest(Request.Method.POST, EDIT_CUSTOMER_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response);

                                            if (jsonObject.getString("status")
                                                    .equalsIgnoreCase("success")){
                                                validUtils.hideProgressDialog();
                                                startActivity(new Intent(CustomerDetailActivity.this, SaleActivity.class));
                                                Bungee.split(CustomerDetailActivity.this);
                                                validUtils.showToast(CustomerDetailActivity.this, jsonObject.getString("message"));

                                            }else if (jsonObject.getString("status")
                                                    .equalsIgnoreCase("no data")){
                                                validUtils.hideProgressDialog();
                                                validUtils.showToast(CustomerDetailActivity.this, jsonObject.getString("message"));

                                            }else {
                                                validUtils.hideProgressDialog();
                                                validUtils.showToast(CustomerDetailActivity.this, "Something went Wrong");
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            validUtils.hideProgressDialog();
                                            validUtils.showToast(CustomerDetailActivity.this, e.getMessage());

                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(CustomerDetailActivity.this, error.getMessage());

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
                                baddress = etBAddress.getText().toString().trim();
                                bcity = etBCity.getText().toString().trim();
                                bstate = etBState.getText().toString().trim();
                                bpincode = etBPincode.getText().toString().trim();
                                saddress = etSAddress.getText().toString().trim();
                                scity = etSCity.getText().toString().trim();
                                sstate = etSState.getText().toString().trim();
                                spincode = etSPincode.getText().toString().trim();

                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("c_name", name);
                                params.put("address", address);
                                params.put("city", city);
                                params.put("state", state);
                                params.put("mobileno", phone);
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
                        RequestQueue queue = Volley.newRequestQueue(CustomerDetailActivity.this);
                        queue.add(request);
                    }
                });
            }
        });
    }
}
