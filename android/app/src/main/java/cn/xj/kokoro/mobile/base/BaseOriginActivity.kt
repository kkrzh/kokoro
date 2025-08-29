package cn.xj.kokoro.mobile.base

import android.app.Instrumentation
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import cn.xj.kokoro.mobile.view.LoadingWindow

/**
 * Created by xianjie on 2023年1月6日16:13:42
 *
 * Description:
 */
abstract class BaseOriginActivity(val useBinding:Boolean): AppCompatActivity() {
    protected val TAG by lazy {
        if (this::class.java.canonicalName!=null){
            val className: List<String> = this::class.java.canonicalName!!.split(".")
            className[className.size-1]
        }else "TAG"
    }

    //加载窗口
    lateinit var loadingWindow: LoadingWindow

    //Activity启动器
    private var mStartActivityLauncher: ActivityResultLauncher<Intent>? = null
    //使用启动器跳转Activity
    fun startActivityForResult(intent: Intent, options: ActivityOptionsCompat? = null, result:(ActivityResult.()->Unit)){
        if (mStartActivityLauncher!=null){
            startActivityResult = result
            mStartActivityLauncher!!.launch(intent,options)
        }else throw RuntimeException("未初始化启动器-Activity")
    }
    private var startActivityResult:(ActivityResult.()->Unit)? = null

    //权限请求启动器
    private var mRequestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null
    //有任一一项权限未请求即走false
     fun requestPermission(permissionList:Array<String>,result:((Boolean)->Unit)){
        if (mRequestPermissionLauncher!=null){
            requestPermissionResult = result
            mRequestPermissionLauncher!!.launch(permissionList)
        }else throw RuntimeException("未初始化启动器-Permission")
    }

    private var requestPermissionResult:((Boolean)->Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView()
        loadingWindow =  LoadingWindow(this)
        mStartActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            startActivityResult?.invoke(it)
        }
        mRequestPermissionLauncher =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val requestPermissionList = it.keys
            var requestSuccess = true
            Log.i(TAG, "RequestPermission: ${it}", )
            for (i in requestPermissionList){
                if (!it[i]!!) {
                    requestSuccess = false
                }
            }
            requestPermissionResult?.invoke(requestSuccess)
        }

    }
    abstract var setContentView:(()->Unit)
    override fun onBackPressed() {
        if (!loadingWindow.isShowing) finishAfterTransition()
        else{
            loadingWindow.hindWindow {
                finishAfterTransition()
            }
        }
    }

    //解决Android10以上动画异常问题
    // 屏幕息屏后解锁，按下Home键返回来，打开了其他页面，其他app显示在了我的应用上边，调用finishAfterTransition()，它就没有动画了
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        if (outState.containsKey("InstrumentationFixBug") && outState.getBoolean("InstrumentationFixBug")) {
//        } else {
//            //ActivityThread调用，用于保存逻辑
//        }
    }

    override fun onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !isFinishing) {
            val bundle = Bundle()
            bundle.putBoolean("InstrumentationFixBug", true)
            Instrumentation().callActivityOnSaveInstanceState(this, bundle)
        }
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingWindow.clearWindow()
        mStartActivityLauncher = null
        mRequestPermissionLauncher = null
    }

    //处理触摸事件的分发 是从dispatchTouchEvent开始的
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            //返回具有焦点的当前视图
            val v: View? = currentFocus
            if (isShouldHideInput(v, ev)) {
//                if (hideSoftInputFromWindow(v!!.windowToken)) {
//                    v.clearFocus()
//                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v != null) {
            if (v is EditText) {
                //命名一个元素为2个的整数数组
                val leftTop = intArrayOf(0, 0)
                //返回两个整数值,分别为X和Y,此X和Y表示此视图,在其屏幕中的坐标(以左上角为原点的坐标)
                v.getLocationInWindow(leftTop)
                val left = leftTop[0]
                val top = leftTop[1]
                val bottom: Int = top + v.getHeight()
                val right: Int = left + v.getWidth()
                return !(ev.x > left && ev.x < right && ev.y > top && ev.y < bottom)
            }
        }
        return false
    }
}