package com.example.sicc.models;

public class Constant {
    // Jangan Lupa Ubah IP ADDRESS
    public static final String URL = "http://192.168.199.53:80/";
    public static final String HOME = URL + "API";
    public static final String LOGIN = HOME + "/users/login";
    public static final String LUPA_PASSWORD = HOME + "/users/lupaPassword";
    public static final String CEK_OTP = HOME + "/users/cekOtp";
    public static final String RESET_PASSWORD = HOME + "/users/resetPassword";
    public static final String LOMBA = HOME + "/lomba/getLomba";
    public static final String DATA_USER = HOME + "/users/getDatauser";
    public static final String DETAIL_LOMBA = HOME + "/lomba/getDetailLomba";
    public static final String DOSEN_PEMBIMBING = HOME + "/PengajuanDospem/getDospem";
    public static final String DETAIL_DOSEN_PEMBIMBING = HOME + "/PengajuanDospem/getDetailDospem";
    public static final String PENGAJUAN_DOSPEM = HOME + "/PengajuanDospem/pengajuanDospem";
    public static final String PENGAJUAN_JUDUL = HOME + "/Judul/pengajuanJudul";
    public static final String PENGAJUAN_REVISI_JUDUL = HOME + "/Judul/pengajuanRevisiJudul";
    public static final String DETAIL_JUDUL = HOME + "/Judul/getDetailJudul";
    public static final String PENGAJUAN_PROPOSAL = HOME + "/Proposal/pengajuanProposal";
    public static final String PENGAJUAN_REVISI_PROPOSAL = HOME + "/Proposal/pengajuanRevisiProposal";
    public static final String DETAIL_PROPOSAL = HOME + "/Proposal/getDetailProposal";
}
