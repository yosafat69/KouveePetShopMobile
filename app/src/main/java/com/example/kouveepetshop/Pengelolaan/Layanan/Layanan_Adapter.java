package com.example.kouveepetshop.Pengelolaan.Layanan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Layanan_Adapter extends RecyclerView.Adapter<Layanan_Adapter.ViewProcessHolder> {
    Context context;
    private ArrayList<LayananDAO> item, itemFilterd;
    private Context mContext;
    private SharedPrefManager sharedPrefManager;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

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
        String link = ip + this.url;

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        final LayananDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.keterangan);
        holder.ukuran.setText(data.ukuran);
        holder.harga.setText(formatRupiah.format(data.harga));

        final String url_gambar = link + data.gambar;
        Picasso.get().load(url_gambar).into(holder.gambar);

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefManager.getSpRole().equals("Owner")) {
                    Intent intent = new Intent(mContext, Layanan_Edit.class);
                    intent.putExtra("id", data.getId());
                    intent.putExtra("nama", data.getKeterangan());
                    intent.putExtra("harga", data.getHarga());
                    intent.putExtra("ukuran", data.getUkuran());
                    intent.putExtra("url_gambar", url_gambar);
                    mContext.startActivity (intent);
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
            sharedPrefManager = new SharedPrefManager(context);
        }
    }
}



