package com.example.kouveepetshop.CS_Transaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Transaksi extends AppCompatActivity{
    private EditText cari;
    private ImageView tambah;

    private Transaksi_Adapter mAdapter;
    private ArrayList<TransaksiDAO> mItems;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_transaksi);
        final PullRefreshLayout layout = findViewById(R.id.swipeRefreshLayout);

        init();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaksi.this, Transaksi_Tambah.class);
                startActivityForResult(i,1);
            }
        });

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItems.clear();
                cari.setText("");
                GetData();
                layout.setRefreshing(false);
            }
        });

        GetData();

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

    private void GetData(){
        mItems.clear();
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/TransaksiLayanan/cs";
        String url_penjualan = ip + this.url + "index.php/TransaksiPenjualan/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");
                    for (int i = 0; i < massage.length() ; i++){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        TransaksiDAO cs_transaksi = new TransaksiDAO();
                        cs_transaksi.setId(massageDetail.getInt("id"));
                        cs_transaksi.setNo_transaksi(massageDetail.getString("no_transaksi"));
                        cs_transaksi.setNo_telp(massageDetail.getString("no_telp"));
                        cs_transaksi.setTotal(massageDetail.getDouble("total"));
                        cs_transaksi.setIsTransaksiLayanan(massageDetail.getInt("isTransaksiLayanan"));
                        cs_transaksi.setStatus(massageDetail.getString("status"));
                        mItems.add(cs_transaksi);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.cancel();
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "error : " + error.getMessage());
            }
        });
        Rest_API.getInstance(this).addToRequestQueue(arrayRequest);

        JsonObjectRequest arrayRequestPenjualan = new JsonObjectRequest(Request.Method.GET, url_penjualan, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");
                    for (int i = 0; i < massage.length() ; i++){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        TransaksiDAO cs_transaksi = new TransaksiDAO();
                        cs_transaksi.setId(massageDetail.getInt("id"));
                        cs_transaksi.setNo_transaksi(massageDetail.getString("no_transaksi"));
                        cs_transaksi.setNo_telp(massageDetail.getString("no_telp"));
                        cs_transaksi.setTotal(massageDetail.getDouble("total"));
                        cs_transaksi.setIsTransaksiLayanan(massageDetail.getInt("isTransaksiLayanan"));
                        cs_transaksi.setStatus(massageDetail.getString("status"));
                        mItems.add(cs_transaksi);
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
        Rest_API.getInstance(this).addToRequestQueue(arrayRequestPenjualan);
    }

    private void init(){
        pd = new ProgressDialog(this);
        RecyclerView mRecyclerView = findViewById(R.id.recycle_cs_transaksi);
        mItems = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new Transaksi_Adapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        tambah = findViewById(R.id.cs_transaksi_add);
        cari = findViewById(R.id.cs_transaksi_search);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            GetData();
            mAdapter.notifyDataSetChanged();
        }
    }
}
