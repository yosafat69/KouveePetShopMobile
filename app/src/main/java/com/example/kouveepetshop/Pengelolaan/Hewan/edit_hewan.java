package com.example.kouveepetshop.Pengelolaan.Hewan;

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
import com.example.kouveepetshop.Pengelolaan.KeteranganDAO;
import com.example.kouveepetshop.Pengelolaan.Layanan.Layanan_Edit;
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

public class edit_hewan extends AppCompatActivity {

    private String nama, tanggal_lahir,jenis;
    private Integer id,id_jenis;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private Button edit, delete;
    private SharedPrefManager sharedPrefManager;
    private EditText nama_text, jenis_text;
    private TextView tanggal_lahir_text;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Spinner jenis_spinner;
    private ArrayList<KeteranganDAO> kategori_jenis;
    private ProgressDialog pd;
    private boolean doubleClickDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hewan);

        init();
        ambiljenis();
        setText();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    edithewan();
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
                    deletehewan();

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
                    Toast.makeText(edit_hewan.this, "Tekan Lagi Untuk Delete", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleClickDelete = false;
                        }
                    }, 2000);
                }
            }
        });

        tanggal_lahir_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalender();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                tanggal_lahir_text.setText(date);
            }
        };
    }

    private void showCalender() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month =  cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(edit_hewan.this, android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setText(){
        if (getIntent().hasExtra("nama")) {
            id = getIntent().getIntExtra("id", -1);
            nama_text.setText(getIntent().getStringExtra("nama"));
            tanggal_lahir_text.setText(getIntent().getStringExtra("tanggal_lahir"));
        }
    }

    private void edithewan(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/Hewan/"+id;
        VolleyMultipartRequest postRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response) {
                // response
                String resultResponse = new String(response.data);
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(edit_hewan.this, "Refresh Halaman", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(edit_hewan.this, "Error", Toast.LENGTH_SHORT).show();
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
                request.put("nama", nama);
                request.put("id_jenis_hewan",  String.valueOf(id_jenis));
                request.put("tanggal_lahir", String.valueOf(tanggal_lahir));
                request.put("updated_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void deletehewan(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/hewan/delete/"+id;
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

    private void ambiljenis() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/JenisHewan/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i = 0; i < massage.length(); i++) {
                        JSONObject massageDetail = massage.getJSONObject(i);

                        mItems.add(massageDetail.getString("keterangan"));

                        KeteranganDAO keterangan = new KeteranganDAO();
                        keterangan.setId(massageDetail.getInt("id"));
                        keterangan.setKeterangan(massageDetail.getString("keterangan"));
                        kategori_jenis.add(keterangan);

                        adapter.notifyDataSetChanged();
                    }

                    String jenis = getIntent().getStringExtra("jenis");
                    for (int i = 0 ; i < mItems.size(); i++) {
                        if(mItems.get(i).equals(jenis)){
                            jenis_spinner.setSelection(i);
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
        edit = findViewById(R.id.hewan_edit_edit);
        delete = findViewById(R.id.hewan_edit_delete);
        nama_text = findViewById(R.id.hewan_edit_nama);
        tanggal_lahir_text = findViewById(R.id.hewan_edit_tanggal_lahir);
        mItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenis_spinner = findViewById(R.id.hewan_edit_spinner);
        jenis_spinner.setAdapter(adapter);

        pd = new ProgressDialog(this);
        kategori_jenis = new ArrayList<>();
    }

    private void getValue(){
        nama = nama_text.getText().toString();
        tanggal_lahir = tanggal_lahir_text.getText().toString();
        String jenis = jenis_spinner.getSelectedItem().toString();
        KeteranganDAO keterangan = new KeteranganDAO();

        for (int i = 0 ; i < kategori_jenis.size(); i++) {
            keterangan = kategori_jenis.get(i);
            if(keterangan.getKeterangan().equals(jenis)){
                break;
            }
        }
        id_jenis = keterangan.getId();
    }

    private boolean validasi() {
        int cek = 0;
        if (nama_text.getText().toString().equals("")) {
            nama_text.setError("Nama Tidak Boleh Kosong");
            cek = 1;
        }
        else if (nama_text.getText().toString().length() < 3) {
            nama_text.setError("Panjang Nama Minimal 3 Karekter");
            cek = 1;
        }

        else if (!nama_text.getText().toString().matches("[a-zA-Z ]+")) {
            nama_text.setError("Format Nama Salah");
            cek = 1;
        }

        if (tanggal_lahir_text.getText().toString().equals("Tanggal Lahir")) {
            tanggal_lahir_text.setError("Kota Tidak Boleh Kosong");
            cek = 1;
        }
        return cek == 0;
    }

}


