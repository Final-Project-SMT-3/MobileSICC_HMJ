package com.example.sicc.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sicc.R;
import com.example.sicc.activities.MainActivity;
import com.example.sicc.adapters.LombaAdapter;
import com.example.sicc.models.Lomba;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CircleImageView photoProfile;
    private TextView txt_name, txt_ketua, txt_kelompok, txt_dospem, txt_lomba, txt_anggota;
    private RecyclerView recyclerView;
    private LombaAdapter adapter;
    private ArrayList<Lomba> arrayList;
    private View view;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        addData();

        init();

        return view;
    }

    private void init() {
        photoProfile = view.findViewById(R.id.image_profile);
        txt_name = view.findViewById(R.id.txt_name);
        txt_ketua = view.findViewById(R.id.nama_ketua_kelompok);
        txt_kelompok = view.findViewById(R.id.nama_kelompok);
        txt_lomba = view.findViewById(R.id.jenis_lomba);
        txt_anggota = view.findViewById(R.id.anggota_kelompok);
        txt_dospem = view.findViewById(R.id.nama_dospem);
        recyclerView = view.findViewById(R.id.recyclerView_Lomba);
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        getData();

        photoProfile.setOnClickListener(v-> {
            ((MainActivity) requireActivity()).setButtonActive(4);

            getFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.fragment_container, SettingFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        });

        adapter = new LombaAdapter(getContext(), arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private static String formatNama(String namaKetua) {
        String[] words = namaKetua.split(" ");
        if (words.length >= 2) {
            return words[0] + " " + words[1];
        } else {
            return namaKetua;
        }
    }

    private static int countStrings(String nimAnggota) {
        String[] splitStrings = nimAnggota.split(", ");
        return splitStrings.length;
    }

    public static String formatDosen(String namaDosen) {
        String[] nameParts = namaDosen.split(", "); // Split the name by comma and space
        if (nameParts.length >= 2) {
            String[] fullName = nameParts[0].split(" "); // Split the full name by space
            StringBuilder convertedName = new StringBuilder();

            // Append the first name
            if (fullName.length >= 1) {
                convertedName.append(fullName[0]);
            }

            // Append the second word of the name
            if (fullName.length >= 2) {
                convertedName.append(" ");
                convertedName.append(fullName[1]);
            }

            // Append the abbreviated middle name
            if (fullName.length > 2) {
                for (int i = 2; i < fullName.length; i++) {
                    convertedName.append(" ");
                    convertedName.append(fullName[i].charAt(0));
                    convertedName.append(".");
                }
            }

            // Append the rest of the name
            for (int i = 1; i < nameParts.length; i++) {
                convertedName.append(", ");
                convertedName.append(nameParts[i]);
            }

            return convertedName.toString();
        } else {
            // Handle invalid input
            return namaDosen;
        }
    }

    private void getData() {
        // "-" is the default value to be returned if the key "name" is not found in shared preferences
        String namaUser = sharedPreferences.getString("nama_ketua", "-");
        String namaKelompok = sharedPreferences.getString("nama_kelompok", "-");
        String namaLomba = sharedPreferences.getString("lomba", "-");
        String nimAnggota = sharedPreferences.getString("nim_kelompok", "-");
        String namaDospem = sharedPreferences.getString("dospem", "Belum Memilih Dospem");

        String nama = formatNama(namaUser);
        int anggota = countStrings(nimAnggota);
        String dospem = formatDosen(namaDospem);

        txt_name.setText(nama + " 👋");
        txt_kelompok.setText(namaKelompok);
        txt_ketua.setText(nama);
        txt_lomba.setText(namaLomba);
        txt_anggota.setText(String.valueOf(anggota) + " Anggota");
        txt_dospem.setText(dospem);
    }

    private void addData() {
        arrayList = new ArrayList<>();
        arrayList.add(new Lomba("PKM", "PKM-C (Teknologi)", "21 Februari 2023", R.string.desc_lomba));
        arrayList.add(new Lomba("KMIPN", "Hackathon","21 Maret 2023", R.string.desc_lomba));
        arrayList.add(new Lomba("PILMAPRES", "Pilmapres","21 Juli 2023", R.string.desc_lomba));
    }
}