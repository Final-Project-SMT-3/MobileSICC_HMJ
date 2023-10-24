package com.example.sicc.authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicc.activities.MainActivity;
import com.example.sicc.R;
import com.example.sicc.models.Constant;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout layoutEmail, layoutPassword;
    private EditText txt_email, txt_password;
    private TextView btn_lupaPassword;
    private Button btn_login;
    private long backPressedTime = 0;

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

        btn_login.setOnClickListener(v-> {
            if (validate()) {
                login();
            }
        });

        btn_lupaPassword.setOnClickListener(v-> {
            startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class));
            finish();
        });
    }

    private boolean validate() {
        if (txt_email.getText().toString().isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email Tidak Boleh Kosong");
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
            Log.d("Res", response);
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200) {
                    JSONObject userData = res.getJSONObject("response");

                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putInt("id", userData.getInt("id"));
                    editor.putString("no_identitas", userData.getString("no_identitas"));
                    editor.putString("name", userData.getString("nama"));
                    editor.putString("username", userData.getString("username"));
                    editor.putString("tipe", userData.getString("tipe"));
                    editor.apply();

                    // If login success
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(getApplicationContext(), "Login Sukses !", Toast.LENGTH_SHORT).show();

                } else {
                    // Handle the case when the response indicates an error
                    Toast.makeText(getApplicationContext(), "Login Gagal : " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle the case when there's a JSON parsing error
                Toast.makeText(getApplicationContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error ->  {
            error.printStackTrace();
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
                params.put("username", txt_email.getText().toString().trim());
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