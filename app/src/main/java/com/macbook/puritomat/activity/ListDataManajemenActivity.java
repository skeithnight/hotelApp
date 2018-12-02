package com.macbook.puritomat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.macbook.puritomat.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListDataManajemenActivity extends AppCompatActivity {
    String menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_manajemen);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            menu= null;
        } else {
            menu= extras.getString("menu");
            if (menu.equals(getString(R.string.manajemen_1))){
                setTitle(R.string.title_data_kamar);
            }else if (menu.equals(getString(R.string.manajemen_2))){
                setTitle(R.string.title_data_menu);
            }else if (menu.equals(getString(R.string.manajemen_3))){
                setTitle(R.string.title_data_laundry);
            }else if (menu.equals(getString(R.string.manajemen_4))){
                setTitle(R.string.title_data_others);
            }
        }
    }

    @OnClick(R.id.fab_listDataManajemen)
    public void onClickFAB(View view){
        Intent intent = new Intent(this, DetailDataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("menu",menu);
        intent.putExtra("typeDetail",getString(R.string.tambah_data));
        startActivity(intent);
//        Toast.makeText(this, String.valueOf(view.getId())+" : "+menu, Toast.LENGTH_SHORT).show();
    }
}
