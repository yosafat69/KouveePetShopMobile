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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.kouveepetshop.API.AppHelper;
import com.example.kouveepetshop.API.Rest_API;
import com.example.kouveepetshop.API.VolleyMultipartRequest;
import com.example.kouveepetshop.API.VolleySingleton;
import com.example.kouveepetshop.MainActivity;
import com.example.kouveepetshop.Pengelolaan.KeteranganDAO;
import com.example.kouveepetshop.R;
import com.example.kouveepetshop.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Produk_Tambah extends AppCompatActivity {
    private String nama, satuan;
    private Integer jumlah, jumlah_minimal, id_jenis;
    private double harga;
    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ProgressDialog pd;
    private String ip = MainActivity.getIp();
    private Spinner kategori_spinner;
    private Button tambah;
    private ArrayList<KeteranganDAO> kategori_produk;
    private ImageView gambar;
    private int PICK_IMAGE_REQUEST = 1;
    private CurrencyEditText harga_text;
    private EditText jumlah_text, jumlah_minimal_text, nama_text, satuan_text;
    private SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produk_add);

        init();

        loadjson();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    addProduk();
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

        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void addProduk(){
        getValue();

        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Produk/";
        VolleyMultipartRequest postRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response) {
                // response
                String resultResponse = new String(response.data);
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    if (jsonObject.getString("error").equals("false")) {
                        Log.i("Produk","Berhasil");
                    }
                    else {
                        Toast.makeText(Produk_Tambah.this, "Error", Toast.LENGTH_SHORT).show();
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
                request.put("id_kategori_produk",  String.valueOf(id_jenis));
                request.put("harga", String.valueOf(harga));
                request.put("satuan", String.valueOf(satuan));
                request.put("jmlh", String.valueOf(jumlah));
                request.put("jmlh_min", String.valueOf(jumlah_minimal));
                request.put("created_by", sharedPrefManager.getSpUsername());
                return request;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> request = new HashMap<>();
                Log.d("TAG", (String) gambar.getTag());
                if (gambar.getTag().equals("Updated")) {
                    request.put("link_gambar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), gambar.getDrawable()), "image/jpeg"));
                    Log.d("TAG", "Masuk");
                    return request;
                }
                return null;
            }
        };
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(postRequest);
    }

    private void loadjson() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/KategoriProduk/";

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
                        kategori_produk.add(keterangan);

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
        pd = new ProgressDialog(this);
        mItems = new ArrayList<>();
        kategori_produk = new ArrayList<>();

        tambah = findViewById(R.id.produk_tambah_add);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori_spinner = findViewById(R.id.produk_add_spinner);
        kategori_spinner.setAdapter(adapter);

        gambar = findViewById(R.id.produk_tambah_gambar);
        gambar.setTag("0");

        nama_text = findViewById(R.id.produk_tambah_nama);
        satuan_text = findViewById(R.id.produk_tambah_satuan);
        harga_text = findViewById(R.id.produk_tambah_harga);
        jumlah_text = findViewById(R.id.produk_tambah_jmlh);
        jumlah_minimal_text = findViewById(R.id.produk_tambah_jmlh_min);
        sharedPrefManager = new SharedPrefManager(this);
    }



    private void getValue(){
        nama = nama_text.getText().toString();
        String jenis = kategori_spinner.getSelectedItem().toString();
        KeteranganDAO keterangan = new KeteranganDAO();

        for (int i = 0 ; i < kategori_produk.size(); i++) {
            keterangan = kategori_produk.get(i);
            if(keterangan.getKeterangan().equals(jenis)){
                break;
            }
        }
        id_jenis = keterangan.getId();
        harga = harga_text.getNumericValue();
        satuan = satuan_text.getText().toString();
        jumlah = Integer.parseInt(jumlah_text.getText().toString());
        jumlah_minimal = Integer.parseInt(jumlah_minimal_text.getText().toString());
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        int bitmap_size = 60;
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        gambar.setTag("Updated");
        gambar.setImageBitmap(decoded);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private boolean validasi() {
        int cek = 0;

        if (nama_text.getText().toString().equals("")){
            nama_text.setError("Nama Tidak Boleh Kosong");
            cek = 1;
        }
        else if (nama_text.getText().toString().length() < 3){
            nama_text.setError("Panjang Nama Minimal 3 Karakter");
            cek = 1;
        }

        if (harga_text.getNumericValue() == 0){
            harga_text.setError("Harga Tidak Boleh Kosong");
            cek = 1;
        }

        if (satuan_text.getText().toString().equals("")){
            satuan_text.setError("Satuan Tidak Boleh Kosong");
            cek = 1;
        }
        else if (satuan_text.getText().toString().length() < 3){
            satuan_text.setError("Panjang Satuan Minimal 3 Karakter");
            cek = 1;
        }

        if (jumlah_text.getText().toString().equals("")){
            jumlah_text.setError("Jumlah Tidak Boleh Kosong");
            cek = 1;
        }

        if (jumlah_minimal_text.getText().toString().equals("")){
            jumlah_minimal_text.setError("Jumlah Minimal Tidak Boleh Kosong");
            cek = 1;
        }

        return cek == 0;
    }
}
