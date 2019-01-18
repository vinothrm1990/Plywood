package com.app.plywood.activity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import spencerstudios.com.bungeelib.Bungee;
import thebat.lib.validutil.ValidUtils;

public class SalaryActivity extends AppCompatActivity {

    ArrayAdapter<String> finishAdapter;
    ValidUtils validUtils;
    ImageView ivDate;
    TextView tvDate;
    CustomEditText etVeenerFt, etVeenerPrice, etDyerFt, etDyerPrice, etPressFt, etPressPrice, etFinishPrice;
    Spinner spinFinish;
    Calendar calendar;
    String strThick;
    Button btnAdd;
    List<String> thickList;
    String SALARY_URL = Constants.BASE_URL + Constants.ADD_SALARY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("SALARY");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        etVeenerFt = findViewById(R.id.sal_et_veener_ft);
        etVeenerPrice = findViewById(R.id.sal_et_veener_price);
        etDyerFt = findViewById(R.id.sal_et_dyrer_ft);
        etDyerPrice = findViewById(R.id.sal_et_dyrer_price);
        etPressFt = findViewById(R.id.sal_et_press_ft);
        etPressPrice = findViewById(R.id.sal_et_press_price);
        etFinishPrice = findViewById(R.id.sal_et_finish_price);
        ivDate = findViewById(R.id.sal_iv_date);
        tvDate = findViewById(R.id.sal_tv_date);
        btnAdd = findViewById(R.id.sal_btn_add);
        spinFinish = findViewById(R.id.sale_spin_ft);

        Calendar ccalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String cDate = sdf.format(ccalendar.getTime());
        tvDate.setText(cDate);

        calendar =  Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDateFormat();
            }

        };

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new DatePickerDialog(SalaryActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String[] thick = new String[] {
                "18 mm",
                "12 mm",
                "9 mm",
                "6 mm"
        };

        thickList = new ArrayList<String>(Arrays.asList(thick));
        finishAdapter = new ArrayAdapter<>(SalaryActivity.this, R.layout.spinner_layout, thickList);
        finishAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinFinish.setAdapter(finishAdapter);
        spinFinish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strThick =  parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validUtils.validateEditTexts(etVeenerFt, etVeenerPrice, etDyerFt, etDyerPrice, etPressFt, etPressPrice, etFinishPrice)){
                    addSalary();
                }else {
                    validUtils.showToast(SalaryActivity.this, "Fill all the Details");
                }
    }

    private void addSalary() {

        validUtils.showProgressDialog(SalaryActivity.this, SalaryActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, SALARY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                                validUtils.hideProgressDialog();

                                validUtils.showToast(SalaryActivity.this, jsonObject.getString("message"));

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(SalaryActivity.this, jsonObject.getString("message"));
                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(SalaryActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(SalaryActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(SalaryActivity.this, error.getMessage());
                    }
                })
        {

            @Override
            protected Map<String, String> getParams()
            {
                String date = tvDate.getText().toString().trim();
                String vFeet = etVeenerFt.getText().toString().trim();
                String vPrice = etVeenerPrice.getText().toString().trim();
                String dFt = etDyerFt.getText().toString().trim();
                String dPrice = etDyerPrice.getText().toString().trim();
                String pFt = etPressFt.getText().toString().trim();
                String pPrice = etPressPrice.getText().toString().trim();
                String fPrice = etFinishPrice.getText().toString().trim();

                int total = Integer.parseInt(vPrice)+Integer.parseInt(dPrice)+
                        Integer.parseInt(pPrice)+Integer.parseInt(fPrice);

                Map<String, String>  params = new HashMap<String, String>();
                params.put("date", date);
                params.put("veener_ft", vFeet);
                params.put("veener_price", vPrice);
                params.put("dyer_ft", dFt);
                params.put("dyer_price", dPrice);
                params.put("press_ft", pFt);
                params.put("press_price", pPrice);
                params.put("finish_ft", strThick);
                params.put("finish_price", fPrice);
                params.put("total", String.valueOf(total));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(SalaryActivity.this);
        queue.add(request);
        }
        });
    }

    private void setDateFormat() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDate.setText(sdf.format(calendar.getTime()));
    }
}
