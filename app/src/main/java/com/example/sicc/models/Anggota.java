package com.example.sicc.models;

public class Anggota {
    private String nama_anggota;
    private String prodi_smt;
    private String tgl_lahir;

    public Anggota(String nama_anggota, String prodi_smt, String tgl_lahir) {
        this.nama_anggota = nama_anggota;
        this.prodi_smt = prodi_smt;
        this.tgl_lahir = tgl_lahir;
    }

    public String getNama_anggota() {
        return nama_anggota;
    }

    public void setNama_anggota(String nama_anggota) {
        this.nama_anggota = nama_anggota;
    }

    public String getProdi_smt() {
        return prodi_smt;
    }

    public void setProdi_smt(String prodi_smt) {
        this.prodi_smt = prodi_smt;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }
}
