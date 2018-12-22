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
import com.app.plywood.adapter.PurchaseAdapter;
import com.app.plywood.data.PurchaseData;
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

public class PurchaseActivity extends AppCompatActivity {

    Button btnAddProduct;
    int id=0;
    String[] vsize, fvsize;
    ArrayAdapter<String> sizeAdapter;
    AlertDialog addDialog;
    CustomEditText etInvoice;
    TextView tvDate, tvGst, tvGrandTotal;
    ImageView ivDate, ivInfo, ivAdd;
    ValidUtils validUtils;
    List<String> vendorList, size_list, product_list;
    Spinner spinVendor;
    String strVendor, sDate, strSize, strProduct;
    Calendar calendar;
    PurchaseAdapter purchaseAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    public static int sumTotal=0;
    public static RecyclerView rvPurchase;
    public static TextView tvNoProduct, tvSubTotal;
    public static String invoice, thick, size, quantity;
    List<PurchaseData> addList;
    ArrayAdapter<String> vendorAdapter;
    String COMPANY_URL = Constants.BASE_URL + Constants.GET_VENDOR_COMPANY;
    String VENDOR_URL =  Constants.BASE_URL + Constants.GET_VENDOR;
    String ADD_PURCHASE_URL = Constants.BASE_URL + Constants.ADD_PURCHASE;
    String ADD_PURCHASE_TOTAL_URL = Constants.BASE_URL + Constants.ADD_PURCHASE_TOTAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("PURCHASE ORDER");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        etInvoice = findViewById(R.id.pur_et_invoice);
        tvDate = findViewById(R.id.pur_tv_date);
        tvSubTotal = findViewById(R.id.pur_tv_subtotal);
        tvGst = findViewById(R.id.pur_tv_gst);
        tvGrandTotal = findViewById(R.id.pur_tv_grandtotal);
        ivDate = findViewById(R.id.pur_iv_date);
        ivInfo = findViewById(R.id.pur_iv_info);
        ivAdd = findViewById(R.id.pur_iv_add);
        spinVendor = findViewById(R.id.pur_spin_vendor);
        tvNoProduct = findViewById(R.id.pur_tv_noproduct);
        rvPurchase = findViewById(R.id.rv_pur);
        btnAddProduct = findViewById(R.id.pur_btn_add);


        addList = new ArrayList<>();
        purchaseAdapter = new PurchaseAdapter(this, addList);
        rvPurchase = findViewById(R.id.rv_pur);
        mLayoutManager = new LinearLayoutManager(this);
        rvPurchase.setLayoutManager(mLayoutManager);
        rvPurchase.setAdapter(purchaseAdapter);


        vendorList = new ArrayList<>();
        getCompany();

        invoice = etInvoice.getText().toString().trim();

        vendorAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, vendorList);
        vendorAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinVendor.setAdapter(vendorAdapter);

        spinVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strVendor = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar ccalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String cDate = sdf.format(ccalendar.getTime());
        tvDate.setText(cDate);
        sDate = tvDate.getText().toString().trim();

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


                new DatePickerDialog(PurchaseActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validUtils.showProgressDialog(PurchaseActivity.this, PurchaseActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, VENDOR_URL,
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

                                        Constants.editor.putString("name", object.getString("v_name"));
                                        Constants.editor.putString("address", object.getString("address"));
                                        Constants.editor.putString("city", object.getString("city"));
                                        Constants.editor.putString("state", object.getString("state"));
                                        Constants.editor.putString("mobile", object.getString("mobile"));
                                        Constants.editor.apply();
                                        Constants.editor.commit();

                                        Intent intent = new Intent(PurchaseActivity.this, VendorDetailActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("id", object.getString("id"));
                                        bundle.putString("name", object.getString("v_name"));
                                        bundle.putString("address", object.getString("address"));
                                        bundle.putString("city", object.getString("city"));
                                        bundle.putString("state", object.getString("state"));
                                        bundle.putString("mobile", object.getString("mobile"));
                                        bundle.putString("company", object.getString("v_company"));
                                        bundle.putString("pincode", object.getString("pincode"));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        Bungee.split(PurchaseActivity.this);

                                    }else if (jsonObject.getString("status")
                                            .equalsIgnoreCase("no data")){
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(PurchaseActivity.this, jsonObject.getString("message"));
                                    }else {
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(PurchaseActivity.this, "Something went wrong");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    validUtils.hideProgressDialog();
                                    validUtils.showToast(PurchaseActivity.this, e.getMessage());
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(PurchaseActivity.this, error.getMessage());
                            }
                        })
                {

                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("company", strVendor);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(PurchaseActivity.this);
                queue.add(request);
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PurchaseActivity.this);
                LayoutInflater inflater = PurchaseActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_purchase_dialog, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);

                Button btnAdd = dialogView.findViewById(R.id.add_pro_btn);
                ImageView ivClose = dialogView.findViewById(R.id.add_pro_close);
                final Spinner spProduct = dialogView.findViewById(R.id.add_pro_thick);
                final Spinner spSize = dialogView.findViewById(R.id.add_pro_size);
                final CustomEditText etPrice = dialogView.findViewById(R.id.add_pro_price);
                final CustomEditText etQuantity = dialogView.findViewById(R.id.add_pro_quantity);
                final CustomEditText etTon = dialogView.findViewById(R.id.add_pro_ton);
                final CustomEditText etLitre = dialogView.findViewById(R.id.add_pro_litre);

                addDialog = dialogBuilder.create();

                String[] product = new String[] {
                        "Veener",
                        "Firewood",
                        "Glue",
                        "Face Veener",
                        "Diesel"
                };
                product_list = new ArrayList<String>(Arrays.asList(product));
                ArrayAdapter<String> productAdapter = new ArrayAdapter<String>
                        (PurchaseActivity.this, R.layout.spinner_layout, product_list);
                productAdapter.setDropDownViewResource(R.layout.spinner_layout);
                spProduct.setAdapter(productAdapter);
                spProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        strProduct = adapterView.getItemAtPosition(pos).toString();

                        if (strProduct.equalsIgnoreCase("Veener")){
                            vsize = new String[]{
                                    "4In x 3In",
                                    "3In x 3In",
                                    "3In x 2In",
                                    "3In x 1In",
                                    "2In x 3In",
                                    "2In x 2In",
                                    "2In x 1In"
                            };
                            size_list = new ArrayList<String>(Arrays.asList(vsize));
                            sizeAdapter = new ArrayAdapter<String>
                                    (PurchaseActivity.this, R.layout.spinner_layout, size_list);
                            sizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
                            spSize.setAdapter(sizeAdapter);
                            spSize.setVisibility(View.VISIBLE);
                            etQuantity.setVisibility(View.VISIBLE);
                            etLitre.setVisibility(View.GONE);
                            etTon.setVisibility(View.GONE);

                        }else if (strProduct.equalsIgnoreCase("Firewood")){
                            spSize.setVisibility(View.GONE);
                            etQuantity.setVisibility(View.GONE);
                            etLitre.setVisibility(View.GONE);
                            etTon.setVisibility(View.VISIBLE);

                        }else if (strProduct.equalsIgnoreCase("Glue")){
                            spSize.setVisibility(View.GONE);
                            etQuantity.setVisibility(View.GONE);
                            etLitre.setVisibility(View.GONE);
                            etTon.setVisibility(View.VISIBLE);

                        }else if (strProduct.equalsIgnoreCase("Face Veener")){
                            fvsize = new String[]{
                                    "8Ft x 4Ft",
                                    "8Ft x 3Ft",
                                    "7Ft x 4Ft",
                                    "7Ft x 3Ft",
                                    "6Ft x 4Ft",
                                    "6Ft x 3Ft"
                            };
                            size_list = new ArrayList<String>(Arrays.asList(fvsize));
                            sizeAdapter = new ArrayAdapter<String>
                                    (PurchaseActivity.this, R.layout.spinner_layout, size_list);
                            sizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
                            spSize.setAdapter(sizeAdapter);
                            spSize.setVisibility(View.VISIBLE);
                            etQuantity.setVisibility(View.VISIBLE);
                            etLitre.setVisibility(View.GONE);
                            etTon.setVisibility(View.GONE);

                        }else if (strProduct.equalsIgnoreCase("Diesel")){
                            spSize.setVisibility(View.GONE);
                            etQuantity.setVisibility(View.GONE);
                            etLitre.setVisibility(View.VISIBLE);
                            etTon.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

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

                        if (validUtils.validateEditTexts(etPrice, etQuantity) || validUtils.validateEditTexts(etPrice, etTon) || validUtils.validateEditTexts(etPrice, etLitre)){
                            String price = etPrice.getText().toString().trim();
                            String quantity = etQuantity.getText().toString().trim();
                            String ton = etTon.getText().toString().trim();
                            String litre = etLitre.getText().toString().trim();

                            if (strProduct.equalsIgnoreCase("Face Veener")){

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

                                id++;
                                add(id, strProduct, strSize, total, quantity, price);

                            }else if (strProduct.equalsIgnoreCase("Veener")){
                                int total = 0;
                                if (strSize.equalsIgnoreCase("4In x 3In")){
                                    total = (Integer.parseInt(price) * 12) * Integer.parseInt(quantity);
                                }
                                else if (strSize.equalsIgnoreCase("3In x 3In")){
                                    total = (Integer.parseInt(price) * 9) * Integer.parseInt(quantity);
                                }
                                else if (strSize.equalsIgnoreCase("3In x 2In")){
                                    total = (Integer.parseInt(price) * 6) * Integer.parseInt(quantity);
                                }
                                else if (strSize.equalsIgnoreCase("3In x 1In")){
                                    total = (Integer.parseInt(price) * 3) * Integer.parseInt(quantity);
                                }
                                else if (strSize.equalsIgnoreCase("2In x 3In")){
                                    total = (Integer.parseInt(price) * 6) * Integer.parseInt(quantity);
                                }
                                else if (strSize.equalsIgnoreCase("2In x 2In")){
                                    total = (Integer.parseInt(price) * 4) * Integer.parseInt(quantity);
                                }
                                else if (strSize.equalsIgnoreCase("2In x 1In")){
                                    total = (Integer.parseInt(price) * 2) * Integer.parseInt(quantity);
                                }

                                id++;
                                add(id, strProduct, strSize, total, quantity, price);

                            }else if (strProduct.equalsIgnoreCase("Firewood")){
                                int total = Integer.parseInt(price) * Integer.parseInt(ton);
                                String size = "NA";
                                id++;
                                add(id, strProduct, size, total, ton, price);

                            }else if (strProduct.equalsIgnoreCase("Glue")){
                                int total = Integer.parseInt(price) * Integer.parseInt(ton);
                                String size = "NA";
                                id++;
                                add(id, strProduct, size, total, ton, price);

                            }else if (strProduct.equalsIgnoreCase("Diesel")){
                                int total = Integer.parseInt(price) * Integer.parseInt(litre);
                                String size = "NA";
                                id++;
                                add(id, strProduct, size, total, litre, price);

                            }

                        }else {
                            validUtils.showToast(PurchaseActivity.this, "Feilds are Empty");
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

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validUtils.showProgressDialog(PurchaseActivity.this, PurchaseActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, ADD_PURCHASE_TOTAL_URL,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);

                                    if (jsonObject.getString("status")
                                            .equalsIgnoreCase("success")){
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(PurchaseActivity.this, jsonObject.getString("message"));

                                    }else if (jsonObject.getString("status")
                                            .equalsIgnoreCase("failed")){
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(PurchaseActivity.this, jsonObject.getString("message"));

                                    }else {
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(PurchaseActivity.this, "Something went wrong");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    validUtils.hideProgressDialog();
                                    validUtils.showToast(PurchaseActivity.this, e.getMessage());
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(PurchaseActivity.this, error.getMessage());
                            }
                        })

                {

                    @Override
                    protected Map<String, String> getParams() {
                        String invoice = etInvoice.getText().toString().trim();
                        String date = tvDate.getText().toString().trim();
                        int gst = (sumTotal) * 18/100;
                        int total = (sumTotal) + gst;
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("date", date);
                        params.put("invoice", invoice);
                        params.put("gst", String.valueOf(gst));
                        params.put("grandtotal", String.valueOf(total));
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(PurchaseActivity.this);
                queue.add(request);

            }
        });

    }

    private void add(int id, String thick, String size, int price, String quantity, String uprice) {

        PurchaseData product = new PurchaseData(id, thick, size, price, quantity);
        addList.add(product);
        purchaseAdapter.notifyDataSetChanged();

        sumTotal = purchaseAdapter.grandTotal(addList);
        tvSubTotal.setText(String.valueOf(sumTotal));

        int gst = (sumTotal) * 18/100;
        int total = (sumTotal) + gst;

        tvGst.setText(String.valueOf(gst));
        tvGrandTotal.setText(String.valueOf(total));

        if (addList.size() > 0){
            rvPurchase.setVisibility(View.VISIBLE);
            tvNoProduct.setVisibility(View.GONE);
        }

        addPurchase(id, thick, uprice, size, quantity, String.valueOf(price));
    }

    private void addPurchase(final int id, final String product, final String uprice, final String size, final String quantity, final String total) {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.POST, ADD_PURCHASE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(PurchaseActivity.this, jsonObject.getString("message"));

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(PurchaseActivity.this, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(PurchaseActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(PurchaseActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(PurchaseActivity.this, error.getMessage());
                    }
                })

        {

            @Override
            protected Map<String, String> getParams() {
                String invoice = etInvoice.getText().toString().trim();
                String date = tvDate.getText().toString().trim();
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));
                params.put("date", date);
                params.put("invoice", invoice);
                params.put("vendor", strVendor);
                params.put("product", product);
                params.put("price", uprice);
                params.put("size", size);
                params.put("quantity", quantity);
                params.put("subtotal", total);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(PurchaseActivity.this);
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
                                String data = jsonObject.getString("message");
                                validUtils.hideProgressDialog();

                                JSONArray array = new JSONArray(data);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    String company = object.getString("v_company");
                                    vendorList.add(company);

                                }
                                vendorAdapter.notifyDataSetChanged();

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("no data")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(PurchaseActivity.this, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(PurchaseActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(PurchaseActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(PurchaseActivity.this, error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(PurchaseActivity.this);
        queue.add(request);
    }

    private void setDateFormat() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDate.setText(sdf.format(calendar.getTime()));
        sDate = tvDate.getText().toString().trim();
    }
}
