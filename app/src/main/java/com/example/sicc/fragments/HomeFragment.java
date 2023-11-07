package com.example.sicc.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicc.R;
import com.example.sicc.activities.MainActivity;
import com.example.sicc.adapters.LoadingMain;
import com.example.sicc.adapters.LombaAdapter;
import com.example.sicc.models.Constant;
import com.example.sicc.models.Lomba;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CircleImageView photoProfile;
    private TextView txt_name, txt_ketua, txt_kelompok, txt_dospem, txt_lomba, txt_anggota;
    private RecyclerView recyclerView;
    private LombaAdapter adapter;
    private ArrayList<Lomba> arrayList;
    private View view;
    private LoadingMain loadingMain;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

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
        loadingMain = new LoadingMain(requireActivity());

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataUser();
                getDataLomba();
            }
        }, 500);

        photoProfile.setOnClickListener(v-> {
            ((MainActivity) requireActivity()).setButtonActive(4);

            getFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.fragment_container, SettingFragment.class, null)
                    .addToBackStack(null)
                    .commit();
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

    private void getDataUser() {
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

    private void getDataLomba() {
        arrayList = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, Constant.LOMBA, response -> {
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200 && message.equals("Success")) {
                    JSONArray dataLomba = res.getJSONArray("response");

                    for (int i = 0; i < dataLomba.length(); i++) {
                        JSONObject objectLomba = dataLomba.getJSONObject(i);

                        Lomba lomba = new Lomba();

                        lomba.setId_lomba(objectLomba.getInt("id"));
                        lomba.setNama_lomba(objectLomba.getString("nama_lomba"));
                        lomba.setFoto_lomba(objectLomba.getJSONArray("detailLomba").getJSONObject(0).optString("foto", "-"));
                        lomba.setJenis_lomba(objectLomba.getJSONArray("detailPelaksanaan").
                                getJSONObject(0).optString("info", "-"));

                        arrayList.add(lomba);
                    }


                    adapter = new LombaAdapter(getContext(), arrayList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    loadingMain.cancel();
                } else {
                    // Handle the case when the response indicates an error

                    loadingMain.cancel();

                    Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                // Handle the case when there's a JSON parsing error

                loadingMain.cancel();

                Toast.makeText(getContext().getApplicationContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            // Handle the case when there's a network error

            loadingMain.cancel();

            Toast.makeText(getContext().getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
        queue.add(request);
    }
}