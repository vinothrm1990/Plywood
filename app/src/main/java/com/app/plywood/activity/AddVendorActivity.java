package com.app.plywood.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class AddVendorActivity extends AppCompatActivity {

    CustomEditText etCompany, etName, etAddress, etPhone, etCity, etState, etPincode;
    Button btnAdd;
    String name, address, city, state, phone, company, pincode;
    ValidUtils validUtils;
    String ADD_VENDOR_URL = Constants.BASE_URL + Constants.ADD_VENDOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("ADD VENDOR");
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
        btnAdd = findViewById(R.id.cus_add_btn_update);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = false;

                if (!validUtils.validateEditTexts(etCompany)){
                    validation =  true;
                    validUtils.showToast(AddVendorActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etName)){
                    validation =  true;
                    validUtils.showToast(AddVendorActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etPhone)){
                    validation =  true;
                    validUtils.showToast(AddVendorActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etAddress)){
                    validation =  true;
                    validUtils.showToast(AddVendorActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etCity)){
                    validation =  true;
                    validUtils.showToast(AddVendorActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etState)){
                    validation =  true;
                    validUtils.showToast(AddVendorActivity.this, "Feilds are Empty");
                }

                if (!validUtils.validateEditTexts(etPincode)){
                    validation =  true;
                    validUtils.showToast(AddVendorActivity.this, "Feilds are Empty");
                }
                if ( validation == false){
                    addVendor();
                }

            }
        });
    }

    private void addVendor() {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.POST, ADD_VENDOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                               validUtils.hideProgressDialog();
                                validUtils.showToast(AddVendorActivity.this,jsonObject.getString("message"));
                                startActivity(new Intent(AddVendorActivity.this, VendorActivity.class));
                                Bungee.split(AddVendorActivity.this);

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(AddVendorActivity.this,jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(AddVendorActivity.this,"Something went Wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(AddVendorActivity.this,e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(AddVendorActivity.this,error.getMessage());
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

                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("company", company);
                params.put("mobile", phone);
                params.put("address", address);
                params.put("city", city);
                params.put("state", state);
                params.put("pincode", pincode);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AddVendorActivity.this);
        queue.add(request);
    }
}
