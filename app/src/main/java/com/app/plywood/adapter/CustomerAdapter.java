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
import com.app.plywood.activity.AddCustomerActivity;
import com.app.plywood.activity.ListCustomerActivity;
import com.app.plywood.data.CustomerMenu;
import com.bumptech.glide.Glide;

import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {
    Context mContext;
    List<CustomerMenu> cusList;

    public CustomerAdapter(Context mContext, List<CustomerMenu> cusList) {
        this.mContext = mContext;
        this.cusList = cusList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        CustomerMenu menu = cusList.get(i);
        myViewHolder.tvTitle.setText(menu.getTitle());
        Glide.with(mContext).load(menu.getImage()).into(myViewHolder.ivIcon);

        myViewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 0){
                    mContext.startActivity(new Intent(mContext, AddCustomerActivity.class));
                    Bungee.split(mContext);
                }else if (i == 1){
                    mContext.startActivity(new Intent(mContext, ListCustomerActivity.class));
                    Bungee.split(mContext);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cusList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton ivIcon;
        TextView tvTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.cus_tv_title);
            ivIcon = itemView.findViewById(R.id.cus_iv_icon);
        }
    }
}
