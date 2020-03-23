package com.example.kouveepetshop.Pengelolaan.Layanan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengelolaan.Produk.Produk;
import com.example.kouveepetshop.Pengelolaan.Produk.Produk_Adapter;
import com.example.kouveepetshop.Pengelolaan.Produk.Produk_Tambah;
import com.example.kouveepetshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Layanan extends AppCompatActivity {

    private Layanan_Adapter mAdapter;
    private ArrayList<LayananDAO> mItems;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mManager;
    ImageView tambah;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layanan);
        final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        init();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Layanan.this, Layanan_Tambah.class);
                startActivityForResult(i,1);
            }
        });


        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItems.clear();
                cari.setText("");
                loadjson();
                layout.setRefreshing(false);
            }
        });

        loadjson();

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) { mAdapter.getFilter().filter(s); }
        });

    }

    private void init(){
        pd = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.recycle_layanan);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new Layanan_Adapter(this,mItems);
        mRecyclerView.setAdapter(mAdapter);
        tambah = findViewById(R.id.layanan_add);
        cari = findViewById(R.id.layanan_search);
    }

    private void loadjson(){

        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = "http://"+ip+"/rest_api-kouvee-pet-shop-master/index.php/Layanan/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i=0; i < massage.length(); i++){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        LayananDAO layanan = new LayananDAO();
                        layanan.setKeterangan(massageDetail.getString("id_layanan"));
                        layanan.setUkuran(massageDetail.getString("id_ukuran_hewan"));
                        layanan.setHarga(massageDetail.getInt("harga"));
                        mItems.add(layanan);
                    }
                    pd.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
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
        loadjson();
        mAdapter.notifyDataSetChanged();
    }
}
