package com.example.kouveepetshop.Pengelolaan.Produk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Produk_Edit extends AppCompatActivity {
    private String nama, satuan, kategori;
    private Integer harga, jmlh, jmlh_min, id, id_jenis;

    private EditText nama_text, satuan_text, harga_text, jmlh_text, jmlh_min_text;
    private Spinner kategori_spinner;

    private ImageView gambar;
    private Button edit, delete;

    private ProgressDialog pd;
    private String ip = MainActivity.getIp();

    private ArrayList<String> mItems = new ArrayList<>();
    private ArrayList<KeteranganDAO> kategori_produk;
    private ArrayAdapter<String> adapter;

    private int PICK_IMAGE_REQUEST = 1;
    private int bitmap_size = 60; // range 1 - 100
    private Bitmap bitmap, decoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produk_edit);

        init();
        LoadKategori();
        setText();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduk();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduk();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void LoadKategori() {
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

                    String jenis = getIntent().getStringExtra("kategori");
                    for (int i = 0 ; i < mItems.size(); i++) {
                        if(mItems.get(i).equals(jenis)){
                            kategori_spinner.setSelection(i);
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

    private void setText(){
        if (getIntent().hasExtra("nama")) {
            id = getIntent().getIntExtra("id", -1);
            Log.d("ID", String.valueOf(id));
            nama_text.setText(getIntent().getStringExtra("nama"));
            harga_text.setText(String.valueOf(getIntent().getIntExtra("harga", 0)));
            satuan_text.setText(getIntent().getStringExtra("satuan"));
            jmlh_text.setText(String.valueOf(getIntent().getIntExtra("jmlh", 0)));
            jmlh_min_text.setText(String.valueOf(getIntent().getIntExtra("jmlh_min", 0)));
            Picasso.get().load(getIntent().getStringExtra("link_gambar")).into(gambar);
            Log.d("volley", getIntent().getStringExtra("link_gambar"));
        }
    }

    private void init(){
        pd = new ProgressDialog(this);

        kategori_produk = new ArrayList<>();
        mItems = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori_spinner = findViewById(R.id.produk_edit_spinner);
        kategori_spinner.setAdapter(adapter);

        nama_text = findViewById(R.id.produk_edit_nama);
        harga_text = findViewById(R.id.produk_edit_harga);
        satuan_text = findViewById(R.id.produk_edit_satuan);
        jmlh_text = findViewById(R.id.produk_edit_jmlh);
        jmlh_min_text = findViewById(R.id.produk_edit_jmlh_min);
        kategori_spinner = findViewById(R.id.produk_edit_spinner);

        edit = findViewById(R.id.produk_edit_edit);
        delete = findViewById(R.id.produk_edit_delete);
        gambar = findViewById(R.id.produk_edit_gambar);
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
        harga = Integer.parseInt(harga_text.getText().toString());
        satuan = satuan_text.getText().toString();
        jmlh = Integer.parseInt(jmlh_text.getText().toString());
        jmlh_min = Integer.parseInt(jmlh_min_text.getText().toString());
    }

    private void editProduk(){
        getValue();

        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Produk/"+ id;
        VolleyMultipartRequest postRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // response
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject jsonObject = new JSONObject(resultResponse);
                            if (jsonObject.getString("error").equals("false")) {
                                Toast.makeText(Produk_Edit.this, "Refresh Halaman", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Produk_Edit.this, "Error", Toast.LENGTH_SHORT).show();
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
                request.put("nama", nama);
                request.put("id_kategori_produk",  String.valueOf(id_jenis));
                request.put("harga", String.valueOf(harga));
                request.put("satuan", String.valueOf(satuan));
                request.put("jmlh", String.valueOf(jmlh));
                request.put("jmlh_min", String.valueOf(jmlh_min));
                request.put("updated_by", "Yosafat9204");
                return request;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> request = new HashMap<>();
                request.put("link_gambar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), gambar.getDrawable()), "image/jpeg"));
                return request;
            }
        };
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(postRequest);
    }

    private void deleteProduk(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/rest_api-kouvee-pet-shop-master/index.php/Produk/delete/"+ id;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Produk_Edit.this, "Refresh Halaman", Toast.LENGTH_SHORT).show();
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
}
