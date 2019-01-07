package com.macbook.puritomat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.macbook.puritomat.R;
import com.macbook.puritomat.TampilDialog;
import com.macbook.puritomat.adapter.ExpandableListAdapterKamar;
import com.macbook.puritomat.api.APIClient;
import com.macbook.puritomat.api.DataKamarService;
import com.macbook.puritomat.api.DataTransaksiService;
import com.macbook.puritomat.model.DetailTransaksi;
import com.macbook.puritomat.model.Kamar;
import com.macbook.puritomat.model.Resepsionis;
import com.macbook.puritomat.model.Tamu;
import com.macbook.puritomat.model.TipeKamar;
import com.macbook.puritomat.model.Transaksi;
import com.microblink.MicroblinkSDK;
import com.microblink.activity.DocumentScanActivity;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.indonesia.IndonesiaIdFrontRecognizer;
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.DocumentUISettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
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

    //    Exp list kamar
    ExpandableListAdapterKamar listAdapter;
    ExpandableListView expListView;
    ArrayList<TipeKamar> listDataHeader;
    HashMap<String, ArrayList<Kamar>> listDataChild;

    private TampilDialog tampilDialog;
    //    SharedPreferences
    String token;
    SharedPreferences mSPLogin;
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
        getDataTipeKamar();

        //        Blink ID
        MicroblinkSDK.setLicenseFile("MB_com.macbook.puritomat_BlinkID_Android_2019-02-03.mblic", this);
        // create MrtdRecognizer
        mRecognizer = new MrtdRecognizer();

        mIndonesiaIdFrontRecognizer = new IndonesiaIdFrontRecognizer();

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = new RecognizerBundle(mIndonesiaIdFrontRecognizer);
    }

    private void initializeSP() {
        mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token", null);
    }

    private void getDataTipeKamar() {
        if (token != null) {
            DataKamarService dataKamarService = APIClient.getClient().create(DataKamarService.class);
            dataKamarService.getListDataTipeKamar("Bearer " + token).enqueue(new Callback<ArrayList<TipeKamar>>() {

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
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed), t.getMessage());
                }
            });
        }
    }

    private void getDataKamar() {
        if (token != null) {
            DataKamarService dataKamarService = APIClient.getClient().create(DataKamarService.class);
            dataKamarService.getListDataKamar("Bearer " + token).enqueue(new Callback<ArrayList<Kamar>>() {
                @Override
                public void onResponse(Call<ArrayList<Kamar>> call, Response<ArrayList<Kamar>> response) {
                    tampilDialog.dismissLoading();
                    showListDataExp(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<Kamar>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed), t.getMessage());
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
    public void click() {
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

                } else {
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed), getString(R.string.dialog_message_5));
                }
            }
        }
    }

    SparseBooleanArray selectedRows;

    @OnClick(R.id.btn_check_in)
    public void submit() {
        selectedRows = listAdapter.getSelectedIds();
        if (!validasiInputan()) {
            tampilDialog.showDialog(getString(R.string.dialog_title_failed), getString(R.string.dialog_message_4));
        } else {
            insertTamu();
        }
    }

    private void insertTamu() {
        Tamu tamu = new Tamu(et_nik.getText().toString(), et_nama.getText().toString(), et_alamat.getText().toString(), et_no_hp.getText().toString());
        DataTransaksiService dataTransaksiService = APIClient.getClient().create(DataTransaksiService.class);
        dataTransaksiService.postTamu("Bearer " + token, tamu).enqueue(new Callback<Tamu>() {
            @Override
            public void onResponse(Call<Tamu> call, Response<Tamu> response) {
                if (response.isSuccessful()) {
                    insertTransaksi(response.body());
                } else {
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed), getString(R.string.dialog_message_2));
                }

            }

            @Override
            public void onFailure(Call<Tamu> call, Throwable t) {
                tampilDialog.showDialog(getString(R.string.dialog_title_failed), t.getMessage());
            }
        });
    }

    private void insertTransaksi(Tamu body) {
        List<DetailTransaksi<Kamar>> listDataKamar = new ArrayList<>();
        for (int i = 0; i < selectedRows.size(); i++) {
            String a = String.valueOf(selectedRows.keyAt(i));
            int groupPosition = Integer.parseInt(a.substring(1, 2));
            int childPosition = Integer.parseInt(a.substring(2, 3));
            Kamar kamar = listDataChild.get(listDataHeader.get(groupPosition).getId())
                    .get(childPosition);
            DetailTransaksi<Kamar> detailTransaksi = new DetailTransaksi<>(kamar, System.currentTimeMillis(), 1);
            listDataKamar.add(detailTransaksi);
        }
        Transaksi transaksi = new Transaksi(new Resepsionis("5bdfa84c3cd4ff24aba3cf00"), body, listDataKamar, "check-in");
        DataTransaksiService dataTransaksiService = APIClient.getClient().create(DataTransaksiService.class);
        dataTransaksiService.postTransaksi("Bearer " + token, transaksi).enqueue(new Callback<Transaksi>() {
            @Override
            public void onResponse(Call<Transaksi> call, Response<Transaksi> response) {
                if (response.isSuccessful()) {
                    tampilDialog.showDialog(getString(R.string.dialog_title_success), getString(R.string.dialog_message_1));
                    Intent intent = new Intent(ScanningActivity.this, ListDataManajemenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("menu",getString(R.string.manajemen_1));
                    startActivity(intent);
                } else {
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed), getString(R.string.dialog_message_2));
                }

            }

            @Override
            public void onFailure(Call<Transaksi> call, Throwable t) {
                tampilDialog.showDialog(getString(R.string.dialog_title_failed), t.getMessage());
            }
        });
    }


    private Boolean validasiInputan() {
        if (et_nik.getText().toString().equals("") || et_nama.getText().toString().equals("") || et_alamat.getText().toString().equals("") ||
                et_no_hp.getText().toString().equals("") || selectedRows.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void showListDataExp(ArrayList<Kamar> listDataKamar) {
//        Preparing data

        listDataChild = new HashMap<String, ArrayList<Kamar>>();

        for (TipeKamar tipekamar : listDataHeader) {
            ArrayList<Kamar> kamarList = new ArrayList<Kamar>();
            for (Kamar kamar : listDataKamar) {
                if (kamar.getTipeKamar().getId().equals(tipekamar.getId())) {
                    kamarList.add(kamar);
                    listDataChild.put(tipekamar.getId(), kamarList);
                }
            }
        }
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.exp_list_data_tipe_kamar);
        expListView.setVisibility(View.VISIBLE);
        listAdapter = new ExpandableListAdapterKamar(this, listDataHeader, listDataChild, "regis");

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }
}
