package com.example.sicc.models;

public class Constant {
    // Jangan Lupa Ubah IP ADDRESS
    public static final String URL = "http://192.168.0.13/be-sicc/public/";
    public static final String HOME = URL + "API";
    public static final String LOGIN = HOME + "/users/login";
    public static final String LOMBA = HOME + "/lomba/getLomba";
    public static final String DATA_USER = HOME + "/users/getDatauser";
    public static final String DETAIL_LOMBA = HOME + "/lomba/getDetailLomba";
    public static final String DOSEN_PEMBIMBING = HOME + "/PengajuanDospem/getDospem";
    public static final String DETAIL_DOSEN_PEMBIMBING = HOME + "/PengajuanDospem/getDetailDospem";
    public static final String PENGAJUAN_DOSPEM = HOME + "/PengajuanDospem/pengajuanDospem";
    public static final String PENGAJUAN_JUDUL = HOME + "/Judul/pengajuanJudul";
    public static final String PENGAJUAN_REVISI_JUDUL = HOME + "/Judul/pengajuanRevisiJudul";
    public static final String DETAIL_JUDUL = HOME + "/Judul/getDetailJudul";
}
