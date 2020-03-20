package com.example.kouveepetshop.Pengelolaan.Supplier;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;

import java.util.HashMap;
import java.util.Map;

public class Supplier_Edit extends AppCompatActivity {

    private Integer id;
    private EditText nama_text, no_telp_text, alamat_text, kota_text;
    private Button edit, delete;
    private String nama, no_telp, alamat, kota;

    private String ip = MainActivity.getIp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_edit);

        init();

        setText();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSupplier();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSupplier();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private void editSupplier(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Supplier/"+ id;
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
                request.put("nama", nama);
                request.put("no_telp",  String.valueOf(no_telp));
                request.put("alamat", String.valueOf(alamat));
                request.put("kota", String.valueOf(kota));
                request.put("updated_by", "Yosafat9204");
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void deleteSupplier(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Supplier/delete/"+ id;
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

    private void getValue() {
        nama = nama_text.getText().toString();
        no_telp = no_telp_text.getText().toString();
        alamat = alamat_text.getText().toString();
        kota = kota_text.getText().toString();
    }

    private void setText(){
        if (getIntent().hasExtra("nama")) {
            id = getIntent().getIntExtra("id", -1);
            nama_text.setText(getIntent().getStringExtra("nama"));
            no_telp_text.setText(getIntent().getStringExtra("no_telp"));
            alamat_text.setText(getIntent().getStringExtra("alamat"));
            kota_text.setText(getIntent().getStringExtra("kota"));
        }
    }

    private void init() {
        nama_text       = findViewById(R.id.supplier_edit_nama);
        no_telp_text    = findViewById(R.id.supplier_edit_no_telp);
        alamat_text     = findViewById(R.id.supplier_edit_alamat);
        kota_text       = findViewById(R.id.supplier_edit_kota);
        edit            = findViewById(R.id.supplier_edit_edit);
        delete          = findViewById(R.id.supplier_edit_delete);
    }
}
