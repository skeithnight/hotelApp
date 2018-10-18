package com.macbook.puritomat.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.macbook.puritomat.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    fun submit(){
        val mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val editor = mSPLogin.edit()

        editor.putString(et_username_login.text.toString(), "userKos")
        editor.putString(et_password_login.text.toString(), "pasKos")
        editor.putString("token", "tokenKos")
        editor.apply()

        val intent = Intent(this@LoginActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}
