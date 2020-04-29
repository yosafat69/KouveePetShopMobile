package com.example.kouveepetshop.CS_Transaksi;

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
import com.example.kouveepetshop.Pengelolaan.Layanan.LayananDAO;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransaksiLayanan_Adapter extends RecyclerView.Adapter<TransaksiLayanan_Adapter.ViewProcessHolder> implements Filterable {
    private Context context;
    private ArrayList<LayananDAO> item, itemFilterd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private SharedPrefManager sharedPrefManager;

    public TransaksiLayanan_Adapter(Context context, ArrayList<LayananDAO> item) {
        this.context = context;
        this.item = item;
        this.itemFilterd = item;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cs_transaksi_penjualan, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        String link = ip + url;
        String substring;

        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        final LayananDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.keterangan + " " + data.ukuran);
        holder.harga.setText(formatRupiah.format(data.harga));

        substring = data.gambar.substring(47);
        final String link_gambar = link + substring;
        Picasso.get().load(link_gambar).into(holder.gambar);

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager = new SharedPrefManager(context);
                Intent intent = new Intent(context, TransaksiLayananKeranjang_Tambah.class);
                intent.putExtra("id", data.id);
                context.startActivity(intent);
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
                    ArrayList<LayananDAO> filteredList = new ArrayList<>();
                    for (LayananDAO row : item) {
                        if (row.getKeterangan().toLowerCase().contains(charString.toLowerCase()) || row.getUkuran().toLowerCase().contains(charString.toLowerCase())) {
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
                itemFilterd = (ArrayList<LayananDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView nama, harga;
        CardView itemList;
        ImageView gambar;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            nama = itemView.findViewById(R.id.list_cs_transaksi_penjualan_nama);
            harga = itemView.findViewById(R.id.list_cs_transaksi_penjualan_harga);
            itemList = itemView.findViewById(R.id.list_cs_transaksi_penjualan_id);
            gambar = itemView.findViewById(R.id.list_cs_transaksi_penjualan_gambar);
        }
    }
}



