package com.app.plywood.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.plywood.R;
import com.app.plywood.data.InvoiceData;
import com.app.plywood.helper.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import thebat.lib.validutil.ValidUtils;
import static com.app.plywood.activity.SaleActivity.quantity;
import static com.app.plywood.activity.SaleActivity.invoice;
import static com.app.plywood.activity.SaleActivity.rvSale;
import static com.app.plywood.activity.SaleActivity.size;
import static com.app.plywood.activity.SaleActivity.sumTotal;
import static com.app.plywood.activity.SaleActivity.thick;
import static com.app.plywood.activity.SaleActivity.tvNoProduct;
import static com.app.plywood.activity.SaleActivity.tvTotal;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {

    Context mContext;
    List<InvoiceData> addList;
    ValidUtils validUtils;
    int id;
    String REMOVE_INVOICE_URL = Constants.BASE_URL + Constants.REMOVE_INVOICE;

    public InvoiceAdapter(Context mContext, List<InvoiceData> addList) {
        this.mContext = mContext;
        this.addList = addList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        final InvoiceData product = addList.get(i);

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
                quantity = product.getQuantity();
                thick = product.getThick();
                size = product.getSize();
                id = product.getId();
                removeBill(id, invoice, thick, size, quantity);
                if (addList.size() == 0){
                    rvSale.setVisibility(View.GONE);
                    tvNoProduct.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }

    private void removeBill(final int id, final String invoice, final String thick, final String size, final String quantity) {

        validUtils = new ValidUtils();
        validUtils.showProgressDialog(mContext, (Activity) mContext);
        StringRequest request = new StringRequest(Request.Method.POST, REMOVE_INVOICE_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status")
                                    .equalsIgnoreCase("success")){

                                validUtils.hideProgressDialog();
                                validUtils.showToast(mContext, jsonObject.getString("message"));

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(mContext, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(mContext, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(mContext, e.getMessage());
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));
                params.put("invoice", invoice);
                params.put("thickness", thick);
                params.put("size", size);
                params.put("quantity", quantity);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);

    }

    public int grandTotal(List<InvoiceData> lists) {
        int totalPrice = 0;
        for (int i = 0; i < lists.size(); i++) {
            totalPrice += lists.get(i).getPrice();
        }
        return totalPrice;
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
