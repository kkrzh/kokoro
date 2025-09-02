package cn.xj.kokoro.mobile.ui.page3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.xj.kokoro.mobile.SCREEN_CAPTURE_ACTION
import cn.xj.kokoro.mobile.base.BaseActivity
import java.lang.ref.WeakReference


class CaptureProvider(context: BaseActivity) {
    private  var activityRef: WeakReference<BaseActivity>
    private var projectionManager: MediaProjectionManager
    private  var mediaProjection: MediaProjection? = null

    init {
        activityRef = WeakReference(context)
        projectionManager =  activityRef.get()?.getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    }
    private var mPermissionIntent: Intent? = null


    fun startCapture(){
        activityRef.get()?.let {
            // 1. 注册广播接收器
            val screenCaptureReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == SCREEN_CAPTURE_ACTION) {
                        // 2. 收到通知后启动屏幕捕获逻辑
                        startScreenProjection(AppCompatActivity.RESULT_OK, mPermissionIntent!!)
                        it.unregisterReceiver(this)
                    }
                }
            }
            val intentFilter = IntentFilter(SCREEN_CAPTURE_ACTION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.registerReceiver(screenCaptureReceiver, intentFilter, Context.RECEIVER_EXPORTED)
            }else{
                ContextCompat.registerReceiver(it, screenCaptureReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED)
            }
            it.startActivityForResult(projectionManager.createScreenCaptureIntent()){
                if (resultCode == AppCompatActivity.RESULT_OK){
                    if (data!=null){
                        mPermissionIntent = data
                        val serviceIntent = Intent(it, MediaProjectionService::class.java)
                        it.startService(serviceIntent)
                    }
                }
            }

        }
    }

    private fun startScreenProjection(resultCode: Int, data: Intent) {
        mediaProjection = projectionManager.getMediaProjection(resultCode, data)
        // 注册回调
        mediaProjection?.registerCallback(  object : MediaProjection.Callback() {
            override fun onStop() {
                super.onStop()
            }
        }, Handler(Looper.getMainLooper()))

        startFinish?.invoke()
    }

    var startFinish:(()->Unit)? = null


    fun createDisplay(surface:SurfaceView){
        DisplayUtils.getDisplayMetricsInfo { screenWidth, screenHeight, densityDpi ->
            // Create a VirtualDisplay to start capturing
            mediaProjection?.createVirtualDisplay(
                "ScreenCapture",
                screenWidth, screenHeight, densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                surface.holder.surface, null, null
            )
        }
    }

    fun createImageReader(imageReaderCallback:((Image)->Unit)){
        this.imageReaderCallback = imageReaderCallback
        DisplayUtils.getDisplayMetricsInfo { screenWidth, screenHeight, densityDpi ->
            val imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 2)

            mediaProjection?.createVirtualDisplay(
                "ScreenCapture", screenWidth, screenHeight, densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.surface, null, null
            )

            imageReader.setOnImageAvailableListener(ImageAvailableListener(imageReaderCallback), null)

        }
    }

    var imageReaderCallback:((Image)->Unit)? = null


    private class ImageAvailableListener(val callback:((Image)->Unit)?) : ImageReader.OnImageAvailableListener {
        override fun onImageAvailable(reader: ImageReader) {
            val image = reader.acquireLatestImage()
            if (image != null) {
                callback?.invoke(image)
                image.close()
            }
        }
    }



}