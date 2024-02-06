package com.example.sicc.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.R;
import com.example.sicc.activity_details.DetailProfileActivity;
import com.example.sicc.activity_details.InformasiAplikasiActivity;
import com.example.sicc.adapters.LoadingMain;
import com.example.sicc.authentication.LoginActivity;
import com.example.sicc.models.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingFragment extends Fragment {
    private TextView txt_nama;
    private Button ya, tidak;
    private LinearLayout btn_profile, btn_info, btn_logout;
    private SharedPreferences sharedPreferences, sharedPreferencesDataLogin;
    private Dialog dialog;
    private LoadingMain loadingMain;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);

        init();

        return view;
    }

    private void init() {
        txt_nama = view.findViewById(R.id.txt_nama);
        btn_profile = view.findViewById(R.id.btn_profile_kelompok);
        btn_info = view.findViewById(R.id.btn_info_aplikasi);
        btn_logout = view.findViewById(R.id.btn_logout);
        loadingMain = new LoadingMain(requireActivity());
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        sharedPreferencesDataLogin = getContext().getApplicationContext().getSharedPreferences("user_login", Context.MODE_PRIVATE);

//        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                getDataUser();
            }
        }, 500);

        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_confirm_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ya = dialog.findViewById(R.id.btn_okay);
        tidak = dialog.findViewById(R.id.btn_cancel);

        buttonFunction();
    }

//    private void getDataUser() {
//        // "-" is the default value to be returned if the key "name" is not found in shared preferences
//        int id_user = sharedPreferencesDataLogin.getInt("id_user", 0);
//
//        StringRequest request = new StringRequest(Request.Method.POST, Constant.DATA_USER, response -> {
//            try {
//                JSONObject res = new JSONObject(response);
//
//                int statusCode = res.getInt("status_code");
//                String message = res.getString("message");
//
//                if (statusCode == 200 && message.equals("Success")) {
//                    JSONObject userData = res.getJSONObject("response");
//
//                    String namaUser = userData.getString("nama");
//
//                    String nama = formatNama(namaUser);
//
//                    txt_nama.setText(nama);
//
//                    loadingMain.cancel();
//                } else {
//                    // Handle the case when the response indicates an error
//                    loadingMain.cancel();
//
//                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                // Handle the case when there's a JSON parsing error
//                e.printStackTrace();
//
//                loadingMain.cancel();
//
//                Toast.makeText(requireContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
//            }
//        }, error -> {
//            // Handle the case when there's a network error
//            error.printStackTrace();
//
//            loadingMain.cancel();
//
//            Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show();
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
//                return headers;
//            }
//
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("id_user", String.valueOf(id_user));
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
//        request.setRetryPolicy(new DefaultRetryPolicy(30000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(request);
//    }

    private void buttonFunction() {
        btn_profile.setOnClickListener(v-> {
            startActivity(new Intent(getContext(), DetailProfileActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(getContext());
        });

        btn_info.setOnClickListener(v-> {
            startActivity(new Intent(getContext(), InformasiAplikasiActivity.class));
            Animatoo.INSTANCE.animateSlideLeft(getContext());
        });

        btn_logout.setOnClickListener(v-> {
            dialog.show();

            ya.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    startActivity(new Intent(getContext(), LoginActivity.class));
                    Animatoo.INSTANCE.animateSlideLeft(getContext());
                    ((Activity) getContext()).finish();

                    dialog.dismiss();
                }
            });

            tidak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
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
}