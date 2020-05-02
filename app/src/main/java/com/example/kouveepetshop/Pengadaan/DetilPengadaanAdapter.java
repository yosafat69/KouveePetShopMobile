package com.example.kouveepetshop.Pengadaan;

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

import com.example.kouveepetshop.CS_Transaksi.DetilTransaksiLayananDAO;
import com.example.kouveepetshop.CS_Transaksi.TransaksiLayananKeranjang_Adapter;
import com.example.kouveepetshop.CS_Transaksi.TransaksiLayananKeranjang_Edit;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetilPengadaanAdapter extends RecyclerView.Adapter <DetilPengadaanAdapter.ViewProcessHolder> implements Filterable {
    private Context context;
    private ArrayList<DetilPengadaanDAO> item, itemFilterd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    public DetilPengadaanAdapter(Context context, ArrayList<DetilPengadaanDAO> item) {
        this.context = context;
        this.item = item;
        this.itemFilterd = item;
    }

    @Override
    public DetilPengadaanAdapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detilpengadaan, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetilPengadaanAdapter.ViewProcessHolder holder, final int position) {
        String link = ip + url;
        String substring;

        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        final DetilPengadaanDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.id_pemesanan.setText(data.id_pemesanan);
        holder.id_produk.setText(formatRupiah.format(data.id_produk));
        holder.jumlah.setText(String.valueOf(data.jumlah));

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
                    ArrayList<DetilPengadaanDAO> filteredList = new ArrayList<>();
                    for (DetilPengadaanDAO row : item) {
                        if (row.getid_produk().toLowerCase().contains(charString.toLowerCase())) {
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
        TextView id_pemesanan,id_produk,jumlah;
        CardView itemList;
        ImageView gambar;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            id_produk = itemView.findViewById(R.id.list_detilpengadaan_id_produk);
            jumlah = itemView.findViewById(R.id.list_detilpengadaan_jumlah);
            itemList = itemView.findViewById(R.id.list_detilpengadaan_id);
            gambar = itemView.findViewById(R.id.list_detilpengadaan_gambar);
        }
    }
}


