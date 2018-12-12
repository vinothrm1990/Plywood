package com.app.plywood.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.app.plywood.adapter.AddAdapter;
import com.app.plywood.data.AddProduct;
import com.app.plywood.data.DashboardMenu;
import com.app.plywood.helper.Constants;
import com.libizo.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import spencerstudios.com.bungeelib.Bungee;
import thebat.lib.validutil.ValidUtils;

public class SaleActivity extends AppCompatActivity {

    Button btnGenerate;
    public static int sumTotal=0;
    Spinner spinCustomer;
    String spCustomer, strThick, strSize;
    TextView tvInvoice, tvDate;
    ImageView ivInfo, ivDate, ivAdd;
    Calendar calendar;
    List<String> customerList, size_list, thick_list;
    public static List<String> get_data;
    ArrayAdapter<String> customerAdapter;
    ValidUtils validUtils;
    public static RecyclerView rvSale;
    public static TextView tvNoProduct, tvTotal;
    List<AddProduct> addList;
    AddAdapter addAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    AlertDialog addDialog;
    public static String invoice, thick;
    String COMPANY_URL = Constants.BASE_URL + Constants.GET_COMPANY;
    String CUSTOMER_URL = Constants.BASE_URL + Constants.GET_CUSTOMER;
    String ADD_INVOICE_URL = Constants.BASE_URL + Constants.ADD_INVOICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("SALES ORDER");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        spinCustomer = findViewById(R.id.sale_spin_customer);
        tvInvoice = findViewById(R.id.sale_tv_invoice);
        tvDate = findViewById(R.id.sale_tv_date);
        ivInfo = findViewById(R.id.sale_iv_edit);
        ivDate = findViewById(R.id.sales_iv_date);
        ivAdd = findViewById(R.id.sales_iv_add);
        tvTotal = findViewById(R.id.sale_tv_total);
        tvNoProduct = findViewById(R.id.sale_tv_noproduct);

        validUtils = new ValidUtils();
        customerList = new ArrayList<>();

        addList = new ArrayList<>();
        addAdapter = new AddAdapter(this, addList);
        rvSale = findViewById(R.id.rv_sale);
        mLayoutManager = new LinearLayoutManager(this);
        rvSale.setLayoutManager(mLayoutManager);
        rvSale.setAdapter(addAdapter);

        invoiceNo();
        getCompany();

        invoice = tvInvoice.getText().toString().trim();

        customerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, customerList);
        customerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinCustomer.setAdapter(customerAdapter);

        spinCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                spCustomer = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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


                new DatePickerDialog(SaleActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validUtils.showProgressDialog(SaleActivity.this, SaleActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, CUSTOMER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);

                                    if (jsonObject.getString("status")
                                            .equalsIgnoreCase("success")){
                                        validUtils.hideProgressDialog();

                                        String data = jsonObject.getString("data");
                                        JSONArray array = new JSONArray(data);
                                        JSONObject object = array.getJSONObject(0);

                                        Intent intent = new Intent(SaleActivity.this, CustomerDetailActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("id", object.getString("c_id"));
                                        bundle.putString("name", object.getString("c_name"));
                                        bundle.putString("address", object.getString("address"));
                                        bundle.putString("city", object.getString("city"));
                                        bundle.putString("state", object.getString("state"));
                                        bundle.putString("mobile", object.getString("mobile"));
                                        bundle.putString("company", object.getString("c_company"));
                                        bundle.putString("pincode", object.getString("pincode"));
                                        bundle.putString("baddress", object.getString("b_address"));
                                        bundle.putString("bcity", object.getString("b_city"));
                                        bundle.putString("bstate", object.getString("b_state"));
                                        bundle.putString("bpincode", object.getString("b_pincode"));
                                        bundle.putString("saddress", object.getString("s_address"));
                                        bundle.putString("scity", object.getString("s_city"));
                                        bundle.putString("sstate", object.getString("s_state"));
                                        bundle.putString("spincode", object.getString("s_pincode"));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        Bungee.split(SaleActivity.this);

                                    }else if (jsonObject.getString("status")
                                            .equalsIgnoreCase("no data")){
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(SaleActivity.this, jsonObject.getString("message"));
                                    }else {
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(SaleActivity.this, "Something went wrong");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    validUtils.hideProgressDialog();
                                    validUtils.showToast(SaleActivity.this, e.getMessage());
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(SaleActivity.this, error.getMessage());
                            }
                        })
                {

                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("company", spCustomer);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(SaleActivity.this);
                queue.add(request);
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleActivity.this);
                LayoutInflater inflater = SaleActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_dialog, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);

                Button btnAdd = dialogView.findViewById(R.id.add_pro_btn);
                ImageView ivClose = dialogView.findViewById(R.id.add_pro_close);
                final Spinner spThick = dialogView.findViewById(R.id.add_pro_thick);
                Spinner spSize = dialogView.findViewById(R.id.add_pro_size);
                final CustomEditText etPrice = dialogView.findViewById(R.id.add_pro_price);
                final CustomEditText etQuantity = dialogView.findViewById(R.id.add_pro_quantity);

                addDialog = dialogBuilder.create();

                String[] thick = new String[] {
                        "18 mm",
                        "15 mm",
                        "12 mm",
                        "9 mm",
                        "6 mm"
                };
                thick_list = new ArrayList<String>(Arrays.asList(thick));
                ArrayAdapter<String> thickAdapter = new ArrayAdapter<String>
                        (SaleActivity.this, R.layout.spinner_layout, thick_list);
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
                ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>
                        (SaleActivity.this, R.layout.spinner_layout, size_list);
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

                        if (validUtils.validateEditTexts(etPrice, etQuantity)){
                            String price = etPrice.getText().toString().trim();
                            String quantity = etQuantity.getText().toString().trim();
                            int total = 0;
                            if (strSize.equalsIgnoreCase("8Ft x 4Ft")){
                                total = (Integer.parseInt(price) * 32) * Integer.parseInt(quantity);
                            }
                            else if (strSize.equalsIgnoreCase("8Ft x 3Ft")){
                                total = (Integer.parseInt(price) * 24) * Integer.parseInt(quantity);
                            }
                            else if (strSize.equalsIgnoreCase("7Ft x 4Ft")){
                                total = (Integer.parseInt(price) * 28) * Integer.parseInt(quantity);
                            }
                            else if (strSize.equalsIgnoreCase("7Ft x 3Ft")){
                                total = (Integer.parseInt(price) * 21) * Integer.parseInt(quantity);
                            }
                            else if (strSize.equalsIgnoreCase("6Ft x 4Ft")){
                                total = (Integer.parseInt(price) * 24) * Integer.parseInt(quantity);
                            }
                            else if (strSize.equalsIgnoreCase("6Ft x 3Ft")){
                                total = (Integer.parseInt(price) * 18) * Integer.parseInt(quantity);
                            }

                            add(strThick, strSize, total, quantity, price);
                        }else {
                            validUtils.showToast(SaleActivity.this, "Feilds are Empty");
                        }
                    }
                });
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        addDialog.dismiss();
                    }
                });
                addDialog.show();

            }
        });

    }

    private void add(String thick, String size, int price, String quantity, String uprice) {

        AddProduct product = new AddProduct(thick, size, price, quantity);
        addList.add(product);
        addAdapter.notifyDataSetChanged();

        sumTotal = addAdapter.grandTotal(addList);
        validUtils.showToast(SaleActivity.this, String.valueOf(sumTotal));
        tvTotal.setText(String.valueOf(sumTotal));
        
        if (addList.size() > 0){
            rvSale.setVisibility(View.VISIBLE);
            tvNoProduct.setVisibility(View.GONE);
        }

        addBill(thick, uprice, size, quantity, String.valueOf(price));
    }

    private void addBill(final String thick, final String price, final String size, final String quantity, final String total) {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.POST, ADD_INVOICE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){

                                validUtils.hideProgressDialog();
                                validUtils.showToast(SaleActivity.this, jsonObject.getString("message"));

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(SaleActivity.this, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(SaleActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(SaleActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(SaleActivity.this, error.getMessage());
                    }
                })

        {

            @Override
            protected Map<String, String> getParams() {
                String date = tvDate.getText().toString().trim();
                String invoice = tvInvoice.getText().toString().trim();

                Map<String, String> params = new HashMap<String, String>();
                params.put("date", date);
                params.put("invoice", invoice);
                params.put("customer", spCustomer);
                params.put("thickness", thick);
                params.put("price", price);
                params.put("size", size);
                params.put("quantity", quantity);
                params.put("subtotal", total);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(SaleActivity.this);
        queue.add(request);
    }

    private void getCompany() {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.GET, COMPANY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                                String data = jsonObject.getString("data");
                                validUtils.hideProgressDialog();

                                JSONArray array = new JSONArray(data);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    String company = object.getString("c_company");
                                    customerList.add(company);

                                }
                                customerAdapter.notifyDataSetChanged();

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("no data")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(SaleActivity.this, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(SaleActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(SaleActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(SaleActivity.this, error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(SaleActivity.this);
        queue.add(request);
    }

    private void invoiceNo() {

        Random random = new Random();
        int no = random.nextInt(10000)+1;
        String value = String.valueOf(no);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String cdate = simpleDateFormat.format(date);

        Random ran = new Random();
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i=0; i<1; i++){
            char noGen = alpha.charAt(ran.nextInt(alpha.length()));
            Object object = noGen;
            String number = object.toString();
            tvInvoice.setText(value+number+cdate);
        }
    }

    private void setDateFormat() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDate.setText(sdf.format(calendar.getTime()));
    }
}
