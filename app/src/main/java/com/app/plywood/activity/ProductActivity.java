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
import com.app.plywood.adapter.DashboardAdapter;
import com.app.plywood.adapter.ProductAdapter;
import com.app.plywood.data.DashboardMenu;
import com.app.plywood.data.ProductMenu;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    RecyclerView rvProduct;
    List<ProductMenu> proList;
    ProductAdapter proAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

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

        proList = new ArrayList<>();
        proAdapter = new ProductAdapter(ProductActivity.this, proList);
        rvProduct = findViewById(R.id.rv_product);
        mLayoutManager = new GridLayoutManager(ProductActivity.this, 2);
        rvProduct.setLayoutManager(mLayoutManager);
        rvProduct.setAdapter(proAdapter);
        getMenu();
    }

    private void getMenu() {

        int [] icons = new int[]{

                R.drawable.ic_add_product,
                R.drawable.ic_list


        };
        ProductMenu menu = new ProductMenu("Add Products", icons[0]);
        proList.add(menu);
        menu = new ProductMenu("List Products", icons[1]);
        proList.add(menu);

        proAdapter.notifyDataSetChanged();
    }
}
