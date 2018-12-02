package com.macbook.puritomat.api;

import com.macbook.puritomat.model.RequestLogin;
import com.macbook.puritomat.model.Resepsionis;
import com.macbook.puritomat.model.ResponseLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {
    @POST("administrator/login")
    Call<ResponseLogin> postLogin(@Body RequestLogin requestLogin);

    @GET("administrator/check-session")
    Call<Resepsionis> getCheckLogin(@Header("Authorization") String token);
}
