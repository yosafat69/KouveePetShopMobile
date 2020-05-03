package com.example.kouveepetshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private String ip = MainActivity.getIp();
    private String url = MainActivity.getUrl();

    private EditText username_text, password_text;
    private Button login;

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        init();

        isLogin();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()){
                    userLogin();
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Login.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);
            }
        });
    }

    private void isLogin() {
        if (sharedPrefManager.getSPSudahLogin()) {
            switch (sharedPrefManager.getSpRole()) {
                case "Owner": {
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    break;
                }
                case "Customer Service": {
                    Intent i = new Intent(Login.this, CS_MainActivity.class);
                    startActivity(i);
                    break;
                }
                case "Cashier": {
                    Intent i = new Intent(Login.this, Cashier_MainActivity.class);
                    startActivity(i);
                    break;
                }
            }
        }
    }

    private void init() {
        username_text = findViewById(R.id.login_username);
        password_text = findViewById(R.id.login_password);
        login = findViewById(R.id.login_btn);
        sharedPrefManager = new SharedPrefManager(this);
    }

    private void userLogin(){
        final String username = username_text.getText().toString();
        final String password = password_text.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ip + this.url +"index.php/Pegawai/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("error").equals("false")) {
                                JSONObject massage = jsonObject.getJSONObject("message");

                                sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME,massage.getString("username"));
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_ROLE,massage.getString("id_role_pegawai"));
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);

                                switch (sharedPrefManager.getSpRole()) {
                                    case "Owner": {
                                        Intent i = new Intent(Login.this, MainActivity.class);
                                        startActivity(i);
                                        break;
                                    }
                                    case "Customer Service": {
                                        Intent i = new Intent(Login.this, CS_MainActivity.class);
                                        startActivity(i);
                                        break;
                                    }
                                    case "Cashier": {
                                        Intent i = new Intent(Login.this, Cashier_MainActivity.class);
                                        startActivity(i);
                                        break;
                                    }
                                }
                            }
                            else {
                                Toast.makeText(Login.this, "Username atau Password Tidak Cocok!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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
                request.put("username", username);
                request.put("password", password);
                return request;
            }
        };
        queue.add(postRequest);
    }

    private boolean validasi() {
        int cek = 0;
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
            password_text.setError("Panjang Username Minimal 6 Karekter");
            cek = 1;
        }

        return cek == 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
