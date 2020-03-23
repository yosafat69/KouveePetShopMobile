package com.example.kouveepetshop.Pengelolaan.Layanan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class Layanan_Tambah extends AppCompatActivity {


    private Integer harga,id_ukuran,id_nama;
    private final String id_pegawai = "Yosafat9204";
    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayList<String> mUkuran = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterUkuran;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private Spinner nama_spinner,ukuran_spinner;
    private ArrayList<KeteranganDAO> nama_layanan;
    private ArrayList<KeteranganDAO> ukuran_layanan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layanan_add);
        pd = new ProgressDialog(this);
        mItems = new ArrayList<>();
        mUkuran = new ArrayList<>();
        nama_layanan = new ArrayList<>();
        ukuran_layanan = new ArrayList<>();


        Button tambah = findViewById(R.id.layanan_tambah_add);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterUkuran = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mUkuran);
        adapterUkuran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nama_spinner = findViewById(R.id.layanan_add_spinner);
        nama_spinner.setAdapter(adapter);
        ukuran_spinner = findViewById(R.id.layanan_spinner_ukuran);
        ukuran_spinner.setAdapter(adapterUkuran);


        loadjson();
        loadUkuran();
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduk();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

    }

    private void addProduk(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Layanan";
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
                request.put("id_ukuran_hewan", String.valueOf(id_ukuran));
                request.put("id_layanan", String.valueOf(id_nama));
                request.put("harga", String.valueOf(harga));
                request.put("created_by", id_pegawai);
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void loadjson() {
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

                    for (int i = 0; i < massage.length(); i++) {
                        JSONObject massageDetail = massage.getJSONObject(i);
                        mItems.add(massageDetail.getString("nama"));

                        KeteranganDAO nama = new KeteranganDAO();
                        nama.setId(massageDetail.getInt("id"));
                        nama.setKeterangan(massageDetail.getString("nama"));
                        nama_layanan.add(nama);

                        adapter.notifyDataSetChanged();
                    }
                    pd.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "error : " + error.getMessage());
            }
        });
        Rest_API.getInstance(this).addToRequestQueue(arrayRequest);
    }

    private void loadUkuran() {
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

                    for (int i = 0; i < massage.length(); i++) {
                        JSONObject massageDetail = massage.getJSONObject(i);
                        mUkuran.add(massageDetail.getString("nama"));

                        KeteranganDAO ukuran = new KeteranganDAO();
                        ukuran.setId(massageDetail.getInt("id"));
                        ukuran.setKeterangan(massageDetail.getString("nama"));
                        ukuran_layanan.add(ukuran);

                        adapterUkuran.notifyDataSetChanged();
                    }
                    pd.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "error : " + error.getMessage());
            }
        });
        Rest_API.getInstance(this).addToRequestQueue(arrayRequest);
    }

    private void getValue(){
        EditText harga_text = findViewById(R.id.layanan_tambah_harga);
        String Nama = nama_spinner.getSelectedItem().toString();
        String Ukuran = ukuran_spinner.getSelectedItem().toString();
        KeteranganDAO nama = new KeteranganDAO();
        KeteranganDAO ukuran = new KeteranganDAO();
        for (int i = 0 ; i < nama_layanan.size(); i++) {
            nama = nama_layanan.get(i);
            if(nama.getKeterangan().equals(Nama)){
                break;
            }
        }
        for (int i = 0 ; i < ukuran_layanan.size(); i++) {
            ukuran = ukuran_layanan.get(i);
            if(ukuran.getKeterangan().equals(Ukuran)){
                break;
            }
        }
        id_nama= nama.getId();
        id_ukuran= ukuran.getId();
        harga = Integer.parseInt(harga_text.getText().toString());

    }
}
