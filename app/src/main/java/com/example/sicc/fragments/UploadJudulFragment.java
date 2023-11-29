package com.example.sicc.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.sicc.notification.MsgWaitingFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UploadJudulFragment extends Fragment {
    private String id_dosen, id_pj;
    private CardView cardReview;
    private TextView txt_nama_dosen, txt_review_dospem, title_review, tgl_pengajuan;
    private EditText txt_judul;
    private Button btn_pengajuan;
    private LoadingMain loadingMain;
    private SharedPreferences sharedPreferences;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_upload_judul, container, false);

        init();

        return view;
    }

    private void init() {
        txt_nama_dosen = view.findViewById(R.id.txt_nama_dospem);
        txt_judul = view.findViewById(R.id.editText_input);
        title_review = view.findViewById(R.id.title_review);
        txt_review_dospem = view.findViewById(R.id.txt_review_dospem);
        tgl_pengajuan = view.findViewById(R.id.tgl_pengajuan);
        cardReview = view.findViewById(R.id.layout_review);
        btn_pengajuan = view.findViewById(R.id.btn_ajukan_judul);

        loadingMain = new LoadingMain(requireContext());
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user_login", Context.MODE_PRIVATE);

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDetailJudul();
            }
        }, 500);

        btn_pengajuan.setOnClickListener(v-> {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pengajuanJudul();
                }
            }, 500);
        });
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

    public String formatDate(String inputDate) {
        // Define the input date format
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Define the output date format
        SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM yyyy", new Locale("id", "ID"));

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate;
        }
    }

    private void getDetailJudul() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.DETAIL_JUDUL, response -> {
            try {
                JSONObject res = new JSONObject(response);

                int status_code = res.getInt("status_code");
                String message = res.getString("message");

                if (status_code == 200 && message.equals("Success")) {
                    JSONObject dataJudul = res.getJSONObject("response");

                    id_dosen = dataJudul.getString("id_dospem");
                    id_pj = dataJudul.getString("id_pengajuan_judul");
                    String nama_dosen = dataJudul.getString("nama_dospem");
                    String judul = dataJudul.getString("judul");
                    String review = dataJudul.getString("review");
                    String tanggal_pengajuan = dataJudul.getString("submit_date");
                    String status_judul = dataJudul.getString("status_judul");

                    if (status_judul.equals("Revision")) {
                        title_review.setVisibility(View.VISIBLE);
                        cardReview.setVisibility(View.VISIBLE);
                        btn_pengajuan.setText("Ajukan Revisi Judul");

                        txt_nama_dosen.setText(formatDosen(nama_dosen));
                        txt_review_dospem.setText(review);
                        txt_judul.setText(judul);
                        tgl_pengajuan.setText("Pengajuan : " + formatDate(tanggal_pengajuan));

                        btn_pengajuan.setOnClickListener(v-> {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pengajuanRevisiJudul();
                                }
                            }, 500);
                        });
                    }

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
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void pengajuanJudul() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PENGAJUAN_JUDUL, response -> {
            try {
                JSONObject res = new JSONObject(response);

                int status_code = res.getInt("status_code");
                String message = res.getString("message");

                if (status_code == 200 && message.equals("Success")) {
                    String detail_message = res.getString("response");

                    Toast.makeText(getContext(), detail_message, Toast.LENGTH_SHORT).show();

                    // Redirect Into Waiting
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.fragment_container_progress, MsgWaitingFragment.class, null);
                    transaction.addToBackStack(null);
                    transaction.commit();

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
            String judul = txt_judul.getText().toString();
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
                params.put("id_dospem", id_dosen);
                params.put("judul", judul);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void pengajuanRevisiJudul() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PENGAJUAN_REVISI_JUDUL, response -> {
            try {
                JSONObject res = new JSONObject(response);

                int status_code = res.getInt("status_code");
                String message = res.getString("message");

                if (status_code == 200 && message.equals("Success")) {
                    String detail_message = res.getString("response");

                    Toast.makeText(getContext(), detail_message, Toast.LENGTH_SHORT).show();

                    // Redirect Into Waiting
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.fragment_container_progress, MsgWaitingFragment.class, null);
                    transaction.addToBackStack(null);
                    transaction.commit();

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
            String judul = txt_judul.getText().toString();
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
                params.put("id_pj", id_pj);
                params.put("judul", judul);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}