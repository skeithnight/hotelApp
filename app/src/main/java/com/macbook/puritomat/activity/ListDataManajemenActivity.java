package com.macbook.puritomat.activity;

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
            if (menu == "kamar"){
                setTitle(R.string.title_data_kamar);
            }else if (menu == "menu"){
                setTitle(R.string.title_data_menu);
            }else if (menu == "laundry"){
                setTitle(R.string.title_data_laundry);
            }else if (menu == "others"){
                setTitle(R.string.title_data_others);
            }
        }
    }

    @OnClick(R.id.fab_listDataManajemen)
    public void onClickFAB(View view){
        Toast.makeText(this, String.valueOf(view.getId())+" : "+menu, Toast.LENGTH_SHORT).show();
    }
}
