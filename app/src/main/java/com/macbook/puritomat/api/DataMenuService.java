package com.macbook.puritomat.api;

import com.macbook.puritomat.model.DataMenu;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataMenuService {
    @GET("menu")
    Call<ArrayList<DataMenu>> getListDataMenu(@Header("Authorization") String token);

    @POST("menu")
    Call<ResponseBody> postDataMenu(@Header("Authorization") String token, @Body DataMenu menu);

    @DELETE("menu/{id}")
    Call<ResponseBody> deleteDataMenu(@Header("Authorization") String token, @Path("id") String id);
}
