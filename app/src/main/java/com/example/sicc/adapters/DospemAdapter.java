package com.example.sicc.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicc.R;
import com.example.sicc.fragments.PengajuanDospemFragment;
import com.example.sicc.models.Dospem;

import java.util.ArrayList;

public class DospemAdapter extends RecyclerView.Adapter<DospemAdapter.DospemViewHolder> {
    private Context context;
    private ArrayList<Dospem> dataList;

    public DospemAdapter(Context context, ArrayList<Dospem> dataList) {
        this.context = context;
        this.dataList = dataList;
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

    @NonNull
    @Override
    public DospemAdapter.DospemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_dospem_item, parent,  false);
        return new DospemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DospemAdapter.DospemViewHolder holder, int position) {
        Dospem dospem = dataList.get(position);

        holder.txtNama.setText(formatDosen(dospem.getNama_dosen()));
        holder.txtNIDN.setText(dospem.getNidn_dosen());
        holder.txtStatus.setText(dospem.getStatus_dosen());

        if (!dataList.get(position).getStatus_dosen().equals("Tersedia")) {
            holder.cardViewStatus.setCardBackgroundColor((ContextCompat.getColor(context, R.color.colorDanger)));
            holder.btnAjukan.setVisibility(View.GONE);
        }

        holder.btnAjukan.setOnClickListener(v-> {
            PengajuanDospemFragment pengajuanDospemFragment = new PengajuanDospemFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("id_dosen", dospem.getId_dospem());
            bundle.putInt("dospem_position", position);
            pengajuanDospemFragment.setArguments(bundle);

            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().
                replace(R.id.fragment_container_progress, pengajuanDospemFragment).
                commit();
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class DospemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtNIDN, txtStatus;
        private CardView cardViewStatus;
        private Button btnAjukan;

        public DospemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNama = itemView.findViewById(R.id.txt_nama_dospem);
            txtNIDN = itemView.findViewById(R.id.txt_nidn_dospem);
            txtStatus = itemView.findViewById(R.id.status_kuota);
            cardViewStatus = itemView.findViewById(R.id.layout_status_kuota);
            btnAjukan = itemView.findViewById(R.id.btn_detail_dospem);
        }
    }
}
