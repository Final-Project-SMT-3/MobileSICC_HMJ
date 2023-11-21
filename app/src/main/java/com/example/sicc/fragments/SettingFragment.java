package com.example.sicc.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.activity_details.DetailProfileActivity;
import com.example.sicc.activity_details.InformasiAplikasiActivity;
import com.example.sicc.authentication.LoginActivity;

public class SettingFragment extends Fragment {
    private TextView txt_nama;
    private Button ya, tidak;
    private LinearLayout btn_profile, btn_info, btn_logout;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);

        init();

        return view;
    }

    private void init() {
        txt_nama = view.findViewById(R.id.txt_nama);
        btn_profile = view.findViewById(R.id.btn_profile_kelompok);
        btn_info = view.findViewById(R.id.btn_info_aplikasi);
        btn_logout = view.findViewById(R.id.btn_logout);
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_confirm_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ya = dialog.findViewById(R.id.btn_okay);
        tidak = dialog.findViewById(R.id.btn_cancel);

        getData();

        buttonFunction();
    }

    private void getData() {
        // "-" is the default value to be returned if the key "name" is not found in shared preferences
        String namaUser = sharedPreferences.getString("nama_ketua", "-");

        String nama = formatNama(namaUser);

        txt_nama.setText(nama);
    }

    private void buttonFunction() {
        btn_profile.setOnClickListener(v-> {
            startActivity(new Intent(getContext(), DetailProfileActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(getContext());
        });

        btn_info.setOnClickListener(v-> {
            startActivity(new Intent(getContext(), InformasiAplikasiActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(getContext());
        });

        btn_logout.setOnClickListener(v-> {
            dialog.show();

            ya.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    Animatoo.INSTANCE.animateSlideLeft(getContext());
                    ((Activity) getContext()).finish();

                    dialog.dismiss();
                }
            });

            tidak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        });
    }

    private static String formatNama(String namaKetua) {
        String[] words = namaKetua.split(" ");
        if (words.length >= 2) {
            return words[0] + " " + words[1];
        } else {
            return namaKetua;
        }
    }
}