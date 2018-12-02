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
import com.macbook.puritomat.TampilDialog;
import com.macbook.puritomat.api.APIClient;
import com.macbook.puritomat.api.LoginService;
import com.macbook.puritomat.fragment.HomeFragment;
import com.macbook.puritomat.fragment.ManajemenFragment;
import com.macbook.puritomat.fragment.TransaksiFragment;
import com.macbook.puritomat.model.Resepsionis;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    static String TAG = "Testing";
    private TampilDialog tampilDialog;
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

        tampilDialog = new TampilDialog(this);

        initializeSP();

//        load home fragment
        loadFragment(new HomeFragment());

//        starting splash screen
        startSplashScreen();

//        Check Login
        CheckLogin();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    private void initializeSP() {
         mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
    }

    //    to start splash screen
    public void startSplashScreen(){
        Splash.Builder splash = new Splash.Builder(MainActivity.this, getSupportActionBar());
        splash.setBackgroundColor(getResources().getColor(R.color.Aquamarine));
        splash.setSplashImage(getDrawable(R.drawable.logo));
        splash.setOneShotStart(true);
        splash.perform();
    }

//    ini function untuk check apakah token user sudah expired atau belum.
    private void CheckLogin() {
        tampilDialog.showLoading();
        String token = mSPLogin.getString("token",null);
//        Log.i(TAG, "CheckLogin: "+ token);
        if (token == null){
            tampilDialog.dismissLoading();
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else {
            LoginService loginService = APIClient.getClient().create(LoginService.class);
            loginService.getCheckLogin("Bearer "+token).enqueue(new Callback<Resepsionis>() {
                @Override
                public void onResponse(Call<Resepsionis> call, Response<Resepsionis> response) {
                    tampilDialog.dismissLoading();
                    if (!response.isSuccessful()){
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Resepsionis> call, Throwable t) {
                    Log.i(TAG, "onFailure: "+t.getMessage());
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
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
