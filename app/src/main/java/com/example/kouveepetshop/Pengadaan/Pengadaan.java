package com.example.kouveepetshop.Pengadaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengelolaan.Layanan.Layanan;
import com.example.kouveepetshop.Pengelolaan.Layanan.Layanan_Adapter;
import com.example.kouveepetshop.Pengelolaan.Layanan.Layanan_Tambah;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pengadaan extends AppCompatActivity {

    private EditText cari;
    private ImageView tambah;
    private Pengadaan_Adapter mAdapter;
    private ArrayList<PengadaanDAO> mItems;
    private Integer id_pemesanan;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengadaan);
        final PullRefreshLayout layout = findViewById(R.id.swipeRefreshLayout);

        init();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(Pengadaan.this, Pengadaan_tambah.class);
                startActivity(k);
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
        String url = ip + this.url + "index.php/Pemesanan/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");
                    for (int i = 0; i < massage.length() ; i++){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        PengadaanDAO pengadaan = new PengadaanDAO();
                        pengadaan.setId(massageDetail.getInt("id"));
                        pengadaan.setNo_pemesanan(massageDetail.getString("no_PO"));
                        pengadaan.setId_supplier(massageDetail.getString("id_supplier"));
                        pengadaan.setTgl_pemesanan(massageDetail.getString("tgl_pemesanan"));
                        pengadaan.setStatus(massageDetail.getString("status"));
                        mItems.add(pengadaan);
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

    private void init(){
        pd = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.recycle_pengadaan);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new Pengadaan_Adapter(this,mItems);
        mRecyclerView.setAdapter(mAdapter);
        cari = findViewById(R.id.pemesanan_search);
        tambah = findViewById(R.id.pengadaan_add);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            GetData();
            mAdapter.notifyDataSetChanged();
        }
    }
}
