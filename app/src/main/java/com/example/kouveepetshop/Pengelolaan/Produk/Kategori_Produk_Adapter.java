package com.example.kouveepetshop.Pengelolaan.Produk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.Pengelolaan.KeteranganDAO;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import java.util.ArrayList;

public class Kategori_Produk_Adapter extends RecyclerView.Adapter<Kategori_Produk_Adapter.ViewProcessHolder> {
    Context context;
    private ArrayList<KeteranganDAO> item,itemFilterd;
    private Context mContext;
    private SharedPrefManager sharedPrefManager;

    public Kategori_Produk_Adapter (Context context, ArrayList<KeteranganDAO> item) {
        this.context = context;
        this.item = item;
        this.itemFilterd = item;
        mContext = context;
    }

    @Override
    public Kategori_Produk_Adapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        ViewProcessHolder processHolder = new ViewProcessHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Kategori_Produk_Adapter.ViewProcessHolder holder, final int position) {
        final KeteranganDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.keterangan.setText(data.keterangan);
        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefManager.getSpRole().equals("Owner")) {
                    Intent intent = new Intent(mContext, kategori_produk_edit.class);
                    intent.putExtra("id", data.getId());
                    intent.putExtra("keterangan", data.getKeterangan());
                    mContext.startActivity(intent);
                }
                else {
                    Toast.makeText(context, "Anda Tidak Memiliki Hak Akses!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemFilterd.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemFilterd = item;
                } else {
                    ArrayList<KeteranganDAO> filteredList = new ArrayList<>();
                    for (KeteranganDAO row : item) {
                        if (row.getKeterangan().toLowerCase().contains(charString.toLowerCase())) {
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
                itemFilterd = (ArrayList<KeteranganDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView keterangan;
        CardView itemList;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            keterangan = itemView.findViewById(R.id.keterangan);
            itemList = itemView.findViewById(R.id.list_id);
            sharedPrefManager = new SharedPrefManager(context);
        }
    }
}



