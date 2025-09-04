package cn.xj.kokoro.mobile.ui.page2

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import cn.xj.kokoro.mobile.NativeLib
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.album.BitmapUtils
import cn.xj.kokoro.mobile.album.FileManage
import cn.xj.kokoro.mobile.album.FileUtils
import cn.xj.kokoro.mobile.album.selectorImage
import cn.xj.kokoro.mobile.base.BaseFragment
import cn.xj.kokoro.mobile.utils.setOnIntervalClickListener
import com.bumptech.glide.Glide
import com.google.common.util.concurrent.ListenableFuture
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class Page2Child1Fragment: BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_page2_child1

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }
    var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    override fun init(view: View, savedInstanceState: Bundle?) {
        val inputImageView = view.findViewById<ImageView>(R.id.inputImageView)
        val outputImageView = view.findViewById<ImageView>(R.id.outputImageView)
        val output2ImageView = view.findViewById<ImageView>(R.id.output2ImageView)

        //输入Mat
        val inputMat: Mat? = Mat()
        //灰度输出Mat
        val outputGRAYMat: Mat? = Mat()
        //位图输出Mat
        val outputBMPMat: Mat? = Mat()

        //输入ContentUri
        var inputContentUri: Uri? = null

        //输入Bitmap
        var inputBitmap: Bitmap? = null
        //灰度输出Bitmap
        var outputGRAYBitmap: Bitmap? = null
        //位图输出Bitmap
        var outputBMPBitmap: Bitmap? = null


        view.findViewById<TextView>(R.id.textView).apply {
            text = NativeLib.Companion.INSTANT.helloWorld()
            setOnIntervalClickListener {
                requireActivity().selectorImage(1, false) {
                    inputContentUri = it[0].toUri()
                    inputBitmap = BitmapUtils.getBitmapFromContentUri(
                        requireContext().contentResolver,
                        inputContentUri
                    )
                    Glide.with(requireContext()).load(inputBitmap).into(inputImageView)
                    Utils.bitmapToMat(inputBitmap, inputMat)

                    //灰度
                    outputGRAYBitmap = createBitmap(inputBitmap!!.width, inputBitmap!!.height)
                    Utils.bitmapToMat(outputGRAYBitmap, outputGRAYMat)
                    Imgproc.cvtColor(inputMat, outputGRAYMat, Imgproc.COLOR_BGR2GRAY)
                    Utils.matToBitmap(outputGRAYMat, outputGRAYBitmap)
                    Glide.with(requireContext()).load(outputGRAYBitmap).into(outputImageView)

                    //位图
//                    Imgproc.threshold(outputCenterMat,outputMat,125.00,255.00,THRESH_BINARY_INV)
                    outputBMPBitmap = createBitmap(inputBitmap!!.width, inputBitmap!!.height)
                    Utils.bitmapToMat(outputBMPBitmap, outputBMPMat)
                    Imgproc.adaptiveThreshold(
                        outputGRAYMat, outputBMPMat, 255.00,
                        Imgproc.ADAPTIVE_THRESH_MEAN_C,
                        Imgproc.THRESH_BINARY, 13, 5.00
                    )
                    Utils.matToBitmap(outputBMPMat, outputBMPBitmap)
                    Glide.with(requireContext()).load(outputBMPBitmap).into(output2ImageView)

                    view.findViewById<Button>(R.id.clickView).setOnIntervalClickListener {
                        //灰度保存
                        FileUtils.saveFileToPicture(
                            requireContext(),
                            outputGRAYBitmap,
                            "${inputContentUri.path}_灰度"
                        )
                        FileUtils.saveFileToPicture(
                            requireContext(),
                            outputBMPBitmap,
                            "${FileManage.getFileName(inputContentUri)}_位图"
                        )
                    }
                }
            }
        }
    }
}