package com.example.sicc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicc.R;
import com.example.sicc.models.Anggota;

import java.util.ArrayList;

public class AnggotaAdapter extends RecyclerView.Adapter<AnggotaAdapter.AnggotaViewHolder> {
    private ArrayList<Anggota> dataList;

    public AnggotaAdapter(ArrayList<Anggota> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AnggotaAdapter.AnggotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_anggota_item, parent, false);
        return new AnggotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnggotaAdapter.AnggotaViewHolder holder, int position) {
        holder.txt_nama.setText(dataList.get(position).getNama_anggota());
        holder.txt_prodi_smt.setText(dataList.get(position).getProdi_smt());
        holder.txt_tgl_lahir.setText(dataList.get(position).getTgl_lahir());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class AnggotaViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_nama, txt_prodi_smt, txt_tgl_lahir;
        public AnggotaViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nama = itemView.findViewById(R.id.nama_anggota);
            txt_prodi_smt = itemView.findViewById(R.id.txt_prodi_smt);
            txt_tgl_lahir = itemView.findViewById(R.id.tgl_lahir);
        }
    }
}
