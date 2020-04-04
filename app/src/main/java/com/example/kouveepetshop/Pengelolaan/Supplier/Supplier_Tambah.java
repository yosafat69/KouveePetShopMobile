package com.example.kouveepetshop.Pengelolaan.Supplier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;


public class Supplier_Tambah extends AppCompatActivity {

    private String nama, no_telp, alamat, kota;
    private String ip = MainActivity.getIp();
    private Button tambah;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_add);

        sharedPrefManager = new SharedPrefManager(this);
        init();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSupplier();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK,returnIntent);
                        finish();
                    }
                }, 500);
            }
        });
    }

    private void addSupplier(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Supplier";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  request = new HashMap<>();
                request.put("nama", nama);
                request.put("no_telp",  String.valueOf(no_telp));
                request.put("alamat", String.valueOf(alamat));
                request.put("kota", String.valueOf(kota));
                request.put("created_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void init(){
        tambah = findViewById(R.id.supplier_tambah_add);
    }

    private void getValue(){
        EditText nama_text = findViewById(R.id.supplier_tambah_nama);
        EditText no_telp_text = findViewById(R.id.supplier_tambah_no_telp);
        EditText alamat_text = findViewById(R.id.supplier_tambah_alamat);
        EditText kota_text = findViewById(R.id.supplier_tambah_kota);

        nama = nama_text.getText().toString();
        no_telp = no_telp_text.getText().toString();
        alamat = alamat_text.getText().toString();
        kota = kota_text.getText().toString();
    }

}
