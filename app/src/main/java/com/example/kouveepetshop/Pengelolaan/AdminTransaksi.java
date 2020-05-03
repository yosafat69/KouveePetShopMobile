package com.example.kouveepetshop.Pengelolaan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kouveepetshop.CS_MainActivity;
import com.example.kouveepetshop.Cashier_MainActivity;
import com.example.kouveepetshop.R;

public class AdminTransaksi extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_transaksi);

        ImageView cs = findViewById(R.id.admin_transaksi_cs);
        ImageView cashier = findViewById(R.id.admin_transaksi_cashier);

        cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTransaksi.this, CS_MainActivity.class);
                startActivity(intent);
            }
        });

        cashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTransaksi.this, Cashier_MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
