package com.example.kouveepetshop.Pengelolaan.Produk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.Pengadaan.DetilPengadaan_Keranjang_Tambah;
import com.example.kouveepetshop.Pengadaan.PengadaanDAO;
import com.example.kouveepetshop.Pengadaan.Pengadaan_Adapter;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import java.util.ArrayList;

public class ProdukMasuk_Adapter extends RecyclerView.Adapter<ProdukMasuk_Adapter.ViewProcessHolder> implements Filterable
{
    private ArrayList<PengadaanDAO> item;
    private ArrayList<PengadaanDAO> itemFilterd;
    private Context mContext;
    private SharedPrefManager sharedPrefManager;

    public ProdukMasuk_Adapter(Context context, ArrayList<PengadaanDAO> item) {
        this.item = item;
        this.itemFilterd = item;
        mContext = context;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pengadaan, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {

        final PengadaanDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.no_pemesanan.setText(data.no_pemesanan);
        holder.id_supplier.setText(data.id_supplier);
        holder.tgl_pemesanan.setText(data.tgl_pemesanan);
        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.status.equals("belum tercetak") || data.status.equals("dibatalkan") || data.status.equals("diterima") )
                {
                    Toast.makeText(mContext, "Status Harus Tercetak", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(mContext, ProdukMasuk_Keranjang.class);
                    intent.putExtra("id", data.getId());
                    intent.putExtra("no_PO", data.getNo_pemesanan());
                    intent.putExtra("tgl_pemesanan", data.getTgl_pemesanan());
                    intent.putExtra("status", data.getStatus());
                    intent.putExtra("id_supplier",data.getId_supplier());
                    sharedPrefManager = new SharedPrefManager(mContext);
                    sharedPrefManager.saveSPInt("spIdPemesanan", data.id);
                    ((Activity) mContext).startActivityForResult (intent, 1);
                }

            }
        });
        holder.status.setText(data.status);
        if (data.status.equals("belum tercetak")){
            holder.status.setTextColor(Color.RED);
        }
        else if ( data.status.equals("tercetak"))
        {
            holder.status.setTextColor(Color.YELLOW);
        }
        else if (data.status.equals("dibatalkan"))
        {
            holder.status.setTextColor(Color.GRAY);
        }
        else {
            holder.status.setTextColor(Color.GREEN);
        }


    }

    @Override
    public int getItemCount() {
        return itemFilterd.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView id_supplier,status,no_pemesanan,tgl_pemesanan;
        CardView itemList;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            no_pemesanan = itemView.findViewById(R.id.pemesanan_no_po);
            tgl_pemesanan = itemView.findViewById(R.id.pemesanan_tanggal_pemesanan);
            id_supplier = itemView.findViewById(R.id.pemesanan_id_supplier);
            status = itemView.findViewById(R.id.pemesanan_status);
            itemList = itemView.findViewById(R.id.list_pemesanan_id);
            sharedPrefManager = new SharedPrefManager(mContext);
        }
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
                    ArrayList<PengadaanDAO> filteredList = new ArrayList<>();
                    for (PengadaanDAO row : item) {
                        if (row.getNo_pemesanan().toLowerCase().contains(charString.toLowerCase())|| row.getId_supplier().toLowerCase().contains(charString.toLowerCase())) {
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
                itemFilterd = (ArrayList<PengadaanDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
