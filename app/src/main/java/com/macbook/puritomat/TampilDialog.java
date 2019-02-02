package com.macbook.puritomat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.macbook.puritomat.activity.DetailDataActivity;
import com.macbook.puritomat.activity.ListDataManajemenActivity;
import com.macbook.puritomat.api.APIClient;
import com.macbook.puritomat.api.DataLaundryService;
import com.macbook.puritomat.api.DataMenuService;
import com.macbook.puritomat.api.DataOthersService;
import com.macbook.puritomat.model.DataLaundry;
import com.macbook.puritomat.model.DataMenu;
import com.macbook.puritomat.model.DataOthers;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TampilDialog {
    Context mcontext;
    ProgressDialog progressDialog;

    public TampilDialog(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void showDialog(String title, String message){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mcontext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mcontext);
        }
        builder.setTitle(title);
        builder.setMessage(message);
        if (title.equals("Information")) {
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                }
            })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info);
        }else if (title.equals("Failed")){
            builder.setIcon(android.R.drawable.ic_dialog_alert);
        }else {
            builder.setIcon(R.drawable.ic_check_circle_black_24dp);
        }
        builder.show();
    }

    public void showLoading(){
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("Its loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void dismissLoading(){
        progressDialog.dismiss();
    }

    public void showValidate(String title, String message, final String id, final String level){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mcontext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mcontext);
        }
        builder.setTitle(title);
        builder.setMessage(message);
        if (title.equals("Information")) {
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                    hapusData(id,level);
                }
            })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info);
        }else if (title.equals("Failed")){
            builder.setIcon(android.R.drawable.ic_dialog_alert);
        }else {
            builder.setIcon(R.drawable.ic_check_circle_black_24dp);
        }
        builder.show();
    }

    public void hapusData(String id,String level){
        //    SharedPreferences
        String token;
        SharedPreferences mSPLogin;

        mSPLogin = mcontext.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token",null);

        if (level.equals(mcontext.getString(R.string.manajemen_2))){
            DataMenuService dataMenuService = APIClient.getClient().create(DataMenuService.class);
            dataMenuService.deleteDataMenu("Bearer "+token,id).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        showDialog(mcontext.getString(R.string.dialog_title_success), mcontext.getString(R.string.dialog_message_1));
                        Intent intent = new Intent(mcontext, ListDataManajemenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("menu",mcontext.getString(R.string.manajemen_2));
                        mcontext.startActivity(intent);
                    }else {
                        showDialog(mcontext.getString(R.string.dialog_title_failed),mcontext.getString(R.string.dialog_message_2));
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showDialog(mcontext.getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }else if (level.equals(mcontext.getString(R.string.manajemen_3))){
            DataLaundryService dataLaundryService = APIClient.getClient().create(DataLaundryService.class);
            dataLaundryService.deleteDataLaundry("Bearer "+token,id).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        showDialog(mcontext.getString(R.string.dialog_title_success), mcontext.getString(R.string.dialog_message_1));
                        Intent intent = new Intent(mcontext, ListDataManajemenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("menu",mcontext.getString(R.string.manajemen_3));
                        mcontext.startActivity(intent);
                    }else {
                        showDialog(mcontext.getString(R.string.dialog_title_failed),mcontext.getString(R.string.dialog_message_2));
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showDialog(mcontext.getString(R.string.dialog_title_failed),t.getMessage());
                }
            });

        }else if (level.equals(mcontext.getString(R.string.manajemen_4))){
            DataOthersService dataOthersService = APIClient.getClient().create(DataOthersService.class);
            dataOthersService.deleteDataOthers("Bearer "+token,id).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        showDialog(mcontext.getString(R.string.dialog_title_success), mcontext.getString(R.string.dialog_message_1));
                        Intent intent = new Intent(mcontext, ListDataManajemenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("menu",mcontext.getString(R.string.manajemen_4));
                        mcontext.startActivity(intent);
                    }else {
                        showDialog(mcontext.getString(R.string.dialog_title_failed),mcontext.getString(R.string.dialog_message_2));
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showDialog(mcontext.getString(R.string.dialog_title_failed),t.getMessage());
                }
            });
        }
    }
}
