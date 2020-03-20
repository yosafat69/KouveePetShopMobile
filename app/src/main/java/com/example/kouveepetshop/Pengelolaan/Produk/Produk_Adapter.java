package com.example.kouveepetshop.Pengelolaan.Produk;

import android.content.Context;
import android.content.Intent;
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

public class Produk_Adapter extends RecyclerView.Adapter<Produk_Adapter.ViewProcessHolder> {
    Context context;
    private ArrayList<ProdukDAO> item;
    private Context mContext;

    public Produk_Adapter(Context context, ArrayList<ProdukDAO> item) {
        this.context = context;
        this.item = item;
        mContext = context;
    }

    @Override
    public Produk_Adapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final ProdukDAO data = item.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama);
        holder.kategoti.setText(data.kategori);
        holder.harga.setText(Integer.toString(data.harga));
        holder.satuan.setText(data.satuan);
        holder.jmlh.setText(Integer.toString(data.jmlh));
        holder.jmlh_min.setText(Integer.toString(data.jmlh_min));

        //Kalau recycle view-nya di click, dia bakal ke halaman Produk_Edit.
        // Terus ngirim data yang di click ke Produk_Edit supaya bisa langsung ditampilin di EditText Produk_Edit
        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Produk_Edit.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("kategori", data.getKategori());
                intent.putExtra("harga", data.getHarga());
                intent.putExtra("satuan", data.getSatuan());
                intent.putExtra("jmlh", data.getJmlh());
                intent.putExtra("jmlh_min", data.getJmlh_min());
                mContext.startActivity (intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView kategoti, jmlh_min, jmlh, nama, satuan, link_gambar, harga;
        CardView itemList;
        ImageView gambar;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            nama = itemView.findViewById(R.id.produk_nama);
            kategoti = itemView.findViewById(R.id.produk_kategori_produk);
            harga = itemView.findViewById(R.id.produk_harga);
            satuan = itemView.findViewById(R.id.produk_satuan);
            jmlh = itemView.findViewById(R.id.produk_jumlah);
            jmlh_min = itemView.findViewById(R.id.produk_jumlah_minimal);
            itemList = itemView.findViewById(R.id.list_produk_id);
            gambar = itemView.findViewById(R.id.produk_gambar);
        }
    }
}



