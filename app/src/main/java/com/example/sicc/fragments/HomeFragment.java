package com.example.sicc.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
    private SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataUser();
                getDataLomba();
            }
        }, 500);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingMain.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataUser();
                        getDataLomba();
                    }
                }, 500);
            }
        });

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
        swipeRefreshLayout.setRefreshing(true);
        // "-" is the default value to be returned if the key "name" is not found in shared preferences
        int id_user = sharedPreferences.getInt("id_user", 0);

        StringRequest request = new StringRequest(Request.Method.POST, Constant.DATA_USER, response -> {
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200 && message.equals("Success")) {
                    JSONObject userData = res.getJSONObject("response");

                    // Share Preferences User After Login
                    SharedPreferences userPref = requireContext().getSharedPreferences("user_login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putInt("id_user", userData.getInt("id"));
                    editor.putString("status_pengajuan", res.getString("status"));
                    editor.putString("status_p_dospem", userData.getString("status_dospem"));
                    editor.putString("status_p_judul", userData.getString("status_judul"));
                    editor.putString("status_p_proposal", userData.getString("status_proposal"));
                    editor.apply();

                    String namaUser = userData.getString("nama");
                    String namaKelompok = userData.getString("nama_kelompok");
                    String namaLomba = userData.getString("nama_lomba");
                    String nimAnggota = userData.getString("nim_anggota");
                    String namaDospem = userData.getString("nama_dospem");

                    if (namaDospem.equals("null")) {
                        txt_dospem.setText("Belum Memilih Dospem");
                    } else if (!namaDospem.equals("null") && res.getString("status").equals("Decline Dospem")) {
                        txt_dospem.setText("Belum Memilih Dospem");
                    } else if (!namaDospem.equals("null")) {
                        String dospem = formatDosen(namaDospem);
                        txt_dospem.setText(dospem);
                    }

                    String nama = formatNama(namaUser);
                    int anggota = countStrings(nimAnggota);

                    txt_name.setText(nama + " 👋");
                    txt_kelompok.setText(namaKelompok);
                    txt_ketua.setText(nama);
                    txt_lomba.setText(namaLomba);
                    txt_anggota.setText(anggota + " Anggota");

                    loadingMain.cancel();
                } else {
                    // Handle the case when the response indicates an error
                    loadingMain.cancel();

                    swipeRefreshLayout.setRefreshing(false);

                    Toast.makeText(requireContext(), "Login Gagal : " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // Handle the case when there's a JSON parsing error
                e.printStackTrace();

                loadingMain.cancel();

                swipeRefreshLayout.setRefreshing(false);

                Toast.makeText(requireContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }

            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
            // Handle the case when there's a network error
            error.printStackTrace();

            loadingMain.cancel();

            swipeRefreshLayout.setRefreshing(false);

            Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
                return headers;
            }

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_user", String.valueOf(id_user));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        request.setRetryPolicy(new DefaultRetryPolicy(30000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private void getDataLomba() {
        arrayList = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);

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

                    swipeRefreshLayout.setRefreshing(false);

                    Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                // Handle the case when there's a JSON parsing error

                loadingMain.cancel();

                swipeRefreshLayout.setRefreshing(false);

                Toast.makeText(getContext().getApplicationContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }

            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
            error.printStackTrace();

            // Handle the case when there's a network error

            loadingMain.cancel();

            swipeRefreshLayout.setRefreshing(false);

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