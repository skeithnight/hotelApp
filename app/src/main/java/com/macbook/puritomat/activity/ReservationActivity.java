package com.macbook.puritomat.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private int PLACE_PICKER_REQUEST = 1;
    private static int MY_REQUEST_CODE = 1;
    static String TAG = "Testing";

    @BindView(R.id.et_reservasi1)
    EditText et_nik;
    @BindView(R.id.et_reservasi2)
    EditText et_nama;
    @BindView(R.id.et_reservasi3)
    EditText et_alamat;
    @BindView(R.id.et_reservasi4)
    EditText et_no_hp;
    @BindView(R.id.tv_pilih_tanggal)
    TextView tv_pilih_tanggal;

    //    Exp list kamar
    ExpandableListAdapterKamar listAdapter;
    ExpandableListView expListView;
    ArrayList<TipeKamar> listDataHeader;
    HashMap<String, ArrayList<Kamar>> listDataChild;
    SparseBooleanArray selectedRows;

    private TampilDialog tampilDialog;
    //    SharedPreferences
    String token;
    SharedPreferences mSPLogin;
    Kamar mKamar = null;
    Long waktuReservasi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);

        tampilDialog = new TampilDialog(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        // show loading
        tampilDialog.showLoading();

        initializeSP();

        // get data kamar
        getDataTipeKamar();
    }

    @OnClick({R.id.btn_tanggal_inap,R.id.btn_reservasi})
    public void clickButton(View view){
        switch (view.getId()){
            case R.id.btn_reservasi:
                selectedRows = listAdapter.getSelectedIds();
                if (!validasiInputan()) {
                    tampilDialog.showDialog(getString(R.string.dialog_title_failed), getString(R.string.dialog_message_4));
                } else {
                    tampilDialog.showLoading();
                    insertTamu();
                }
            case R.id.btn_tanggal_inap:
                showDateDialog();
        }
    }

    private Boolean validasiInputan() {
        if (et_nik.getText().toString().equals("") || et_nama.getText().toString().equals("") || et_alamat.getText().toString().equals("") ||
                et_no_hp.getText().toString().equals("") || selectedRows.size() == 0 || waktuReservasi == null) {
            return false;
        } else {
            return true;
        }
    }

    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                waktuReservasi = newDate.getTimeInMillis();
                tv_pilih_tanggal.setVisibility(View.VISIBLE);
                tv_pilih_tanggal.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
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

//    insert transaksi
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
            DetailTransaksi<Kamar> detailTransaksi = new DetailTransaksi<>(kamar, waktuReservasi, 1);
            listDataKamar.add(detailTransaksi);
        }
        Transaksi transaksi = new Transaksi(new Resepsionis("5bdfa84c3cd4ff24aba3cf00"), body, listDataKamar, "reservasi");
        DataTransaksiService dataTransaksiService = APIClient.getClient().create(DataTransaksiService.class);
        dataTransaksiService.postTransaksi("Bearer " + token, transaksi).enqueue(new Callback<Transaksi>() {
            @Override
            public void onResponse(Call<Transaksi> call, Response<Transaksi> response) {
                if (response.isSuccessful()) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog(getString(R.string.dialog_title_success), getString(R.string.dialog_message_1));
                    Intent intent = new Intent(ReservationActivity.this, MainActivity.class);
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

}
