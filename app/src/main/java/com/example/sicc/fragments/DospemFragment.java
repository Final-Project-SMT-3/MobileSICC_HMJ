package com.example.sicc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicc.R;
import com.example.sicc.adapters.DospemAdapter;
import com.example.sicc.adapters.LoadingMain;
import com.example.sicc.models.Constant;
import com.example.sicc.models.Dospem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DospemFragment extends Fragment {
    private RecyclerView recyclerView;
    private DospemAdapter adapter;
    private ArrayList<Dospem> arrayList;
    private View view;
    private LoadingMain loadingMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dospem, container, false);

        init();

        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerView_Dospem);
        loadingMain = new LoadingMain(requireActivity());

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataDospem();
            }
        }, 500);
    }

    private void getDataDospem() {
        arrayList = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, Constant.DOSEN_PEMBIMBING, response -> {
            try {
                JSONObject res = new JSONObject(response);

                int status_code = res.getInt("status_code");
                String message = res.getString("message");

                if (status_code == 200 && message.equals("Success")) {
                    JSONArray dataDospem = res.getJSONArray("response");

                    for (int i = 0; i < dataDospem.length(); i++) {
                        JSONObject objectDospem = dataDospem.getJSONObject(i);

                        Dospem dospem = new Dospem();

                        dospem.setId_dospem(objectDospem.getInt("id"));
                        dospem.setNama_dosen(objectDospem.getString("nama"));
                        dospem.setNidn_dosen(objectDospem.getString("no_identitas"));
                        dospem.setStatus_dosen(objectDospem.getString("status_dosen"));

                        arrayList.add(dospem);
                    }

                    adapter = new DospemAdapter(requireContext(), arrayList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    loadingMain.cancel();
                } else {
                    loadingMain.cancel();

                    // Handle the case when the response indicates an error
                    Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                loadingMain.cancel();

                // Handle the case when there's a JSON parsing error
                Toast.makeText(getContext().getApplicationContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            loadingMain.cancel();

            // Handle the case when there's a network error
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