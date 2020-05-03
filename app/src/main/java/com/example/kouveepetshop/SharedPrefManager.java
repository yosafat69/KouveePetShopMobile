package com.example.kouveepetshop;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_KOUVEPETSHOP_APP = "spKouvePetShopApp";

    public static final String SP_USERNAME = "spUsername";
    public static final String SP_ROLE = "spRole";

    public static final String SP_ID_TRANSAKSI = "spIdTransaksi";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    public static final String SP_ID_PEMESANAN = "spIdPemesanan";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_KOUVEPETSHOP_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }



    public String getSpUsername(){
        return sp.getString(SP_USERNAME, "");
    }

    public String getSpRole(){
        return sp.getString(SP_ROLE, "");
    }

    public Integer getSpIdTransaksi(){
        return sp.getInt(SP_ID_TRANSAKSI, -1);
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

    public Integer getSpIdPemesanan(){ return sp.getInt(SP_ID_PEMESANAN, -1);}
}
