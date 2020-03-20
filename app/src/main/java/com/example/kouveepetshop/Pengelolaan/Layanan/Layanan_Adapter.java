package com.example.kouveepetshop.Pengelolaan.Layanan;

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

public class Layanan_Adapter extends RecyclerView.Adapter<Layanan_Adapter.ViewProcessHolder> {
    Context context;
    private ArrayList<LayananDAO> item;
    private Context mContext;

    public Layanan_Adapter(Context context, ArrayList<LayananDAO> item) {
        this.context = context;
        this.item = item;
        mContext = context;
    }

    @Override
    public Layanan_Adapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layanan, parent, false);
        ViewProcessHolder processHolder = new ViewProcessHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final LayananDAO data = item.get(position);
        holder.id = data.id;
        holder.nama.setText(data.keterangan);
        holder.ukuran.setText(data.ukuran);
        holder.harga.setText(Integer.toString(data.harga));
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
        TextView nama, ukuran, harga;
        CardView itemList;
        ImageView gambar;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            nama = itemView.findViewById(R.id.layanan_nama);
            ukuran = itemView.findViewById(R.id.layanan_ukuran);
            harga = itemView.findViewById(R.id.layanan_harga);
            itemList = itemView.findViewById(R.id.list_layanan_id);
            gambar = itemView.findViewById(R.id.layanan_gambar);
        }
    }
}



