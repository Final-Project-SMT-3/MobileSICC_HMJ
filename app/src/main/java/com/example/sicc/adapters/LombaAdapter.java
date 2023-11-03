package com.example.sicc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.sicc.activity_details.DetailInformationActivity;
import com.example.sicc.R;
import com.example.sicc.models.Lomba;

import java.util.ArrayList;

public class LombaAdapter extends RecyclerView.Adapter<LombaAdapter.LombaViewHolder> {
    private Context context;
    private ArrayList<Lomba> dataList;

    public LombaAdapter(Context context, ArrayList<Lomba> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public LombaAdapter.LombaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_lomba_item, parent, false);
        return new LombaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LombaAdapter.LombaViewHolder holder, int position) {
        Lomba lomba = dataList.get(position);

        holder.txt_nama_lomba.setText(lomba.getNama_lomba());
        holder.txt_jenis_lomba.setText(lomba.getJenis_lomba());

        holder.btn_detail.setOnClickListener(v-> {
                Intent intent = new Intent(context, DetailInformationActivity.class);
                intent.putExtra("id_lomba", lomba.getId_lomba());
                intent.putExtra("lombaPosition", position);

                context.startActivity(intent);
                Animatoo.INSTANCE.animateSlideLeft(context);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class LombaViewHolder extends RecyclerView.ViewHolder {
        private ImageView foto_lomba;
        private TextView txt_nama_lomba, txt_jenis_lomba, btn_detail;

        public LombaViewHolder(@NonNull View itemView) {
            super(itemView);

            foto_lomba = itemView.findViewById(R.id.logo_kegiatan_lomba);
            txt_nama_lomba = itemView.findViewById(R.id.nama_kegiatan_lomba);
            txt_jenis_lomba = itemView.findViewById(R.id.jenis_kegiatan_lomba);
            btn_detail = itemView.findViewById(R.id.txt_detail_lomba);
        }
    }
}
