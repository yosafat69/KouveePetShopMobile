package com.example.kouveepetshop.Pengelolaan.Pegawai;

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


public class Pegawai_Tambah extends AppCompatActivity {

    private String nama, no_telp, alamat, tanggal_lahir, username, password;
    private Integer id_role;
    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();
    private Button tambah;
    private SharedPrefManager sharedPrefManager;
    private EditText nama_text, no_telp_text, alamat_text, username_text, password_text, password_confirm_text;
    private TextView tanggal_lahir_text;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Spinner role_spinner;
    private ArrayList<KeteranganDAO> kategori_role;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pegawai_add);

        init();
        ambilRole();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    addPegawai();
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

        DatePickerDialog dialog = new DatePickerDialog(Pegawai_Tambah.this, android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addPegawai(){
        getValue();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/Pegawai";
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
                request.put("nama", nama);
                request.put("no_telp",  String.valueOf(no_telp));
                request.put("id_role_pegawai",  String.valueOf(id_role));
                request.put("alamat", String.valueOf(alamat));
                request.put("tanggal_lahir", String.valueOf(tanggal_lahir));
                request.put("username", String.valueOf(username));
                request.put("password", String.valueOf(password));
                request.put("created_by", sharedPrefManager.getSpUsername());
                return request;
            }
        };
        queue.add(postRequest);
    }

    private void ambilRole() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/RolePegawai/";

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
                        kategori_role.add(keterangan);

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

    private void init(){
        sharedPrefManager = new SharedPrefManager(this);
        tambah = findViewById(R.id.pegawai_tambah_add);
        nama_text = findViewById(R.id.pegawai_tambah_nama);
        no_telp_text = findViewById(R.id.pegawai_tambah_no_telp);
        alamat_text = findViewById(R.id.pegawai_tambah_alamat);
        tanggal_lahir_text = findViewById(R.id.pegawai_tambah_tanggal_lahir);
        username_text = findViewById(R.id.pegawai_tambah_username);
        password_text = findViewById(R.id.pegawai_tambah_password);
        password_confirm_text = findViewById(R.id.pegawai_tambah_password_confirm);

        mItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role_spinner = findViewById(R.id.pegawai_add_spinner);
        role_spinner.setAdapter(adapter);

        pd = new ProgressDialog(this);
        kategori_role = new ArrayList<>();
    }

    private void getValue(){
        nama = nama_text.getText().toString();
        no_telp = no_telp_text.getText().toString();
        alamat = alamat_text.getText().toString();
        tanggal_lahir = tanggal_lahir_text.getText().toString();
        username = username_text.getText().toString();
        password = password_text.getText().toString();

        String role = role_spinner.getSelectedItem().toString();
        KeteranganDAO keterangan = new KeteranganDAO();

        for (int i = 0 ; i < kategori_role.size(); i++) {
            keterangan = kategori_role.get(i);
            if(keterangan.getKeterangan().equals(role)){
                break;
            }
        }
        id_role = keterangan.getId();
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

        if (username_text.getText().toString().equals("")) {
            username_text.setError("Username Tidak Boleh Kosong");
            cek = 1;
        }
        else if (username_text.getText().toString().length() < 6) {
            username_text.setError("Panjang Username Minimal 6 Karekter");
            cek = 1;
        }

        if (password_text.getText().toString().equals("")) {
            password_text.setError("Password Tidak Boleh Kosong");
            cek = 1;
        }

        else if (password_text.getText().toString().length() < 6) {
            password_text.setError("Panjang Password Minimal 6 Karakter");
            cek = 1;
        }

        if (password_confirm_text.getText().toString().equals("")) {
            password_confirm_text.setError("Password Tidak Boleh Kosong");
            cek = 1;
        }
        else if (password_confirm_text.getText().toString().length() < 6) {
            password_confirm_text.setError("Panjang Password Minimal 6 Karakter");
            cek = 1;
        }
        else if (!password_text.getText().toString().equals(password_confirm_text.getText().toString())) {
            password_confirm_text.setError("Password Tidak Sama");
            cek = 1;
        }

        return cek == 0;
    }

}
