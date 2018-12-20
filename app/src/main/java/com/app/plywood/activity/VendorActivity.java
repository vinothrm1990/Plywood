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
import com.app.plywood.adapter.VendorAdapter;
import com.app.plywood.data.VendorMenu;

import java.util.ArrayList;
import java.util.List;

public class VendorActivity extends AppCompatActivity {

    RecyclerView rvVendor;
    List<VendorMenu> venList;
    VendorAdapter vendorAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("VENDORS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        venList = new ArrayList<>();
        vendorAdapter = new VendorAdapter(this, venList);
        rvVendor = findViewById(R.id.rv_vendor);
        mLayoutManager = new GridLayoutManager(this, 2);
        rvVendor.setLayoutManager(mLayoutManager);
        rvVendor.setAdapter(vendorAdapter);
        getMenu();
    }

    private void getMenu() {

        int [] icons = new int[]{

                R.drawable.ic_add_product,
                R.drawable.ic_list


        };
        VendorMenu menu = new VendorMenu("Add Vendor", icons[0]);
        venList.add(menu);
        menu = new VendorMenu("View Vendors", icons[1]);
        venList.add(menu);

        vendorAdapter.notifyDataSetChanged();
    }
}
