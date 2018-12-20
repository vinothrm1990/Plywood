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

public class VendorDetailActivity extends AppCompatActivity {

    CustomEditText etCompany, etName, etAddress, etPhone, etCity, etState, etPincode;
    TextView tvCompany, tvName, tvAddress, tvPhone, tvCity, tvState, tvPincode;
    Button btnEdit, btnUpdate;
    String id, name, phone, address, city, state, company, pincode;
    LinearLayout layoutNonEdit, layoutEdit;
    ValidUtils validUtils;
    String EDIT_VENDOR_URL = Constants.BASE_URL + Constants.EDIT_VENDOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_detail);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("VENDOR DETAILS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        tvCompany = findViewById(R.id.ven_det_tv_cname);
        etCompany = findViewById(R.id.ven_det_et_cname);
        tvName = findViewById(R.id.ven_det_tv_name);
        etName = findViewById(R.id.ven_det_et_name);
        tvAddress = findViewById(R.id.ven_det_tv_address);
        etAddress = findViewById(R.id.ven_det_et_address);
        tvPhone = findViewById(R.id.ven_det_tv_phone);
        etPhone = findViewById(R.id.ven_det_et_phone);
        tvCity= findViewById(R.id.ven_det_tv_city);
        etCity= findViewById(R.id.ven_det_et_city);
        tvState= findViewById(R.id.ven_det_tv_state);
        tvPincode= findViewById(R.id.ven_det_tv_pincode);
        etState= findViewById(R.id.ven_det_et_state);
        etPincode= findViewById(R.id.ven_det_et_pincode);
        btnEdit = findViewById(R.id.ven_det_btn_edit);
        btnUpdate = findViewById(R.id.ven_det_btn_update);
        layoutEdit = findViewById(R.id.edit_layout);
        layoutNonEdit = findViewById(R.id.nonedit_layout);

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

                    etName.setText(name);
                    etState.setText(state);
                    etCity.setText(city);
                    etPhone.setText(phone);
                    etCompany.setText(company);
                    etAddress.setText(address);
                    etPincode.setText(pincode);
                }

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        validUtils.showProgressDialog(VendorDetailActivity.this, VendorDetailActivity.this);
                        StringRequest request = new StringRequest(Request.Method.POST, EDIT_VENDOR_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response);

                                            if (jsonObject.getString("status")
                                                    .equalsIgnoreCase("success")){
                                                validUtils.hideProgressDialog();
                                                startActivity(new Intent(VendorDetailActivity.this, PurchaseActivity.class));
                                                Bungee.split(VendorDetailActivity.this);
                                                validUtils.showToast(VendorDetailActivity.this, jsonObject.getString("message"));

                                            }else if (jsonObject.getString("status")
                                                    .equalsIgnoreCase("no data")){
                                                validUtils.hideProgressDialog();
                                                validUtils.showToast(VendorDetailActivity.this, jsonObject.getString("message"));

                                            }else if (jsonObject.getString("status")
                                                    .equalsIgnoreCase("failed")){
                                                validUtils.hideProgressDialog();
                                                validUtils.showToast(VendorDetailActivity.this, jsonObject.getString("message"));

                                            }else {
                                                validUtils.hideProgressDialog();
                                                validUtils.showToast(VendorDetailActivity.this, "Something went Wrong");
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            validUtils.hideProgressDialog();
                                            validUtils.showToast(VendorDetailActivity.this, e.getMessage());

                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(VendorDetailActivity.this, error.getMessage());

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

                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("name", name);
                                params.put("company", company);
                                params.put("mobileno", phone);
                                params.put("address", address);
                                params.put("city", city);
                                params.put("state", state);
                                params.put("pincode", pincode);
                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(VendorDetailActivity.this);
                        queue.add(request);
                    }
                });
            }
        });
    }
}
