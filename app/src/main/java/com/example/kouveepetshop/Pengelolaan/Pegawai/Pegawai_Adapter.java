package com.example.kouveepetshop.Pengelolaan.Pegawai;

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

public class Pegawai_Adapter extends RecyclerView.Adapter<Pegawai_Adapter.ViewProcessHolder> implements Filterable {
    private ArrayList<PegawaiDAO> item;
    private ArrayList<PegawaiDAO> itemFilterd;
    private Context mContext;

    public Pegawai_Adapter(Context context, ArrayList<PegawaiDAO> item) {
        this.item = item;
        this.itemFilterd = item;
        mContext = context;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pegawai, parent, false);
        return new ViewProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProcessHolder holder, final int position) {
        final PegawaiDAO data = itemFilterd.get(position);
        holder.id = data.id;
        holder.nama.setText(data.nama);
        holder.no_telp.setText(data.no_telp);
        holder.alamat.setText(data.alamat);
        holder.tanggal_lahir.setText(data.tanggal_lahir);
        holder.username.setText(data.username);
        holder.role.setText(data.role);

        holder.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Pegawai_Edit.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("role", data.getRole());
                intent.putExtra("alamat", data.getAlamat());
                intent.putExtra("no_telp", data.getNo_telp());
                intent.putExtra("tanggal_lahir", data.getTanggal_lahir());
                intent.putExtra("username", data.getUsername());
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
        TextView nama, no_telp, alamat, tanggal_lahir, role, username;
        CardView itemList;

        public ViewProcessHolder(@NonNull final View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.pegawai_nama);
            no_telp = itemView.findViewById(R.id.pegawai_no_telp);
            alamat = itemView.findViewById(R.id.pegawai_alamat);
            tanggal_lahir = itemView.findViewById(R.id.pegawai_tanggal_lahir);
            role = itemView.findViewById(R.id.pegawai_role);
            username = itemView.findViewById(R.id.pegawai_username);
            itemList = itemView.findViewById(R.id.list_pegawai_id);
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
                    ArrayList<PegawaiDAO> filteredList = new ArrayList<>();
                    for (PegawaiDAO row : item) {
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || row.getRole().toLowerCase().contains(charString.toLowerCase())) {
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
                itemFilterd = (ArrayList<PegawaiDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}



