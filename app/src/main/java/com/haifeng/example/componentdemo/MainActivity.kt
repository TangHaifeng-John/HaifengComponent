package com.haifeng.example.componentdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.haifeng.example.base.ApiService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_login.setOnClickListener{



            ARouter.getInstance().build("/haifengdemo/showToast").navigation()



        }

        show_toast.setOnClickListener{
            ApiService.singleInstance.showToast("与Login模块通过接口通信成功")
    }


        test_jump_fragment.setOnClickListener {
            val  fragment:Fragment= ARouter.getInstance().build("/haifengdemo/test_fragment") .navigation() as Fragment


            val  bundle =Bundle()
            bundle.putInt("test",123)
            fragment.arguments=bundle

            supportFragmentManager.beginTransaction().replace(R.id.content,fragment).commitAllowingStateLoss()
        }

    }
}
