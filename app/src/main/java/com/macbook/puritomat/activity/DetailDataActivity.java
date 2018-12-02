package com.macbook.puritomat.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.macbook.puritomat.R;
import com.macbook.puritomat.TampilDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailDataActivity extends AppCompatActivity {
    String menu = "";
    String typeDetail = "";
    ViewStub stub = null;
    static String TAG = "Testing";
    private TampilDialog tampilDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);
        ButterKnife.bind(this);

        tampilDialog = new TampilDialog(this);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            menu= null;
        } else {
            menu= extras.getString("menu");
            typeDetail= extras.getString("typeDetail");
        }

        setDetailDataCardView();

    }

    private void setDetailDataCardView() {
        if (stub == null){
            stub = (ViewStub) findViewById(R.id.VS_detail_data);
            if (menu.equals(getString(R.string.manajemen_1))){
                setTitle(typeDetail+" "+getString(R.string.manajemen_1));
                stub.setLayoutResource(R.layout.detail_data_kamar);
                View inflated = stub.inflate();
                contentDataKamar();
            }else if (menu.equals(getString(R.string.manajemen_2))){
                setTitle(typeDetail+" "+getString(R.string.manajemen_2));
                stub.setLayoutResource(R.layout.detail_data_menu);
                View inflated = stub.inflate();
                contentDataMenu();
            }else if (menu.equals(getString(R.string.manajemen_3))){
                stub.setLayoutResource(R.layout.detail_data_laundry);
                setTitle(typeDetail+" "+getString(R.string.manajemen_3));
                View inflated = stub.inflate();
                contentDataLaundry();
            }else if (menu.equals(getString(R.string.manajemen_4))){
                stub.setLayoutResource(R.layout.detail_data_yang_lain);
                setTitle(typeDetail+" "+getString(R.string.manajemen_4));
                View inflated = stub.inflate();
                contentDataOthers();
            }else {
                Toast.makeText(this, "Failed to render", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    Data Kamar
    Spinner spinner;
    TextInputEditText nomorKamar;
    String mtipeKamar = null;
    private void contentDataKamar() {
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner_kategori);
        nomorKamar = (TextInputEditText) findViewById(R.id.et_nomor_kamar);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                mtipeKamar = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // Showing no changed
                Toast.makeText(parent.getContext(), "no changed!", Toast.LENGTH_LONG).show();
            }
        });
    }

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
            tampilDialog.showDialog(getString(R.string.dialog_title_success),getString(R.string.dialog_message_1));
        }
    }

//    Data Menu
    TextInputEditText namaMenu, hargaMenu, deskripsiMenu;
    public void contentDataMenu(){
        namaMenu = (TextInputEditText) findViewById(R.id.et_nama_menu);
        hargaMenu = (TextInputEditText) findViewById(R.id.et_harga_menu);
        deskripsiMenu = (TextInputEditText) findViewById(R.id.et_deskripsi_menu);
    }
    public boolean validasiDataMenu(){
        if (namaMenu.getText().toString().equals("")|| hargaMenu.getText().toString().equals("")|| deskripsiMenu.getText().toString().equals("")){
            return true;
        }else {
            return false;
        }
    }
    public void inputDataMenu(){
        Log.i(TAG, "inputDataMenu: "+namaMenu.getText());
        Log.i(TAG, "inputDataMenu: "+hargaMenu.getText());
        Log.i(TAG, "inputDataMenu: "+deskripsiMenu.getText());
        if (validasiDataMenu()){
//            kalau kosong muncul alert
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4));
//            Toast.makeText(this, "kosong", Toast.LENGTH_SHORT).show();
        }else {
//            kalau tidak kosong maka lanjutkan input data
            tampilDialog.showDialog(getString(R.string.dialog_title_success),getString(R.string.dialog_message_1));
        }
    }

    //    Data Laundry
    TextInputEditText namaLaundry, hargaLaundry, deskripsiLaundry;
    public void contentDataLaundry(){
        namaLaundry = (TextInputEditText) findViewById(R.id.et_nama_laundry);
        hargaLaundry = (TextInputEditText) findViewById(R.id.et_harga_laundry);
        deskripsiLaundry = (TextInputEditText) findViewById(R.id.et_deskripsi_laundry);
    }
    public boolean validasiDataLaundry(){
        if (namaLaundry.getText().toString().equals("")|| hargaLaundry.getText().toString().equals("")|| deskripsiLaundry.getText().toString().equals("")){
            return true;
        }else {
            return false;
        }
    }
    public void inputDataLaundry(){
        if (validasiDataLaundry()){
//            kalau kosong muncul alert
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4));
        }else {
//            kalau tidak kosong maka lanjutkan input data
            tampilDialog.showDialog(getString(R.string.dialog_title_success),getString(R.string.dialog_message_1));
        }
    }

    //    Data Laundry
    TextInputEditText namaOthers, hargaOthers, deskripsiOthers;
    public void contentDataOthers(){
        namaOthers = (TextInputEditText) findViewById(R.id.et_nama_others);
        hargaOthers = (TextInputEditText) findViewById(R.id.et_harga_others);
        deskripsiOthers = (TextInputEditText) findViewById(R.id.et_deskripsi_others);
    }
    public boolean validasiDataOthers(){
        if (namaOthers.getText().toString().equals("")|| hargaOthers.getText().toString().equals("")|| deskripsiOthers.getText().toString().equals("")){
            return true;
        }else {
            return false;
        }
    }
    public void inputDataOthers(){
        if (validasiDataOthers()){
//            kalau kosong muncul alert
            tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4));
        }else {
//            kalau tidak kosong maka lanjutkan input data
            tampilDialog.showDialog(getString(R.string.dialog_title_success),getString(R.string.dialog_message_1));
        }
    }

    @OnClick(R.id.btn_simpan_data_manajemen)
    public void save(){
        if (typeDetail.equals(getString(R.string.tambah_data))) {
            if (menu.equals(getString(R.string.manajemen_1))) {
                inputDataKamar();
            }else if (menu.equals(getString(R.string.manajemen_2))) {
                inputDataMenu();
            }else if (menu.equals(getString(R.string.manajemen_3))) {
                inputDataLaundry();
            }else if (menu.equals(getString(R.string.manajemen_4))) {
                inputDataOthers();
            }else {
                tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.manajemen_3));
            }
        }
    }


}
