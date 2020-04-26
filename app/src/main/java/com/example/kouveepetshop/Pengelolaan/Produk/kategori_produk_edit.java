package com.example.kouveepetshop.Pengelolaan.Produk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class kategori_produk_edit extends AppCompatActivity {

    private String kategori_produk;
    private Integer id;
    private EditText kategori_produk_text;
    private Button edit, delete;

    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    private boolean doubleClickDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategori_produk_edit);

        init();

        setText();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    editKategoriProduk();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 500);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doubleClickDelete) {
                    deleteKategoriProduk();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 500);
                } else {
                    doubleClickDelete = true;
                    Toast.makeText(kategori_produk_edit.this, "Tekan Lagi Untuk Delete", Toast.LENGTH_SHORT).show();

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

    private void editKategoriProduk(){
        kategori_produk = kategori_produk_text.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/KategoriProduk/"+ id;

        Log.d("URL", url);
        Log.d("ID", String.valueOf(id));

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
                request.put("keterangan", kategori_produk);
                request.put("updated_by", "KelvinAja");
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void deleteKategoriProduk(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/kategoriProduk/delete/"+ id;

        Log.d("URL", url);
        Log.d("ID", String.valueOf(id));

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
                request.put("updated_by", "KelvinAja");
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void setText(){
        if (getIntent().hasExtra("keterangan")) {
            id = getIntent().getIntExtra("id", -1);
            kategori_produk_text.setText(getIntent().getStringExtra("keterangan"));
        }
    }

    private void init() {
        pd = new ProgressDialog(this);
        kategori_produk_text = findViewById(R.id.kategori_produk_edit_jenis);
        edit = findViewById(R.id.kategori_produk_edit);
        delete = findViewById(R.id.kategori_produk_delete);
    }

    private boolean validasi() {
        int cek = 0;

        if (kategori_produk_text.getText().toString().equals("")) {
            kategori_produk_text.setError("Kategori Produk Tidak Boleh Kosong");
            cek = 1;
        }
        else if (kategori_produk_text.getText().toString().length() < 3) {
            kategori_produk_text.setError("Panjang Kategori Produk Minimal 3 Karakter");
            cek = 1;
        }

        else if (!kategori_produk_text.getText().toString().matches("[a-zA-Z ]+")) {
            kategori_produk_text.setError("Format Kategori Produk  Salah");
            cek = 1;
        }

        return cek == 0;
    }
}