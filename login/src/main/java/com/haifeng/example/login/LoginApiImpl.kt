package com.haifeng.example.login

import android.widget.Toast
import com.haifeng.example.base.LoginApi

class LoginApiImpl :LoginApi {
    override fun showToast(toast: String) {
        Toast.makeText(LoginApp.app,toast,Toast.LENGTH_SHORT).show()
    }


}