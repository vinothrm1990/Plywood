package com.app.plywood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.plywood.R;
import com.app.plywood.activity.ProductReportActivity;
import com.app.plywood.activity.PurchaseReportActivity;
import com.app.plywood.activity.SalesReportActivity;
import com.app.plywood.data.ReportMenu;
import com.bumptech.glide.Glide;

import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    Context mContext;
    List<ReportMenu> repList;

    public ReportAdapter(Context mContext, List<ReportMenu> repList) {
        this.mContext = mContext;
        this.repList = repList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        ReportMenu menu = repList.get(i);

        myViewHolder.tvTitle.setText(menu.getTitle());
        Glide.with(mContext).load(menu.getImage()).into(myViewHolder.ivIcon);

        myViewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 0){
                    mContext.startActivity(new Intent(mContext, SalesReportActivity.class));
                    Bungee.shrink(mContext);
                }else if (i == 1){
                    mContext.startActivity(new Intent(mContext, PurchaseReportActivity.class));
                    Bungee.shrink(mContext);
                }else if (i == 2){
                    mContext.startActivity(new Intent(mContext, ProductReportActivity.class));
                    Bungee.shrink(mContext);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton ivIcon;
        TextView tvTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.rep_tv_title);
            ivIcon = itemView.findViewById(R.id.rep_iv_icon);
        }
    }
}
