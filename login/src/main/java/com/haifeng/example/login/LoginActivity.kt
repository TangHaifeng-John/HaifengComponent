package com.haifeng.example.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.haifeng.example.base.ApiService
import kotlinx.android.synthetic.main.login_activity_login.*


@Route(path = "/haifengdemo/showToast")
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_login)
        login.setOnClickListener{
        }
    }
}
