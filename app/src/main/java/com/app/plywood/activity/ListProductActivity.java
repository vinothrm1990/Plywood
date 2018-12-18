package com.app.plywood.activity;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.plywood.R;
import com.app.plywood.helper.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import thebat.lib.validutil.ValidUtils;

public class ListProductActivity extends AppCompatActivity {

    ArrayList<HashMap<String,String>> listProducts;
    HashMap<String, String> map;
    ValidUtils validUtils;
    String id, date, thick, size, quantity;
    TextView tv_18_8_4, tv_18_8_3, tv_18_7_4, tv_18_7_3, tv_18_6_4, tv_18_6_3,
            tv_15_8_4, tv_15_8_3, tv_15_7_4, tv_15_7_3, tv_15_6_4, tv_15_6_3,
            tv_12_8_4, tv_12_8_3, tv_12_7_4, tv_12_7_3, tv_12_6_4, tv_12_6_3,
            tv_9_8_4, tv_9_8_3, tv_9_7_4, tv_9_7_3, tv_9_6_4, tv_9_6_3,
            tv_6_8_4, tv_6_8_3, tv_6_7_4, tv_6_7_3, tv_6_6_4, tv_6_6_3;
    String GET_PRODUCT_URL = Constants.BASE_URL + Constants.GET_PRODUCT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("LIST PRODUCTS");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "montser_bold.otf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        listProducts = new ArrayList<>();

        tv_18_8_4 = findViewById(R.id.tv_18_8_4);
        tv_18_8_3 = findViewById(R.id.tv_18_8_3);
        tv_18_7_4 = findViewById(R.id.tv_18_7_4);
        tv_18_7_3 = findViewById(R.id.tv_18_7_3);
        tv_18_6_4 = findViewById(R.id.tv_18_6_4);
        tv_18_6_3 = findViewById(R.id.tv_18_6_3);
        tv_15_8_4 = findViewById(R.id.tv_15_8_4);
        tv_15_8_3 = findViewById(R.id.tv_15_8_3);
        tv_15_7_4 = findViewById(R.id.tv_15_7_4);
        tv_15_7_3 = findViewById(R.id.tv_15_7_3);
        tv_15_6_4 = findViewById(R.id.tv_15_6_4);
        tv_15_6_3 = findViewById(R.id.tv_15_6_3);
        tv_12_8_4 = findViewById(R.id.tv_12_8_4);
        tv_12_8_3 = findViewById(R.id.tv_12_8_3);
        tv_12_7_4 = findViewById(R.id.tv_12_7_4);
        tv_12_7_3 = findViewById(R.id.tv_12_7_3);
        tv_12_6_4 = findViewById(R.id.tv_12_6_4);
        tv_12_6_3 = findViewById(R.id.tv_12_6_3);
        tv_9_8_4 = findViewById(R.id.tv_9_8_4);
        tv_9_8_3 = findViewById(R.id.tv_9_8_3);
        tv_9_7_4 = findViewById(R.id.tv_9_7_4);
        tv_9_7_3 = findViewById(R.id.tv_9_7_3);
        tv_9_6_4 = findViewById(R.id.tv_9_6_4);
        tv_9_6_3 = findViewById(R.id.tv_9_6_3);
        tv_6_8_4 = findViewById(R.id.tv_6_8_4);
        tv_6_8_3 = findViewById(R.id.tv_6_8_3);
        tv_6_7_4 = findViewById(R.id.tv_6_7_4);
        tv_6_7_3 = findViewById(R.id.tv_6_7_3);
        tv_6_6_4 = findViewById(R.id.tv_6_6_4);
        tv_6_6_3 = findViewById(R.id.tv_6_6_3);

        getProducts();
    }

    private void getProducts() {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.GET, GET_PRODUCT_URL,

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
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    map = new HashMap<String, String>();

                                    id = object.getString("pid");
                                    date = object.getString("date");
                                    thick = object.getString("thickness");
                                    size = object.getString("size");
                                    quantity = object.getString("quantity");

                                    map.put("pid", id);
                                    map.put("date", date);
                                    map.put("thickness", thick);
                                    map.put("size", size);
                                    map.put("quantity", quantity);

                                    listProducts.add(map);

                                   if (listProducts.get(i).containsValue("18 mm")){
                                       if (listProducts.get(i).containsValue("8Ft x 4Ft")){
                                           tv_18_8_4.setText(map.get("quantity"));
                                       }else if (listProducts.get(i).containsValue("8Ft x 3Ft")){
                                           tv_18_8_3.setText(map.get("quantity"));
                                       }else if (listProducts.get(i).containsValue("7Ft x 4Ft")){
                                           tv_18_7_4.setText(map.get("quantity"));
                                       }else if (listProducts.get(i).containsValue("7Ft x 3Ft")){
                                           tv_18_7_3.setText(map.get("quantity"));
                                       }else if (listProducts.get(i).containsValue("6Ft x 4Ft")){
                                           tv_18_6_4.setText(map.get("quantity"));
                                       }else if (listProducts.get(i).containsValue("6Ft x 3Ft")){
                                           tv_18_6_3.setText(map.get("quantity"));
                                       }
                                   }
                                    if (listProducts.get(i).containsValue("15 mm")){
                                        if (listProducts.get(i).containsValue("8Ft x 4Ft")){
                                            tv_15_8_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("8Ft x 3Ft")){
                                            tv_15_8_3.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("7Ft x 4Ft")){
                                            tv_15_7_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("7Ft x 3Ft")){
                                            tv_15_7_3.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("6Ft x 4Ft")){
                                            tv_15_6_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("6Ft x 3Ft")){
                                            tv_15_6_3.setText(map.get("quantity"));
                                        }
                                    }
                                    if (listProducts.get(i).containsValue("12 mm")){
                                        if (listProducts.get(i).containsValue("8Ft x 4Ft")){
                                            tv_12_8_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("8Ft x 3Ft")){
                                            tv_12_8_3.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("7Ft x 4Ft")){
                                            tv_12_7_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("7Ft x 3Ft")){
                                            tv_12_7_3.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("6Ft x 4Ft")){
                                            tv_12_6_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("6Ft x 3Ft")){
                                            tv_12_6_3.setText(map.get("quantity"));
                                        }
                                    }
                                    if (listProducts.get(i).containsValue("9 mm")){
                                        if (listProducts.get(i).containsValue("8Ft x 4Ft")){
                                            tv_9_8_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("8Ft x 3Ft")){
                                            tv_9_8_3.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("7Ft x 4Ft")){
                                            tv_9_7_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("7Ft x 3Ft")){
                                            tv_9_7_3.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("6Ft x 4Ft")){
                                            tv_9_6_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("6Ft x 3Ft")){
                                            tv_9_6_3.setText(map.get("quantity"));
                                        }
                                    }
                                    if (listProducts.get(i).containsValue("6 mm")){
                                        if (listProducts.get(i).containsValue("8Ft x 4Ft")){
                                            tv_6_8_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("8Ft x 3Ft")){
                                            tv_6_8_3.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("7Ft x 4Ft")){
                                            tv_6_7_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("7Ft x 3Ft")){
                                            tv_6_7_3.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("6Ft x 4Ft")){
                                            tv_6_6_4.setText(map.get("quantity"));
                                        }else if (listProducts.get(i).containsValue("6Ft x 3Ft")){
                                            tv_6_6_3.setText(map.get("quantity"));
                                        }
                                    }
                                }


                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("empty")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(ListProductActivity.this, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(ListProductActivity.this, "Something went wrong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(ListProductActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(ListProductActivity.this, error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(ListProductActivity.this);
        queue.add(request);
    }
}
