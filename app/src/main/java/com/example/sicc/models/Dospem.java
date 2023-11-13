package com.example.sicc.models;

public class Dospem {
    private int id_dospem;
    private String foto_dosen;
    private String nama_dosen;
    private int nidn_dosen;
    private String status_dosen;

    public Dospem(int id_dospem, String nama_dosen, int nidn_dosen, String status_dosen) {
        this.id_dospem = id_dospem;
        this.nama_dosen = nama_dosen;
        this.nidn_dosen = nidn_dosen;
        this.status_dosen = status_dosen;
    }

    public int getId_dospem() {
        return id_dospem;
    }

    public void setId_dospem(int id_dospem) {
        this.id_dospem = id_dospem;
    }

    public String getFoto_dosen() {
        return foto_dosen;
    }

    public void setFoto_dosen(String foto_dosen) {
        this.foto_dosen = foto_dosen;
    }

    public String getNama_dosen() {
        return nama_dosen;
    }

    public void setNama_dosen(String nama_dosen) {
        this.nama_dosen = nama_dosen;
    }

    public int getNidn_dosen() {
        return nidn_dosen;
    }

    public void setNidn_dosen(int nidn_dosen) {
        this.nidn_dosen = nidn_dosen;
    }

    public String getStatus_dosen() {
        return status_dosen;
    }

    public void setStatus_dosen(String status_dosen) {
        this.status_dosen = status_dosen;
    }
}
