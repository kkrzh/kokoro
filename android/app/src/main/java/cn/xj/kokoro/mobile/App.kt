package cn.xj.kokoro.mobile

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import cn.xj.kokoro.mobile.album.AlbumConfig
import cn.xj.kokoro.mobile.album.albumConfig

fun getApplicationContext(): Context = App.application.applicationContext


class App : Application() {
    companion object {
        lateinit var application: App
    }

    override fun onCreate() {
        super.onCreate()
        //应用Application初始化完成
        application = this

        albumConfig{
            folderName = "kokoro"
            fileProviderAuthorities = "cn.xj.kokoro.mobile.fileprovider"
        }

        hideAppWindow()
    }
    private fun hideAppWindow() {
        try {
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .appTasks[0].setExcludeFromRecents(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
//        if (isRelease){
//            initAcra {
//                buildConfigClass = BuildConfig::class.java
//                reportFormat = StringFormat.JSON
//                dialog {
//                    title = "程序崩溃了"
//                    text = "一个不可预知的错误导致程序停止正常运行，上传日志帮助我们改进它"
//                    positiveButtonText = "上传错误日志"
//                    negativeButtonText = ""
//                    resIcon = null
//                    resTheme = R.style.DialogTheme
//                }
//
//            }
//        }
//        startService(Intent(this, MainService::class.java))
    }



}