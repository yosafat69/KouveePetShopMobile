package com.example.kouveepetshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.CS_Transaksi.TransaksiDAO;
import com.example.kouveepetshop.Cashier_Transaksi.Cashier_Transaksi_Adapter;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cashier_MainActivity extends AppCompatActivity{
    private EditText cari;

    private Cashier_Transaksi_Adapter mAdapter;
    private ArrayList<TransaksiDAO> mItems;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private boolean doubleBackToExitPressedOnce = false;
    private SharedPrefManager sharedPrefManager;
    private FloatingActionButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier_activity_main);
        final PullRefreshLayout layout = findViewById(R.id.swipeRefreshLayout);

        init();

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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN,false);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME,"");
                sharedPrefManager.saveSPString(SharedPrefManager.SP_ROLE,"");
                Intent intent = new Intent(Cashier_MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void GetData(){
        mItems.clear();
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/TransaksiLayanan/cashier";
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
        RecyclerView mRecyclerView = findViewById(R.id.recycle_cashier_pembayaran);
        mItems = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new Cashier_Transaksi_Adapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        cari = findViewById(R.id.cashier_pembayaran_search);
        sharedPrefManager = new SharedPrefManager(this);
        logout = findViewById(R.id.cashier_pembayaran_logout);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            GetData();
            mAdapter.notifyDataSetChanged();
        }
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
}
