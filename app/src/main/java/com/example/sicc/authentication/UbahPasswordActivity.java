package com.example.sicc.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.adapters.LoadingMain;
import com.example.sicc.models.Constant;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UbahPasswordActivity extends AppCompatActivity {
    private EditText txt_password, txt_conf_password;
    private TextInputLayout layout_password, layout_conf_password;
    private Button btn_resetPassword;
    private long backPressedTime = 0;
    private LoadingMain loadingMain;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        init();
    }

    private void init() {
        txt_password = findViewById(R.id.txt_password);
        txt_conf_password = findViewById(R.id.txt_confPassword);
        layout_password = findViewById(R.id.password_input_layout);
        layout_conf_password = findViewById(R.id.conf_password_input_layout);
        btn_resetPassword = findViewById(R.id.btn_resetPass);
        sharedPreferences = getApplicationContext().getSharedPreferences("email_reset", Context.MODE_PRIVATE);
        loadingMain = new LoadingMain(this);

        Handler handler = new Handler();

        txt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txt_password.getText().toString().isEmpty()) {
                    layout_password.setErrorEnabled(false);
                }

                if (txt_password.getText().toString().length() >= 8) {
                    layout_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_conf_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txt_conf_password.getText().toString().isEmpty()) {
                    layout_conf_password.setErrorEnabled(false);
                }

                if (txt_conf_password.getText().toString().length() >= 8) {
                    layout_conf_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_resetPassword.setOnClickListener(v-> {
            if (checkPassword()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingMain.show();

//                        resetPassword();
                    }
                }, 500);
            }
        });
    }

    private Boolean checkPassword() {
        if (txt_password.getText().toString().isEmpty()) {
            layout_password.setErrorEnabled(true);
            layout_password.setError("Username Tidak Boleh Kosong");
            return false;
        }

        if (txt_conf_password.getText().toString().isEmpty()) {
            layout_conf_password.setErrorEnabled(true);
            layout_conf_password.setError("Konfimasi Password Tidak Boleh Kosong");
            return false;
        }

        if (txt_password.getText().toString().length() < 8) {
            layout_password.setErrorEnabled(true);
            layout_password.setError("Password Setidaknya 8 Karakter");
            return false;
        }

        if (txt_conf_password.getText().toString().length() < 8) {
            layout_conf_password.setErrorEnabled(true);
            layout_conf_password.setError("Konfimasi Password Setidaknya 8 Karakter");
            return false;
        }

        if (!txt_conf_password.getText().toString().trim().equals(txt_password.getText().toString().trim())) {
            layout_conf_password.setErrorEnabled(true);
            layout_conf_password.setError("Konfimasi Password Tidak Sama");
           return false;
        }

        return true;
    }

//    private void resetPassword() {
//        StringRequest request = new StringRequest(Request.Method.POST, Constant.RESET_PASSWORD, response -> {
//            try {
//                JSONObject res = new JSONObject(response);
//
//                int statusCode = res.getInt("status_code");
//                String message = res.getString("message");
//
//                if (statusCode == 200 && message.equals("Success")) {
//
//                    startActivity(new Intent(UbahPasswordActivity.this, LoginActivity.class));
//                    Animatoo.INSTANCE.animateSlideLeft(this);
//                    finish();
//
//                    Toast.makeText(this, "Password Berhasil Di Ubah", Toast.LENGTH_SHORT).show();
//
//                    loadingMain.cancel();
//                } else {
//                    // Handle the case when the response indicates an error
//
//                    loadingMain.cancel();
//
//                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//                loadingMain.cancel();
//
//                Toast.makeText(this, "JSON Parsing Error", Toast.LENGTH_SHORT).show();
//            }
//        }, error -> {
//            error.printStackTrace();
//
//            loadingMain.cancel();
//
//            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
//        }) {
//            String email = sharedPreferences.getString("email", "-");
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
//                return headers;
//            }
//
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", txt_conf_password.getText().toString().trim());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        request.setRetryPolicy(new DefaultRetryPolicy(30000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(request);
//    }

    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - backPressedTime > 3500) {
            Toast.makeText(this, "Tekan Sekali Lagi Untuk Keluar", Toast.LENGTH_SHORT).show();
            backPressedTime = currentTime;
        } else {
            super.onBackPressed();
            finish();
        }
    }
}