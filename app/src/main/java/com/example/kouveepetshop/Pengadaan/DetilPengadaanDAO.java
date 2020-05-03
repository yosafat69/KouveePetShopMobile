package com.example.kouveepetshop.Pengadaan;

public class DetilPengadaanDAO
{
    public int id,jumlah,id_pemesanan,id_produk;
    public String nama,gambar;

    public int getid() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getid_pemesanan() {
        return id_pemesanan;
    }

    public void setid_pemesanan(int id_pemesanan) {
        this.id_pemesanan = id_pemesanan;
    }

    public int getid_produk() {
        return id_produk;
    }

    public void setid_produk(int id_produk) {
        this.id_produk = id_produk;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) { this.gambar = gambar;}

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
