package com.example.kouveepetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kouveepetshop.Pengelolaan.Pengelolaan;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private SharedPrefManager sharedPrefManager;
    private ImageView logout, pengelolaan, transaksi, pelaporan, pengadaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefManager = new SharedPrefManager(this);

        init();

        pengelolaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Pengelolaan.class);
                startActivity(i);
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

    private void init() {
        pengelolaan = findViewById(R.id.menu_pengelolaan);
        transaksi = findViewById(R.id.menu_transaksi);
        pelaporan = findViewById(R.id.menu_pelaporan);
        pengadaan = findViewById(R.id.menu_pengadaan);
        logout = findViewById(R.id.menu_logout);
    }

    @Override
    public void onBackPressed() {
        if (sharedPrefManager.getSPSudahLogin()){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                System.exit(0);
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    public static String getIp(){ return "http://192.168.1.5"; }

    public static String getUrl() {
        return "/rest_api-kouvee-pet-shop/";
    }
}
