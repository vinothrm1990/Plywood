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
import com.app.plywood.adapter.ListCustomerAdapter;
import com.app.plywood.data.CustomerData;
import com.app.plywood.helper.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thebat.lib.validutil.ValidUtils;

public class ListCustomerActivity extends AppCompatActivity implements ListCustomerAdapter.ListCustomerAdapterListener {

    RecyclerView rvCustomer;
    List<CustomerData> customerList;
    String VIEW_CUSTOMER_URL = Constants.BASE_URL + Constants.VIEW_CUSTOMER;
    ListCustomerAdapter customerAdapter;
    SearchView searchView;
    RecyclerView.LayoutManager layoutManager;
    ValidUtils validUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("VIEW CUSTOMERS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        customerList = new ArrayList<>();
        rvCustomer = findViewById(R.id.rv_view_customer);
        layoutManager = new LinearLayoutManager(this);
        rvCustomer.setLayoutManager(layoutManager);

        viewCustomer();
    }

    private void viewCustomer() {

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
                                customerList.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    CustomerData customer = new CustomerData();
                                    customer.name = object.getString("c_name");
                                    customer.cname = object.getString("c_company");
                                    customer.mobile = object.getString("mobile");
                                    customerList.add(customer);
                                }
                                customerAdapter = new ListCustomerAdapter(ListCustomerActivity.this, customerList, ListCustomerActivity.this);
                                rvCustomer.setAdapter(customerAdapter);
                                customerAdapter.notifyDataSetChanged();

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("no data")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(ListCustomerActivity.this,jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(ListCustomerActivity.this,"Something went Wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(ListCustomerActivity.this,e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(ListCustomerActivity.this, error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(ListCustomerActivity.this);
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
                customerAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                customerAdapter.getFilter().filter(query);
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
    public void onCustomerSelected(CustomerData customer) {

    }
}
