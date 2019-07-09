package com.haifeng.example.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route


@Route(path = "/haifengdemo/test_fragment")
class TestFragment :Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val  test: Int? = arguments?.getInt("test")
        return  layoutInflater.inflate(R.layout.login_fragment_test,null)


    }
}