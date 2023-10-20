package com.example.sicc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sicc.R;
import com.example.sicc.adapters.LombaAdapter;
import com.example.sicc.models.Lomba;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private LombaAdapter adapter;
    private ArrayList<Lomba> arrayList;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        addData();

        recyclerView = view.findViewById(R.id.recyclerView_Lomba);
        adapter = new LombaAdapter(arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void addData() {
        arrayList = new ArrayList<>();
        arrayList.add(new Lomba("PKM", "PKM-C (Teknologi)"));
        arrayList.add(new Lomba("KMIPN", "Hackathon"));
        arrayList.add(new Lomba("PILMAPRES", "Pilmapres"));
    }
}