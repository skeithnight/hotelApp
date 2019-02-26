package com.macbook.puritomat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.macbook.puritomat.R;
import com.macbook.puritomat.TampilDialog;
import com.macbook.puritomat.api.APIClient;
import com.macbook.puritomat.api.DataKamarService;
import com.macbook.puritomat.api.DataLaundryService;
import com.macbook.puritomat.api.DataMenuService;
import com.macbook.puritomat.api.DataOthersService;
import com.macbook.puritomat.model.DataLaundry;
import com.macbook.puritomat.model.DataMenu;
import com.macbook.puritomat.model.DataOthers;
import com.macbook.puritomat.model.Kamar;
import com.macbook.puritomat.model.TipeKamar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDataActivity extends AppCompatActivity {
    String menu = "";
    String typeDetail = "";
    ViewStub stub = null;
    Bundle extras;
    static String TAG = "Testing";
    private TampilDialog tampilDialog;
    //    SharedPreferences
    String token;
    SharedPreferences mSPLogin;
    DataMenu dataMenu;
    DataOthers dataOthers;
    DataLaundry dataLaundry;
    Kamar kamar;

    @BindView(R.id.btn_hapus_data_manajemen)
    Button btnHapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);
        ButterKnife.bind(this);

        tampilDialog = new TampilDialog(this);

        // show loading
        tampilDialog.showLoading();

        initializeSP();

        extras = getIntent().getExtras();
        if(extras == null) {
            menu= null;
        } else {
            menu= extras.getString("menu");
            typeDetail= extras.getString("typeDetail");
        }

        setDetailDataCardView();

    }

    private void initializeSP() {
        mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token",null);
    }

    private void setDetailDataCardView() {
        Gson gson = new Gson();
        if (stub == null){
            stub = (ViewStub) findViewById(R.id.VS_detail_data);

            if (typeDetail.equals("detail")){
                btnHapus.setVisibility(View.VISIBLE);
            }

            if (menu.equals(getString(R.string.manajemen_1))){
                setTitle(typeDetail+" "+getString(R.string.manajemen_1));
                stub.setLayoutResource(R.layout.detail_data_kamar);
                View inflated = stub.inflate();
                contentDataKamar();
            }else if (menu.equals(getString(R.string.manajemen_2))){
                setTitle(typeDetail+" "+getString(R.string.manajemen_2));
                stub.setLayoutResource(R.layout.detail_data_menu);
                View inflated = stub.inflate();
                dataMenu = gson.fromJson(extras.getString("data"),DataMenu.class);
                contentDataMenu();
            }else if (menu.equals(getString(R.string.manajemen_3))){
                stub.setLayoutResource(R.layout.detail_data_laundry);
                setTitle(typeDetail+" "+getString(R.string.manajemen_3));
                View inflated = stub.inflate();
                dataLaundry = gson.fromJson(extras.getString("data"),DataLaundry.class);
                contentDataLaundry();
            }else if (menu.equals(getString(R.string.manajemen_4))){
                stub.setLayoutResource(R.layout.detail_data_yang_lain);
                setTitle(typeDetail+" "+getString(R.string.manajemen_4));
                View inflated = stub.inflate();
                dataOthers = gson.fromJson(extras.getString("data"),DataOthers.class);
                contentDataOthers();
            }else if (menu.equals("tipe-kamar")){
//                Toast.makeText(this, "tipe-kamar", Toast.LENGTH_SHORT).show();
                setTitle(typeDetail+" "+getString(R.string.manajemen_1));
                stub.setLayoutResource(R.layout.detail_data_tipe_kamar);
                View inflated = stub.inflate();
                contentDataTipeKamar();
            }else {
                Toast.makeText(this, "Failed to render", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    Tipe Kamar
TextInputEditText tipeKamar;
    TextInputEditText hargaKamar;
private void contentDataTipeKamar() {
    tipeKamar = (TextInputEditText) findViewById(R.id.et_tipe_kamar);
    hargaKamar = (TextInputEditText) findViewById(R.id.et_harga_kamar);

    // getDataTipeKamar
    if (token != null){

        tampilDialog.dismissLoading();
    }
}
    public boolean validasiDataTipeKamar(){
        if (tipeKamar.getText().toString().equals("")|| hargaKamar.getText().toString().equals("")){
            return true;
        }else {
            return false;
        }
    }

    public void inputDataTipeKamar(){
        if (validasiDataTipeKamar()){
//            kalau kosong muncul alert
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4));
        }else {
//            kalau tidak kosong maka lanjutkan input data
            if (token != null){
                TipeKamar tipeKamar1 = new TipeKamar(tipeKamar.getText().toString(),Integer.parseInt(hargaKamar.getText().toString()));
//                Gson gson = new Gson();
//                Log.i(TAG, "inputDataKamar: "+gson.toJson(kamar));
                DataKamarService dataKamarService = APIClient.getClient().create(DataKamarService.class);
                dataKamarService.postDataTipeKamar("Bearer "+token,tipeKamar1).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tampilDialog.showDialog(getString(R.string.dialog_title_success), getString(R.string.dialog_message_1));
                            Intent intent = new Intent(DetailDataActivity.this, ListDataManajemenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("menu",getString(R.string.manajemen_1));
                            startActivity(intent);
                        }else {
                            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_2));
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                    }
                });
            }
        }
    }


    //    Data Kamar
    Spinner spinner;
    TextInputEditText nomorKamar;
    ArrayList<TipeKamar> tipeKamars;
    TipeKamar mtipeKamar = null;
    private void contentDataKamar() {
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner_kategori);
        nomorKamar = (TextInputEditText) findViewById(R.id.et_nomor_kamar);

        // getDataTipeKamar
        if (token != null){
            DataKamarService dataKamarService = APIClient.getClient().create(DataKamarService.class);
            dataKamarService.getListDataTipeKamar("Bearer "+token).enqueue(new Callback<ArrayList<TipeKamar>>() {

                @Override
                public void onResponse(Call<ArrayList<TipeKamar>> call, Response<ArrayList<TipeKamar>> response) {
                    tampilDialog.dismissLoading();
                    // get Data Kamar
                    tipeKamars = new ArrayList<TipeKamar>();
                    tipeKamars = response.body();
                    // initiate spinner
                    ArrayList<String> listNamaTipeKamar = new ArrayList<>();
                    for (TipeKamar item:tipeKamars) {
                        listNamaTipeKamar.add(item.getNama());
                    }
                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DetailDataActivity.this, android.R.layout.simple_spinner_item,listNamaTipeKamar);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);


                    // Spinner click listener
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // On selecting a spinner item
                            mtipeKamar = tipeKamars.get(position);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<TipeKamar>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }


//    Data Kamar
    public boolean validasiDataKamar(){
        if (nomorKamar.getText().toString().equals("")|| mtipeKamar == null){
            return true;
        }else {
            return false;
        }
    }
    public void inputDataKamar(){
        if (validasiDataKamar()){
//            kalau kosong muncul alert
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4));
        }else {
//            kalau tidak kosong maka lanjutkan input data
            if (token != null){
                String nama = mtipeKamar.getNama().trim().toString().concat(" "+String.valueOf(nomorKamar.getText().toString()));
                Kamar kamar = new Kamar( nama,Integer.parseInt(nomorKamar.getText().toString()),mtipeKamar,true);
//                Gson gson = new Gson();
//                Log.i(TAG, "inputDataKamar: "+gson.toJson(kamar));
                DataKamarService dataKamarService = APIClient.getClient().create(DataKamarService.class);
                dataKamarService.postDataKamar("Bearer "+token,kamar).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tampilDialog.showDialog(getString(R.string.dialog_title_success), getString(R.string.dialog_message_1));
                            Intent intent = new Intent(DetailDataActivity.this, ListDataManajemenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("menu",getString(R.string.manajemen_1));
                            startActivity(intent);
                        }else {
                            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_2));
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                    }
                });
            }
        }
    }

//    Data Menu
    TextInputEditText namaMenu, hargaMenu, deskripsiMenu;
    public void contentDataMenu(){
        namaMenu = (TextInputEditText) findViewById(R.id.et_nama_menu);
        hargaMenu = (TextInputEditText) findViewById(R.id.et_harga_menu);
        deskripsiMenu = (TextInputEditText) findViewById(R.id.et_deskripsi_menu);
        if (typeDetail.equals("detail")){
            namaMenu.setText(dataMenu.getNama());
            hargaMenu.setText(String.valueOf(dataMenu.getHarga()));
            deskripsiMenu.setText(dataMenu.getDeskripsi());
        }
        tampilDialog.dismissLoading();
    }
    public boolean validasiDataMenu(){
        if (namaMenu.getText().toString().equals("")|| hargaMenu.getText().toString().equals("")|| deskripsiMenu.getText().toString().equals("")){
            return true;
        }else {
            return false;
        }
    }
    public void inputDataMenu(){
        DataMenu dataMenu1;
        Log.i(TAG, "inputDataMenu: woooi");
        if (validasiDataMenu()){
//            kalau kosong muncul alert
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4));
//            Toast.makeText(this, "kosong", Toast.LENGTH_SHORT).show();
        }else {
//            kalau tidak kosong maka lanjutkan input data
            if (typeDetail.equals("detail")){
                dataMenu1 = new DataMenu(dataMenu.getId(),namaMenu.getText().toString(), Integer.parseInt(hargaMenu.getText().toString()), deskripsiMenu.getText().toString());
            }else {
                dataMenu1 = new DataMenu(namaMenu.getText().toString(), Integer.parseInt(hargaMenu.getText().toString()), deskripsiMenu.getText().toString());
            }
            //            Gson gson = new Gson();
//            Log.i(TAG, "inputDataKamar: "+gson.toJson(dataMenu));
            DataMenuService dataMenuService = APIClient.getClient().create(DataMenuService.class);
            dataMenuService.postDataMenu("Bearer "+token,dataMenu1).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        tampilDialog.showDialog(getString(R.string.dialog_title_success), getString(R.string.dialog_message_1));
                        Intent intent = new Intent(DetailDataActivity.this, ListDataManajemenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("menu",getString(R.string.manajemen_2));
                        startActivity(intent);
                    }else {
                        tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_2));
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }

    //    Data Laundry
    TextInputEditText namaLaundry, hargaLaundry, deskripsiLaundry;
    public void contentDataLaundry(){
        namaLaundry = (TextInputEditText) findViewById(R.id.et_nama_laundry);
        hargaLaundry = (TextInputEditText) findViewById(R.id.et_harga_laundry);
        deskripsiLaundry = (TextInputEditText) findViewById(R.id.et_deskripsi_laundry);
        if (typeDetail.equals("detail")){
            namaLaundry.setText(dataLaundry.getNama());
            hargaLaundry.setText(String.valueOf(dataLaundry.getHarga()));
            deskripsiLaundry.setText(dataLaundry.getDeskripsi());
        }
        tampilDialog.dismissLoading();
    }
    public boolean validasiDataLaundry(){
        if (namaLaundry.getText().toString().equals("")|| hargaLaundry.getText().toString().equals("")|| deskripsiLaundry.getText().toString().equals("")){
            return true;
        }else {
            return false;
        }
    }
    public void inputDataLaundry(){
        DataLaundry dataLaundry1;
        if (validasiDataLaundry()){
//            kalau kosong muncul alert
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4));
        }else {
//            kalau tidak kosong maka lanjutkan input data
            if (typeDetail.equals("detail")) {
                dataLaundry1 = new DataLaundry(dataLaundry.getId(),namaLaundry.getText().toString(), Integer.parseInt(hargaLaundry.getText().toString()), deskripsiLaundry.getText().toString());
            }else {
                dataLaundry1 = new DataLaundry(namaLaundry.getText().toString(), Integer.parseInt(hargaLaundry.getText().toString()), deskripsiLaundry.getText().toString());
            }
            Gson gson = new Gson();
            Log.i(TAG, "inputDataKamar: "+gson.toJson(dataLaundry));
            DataLaundryService dataLaundryService = APIClient.getClient().create(DataLaundryService.class);
            dataLaundryService.postDataLaundry("Bearer "+token,dataLaundry1).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        tampilDialog.showDialog(getString(R.string.dialog_title_success), getString(R.string.dialog_message_1));
                        Intent intent = new Intent(DetailDataActivity.this, ListDataManajemenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("menu",getString(R.string.manajemen_3));
                        startActivity(intent);
                    }else {
                        tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_2));
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }

    //    Data Laundry
    TextInputEditText namaOthers, hargaOthers, deskripsiOthers;
    public void contentDataOthers(){
        namaOthers = (TextInputEditText) findViewById(R.id.et_nama_others);
        hargaOthers = (TextInputEditText) findViewById(R.id.et_harga_others);
        deskripsiOthers = (TextInputEditText) findViewById(R.id.et_deskripsi_others);
        if (typeDetail.equals("detail")){
            namaOthers.setText(dataOthers.getNama());
            hargaOthers.setText(String.valueOf(dataOthers.getHarga()));
            deskripsiOthers.setText(dataOthers.getDeskripsi());
        }
        tampilDialog.dismissLoading();
    }
    public boolean validasiDataOthers(){
        if (namaOthers.getText().toString().equals("")|| hargaOthers.getText().toString().equals("")|| deskripsiOthers.getText().toString().equals("")){
            return true;
        }else {
            return false;
        }
    }
    public void inputDataOthers(){
        DataOthers dataOthers1;
        if (validasiDataOthers()){
//            kalau kosong muncul alert
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4));
        }else {
//            kalau tidak kosong maka lanjutkan input data
            if(typeDetail.equals("detail")) {
                dataOthers1 = new DataOthers(dataOthers.getId(),namaOthers.getText().toString(), Integer.parseInt(hargaOthers.getText().toString()), deskripsiOthers.getText().toString());
            }else {
                dataOthers1 = new DataOthers(namaOthers.getText().toString(), Integer.parseInt(hargaOthers.getText().toString()), deskripsiOthers.getText().toString());
            }
            Gson gson = new Gson();
            Log.i(TAG, "inputDataKamar: "+gson.toJson(dataOthers));
            DataOthersService dataOthersService = APIClient.getClient().create(DataOthersService.class);
            dataOthersService.postDataOthers("Bearer "+token,dataOthers1).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        tampilDialog.showDialog(getString(R.string.dialog_title_success), getString(R.string.dialog_message_1));
                        Intent intent = new Intent(DetailDataActivity.this, ListDataManajemenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("menu",getString(R.string.manajemen_4));
                        startActivity(intent);
                    }else {
                        tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_2));
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }

//    Submit Data
    @OnClick(R.id.btn_simpan_data_manajemen)
    public void save(){
        if (menu.equals(getString(R.string.manajemen_1))) {
            inputDataKamar();
        }else if (menu.equals(getString(R.string.manajemen_2))) {
            inputDataMenu();
        }else if (menu.equals(getString(R.string.manajemen_3))) {
            inputDataLaundry();
        }else if (menu.equals(getString(R.string.manajemen_4))) {
            inputDataOthers();
        }else if (menu.equals("tipe-kamar")) {
            inputDataTipeKamar();
        }else {
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.manajemen_3));
        }
    }

    @OnClick(R.id.btn_hapus_data_manajemen)
    public void hapusData(){
        if (menu.equals(getString(R.string.manajemen_1))) {
//            tampilDialog.showValidate("Information","Apakah anda yakin akan menghapus ini?",dat);
        }else if (menu.equals(getString(R.string.manajemen_2))) {
            tampilDialog.showValidate("Information","Apakah anda yakin akan menghapus ini?",dataMenu.getId(),getString(R.string.manajemen_2));
        }else if (menu.equals(getString(R.string.manajemen_3))) {
            tampilDialog.showValidate("Information","Apakah anda yakin akan menghapus ini?",dataLaundry.getId(),getString(R.string.manajemen_3));
        }else if (menu.equals(getString(R.string.manajemen_4))) {
            tampilDialog.showValidate("Information","Apakah anda yakin akan menghapus ini?",dataOthers.getId(),getString(R.string.manajemen_4));
        }else {
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.manajemen_3));
        }
    }


}
