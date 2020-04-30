package com.example.kouveepetshop.Pengelolaan.Hewan;

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

import com.example.kouveepetshop.Pengelolaan.Pegawai.Pegawai_Edit;
import com.example.kouveepetshop.R;

import java.util.ArrayList;

public class Hewan_Adapter extends RecyclerView.Adapter<Hewan_Adapter.ViewProcessHolder> implements Filterable {
    private ArrayList<HewanDAO> item;
    private ArrayList<HewanDAO> itemFilterd;
    private Context mContext;


    public Hewan_Adapter(Context context, ArrayList<HewanDAO> item) {
        this.itemFilterd = item;
        this.item = item;
        mContext = context;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hewan, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final HewanDAO data = item.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama);
        holder.tanggal_lahir.setText(data.tanggal_lahir);
        holder.jenis.setText(data.jenis);
        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, edit_hewan.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("tanggal_lahir", data.getTanggal_lahir());
                intent.putExtra("jenis", data.getJenis());
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
        TextView nama, tanggal_lahir, jenis;
        CardView itemList;


        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.hewan_nama);
            tanggal_lahir = itemView.findViewById(R.id.hewan_tanggal_lahir);
            jenis = itemView.findViewById(R.id.hewan_jenis);
            itemList = itemView.findViewById(R.id.list_hewan_id);

        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemFilterd = item;
                } else {
                    ArrayList<HewanDAO> filteredList = new ArrayList<>();
                    for (HewanDAO row : item) {
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase())){
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
                itemFilterd = (ArrayList<HewanDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}



