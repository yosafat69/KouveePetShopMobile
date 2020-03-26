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

public class Layanan_Edit extends AppCompatActivity {

    String nama,ukuran;
    Integer harga,id_nama,id,id_ukuran;
    EditText harga_text;
    Spinner nama_spinner,ukuran_spinner;

    Button edit, delete;

    private ProgressDialog pd;
    private String ip = MainActivity.getIp();

    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayList<String> mUkuran = new ArrayList<>();
    private ArrayList<KeteranganDAO> nama_layanan;
    private ArrayList<KeteranganDAO> ukuran_layanan;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterUkuran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layanan__edit);

        init();
        LoadLayanan();
        LoadUkuran();
        setText();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editlayanan();

                //Supaya dia balik ke halaman sebelumnya kalau udah selesai edit
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletlayanan();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    //Ngmbil data Kategori layanan untuk spinnernya
    private void LoadLayanan() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/jenislayanan/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i = 0; i < massage.length(); i++) {
                        JSONObject massageDetail = massage.getJSONObject(i);

                        mItems.add(massageDetail.getString("nama"));

                        KeteranganDAO keterangan = new KeteranganDAO();
                        keterangan.setId(massageDetail.getInt("id"));
                        keterangan.setKeterangan(massageDetail.getString("nama"));
                        nama_layanan.add(keterangan);

                        adapter.notifyDataSetChanged();
                    }

                    String jenis = getIntent().getStringExtra("nama");
                    for (int i = 0 ; i < mItems.size(); i++) {
                        if(mItems.get(i).equals(jenis)){
                            nama_spinner.setSelection(i);
                            break;
                        }
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

    private void LoadUkuran() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/ukuranhewan/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i = 0; i < massage.length(); i++) {
                        JSONObject massageDetail = massage.getJSONObject(i);

                        mUkuran.add(massageDetail.getString("nama"));

                        KeteranganDAO keterangan = new KeteranganDAO();
                        keterangan.setId(massageDetail.getInt("id"));
                        keterangan.setKeterangan(massageDetail.getString("nama"));
                        ukuran_layanan.add(keterangan);

                        adapterUkuran.notifyDataSetChanged();
                    }

                    String jenis = getIntent().getStringExtra("ukuran");
                    for (int i = 0 ; i < mUkuran.size(); i++) {
                        if(mUkuran.get(i).equals(jenis)){
                            ukuran_spinner.setSelection(i);
                            break;
                        }
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

    // Untuk ngambil data yang udah dikasih dari layanan_Adapter. Terus datanya langsung di set ke EditText
    private void setText(){
        if (getIntent().hasExtra("nama")) {
            id = getIntent().getIntExtra("id", -1);
            Log.d("ID", String.valueOf(id));
            harga_text.setText(String.valueOf(getIntent().getIntExtra("harga", 0)));

        }
    }

    private void init(){
        pd = new ProgressDialog(this);

        ukuran_layanan = new ArrayList<>();
        nama_layanan = new ArrayList<>();

        mItems = new ArrayList<>();
        mUkuran = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterUkuran = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mUkuran);
        adapterUkuran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nama_spinner = findViewById(R.id.layanan_nama_edit_spinner);
        ukuran_spinner = findViewById(R.id.layanan_edit_spinner);
        nama_spinner.setAdapter(adapter);
        ukuran_spinner.setAdapter(adapterUkuran);
        harga_text = findViewById(R.id.layanan_edit_harga);
        edit = findViewById(R.id.layanan_edit_edit);
        delete = findViewById(R.id.layanan_edit_delete);
    }

    //Ngambil Data dari Edit Text
    private void getValue(){


        String jenis = nama_spinner.getSelectedItem().toString();
        String ukuran = ukuran_spinner.getSelectedItem().toString();
        KeteranganDAO keterangan = new KeteranganDAO();

        for (int i = 0 ; i < nama_layanan.size(); i++) {
            keterangan = nama_layanan.get(i);
            if(keterangan.getKeterangan().equals(jenis)){
                break;
            }
        }
        for (int i = 0 ; i < ukuran_layanan.size(); i++) {
            keterangan = ukuran_layanan.get(i);
            if(keterangan.getKeterangan().equals(ukuran)){
                break;
            }
        }
        id_ukuran= keterangan.getId();
        id_nama = keterangan.getId();
        harga = Integer.parseInt(harga_text.getText().toString());

    }

    private void editlayanan(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Layanan/"+ id;
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
                request.put("id_layanan", String.valueOf(id_nama));
                request.put("id_ukuran_hewan",  String.valueOf(id_ukuran));
                request.put("harga", String.valueOf(harga));
                request.put("updated_by", "KelvinAja");
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void deletlayanan(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Layanan/delete/"+ id;
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
                request.put("updated_by", "Yosafat9204");
                return request;
            }
        };
        queue.add(postRequest);
    }
}
