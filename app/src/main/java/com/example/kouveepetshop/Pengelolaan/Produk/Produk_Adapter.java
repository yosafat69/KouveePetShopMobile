package com.example.kouveepetshop.Pengelolaan.Produk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Produk_Adapter extends RecyclerView.Adapter<Produk_Adapter.ViewProcessHolder> implements Filterable {
    Context context;
    private ArrayList<ProdukDAO> item, itemFilterd;
    private Context mContext;
    private String ip = MainActivity.getIp();

    public Produk_Adapter(Context context, ArrayList<ProdukDAO> item) {
        this.context = context;
        this.item = item;
        this.itemFilterd = item;
        mContext = context;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        String link = "http://"+ip+"/rest_api-kouvee-pet-shop-master/";

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        final ProdukDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama);
        holder.kategoti.setText(data.kategori);
        holder.harga.setText(formatRupiah.format(data.harga));
        holder.satuan.setText(data.satuan);
        holder.jmlh.setText(String.format("%,d",data.jmlh));
        holder.jmlh_min.setText(String.format("%,d",data.jmlh_min));

        final String link_gambar = link + data.link_gambar;
        Picasso.get().load(link_gambar).into(holder.gambar);

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
                intent.putExtra("link_gambar", link_gambar);
                ((Activity) mContext).startActivityForResult (intent, 1);
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
                    ArrayList<ProdukDAO> filteredList = new ArrayList<>();
                    for (ProdukDAO row : item) {
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || row.getKategori().contains(charSequence)) {
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
                itemFilterd = (ArrayList<ProdukDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView kategoti, jmlh_min, jmlh, nama, satuan, harga;
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



