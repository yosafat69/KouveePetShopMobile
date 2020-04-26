package com.example.kouveepetshop.Pengelolaan.Member;

public class MemberDAO {
    public String nama, no_telp, alamat, tanggal_lahir;
    public Integer id;

    public String getAlamat() {
        return alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
