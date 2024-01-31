package com.example.sicc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.adapters.LombaAdapter;
import com.example.sicc.adapters.LombaSearchAdapter;
import com.example.sicc.models.Constant;
import com.example.sicc.models.Lomba;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchLombaActivity extends AppCompatActivity {
    private ImageView btn_back;
    private RecyclerView recyclerView;
    private LombaSearchAdapter adapter;
    private ArrayList<Lomba> arrayList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lomba);

        init();
    }

    private void init() {
        btn_back = findViewById(R.id.btn_back);
        searchView = findViewById(R.id.SearchViewLomba);
        searchView.clearFocus();
        recyclerView = findViewById(R.id.recyclerView_Lomba);

        getDataLomba();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                return false;
            }
        });

        btn_back.setOnClickListener(v-> {
            finish();
            Animatoo.INSTANCE.animateSlideRight(this);
        });
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


                    adapter = new LombaSearchAdapter(this, arrayList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                } else {
                    // Handle the case when the response indicates an error

                    Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                // Handle the case when there's a JSON parsing error

                Toast.makeText(this.getApplicationContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            // Handle the case when there's a network error

            Toast.makeText(this.getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        queue.add(request);
    }

    private void filterResults(String searchText) {
        adapter.getFilter().filter(searchText);
    }
}