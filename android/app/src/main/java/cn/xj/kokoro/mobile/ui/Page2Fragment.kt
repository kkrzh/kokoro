package cn.xj.kokoro.mobile.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import cn.xj.kokoro.mobile.NativeLib
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.album.FileManage
import cn.xj.kokoro.mobile.album.selectorImage
import cn.xj.kokoro.mobile.base.BaseFragment
import cn.xj.kokoro.mobile.utils.setOnIntervalClickListener
import com.google.common.util.concurrent.ListenableFuture
import java.io.File

class Page2Fragment: BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_page2

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }
    var cameraProviderFuture:ListenableFuture<ProcessCameraProvider>? = null

    override fun init(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.textView).apply {
            text = NativeLib.INSTANT.helloWorld()
            setOnIntervalClickListener{
                requireActivity().selectorImage(1, false) {
                    val outputPath = FileManage.getPicturesFile("out.jpg")
                    Log.e(TAG, "init: ${outputPath.path}", )
                    NativeLib.INSTANT.showText(FileManage.getImageAbsolutePath(requireContext(),it[0].toUri())?:"error.jpg", outputPath.path)
                }
            }
        }
//        NativeLib.INSTANT.showCamera()



//        val previewView = view.findViewById<PreviewView>(R.id.preView)
//        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
//
//        cameraProviderFuture?.addListener(Runnable {
//            val cameraProvider = cameraProviderFuture?.get()
//            bindPreview(previewView,cameraProvider!!)
//        }, ContextCompat.getMainExecutor(requireContext()))


    }

    fun bindPreview(previewView:PreviewView,cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.getSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }

}