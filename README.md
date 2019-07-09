# Android组件化开发方案

[Demo Github地址](https://github.com/TangHaifeng-John/HaifengComponent)

Demo基础框架图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190709192748364.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1RFMjgwOTMxNjM=,size_16,color_FFFFFF,t_70)

##  背景:  为什么要组件化开发？

#####    1.  随着功能的增加代码越来越臃肿，到底项目调试越来越困难
#####    2.  修改某个模块的代码，导致整个APP都会受到影响
#####    3.  代码量大，编译速度也越来越慢

##  问题：进行组件化开发，需要解决哪些问题？

#####    1. 每个模块既可以单独运行又可以作为库项目被引入到主模块中
#####    2. 模块和模块之间代码和资源文件都需要隔离
#####    3. 代码隔离后的组件和组件之间的通信问题

##  方案 （以Login模块为例，其他模块类似）

## 问题1（每个模块既可以单独运行又可以作为库项目被引入到主模块中）的解决方案
###  隔离AndroidManifest.xml文件

在Login模块的目录下面新建一个gradle.properties文件
###	添加一个变量asApp

```java
#当为true时，表示作为一个单独的App运行，当为false时，表示作为一个库项目运行
asApp=false,
```
###	插件控制

```java
if(asApp.toBoolean()){
    apply plugin: 'com.android.application'
}else {
    apply plugin: 'com.android.library'
}
```

###	applicationId 控制
```
  if (asApp.toBoolean()){
            applicationId "com.haifeng.example.login"
        }
```

###  Application 隔离
#### 第一步，在公用模块创建Application基础类
```


public abstract class BaseApp extends Application {
    /**
     * Application 初始化
     */
    public abstract void onCreateModuleApp(Application application);


}

```
#### 第二步，在模块中定义自己的Application继承BaseApp
```
class LoginApp :BaseApp(){

    override fun onCreate() {
        super.onCreate()
        onCreateModuleApp(this)
    }
    override fun onCreateModuleApp(application: Application?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
```
#### 第三步,在src/main/manifest/app/AndroidManifest.xml,注册LoginApp
```
    <application
            android:allowBackup="true"
            android:icon="@mipmap/ic_launcher"
            android:label="@string/app_name"
            android:roundIcon="@mipmap/ic_launcher_round"
            android:supportsRtl="true"
            android:name=".LoginApp"
            android:theme="@style/login_AppTheme">
        <activity android:name=".LoginActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
    </application>

```
#### 第四步主项目中创建MainApplication，初始化各个模块的Application
```
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
```

###	 隔离AndroidManifest.xml文件
按照图片的示例新建一个manifest目录，然后分别创建一个app目录和一个lib目录,然后在这两个目录里面分别存放 作为app运行的清单文件和作为lib运行的清单文件,如下图所示

![a](https://github.com/TangHaifeng-John/HaifengComponent/blob/master/resources/a.png)

```java
  sourceSets {
        main {
            if (asApp.toBoolean()) {
                manifest.srcFile 'src/main/manifest/app/AndroidManifest.xml'
            } else {
                manifest.srcFile 'src/main/manifest/lib/AndroidManifest.xml'
            }
        }
    }
```
###解决依赖问题
做完上面这些操作其实每个模块已经可以单独运行起来了，现在还需要解决一个问题，如果打包到主项目中，在gradle3.0的插件已经有实现了 runtimeOnly，在主项目的build.gradle的依赖中添加代码
```java
dependencies {

 runtimeOnly project(path: ':login')
}
```
runtimeOnly的意思是login这个模块在编译期间对app模块不可见，只在运行期间可见
完成了上面这些步骤
## 问题2（模块和模块之间代码和资源文件都需要隔离）

这个问题其实在问题1已经有解决，这里主要解决一下资源冲突的问题

在login模块中的build.gradle中添加配置

```xml

resourcePrefix "login_"
```

resourcePrefix表示约束，加上这个配置后，login模块的所有资源必须以login_开头

![image-20190709144301065](https://github.com/TangHaifeng-John/HaifengComponent/blob/master/resources/b.png)

如图，加上login_后编译成功，不加上则报错

##	问题3（怎么解决组件化通信问题）

组件化通信下面几个关键方式

- Activity通信

- Service通信

- 其他通信


关于Activity和Service通信方式可以参考[Arouter](https://github.com/alibaba/ARouter),关于Aronter如何使用可以看看里面的文档

其他通信怎么实现？目前我想到了下面几种方式

- 通过广播简历通信
- 通过[EventBus](https://github.com/greenrobot/EventBus)通信
- 通过接口方式创建通信

广播和EventBus大家应该都比较熟悉，接下来我们重点介绍一下接口方式是怎么通信的

###我们本次的例子是在主模块中调用Login模块的接口，显示一个Toast
####第一步，在Base模块中创建LoginApi接口

```kotlin
interface LoginApi{
    fun showToast(toast:String)
}
```

####第二步，在Base模块创建一个基础服务ApiService

```kotlin
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
```
ApiService 是一个单例，提供模块间基础服务的通信，所有的模块服务都需要在ApiService注册

####第三步，在Login模块中实现LoginApi
```kotlin
class LoginApiImpl :LoginApi {
    override fun showToast(toast: String) {
        Toast.makeText(LoginApp.app,toast,Toast.LENGTH_SHORT).show()
    }


}
```

####第四步，注册Login模块服务
```kotlin
    override fun onCreateModuleApp(application: Application) {
        app=application

        ApiService.singleInstance.loginApi = LoginApiImpl()
    }
```
