package com.example.kouveepetshop.Pengelolaan.Layanan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kouveepetshop.R;

import java.util.ArrayList;

public class Layanan_Adapter extends RecyclerView.Adapter<Layanan_Adapter.ViewProcessHolder> {
    Context context;
    private ArrayList<LayananDAO> item, itemFilterd;
    private Context mContext;

    public Layanan_Adapter(Context context, ArrayList<LayananDAO> item) {
        this.context = context;
        this.item = item;
        this.itemFilterd = item;
        mContext = context;
    }

    @Override
    public Layanan_Adapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layanan, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final LayananDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.keterangan);
        holder.ukuran.setText(data.ukuran);
        holder.harga.setText(Integer.toString(data.harga));
        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Layanan_Edit.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("nama", data.getKeterangan());
                intent.putExtra("harga", data.getHarga());
                intent.putExtra("ukuran", data.getUkuran());

                mContext.startActivity (intent);
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
                    ArrayList<LayananDAO> filteredList = new ArrayList<>();
                    for (LayananDAO row : item) {
                        if (row.getKeterangan().toLowerCase().contains(charString.toLowerCase()) || row.getUkuran().contains(charSequence)) {
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
        TextView nama, ukuran, harga;
        CardView itemList;
        ImageView gambar;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            nama = itemView.findViewById(R.id.layanan_nama);
            ukuran = itemView.findViewById(R.id.layanan_ukuran);
            harga = itemView.findViewById(R.id.layanan_harga);
            itemList = itemView.findViewById(R.id.list_layanan_id);
            gambar = itemView.findViewById(R.id.layanan_gambar);
        }
    }
}



