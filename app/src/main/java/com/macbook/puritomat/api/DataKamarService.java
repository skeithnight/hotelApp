package com.macbook.puritomat.api;

import com.macbook.puritomat.model.Kamar;
import com.macbook.puritomat.model.TipeKamar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DataKamarService {
    @GET("tipe-kamar")
    Call<ArrayList<TipeKamar>> getListDataTipeKamar(@Header("Authorization") String token);

    @GET("kamar")
    Call<ArrayList<Kamar>> getListDataKamar(@Header("Authorization") String token);

    @POST("kamar")
    Call<ResponseBody> postDataKamar(@Header("Authorization") String token, @Body Kamar kamar);
}
