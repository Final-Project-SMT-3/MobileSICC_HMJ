package com.example.sicc.models;

public class Lomba {
    private String nama_lomba;
    private String jenis_lomba;
    private String tgl_lomba;
    private int deskripsi_lomba;

    public Lomba(String nama_lomba, String jenis_lomba, String tgl_lomba, int deskripsi_lomba) {
        this.nama_lomba = nama_lomba;
        this.jenis_lomba = jenis_lomba;
        this.tgl_lomba = tgl_lomba;
        this.deskripsi_lomba = deskripsi_lomba;
    }

    public String getNama_lomba() {
        return nama_lomba;
    }

    public void setNama_lomba(String nama_lomba) {
        this.nama_lomba = nama_lomba;
    }

    public String getJenis_lomba() {
        return jenis_lomba;
    }

    public void setJenis_lomba(String jenis_lomba) {
        this.jenis_lomba = jenis_lomba;
    }

    public String getTgl_lomba() {
        return tgl_lomba;
    }

    public void setTgl_lomba(String tgl_lomba) {
        this.tgl_lomba = tgl_lomba;
    }

    public int getDeskripsi_lomba() {
        return deskripsi_lomba;
    }

    public void setDeskripsi_lomba(int deskripsi_lomba) {
        this.deskripsi_lomba = deskripsi_lomba;
    }
}
