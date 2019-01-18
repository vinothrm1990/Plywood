package com.app.plywood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.plywood.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SalaryReportAdapter extends RecyclerView.Adapter<SalaryReportAdapter.MyViewHolder>  {

    Context mContext;
    ArrayList<HashMap<String,String>> reportList;

    public SalaryReportAdapter(Context mContext, ArrayList<HashMap<String,String>> reportList) {
        this.mContext = mContext;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.salary_report_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

       HashMap<String, String> map = reportList.get(i);

        myViewHolder.tvId.setText("#\t"+map.get("id"));
        myViewHolder.tvVeenerFt.setText(map.get("veener_ft"));
        myViewHolder.tvVeenerPrice.setText(map.get("veener_price"));
        myViewHolder.tvDyerFt.setText(map.get("dyer_ft"));
        myViewHolder.tvDyerPrice.setText(map.get("dyer_price"));
        myViewHolder.tvPressFt.setText(map.get("press_ft"));
        myViewHolder.tvPressPrice.setText(map.get("press_price"));
        myViewHolder.tvFinishFt.setText(map.get("finish_ft"));
        myViewHolder.tvFinishPrice.setText(map.get("finish_price"));
        myViewHolder.tvTotal.setText("₹"+map.get("total"));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvTotal, tvVeenerFt, tvVeenerPrice, tvDyerFt, tvDyerPrice, tvPressFt, tvPressPrice, tvFinishFt, tvFinishPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.sal_rep_tv_id);
            tvVeenerFt = itemView.findViewById(R.id.sal_rep_tv_v_ft);
            tvVeenerPrice = itemView.findViewById(R.id.sal_rep_tv_v_price);
            tvDyerFt = itemView.findViewById(R.id.sal_rep_tv_d_ft);
            tvDyerPrice = itemView.findViewById(R.id.sal_rep_tv_d_price);
            tvPressFt = itemView.findViewById(R.id.sal_rep_tv_p_ft);
            tvPressPrice = itemView.findViewById(R.id.sal_rep_tv_p_price);
            tvFinishFt = itemView.findViewById(R.id.sal_rep_tv_f_ft);
            tvFinishPrice = itemView.findViewById(R.id.sal_rep_tv_f_price);
            tvTotal = itemView.findViewById(R.id.sal_rep_tv_total);

        }
    }
}
