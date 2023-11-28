package com.example.sicc.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sicc.R;
import com.example.sicc.models.Constant;
import com.example.sicc.notification.MsgWaitingFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadProposalFragment extends Fragment{
    private EditText name_file;
    private ImageView btn_choose;
    private Button btn_pengajuan;
    private View view;
    private Uri pdfUri;
    private static final int PICK_FILE_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_upload_proposal, container, false);

        init();

        return view;
    }

    private void init() {
        name_file = view.findViewById(R.id.editText_input);
        btn_choose = view.findViewById(R.id.btn_choose_file);
        btn_pengajuan = view.findViewById(R.id.btn_ajukan_judul);

        btn_choose.setOnClickListener(v-> {
            showFileChooser();
        });

        btn_pengajuan.setOnClickListener(v-> {
            // uploadMultipart();
            uploadProposal();
        });
    }

    private void uploadProposal() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PENGAJUAN_PROPOSAL, response -> {
            Log.d("Response", response);
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

                } else {
                    // Handle the case when the response indicates an error
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                // Handle the case when there's a JSON parsing error
                Toast.makeText(getContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();

            // Handle the case when there's a network error
            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }) {
            String file = name_file.getText().toString();

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
                params.put("id_dospem", "22");
                params.put("id_judul", "6");

                if (pdfUri != null) {
                    try {
                        String pdfBase64 = pdfToBase64(pdfUri);
                        Log.d("String", pdfBase64);
                        params.put("file_path", pdfBase64);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    // method to show file chooser
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

            // Display some feedback indicating the PDF file has been selected
            Toast.makeText(requireContext(), "PDF selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String pdfToBase64(Uri pdfUri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(pdfUri);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}