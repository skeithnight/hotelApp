package com.macbook.puritomat.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.macbook.puritomat.R;
import com.macbook.puritomat.TampilDialog;
import com.macbook.puritomat.api.APIClient;
import com.macbook.puritomat.api.DataKamarService;
import com.macbook.puritomat.api.DataTransaksiService;
import com.macbook.puritomat.model.TipeKamar;
import com.macbook.puritomat.model.Transaksi;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiFragment extends Fragment {

    View mView;
    static String TAG = "Testing";

    private TampilDialog tampilDialog;
    //    SharedPreferences
    String token;
    SharedPreferences mSPLogin;

    ArrayList<Transaksi> transaksiArrayList = new ArrayList<>();

    public TransaksiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_transaksi, container, false);
        ButterKnife.bind(this,mView);
        tampilDialog = new TampilDialog(mView.getContext());

        // show loading
        tampilDialog.showLoading();
        initializeSP();

        getDataTransaksi();

        return mView ;
    }

    private void initializeSP() {
        mSPLogin = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token", null);
    }

    private void getDataTransaksi() {
        if (token != null) {
            DataTransaksiService dataTransaksiService = APIClient.getClient().create(DataTransaksiService.class);
            dataTransaksiService.getlistTransaksi("Bearer " + token).enqueue(new Callback<ArrayList<Transaksi>>() {

                @Override
                public void onResponse(Call<ArrayList<Transaksi>> call, Response<ArrayList<Transaksi>> response) {
                    tampilDialog.dismissLoading();
                    try {
                        transaksiArrayList = response.body();
                    }catch (Exception e){
                        tampilDialog.showDialog(getString(R.string.dialog_title_failed), e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Transaksi>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed), t.getMessage());
                }
            });
        }
    }

}
