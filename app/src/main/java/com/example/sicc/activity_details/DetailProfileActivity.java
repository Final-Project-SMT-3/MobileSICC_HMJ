package com.example.sicc.activity_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.activities.MainActivity;
import com.example.sicc.adapters.AnggotaAdapter;
import com.example.sicc.adapters.LoadingMain;
import com.example.sicc.authentication.LoginActivity;
import com.example.sicc.models.Anggota;
import com.example.sicc.models.Constant;
import com.example.sicc.models.Lomba;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailProfileActivity extends AppCompatActivity {
    private TextView txt_nama, txt_dospem, txt_lomba;
    private ImageView btn_back;
    private RecyclerView recyclerView;
    private AnggotaAdapter adapter;
    private ArrayList<Anggota> arrayList;
    private SharedPreferences sharedPreferences;
    private LoadingMain loadingMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);

        init();
    }

    private void init() {
        txt_nama = findViewById(R.id.txt_nama);
        txt_dospem = findViewById(R.id.txt_dosen_pembimbing);
        txt_lomba = findViewById(R.id.txt_jenis_lomba);
        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recyclerView_Anggota);
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        loadingMain = new LoadingMain(this);

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 500);

        btn_back.setOnClickListener(v-> {
            finish();
            Animatoo.INSTANCE.animateSlideRight(this);
        });
    }

    private String[] getDataParams() {
        String username = sharedPreferences.getString("user", "-");
        String password = sharedPreferences.getString("pass", "-");

        String[] result = {username, password};
        return result;
    }

    private static String formatNama(String namaKetua) {
        String[] words = namaKetua.split(" ");
        if (words.length >= 2) {
            return words[0] + " " + words[1];
        } else {
            return namaKetua;
        }
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
        arrayList = new ArrayList<>();

        // Calling Method "getDataParams" Into Parameter API
        String[] dataParams = getDataParams();
        String username = dataParams[0];
        String password = dataParams[1];

        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN, response -> {
            Log.d("Response", response);
            try {
                JSONObject res = new JSONObject(response);

                int statusCode = res.getInt("status_code");
                String message = res.getString("message");

                if (statusCode == 200 && message.equals("Success")) {
                    JSONObject userData = res.getJSONObject("response");

                    // Data Detail Kelompok
                    String nama = formatNama(userData.getString("nama"));
                    String lomba = userData.getString("nama_lomba");
                    String dospem = formatDosen(userData.getString("nama_dospem"));

                    txt_nama.setText(nama);
                    txt_lomba.setText(lomba);
                    txt_dospem.setText(dospem != null ? dospem : "Belum Memilih Dospem");

                    // Data Anggota Kelompok
                    String nimAnggota = userData.getString("nim_anggota");
                    String namaAnggota = userData.getString("nama_anggota");

                    // Split Data Into Array Element's
                    String[] nimArray = nimAnggota.split(", ");
                    String[] namaArray = namaAnggota.split(", ");

                    for (int i = 0; i < nimArray.length; i++) {
                        Anggota anggota = new Anggota();
                        anggota.setNim_anggota(nimArray[i]);
                        anggota.setNama_anggota(namaArray[i]);
                        arrayList.add(anggota);
                    }

                    loadingMain.cancel();

                    adapter = new AnggotaAdapter(getApplicationContext(), arrayList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                } else {
                    loadingMain.cancel();

                    // Handle the case when the response indicates an error
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                loadingMain.cancel();

                // Handle the case when there's a JSON parsing error
                Toast.makeText(getApplicationContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            loadingMain.cancel();

            // Handle the case when there's a network error
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
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
                params.put("password", password);
                params.put("username", username);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.INSTANCE.animateSlideRight(this);
    }
}