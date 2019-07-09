package com.haifeng.example.base

import android.app.Application

abstract  class BaseApp :Application(){
   abstract fun onCreateModuleApp(application: Application)
}