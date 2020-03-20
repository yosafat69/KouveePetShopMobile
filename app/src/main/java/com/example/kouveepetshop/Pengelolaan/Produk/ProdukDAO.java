package com.example.kouveepetshop.Pengelolaan.Produk;

public class ProdukDAO {
    public Integer id, jmlh_min, jmlh, harga;
    public String nama, kategori ,satuan, link_gambar;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Integer getJmlh() {
        return jmlh;
    }

    public void setJmlh(Integer jmlh) {
        this.jmlh = jmlh;
    }

    public Integer getJmlh_min() {
        return jmlh_min;
    }

    public void setJmlh_min(Integer jmlh_min) {
        this.jmlh_min = jmlh_min;
    }

    public String getLink_gambar() {
        return link_gambar;
    }

    public void setLink_gambar(String link_gambar) {
        this.link_gambar = link_gambar;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
