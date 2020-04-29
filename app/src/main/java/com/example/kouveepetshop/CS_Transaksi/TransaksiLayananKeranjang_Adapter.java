package com.example.kouveepetshop.CS_Transaksi;

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

public class TransaksiLayananKeranjang_Adapter extends RecyclerView.Adapter<TransaksiLayananKeranjang_Adapter.ViewProcessHolder> implements Filterable {
    private Context context;
    private ArrayList<DetilTransaksiLayananDAO> item, itemFilterd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    public TransaksiLayananKeranjang_Adapter(Context context, ArrayList<DetilTransaksiLayananDAO> item) {
        this.context = context;
        this.item = item;
        this.itemFilterd = item;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cs_transaksi_penjualan_keranjang, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        String link = ip + url;
        String substring;

        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        final DetilTransaksiLayananDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama_layanan);
        holder.harga.setText(formatRupiah.format(data.harga));
        holder.jumlah.setText(String.valueOf(data.nama_hewan));

        substring = data.gambar.substring(47);
        final String link_gambar = link + substring;
        Picasso.get().load(link_gambar).into(holder.gambar);

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransaksiLayananKeranjang_Edit.class);
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
                    ArrayList<DetilTransaksiLayananDAO> filteredList = new ArrayList<>();
                    for (DetilTransaksiLayananDAO row : item) {
                        if (row.getNama_hewan().toLowerCase().contains(charString.toLowerCase())) {
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
                itemFilterd = (ArrayList<DetilTransaksiLayananDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView nama, harga, jumlah;
        CardView itemList;
        ImageView gambar;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            nama = itemView.findViewById(R.id.list_cs_transaksi_penjualan_keranjang_nama);
            harga = itemView.findViewById(R.id.list_cs_transaksi_penjualan_keranjang_harga);
            jumlah = itemView.findViewById(R.id.list_cs_transaksi_penjualan_keranjang_jumlah);
            itemList = itemView.findViewById(R.id.list_cs_transaksi_penjualan_keranjang_id);
            gambar = itemView.findViewById(R.id.list_cs_transaksi_penjualan_keranjang_gambar);
        }
    }
}



