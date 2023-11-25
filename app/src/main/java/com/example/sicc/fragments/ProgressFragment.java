package com.example.sicc.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicc.R;
import com.example.sicc.adapters.LoadingMain;
import com.example.sicc.models.Constant;
import com.example.sicc.notification.MsgFinalSuccesFragment;
import com.example.sicc.notification.MsgNotRecognitionFragment;
import com.example.sicc.notification.MsgRejectFragment;
import com.example.sicc.notification.MsgRevisionFragment;
import com.example.sicc.notification.MsgSuccesFragment;
import com.example.sicc.notification.MsgWaitingFragment;
import com.shuhart.stepview.StepView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProgressFragment extends Fragment {
    private StepView stepView;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadingMain loadingMain;
    private  String status_pengajuan, status_p_dospem, status_p_judul, status_p_proposal;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_progress, container, false);

        init();

        return view;
    }

    private void init() {
        stepView = view.findViewById(R.id.step_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        loadingMain = new LoadingMain(requireActivity());
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user_login", Context.MODE_PRIVATE);

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getStatus_Pengajuan();
            }
        }, 500);

        // Handle step click events
        stepView.setOnStepClickListener(step -> {
            if (step != stepView.getCurrentStep()) {
                switch (step) {
                    case 0:
                        replaceFragment(new MsgSuccesFragment());
                        stepView.done(true);
                        break;
                    case 1:
                        switch (status_p_dospem) {
                            case "Accept":
                                replaceFragment(new MsgSuccesFragment());
                                stepView.done(true);
                                break;
                            case "Waiting Approval":
                                replaceFragment(new MsgWaitingFragment());
                                stepView.done(false);
                                break;
                            case "Decline":
                                replaceFragment(new MsgRejectFragment());
                                stepView.done(false);
                                break;
                            default:
                                replaceFragment(new DospemFragment());
                                stepView.done(false);
                                break;
                        }
                        break;
                    case 2:
                        switch (status_p_judul) {
                            case "Accept":
                                replaceFragment(new MsgSuccesFragment());
                                stepView.done(true);
                                break;
                            case "Waiting Approval":
                                replaceFragment(new MsgWaitingFragment());
                                stepView.done(false);
                                break;
                            case "Revision":
                                replaceFragment(new MsgRevisionFragment());
                                stepView.done(false);
                                break;
                            case "Decline":
                                replaceFragment(new MsgRejectFragment());
                                stepView.done(false);
                                break;
                            case "null":
                                replaceFragment(new MsgNotRecognitionFragment());
                                stepView.done(false);
                                break;
                            default:
                            replaceFragment(new UploadJudulFragment());
                            stepView.done(false);
                            break;
                        }
                        break;
                    case 3:
                        switch (status_p_proposal) {
                            case "Accept":
                                replaceFragment(new MsgSuccesFragment());
                                stepView.done(true);
                                break;
                            case "Waiting Approval":
                                replaceFragment(new MsgWaitingFragment());
                                stepView.done(false);
                                break;
                            case "Revision":
                                replaceFragment(new MsgRevisionFragment());
                                stepView.done(false);
                                break;
                            case "Decline":
                                replaceFragment(new MsgRejectFragment());
                                stepView.done(false);
                                break;
                            default:
                                replaceFragment(new MsgNotRecognitionFragment());
                                stepView.done(false);
                                break;
                        }
                        break;
                    case 4:
                        switch (status_p_proposal) {
                            case "Accept":
                                replaceFragment(new MsgFinalSuccesFragment());
                                stepView.done(true);
                                break;
                            default:
                                replaceFragment(new MsgNotRecognitionFragment());
                                stepView.done(false);
                                break;
                        }
                        break;
                }

                stepView.go(step, true);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingMain.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingStepView();

                        getStatus_Pengajuan();
                    }
                }, 500);
            }
        });
    }

    private void settingStepView() {
        stepView.getState()
                .animationType(StepView.ANIMATION_ALL)
                .steps(new ArrayList<String>() {{
                    add("Team");
                    add("Dospem");
                    add("Judul");
                    add("Proposal");
                    add("Lolos");
                }})
                .stepsNumber(5)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .commit();

        // Set an initial step as selected
        stepView.go(0, true);
    }

    private void check_StepView(String status_pengajuan, String status_p_dospem,
                                String status_p_judul, String status_p_proposal) {

        // Check Condition Data Pengajuan Dosen Pembimbing
        if (status_pengajuan.equals("Belum Memilih Dosen Pembimbing.")) {
            replaceFragment(new DospemFragment());
            stepView.go(1, true);
            stepView.done(false);
        } else if (status_p_dospem.equals("Waiting Approval")) {
            replaceFragment(new MsgWaitingFragment());
            stepView.go(1, true);
            stepView.done(false);
        } else if (status_p_dospem.equals("Decline")) {
            replaceFragment(new MsgRejectFragment());
            stepView.go(1, true);
            stepView.done(false);
        } else if (status_p_dospem.equals("Accept")) {
            replaceFragment(new MsgSuccesFragment());
            stepView.go(1, true);
            stepView.done(true);
        } else if (status_p_dospem.equals("Accept") && status_pengajuan.equals("Belum Mengajukan Judul.")) {
            replaceFragment(new UploadJudulFragment());
            stepView.go(2, true);
            stepView.done(false);
        }

        // Check Condition Data Pengajuan Judul
        if (status_pengajuan.equals("Belum Mengajukan Judul.")) {
            replaceFragment(new UploadJudulFragment());
            stepView.go(2, true);
            stepView.done(false);
        } else if (status_p_judul.equals("Waiting Approval")) {
            replaceFragment(new MsgWaitingFragment());
            stepView.go(2, true);
            stepView.done(false);
        } else if (status_p_judul.equals("Revision")) {
            replaceFragment(new MsgRevisionFragment());
            stepView.go(2, true);
            stepView.done(false);
        } else if (status_p_judul.equals("Decline")) {
            replaceFragment(new MsgRejectFragment());
            stepView.go(2, true);
            stepView.done(false);
        } else if (status_p_judul.equals("Accept")) {
            replaceFragment(new MsgSuccesFragment());
            stepView.go(2, true);
            stepView.done(true);
        } else if (status_p_dospem.equals("Accept") && status_pengajuan.equals("Belum Mengajukan Proposal.")) {
            stepView.go(3, true);
            stepView.done(false);
        }

        // Check Condition Data Pengajuan Proposal
        if (status_pengajuan.equals("Belum Mengajukan Proposal.")) {
            stepView.go(3, true);
            stepView.done(false);
        } else if (status_p_proposal.equals("Waiting Approval")) {
            replaceFragment(new MsgWaitingFragment());
            stepView.go(3, true);
            stepView.done(false);
        } else if (status_p_proposal.equals("Revision")) {
            replaceFragment(new MsgRevisionFragment());
            stepView.go(3, true);
            stepView.done(false);
        } else if (status_p_proposal.equals("Decline")) {
            replaceFragment(new MsgRejectFragment());
            stepView.go(3, true);
            stepView.done(false);
        } else if (status_p_proposal.equals("Accept") && status_pengajuan.equals("Accept Proposal")) {
            replaceFragment(new MsgFinalSuccesFragment());
            stepView.go(4, true);
            stepView.done(true);
        } else if (status_p_proposal.equals("Accept")) {
            replaceFragment(new MsgSuccesFragment());
            stepView.go(3, true);
            stepView.done(true);
        }
    }

    private void getStatus_Pengajuan() {
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

                    status_pengajuan = res.getString("status");
                    status_p_dospem = userData.getString("status_dospem");
                    status_p_judul = userData.getString("status_judul");
                    status_p_proposal = userData.getString("status_proposal");

                    settingStepView();

                    check_StepView(status_pengajuan, status_p_dospem, status_p_judul, status_p_proposal);

                    loadingMain.cancel();
                } else {
                    // Handle the case when the response indicates an error
                    loadingMain.cancel();

                    swipeRefreshLayout.setRefreshing(false);

                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragment_container_progress, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}