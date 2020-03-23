package com.example.kouveepetshop.Pengelolaan.Layanan;

import android.content.Context;
import android.content.Intent;
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

public class Jenis_Layanan_Adapter extends RecyclerView.Adapter<Jenis_Layanan_Adapter.ViewProcessHolder>
{
    Context context;
    private ArrayList<KeteranganDAO> item;
    private Context mContext;

    public Jenis_Layanan_Adapter(Context context, ArrayList<KeteranganDAO> item) {
        this.context = context;
        this.item = item;
        mContext = context;
    }

    @Override
    public Jenis_Layanan_Adapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        ViewProcessHolder processHolder = new ViewProcessHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final KeteranganDAO data = item.get(position);
        holder.id = data.id;
        holder.keterangan.setText(data.keterangan);
        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Jenis_layanan_Edit.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("keterangan", data.getKeterangan());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
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
        }
    }
}
