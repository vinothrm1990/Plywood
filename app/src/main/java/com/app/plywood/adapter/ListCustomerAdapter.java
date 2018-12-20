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
import com.app.plywood.activity.ViewCustomerDetailActivity;
import com.app.plywood.data.CustomerData;
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

public class ListCustomerAdapter extends RecyclerView.Adapter<ListCustomerAdapter.MyViewHolder>
        implements Filterable {

    Context mContext;
    ValidUtils validUtils;
    List<CustomerData> customerList;
    List<CustomerData> customerFilteredList;
    ListCustomerAdapterListener adapterListener;
    String CUSTOMER_URL = Constants.BASE_URL + Constants.GET_CUSTOMER;
    String REMOVE_CUSTOMER_URL = Constants.BASE_URL + Constants.REMOVE_CUSTOMER;

    public ListCustomerAdapter(Context mContext, List<CustomerData> customerList, ListCustomerAdapterListener adapterListener) {
        this.mContext = mContext;
        this.adapterListener = adapterListener;
        this.customerList = customerList;
        this.customerFilteredList = customerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_customer_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final CustomerData customer = customerFilteredList.get(i);

        myViewHolder.tvName.setText(customer.getName());
        myViewHolder.tvCName.setText(customer.getCname());

        validUtils = new ValidUtils();

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validUtils.showProgressDialog(mContext, (Activity) mContext);
                StringRequest request = new StringRequest(Request.Method.POST, CUSTOMER_URL,
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

                                        Intent intent = new Intent(mContext, ViewCustomerDetailActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("id", object.getString("c_id"));
                                        bundle.putString("name", object.getString("c_name"));
                                        bundle.putString("address", object.getString("address"));
                                        bundle.putString("city", object.getString("city"));
                                        bundle.putString("state", object.getString("state"));
                                        bundle.putString("mobile", object.getString("mobile"));
                                        bundle.putString("company", object.getString("c_company"));
                                        bundle.putString("pincode", object.getString("pincode"));
                                        bundle.putString("baddress", object.getString("b_address"));
                                        bundle.putString("bcity", object.getString("b_city"));
                                        bundle.putString("bstate", object.getString("b_state"));
                                        bundle.putString("bpincode", object.getString("b_pincode"));
                                        bundle.putString("saddress", object.getString("s_address"));
                                        bundle.putString("scity", object.getString("s_city"));
                                        bundle.putString("sstate", object.getString("s_state"));
                                        bundle.putString("spincode", object.getString("s_pincode"));
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
                                validUtils.showToast(mContext, error.getMessage());
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
                builder.setMessage("Are you sure that you want remove your Customer from Lists?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        customerList.remove(i);
                        notifyItemRemoved(i);
                        notifyDataSetChanged();
                        dialog.dismiss();
                        String mobile = customer.getMobile();
                        removeCustomer(mobile);
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

    private void removeCustomer(final String mobile) {

        StringRequest request = new StringRequest(Request.Method.POST, REMOVE_CUSTOMER_URL,
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
                        validUtils.showToast(mContext, error.getMessage());
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
        return customerFilteredList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivEdit, ivRemove;
        TextView tvName, tvCName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.view_cus_name);
            tvCName = itemView.findViewById(R.id.view_cus_cname);
            ivEdit = itemView.findViewById(R.id.view_cus_edit);
            ivRemove = itemView.findViewById(R.id.view_cus_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adapterListener.onCustomerSelected(customerFilteredList.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    customerFilteredList = customerList;
                } else {
                    List<CustomerData> filteredList = new ArrayList<>();
                    for (CustomerData row : customerList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCname().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    customerFilteredList = filteredList;
                }

                FilterResults results = new FilterResults();
                results.values = customerFilteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                customerFilteredList = (ArrayList<CustomerData>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ListCustomerAdapterListener {
        void onCustomerSelected(CustomerData customer);
    }
}
