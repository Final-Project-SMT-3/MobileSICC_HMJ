package com.example.sicc.adapters;

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
import com.example.sicc.R;
import com.example.sicc.activity_details.DetailInformationActivity;
import com.example.sicc.models.Lomba;

import java.util.ArrayList;

public class CapsuleAdapter extends RecyclerView.Adapter<CapsuleAdapter.LombaViewHolder> {
    private Context context;
    private ArrayList<Lomba> dataList;
    private ArrayList<Lomba> dataListAll;

    public CapsuleAdapter(Context context, ArrayList<Lomba> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.dataListAll = new ArrayList<>(dataList) ;
    }

    @NonNull
    @Override
    public CapsuleAdapter.LombaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.capsule_menu_item, parent, false);
        return new LombaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CapsuleAdapter.LombaViewHolder holder, int position) {
        Lomba lomba = dataList.get(position);

        holder.txt_nama_lomba.setText(lomba.getNama_lomba());

        holder.txt_nama_lomba.setOnClickListener(v-> {

        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class LombaViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_nama_lomba;

        public LombaViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nama_lomba = itemView.findViewById(R.id.txt_menu);
        }
    }
}
