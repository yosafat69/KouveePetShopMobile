package com.example.kouveepetshop.Pengelolaan.Hewan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.example.kouveepetshop.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ukuran_Hewan extends AppCompatActivity {

    private Ukuran_Hewan_Adapter mAdapter;
    private ArrayList<KeteranganDAO> mItems;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private RecyclerView.LayoutManager mManager;
    private RecyclerView mRecyclerView;
    private EditText ukuran_hewan, cari;
    private Button tambah;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ukuran_hewan);
        final PullRefreshLayout layout = findViewById(R.id.swipeRefreshLayout);

        init();

        ambilData();
        if (!sharedPrefManager.getSpRole().equals("Owner")) {
            ukuran_hewan.setEnabled(false);
        }

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefManager.getSpRole().equals("Owner")) {
                    addUkuran();
                    ambilData();
                    mAdapter.notifyDataSetChanged();
                }

                else {
                    Toast.makeText(Ukuran_Hewan.this, "Anda Tidak Memiliki Hak Akses!", Toast.LENGTH_SHORT).show();
                }
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


    private void ambilData(){
        mItems.clear();
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Ukuranhewan";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i = massage.length()-1; i > -1 ; i--){
                        JSONObject massageDetail = massage.getJSONObject(i);
                        KeteranganDAO hewan = new KeteranganDAO();
                        hewan.setId(massageDetail.getInt("id"));
                        hewan.setKeterangan(massageDetail.getString("nama"));
                        mItems.add(hewan);
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

    private void addUkuran(){
        final String ukuran = ukuran_hewan.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Ukuranhewan";
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
                Map<String, String>  request = new HashMap<String, String>();
                request.put("nama", ukuran);
                request.put("created_by", "Yosafat9204");
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void init()
    {
        pd = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.recycle_ukuran_hewan);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new Ukuran_Hewan_Adapter(this,mItems);
        mRecyclerView.setAdapter(mAdapter);
        ukuran_hewan = findViewById(R.id.ukuran_hewan_tambah);
        tambah = findViewById(R.id.ukuran_hewan_add);
        cari = findViewById(R.id.jenis_hewan_search);
        sharedPrefManager = new SharedPrefManager(this);
    }
}

