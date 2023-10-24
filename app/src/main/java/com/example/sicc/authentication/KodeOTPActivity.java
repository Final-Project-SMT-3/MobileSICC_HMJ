package com.example.sicc.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.chaos.view.PinView;

import com.example.sicc.R;


public class KodeOTPActivity extends AppCompatActivity {
    private Button btn_konfirmasi;
    private ImageView btn_back;
    private PinView pinView;
    private long backPressedTime = 0;

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

        pinView.requestFocus();
        pinView.setAnimationEnable(true);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        btn_back.setOnClickListener(v-> {
            startActivity(new Intent(KodeOTPActivity.this, LupaPasswordActivity.class));
            finish();
        });

        btn_konfirmasi.setOnClickListener(v-> {
            startActivity(new Intent(KodeOTPActivity.this, UbahPasswordActivity.class));
            finish();
        });

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 4) {
                    Toast.makeText(getApplicationContext(), "Suksess!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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