package com.example.sicc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicc.R;
import com.example.sicc.models.Dospem;

import java.util.ArrayList;

public class DospemAdapter extends RecyclerView.Adapter<DospemAdapter.DospemViewHolder> {
    private Context context;
    private ArrayList<Dospem> dataList;

    public DospemAdapter(Context context, ArrayList<Dospem> dataList) {
        this.context = context;
        this.dataList = dataList;
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
        holder.txtNama.setText(dataList.get(position).getNama_dosen());
        holder.txtNIDN.setText(String.valueOf(dataList.get(position).getNidn_dosen()));
        holder.txtStatus.setText(dataList.get(position).getStatus_dosen());

        if (!dataList.get(position).getStatus_dosen().equals("Tersedia")) {
            holder.cardViewStatus.setCardBackgroundColor((ContextCompat.getColor(context, R.color.colorDanger)));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class DospemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtNIDN, txtStatus;
        private CardView cardViewStatus;

        public DospemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNama = itemView.findViewById(R.id.txt_nama_dospem);
            txtNIDN = itemView.findViewById(R.id.txt_nidn_dospem);
            txtStatus = itemView.findViewById(R.id.status_kuota);
            cardViewStatus = itemView.findViewById(R.id.layout_status_kuota);
        }
    }
}
