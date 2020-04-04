package com.example.kouveepetshop.Pengelolaan.Supplier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.R;

import java.util.ArrayList;

public class Supplier_Adapter extends RecyclerView.Adapter<Supplier_Adapter.ViewProcessHolder> implements Filterable {
    Context context;
    private ArrayList<SupplierDAO> item;
    private ArrayList<SupplierDAO> itemFilterd;
    private Context mContext;

    public Supplier_Adapter(Context context, ArrayList<SupplierDAO> item) {
        this.context = context;
        this.item = item;
        this.itemFilterd = item;
        mContext = context;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_supplier, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final SupplierDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama);
        holder.no_telp.setText(data.no_telp);
        holder.alamat.setText(data.alamat);
        holder.kota.setText(data.kota);

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Supplier_Edit.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("alamat", data.getAlamat());
                intent.putExtra("no_telp", data.getNo_telp());
                intent.putExtra("kota", data.getKota());
                ((Activity) mContext).startActivityForResult (intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemFilterd.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView nama, no_telp, alamat, kota;
        CardView itemList;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            nama = itemView.findViewById(R.id.supplier_nama);
            no_telp = itemView.findViewById(R.id.supplier_no_telp);
            alamat = itemView.findViewById(R.id.supplier_alamat);
            kota = itemView.findViewById(R.id.supplier_kota);
            itemList = itemView.findViewById(R.id.list_supplier_id);
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
                    ArrayList<SupplierDAO> filteredList = new ArrayList<>();
                    for (SupplierDAO row : item) {
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || row.getNo_telp().contains(charSequence)) {
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
                itemFilterd = (ArrayList<SupplierDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}



