package com.example.kouveepetshop.CS_Transaksi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengelolaan.Member.Member_Tambah;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Transaksi_Tambah extends AppCompatActivity {
    private Button layanan, penjualan, member;
    private EditText no_telp_text;
    private CheckBox isMember;
    private Integer is_member, id_transaksi;
    private String no_telp;
    private boolean isNoTelpTersedia = false;

    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_transaksi_tambah);

        init();

        penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()){
                    createTransaksiPenjualan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        if (isNoTelpTersedia) {
                            Intent intent = new Intent(Transaksi_Tambah.this, TransaksiPenjualan.class);
                            intent.putExtra("id_transaksi",id_transaksi);
                            startActivity(intent);
                        }
                        else {
                            no_telp_text.setError("Member dengan Nomor Telepon tidak tersedia");
                        }
                        }
                    }, 500);
                }
            }
        });

        layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()){
                    createTransaksiLayanan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isNoTelpTersedia) {
                                Intent intent = new Intent(Transaksi_Tambah.this, TransaksiLayanan.class);
                                intent.putExtra("id_transaksi",id_transaksi);
                                startActivity(intent);
                            }
                            else {
                                no_telp_text.setError("Nomor Telepon Sudah Dipakai");
                            }
                        }
                    }, 500);
                }
            }
        });

        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transaksi_Tambah.this, Member_Tambah.class);
                startActivity(intent);
            }
        });
    }

    private void getValue(){
        if (isMember.isChecked()){
            is_member = 1;
        }
        else {
            is_member = 0;
        }
        no_telp = no_telp_text.getText().toString();
    }

    private void createTransaksiPenjualan(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/TransaksiPenjualan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            if (!jsonObject.getString("error").equals("true")) {
                                isNoTelpTersedia = true;
                                id_transaksi = Integer.parseInt(jsonObject.getString("message"));
                            }
                            Log.d("Response", jsonObject.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                request.put("is_member", String.valueOf(is_member));
                request.put("no_telp",  String.valueOf(no_telp));
                request.put("id_CS", sharedPrefManager.getSpUsername());
                request.put("created_by", sharedPrefManager.getSpUsername());
                request.put("updated_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void createTransaksiLayanan() {
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/TransaksiLayanan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            if (!jsonObject.getString("error").equals("true")) {
                                isNoTelpTersedia = true;
                                id_transaksi = Integer.parseInt(jsonObject.getString("message"));
                            }
                            Log.d("Response", jsonObject.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> request = new HashMap<>();
                request.put("is_member", String.valueOf(is_member));
                request.put("no_telp", String.valueOf(no_telp));
                request.put("id_CS", sharedPrefManager.getSpUsername());
                request.put("created_by", sharedPrefManager.getSpUsername());
                request.put("updated_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private boolean validasi(){
        int cek = 0;
        if (no_telp_text.getText().toString().equals("")) {
            no_telp_text.setError("Nomor Telepon Tidak Boleh Kosong");
            cek = 1;
        }
        else if (no_telp_text.getText().toString().length() < 10 || no_telp_text.getText().toString().length() > 13 ) {
            no_telp_text.setError("Nomor Telepon 10 - 13 Karakter");
            cek = 1;
        }
        else if (!String.valueOf(no_telp_text.getText().toString().charAt(0)).equals("0") && !String.valueOf(no_telp_text.getText().toString().charAt(1)).equals("8")) {
            no_telp_text.setError("Format Nomor Telepon Salah");
            cek = 1;
        }
        return cek == 0;
    }

    private void init(){
        layanan = findViewById(R.id.cs_transaksi_tambah_transaksi_layanan);
        penjualan = findViewById(R.id.cs_transaksi_tambah_transaksi_penjualan);
        no_telp_text = findViewById(R.id.cs_transaksi_tambah_no_telp);
        member = findViewById(R.id.cs_transaksi_tambah_daftar_member);
        isMember= findViewById(R.id.cs_transaksi_tambah_isMember);
        sharedPrefManager = new SharedPrefManager(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
