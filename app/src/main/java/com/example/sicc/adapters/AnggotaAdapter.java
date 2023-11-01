package com.example.sicc.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
    private Context context;
    private ArrayList<Anggota> dataList;
    private SharedPreferences sharedPreferences;

    public AnggotaAdapter(Context context, ArrayList<Anggota> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.sharedPreferences = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AnggotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_anggota_item, parent, false);
        return new AnggotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnggotaAdapter.AnggotaViewHolder holder, int position) {
        Anggota anggota = dataList.get(position);

        holder.txt_nim.setText(anggota.getNim_anggota());
        holder.txt_nama.setText(anggota.getNama_anggota());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class AnggotaViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_nim, txt_nama;
        public AnggotaViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nim = itemView.findViewById(R.id.txt_nim);
            txt_nama = itemView.findViewById(R.id.nama_anggota);
        }
    }
}
