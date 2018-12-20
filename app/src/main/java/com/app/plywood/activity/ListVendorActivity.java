package com.app.plywood.activity;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.plywood.R;
import com.app.plywood.adapter.ListVendorAdapter;
import com.app.plywood.data.VendorData;
import com.app.plywood.helper.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thebat.lib.validutil.ValidUtils;

public class ListVendorActivity extends AppCompatActivity implements ListVendorAdapter.ListVendorAdapterListener {

    RecyclerView rvCustomer;
    List<VendorData> vendorList;
    String VIEW_CUSTOMER_URL = Constants.BASE_URL + Constants.VIEW_VENDOR;
    public static AVLoadingIndicatorView progress;
    ListVendorAdapter vendorAdapter;
    SearchView searchView;
    ValidUtils validUtils;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vendor);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("VIEW VENDORS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        vendorList = new ArrayList<>();
        rvCustomer = findViewById(R.id.rv_view_vendor);
        layoutManager = new LinearLayoutManager(this);
        rvCustomer.setLayoutManager(layoutManager);
        viewVendor();
    }

    private void viewVendor() {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.GET, VIEW_CUSTOMER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                                validUtils.hideProgressDialog();

                                String data = jsonObject.getString("message");

                                JSONArray array = new JSONArray(data);
                                vendorList.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    VendorData vendor = new VendorData();
                                    vendor.name = object.getString("v_name");
                                    vendor.cname = object.getString("v_company");
                                    vendor.mobile = object.getString("mobile");
                                    vendorList.add(vendor);
                                }
                                vendorAdapter = new ListVendorAdapter(ListVendorActivity.this, vendorList, ListVendorActivity.this);
                                rvCustomer.setAdapter(vendorAdapter);
                                vendorAdapter.notifyDataSetChanged();

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("no data")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(ListVendorActivity.this,jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(ListVendorActivity.this,"Something went Wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(ListVendorActivity.this,e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(ListVendorActivity.this,error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(ListVendorActivity.this);
        queue.add(request);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vendorAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                vendorAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onVendorSelected(VendorData vendor) {

    }
}
