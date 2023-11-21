package com.example.sicc.models;

public class Constant {
    // Jangan Lupa Ubah IP ADDRESS
    public static final String URL = "http://10.10.6.164/be-sicc/public/";
    public static final String HOME = URL + "API";
    public static final String LOGIN = HOME + "/users/login";
    public static final String LOMBA = HOME + "/lomba/getLomba";
    public static final String DATA_USER = HOME + "/users/getDatauser";
    public static final String DETAIL_LOMBA = HOME + "/lomba/getDetailLomba";
    public static final String DOSEN_PEMBIMBING = HOME + "/PengajuanDospem/getDospem";
    public static final String DETAIL_DOSEN_PEMBIMBING = HOME + "/PengajuanDospem/getDetailDospem";
    public static final String PENGAJUAN_DOSPEM = HOME + "/PengajuanDospem/pengajuanDospem";
}
