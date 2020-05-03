package com.example.kouveepetshop.Cashier_Transaksi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kouveepetshop.Cashier_MainActivity;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Nota extends AppCompatActivity {
    private int isTransaksiLayanan;
    private PDFView webView;
    private String link, diskon, no_transaksi;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nota);

        diskon = getIntent().getStringExtra("diskon");
        no_transaksi = getIntent().getStringExtra("no_transaksi");
        isTransaksiLayanan = getIntent().getIntExtra("isTransaksiLayanan", -1);

        if (isTransaksiLayanan == 0) {
            link = ip + url + "index.php/NotaProduk?no="+no_transaksi+"&diskon="+diskon;
        }
        else {
            link = ip + url + "index.php/NotaLayanan?no="+no_transaksi+"&diskon="+diskon;
        }

        Log.d("URL", link);

        webView = findViewById(R.id.web);

        new PDF().execute(link);
    }

    @SuppressLint("StaticFieldLeak")
    class PDF extends AsyncTask<String,Void, byte[]>{

        @Override
        protected byte[] doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            try {
                return IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            webView.fromBytes(bytes).load();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Nota.this, Cashier_MainActivity.class);
        startActivity(intent);
    }
}
