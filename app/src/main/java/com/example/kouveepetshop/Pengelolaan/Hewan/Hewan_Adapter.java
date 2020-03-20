package com.example.kouveepetshop.Pengelolaan.Hewan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.R;

import java.util.ArrayList;

public class Hewan_Adapter extends RecyclerView.Adapter<Hewan_Adapter.ViewProcessHolder> {
    Context context;
    private ArrayList<HewanDAO> item;
    private Context mContext;

    public Hewan_Adapter(Context context, ArrayList<HewanDAO> item) {
        this.context = context;
        this.item = item;
        mContext = context;
    }

    @Override
    public Hewan_Adapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hewan, parent, false);
        ViewProcessHolder processHolder = new ViewProcessHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final HewanDAO data = item.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama);
        holder.tanggal_lahir.setText(data.tanggal_lahir);
        holder.jenis.setText(data.jenis);
//        holder.itemList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, HalamanEditProduk.class);
//                intent.putExtra("id_makanan", data.getIdProduk());
//                intent.putExtra("nama", data.getNama());
//                intent.putExtra("harga", data.getHarga());
//                intent.putExtra("keterangan", data.getKeterangan());
//                intent.putExtra("ketersediaan", data.getKetersediaan());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView nama, tanggal_lahir, jenis;
        CardView itemList;
        ImageView gambar;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            nama = itemView.findViewById(R.id.hewan_nama);
            tanggal_lahir = itemView.findViewById(R.id.hewan_tanggal_lahir);
            jenis = itemView.findViewById(R.id.hewan_jenis);
            itemList = itemView.findViewById(R.id.list_hewan_id);
            gambar = itemView.findViewById(R.id.hewan_gambar);
        }
    }
}



