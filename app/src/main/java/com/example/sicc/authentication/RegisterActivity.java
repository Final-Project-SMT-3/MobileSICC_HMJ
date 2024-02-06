package com.example.sicc.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.activities.MainActivity;
import com.example.sicc.adapters.LoadingDialog;
import com.example.sicc.models.Constant;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout layoutEmail, layoutName, layoutPassword;
    private EditText txt_email, txt_name, txt_password;
    private TextView txt_login, btn_login;
    private Button btn_register;
    private long backPressedTime = 0;
    private final LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        layoutEmail = findViewById(R.id.email_input_layout);
        layoutName = findViewById(R.id.name_input_layout);
        layoutPassword = findViewById(R.id.password_input_layout);
        txt_email = findViewById(R.id.txt_email);
        txt_name = findViewById(R.id.txt_nama_ketua);
        txt_password = findViewById(R.id.txt_password);
        btn_register = findViewById(R.id.btn_register);
        txt_login = findViewById(R.id.txt_login);
        btn_login = findViewById(R.id.btn_login);

        txt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txt_name.getText().toString().isEmpty()) {
                    layoutName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txt_email.getText().toString().isEmpty()) {
                    layoutEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txt_password.getText().toString().isEmpty()) {
                    layoutPassword.setErrorEnabled(false);
                }

                if (txt_password.getText().toString().length() >= 8) {
                    layoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_register.setOnClickListener(v -> {
            if (validate()) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        register();
                    }
                }, 500);
            }
        });

        txt_login.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(this);
            finish();
        });

        btn_login.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(this);
            finish();
        });
    }

    private boolean validate() {
        if (txt_name.getText().toString().isEmpty()) {
            layoutName.setErrorEnabled(true);
            layoutName.setError("Nama Ketua Kelompok Tidak Boleh Kosong");
            return false;
        }

        if (txt_email.getText().toString().isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email Tidak Boleh Kosong");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(txt_email.getText().toString()).matches()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email Tidak Valid");
            return false;
        }

        if (txt_password.getText().toString().isEmpty()) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Password Tidak Boleh Kosong");
            return false;
        }

        if (txt_password.getText().toString().length() < 8) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Password Setidaknya 8 Karakter");
            return false;
        }

        return true;
    }

    private void register() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTER, response ->  {
            // Handle the response
            Log.d("Response", response);
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200 && message.equals("Success")) {
                    loadingDialog.dismissLoadingDialog();

                    // If login success
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    Animatoo.INSTANCE.animateSwipeRight(this);
                    finish();

                    Toast.makeText(getApplicationContext(), "Register Sukses !", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.dismissLoadingDialog();

                    // Handle the case when the response indicates an error
                    Toast.makeText(getApplicationContext(), "Register Gagal : " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                loadingDialog.dismissLoadingDialog();

                // Handle the case when there's a JSON parsing error
                Toast.makeText(getApplicationContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error ->  {
            error.printStackTrace();

            loadingDialog.dismissLoadingDialog();

            // Handle the case when there's a network error
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("nama_ketua", txt_name.getText().toString());
                params.put("email", txt_email.getText().toString().trim());
                params.put("password", txt_password.getText().toString().trim());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    @Override
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