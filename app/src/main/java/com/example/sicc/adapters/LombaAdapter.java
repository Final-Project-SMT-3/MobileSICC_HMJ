package com.example.sicc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicc.DetailInformationActivity;
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
        holder.txt_nama_lomba.setText(dataList.get(position).getNama_lomba());
        holder.txt_jenis_lomba.setText(dataList.get(position).getJenis_lomba());
        holder.txt_tgl_lomba.setText(dataList.get(position).getTgl_lomba());
        holder.txt_desk_lomba.setText(dataList.get(position).getDeskripsi_lomba());

        holder.txt_desk_lomba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailInformationActivity.class);
                intent.putExtra("judul", dataList.get(holder.getAdapterPosition()).getNama_lomba());
                intent.putExtra("sub_Judul", dataList.get(holder.getAdapterPosition()).getJenis_lomba());
                intent.putExtra("tgl_Lomba", dataList.get(holder.getAdapterPosition()).getTgl_lomba());
                intent.putExtra("desc_Lomba", dataList.get(holder.getAdapterPosition()).getDeskripsi_lomba());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class LombaViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_nama_lomba, txt_jenis_lomba, txt_tgl_lomba, txt_desk_lomba;

        public LombaViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nama_lomba = itemView.findViewById(R.id.nama_kegiatan_lomba);
            txt_jenis_lomba = itemView.findViewById(R.id.jenis_kegiatan_lomba);
            txt_tgl_lomba = itemView.findViewById(R.id.tgl_kegiatan_lomba);
            txt_desk_lomba = itemView.findViewById(R.id.deskripsi_kegiatan_lomba);
        }
    }
}
