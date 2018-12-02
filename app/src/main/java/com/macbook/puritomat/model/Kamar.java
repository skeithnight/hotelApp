package com.macbook.puritomat.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kamar {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("nomor")
    @Expose
    private Integer nomor;
    @SerializedName("tipeKamar")
    @Expose
    private TipeKamar tipeKamar;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public Kamar() {
    }

    public Kamar(String nama, Integer nomor, TipeKamar tipeKamar, Boolean status) {
        this.nama = nama;
        this.nomor = nomor;
        this.tipeKamar = tipeKamar;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getNomor() {
        return nomor;
    }

    public void setNomor(Integer nomor) {
        this.nomor = nomor;
    }

    public TipeKamar getTipeKamar() {
        return tipeKamar;
    }

    public void setTipeKamar(TipeKamar tipeKamar) {
        this.tipeKamar = tipeKamar;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
