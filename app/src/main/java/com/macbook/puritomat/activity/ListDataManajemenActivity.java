package com.macbook.puritomat.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.macbook.puritomat.R;
import com.macbook.puritomat.TampilDialog;
import com.macbook.puritomat.adapter.ExpandableListAdapterKamar;
import com.macbook.puritomat.adapter.RecyclerViewAdapterLaundry;
import com.macbook.puritomat.adapter.RecyclerViewAdapterMenu;
import com.macbook.puritomat.adapter.RecyclerViewAdapterOthers;
import com.macbook.puritomat.api.APIClient;
import com.macbook.puritomat.api.DataKamarService;
import com.macbook.puritomat.api.DataLaundryService;
import com.macbook.puritomat.api.DataMenuService;
import com.macbook.puritomat.api.DataOthersService;
import com.macbook.puritomat.api.LoginService;
import com.macbook.puritomat.model.DataLaundry;
import com.macbook.puritomat.model.DataMenu;
import com.macbook.puritomat.model.DataOthers;
import com.macbook.puritomat.model.Kamar;
import com.macbook.puritomat.model.TipeKamar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDataManajemenActivity extends AppCompatActivity {
    String menu, token;

    static String TAG = "Testing";
    private TampilDialog tampilDialog;
    //    SharedPreferences
    SharedPreferences mSPLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_manajemen);
        ButterKnife.bind(this);

        tampilDialog = new TampilDialog(this);

        // show loading
        tampilDialog.showLoading();

        initializeSP();

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            menu= null;
        } else {
            menu= extras.getString("menu");
            if (menu.equals(getString(R.string.manajemen_1))){
                setTitle(R.string.title_data_kamar);
                getDataTipeKamar();
            }else if (menu.equals(getString(R.string.manajemen_2))){
                setTitle(R.string.title_data_menu);
                getDataMenu();
            }else if (menu.equals(getString(R.string.manajemen_3))){
                setTitle(R.string.title_data_laundry);
                getDataLaundry();
            }else if (menu.equals(getString(R.string.manajemen_4))){
                setTitle(R.string.title_data_others);
                getDataOthers();
            }
        }
    }


    private void initializeSP() {
        mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token",null);
    }
    // Data Kamar
    ExpandableListAdapterKamar listAdapter;
    ExpandableListView expListView;
    ArrayList<TipeKamar> listDataHeader;
    HashMap<String, ArrayList<Kamar>> listDataChild;
    private void getDataTipeKamar() {
        if (token != null){
            DataKamarService dataKamarService = APIClient.getClient().create(DataKamarService.class);
            dataKamarService.getListDataTipeKamar("Bearer "+token).enqueue(new Callback<ArrayList<TipeKamar>>() {

                @Override
                public void onResponse(Call<ArrayList<TipeKamar>> call, Response<ArrayList<TipeKamar>> response) {
                    // get Data Kamar
                    listDataHeader = new ArrayList<TipeKamar>();
                    listDataHeader = response.body();
                    //getDataKamar
                    getDataKamar();
                }

                @Override
                public void onFailure(Call<ArrayList<TipeKamar>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }

    private void getDataKamar() {
        if (token != null){
            DataKamarService dataKamarService = APIClient.getClient().create(DataKamarService.class);
            dataKamarService.getListDataKamar("Bearer "+token).enqueue(new Callback<ArrayList<Kamar>>() {
                @Override
                public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                    tampilDialog.dismissLoading();
                    showListDataExp(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }

    private void showListDataExp(ArrayList<Kamar> listDataKamar) {
//        Preparing data
        listDataChild = new HashMap<String, ArrayList<Kamar>>();

        for (TipeKamar tipekamar: listDataHeader) {
            ArrayList<Kamar> kamarList = new ArrayList<Kamar>();
            for (Kamar kamar:listDataKamar) {
                if (kamar.getTipeKamar().getId().equals(tipekamar.getId())){
                    kamarList.add(kamar);
                    listDataChild.put(tipekamar.getId(),kamarList);
                }
            }
        }
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.exp_list_data_tipe_kamar);
        expListView.setVisibility(View.VISIBLE);
        listAdapter = new ExpandableListAdapterKamar(ListDataManajemenActivity.this, listDataHeader, listDataChild,"");

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

//    Data Menu
    ArrayList<DataMenu> listDataMenu = new ArrayList<DataMenu>();
    RecyclerView recyclerView;
    private RecyclerViewAdapterMenu mAdapter;
    private void getDataMenu() {
    if (token != null){

        DataMenuService dataMenuService = APIClient.getClient().create(DataMenuService.class);
        dataMenuService.getListDataMenu("Bearer "+token).enqueue(new Callback<ArrayList<DataMenu>>() {
            @Override
            public void onResponse(Call<ArrayList<DataMenu>> call, Response<ArrayList<DataMenu>> response) {
//                Gson gson = new Gson();
//                Log.i(TAG, "onResponse: "+gson.toJson(response.body()));
                listDataMenu = response.body();

                tampilDialog.dismissLoading();
                // initiate RecyclerView
                recyclerView = (RecyclerView) findViewById(R.id.rv_list_data_manajemen);
                recyclerView.setVisibility(View.VISIBLE);

                mAdapter = new RecyclerViewAdapterMenu(listDataMenu);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListDataManajemenActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ArrayList<DataMenu>> call, Throwable t) {
                tampilDialog.dismissLoading();
                tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
            }
        });
    }
}

    //    Data Laundry
    ArrayList<DataLaundry> listDataLaundry = new ArrayList<DataLaundry>();
    private RecyclerViewAdapterLaundry mAdapterLaundry;
    private void getDataLaundry() {
        if (token != null){

            DataLaundryService dataLaundryService = APIClient.getClient().create(DataLaundryService.class);
            dataLaundryService.getListDataLaundry("Bearer "+token).enqueue(new Callback<ArrayList<DataLaundry>>() {
                @Override
                public void onResponse(Call<ArrayList<DataLaundry>> call, Response<ArrayList<DataLaundry>> response) {
//                Gson gson = new Gson();
//                Log.i(TAG, "onResponse: "+gson.toJson(response.body()));
                    listDataLaundry = response.body();

                    tampilDialog.dismissLoading();
                    // initiate RecyclerView
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_data_manajemen);
                    recyclerView.setVisibility(View.VISIBLE);

                    mAdapterLaundry = new RecyclerViewAdapterLaundry(listDataLaundry);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListDataManajemenActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapterLaundry);
                    mAdapterLaundry.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<ArrayList<DataLaundry>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }

    //    Data Others
    ArrayList<DataOthers> listDataOthers = new ArrayList<DataOthers>();
    private RecyclerViewAdapterOthers mAdapterothers;
    private void getDataOthers() {
        if (token != null){

            DataOthersService dataOthersService = APIClient.getClient().create(DataOthersService.class);
            dataOthersService.getListDataOthers("Bearer "+token).enqueue(new Callback<ArrayList<DataOthers>>() {
                @Override
                public void onResponse(Call<ArrayList<DataOthers>> call, Response<ArrayList<DataOthers>> response) {
//                Gson gson = new Gson();
//                Log.i(TAG, "onResponse: "+gson.toJson(response.body()));
                    listDataOthers = response.body();

                    tampilDialog.dismissLoading();
                    // initiate RecyclerView
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_data_manajemen);
                    recyclerView.setVisibility(View.VISIBLE);

                    mAdapterothers = new RecyclerViewAdapterOthers(listDataOthers);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListDataManajemenActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapterothers);
                    mAdapterothers.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<ArrayList<DataOthers>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }

    private AlertDialog myDialog;
    private String[] items = {"Jenis Kamar","Nomor Kamar"};
    @OnClick(R.id.fab_listDataManajemen)
    public void onClickFAB(View view){
        if (menu.equals(getString(R.string.manajemen_1))){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pilih tambah data");
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0){
                        changeScreen("tipe-kamar");
                    }else {
                        changeScreen(menu);
                    }
                }
            });


            builder.setCancelable(false);
            myDialog = builder.create();
            myDialog.show();

        }else {
            changeScreen(menu);
        }
//        Toast.makeText(this, String.valueOf(view.getId())+" : "+menu, Toast.LENGTH_SHORT).show();
    }

    private void changeScreen(String menu){
        Intent intent = new Intent(this, DetailDataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("menu", menu);
        intent.putExtra("typeDetail", getString(R.string.tambah_data));
        startActivity(intent);
    }
}
