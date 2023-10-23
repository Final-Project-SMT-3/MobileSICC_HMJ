package com.example.sicc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailInformationActivity extends AppCompatActivity {
    private ImageView btn_back;
    private Button btn_close;
    private TextView judul, jenis, tgl, deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_information);

        init();
    }

    private void init() {
        btn_back = findViewById(R.id.btn_back);
        btn_close = findViewById(R.id.btn_close);

        judul = findViewById(R.id.title_lomba);
        jenis = findViewById(R.id.subTitle_lomba);
        tgl = findViewById(R.id.tgl_lomba);
        deskripsi = findViewById(R.id.desc_text);


        btn_back.setOnClickListener(v-> {
            startActivity(new Intent(DetailInformationActivity.this, MainActivity.class));
            finish();
        });

        btn_close.setOnClickListener(v-> {
            startActivity(new Intent(DetailInformationActivity.this, MainActivity.class));
            finish();
        });
        
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            judul.setText(bundle.getString("judul"));
            jenis.setText(bundle.getString("sub_Judul"));
            tgl.setText(bundle.getString("tgl_Lomba"));
            deskripsi.setText(bundle.getInt("desc_Lomba"));
        }
    }
}