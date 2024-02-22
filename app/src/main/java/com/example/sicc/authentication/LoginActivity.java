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
import com.example.sicc.activities.MainActivity;
import com.example.sicc.R;
import com.example.sicc.adapters.LoadingDialog;
import com.example.sicc.models.Constant;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout layoutEmail, layoutPassword;
    private EditText txt_email, txt_password;
    private TextView btn_lupaPassword, txt_register, btn_register;
    private Button btn_login;
    private long backPressedTime = 0;
    private final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        layoutEmail = findViewById(R.id.email_input_layout);
        layoutPassword = findViewById(R.id.password_input_layout);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_lupaPassword = findViewById(R.id.lupa_password);
        txt_register = findViewById(R.id.txt_register);
        btn_register = findViewById(R.id.btn_register);

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

        btn_login.setOnClickListener(v-> {
            if (validate()) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login();  
                    }
                }, 500);
            }
        });

        btn_lupaPassword.setOnClickListener(v-> {
            startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(this);
            finish();
        });

        txt_register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(this);
            finish();
        });

        btn_register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(this);
            finish();
        });
    }

    private boolean validate() {
        if (txt_email.getText().toString().isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Username Tidak Boleh Kosong");
            return false;
        }

        if (txt_password.getText().toString().isEmpty()) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Password Tidak Boleh Kosong");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(txt_email.getText().toString()).matches()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email Tidak Valid");
            return false;
        }

        if (txt_password.getText().toString().length() < 8) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Password Setidaknya 8 Karakter");
            return false;
        }

        return true;
    }

    private void login() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN, response ->  {
            // Handle the response
            Log.d("Response", response);
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200 && message.equals("Success")) {
                    JSONObject responseData = res.getJSONObject("response");

                    JSONObject userData = responseData.getJSONObject("user");

                    // Check Role Data User
                    if (userData.getString("role").equals("Admin")) {
                        loadingDialog.dismissLoadingDialog();

                        Toast.makeText(getApplicationContext(), "Anda Tidak Memiliki Akses !", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putInt("id_user", userData.getInt("id"));
                        editor.putString("nama_ketua", userData.getString("name"));
                        editor.putString("email", userData.getString("email"));
                        editor.putString("role", userData.getString("role"));
                        editor.putBoolean("isLogin", true);
                        editor.apply();

                        loadingDialog.dismissLoadingDialog();

                        // If login success
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Animatoo.INSTANCE.animateSlideLeft(this);
                        finish();

                        Toast.makeText(getApplicationContext(), "Login Sukses !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loadingDialog.dismissLoadingDialog();

                    // Handle the case when the response indicates an error
                    Toast.makeText(getApplicationContext(), "Login Gagal : " + message, Toast.LENGTH_SHORT).show();
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