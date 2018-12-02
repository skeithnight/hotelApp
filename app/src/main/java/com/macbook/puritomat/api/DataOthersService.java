package com.macbook.puritomat.api;

import android.view.Menu;

import com.macbook.puritomat.model.DataMenu;
import com.macbook.puritomat.model.DataOthers;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DataOthersService {
    @GET("layanan-lain")
    Call<ArrayList<DataOthers>> getListDataOthers(@Header("Authorization") String token);

    @POST("layanan-lain")
    Call<ResponseBody> postDataOthers(@Header("Authorization") String token, @Body DataOthers others);
}
