package com.example.kouveepetshop.Pengelolaan.Layanan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.kouveepetshop.Pengelolaan.KeteranganDAO;
import com.example.kouveepetshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Jenis_Layanan extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private ArrayList<KeteranganDAO> mItems;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private RecyclerView.LayoutManager mManager;
    private RecyclerView mRecyclerView;
    private EditText jenis_layanan;
    private Button tambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jenis__layanan);
        final PullRefreshLayout layout = findViewById(R.id.swipeRefreshLayout);

        init();

        ambilData();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addjenislayanan();
                ambilData();
                mAdapter.notifyDataSetChanged();
            }
        });

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItems.clear();
                ambilData();
                layout.setRefreshing(false);
            }
        });
    }


    private void ambilData(){
        mItems.clear();
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/jenislayanan";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i = massage.length()-1; i > -1 ; i--){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        KeteranganDAO layanan = new KeteranganDAO();
                        layanan.setId(massageDetail.getInt("id"));
                        layanan.setKeterangan(massageDetail.getString("nama"));
                        mItems.add(layanan);
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

    private void addjenislayanan(){
        final String layanan = jenis_layanan.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/jenislayanan";
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
        )

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  request = new HashMap<String, String>();
                request.put("nama", layanan);
                request.put("created_by", "Yosafat9204");
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void init()
    {
        pd = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.recycle_jenis_layanan);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new Jenis_Layanan_Adapter(this,mItems);
        mRecyclerView.setAdapter(mAdapter);
        jenis_layanan = findViewById(R.id.jenis_layanan_tambah);
        tambah = findViewById(R.id.jenis_layanan_add);
    }
}
