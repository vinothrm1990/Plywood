package com.app.plywood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.plywood.R;
import com.app.plywood.data.AddProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thebat.lib.validutil.ValidUtils;

import static com.app.plywood.activity.SaleActivity.get_data;
import static com.app.plywood.activity.SaleActivity.rvSale;
import static com.app.plywood.activity.SaleActivity.sumTotal;
import static com.app.plywood.activity.SaleActivity.tvNoProduct;
import static com.app.plywood.activity.SaleActivity.tvTotal;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.MyViewHolder> {

    Context mContext;
    List<AddProduct> addList;
    List<String> data;

    public AddAdapter(Context mContext, List<AddProduct> addList) {
        this.mContext = mContext;
        this.addList = addList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        final AddProduct product = addList.get(i);

        myViewHolder.tvThick.setText(product.getThick());
        myViewHolder.tvSize.setText(product.getSize());
        myViewHolder.tvQuantity.setText(product.getQuantity());

        myViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                addList.remove(i);
                notifyItemRemoved(i);
                notifyItemChanged(i, addList.size());
                notifyDataSetChanged();
                sumTotal = grandTotal(addList);
                tvTotal.setText(String.valueOf(sumTotal));
                get_data = new ArrayList<>(Arrays.asList(getData(addList)));
                ValidUtils validUtils = new ValidUtils();
                validUtils.showToast(mContext, String.valueOf(get_data));

                if (addList.size() == 0){
                    rvSale.setVisibility(View.GONE);
                    tvNoProduct.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }

    public int grandTotal(List<AddProduct> lists) {
        int totalPrice = 0;
        for (int i = 0; i < lists.size(); i++) {
            totalPrice += lists.get(i).getPrice();
        }
        return totalPrice;
    }

    public String getData(List<AddProduct> lists) {
        data = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            data.add(lists.get(i).getThick()  +"_"+ lists.get(i).getSize() +"_"+ lists.get(i).getQuantity() +"_"+String.valueOf(lists.get(i).getPrice()));
            notifyDataSetChanged();
        }
        return String.valueOf(data);
    }


    @Override
    public int getItemCount() {
        return addList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvThick, tvSize, tvQuantity;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvThick = itemView.findViewById(R.id.add_tv_thick);
            tvSize = itemView.findViewById(R.id.add_tv_size);
            tvQuantity = itemView.findViewById(R.id.add_tv_quantity);
            cardView = itemView.findViewById(R.id.cv_add);
        }
    }
}
