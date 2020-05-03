package com.example.kouveepetshop.Cashier_Transaksi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.CS_Transaksi.TransaksiDAO;
import com.example.kouveepetshop.Pengelolaan.Member.Member_Edit;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Cashier_Transaksi_Adapter extends RecyclerView.Adapter<Cashier_Transaksi_Adapter.ViewProcessHolder> implements Filterable {
    private ArrayList<TransaksiDAO> item;
    private ArrayList<TransaksiDAO> itemFilterd;
    private Context mContext;
    private SharedPrefManager sharedPrefManager;

    public Cashier_Transaksi_Adapter(Context context, ArrayList<TransaksiDAO> item) {
        this.item = item;
        this.itemFilterd = item;
        mContext = context;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cs_transaksi, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        final TransaksiDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.isTransaksiLayanan = data.isTransaksiLayanan;
        holder.no_transaksi.setText(data.no_transaksi);
        holder.no_telp.setText(data.no_telp);
        holder.total.setText(formatRupiah.format(data.total));
        holder.status.setText(data.status);
        if (data.status.equals("belum selesai") || data.status.equals("belum lunas") ){
            holder.status.setTextColor(Color.RED);
        }
        else {
            holder.status.setTextColor(Color.GREEN);
        }

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.isTransaksiLayanan == 0) {
                    Intent intent = new Intent(mContext, Cashier_DetailPembayaranPenjualan.class);
                    intent.putExtra("id", data.id);
                    sharedPrefManager = new SharedPrefManager(mContext);
                    sharedPrefManager.saveSPInt("spIdTransaksi", data.id);
                    ((Activity) mContext).startActivityForResult(intent, 1);
                }
                else {
                    Intent intent = new Intent(mContext, Cashier_DetailPembayaranLayanan.class);
                    intent.putExtra("id", data.id);
                    sharedPrefManager = new SharedPrefManager(mContext);
                    sharedPrefManager.saveSPInt("spIdTransaksi", data.id);
                    ((Activity) mContext).startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemFilterd.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id, isTransaksiLayanan;
        TextView no_transaksi, no_telp, total, status;
        CardView itemList;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            no_transaksi = itemView.findViewById(R.id.cs_transaksi_no_transaksi);
            no_telp = itemView.findViewById(R.id.cs_transaksi_no_telp);
            total = itemView.findViewById(R.id.cs_transaksi_total);
            status = itemView.findViewById(R.id.cs_transaksi_status);
            itemList = itemView.findViewById(R.id.list_cs_transaksi_id);
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
                    ArrayList<TransaksiDAO> filteredList = new ArrayList<>();
                    for (TransaksiDAO row : item) {
                        if (row.getNo_transaksi().toLowerCase().contains(charString.toLowerCase()) || row.getNo_telp().toLowerCase().contains(charString.toLowerCase())) {
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
                itemFilterd = (ArrayList<TransaksiDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}



