package com.example.sicc.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LupaPasswordActivity extends AppCompatActivity {
    private Button btn_sendCode;
    private ImageView btn_back;
    private EditText txt_email;
    private LoadingMain loadingMain;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        init();
    }

    private void init() {
        txt_email = findViewById(R.id.txt_email);
        btn_back = findViewById(R.id.btn_back);
        btn_sendCode = findViewById(R.id.btn_sendCode);
        loadingMain = new LoadingMain(this);

        btn_sendCode.setOnClickListener(v-> {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!txt_email.getText().toString().isEmpty()) {
                        loadingMain.show();

                        sendCodeOTP();
                    } else {
                        Toast.makeText(getApplicationContext(), "Mohon Masukan Email Anda", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 500);
        });

        btn_back.setOnClickListener(v-> {
            startActivity(new Intent(LupaPasswordActivity.this, LoginActivity.class));
            Animatoo.INSTANCE.animateSlideRight(this);
            finish();
        });
    }

    private void sendCodeOTP() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.LUPA_PASSWORD, response -> {
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200 && message.equals("Success")) {

                    SharedPreferences userPref = getSharedPreferences("email_reset", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("email", txt_email.getText().toString().trim());
                    editor.apply();

                    startActivity(new Intent(LupaPasswordActivity.this, KodeOTPActivity.class));
                    Animatoo.INSTANCE.animateSlideLeft(this);
                    finish();

                    loadingMain.cancel();
                } else {
                    // Handle the case when the response indicates an error

                    loadingMain.cancel();

                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                loadingMain.cancel();

                Toast.makeText(this, "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            loadingMain.cancel();

            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
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
                params.put("email", txt_email.getText().toString().trim());
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