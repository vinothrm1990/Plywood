package com.app.plywood.activity;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.plywood.R;

public class SalesReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("SALES REPORT");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);
    }
}
