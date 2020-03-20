package com.example.kouveepetshop.Pengelolaan.Hewan;


public class HewanDAO {
    public Integer id;
    public String nama, tanggal_lahir, jenis;

    public Integer getId() {
        return id;
    }

    public String getJenis() {
        return jenis;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
