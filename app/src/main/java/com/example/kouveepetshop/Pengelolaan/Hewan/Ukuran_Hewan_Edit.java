package com.example.kouveepetshop.Pengelolaan.Hewan;

import android.app.ProgressDialog;
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

public class Ukuran_Hewan_Edit extends AppCompatActivity {
    private String ukuran_hewan;
    private Integer id;
    private EditText ukuran_hewan_text;
    private Button edit, delete;

    private ProgressDialog pd;
    private String ip = MainActivity.getIp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ukuran__hewan__edit);

        init();

        setText();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editJenisHewan();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteJenisHewan();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private void editJenisHewan(){
        ukuran_hewan = ukuran_hewan_text.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/ukuranHewan/"+ id;

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
                request.put("nama", ukuran_hewan);
                request.put("updated_by", "KelvinAja");
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void deleteJenisHewan(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/ukuranhewan/delete/"+ id;

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
            ukuran_hewan_text.setText(getIntent().getStringExtra("keterangan"));
        }
    }

    private void init() {
        pd = new ProgressDialog(this);
        ukuran_hewan_text = findViewById(R.id.ukuran_hewan_edit_jenis);
        edit = findViewById(R.id.ukuran_hewan_edit);
        delete = findViewById(R.id.ukuran_hewan_delete);
    }

}
