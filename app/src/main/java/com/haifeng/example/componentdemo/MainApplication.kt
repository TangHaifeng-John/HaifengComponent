package com.haifeng.example.componentdemo

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.haifeng.example.base.AppConfig
import com.haifeng.example.base.BaseApp

class  MainApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        ARouter.openLog()     // 打印日志
        ARouter.openDebug()  // 开启调
        ARouter.init(this)
        initModuleApp()
    }


    /**
     * 初始化组件APP
     */
    fun initModuleApp() {
        for (moduleApp in AppConfig.moduleApps) {
            try {
                val clazz = Class.forName(moduleApp)
                val baseApp = clazz.newInstance() as BaseApp
                baseApp.onCreateModuleApp(this)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }

        }
    }
}
