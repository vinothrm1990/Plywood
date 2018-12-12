package com.app.plywood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.plywood.R;
import com.app.plywood.activity.SaleActivity;
import com.app.plywood.data.DashboardMenu;
import com.bumptech.glide.Glide;

import java.util.List;
import spencerstudios.com.bungeelib.Bungee;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {

    Context mContext;
    List<DashboardMenu> dashList;

    public DashboardAdapter(Context mContext, List<DashboardMenu> dashList) {
        this.mContext = mContext;
        this.dashList = dashList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        DashboardMenu menu = dashList.get(i);

        myViewHolder.tvTitle.setText(menu.getTitle());
        Glide.with(mContext).load(menu.getImage()).into(myViewHolder.ivIcon);

        myViewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 1){
                    mContext.startActivity(new Intent(mContext, SaleActivity.class));
                    Bungee.shrink(mContext);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dashList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton ivIcon;
        TextView tvTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.dash_tv_title);
            ivIcon = itemView.findViewById(R.id.dash_iv_icon);
        }
    }
}
