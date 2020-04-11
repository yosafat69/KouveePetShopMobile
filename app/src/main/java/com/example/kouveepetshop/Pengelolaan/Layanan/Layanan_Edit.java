package com.example.kouveepetshop.Pengelolaan.Layanan;


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
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.kouveepetshop.Pengelolaan.KeteranganDAO;
import com.example.kouveepetshop.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Layanan_Edit extends AppCompatActivity {

    Integer id_nama,id,id_ukuran;
    private double harga;
    Spinner nama_spinner,ukuran_spinner;
    private ImageView gambar;
    Button edit, delete;
    private CurrencyEditText harga_text;
    private ProgressDialog pd;

    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayList<String> mUkuran = new ArrayList<>();
    private ArrayList<KeteranganDAO> nama_layanan;
    private ArrayList<KeteranganDAO> ukuran_layanan;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterUkuran;

    private boolean doubleClickDelete = false;

    private int PICK_IMAGE_REQUEST = 1;
    private int bitmap_size = 60; // range 1 - 100
    private Bitmap bitmap, decoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layanan__edit);

        init();
        LoadLayanan();
        LoadUkuran();
        setText();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    editlayanan();

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
                    deletlayanan();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 1000);
                }
                else {
                    doubleClickDelete = true;
                    Toast.makeText(Layanan_Edit.this, "Tekan Lagi Untuk Delete", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleClickDelete = false;
                        }
                    }, 2000);
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

    //Ngmbil data Kategori layanan untuk spinnernya
    private void LoadLayanan() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/jenislayanan/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i = 0; i < massage.length(); i++) {
                        JSONObject massageDetail = massage.getJSONObject(i);

                        mItems.add(massageDetail.getString("nama"));

                        KeteranganDAO keterangan = new KeteranganDAO();
                        keterangan.setId(massageDetail.getInt("id"));
                        keterangan.setKeterangan(massageDetail.getString("nama"));
                        nama_layanan.add(keterangan);

                        adapter.notifyDataSetChanged();
                    }

                    String jenis = getIntent().getStringExtra("nama");
                    for (int i = 0 ; i < mItems.size(); i++) {
                        if(mItems.get(i).equals(jenis)){
                            nama_spinner.setSelection(i);
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

    private void LoadUkuran() {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        String url = ip + this.url + "index.php/ukuranhewan/";

        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("volley", "response : " + response.toString());

                try {
                    JSONArray massage = response.getJSONArray("message");

                    for (int i = 0; i < massage.length(); i++) {
                        JSONObject massageDetail = massage.getJSONObject(i);

                        mUkuran.add(massageDetail.getString("nama"));

                        KeteranganDAO keterangan = new KeteranganDAO();
                        keterangan.setId(massageDetail.getInt("id"));
                        keterangan.setKeterangan(massageDetail.getString("nama"));
                        ukuran_layanan.add(keterangan);

                        adapterUkuran.notifyDataSetChanged();
                    }

                    String jenis = getIntent().getStringExtra("ukuran");
                    for (int i = 0 ; i < mUkuran.size(); i++) {
                        if(mUkuran.get(i).equals(jenis)){
                            ukuran_spinner.setSelection(i);
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

    // Untuk ngambil data yang udah dikasih dari layanan_Adapter. Terus datanya langsung di set ke EditText
    private void setText(){
        if (getIntent().hasExtra("nama")) {
            id = getIntent().getIntExtra("id", -1);
            Log.d("ID", String.valueOf(id));
            DecimalFormat precision = new DecimalFormat("0");
            harga_text.setText(precision.format(getIntent().getDoubleExtra("harga", 0)));
            Picasso.get().load(getIntent().getStringExtra("url_gambar")).into(gambar);
            Log.d("volley", getIntent().getStringExtra("url_gambar"));
        }
    }

    private void init(){
        pd = new ProgressDialog(this);

        ukuran_layanan = new ArrayList<>();
        nama_layanan = new ArrayList<>();

        mItems = new ArrayList<>();
        mUkuran = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterUkuran = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mUkuran);
        adapterUkuran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nama_spinner = findViewById(R.id.layanan_nama_edit_spinner);
        ukuran_spinner = findViewById(R.id.layanan_edit_spinner);
        nama_spinner.setAdapter(adapter);
        ukuran_spinner.setAdapter(adapterUkuran);
        harga_text = findViewById(R.id.layanan_edit_harga);
        edit = findViewById(R.id.layanan_edit_edit);
        delete = findViewById(R.id.layanan_edit_delete);
        gambar = findViewById(R.id.layanan_edit_gambar);
        harga_text.setCurrencySymbol("Rp", true);
    }

    //Ngambil Data dari Edit Text
    private void getValue(){


        String jenis = nama_spinner.getSelectedItem().toString();
        String ukuran = ukuran_spinner.getSelectedItem().toString();
        KeteranganDAO keterangan = new KeteranganDAO();

        for (int i = 0 ; i < nama_layanan.size(); i++) {
            keterangan = nama_layanan.get(i);
            if(keterangan.getKeterangan().equals(jenis)){
                break;
            }
        }
        id_nama = keterangan.getId();
        for (int i = 0 ; i < ukuran_layanan.size(); i++) {
            keterangan = ukuran_layanan.get(i);
            if(keterangan.getKeterangan().equals(ukuran)){
                break;
            }
        }
        id_ukuran= keterangan.getId();
        gambar = findViewById(R.id.layanan_edit_gambar);
        harga = harga_text.getNumericValue();

    }

    private void editlayanan(){
        getValue();

        String url = ip + this.url + "index.php/Layanan/"+ id;
        VolleyMultipartRequest postRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response) {
                // response
                String resultResponse = new String(response.data);
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(Layanan_Edit.this, "Refresh Halaman", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Layanan_Edit.this, "Error", Toast.LENGTH_SHORT).show();
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
                Map<String, String>  request = new HashMap<String, String>();
                request.put("id_layanan", String.valueOf(id_nama));
                request.put("id_ukuran_hewan",  String.valueOf(id_ukuran));
                request.put("harga", String.valueOf(harga));
                request.put("updated_by", "KelvinAja");
                return request;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> request = new HashMap<>();
                request.put("url_gambar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), gambar.getDrawable()), "image/jpeg"));
                return request;
            }
        };
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(postRequest);
    }

    private void deletlayanan(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url + "index.php/Layanan/delete/"+ id;
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

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        gambar.setImageBitmap(decoded);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private boolean validasi() {
        int cek = 0;


        if (harga_text.getNumericValue() == 0){
            harga_text.setError("Harga Tidak Boleh Kosong");
            cek = 1;
        }



        return cek == 0;
    }
}
