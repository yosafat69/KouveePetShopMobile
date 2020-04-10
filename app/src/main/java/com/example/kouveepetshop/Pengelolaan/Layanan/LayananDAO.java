package com.example.kouveepetshop.Pengelolaan.Layanan;

public class LayananDAO {
    public Integer id;
    public double harga;
    public String keterangan, ukuran, gambar;

    public double getHarga() {
        return harga;
    }

    public Integer getId() {
        return id;
    }

    public String getGambar() {
        return gambar;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getUkuran() {
        return ukuran;
    }

    public void setHarga(double harga) { this.harga = harga; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }
}
