package com.example.sicc.activity_details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.activities.MainActivity;
import com.example.sicc.fragments.SettingFragment;

public class InformasiAplikasiActivity extends AppCompatActivity {
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_aplikasi);

        init();
    }

    private void init() {
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(v-> {
            finish();
            Animatoo.INSTANCE.animateSlideRight(this);
        });
    }
}