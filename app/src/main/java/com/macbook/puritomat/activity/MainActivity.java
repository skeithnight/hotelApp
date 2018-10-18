package com.macbook.puritomat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.appus.splash.Splash;
import com.macbook.puritomat.R;

public class MainActivity extends AppCompatActivity {

//    SharedPreferences
    SharedPreferences mSPLogin;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initializeSP();

        startSplashScreen();

    }

    private void initializeSP() {
         mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = mSPLogin.edit();
         editor.clear();
    }

    //    to start splash screen
    public void startSplashScreen(){
        Splash.Builder splash = new Splash.Builder(MainActivity.this, getSupportActionBar());
        splash.setOneShotStart(true);
        splash.perform();
        CheckLogin();

//        after splash execute routing login
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                CheckLogin();
            }
        }, 3000);

    }

    private void CheckLogin() {
        String token = mSPLogin.getString("token",null);
        Log.i("testing", "CheckLogin: "+ token);
        if (token == null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
