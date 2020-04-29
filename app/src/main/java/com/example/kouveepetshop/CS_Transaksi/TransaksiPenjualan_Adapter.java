package com.example.kouveepetshop.CS_Transaksi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengelolaan.Produk.ProdukDAO;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransaksiPenjualan_Adapter extends RecyclerView.Adapter<TransaksiPenjualan_Adapter.ViewProcessHolder> implements Filterable {
    private Context context;
    private ArrayList<ProdukDAO> item, itemFilterd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private Dialog mDialog;
    private FragmentManager fragmentManager;

    public TransaksiPenjualan_Adapter(Context context, ArrayList<ProdukDAO> item, FragmentManager fragmentManager) {
        this.context = context;
        this.item = item;
        this.itemFilterd = item;
        this.fragmentManager = fragmentManager;
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

        final ProdukDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama);
        holder.harga.setText(formatRupiah.format(data.harga));

        substring = data.link_gambar.substring(47);
        final String link_gambar = link + substring;
        Picasso.get().load(link_gambar).into(holder.gambar);

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(data.id);
            }
        });
    }

    private void openDialog(Integer id){
        Bundle b = new Bundle();
        b.putInt("id", id);
        b.putInt("isKeranjang", 0);

        Dialog_Transaksi_penjualan dialog_transaksi_penjualan = new Dialog_Transaksi_penjualan();
        dialog_transaksi_penjualan.setArguments(b);
        dialog_transaksi_penjualan.show(fragmentManager, "mTag");
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
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || row.getKategori().toLowerCase().contains(charString.toLowerCase())) {
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



