package cn.xj.kokoro.mobile.ui.page3

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.base.BaseActivity
import cn.xj.kokoro.mobile.base.BaseFragment
import cn.xj.kokoro.mobile.ui.page3.mlkit.BitmapUtils
import cn.xj.kokoro.mobile.ui.page3.mlkit.VisionImageProcessor
import cn.xj.kokoro.mobile.ui.page3.mlkit.kotlin.objectdetector.ObjectDetectorProcessor
import cn.xj.kokoro.mobile.utils.setOnIntervalClickListener
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import org.opencv.android.Utils

class Page3Fragment: BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_page3

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }
    private var imageProcessor: VisionImageProcessor? = null

    override fun init(view: View, savedInstanceState: Bundle?) {
        if (requireContext() is BaseActivity){
            val captureProvider = CaptureProvider(requireContext() as BaseActivity)
            captureProvider.startCapture()
            val objectDetectorOptions =
                ObjectDetectorOptions.Builder().setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                    .enableMultipleObjects()
                    .enableClassification().build()
            if (imageProcessor != null) {
                imageProcessor!!.stop()
            }
            imageProcessor = ObjectDetectorProcessor(requireContext(), objectDetectorOptions)
            view.findViewById<SurfaceView>(R.id.surface).apply {
                setOnIntervalClickListener {
//                    captureProvider.createDisplay(this)
                    captureProvider.createImageReader{
                        Log.e(TAG, "init: ${it != null}" )
                        val bitmap: Bitmap? = BitmapUtils.imageToBitmap(it)
                        if (bitmap != null){
                            Glide.with(requireContext()).load(bitmap).into(findViewById<ImageView>(R.id.showImage))

                        }
//                        imageProcessor!!.processBitmap(BitmapUtils.getBitmap(it))
//                        imageProcessor!!.processImageProxy(imageProxy, graphicOverlay)

                    }
                }
            }
        }
    }
}