package com.app.plywood.activity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.plywood.R;
import com.app.plywood.adapter.SalaryReportAdapter;
import com.app.plywood.helper.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import thebat.lib.validutil.ValidUtils;

public class SalaryReportActivity extends AppCompatActivity {

    RecyclerView rvSalary;
    TextView tvEmpty;
    ArrayList<HashMap<String,String>> reportList;
    HashMap<String, String> map;
    SalaryReportAdapter reportAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ImageView ivDate;
    TextView tvDate;
    Calendar calendar;
    ValidUtils validUtils;
    String REPORT_URL = Constants.BASE_URL + Constants.GET_SALARY_REPORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_report);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("SALARY REPORT");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        tvDate = findViewById(R.id.sal_rep_tv_date);
        tvEmpty = findViewById(R.id.sal_rep_tv_empty);
        ivDate = findViewById(R.id.sal_rep_iv_date);

        Calendar ccalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String cDate = sdf.format(ccalendar.getTime());
        tvDate.setText(cDate);

        reportList = new ArrayList<>();
        rvSalary = findViewById(R.id.rv_sal_rep);
        mLayoutManager = new LinearLayoutManager(this);
        rvSalary.setLayoutManager(mLayoutManager);
        getReport();

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


                new DatePickerDialog(SalaryReportActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void getReport() {

        validUtils.showProgressDialog(SalaryReportActivity.this, SalaryReportActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, REPORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                                validUtils.hideProgressDialog();
                                rvSalary.setVisibility(View.VISIBLE);
                                tvEmpty.setVisibility(View.GONE);
                                String data = jsonObject.getString("data");
                                validUtils.hideProgressDialog();

                                JSONArray array = new JSONArray(data);
                                reportList.clear();
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    map = new HashMap<String, String>();

                                    String id = object.getString("id");
                                    String v_ft = object.getString("veener_ft");
                                    String v_price = object.getString("veener_price");
                                    String d_ft = object.getString("dyer_ft");
                                    String d_price = object.getString("dyer_price");
                                    String p_ft = object.getString("press_ft");
                                    String p_price = object.getString("press_price");
                                    String f_ft = object.getString("finish_ft");
                                    String f_price = object.getString("finish_price");
                                    String total = object.getString("total");

                                    map.put("id", id);
                                    map.put("veener_ft", v_ft);
                                    map.put("veener_price", v_price);
                                    map.put("dyer_ft", d_ft);
                                    map.put("dyer_price", d_price);
                                    map.put("press_ft", p_ft);
                                    map.put("press_price", p_price);
                                    map.put("finish_ft", f_ft);
                                    map.put("finish_price", f_price);
                                    map.put("total", total);

                                    reportList.add(map);

                                }

                                reportAdapter = new SalaryReportAdapter(SalaryReportActivity.this, reportList);
                                rvSalary.setAdapter(reportAdapter);

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("no data")){
                                validUtils.hideProgressDialog();
                                rvSalary.setVisibility(View.GONE);
                                tvEmpty.setVisibility(View.VISIBLE);
                                validUtils.showToast(SalaryReportActivity.this, jsonObject.getString("message"));
                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(SalaryReportActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(SalaryReportActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(SalaryReportActivity.this, error.getMessage());
                    }
                })
        {

            @Override
            protected Map<String, String> getParams()
            {
                String date = tvDate.getText().toString().trim();

                Map<String, String>  params = new HashMap<String, String>();
                params.put("date", date);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(SalaryReportActivity.this);
        queue.add(request);
    }

    private void setDateFormat() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDate.setText(sdf.format(calendar.getTime()));
        getReport();
    }
}
