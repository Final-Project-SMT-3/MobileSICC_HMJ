package com.example.sicc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sicc.R;
import com.example.sicc.adapters.DospemAdapter;
import com.example.sicc.models.Dospem;

import java.util.ArrayList;

public class DospemFragment extends Fragment {
    private RecyclerView recyclerView;
    private DospemAdapter adapter;
    private ArrayList<Dospem> arrayList;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dospem, container, false);

        init();

        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerView_Dospem);

        addData();

        adapter = new DospemAdapter(requireContext(), arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void addData() {
        arrayList = new ArrayList<>();
        arrayList.add(new Dospem(1, "Bima", 234623874, "Tersedia"));
        arrayList.add(new Dospem(2, "Bima", 234623874, "Penuh"));
        arrayList.add(new Dospem(3, "Bima", 234623874, "Tersedia"));
        arrayList.add(new Dospem(4, "Bima", 234623874, "Tersedia"));
        arrayList.add(new Dospem(5, "Bima", 234623874, "Penuh"));
        arrayList.add(new Dospem(6, "Bima", 234623874, "Tersedia"));
        arrayList.add(new Dospem(7, "Bima", 234623874, "Tersedia"));
    }
}