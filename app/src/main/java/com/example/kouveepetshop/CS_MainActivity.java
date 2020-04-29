package com.example.kouveepetshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kouveepetshop.CS_Transaksi.Transaksi;
import com.example.kouveepetshop.CS_Transaksi.TransaksiPenjualan;
import com.example.kouveepetshop.Pengelolaan.Member.Member;

public class CS_MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private SharedPrefManager sharedPrefManager;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_activity_main);

        sharedPrefManager = new SharedPrefManager(this);

        ImageView pengelolaan = findViewById(R.id.cs_menu_pengelolaan_member);
        ImageView transaksi = findViewById(R.id.cs_menu_transaksi);
        ImageView logout = findViewById(R.id.cs_menu_logout);

        pengelolaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CS_MainActivity.this, Member.class);
                startActivity(intent);
            }
        });

        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CS_MainActivity.this, Transaksi.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN,false);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME,"");
                sharedPrefManager.saveSPString(SharedPrefManager.SP_ROLE,"");
                finish();
            }
        });

        Toast.makeText(this, sharedPrefManager.getSpUsername() + " " + sharedPrefManager.getSpRole(), Toast.LENGTH_LONG).show();
    }
}
