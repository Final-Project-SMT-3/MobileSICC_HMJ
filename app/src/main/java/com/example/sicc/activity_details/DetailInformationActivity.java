package com.example.sicc.activity_details;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.activities.MainActivity;
import com.example.sicc.models.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailInformationActivity extends AppCompatActivity {
    private int id_lomba = 0;
    private static int lombaPosition = 0;
    private ImageView btn_back;
    private Button btn_close;
    private TextView judul, jenis, status, tgl_mulai, tgl_selesai, deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_information);

        init();
    }

    private void init() {
        btn_back = findViewById(R.id.btn_back);
        btn_close = findViewById(R.id.btn_close);

        id_lomba = getIntent().getIntExtra("id_lomba", 0);
        lombaPosition = getIntent().getIntExtra("lombaPosition", -1);

        judul = findViewById(R.id.title_lomba);
        jenis = findViewById(R.id.subTitle_lomba);
        status = findViewById(R.id.status_lomba);
        deskripsi = findViewById(R.id.desc_text);
        tgl_mulai = findViewById(R.id.tgl_mulai);
        tgl_selesai = findViewById(R.id.tgl_selesai);

        getDetailLomba();

        btn_back.setOnClickListener(v-> {
            startActivity(new Intent(DetailInformationActivity.this, MainActivity.class));
            Animatoo.INSTANCE.animateSlideRight(this);
            finish();
        });

        btn_close.setOnClickListener(v-> {
            startActivity(new Intent(DetailInformationActivity.this, MainActivity.class));
            Animatoo.INSTANCE.animateSlideRight(this);
            finish();
        });
    }

    private void getDetailLomba() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.DETAIL_LOMBA, response -> {
            // Handle the response
            Log.d("Response", response);
            try {
                JSONObject res = new JSONObject(response);

                int status_code = res.getInt("status_code");
                String message =  res.getString("message");

                if (status_code == 200 && message.equals("Success")) {
                    JSONObject detailLomba = res.getJSONObject("response");

                    judul.setText(detailLomba.getString("nama_lomba"));
                    jenis.setText(detailLomba.getString("info"));
                    status.setText(detailLomba.getString("status"));
                    deskripsi.setText(detailLomba.getString("detail_lomba"));
                    tgl_mulai.setText(detailLomba.getString("tanggal_mulai"));
                    tgl_selesai.setText(detailLomba.getString("tanggal_akhir"));

                } else {
                    // Handle the case when the response indicates an error
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                // Handle the case when there's a JSON parsing error
                Toast.makeText(getApplicationContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            // Handle the case when there's a network error
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }) {
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
                params.put("id_lomba", id_lomba + "");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}