package org.sopt24.dshyun0226.androidseminar.Activity

import android.content.Intent
import android.opengl.ETC1.isValid
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.sopt24.dshyun0226.androidseminar.Activity.SignupActivity
import org.sopt24.dshyun0226.androidseminar.DB.SharedPreferenceController
import org.sopt24.dshyun0226.androidseminar.Network.ApplicationController
import org.sopt24.dshyun0226.androidseminar.Network.NetworkService
import org.sopt24.dshyun0226.androidseminar.Network.POST.PostLoginResponse
import org.sopt24.dshyun0226.androidseminar.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val REQUEST_CODE_LOGIN_ACTIVITY = 1000

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        edtLoginID.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) v.setBackgroundResource(R.drawable.primary_border)
            else v.setBackgroundResource(R.drawable.gray_border)
        }

        edtLoginPW.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) v.setBackgroundResource(R.drawable.primary_border)
            else v.setBackgroundResource(R.drawable.gray_border)
        }

        btnLoginSubmit.setOnClickListener {
            val login_u_id = edtLoginID.text.toString()
            val login_u_pw: String = edtLoginPW.text.toString()
            if(isValid(login_u_id,login_u_pw)) postLoginResponse(login_u_id,login_u_pw)
        }

        txtLoginSignup.setOnClickListener{
            val intent: Intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun isValid(u_id:String, u_pw:String):Boolean{
        if(u_id == "") edtLoginID.requestFocus()
        else if(u_pw == "") edtLoginPW.requestFocus()
        else return true
        return false
    }

    fun postLoginResponse(u_id: String, u_pw: String){
        var jsonObject = JSONObject()
        jsonObject.put("id", u_id)
        jsonObject.put("password", u_pw)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val postLoginResponse: Call<PostLoginResponse> =
        networkService.postLoginResponse("application/json", gsonObject)
        postLoginResponse.enqueue(object :Callback<PostLoginResponse>{
            override fun onFailure(call: Call<PostLoginResponse>, t:Throwable){
                Log.e("Login failed", t.toString())
            }

            override fun onResponse(call:Call<PostLoginResponse>, response: Response<PostLoginResponse>){
                if(response.isSuccessful){
                    if(response.body()!!.status == 201){

                        // Request Login
                        SharedPreferenceController.setUserToken(applicationContext, response.body()!!.data!!)
                        finish()
                    }
                }
            }
        })
    }
}























