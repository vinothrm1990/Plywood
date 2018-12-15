package com.app.plywood.activity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thebat.lib.validutil.ValidUtils;


public class GenerateActivity extends AppCompatActivity {

    WebView mWebView;
    String name, invoice, date, subtotal;
    ValidUtils validUtils;
    FloatingActionButton fabPrint;
    String CUSTOMER_URL = Constants.BASE_URL + Constants.GET_CUSTOMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("VIEW INVOICE");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();
        mWebView = findViewById(R.id.webView);
        fabPrint = findViewById(R.id.fab_print);
        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){

            name = bundle.getString("customer");
            invoice = bundle.getString("invoice");
            date = bundle.getString("date");
            subtotal = bundle.getString("subtotal");
        }
        generateInvoice(name);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                //if page loaded successfully then show print button
                findViewById(R.id.fab_print).setVisibility(View.VISIBLE);
            }
        });

        fabPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createWebPrintJob(mWebView);
            }
        });
    }

    private void generateInvoice(final String name) {

        validUtils.showProgressDialog(GenerateActivity.this, GenerateActivity.this);
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

                                Constants.editor.putString("name", object.getString("c_name"));
                                Constants.editor.putString("address", object.getString("address"));
                                Constants.editor.putString("city", object.getString("city"));
                                Constants.editor.putString("state", object.getString("state"));
                                Constants.editor.putString("mobile", object.getString("mobile"));
                                Constants.editor.apply();
                                Constants.editor.commit();
                                doWebViewPrint();

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("no data")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(GenerateActivity.this, jsonObject.getString("message"));
                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(GenerateActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(GenerateActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(GenerateActivity.this, error.getMessage());
                    }
                })
        {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("company", name);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(GenerateActivity.this);
        queue.add(request);
    }


    private void doWebViewPrint() {

        String name = Constants.pref.getString("name", "");
        String address = Constants.pref.getString("address", "");
        String city = Constants.pref.getString("city", "");
        String state = Constants.pref.getString("state", "");
        String mobile = Constants.pref.getString("mobile", "");
        int gst = Integer.parseInt(subtotal) * 18/100;
        int total = Integer.parseInt(subtotal) + gst;
        String strGst = String.valueOf(gst);
        String strTotal = String.valueOf(total);
        mWebView.loadUrl("http://pandiyanadu.in/err/generate_invoice.php?invoice="+invoice+"&date="+date+"&tname="+name+"&taddress="+address+"&tcity="+city+"&tstate="+state+"&tmobile="+mobile+"&subtotal="+subtotal+"&gst="+strGst+"&grandtotal="+strTotal);

    }

    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = Constants.pref.getString("name", "") + invoice;

        //open print dialog
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }


}
