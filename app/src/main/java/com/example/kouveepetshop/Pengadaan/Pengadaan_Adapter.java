package com.example.kouveepetshop.Pengadaan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.kouveepetshop.CS_Transaksi.TransaksiPenjualanKeranjang;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengelolaan.Hewan.edit_hewan;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Pengadaan_Adapter extends RecyclerView.Adapter<Pengadaan_Adapter.ViewProcessHolder> implements Filterable {
    private ArrayList<PengadaanDAO> item;
    private ArrayList<PengadaanDAO> itemFilterd;
    private Context mContext;
    private SharedPrefManager sharedPrefManager;

    public Pengadaan_Adapter(Context context, ArrayList<PengadaanDAO> item) {
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
                Intent intent = new Intent(mContext, Pengadaan_Edit.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("no_PO", data.getNo_pemesanan());
                intent.putExtra("tgl_pemesanan", data.getTgl_pemesanan());
                intent.putExtra("status", data.getStatus());
                intent.putExtra("id_supplier",data.getId_supplier());
                sharedPrefManager = new SharedPrefManager(mContext);
                sharedPrefManager.saveSPInt("spIdPemesanan", data.id);
                ((Activity) mContext).startActivityForResult (intent, 1);
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
