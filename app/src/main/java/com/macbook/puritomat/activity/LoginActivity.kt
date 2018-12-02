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
//                    val mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE)
//                    val editor = mSPLogin.edit()
//
//                    editor.putString(et_username_login.text.toString(), "userKos")
//                    editor.putString(et_password_login.text.toString(), "pasKos")
//                    editor.putString("token", "tokenKos")
//                    editor.commit()
//
//                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                    startActivity(intent)
                }

            }
        }
    }

    private fun getToken() {
        val retrofit: Retrofit = APIClient.getClient()
        val service: LoginService = retrofit.create(LoginService::class.java)
        val gson = Gson()

        val requestLogin:Map<String,String> = HashMap(hashMapOf("username" to et_username_login.text.toString(),"password" to et_password_login.text.toString()))
//        Log.i(TAG,gson.toJson(requestLogin))
        service.postKontak(requestLogin).enqueue(object : Callback<Map<String,String>>{
            override fun onResponse(call: Call<Map<String, String>>?, response: Response<Map<String, String>>?) {

                Log.i(TAG,gson.toJson(response))
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    Log.i(TAG,gson.toJson(response))
//                }else{
//                    TampilDialog(this@LoginActivity).showDialog(getString(R.string.dialog_title_failed),getString(R.string.dialog_message_6))
//                }
            }

            override fun onFailure(call: Call<Map<String, String>>?, t: Throwable?) {
                TampilDialog(this@LoginActivity).showDialog(getString(R.string.dialog_title_failed),t.toString())
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
