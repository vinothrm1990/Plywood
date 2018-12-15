package com.app.plywood.activity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
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

import thebat.lib.validutil.ValidUtils;

public class AddProductActivity extends AppCompatActivity {

    Spinner spThick, spSize;
    String strThick, strSize, sdate;
    ImageView ivDate;
    Calendar calendar;
    TextView tvDate;
    ArrayAdapter<String> thickAdapter, sizeAdapter;
    List<String> size_list, thick_list;
    ValidUtils validUtils;
    Button btnAdd;
    CustomEditText etQuantity;
    String ADD_PRODUCT_URL = Constants.BASE_URL + Constants.ADD_PRODUCT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("ADD PRODUCTS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        spSize = findViewById(R.id.pro_spin_size);
        spThick = findViewById(R.id.pro_spin_thick);
        ivDate = findViewById(R.id.pro_iv_date);
        etQuantity = findViewById(R.id.pro_et_qty);
        btnAdd = findViewById(R.id.pro_btn_add);
        tvDate = findViewById(R.id.pro_tv_date);

        validUtils = new ValidUtils();

        Calendar ccalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String cDate = sdf.format(ccalendar.getTime());
        tvDate.setText(cDate);
        sdate = tvDate.getText().toString().trim();

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


                new DatePickerDialog(AddProductActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String[] thick = new String[] {
                "18 mm",
                "15 mm",
                "12 mm",
                "9 mm",
                "6 mm"
        };

        thick_list = new ArrayList<String>(Arrays.asList(thick));
        thickAdapter = new ArrayAdapter<String>
                (AddProductActivity.this, R.layout.spinner_layout, thick_list);
        thickAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spThick.setAdapter(thickAdapter);

        spThick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                strThick = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] size = new String[] {
                "8Ft x 4Ft",
                "8Ft x 3Ft",
                "7Ft x 4Ft",
                "7Ft x 3Ft",
                "6Ft x 4Ft",
                "6Ft x 3Ft"
        };

        size_list = new ArrayList<String>(Arrays.asList(size));
        sizeAdapter = new ArrayAdapter<String>
                (AddProductActivity.this, R.layout.spinner_layout, size_list);
        sizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spSize.setAdapter(sizeAdapter);
        spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                strSize = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validUtils.validateEditTexts(etQuantity)){

                    String quantity = etQuantity.getText().toString().trim();
                    addProduct(strThick, strSize, quantity);

                }else {
                    validUtils.showToast(AddProductActivity.this, "Feilds are Empty");
                }
            }
        });
    }

    private void addProduct(final String strThick, final String strSize, final String quantity) {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.POST, ADD_PRODUCT_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){

                                validUtils.hideProgressDialog();
                                validUtils.showToast(AddProductActivity.this, jsonObject.getString("message"));

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(AddProductActivity.this, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(AddProductActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(AddProductActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(AddProductActivity.this, error.getMessage());
                    }
                })

        {

            @Override
            protected Map<String, String> getParams() {
                String date = tvDate.getText().toString().trim();
                Map<String, String> params = new HashMap<String, String>();
                params.put("date", date);
                params.put("thickness", strThick);
                params.put("size", strSize);
                params.put("quantity", quantity);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AddProductActivity.this);
        queue.add(request);
    }

    private void setDateFormat() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDate.setText(sdf.format(calendar.getTime()));
        sdate = tvDate.getText().toString().trim();
    }
}
