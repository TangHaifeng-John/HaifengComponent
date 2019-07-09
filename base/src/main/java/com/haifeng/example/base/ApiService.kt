package com.haifeng.example.base


class ApiService private  constructor(){
     var loginApi:LoginApi?=  null
        companion object{
            val  singleInstance:ApiService by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){
                ApiService()
            }
        }



    fun  showToast(toast:String){

        loginApi?.showToast(toast)

    }
}