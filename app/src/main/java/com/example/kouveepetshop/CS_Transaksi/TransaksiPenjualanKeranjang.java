package com.example.kouveepetshop.CS_Transaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransaksiPenjualanKeranjang extends AppCompatActivity {
    private EditText cari;
    private TransaksiPenjualanKeranjang_Adapter mAdapter;
    private ArrayList<DetilTransaksiPenjualanDAO> mItems;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private int id;
    private FloatingActionButton tambah, delete;
    private SharedPrefManager sharedPrefManager;
    private boolean deleteDoubleTap = false;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_transaksi_penjualan_keranjang);
        final PullRefreshLayout layout = findViewById(R.id.swipeRefreshLayout);

        init();

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItems.clear();
                cari.setText("");
                getPenjualan();
                layout.setRefreshing(false);
            }
        });

        getPenjualan();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiPenjualanKeranjang.this,TransaksiPenjualan.class);
                intent.putExtra("isTambah", 1);
                intent.putExtra("id_transaksi", sharedPrefManager.getSpIdTransaksi());
                startActivityForResult(intent,1);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteDoubleTap) {
                    delete();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 1000);
                }
                else {
                    deleteDoubleTap = true;
                    Toast.makeText(TransaksiPenjualanKeranjang.this, "Tekan Lagi Untuk Delete", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            deleteDoubleTap = false;
                        }
                    }, 2000);
                }
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
        mRecyclerView = findViewById(R.id.recycle_cs_transaksi_penjualan_keranjang);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new TransaksiPenjualanKeranjang_Adapter(this, mItems, this.getSupportFragmentManager());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(mAdapter);
        cari = findViewById(R.id.cs_transaksi_penjualan_keranjang_search);
        id = getIntent().getIntExtra("id", -1);
        tambah = findViewById(R.id.cs_transaksi_penjualan_keranjang_tambah);
        sharedPrefManager = new SharedPrefManager(this);
        delete = findViewById(R.id.cs_transaksi_penjualan_keranjang_delete);
    }

    private void delete(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/TransaksiPenjualan/cancel/"+id;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
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
                request.put("updated_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void getPenjualan(){
        mItems.clear();
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/DetilTransaksiPenjualan/"+id;

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");
                    for (int i = massage.length()-1; i > -1 ; i--){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        DetilTransaksiPenjualanDAO produk = new DetilTransaksiPenjualanDAO();
                        produk.setId(massageDetail.getInt("id"));
                        produk.setId_produk(massageDetail.getInt("id_produk"));
                        produk.setId_transaksi(massageDetail.getInt("id_transaksi"));
                        produk.setNama(massageDetail.getString("nama"));
                        produk.setJumlah(massageDetail.getInt("jumlah"));
                        produk.setHarga(massageDetail.getDouble("harga"));
                        produk.setSubtotal(massageDetail.getDouble("subtotal"));
                        produk.setLink(massageDetail.getString("link_gambar"));
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
        if (resultCode == 1) {
            getPenjualan();
            mAdapter.notifyDataSetChanged();
        }
    }
}
