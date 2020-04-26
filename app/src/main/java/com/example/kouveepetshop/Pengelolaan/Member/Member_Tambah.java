package com.example.kouveepetshop.Pengelolaan.Member;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Member_Tambah extends AppCompatActivity {

    private String nama, no_telp, alamat, tanggal_lahir;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private Button tambah;
    private SharedPrefManager sharedPrefManager;
    private EditText nama_text, no_telp_text, alamat_text;
    private TextView tanggal_lahir_text;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private boolean no_telpUnique = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_add);

        init();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    addMember();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (no_telpUnique) {
                                Intent returnIntent = new Intent();
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            }
                            else {
                                no_telp_text.setError("Nomor Telepon Sudah Dipakai");
                            }
                        }
                    }, 500);
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

        DatePickerDialog dialog = new DatePickerDialog(Member_Tambah.this, android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addMember(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/Member";
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
                                no_telpUnique = true;
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
                request.put("nama", nama);
                request.put("no_telp",  String.valueOf(no_telp));
                request.put("alamat", String.valueOf(alamat));
                request.put("tanggal", String.valueOf(tanggal_lahir));
                request.put("created_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void init(){
        sharedPrefManager = new SharedPrefManager(this);
        tambah = findViewById(R.id.member_tambah_add);
        nama_text = findViewById(R.id.member_tambah_nama);
        no_telp_text = findViewById(R.id.member_tambah_no_telp);
        alamat_text = findViewById(R.id.member_tambah_alamat);
        tanggal_lahir_text = findViewById(R.id.member_tambah_tanggal_lahir);

    }

    private void getValue(){
        nama = nama_text.getText().toString();
        no_telp = no_telp_text.getText().toString();
        alamat = alamat_text.getText().toString();
        tanggal_lahir = tanggal_lahir_text.getText().toString();
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

        if (no_telp_text.getText().toString().equals("")) {
            no_telp_text.setError("Nomor Telepon Tidak Boleh Kosong");
            cek = 1;
        }
        else if (no_telp_text.getText().toString().length() < 10 || no_telp_text.getText().toString().length() > 13 ) {
            no_telp_text.setError("Nomor Telepon 10 - 13 Karakter");
            cek = 1;
        }

        else if (!String.valueOf(no_telp_text.getText().toString().charAt(0)).equals("0") && !String.valueOf(no_telp_text.getText().toString().charAt(1)).equals("8")) {
            no_telp_text.setError("Format Nomor Telepon Salah");
            cek = 1;
        }

        if (alamat_text.getText().toString().equals("")) {
            alamat_text.setError("Alamat Tidak Boleh Kosong");
            cek = 1;
        }
        else if (alamat_text.getText().toString().length() < 3) {
            alamat_text.setError("Panjang Alamat Minimal 3 Karekter");
            cek = 1;
        }

        if (tanggal_lahir_text.getText().toString().equals("Tanggal Lahir")) {
            tanggal_lahir_text.setError("Kota Tidak Boleh Kosong");
            cek = 1;
        }

        return cek == 0;
    }

}
