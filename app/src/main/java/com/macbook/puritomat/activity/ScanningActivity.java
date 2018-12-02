package com.macbook.puritomat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.macbook.puritomat.R;
import com.macbook.puritomat.TampilDialog;
import com.macbook.puritomat.api.APIClient;
import com.macbook.puritomat.api.DataKamarService;
import com.macbook.puritomat.model.Kamar;
import com.macbook.puritomat.model.TipeKamar;
import com.microblink.MicroblinkSDK;
import com.microblink.activity.DocumentScanActivity;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.indonesia.IndonesiaIdFrontRecognizer;
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.DocumentUISettings;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanningActivity extends AppCompatActivity {

    //  Blink ID
    private MrtdRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;
    private IndonesiaIdFrontRecognizer mIndonesiaIdFrontRecognizer;
    private static int MY_REQUEST_CODE = 1;
    static String TAG = "Testing";

    @BindView(R.id.et_scanning1)
    EditText et_nik;
    @BindView(R.id.et_scanning2)
    EditText et_nama;
    @BindView(R.id.et_scanning3)
    EditText et_alamat;
    @BindView(R.id.et_scanning4)
    EditText et_no_hp;
    @BindView(R.id.spinner_tipe_kamar_CI)
    Spinner sp_tipe_kamar;
    @BindView(R.id.et_jumlah_kamar_CI)
    EditText et_nomor_kamar;

    private TampilDialog tampilDialog;
    //    SharedPreferences
    String token;
    SharedPreferences mSPLogin;
    ArrayList<Kamar> kamarArrayList;
    Kamar mKamar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        ButterKnife.bind(this);

        tampilDialog = new TampilDialog(this);

        // show loading
        tampilDialog.showLoading();

        initializeSP();

        // get data kamar
        getDataKamar();

        //        Blink ID
        MicroblinkSDK.setLicenseFile("MB_com.macbook.puritomat_BlinkID_Android_2018-12-16.mblic", this);
        // create MrtdRecognizer
        mRecognizer = new MrtdRecognizer();

        mIndonesiaIdFrontRecognizer = new IndonesiaIdFrontRecognizer();

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = new RecognizerBundle(mIndonesiaIdFrontRecognizer);
    }
    private void initializeSP() {
        mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token",null);
    }

    private void getDataKamar() {
        // getDataTipeKamar
        if (token != null){
            DataKamarService dataKamarService = APIClient.getClient().create(DataKamarService.class);
            dataKamarService.getListDataKamar("Bearer "+token).enqueue(new Callback<ArrayList<Kamar>>() {

                @Override
                public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                    tampilDialog.dismissLoading();
                    // get Data Kamar
                    kamarArrayList = new ArrayList<Kamar>();
                    kamarArrayList = response.body();
                    // initiate spinner
                    ArrayList<String> listNamaTipeKamar = new ArrayList<>();
                    for (Kamar item:kamarArrayList) {
                        listNamaTipeKamar.add(item.getTipeKamar().getNama()+" "+item.getNomor());
                    }
                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ScanningActivity.this, android.R.layout.simple_spinner_item,listNamaTipeKamar);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    sp_tipe_kamar.setAdapter(dataAdapter);


                    // Spinner click listener
                    sp_tipe_kamar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // On selecting a spinner item
                            mKamar = kamarArrayList.get(position);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }

    // Blink ID Start activity scanning
    public void startScanning() {
        // Settings for DocumentScanActivity Activity
        DocumentUISettings settings = new DocumentUISettings(mRecognizerBundle);

        // tweak settings as you wish

        // Start activity
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, settings);
    }

    @OnClick(R.id.btn_scanning)
    public void click(){
        startScanning();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == DocumentScanActivity.RESULT_OK && data != null) {
                // load the data into all recognizers bundled within your RecognizerBundle

                mRecognizerBundle.loadFromIntent(data);

                // now every recognizer object that was bundled within RecognizerBundle
                // has been updated with results obtained during scanning session

                // you can get the result by invoking getResult on recognizer
//                MrtdRecognizer.Result result = mRecognizer.getResult();
                IndonesiaIdFrontRecognizer.Result result = mIndonesiaIdFrontRecognizer.getResult();
                if (result.getResultState() == Recognizer.Result.State.Valid) {
                    // result is valid, you can use it however you wish
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                    et_nik.setText(result.getDocumentNumber());
                    et_nama.setText(result.getName());
                    et_alamat.setText(result.getAddress());

                }else {
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_5));
                }
            }
        }
    }

    @OnClick(R.id.btn_check_in)
    public void submit(){
        if (validasiInputan()){
            tampilDialog.showDialog(getString(R.string.dialog_title_failed), getString(R.string.dialog_message_4));
        }else {

            Log.i(TAG, "submit: ");
        Toast.makeText(this, "woi", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean validasiInputan(){
        if (et_nik.getText().toString().equals("") || et_nama.getText().toString().equals("") || et_alamat.getText().toString().equals("") ||
                et_no_hp.getText().toString().equals("") || et_nomor_kamar.getText().toString().equals("") || mKamar == null){
            return true;
        }else {
            return false;
        }
    }
}
