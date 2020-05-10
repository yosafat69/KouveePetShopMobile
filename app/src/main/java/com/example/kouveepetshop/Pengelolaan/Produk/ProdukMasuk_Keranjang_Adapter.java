package com.example.kouveepetshop.Pengelolaan.Produk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengadaan.DetilPengadaanDAO;
import com.example.kouveepetshop.Pengadaan.DetilPengadaan_Keranjang_Adapter;
import com.example.kouveepetshop.Pengadaan.Dialog_Pengadaan;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProdukMasuk_Keranjang_Adapter extends RecyclerView.Adapter <ProdukMasuk_Keranjang_Adapter.ViewProcessHolder> implements Filterable {
    private Context mContext;
    private ArrayList<DetilPengadaanDAO> item, itemFilterd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private Dialog mDialog;
    FragmentManager fragmentManager;
    private SharedPrefManager sharedPrefManager;

    public ProdukMasuk_Keranjang_Adapter(Context context, ArrayList<DetilPengadaanDAO> item, FragmentManager fragmentManager) {
        this.item = item;
        this.itemFilterd = item;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detilpengadaan_keranjang, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        String link = ip + url;
        String substring;


        final DetilPengadaanDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama);
        holder.jumlah.setText(String.valueOf(data.jumlah));

        substring = data.gambar.substring(47);
        final String link_gambar = link + substring;
        Picasso.get().load(link_gambar).into(holder.gambar);

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProdukMasuk_Input.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemFilterd.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemFilterd = item;
                } else {
                    ArrayList<DetilPengadaanDAO> filteredList = new ArrayList<>();
                    for (DetilPengadaanDAO row : item) {
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    itemFilterd = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemFilterd;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemFilterd = (ArrayList<DetilPengadaanDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView nama,jumlah;
        CardView itemList;
        ImageView gambar;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            nama = itemView.findViewById(R.id.list_detilpengadaan_keranjang_id_produk);
            jumlah = itemView.findViewById(R.id.list_detilpengadaan_keranjang_jumlah);
            itemList = itemView.findViewById(R.id.list_detilpengadaan_keranjang_id);
            gambar = itemView.findViewById(R.id.list_detilpengadaan_keranjang_gambar);
        }
    }
}
