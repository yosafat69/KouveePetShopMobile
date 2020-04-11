package com.example.kouveepetshop.Pengelolaan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kouveepetshop.Pengelolaan.Hewan.Hewan;
import com.example.kouveepetshop.Pengelolaan.Hewan.Jenis_Hewan;
import com.example.kouveepetshop.Pengelolaan.Hewan.Ukuran_Hewan;
import com.example.kouveepetshop.Pengelolaan.Layanan.Layanan;
import com.example.kouveepetshop.Pengelolaan.Produk.Kategori_Produk;
import com.example.kouveepetshop.Pengelolaan.Produk.Produk;
import com.example.kouveepetshop.Pengelolaan.Supplier.Supplier;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

public class Pengelolaan extends AppCompatActivity {

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengelolaan);

        sharedPrefManager = new SharedPrefManager(this);

        ImageView hewan = findViewById(R.id.menu_hewan);
        hewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pengelolaan.this, Hewan.class);
                startActivity(i);
            }
        });

        ImageView jenis_hewan = findViewById(R.id.menu_jenis_hewan);
        jenis_hewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pengelolaan.this, Jenis_Hewan.class);
                startActivity(i);
            }
        });

        ImageView ukuran_hewan = findViewById(R.id.menu_ukuran_hewan);
        ukuran_hewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pengelolaan.this, Ukuran_Hewan.class);
                startActivity(i);
            }
        });

        ImageView layanan = findViewById(R.id.menu_layanan);
        layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pengelolaan.this, Layanan.class);
                startActivity(i);
            }
        });

        ImageView produk = findViewById(R.id.menu_produk);
        produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefManager.getSpRole().equals("Owner")) {
                    Intent i = new Intent(Pengelolaan.this, Produk.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Pengelolaan.this, "Anda Tidak Memiliki Hak Akses!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView kategori_produk = findViewById(R.id.menu_kategori_produk);
        kategori_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pengelolaan.this, Kategori_Produk.class);
                startActivity(i);
            }
        });

        ImageView supplier = findViewById(R.id.menu_supplier);
        supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefManager.getSpRole().equals("Owner")) {
                    Intent i = new Intent(Pengelolaan.this, Supplier.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Pengelolaan.this, "Anda Tidak Memiliki Hak Akses!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView Jenis_Layanan = findViewById(R.id.menu_jenis_layanan);
        Jenis_Layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pengelolaan.this, com.example.kouveepetshop.Pengelolaan.Layanan.Jenis_Layanan.class);
                startActivity(i);
            }
        });

        ImageView Pegawai = findViewById(R.id.menu_pegawai);
        Pegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pengelolaan.this, com.example.kouveepetshop.Pengelolaan.Pegawai.Pegawai.class);
                startActivity(i);
            }
        });
    }
}
