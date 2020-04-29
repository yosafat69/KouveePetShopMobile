package com.example.kouveepetshop.CS_Transaksi;

public class DetilTransaksiLayananDAO {
    public int id, id_layanan, id_jenis_hewan, jumlah;
    public double harga;
    public String nama_layanan, nama_hewan, tanggal_lahir, gambar;

    public int getJumlah() {
        return jumlah;
    }

    public int getId() {
        return id;
    }

    public double getHarga() {
        return harga;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public String getGambar() {
        return gambar;
    }

    public int getId_jenis_hewan() {
        return id_jenis_hewan;
    }

    public int getId_layanan() {
        return id_layanan;
    }

    public String getNama_hewan() {
        return nama_hewan;
    }

    public String getNama_layanan() {
        return nama_layanan;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setId_jenis_hewan(int id_jenis_hewan) {
        this.id_jenis_hewan = id_jenis_hewan;
    }

    public void setId_layanan(int id_layanan) {
        this.id_layanan = id_layanan;
    }

    public void setNama_hewan(String nama_hewan) {
        this.nama_hewan = nama_hewan;
    }

    public void setNama_layanan(String nama_layanan) {
        this.nama_layanan = nama_layanan;
    }
}
