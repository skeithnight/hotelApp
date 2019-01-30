package com.macbook.puritomat.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.macbook.puritomat.R;
import com.macbook.puritomat.Secret;
import com.macbook.puritomat.TampilDialog;
import com.macbook.puritomat.model.Transaksi;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckOutActivity extends AppCompatActivity implements TransactionFinishedCallback {

    Transaksi transaksi;
    @BindView(R.id.et_nama_Checkout)
    EditText etNama;
    @BindView(R.id.et_NIK_Checkout)
    EditText etNIK;
    @BindView(R.id.et_alamat_Checkout)
    EditText etAlamat;
    @BindView(R.id.et_nohp_Checkout)
    EditText etNoHp;

    TampilDialog tampilDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ButterKnife.bind(this);

        tampilDialog = new TampilDialog(this);

        // get extra data from list pesanan
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            transaksi= null;
        } else {
            Gson gson = new Gson();
            transaksi= gson.fromJson(extras.getString("transaksi"),Transaksi.class);
        }

//        Log.i("Testing", "onCreate: "+transaksi.getId());
        setData();
        // SDK initiation for UIflow
        initMidtransSdk();

    }

    private void setData() {
        String nik = transaksi.getTamu().getNik() == null?"null":transaksi.getTamu().getNik();
        String nama =transaksi.getTamu().getNama() == null?"null":transaksi.getTamu().getNama();
        String alamat =transaksi.getTamu().getAlamat() == null?"null":transaksi.getTamu().getAlamat();
        String noHP =transaksi.getTamu().getNomorHandphone() == null?"null":transaksi.getTamu().getNomorHandphone();
        etNIK.setText(nik);
        etNama.setText(nama);
        etAlamat.setText(alamat);
        etNoHp.setText(noHP);
    }

    private void initMidtransSdk() {
        String client_key = "SB-Mid-client-po-woWP8f3HfKLHh";
        String base_url = Secret.url;

        SdkUIFlowBuilder.init()
                .setClientKey(client_key) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(base_url) //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .buildSDK();
    }

    @OnClick(R.id.btn_check_out)
    public void Submit() {
        checkOutDialog();
    }

    private void OnlinePayment(){
//        MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
//        MidtransSDK.getInstance().startPaymentUiFlow(CheckOutActivity.this);
        MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
        MidtransSDK.getInstance().UiCardRegistration(CheckOutActivity.this, new CardRegistrationCallback() {
            @Override
            public void onSuccess(CardRegistrationResponse cardRegistrationResponse) {
                Toast.makeText(CheckOutActivity.this, "register card token success", Toast.LENGTH_SHORT).show();
                MidtransSDK.getInstance().startPaymentUiFlow(CheckOutActivity.this);;
            }

            @Override
            public void onFailure(CardRegistrationResponse cardRegistrationResponse, String s) {
                Toast.makeText(CheckOutActivity.this, "register card token Failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(CheckOutActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TransactionRequest initTransactionRequest() {
        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new
                TransactionRequest(System.currentTimeMillis() + "", 20000);

        //set customer details
        transactionRequestNew.setCustomerDetails(initCustomerDetails());


        // set item details
        ItemDetails itemDetails = new ItemDetails("1", 20000, 1, "Trekking Shoes");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);


        // Create creditcard options for payment
        CreditCard creditCard = new CreditCard();

        creditCard.setSaveCard(false); // when using one/two click set to true and if normal set to  false

//        this methode deprecated use setAuthentication instead
//        creditCard.setSecure(true); // when using one click must be true, for normal and two click (optional)

        creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_3DS);

        // noted !! : channel migs is needed if bank type is BCA, BRI or MyBank
//        creditCard.setChannel(CreditCard.MIGS); //set channel migs
        creditCard.setBank(BankType.BCA); //set spesific acquiring bank

        transactionRequestNew.setCreditCard(creditCard);

        return transactionRequestNew;
    }

    private CustomerDetails initCustomerDetails() {

        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("085310102020");
        mCustomerDetails.setFirstName("dodo");
        mCustomerDetails.setEmail("dodo@mail.com");
        return mCustomerDetails;
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkOutDialog(){
        final String[] items = {"Cash","Online Payment","EDC"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose payment");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Toast.makeText(getApplicationContext(), "Cash", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        OnlinePayment();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "EDC", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
