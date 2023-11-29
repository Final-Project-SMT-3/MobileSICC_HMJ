package com.example.sicc.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chaos.view.PinView;

import com.example.sicc.R;
import com.example.sicc.adapters.LoadingMain;
import com.example.sicc.models.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class KodeOTPActivity extends AppCompatActivity {
    private Button btn_konfirmasi;
    private ImageView btn_back;
    private PinView pinView;
    private long backPressedTime = 0;
    private LoadingMain loadingMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kode_otpactivity);

        init();
    }

    private void init() {
        btn_back = findViewById(R.id.btn_back);
        btn_konfirmasi = findViewById(R.id.btn_Confirm);
        pinView = findViewById(R.id.txt_codeOTP);
        loadingMain = new LoadingMain(this);

        Handler handler = new Handler();

        pinView.requestFocus();
        pinView.setAnimationEnable(true);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        btn_konfirmasi.setOnClickListener(v-> {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!pinView.getText().toString().isEmpty()) {
                        loadingMain.show();

                        cekCodeOTP();
                    } else {
                        Toast.makeText(getApplicationContext(), "Masukan Kode OTP Yang Kami Kirim Di Email Anda", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 500);
        });

        btn_back.setOnClickListener(v-> {
            startActivity(new Intent(KodeOTPActivity.this, LupaPasswordActivity.class));
            Animatoo.INSTANCE.animateSlideRight(this);
            finish();
        });

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 4) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingMain.show();

                            cekCodeOTP();
                        }
                    }, 500);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void cekCodeOTP() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.CEK_OTP, response -> {
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200 && message.equals("Success")) {

                    startActivity(new Intent(KodeOTPActivity.this, UbahPasswordActivity.class));
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
                params.put("otp", pinView.getText().toString());
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