package com.example.sicc.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;

public class LupaPasswordActivity extends AppCompatActivity {
    private Button btn_sendCode;
    private ImageView btn_back;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        init();
    }

    private void init() {
        btn_back = findViewById(R.id.btn_back);
        btn_sendCode = findViewById(R.id.btn_sendCode);

        btn_back.setOnClickListener(v-> {
            startActivity(new Intent(LupaPasswordActivity.this, LoginActivity.class));
            Animatoo.INSTANCE.animateSlideRight(this);
            finish();
        });

        btn_sendCode.setOnClickListener(v-> {
            startActivity(new Intent(LupaPasswordActivity.this, KodeOTPActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(this);
            finish();
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