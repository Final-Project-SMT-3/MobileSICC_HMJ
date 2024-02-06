package com.example.sicc.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.sicc.notification.MsgWaitingFragment;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UploadProposalFragment extends Fragment implements PickiTCallbacks {
    private String id_dospem, id_judul, id_pp;
    private TextView txt_title, txt_nama_dosen, txt_review_dospem, tgl_pengajuan;
    private CardView card_review;
    private EditText name_file;
    private ImageView btn_choose;
    private Button btn_pengajuan;
    private View view;
    private Uri pdfUri;
    private PickiT pickiT;
    private LoadingMain loadingMain;
    private SharedPreferences sharedPreferences;
    private static final int PICK_FILE_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_upload_proposal, container, false);

        init();

        return view;
    }

    private void init() {
        txt_nama_dosen = view.findViewById(R.id.txt_nama_dospem);
        txt_review_dospem = view.findViewById(R.id.txt_review_dospem);
        txt_title = view.findViewById(R.id.title_review);
        tgl_pengajuan = view.findViewById(R.id.tgl_pengajuan);
        card_review = view.findViewById(R.id.layout_review);
        name_file = view.findViewById(R.id.editText_input);
        btn_choose = view.findViewById(R.id.btn_choose_file);
        btn_pengajuan = view.findViewById(R.id.btn_ajukan_judul);

        loadingMain = new LoadingMain(requireContext());
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user_login", Context.MODE_PRIVATE);

        loadingMain.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                getDetailJudul();
            }
        }, 500);

        pickiT = new PickiT(requireContext(), this, requireActivity());

        btn_choose.setOnClickListener(v-> {
            showFileChooser();
        });

        btn_pengajuan.setOnClickListener(v-> {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    uploadProposal();
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

//    private void getDetailJudul() {
//        StringRequest request = new StringRequest(Request.Method.POST, Constant.DETAIL_PROPOSAL, response -> {
//            try {
//                JSONObject res = new JSONObject(response);
//
//                int status_code = res.getInt("status_code");
//                String message = res.getString("message");
//
//                if (status_code == 200 && message.equals("Success")) {
//                    JSONObject dataProposal = res.getJSONObject("response");
//
//                    id_dospem = dataProposal.getString("id_dospem");
//                    id_judul = dataProposal.getString("id_judul");
//                    id_pp = dataProposal.getString("id_pengajuan_proposal");
//                    String nama_dosen = dataProposal.getString("nama_dospem");
//                    String file_proposal = dataProposal.getString("path");
//                    String review = dataProposal.getString("review");
//                    String tanggal_pengajuan = dataProposal.getString("submit_date");
//                    String status_judul = dataProposal.getString("status_proposal");
//
//                    if (status_judul.equals("Revision")) {
//                        txt_title.setVisibility(View.VISIBLE);
//                        card_review.setVisibility(View.VISIBLE);
//                        btn_pengajuan.setText("Ajukan Revisi Proposal");
//
//                        txt_nama_dosen.setText(formatDosen(nama_dosen));
//                        txt_review_dospem.setText(review);
//                        name_file.setText(file_proposal);
//                        tgl_pengajuan.setText("Pengajuan : " + formatDate(tanggal_pengajuan));
//
//                        btn_pengajuan.setOnClickListener(v-> {
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    uploadRevisiProposal();
//                                }
//                            }, 500);
//                        });
//                    }
//
//                    loadingMain.cancel();
//                } else {
//                    loadingMain.cancel();
//
//                    // Handle the case when the response indicates an error
//                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//                loadingMain.cancel();
//
//                // Handle the case when there's a JSON parsing error
//                Toast.makeText(getContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
//            }
//        }, error -> {
//            error.printStackTrace();
//
//            loadingMain.cancel();
//
//            // Handle the case when there's a network error
//            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
//        }) {
//            String id_mahasiswa = String.valueOf(sharedPreferences.getInt("id_user", 0));
//
//            @Nullable
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
//                return headers;
//            }
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("id_mhs", id_mahasiswa);
//                return params;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(requireContext());
//        queue.add(request);
//    }

//    private void uploadProposal() {
//        StringRequest request = new StringRequest(Request.Method.POST, Constant.PENGAJUAN_PROPOSAL, response -> {
//            Log.d("Response", response);
//            try {
//                JSONObject res = new JSONObject(response);
//
//                int status_code = res.getInt("status_code");
//                String message = res.getString("message");
//
//                if (status_code == 200 && message.equals("Success")) {
//                    String detail_message = res.getString("response");
//
//                    Toast.makeText(getContext(), detail_message, Toast.LENGTH_SHORT).show();
//
//                    // Redirect Into Waiting
//                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.setReorderingAllowed(true);
//                    transaction.replace(R.id.fragment_container_progress, MsgWaitingFragment.class, null);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//
//                    loadingMain.cancel();
//                } else {
//                    loadingMain.cancel();
//
//                    // Handle the case when the response indicates an error
//                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//                loadingMain.cancel();
//
//                // Handle the case when there's a JSON parsing error
//                Toast.makeText(getContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
//            }
//        }, error -> {
//            error.printStackTrace();
//
//            loadingMain.cancel();
//
//            // Handle the case when there's a network error
//            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
//        }) {
//            String file = name_file.getText().toString();
//
//            @Nullable
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
//                return headers;
//            }
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("id_dospem", id_dospem);
//                params.put("id_judul", id_judul);
//
//                if (pdfUri != null) {
//                    try {
//                        String pdfBase64 = pdfToBase64(pdfUri);
//                        Log.d("String", pdfBase64);
//                        params.put("file_path", pdfBase64);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                return params;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(requireContext());
//        queue.add(request);
//    }

//  private void uploadRevisiProposal() {
//        StringRequest request = new StringRequest(Request.Method.POST, Constant.PENGAJUAN_REVISI_PROPOSAL, response -> {
//            Log.d("Response", response);
//            try {
//                JSONObject res = new JSONObject(response);
//
//                int status_code = res.getInt("status_code");
//                String message = res.getString("message");
//
//                if (status_code == 200 && message.equals("Success")) {
//                    String detail_message = res.getString("response");
//
//                    Toast.makeText(getContext(), detail_message, Toast.LENGTH_SHORT).show();
//
//                    // Redirect Into Waiting
//                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.setReorderingAllowed(true);
//                    transaction.replace(R.id.fragment_container_progress, MsgWaitingFragment.class, null);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//
//                    loadingMain.cancel();
//                } else {
//                    loadingMain.cancel();
//
//                    // Handle the case when the response indicates an error
//                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//                loadingMain.cancel();
//
//                // Handle the case when there's a JSON parsing error
//                Toast.makeText(getContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
//            }
//        }, error -> {
//            error.printStackTrace();
//
//            loadingMain.cancel();
//
//            // Handle the case when there's a network error
//            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
//        }) {
//            String file = name_file.getText().toString();
//
//            @Nullable
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("HTTP-TOKEN", "KgncmLUc7qvicKI1OjaLYLkPi");
//                return headers;
//            }
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("id_pp", id_pp);
//                params.put("id_judul", id_judul);
//
//                if (pdfUri != null) {
//                    try {
//                        String pdfBase64 = pdfToBase64(pdfUri);
//                        Log.d("String", pdfBase64);
//                        params.put("file_path", pdfBase64);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                return params;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(requireContext());
//        queue.add(request);
//    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICK_FILE_REQUEST_CODE) && (resultCode == RESULT_OK) && (data != null) && (data.getData() != null)) {
            pdfUri = data.getData();

            pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
        }
    }

    private String pdfToBase64(Uri pdfUri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(pdfUri);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        if (wasSuccessful) {
            if (path != null) {
                String fileName = path.substring(path.lastIndexOf('/') + 1);

                name_file.setText(fileName);

                Toast.makeText(requireContext(), fileName, Toast.LENGTH_SHORT).show();
            } else {
                name_file.setText("File path not found");
            }
        } else {
            Log.e("FileOperationFailed", "Reason: " + Reason);
        }
    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {

    }
}