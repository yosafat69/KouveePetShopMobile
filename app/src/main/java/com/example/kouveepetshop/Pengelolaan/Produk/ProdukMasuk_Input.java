package com.example.kouveepetshop.Pengelolaan.Produk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.kouveepetshop.API.AppHelper;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.API.VolleyMultipartRequest;
import com.example.kouveepetshop.API.VolleySingleton;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengadaan.Pengadaan_Edit;
import com.example.kouveepetshop.Pengelolaan.KeteranganDAO;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProdukMasuk_Input extends AppCompatActivity
{

    private Integer jumlah,id_produk;

    private EditText  satuan_text, jmlh_text, jmlh_min_text;
    private ImageView gambar;
    private Button edit, delete;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    private ArrayList<String> mItems = new ArrayList<>();



    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produkmasuk_input);

        init();
        Log.d("id_produk",String.valueOf(id_produk));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    post();
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

    }



    private void init(){
        pd = new ProgressDialog(this);
        mItems = new ArrayList<>();
        jmlh_text = findViewById(R.id.produk_edit_jmlh);
        edit = findViewById(R.id.produk_edit_edit);
        sharedPrefManager = new SharedPrefManager(this);
        id_produk = sharedPrefManager.getSpIdPemesanan();
    }

    private void getValue()
    {
        jumlah = Integer.parseInt(jmlh_text.getText().toString());
    }


    private void post(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/Produk/stock/" + id_produk;
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
                request.put("jmlh", String.valueOf(jumlah));
                request.put("updated_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }


    private boolean validasi() {
        int cek = 0;

        if (jmlh_text.getText().toString().equals("")){
            jmlh_text.setError("Jumlah Tidak Boleh Kosong");
            cek = 1;
        }

        return cek == 0;
    }
}

