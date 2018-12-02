package com.macbook.puritomat.activity

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.macbook.puritomat.R
import com.macbook.puritomat.TampilDialog
import com.macbook.puritomat.api.APIClient
import com.macbook.puritomat.api.LoginService
import com.macbook.puritomat.model.RequestLogin
import com.macbook.puritomat.model.ResponseLogin
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    val TAG: String = "Testing"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener(this)
    }

    override fun onClick(v: View){
        when(v.id){
            R.id.btn_login -> {
                Log.i(TAG,"login: "+et_username_login.text.toString())
                if (validasiDataInput()){

                    TampilDialog(this@LoginActivity).showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_4))
                }else {
                    getToken()
                }

            }
        }
    }

    private fun getToken() {
//        val retrofit: Retrofit = APIClient.getClient().create(LoginService::class.java)
        val service = APIClient.getClient().create(LoginService::class.java)
        val gson = Gson()

//        val requestLogin:Map<String,String> = HashMap(hashMapOf("username" to et_username_login.text.toString(),"password" to et_password_login.text.toString()))
        val requestLogin:RequestLogin = RequestLogin(et_username_login.text.toString(),et_password_login.text.toString())
//        Log.i(TAG,gson.toJson(requestLogin))
        service.postLogin(requestLogin).enqueue(object : Callback<ResponseLogin>{
            override fun onFailure(call: Call<ResponseLogin>?, t: Throwable?) {
                TampilDialog(this@LoginActivity).showDialog(getString(R.string.dialog_title_failed),t.toString())
            }

            override fun onResponse(call: Call<ResponseLogin>?, response: Response<ResponseLogin>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        val responseLogin: ResponseLogin = response.body()
                        val mSPLogin:SharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
                        val editor = mSPLogin.edit()

                        editor.putString("token", responseLogin.token)
                        editor.commit()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        TampilDialog(this@LoginActivity).showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_6))
                    }
                }
            }

        })
    }

    fun validasiDataInput() : Boolean{
        if (et_username_login.text.toString() == "" || et_password_login.text.toString() == "") {
            return true
        }else {
            return false
        }
    }
}
