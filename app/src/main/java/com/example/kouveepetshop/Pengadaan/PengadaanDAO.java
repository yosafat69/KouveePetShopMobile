package com.example.kouveepetshop.Pengadaan;

public class PengadaanDAO {

    public Integer id;
    public String id_supplier,status,no_pemesanan,tgl_pemesanan;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTgl_pemesanan(){return tgl_pemesanan;}

    public void setTgl_pemesanan(String tgl_pemesanan) {this.tgl_pemesanan = tgl_pemesanan;}

    public String getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(String id_supplier) {
        this.id_supplier = id_supplier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNo_pemesanan() {
        return no_pemesanan;
    }

    public void setNo_pemesanan(String no_pemesanan) {
        this.no_pemesanan = no_pemesanan;
    }


}
