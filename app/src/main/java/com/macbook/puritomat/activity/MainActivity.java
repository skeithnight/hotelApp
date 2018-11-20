package com.macbook.puritomat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appus.splash.Splash;
import com.macbook.puritomat.R;
import com.macbook.puritomat.fragment.HomeFragment;
import com.macbook.puritomat.fragment.ManajemenFragment;
import com.macbook.puritomat.fragment.TransaksiFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

//    SharedPreferences
    SharedPreferences mSPLogin;


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                fragment = new HomeFragment();
                break;
            case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_manajemen);
                fragment = new ManajemenFragment();
                break;
            case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_transaksi);
                fragment = new TransaksiFragment();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeSP();

//        load home fragment
        loadFragment(new HomeFragment());

//        starting splash screen
        startSplashScreen();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    private void initializeSP() {
         mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = mSPLogin.edit();
         editor.clear();
    }

    //    to start splash screen
    public void startSplashScreen(){
        Splash.Builder splash = new Splash.Builder(MainActivity.this, getSupportActionBar());
        splash.setBackgroundColor(getResources().getColor(R.color.Aquamarine));
        splash.setSplashImage(getDrawable(R.drawable.logo));
        splash.setOneShotStart(true);
        splash.perform();
//        CheckLogin();

//        after splash execute routing login
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                CheckLogin();
            }
        }, 3000);

    }

//    ini function untuk check apakah token user sudah expired atau belum.
    private void CheckLogin() {
        String token = mSPLogin.getString("token",null);
        Log.i("testing", "CheckLogin: "+ token);
        if (token == null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

//    ini function untuk meload fragment
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @OnClick({R.id.menu_item_check_in,R.id.menu_item_reservasi})
    public void floatingClick(View view){
        switch (view.getId()) {
            case R.id.menu_item_check_in:
                // do something
                Intent intent = new Intent(this,ScanningActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_reservasi:
                // do something else
                Toast.makeText(view.getContext(), "2", Toast.LENGTH_SHORT).show();
                break;

        }
    }

}
