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
import com.app.plywood.activity.AddProductActivity;
import com.app.plywood.activity.ListProductActivity;
import com.app.plywood.data.ProductMenu;
import com.bumptech.glide.Glide;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context mContext;
    List<ProductMenu> proList;

    public ProductAdapter(Context mContext, List<ProductMenu> proList) {
        this.mContext = mContext;
        this.proList = proList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        ProductMenu menu = proList.get(i);

        myViewHolder.tvTitle.setText(menu.getTitle());
        Glide.with(mContext).load(menu.getImage()).into(myViewHolder.ivIcon);

        myViewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 0){
                    mContext.startActivity(new Intent(mContext, AddProductActivity.class));
                    Bungee.shrink(mContext);
                }else if (i == 1){
                    mContext.startActivity(new Intent(mContext, ListProductActivity.class));
                    Bungee.shrink(mContext);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return proList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton ivIcon;
        TextView tvTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.pro_tv_title);
            ivIcon = itemView.findViewById(R.id.pro_iv_icon);
        }
    }
}
