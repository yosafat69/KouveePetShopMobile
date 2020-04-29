package com.example.kouveepetshop.CS_Transaksi;
import android.app.DatePickerDialog;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.example.kouveepetshop.Pengelolaan.Member.Member_Edit;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TransaksiLayananKeranjang_Tambah extends AppCompatActivity {
    private int id, id_transaksi, id_jenis_hewan, id_layanan;
    double harga_layanan;
    private ImageView gambar;
    private TextView nama_layanan, harga, tanggal_lahir;
    private Spinner jenis_hewan;
    private Button add;
    private EditText nama_hewan, jumlah;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    private ArrayList<String> mItems;
    private ArrayAdapter<String> adapter;
    private ArrayList<KeteranganDAO> jenis_hewan_keterangan;

    private SharedPrefManager sharedPrefManager;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_transaksi_layanan_keranjang_tambah);

        init();

        getDetail();
        getJenisHewan();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    addLayanan();
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

        tanggal_lahir.setOnClickListener(new View.OnClickListener() {
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
                tanggal_lahir.setText(date);
            }
        };
    }

    private void showCalender() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month =  cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addLayanan() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/DetilTransaksiLayanan/";
        Log.d("URL : ", url);
        Log.d("nama_hewan", nama_hewan.getText().toString());
        Log.d("id_jenis_hewan", String.valueOf(getIDJenisHewan()));
        Log.d("tanggal_lahir", String.valueOf(tanggal_lahir.getText()));
        Log.d("id_layanan", String.valueOf(id_layanan));
        Log.d("harga", String.valueOf(harga_layanan));
        Log.d("jumlah", jumlah.getText().toString());
        Log.d("id_transaksi", String.valueOf(id_transaksi));
        Log.d("pegawai", sharedPrefManager.getSpUsername());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
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
                request.put("nama_hewan", nama_hewan.getText().toString());
                request.put("id_jenis_hewan", String.valueOf(getIDJenisHewan()));
                request.put("tanggal_lahir", String.valueOf(tanggal_lahir.getText()));
                request.put("id_layanan", String.valueOf(id_layanan));
                request.put("harga", String.valueOf(harga_layanan));
                request.put("jumlah", jumlah.getText().toString());
                request.put("id_transaksi", String.valueOf(id_transaksi));
                request.put("pegawai", sharedPrefManager.getSpUsername());
                request.put("created_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private int getIDJenisHewan() {
        String jenis = jenis_hewan.getSelectedItem().toString();
        new KeteranganDAO();
        KeteranganDAO keterangan;
        for (int i = 0 ; i < jenis_hewan_keterangan.size(); i++) {
            keterangan = jenis_hewan_keterangan.get(i);
            if(keterangan.getKeterangan().equals(jenis)){
                return keterangan.getId();
            }
        }
        return -1;
    }

    private void getDetail() {
        String url = ip + this.url + "index.php/Layanan/"+id;
        final String link = ip + this.url;

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String substring;

                Log.d("volley", "response : " + response);
                try {
                    JSONArray massage = response.getJSONArray("message");
                    JSONObject massageDetail = massage.getJSONObject(0);
                    nama_layanan.setText(massageDetail.getString("id_layanan") + " " + massageDetail.getString("id_ukuran_hewan"));

                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    harga_layanan = massageDetail.getDouble("harga");
                    harga.setText(formatRupiah.format(harga_layanan));
                    id_layanan = massageDetail.getInt("id");

                    substring = massageDetail.getString("url_gambar").substring(47);
                    final String link_gambar = link + substring;
                    Picasso.get().load(link_gambar).into(gambar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "error : " + error.getMessage());
            }
        });
        Rest_API.getInstance(this).addToRequestQueue(arrayRequest);
    }

    private void getJenisHewan() {
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
                        jenis_hewan_keterangan.add(keterangan);

                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "error : " + error.getMessage());
            }
        });
        Rest_API.getInstance(this).addToRequestQueue(arrayRequest);
    }

    private void init() {
        gambar = findViewById(R.id.cs_transaksi_layanan_keranjang_tambah_gambar);
        harga = findViewById(R.id.cs_transaksi_layanan_keranjang_tambah_harga);
        nama_layanan = findViewById(R.id.cs_transaksi_layanan_keranjang_tambah_name_layanan);
        nama_hewan = findViewById(R.id.cs_transaksi_layanan_keranjang_tambah_name_hewan);
        tanggal_lahir = findViewById(R.id.cs_transaksi_layanan_keranjang_tambah_tanggal_lahir);
        jenis_hewan = findViewById(R.id.cs_transaksi_layanan_keranjang_tambah_jenis_hewan_spinner);
        jumlah = findViewById(R.id.cs_transaksi_layanan_keranjang_tambah_jumlah);
        add = findViewById(R.id.cs_transaksi_layanan_keranjang_tambah_add);
        add.setText("Tambah");

        mItems = new ArrayList<>();

        sharedPrefManager = new SharedPrefManager(this);
        id_transaksi = sharedPrefManager.getSpIdTransaksi();
        id = getIntent().getIntExtra("id", -1);

        jenis_hewan_keterangan = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenis_hewan.setAdapter(adapter);
    }

    private boolean validasi() {
        int cek = 0;
        if (nama_hewan.getText().toString().equals("")) {
            nama_hewan.setError("Nama Tidak Boleh Kosong");
            cek = 1;
        }
        else if (nama_hewan.getText().toString().length() < 3) {
            nama_hewan.setError("Panjang Nama Minimal 3 Karekter");
            cek = 1;
        }

        else if (!nama_hewan.getText().toString().matches("[a-zA-Z ]+")) {
            nama_hewan.setError("Format Nama Salah");
            cek = 1;
        }

        if (tanggal_lahir.getText().toString().equals("Tanggal Lahir")) {
            tanggal_lahir.setError("Kota Tidak Boleh Kosong");
            cek = 1;
        }

        if (jumlah.getText().toString().equals("")) {
            jumlah.setError("Nama Tidak Boleh Kosong");
            cek = 1;
        }

        return cek == 0;
    }
}
