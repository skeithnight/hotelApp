package com.macbook.puritomat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tamu {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nik")
    @Expose
    private String nik;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("foto")
    @Expose
    private Object foto;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("nomorHandphone")
    @Expose
    private String nomorHandphone;

    public Tamu() {
    }

    public Tamu(String nik, String nama, String alamat, String nomorHandphone) {
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.nomorHandphone = nomorHandphone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Object getFoto() {
        return foto;
    }

    public void setFoto(Object foto) {
        this.foto = foto;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomorHandphone() {
        return nomorHandphone;
    }

    public void setNomorHandphone(String nomorHandphone) {
        this.nomorHandphone = nomorHandphone;
    }

}
