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
import com.app.plywood.adapter.ProductAdapter;
import com.app.plywood.adapter.ReportAdapter;
import com.app.plywood.data.ProductMenu;
import com.app.plywood.data.ReportMenu;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    RecyclerView rvReport;
    List<ReportMenu> repList;
    ReportAdapter repAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("REPORT LISTS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        repList = new ArrayList<>();
        repAdapter = new ReportAdapter(ReportActivity.this, repList);
        rvReport = findViewById(R.id.rv_report);
        mLayoutManager = new GridLayoutManager(ReportActivity.this, 2);
        rvReport.setLayoutManager(mLayoutManager);
        rvReport.setAdapter(repAdapter);
        getMenu();
    }

    private void getMenu() {

        int [] icons = new int[]{

                R.drawable.ic_reports,
                R.drawable.ic_reports,
                R.drawable.ic_reports,
                R.drawable.ic_reports


        };
        ReportMenu menu = new ReportMenu("Sales Report", icons[0]);
        repList.add(menu);
        menu = new ReportMenu("Purchase Report", icons[1]);
        repList.add(menu);
        menu = new ReportMenu("Product Report", icons[2]);
        repList.add(menu);
        menu = new ReportMenu("Salary Report", icons[3]);
        repList.add(menu);

        repAdapter.notifyDataSetChanged();
    }
}
