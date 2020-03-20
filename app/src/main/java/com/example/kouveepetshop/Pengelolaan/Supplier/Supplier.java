package com.example.kouveepetshop.Pengelolaan.Supplier;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Supplier extends AppCompatActivity {
    private String nama, no_telp, alamat, kota;
    private TextView nama_text, no_telp_text, alamat_text, kote_text;
    private ImageView tambah;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<SupplierDAO> mItems;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier);
        final PullRefreshLayout layout = findViewById(R.id.swipeRefreshLayout);

        init();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Supplier.this, Supplier_Tambah.class);
                startActivityForResult(i,1);
            }
        });

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItems.clear();
                GetData();
                layout.setRefreshing(false);
            }
        });

        GetData();
    }

    private void GetData(){
        mItems.clear();
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = "http://"+ip+"/rest_api-kouvee-pet-shop-master/index.php/Supplier/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");
                    for (int i = massage.length()-1; i > -1 ; i--){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        SupplierDAO supplier = new SupplierDAO();
                        supplier.setId(massageDetail.getInt("id"));
                        supplier.setNama(massageDetail.getString("nama"));
                        supplier.setNo_telp(massageDetail.getString("no_telp"));
                        supplier.setAlamat(massageDetail.getString("alamat"));
                        supplier.setKota(massageDetail.getString("kota"));
                        mItems.add(supplier);
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

    private void init(){
        pd = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.recycle_suppleir);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new Supplier_Adapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        tambah = findViewById(R.id.supplier_add);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GetData();
        mAdapter.notifyDataSetChanged();
    }
}
