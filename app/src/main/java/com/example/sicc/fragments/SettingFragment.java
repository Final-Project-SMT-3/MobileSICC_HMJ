package com.example.sicc.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.activity_details.InformasiAplikasiActivity;
import com.example.sicc.authentication.LoginActivity;

public class SettingFragment extends Fragment {
    private LinearLayout btn_profile, btn_info, btn_logout;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);

        init();

        return view;
    }

    private void init() {
        btn_profile = view.findViewById(R.id.btn_profile_kelompok);
        btn_info = view.findViewById(R.id.btn_info_aplikasi);
        btn_logout = view.findViewById(R.id.btn_logout);

        btn_info.setOnClickListener(v-> {
            startActivity(new Intent(getContext(), InformasiAplikasiActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(getContext());
        });

        btn_logout.setOnClickListener(v-> {
            startActivity(new Intent(getContext(), LoginActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(getContext());
            ((Activity) getContext()).finish();
        });
    }
}