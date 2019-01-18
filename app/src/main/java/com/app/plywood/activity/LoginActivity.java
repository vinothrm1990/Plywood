package com.app.plywood.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.plywood.R;
import com.app.plywood.helper.Constants;
import com.libizo.CustomEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import spencerstudios.com.bungeelib.Bungee;
import thebat.lib.validutil.ValidUtils;

public class LoginActivity extends AppCompatActivity {

    CustomEditText etUsername, etPassword;
    Button btnGo;
    String uname, pass;
    ValidUtils validUtils;
    String LOGIN_URL = Constants.BASE_URL + Constants.LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Constants.pref = getApplicationContext().getSharedPreferences("PLYWOOD", 0);
        Constants.editor = Constants.pref.edit();

        etUsername = findViewById(R.id.log_et_uname);
        etPassword = findViewById(R.id.log_et_pass);
        btnGo = findViewById(R.id.log_btn_login);

        validUtils = new ValidUtils();

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validUtils.validateEditTexts(etUsername, etPassword)){
                    String uname = etUsername.getText().toString().trim();
                    String pass = etPassword.getText().toString().trim();
                    login(uname, pass);
                }else {
                    validUtils.showToast(LoginActivity.this, "Feilds are Empty");
                }

            }
        });
    }

    private void login(final String uname, final String pass) {

        validUtils.showProgressDialog(this, this);
        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL,
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

                                String id = object.getString("userid");
                                String username = object.getString("username");
                                String password = object.getString("password");

                                Constants.editor.putBoolean("alreadyLoggedIn", true);
                                Constants.editor.putString("id", id);
                                Constants.editor.putString("username", username);
                                Constants.editor.putString("password", password);
                                Constants.editor.apply();
                                Constants.editor.commit();

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                Bungee.shrink(LoginActivity.this);

                            }else if (jsonObject.getString("status")
                                    .equalsIgnoreCase("failed")){
                                validUtils.hideProgressDialog();
                                validUtils.showToast(LoginActivity.this, jsonObject.getString("message"));

                            }else {
                                validUtils.hideProgressDialog();
                                validUtils.showToast(LoginActivity.this, "Something went wrong!");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            validUtils.hideProgressDialog();
                            validUtils.showToast(LoginActivity.this, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        validUtils.hideProgressDialog();
                        validUtils.showToast(LoginActivity.this, error.getMessage());
                    }
                })
        {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", uname);
                params.put("password", pass);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (validUtils.isNetworkAvailable(this)){
            if (Constants.pref.getBoolean("alreadyLoggedIn", false)){
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
                Bungee.fade(LoginActivity.this);
            }
        }else {
            validUtils.showToast(this, "No Internet Connection Available");
        }

    }
}
