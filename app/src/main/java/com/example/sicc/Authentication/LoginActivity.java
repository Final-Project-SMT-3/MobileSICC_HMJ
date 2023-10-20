package com.example.sicc.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sicc.MainActivity;
import com.example.sicc.R;

public class LoginActivity extends AppCompatActivity {
    private TextView btn_lupaPassword;
    private EditText txt_email, txt_password;
    private Button btn_login;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_lupaPassword = findViewById(R.id.lupa_password);

        btn_login.setOnClickListener(v-> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });

        btn_lupaPassword.setOnClickListener(v-> {
            startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class));
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