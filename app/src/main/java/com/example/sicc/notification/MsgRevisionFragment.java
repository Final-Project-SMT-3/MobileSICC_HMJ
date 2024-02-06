package com.example.sicc.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicc.R;
import com.example.sicc.fragments.DospemFragment;
import com.example.sicc.fragments.UploadJudulFragment;
import com.example.sicc.fragments.UploadProposalFragment;
import com.example.sicc.models.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MsgRevisionFragment extends Fragment {
    private Button btn_redirect;
    private SharedPreferences sharedPreferences;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_msg_revision, container, false);

        init();

        return view;
    }

    private void init() {
        btn_redirect = view.findViewById(R.id.btn_redirect);
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user_login", Context.MODE_PRIVATE);

//        getStatus_Pengajuan();
    }

//    private void getStatus_Pengajuan() {
//        // "-" is the default value to be returned if the key "name" is not found in shared preferences
//        int id_user = sharedPreferences.getInt("id_user", 0);
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
//                    String status_pengajuan = res.getString("status");
//                    String status_p_dospem = userData.getString("status_dospem");
//                    String status_p_judul = userData.getString("status_judul");
//                    String status_p_proposal = userData.getString("status_proposal");
//
//                    // Setting Message And Redirect By Status Pengajuan
//                    setStatus_Message(status_pengajuan, status_p_judul, status_p_proposal);
//
//                } else {
//                    // Handle the case when the response indicates an error
//                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                // Handle the case when there's a JSON parsing error
//                e.printStackTrace();
//
//                Toast.makeText(requireContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
//            }
//        }, error -> {
//            // Handle the case when there's a network error
//            error.printStackTrace();
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

    private void setStatus_Message(String status_p, String p_judul, String p_proposal) {
        if (status_p.equals("Revision Judul") && p_judul.equals("Revision")) {
            btn_redirect.setVisibility(View.VISIBLE);
            btn_redirect.setText("Lihat Review Dospem");

            btn_redirect.setOnClickListener(v-> {
                requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container_progress, UploadJudulFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            });
        } else if (status_p.equals("Revision Proposal") && p_proposal.equals("Revision")) {
            btn_redirect.setVisibility(View.VISIBLE);
            btn_redirect.setText("Lihat Review Dospem");

            btn_redirect.setOnClickListener(v-> {
                requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container_progress, UploadProposalFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
}