package com.example.sicc.activity_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.activities.MainActivity;
import com.example.sicc.adapters.AnggotaAdapter;
import com.example.sicc.models.Anggota;
import com.example.sicc.models.Lomba;

import java.util.ArrayList;

public class DetailProfileActivity extends AppCompatActivity {
    private ImageView btn_back;
    private RecyclerView recyclerView;
    private AnggotaAdapter adapter;
    private ArrayList<Anggota> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);

        addData();

        init();
    }

    private void init() {
        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recyclerView_Anggota);

        adapter = new AnggotaAdapter(arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btn_back.setOnClickListener(v-> {
            finish();
            Animatoo.INSTANCE.animateSlideRight(this);
        });
    }

    private void addData() {
        arrayList = new ArrayList<>();
        arrayList.add(new Anggota("Zarif Qomarudi", "TIF - SMT 3", "21 Februari 2023"));
        arrayList.add(new Anggota("Bima Bimo Binjar", "TIF - SMT 3","21 Maret 2023"));
        arrayList.add(new Anggota("Lutfi Hakim", "TIF - SMT 3","21 Juli 2023"));
        arrayList.add(new Anggota("Adi Hendra", "TIF - SMT 3","21 Juli 2023"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.INSTANCE.animateSlideRight(this);
    }
}