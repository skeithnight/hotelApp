package com.macbook.puritomat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailTransaksi<T> {

    @SerializedName("layanan")
    @Expose
    private T layanan;
    @SerializedName("from")
    @Expose
    private Long from;
    @SerializedName("to")
    @Expose
    private Long to;
    @SerializedName("jumlah")
    @Expose
    private Integer jumlah;

    public DetailTransaksi() {
    }

    public DetailTransaksi(T layanan, Long from, Integer jumlah) {
        this.layanan = layanan;
        this.from = from;
        this.jumlah = jumlah;
    }

    public T getLayanan() {
        return layanan;
    }

    public void setLayanan(T layanan) {
        this.layanan = layanan;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }
}
