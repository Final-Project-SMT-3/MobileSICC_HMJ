package com.example.sicc.models;

public class Lomba {
    private String nama_lomba;
    private String jenis_lomba;

    public Lomba(String nama_lomba, String jenis_lomba) {
        this.nama_lomba = nama_lomba;
        this.jenis_lomba = jenis_lomba;
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
}
