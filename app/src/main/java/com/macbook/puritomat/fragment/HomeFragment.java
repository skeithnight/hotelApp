package com.macbook.puritomat.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.macbook.puritomat.R;
import com.macbook.puritomat.TampilDialog;
import com.macbook.puritomat.api.APIClient;
import com.macbook.puritomat.api.DataKamarService;
import com.macbook.puritomat.api.DataTransaksiService;
import com.macbook.puritomat.model.Kamar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    static String TAG = "Testing";

    View mview;private TampilDialog tampilDialog;
    //    SharedPreferences
    String token;
    SharedPreferences mSPLogin;

    @BindView(R.id.tx_terisi)
    TextView tx_terisi;
    @BindView(R.id.tx_kosong)
    TextView tx_kosong;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,mview);

        tampilDialog = new TampilDialog(mview.getContext());

        // show loading
        tampilDialog.showLoading();

        initializeSP();

        // get Data terisi
        getDataTerisi();
        // get data kosong
        getDataKosong();

        return mview;
    }
    private void initializeSP() {
        mSPLogin = mview.getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token",null);
    }

    private void getDataTerisi() {
        DataTransaksiService dataTransaksiService = APIClient.getClient().create(DataTransaksiService.class);
        dataTransaksiService.getlistKamarIsi("Bearer "+token).enqueue(new Callback<ArrayList<Kamar>>() {

            @Override
            public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                // get Data Kamar
                ArrayList<Kamar> kamars = new ArrayList<>();
                kamars = response.body();
                try {
                    tx_terisi.setText(String.valueOf(kamars.size()));
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {
                tampilDialog.dismissLoading();
                tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
            }
        });
    }

    private void getDataKosong() {
        DataTransaksiService dataTransaksiService = APIClient.getClient().create(DataTransaksiService.class);
        dataTransaksiService.getlistKamarKosong("Bearer "+token).enqueue(new Callback<ArrayList<Kamar>>() {

            @Override
            public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                tampilDialog.dismissLoading();
                // get Data Kamar
                ArrayList<Kamar> kamars = new ArrayList<>();
                kamars = response.body();
                try {
                    tx_kosong.setText(String.valueOf(kamars.size()));
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {
                tampilDialog.dismissLoading();
                tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
            }
        });

    }

}
