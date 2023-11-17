package com.example.sicc.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicc.R;
import com.example.sicc.adapters.LoadingMain;
import com.example.sicc.models.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PengajuanDospemFragment extends Fragment {
    private int id_dospem = 0;
    private ImageView btn_back;
    private TextView nama_dosen, email_dosen, total_pengajuan, total_diterima;
    private Button btn_pengajuan;
    private CheckBox btn_check;
    private Bundle bundle;
    private LoadingMain loadingMain;
    private View view;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pengajuan_dospem, container, false);

        init();

        return view;
    }

    private void init() {
        btn_back = view.findViewById(R.id.btn_back);
        bundle = getArguments();

        id_dospem = bundle.getInt("id_dosen", 0);

        nama_dosen = view.findViewById(R.id.txt_nama);
        email_dosen = view.findViewById(R.id.txt_email);
        total_pengajuan = view.findViewById(R.id.total_pengajuan);
        total_diterima = view.findViewById(R.id.total_diterima);
        btn_pengajuan = view.findViewById(R.id.btn_pengajuan);
        btn_check = view.findViewById(R.id.check_permission);
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        loadingMain = new LoadingMain(requireContext());

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataDospem();
            }
        }, 500);

        btn_back.setOnClickListener(v-> {
            requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.fragment_container_progress, DospemFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        });

        btn_pengajuan.setOnClickListener(v-> {
            if (!btn_check.isChecked()) {
                Toast.makeText(requireContext(), "Mohon Membaca Dan Menyetujui Kondisi", Toast.LENGTH_SHORT).show();
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pengajuanDospem();
                    }
                }, 500);
            }
        });
    }

    private void getDataDospem() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.DETAIL_DOSEN_PEMBIMBING, response -> {
            Log.d("Response", response);

            try {
                JSONObject res = new JSONObject(response);

                int status_code = res.getInt("status_code");
                String message = res.getString("message");

                if (status_code == 200 && message.equals("Success")) {
                    JSONObject detailDospem = res.getJSONObject("response");

                    nama_dosen.setText(detailDospem.getString("nama"));
                    email_dosen.setText(detailDospem.getString("email"));

                    loadingMain.cancel();
                } else {
                    loadingMain.cancel();

                    // Handle the case when the response indicates an error
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                loadingMain.cancel();

                // Handle the case when there's a JSON parsing error
                Toast.makeText(getContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            loadingMain.cancel();

            // Handle the case when there's a network error
            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_dosen", id_dospem + "");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void pengajuanDospem() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PENGAJUAN_DOSPEM, response -> {
            Log.d("Response", response);

            try {
                JSONObject res = new JSONObject(response);

                int status_code = res.getInt("status_code");
                String message = res.getString("message");

                if (status_code == 200 && message.equals("Success")) {
                    String detail_message = res.getString("response");

                    Toast.makeText(getContext(), detail_message, Toast.LENGTH_SHORT).show();

                    loadingMain.cancel();
                } else {
                    loadingMain.cancel();

                    // Handle the case when the response indicates an error
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                loadingMain.cancel();

                // Handle the case when there's a JSON parsing error
                Toast.makeText(getContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            loadingMain.cancel();

            // Handle the case when there's a network error
            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }) {
            // Get User Login Id
            String id_mahasiswa = String.valueOf(sharedPreferences.getInt("id_user", 0));

            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_mhs", id_mahasiswa);
                params.put("id_dosen", String.valueOf(id_dospem));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}