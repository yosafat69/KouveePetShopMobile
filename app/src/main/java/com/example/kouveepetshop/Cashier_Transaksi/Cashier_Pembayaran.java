package com.example.kouveepetshop.Cashier_Transaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.Cashier_MainActivity;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Cashier_Pembayaran extends AppCompatActivity {
    private int id, isTransaksiLayanan;
    private double total, diskon = 0, uang = 0, kembalian= 0;
    private TextView no_transaksi, no_telp, total_text, kembalian_text, nota_text;
    private CurrencyEditText diskon_text, uang_text;
    private Button bayar;
    private CheckBox nota;
    private SharedPrefManager sharedPrefManager;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private String stok;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier_pembayaran);
        dialog = new ProgressDialog(Cashier_Pembayaran.this);
        init();

        getDetail();

        nota_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nota.isChecked()) {
                    nota.setChecked(false);
                }
                else {
                    nota.setChecked(true);
                }
            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bayar_post();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (nota.isChecked()) {
                            Intent intent = new Intent(Cashier_Pembayaran.this, Nota.class);
                            intent.putExtra("diskon", String.valueOf(diskon));
                            intent.putExtra("no_transaksi", no_transaksi.getText().toString());
                            intent.putExtra("isTransaksiLayanan", isTransaksiLayanan);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(Cashier_Pembayaran.this, Cashier_MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }, 1000);
            }
        });

        uang_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                uang = uang_text.getNumericValue();

                if (diskon + uang < total) {
                    kembalian = 0;
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    kembalian_text.setText("Kembalian : " + formatRupiah.format(kembalian));
                }
                else {
                    kembalian = uang + diskon - total;
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    kembalian_text.setText("Kembalian : " + formatRupiah.format(kembalian));
                }
            }
        });

        diskon_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                diskon = diskon_text.getNumericValue();

                if (diskon + uang < total) {
                    kembalian = 0;
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    kembalian_text.setText("Kembalian : " + formatRupiah.format(kembalian));
                }
                else {
                    kembalian = uang + diskon - total;
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    kembalian_text.setText("Kembalian : " + formatRupiah.format(kembalian));
                }
            }
        });
    }

    private void getDetail() {
        String link;
        if (isTransaksiLayanan == 0) {
            link = ip + this.url + "index.php/TransaksiPenjualan/" + id;
        }
        else {
            link = ip + this.url + "index.php/TransaksiLayanan/cashier/" + id;
        }
        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response);
                try {
                    JSONObject massageDetail = response.getJSONObject("message");
                    no_transaksi.setText(massageDetail.getString("no_transaksi"));
                    no_telp.setText(massageDetail.getString("no_telp"));
                    total = massageDetail.getDouble("total");
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    total_text.setText(formatRupiah.format(total));
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

    private void bayar_post() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String link;
        if (isTransaksiLayanan == 0) {
            link = ip + this.url + "index.php/PembayaranPenjualan/";
        }
        else {
            link = ip + this.url + "index.php/PembayaranLayanan/";
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, link,
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
                request.put("id_transaksi", String.valueOf(id));
                request.put("diskon", String.valueOf(diskon));
                request.put("bayar", String.valueOf(uang));
                request.put("created_by", sharedPrefManager.getSpRole());
                request.put("id_cashier", sharedPrefManager.getSpRole());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void init() {
        no_transaksi = findViewById(R.id.cashier_pembayaran_no_transaksi);
        no_telp = findViewById(R.id.cashier_pembayaran_no_telepon);
        total_text = findViewById(R.id.cashier_pembayaran_total);
        kembalian_text = findViewById(R.id.cashier_pembayaran_kembalian);
        nota = findViewById(R.id.cashier_pembayaran_cetak_nota);
        nota_text = findViewById(R.id.cashier_pembayaran_cetak_nota_text);
        diskon_text = findViewById(R.id.cashier_pembayaran_diskon);
        uang_text = findViewById(R.id.cashier_pembayaran_uang);
        bayar = findViewById(R.id.cashier_pembayaran_bayar);
        sharedPrefManager = new SharedPrefManager(this);
        id = sharedPrefManager.getSpIdTransaksi();

        DecimalFormat precision = new DecimalFormat("0");

        diskon_text.setText(precision.format(diskon));
        uang_text.setText(precision.format(uang));

        diskon_text.setCurrencySymbol("Rp", true);
        uang_text.setCurrencySymbol("Rp", true);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        kembalian_text.setText("Kembalian : " + formatRupiah.format(kembalian));

        isTransaksiLayanan = getIntent().getIntExtra("isTransaksiLayanan", -1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Cashier_Pembayaran.this, Cashier_MainActivity.class);
        startActivity(intent);
    }
}
