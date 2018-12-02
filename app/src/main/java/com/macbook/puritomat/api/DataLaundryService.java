package com.macbook.puritomat.api;

import com.macbook.puritomat.model.DataLaundry;
import com.macbook.puritomat.model.DataOthers;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DataLaundryService {
    @GET("laundry")
    Call<ArrayList<DataLaundry>> getListDataLaundry(@Header("Authorization") String token);

    @POST("laundry")
    Call<ResponseBody> postDataLaundry(@Header("Authorization") String token, @Body DataLaundry laundry);

}
