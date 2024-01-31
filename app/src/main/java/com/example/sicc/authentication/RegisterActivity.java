package com.example.sicc.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.adapters.LoadingDialog;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout layoutEmail, layoutUsername;
    private EditText txt_email, txt_username;
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
        layoutUsername = findViewById(R.id.username_input_layout);
        txt_email = findViewById(R.id.txt_email);
        txt_username = findViewById(R.id.txt_username);
        btn_register = findViewById(R.id.btn_register);
        txt_login = findViewById(R.id.txt_login);
        btn_login = findViewById(R.id.btn_login);

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
        if (txt_username.getText().toString().isEmpty()) {
            layoutUsername.setErrorEnabled(true);
            layoutUsername.setError("Username Tidak Boleh Kosong");
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

        return true;
    }

    private void register() {

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