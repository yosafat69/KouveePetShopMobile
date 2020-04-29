package com.example.kouveepetshop.CS_Transaksi;

public class DetilTransaksiPenjualanDAO {
    public int id, id_transaksi, id_produk, jumlah;
    public double harga, subtotal;
    public String nama, link;

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public int getId() {
        return id;
    }

    public int getId_transaksi() {
        return id_transaksi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setId_transaksi(int id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}

