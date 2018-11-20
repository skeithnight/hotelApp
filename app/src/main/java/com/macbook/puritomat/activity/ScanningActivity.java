package com.macbook.puritomat.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.macbook.puritomat.R;
import com.microblink.MicroblinkSDK;
import com.microblink.activity.DocumentScanActivity;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.indonesia.IndonesiaIdFrontRecognizer;
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.DocumentUISettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanningActivity extends AppCompatActivity {

    //  Blink ID
    private MrtdRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;
    private IndonesiaIdFrontRecognizer mIndonesiaIdFrontRecognizer;
    private static int MY_REQUEST_CODE = 1;

    @BindView(R.id.et_scanning1)
    TextView et_nama;
    @BindView(R.id.et_scanning2)
    TextView et_alamat;
    @BindView(R.id.et_scanning3)
    TextView et_ttl;
    @BindView(R.id.et_scanning4)
    TextView et_jk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        ButterKnife.bind(this);

        //        Blink ID
        MicroblinkSDK.setLicenseFile("MB_com.macbook.puritomat_BlinkID_Android_2018-12-16.mblic", this);
        // create MrtdRecognizer
        mRecognizer = new MrtdRecognizer();

        mIndonesiaIdFrontRecognizer = new IndonesiaIdFrontRecognizer();

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = new RecognizerBundle(mIndonesiaIdFrontRecognizer);
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
                    et_nama.setText(result.getName());
                    et_alamat.setText(result.getAddress());
                    et_ttl.setText(result.getPlaceOfBirth() + String.valueOf(result.getDateOfBirth()));
                    et_jk.setText(result.getSex());

                }else {
                    Toast.makeText(this, "Tidak Berhasil", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
