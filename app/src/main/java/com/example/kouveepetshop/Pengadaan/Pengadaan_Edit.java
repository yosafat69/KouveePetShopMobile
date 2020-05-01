package com.example.kouveepetshop.Pengadaan;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.API.VolleyMultipartRequest;
import com.example.kouveepetshop.MainActivity;
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

public class Pengadaan_Edit extends AppCompatActivity {

    private String  tanggal_pemesanan,status,no_pemesanan;
    private Integer id,id_supplier;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private Button edit, delete;
    private SharedPrefManager sharedPrefManager;
    private TextView tanggal_pemesanan_text,status_text, no_pemesanan_text;
    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Spinner supplier_spinner;
    private ArrayList<SupplierDAO> kategori_supplier;
    private ProgressDialog pd;
    private boolean doubleClickDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengadaan_edit);

        init();
        ambilsupplier();
        setText();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    editPengadaan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 1000);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doubleClickDelete) {
                    deletePengadaan();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 500);
                }
                else {
                    doubleClickDelete = true;
                    Toast.makeText(Pengadaan_Edit.this, "Tekan Lagi Untuk Delete", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleClickDelete = false;
                        }
                    }, 2000);
                }
            }
        });

    }


    private void setText(){
        if (getIntent().hasExtra("nama")) {
            id = getIntent().getIntExtra("id", -1);
            no_pemesanan_text.setText(getIntent().getStringExtra("no_PO"));
            status_text.setText(getIntent().getStringExtra("status"));
            tanggal_pemesanan_text.setText(getIntent().getStringExtra("tgl_pemesanan"));
        }
    }

    private void editPengadaan(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/Pemesanan/"+id;
        VolleyMultipartRequest postRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response) {
                // response
                String resultResponse = new String(response.data);
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(Pengadaan_Edit.this, "Refresh Halaman", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Pengadaan_Edit.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.i("Error", jsonObject.getString("message"));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
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
                request.put("id_supplier",  String.valueOf(id_supplier));
                request.put("updated_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void deletePengadaan(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/pemesanan/delete/"+id;
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
                Map<String, String>  request = new HashMap<>();
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

                    String jenis = getIntent().getStringExtra("jenis");
                    for (int i = 0 ; i < mItems.size(); i++) {
                        if(mItems.get(i).equals(jenis)){
                            supplier_spinner.setSelection(i);
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

    private void init(){
        sharedPrefManager = new SharedPrefManager(this);
        edit = findViewById(R.id.pengadaan_edit_edit);
        delete = findViewById(R.id.pengadaan_edit_delete);
        no_pemesanan_text = findViewById(R.id.text_nomor_pemesanan);
        tanggal_pemesanan_text = findViewById(R.id.text_tanggal_pemesanan);
        status_text = findViewById(R.id.text_pemesanan_status);
        mItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplier_spinner = findViewById(R.id.supplier_edit_spinner);
        supplier_spinner.setAdapter(adapter);

        pd = new ProgressDialog(this);
        kategori_supplier = new ArrayList<>();
    }

    private void getValue(){
        no_pemesanan = no_pemesanan_text.getText().toString();
        tanggal_pemesanan = tanggal_pemesanan_text.getText().toString();
        status = status_text.getText().toString();
        String jenis = supplier_spinner.getSelectedItem().toString();
        SupplierDAO nama = new SupplierDAO();

        for (int i = 0 ; i < kategori_supplier.size(); i++) {
            nama = kategori_supplier.get(i);
            if(nama.getNama().equals(jenis)){
                break;
            }
        }
        id_supplier = nama.getId();
    }

    private boolean validasi() {
        int cek = 0;
        if (no_pemesanan_text.getText().toString().equals("")) {
            no_pemesanan_text.setError("Nama Tidak Boleh Kosong");
            cek = 1;
        }

        return cek == 0;
    }
}

