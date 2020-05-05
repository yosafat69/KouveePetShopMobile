package com.example.kouveepetshop.Pengadaan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.example.kouveepetshop.API.Rest_API;

import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengelolaan.Produk.ProdukDAO;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class detilpengadaan_tambah extends AppCompatActivity
{
    private EditText cari;
    private DetilPengadaanAdapter mAdapter;
    private ArrayList<ProdukDAO> mItems;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private int id_pemesanan,id;
    private SharedPrefManager sharedPrefManager;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;

    FloatingActionButton keranjang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detilpengadaan_produk);
        final PullRefreshLayout layout = findViewById(R.id.swipeRefreshLayout);

        init();

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItems.clear();
                cari.setText("");
                get();
                layout.setRefreshing(false);
            }
        });

        get();



        id = getIntent().getIntExtra("id", -1);
        sharedPrefManager.saveSPInt("spIdPemesanan", id);
        Log.d("id",String.valueOf(id));

        keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detilpengadaan_tambah.this, DetilPengadaan_Keranjang_Tambah_Edit.class);
                intent.putExtra("id", id);
                Log.d("id",String.valueOf(id));
                startActivity(intent);

            }
        });

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.getFilter().filter(s);
            }
        });
    }

    private void init(){
        pd = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.recycle_detilpengadaan_produk);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new DetilPengadaanAdapter(this, mItems, this.getSupportFragmentManager());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(mAdapter);
        cari = findViewById(R.id.detilpengadaan_produk_cari);
        sharedPrefManager = new SharedPrefManager(this);
        keranjang = findViewById(R.id.detil_pengadaan_keranjang);
        id = sharedPrefManager.getSpIdPemesanan();
    }

    private void get(){
        mItems.clear();
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/Produk/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");
                    for (int i = massage.length()-1; i > -1 ; i--){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        ProdukDAO produk = new ProdukDAO();
                        produk.setId(massageDetail.getInt("id"));
                        produk.setNama(massageDetail.getString("nama"));
                        produk.setKategori(massageDetail.getString("id_kategori_produk"));
                        produk.setJmlh(massageDetail.getInt("jmlh"));
                        produk.setLink_gambar(massageDetail.getString("link_gambar"));
                        mItems.add(produk);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
                pd.cancel();
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "error : " + error.getMessage());
            }
        });
        Rest_API.getInstance(this).addToRequestQueue(arrayRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            get();
            mAdapter.notifyDataSetChanged();
        }
    }
}

