package com.example.kouveepetshop.Pengadaan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.CS_Transaksi.TransaksiLayanan;
import com.example.kouveepetshop.CS_Transaksi.Transaksi_Tambah;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengelolaan.KeteranganDAO;
import com.example.kouveepetshop.Pengelolaan.Member.Member_Tambah;
import com.example.kouveepetshop.Pengelolaan.Pegawai.Pegawai_Tambah;
import com.example.kouveepetshop.Pengelolaan.Supplier.SupplierDAO;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Pengadaan_tambah extends AppCompatActivity
{
    private Button tambah;
    private Integer id,id_supplier,id_pemesanan;
    private String detil_pengadaan_tanggal;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private SharedPrefManager sharedPrefManager;
    private TextView detil_pengadaan_tanggal_text;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Spinner supplier_spinner;
    private ArrayList<SupplierDAO> kategori_supplier;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengadaan_tambah);

        init();
        ambilsupplier();


        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    TambahDetilPengadaan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(Pengadaan_tambah.this, detilpengadaan_tambah.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }

                    }, 500);
                }
            }
        });
    }


    private void TambahDetilPengadaan(){

        getValue();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/Pemesanan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            if (!jsonObject.getString("error").equals("true")) {
                                id = Integer.parseInt(jsonObject.getString("message"));
                                Log.d("id",String.valueOf(id));
                            }
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
                request.put("id_supplier", String.valueOf(id_supplier));
                request.put("created_by", sharedPrefManager.getSpUsername());
                request.put("updated_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void ambilsupplier() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/Supplier/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i = 0; i < massage.length(); i++) {
                        JSONObject massageDetail = massage.getJSONObject(i);

                        mItems.add(massageDetail.getString("nama"));

                        SupplierDAO nama = new SupplierDAO();
                        nama.setId(massageDetail.getInt("id"));
                        nama.setNama(massageDetail.getString("nama"));
                        kategori_supplier.add(nama);

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

    private void getValue(){

        String supplier = supplier_spinner.getSelectedItem().toString();
        SupplierDAO nama = new SupplierDAO();

        for (int i = 0 ; i < kategori_supplier.size(); i++) {
            nama = kategori_supplier.get(i);
            if(nama.getNama().equals(supplier)){
                break;
            }
        }
        id_supplier = nama.getId();
    }

    private boolean validasi(){
        int cek = 0;


        return cek == 0;
    }

    private void init(){
        sharedPrefManager = new SharedPrefManager(this);
        tambah = findViewById(R.id.detilpengadaan_tambah);
        mItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplier_spinner = findViewById(R.id.detil_pengadaan_spinner);
        supplier_spinner.setAdapter(adapter);

        pd = new ProgressDialog(this);
        kategori_supplier = new ArrayList<>();


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}


