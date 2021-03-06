package com.app.plywood.activity;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.plywood.R;
import com.app.plywood.adapter.CustomerAdapter;
import com.app.plywood.data.CustomerMenu;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    RecyclerView rcCustomer;
    List<CustomerMenu> cusList;
    CustomerAdapter customerAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("CUSTOMERS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        cusList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(this, cusList);
        rcCustomer = findViewById(R.id.rv_customer);
        mLayoutManager = new GridLayoutManager(this, 2);
        rcCustomer.setLayoutManager(mLayoutManager);
        rcCustomer.setAdapter(customerAdapter);
        getMenu();
    }

    private void getMenu() {

        int [] icons = new int[]{

                R.drawable.ic_add_product,
                R.drawable.ic_list


        };
        CustomerMenu menu = new CustomerMenu("Add Customer", icons[0]);
        cusList.add(menu);
        menu = new CustomerMenu("View Customers", icons[1]);
        cusList.add(menu);

        customerAdapter.notifyDataSetChanged();
    }
}
