package com.macbook.puritomat.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("administrator/login")
    Call<Map<String,String>> postKontak(@Body Map<String,String> login);
}
