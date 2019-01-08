package com.macbook.puritomat.api;

import com.macbook.puritomat.model.DataLaundry;
import com.macbook.puritomat.model.Kamar;
import com.macbook.puritomat.model.Tamu;
import com.macbook.puritomat.model.Transaksi;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DataTransaksiService {
    @GET("kamar/filter/kosong")
    Call<ArrayList<Kamar>> getlistKamarKosong(@Header("Authorization") String token);

    @GET("kamar/filter/isi")
    Call<ArrayList<Kamar>> getlistKamarIsi(@Header("Authorization") String token);

    @GET("transaksi")
    Call<ArrayList<Transaksi>> getlistTransaksi(@Header("Authorization") String token);

    @POST("tamu")
    Call<Tamu> postTamu(@Header("Authorization") String token, @Body Tamu tamu);

    @POST("transaksi")
    Call<Transaksi> postTransaksi(@Header("Authorization") String token, @Body Transaksi transaksi);
}
