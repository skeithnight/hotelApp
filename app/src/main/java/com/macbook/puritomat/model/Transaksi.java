package com.macbook.puritomat.model;

import android.view.Menu;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaksi {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Resepsionis")
    @Expose
    private Resepsionis Resepsionis;
    @SerializedName("tamu")
    @Expose
    private Tamu tamu;
    @SerializedName("kamar")
    @Expose
    private List<DetailTransaksi<Kamar>> kamar;
    @SerializedName("menu")
    @Expose
    private List<DetailTransaksi<DataMenu>> menu;
    @SerializedName("laundry")
    @Expose
    private List<DetailTransaksi<DataLaundry>> laundry;
    @SerializedName("layananLain")
    @Expose
    private List<DetailTransaksi<DataOthers>> layananLain;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paymentMethod")
    @Expose
    private String paymentMethod;

    public Transaksi() {
    }

    public Transaksi(Resepsionis resepsionis, Tamu tamu, List<DetailTransaksi<Kamar>> kamar, String status) {
        Resepsionis = resepsionis;
        this.tamu = tamu;
        this.kamar = kamar;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public com.macbook.puritomat.model.Resepsionis getResepsionis() {
        return Resepsionis;
    }

    public void setResepsionis(com.macbook.puritomat.model.Resepsionis resepsionis) {
        Resepsionis = resepsionis;
    }

    public Tamu getTamu() {
        return tamu;
    }

    public void setTamu(Tamu tamu) {
        this.tamu = tamu;
    }

    public List<DetailTransaksi<Kamar>> getKamar() {
        return kamar;
    }

    public void setKamar(List<DetailTransaksi<Kamar>> kamar) {
        this.kamar = kamar;
    }

    public List<DetailTransaksi<DataMenu>> getMenu() {
        return menu;
    }

    public void setMenu(List<DetailTransaksi<DataMenu>> menu) {
        this.menu = menu;
    }

    public List<DetailTransaksi<DataLaundry>> getLaundry() {
        return laundry;
    }

    public void setLaundry(List<DetailTransaksi<DataLaundry>> laundry) {
        this.laundry = laundry;
    }

    public List<DetailTransaksi<DataOthers>> getLayananLain() {
        return layananLain;
    }

    public void setLayananLain(List<DetailTransaksi<DataOthers>> layananLain) {
        this.layananLain = layananLain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}