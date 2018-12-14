package com.app.plywood.activity;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    String name, invoice, date, thick, size, price, quantity, subtotal, html;
    ValidUtils validUtils;
    HashMap<String,String> map, prodata;
    ArrayList<HashMap<String,String>> proList = new ArrayList<>();
    FloatingActionButton fabPrint;
    String CUSTOMER_URL = Constants.BASE_URL + Constants.GET_CUSTOMER;
    String INVOICE_URL = Constants.BASE_URL + Constants.GENERATE_INVOICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        validUtils = new ValidUtils();
        mWebView = findViewById(R.id.webView);
        fabPrint = findViewById(R.id.fab_print);
        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){

            name = bundle.getString("customer");
            invoice = bundle.getString("invoice");
            date = bundle.getString("date");
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
                                getProducts(invoice);

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

    private void getProducts(final String invoice) {

        validUtils.showProgressDialog(GenerateActivity.this, GenerateActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, INVOICE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            doWebViewPrint();
                            /*if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                                validUtils.hideProgressDialog();

                                String data = jsonObject.getString("data");
                                JSONArray array = new JSONArray(data);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    map = new HashMap<>();

                                    thick = object.getString("thickness");
                                    size = object.getString("size");
                                    price = object.getString("price");
                                    quantity = object.getString("quantity");
                                    subtotal = object.getString("subtotal");

                                    map.put("thick", thick);
                                    map.put("size", size);
                                    map.put("price", price);
                                    map.put("quantity", quantity);
                                    map.put("subtotal", subtotal);

                                    proList.add(map);
                                }

                                doWebViewPrint();

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("no data")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(GenerateActivity.this, jsonObject.getString("message"));
                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(GenerateActivity.this, "Something went wrong");
                            }*/

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
                params.put("invoice", invoice);
                params.put("date", date);
                params.put("tname", Constants.pref.getString("name", ""));
                params.put("taddress", Constants.pref.getString("address", ""));
                params.put("tcity",  Constants.pref.getString("city", ""));
                params.put("tstate", Constants.pref.getString("state", ""));
                params.put("tmobile", Constants.pref.getString("mobile", ""));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(GenerateActivity.this);
        queue.add(request);
    }

    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        /*html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\" >\n" +
                "\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>Invoice Bill</title>\n" +
                "  \n" +
                "  \n" +
                "  <link rel='stylesheet' href='http://pandiyanadu.in/err/bootstrap.css'>\n" +
                "\n" +
                "  \n" +
                "  \n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "  <div class=\"container\">\n" +
                "  <div class=\"card\">\n" +
                "<div class=\"card-header\">\n" +
                "Invoice No :\n" +
                "<strong>invoice</strong> \n" +
                "  <span class=\"float-right\">Date :<strong>date</strong></span>\n" +
                "\n" +
                "</div>\n" +
                "<div class=\"card-body\">\n" +
                "<div class=\"row mb-4\">\n" +
                "<div class=\"col-sm-6\">\n" +
                "<h6 class=\"mb-3\">Bill From:</h6>\n" +
                "<div>\n" +
                "<strong>Vinoth</strong>\n" +
                "</div>\n" +
                "<div>Anna Nagar</div>\n" +
                "<div>Madurai</div>\n" +
                "<div>Tamilnadu</div>\n" +
                "<div>Phone: 1234567890</div>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"col-sm-6\">\n" +
                "<h6 class=\"mb-3\">Bill To :</h6>\n" +
                "<div>\n" +
                "<strong>Constants.pref.getString(\"name\", \"\")</strong>\n" +
                "</div>\n" +
                "<div>Constants.pref.getString(\"address\", \"\")</div>\n" +
                "<div>Constants.pref.getString(\"city\", \"\")</div>\n" +
                "<div>Constants.pref.getString(\"state\", \"\")</div>\n" +
                "<div>Phone: Constants.pref.getString(\"mobile\", \"\")</div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"table-responsive-sm\">\n" +
                "<table class=\"table table-striped\">\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th class=\"center\">#</th>\n" +
                "<th>Item</th>\n" +
                "<th>Description</th>\n" +
                "\n" +
                "<th class=\"right\">Price</th>\n" +
                "  <th class=\"center\">Qty</th>\n" +
                "<th class=\"right\">Total</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "\n" +
                "<% \n" +
                "for(int i=0;i<proList.size();i++){\n" +
                "proData = proList.get(i);\n" +
                "%>\n" +
                "<tr>\n" +
                "<td class=\"center\">1</td>\n" +
                "<td class=\"left strong\">"+prodata.get(thick)+"</td>\n" +
                "<td class=\"left\"> prodata.get(\"size\")</td>\n" +
                "\n" +
                "<td class=\"right\">prodata.get(\"price\")</td>\n" +
                "  <td class=\"center\">prodata.get(\"quantity\")</td>\n" +
                "<td class=\"right\">prodata.get(\"subtotal\")</td>\n" +
                "</tr>\n" +
                "<%\n" +
                "}\n" +
                "%>\n" +
                "\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</div>\n" +
                "<div class=\"row\">\n" +
                "<div class=\"col-lg-4 col-sm-5\">\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"col-lg-4 col-sm-5 ml-auto\">\n" +
                "<table class=\"table table-clear\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"left\">\n" +
                "<strong>Subtotal</strong>\n" +
                "</td>\n" +
                "<td class=\"right\">$8.497,00</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td class=\"left\">\n" +
                "<strong>Discount (20%)</strong>\n" +
                "</td>\n" +
                "<td class=\"right\">$1,699,40</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td class=\"left\">\n" +
                " <strong>GST (18%)</strong>\n" +
                "</td>\n" +
                "<td class=\"right\">$679,76</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td class=\"left\">\n" +
                "<strong>Total</strong>\n" +
                "</td>\n" +
                "<td class=\"right\">\n" +
                "<strong>$7.477,36</strong>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "  \n" +
                "  \n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";

        mWebView.loadData(html, "text/HTML", "UTF-8");*/

        mWebView.loadUrl("http://pandiyanadu.in/err/generate_invoice.php");


    }

    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = "Invoice" + invoice;

        //open print dialog
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }


}
