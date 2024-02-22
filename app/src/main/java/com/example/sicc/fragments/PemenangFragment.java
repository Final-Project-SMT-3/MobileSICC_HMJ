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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicc.R;
import com.example.sicc.adapters.CapsuleAdapter;
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

public class PemenangFragment extends Fragment {
    private RecyclerView recyclerView;
    private CapsuleAdapter adapter;
    private ArrayList<Lomba> arrayList;
    private View view;
    private LoadingMain loadingMain;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pemenang, container, false);

        init();

        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerView_Menu);
        loadingMain = new LoadingMain(requireActivity());
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
                        getDataLomba();
                    }
                }, 500);
            }
        });
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

                        arrayList.add(lomba);
                    }


                    adapter = new CapsuleAdapter(getContext(), arrayList);
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
        });

        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
        queue.add(request);
    }
}