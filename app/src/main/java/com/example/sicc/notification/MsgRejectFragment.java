package com.example.sicc.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sicc.R;
import com.example.sicc.fragments.DospemFragment;

public class MsgRejectFragment extends Fragment {
    private Button btn_dospem;
    private SharedPreferences sharedPreferences;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_msg_reject, container, false);

        init();

        return view;
    }

    private void init() {
        btn_dospem = view.findViewById(R.id.btn_redirect);
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user_login", Context.MODE_PRIVATE);

        String status_pengajuan = sharedPreferences.getString("status_pengajuan", "-");

        if (status_pengajuan.equals("Decline Dospem")) {
            btn_dospem.setVisibility(View.VISIBLE);
            btn_dospem.setText("Cari Dospem Baru");

            btn_dospem.setOnClickListener(v-> {
                requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container_progress, DospemFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            });
        } else if (status_pengajuan.equals("Decline Judul")) {
            btn_dospem.setVisibility(View.VISIBLE);
            btn_dospem.setText("Upload Judul Baru");
        } else if (status_pengajuan.equals("Decline Proposal")) {
            btn_dospem.setVisibility(View.VISIBLE);
            btn_dospem.setText("Upload Proposal Baru");
        }
    }
}