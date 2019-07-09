package com.haifeng.example.login

import android.app.Application
import com.haifeng.example.base.ApiService
import com.haifeng.example.base.BaseApp

class LoginApp : BaseApp() {

    companion object{
        var app: Application? = null
    }

    override fun onCreateModuleApp(application: Application) {
        app=application

        ApiService.singleInstance.loginApi = LoginApiImpl()
    }

    override fun onCreate() {
        super.onCreate()
        onCreateModuleApp(this)
    }


}