package com.example.kouveepetshop.Pengelolaan.Produk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshop.Pengelolaan.KeteranganDAO;
import com.example.kouveepetshop.R;

import java.util.ArrayList;

public class Kategori_Produk_Adapter extends RecyclerView.Adapter<Kategori_Produk_Adapter.ViewProcessHolder> {
    Context context;
    private ArrayList<KeteranganDAO> item;
    private Context mContext;

    public Kategori_Produk_Adapter(Context context, ArrayList<KeteranganDAO> item) {
        this.context = context;
        this.item = item;
        mContext = context;
    }

    @Override
    public Kategori_Produk_Adapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        ViewProcessHolder processHolder = new ViewProcessHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final KeteranganDAO data = item.get(position);
        holder.id = data.id;
        holder.jenis.setText(data.keterangan);
//        holder.itemList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, HalamanEditProduk.class);
//                intent.putExtra("id_makanan", data.getIdProduk());
//                intent.putExtra("nama", data.getNama());
//                intent.putExtra("harga", data.getHarga());
//                intent.putExtra("keterangan", data.getKeterangan());
//                intent.putExtra("ketersediaan", data.getKetersediaan());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        Integer id;
        TextView jenis;
        CardView itemList;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            context = itemView.getContext();
            jenis = itemView.findViewById(R.id.keterangan);
            itemList = itemView.findViewById(R.id.list_id);
        }
    }
}



