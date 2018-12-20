package com.app.plywood.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.plywood.R;
import com.app.plywood.activity.ViewVendorDetailActivity;
import com.app.plywood.data.VendorData;
import com.app.plywood.helper.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spencerstudios.com.bungeelib.Bungee;
import thebat.lib.validutil.ValidUtils;

public class ListVendorAdapter extends RecyclerView.Adapter<ListVendorAdapter.MyViewHolder>
        implements Filterable {

    Context mContext;
    List<VendorData> vendorList;
    List<VendorData> vendorFilteredList;
    ValidUtils validUtils;
    ListVendorAdapterListener adapterListener;
    String VENDOR_URL = Constants.BASE_URL + Constants.GET_VENDOR;
    String REMOVE_VENDOR_URL = Constants.BASE_URL + Constants.REMOVE_VENDOR;

    public ListVendorAdapter(Context mContext, List<VendorData> vendorList,  ListVendorAdapterListener adapterListener) {
        this.mContext = mContext;
        this.adapterListener = adapterListener;
        this.vendorList = vendorList;
        this.vendorFilteredList = vendorList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_vendor_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final VendorData vendor = vendorFilteredList.get(i);

        myViewHolder.tvName.setText(vendor.getName());
        myViewHolder.tvCName.setText(vendor.getCname());

        validUtils = new ValidUtils();

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validUtils.showProgressDialog(mContext, (Activity) mContext);

                StringRequest request = new StringRequest(Request.Method.POST, VENDOR_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);

                                    if (jsonObject.getString("status")
                                            .equalsIgnoreCase("success")){
                                        validUtils.hideProgressDialog();
                                        String data = jsonObject.getString("data");
                                        JSONArray array = new JSONArray(data);
                                        JSONObject object = array.getJSONObject(0);

                                        Intent intent = new Intent(mContext, ViewVendorDetailActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("id", object.getString("id"));
                                        bundle.putString("name", object.getString("v_name"));
                                        bundle.putString("address", object.getString("address"));
                                        bundle.putString("city", object.getString("city"));
                                        bundle.putString("state", object.getString("state"));
                                        bundle.putString("mobile", object.getString("mobile"));
                                        bundle.putString("company", object.getString("v_company"));
                                        bundle.putString("pincode", object.getString("pincode"));
                                        intent.putExtras(bundle);
                                        mContext.startActivity(intent);
                                        Bungee.split(mContext);

                                    }else if (jsonObject.getString("status")
                                            .equalsIgnoreCase("no data")){
                                        validUtils.hideProgressDialog();
                                       validUtils.showToast(mContext,jsonObject.getString("message"));

                                    }else {
                                        validUtils.hideProgressDialog();
                                        validUtils.showToast(mContext,"Something went Wrong");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    validUtils.hideProgressDialog();
                                    validUtils.showToast(mContext,e.getMessage());
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(mContext,error.getMessage());
                            }
                        })
                {

                    @Override
                    protected Map<String, String> getParams()
                    {
                        String company = myViewHolder.tvCName.getText().toString().trim();
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("company", company);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(mContext);
                queue.add(request);
            }
        });

        myViewHolder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure that you want remove your Vendor from Lists?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vendorList.remove(i);
                        notifyItemRemoved(i);
                        notifyDataSetChanged();
                        dialog.dismiss();
                        String mobile = vendor.getMobile();
                        removeVendor(mobile);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void removeVendor(final String mobile) {

        StringRequest request = new StringRequest(Request.Method.POST, REMOVE_VENDOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){
                                validUtils.showToast(mContext,jsonObject.getString("message"));


                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.showToast(mContext,jsonObject.getString("message"));

                            }else {
                                validUtils.showToast(mContext,"Something went Wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.showToast(mContext,e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.showToast(mContext,error.getMessage());
                    }
                })
        {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return vendorFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    vendorFilteredList = vendorList;
                } else {
                    List<VendorData> filteredList = new ArrayList<>();
                    for (VendorData row : vendorList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCname().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    vendorFilteredList = filteredList;
                }

                FilterResults results = new FilterResults();
                results.values = vendorFilteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                vendorFilteredList = (ArrayList<VendorData>) results.values;
                notifyDataSetChanged();
            }
        };

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivEdit, ivRemove;
        TextView tvName, tvCName;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.view_ven_name);
            tvCName = itemView.findViewById(R.id.view_ven_cname);
            ivEdit = itemView.findViewById(R.id.view_ven_edit);
            ivRemove = itemView.findViewById(R.id.view_ven_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adapterListener.onVendorSelected(vendorFilteredList.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface ListVendorAdapterListener {
        void onVendorSelected(VendorData vendor);
    }
}
