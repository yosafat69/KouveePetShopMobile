package com.example.kouveepetshop.CS_Transaksi;

public class TransaksiDAO {
    public Integer id, isTransaksiLayanan;
    public String no_transaksi, no_telp, status;
    public double total;

    public Integer getId() {
        return id;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public double getTotal() {
        return total;
    }

    public Integer getIsTransaksiLayanan() {
        return isTransaksiLayanan;
    }

    public String getNo_transaksi() {
        return no_transaksi;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public void setIsTransaksiLayanan(Integer isTransaksiLayanan) {
        this.isTransaksiLayanan = isTransaksiLayanan;
    }

    public void setNo_transaksi(String no_transaksi) {
        this.no_transaksi = no_transaksi;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
