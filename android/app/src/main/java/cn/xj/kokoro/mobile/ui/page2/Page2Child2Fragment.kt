package cn.xj.kokoro.mobile.ui.page2

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.album.BitmapUtils
import cn.xj.kokoro.mobile.album.selectorImage
import cn.xj.kokoro.mobile.base.BaseFragment
import cn.xj.kokoro.mobile.utils.setOnIntervalClickListener
import com.bumptech.glide.Glide
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class Page2Child2Fragment: BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_page2_child2

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

    }
    //输入Mat
    val inputMat: Mat? = Mat()
    //输入ContentUri
    var inputContentUri: Uri? = null
    //输入Bitmap
    var inputBitmap: Bitmap? = null

    //RGB Mat
    val outputRGBMat: Mat? = Mat()

    //HSV Mat
    val outputHSVMat: Mat? = Mat()
    //识别
    val outputRedMat: Mat? = Mat()



    override fun init(view: View, savedInstanceState: Bundle?) {
        val inputImageView = view.findViewById<ImageView>(R.id.inputImageView)
        val outputRGBImageView = view.findViewById<ImageView>(R.id.outputRGBImageView)
        val outputHSVImageView = view.findViewById<ImageView>(R.id.outputHSVImageView)
        val outputClipImageView = view.findViewById<ImageView>(R.id.outputClipImageView)
        val imageSelectorButton = view.findViewById<Button>(R.id.ImageSelectorButton)
        imageSelectorButton.setOnIntervalClickListener {
            requireActivity().selectorImage(1, false) {
                //选择的图片
                inputContentUri = it[0].toUri()
                inputBitmap = BitmapUtils.getBitmapFromContentUri(
                    requireContext().contentResolver,
                    inputContentUri
                )
                Glide.with(requireContext()).load(inputBitmap).into(inputImageView) //默认加载 是正确的
                Utils.bitmapToMat(inputBitmap, inputMat)                            // 现在inputMat是BGRA四通道

                //BGR -> RGB
                Imgproc.cvtColor(inputMat, outputRGBMat, Imgproc.COLOR_BGR2RGB)

                //HSV
                Imgproc.cvtColor(inputMat, outputHSVMat, Imgproc.COLOR_RGB2HSV)

                //颜色识别
                Core.inRange(outputHSVMat, Scalar(160.00,90.00,90.00), Scalar(179.00,255.00,255.00),outputRedMat)

                //去噪点
                val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(3.00,3.00))
                //开运算
                Imgproc.morphologyEx(outputRedMat,outputRedMat,Imgproc.MORPH_OPEN,kernel)
                //闭运算
                Imgproc.morphologyEx(outputRedMat,outputRedMat,Imgproc.MORPH_CLOSE,kernel)

                val resultBitmap = createBitmap(outputRedMat!!.width(), outputRedMat.height())
                val resultMat = Mat()
//                Imgproc.cvtColor(outputRedMat, resultMat, Imgproc.COLOR_HSV2BGR)

                Utils.matToBitmap(outputRedMat, resultBitmap)
                Glide.with(requireContext()).load(resultBitmap).into(outputRGBImageView)




                //裁剪
                val clipMat = Mat(inputMat, Rect(10,100,inputMat!!.width()-200,inputMat!!.height()-200))
                val clipBitmap = createBitmap(clipMat.width(), clipMat.height())
                Utils.matToBitmap(clipMat, clipBitmap)
                Glide.with(requireContext()).load(clipBitmap).into(outputClipImageView)

            }
        }
    }

    fun clipMat(){

    }
}