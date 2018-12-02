package com.macbook.puritomat.api;

import com.macbook.puritomat.model.DataLaundry;
import com.macbook.puritomat.model.Kamar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface DataTransaksiService {
    @GET("kamar/filter/kosong")
    Call<ArrayList<Kamar>> getlistKamarKosong(@Header("Authorization") String token);

    @GET("kamar/filter/isi")
    Call<ArrayList<Kamar>> getlistKamarIsi(@Header("Authorization") String token);
}
