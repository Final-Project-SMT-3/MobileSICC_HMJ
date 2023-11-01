package com.example.sicc.authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextInputLayout layoutUsername, layoutPassword;
    private EditText txt_username, txt_password;
    private TextView btn_lupaPassword;
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
        layoutUsername = findViewById(R.id.username_input_layout);
        layoutPassword = findViewById(R.id.password_input_layout);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_lupaPassword = findViewById(R.id.lupa_password);

        txt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txt_username.getText().toString().isEmpty()) {
                    layoutUsername.setErrorEnabled(false);
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
    }

    private boolean validate() {
        if (txt_username.getText().toString().isEmpty()) {
            layoutUsername.setErrorEnabled(true);
            layoutUsername.setError("Username Tidak Boleh Kosong");
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

    private void login() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN, response ->  {
            // Handle the response
            Log.d("Response", response);
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200 && message.equals("Success")) {
                    JSONObject userData = res.getJSONObject("response");

                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putInt("id", userData.getInt("id"));
                    editor.putString("user", userData.getString("username"));
                    editor.putString("pass", userData.getString("password"));
                    editor.putString("nim_ketua", userData.getString("no_identitas"));
                    editor.putString("nama_ketua", userData.getString("nama"));
                    editor.putString("nama_kelompok", userData.getString("nama_kelompok"));
                    editor.putString("nim_kelompok", userData.getString("nim_anggota"));
                    editor.putString("nama_anggota", userData.getString("nama_anggota"));
                    editor.putString("dospem", userData.getString("nama_dospem"));
                    editor.putString("lomba", userData.getString("nama_lomba"));
                    editor.apply();

                    loadingDialog.dismissLoadingDialog();

                    // If login success
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Animatoo.INSTANCE.animateSlideLeft(this);
                    finish();

                    Toast.makeText(getApplicationContext(), "Login Sukses !", Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
                return headers;
            }

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("password", txt_password.getText().toString().trim());
                params.put("username", txt_username.getText().toString().trim());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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