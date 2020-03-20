package com.example.kouveepetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.kouveepetshop.Pengelolaan.Pengelolaan;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView pengelolaan = findViewById(R.id.menu_pengelolaan);
        ImageView transaksi = findViewById(R.id.menu_transaksi);
        ImageView pelaporan = findViewById(R.id.menu_pelaporan);
        ImageView pengadaan = findViewById(R.id.menu_pengadaan);

        pengelolaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Pengelolaan.class);
                startActivity(i);
            }
        });
    }

    public static String getIp(){
        return "192.168.43.202";
    }
}
