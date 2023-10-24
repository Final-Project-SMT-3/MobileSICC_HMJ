package com.example.sicc.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sicc.R;
import com.example.sicc.adapters.LombaAdapter;
import com.example.sicc.models.Lomba;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private TextView txt_name, txt_ketua;
    private RecyclerView recyclerView;
    private LombaAdapter adapter;
    private ArrayList<Lomba> arrayList;
    private View view;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        init();

        return view;
    }

    private void init() {
        addData();

        txt_name = view.findViewById(R.id.txt_name);
        txt_ketua = view.findViewById(R.id.nama_ketua_kelompok);
        recyclerView = view.findViewById(R.id.recyclerView_Lomba);
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        getData();

        adapter = new LombaAdapter(getContext(), arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private String formatName(String name) {
        String[] words = name.split(" ");
        if (words.length >= 2) {
            return words[0] + " " + words[1];
        } else {
            return name;
        }
    }

    private void getData() {
        // "Guest" is the default value to be returned if the key "name" is not found in shared preferences
        String namaUser = sharedPreferences.getString("name", "Guest");
        String nama= formatName(namaUser);

        txt_name.setText(nama + " 👋");
        txt_ketua.setText(nama);
    }

    private void addData() {
        arrayList = new ArrayList<>();
        arrayList.add(new Lomba("PKM", "PKM-C (Teknologi)", "21 Februari 2023", R.string.desc_lomba));
        arrayList.add(new Lomba("KMIPN", "Hackathon","21 Maret 2023", R.string.desc_lomba));
        arrayList.add(new Lomba("PILMAPRES", "Pilmapres","21 Juli 2023", R.string.desc_lomba));
    }
}