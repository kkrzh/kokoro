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
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cn.xj.kokoro.mobile.NativeLib
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.album.BitmapUtils
import cn.xj.kokoro.mobile.album.FileManage
import cn.xj.kokoro.mobile.album.FileUtils
import cn.xj.kokoro.mobile.album.selectorImage
import cn.xj.kokoro.mobile.base.BaseFragment
import cn.xj.kokoro.mobile.ui.MainActivity
import cn.xj.kokoro.mobile.ui.Page1Fragment
import cn.xj.kokoro.mobile.utils.setOnIntervalClickListener
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.common.util.concurrent.ListenableFuture
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class Page2Fragment: BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_page2

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }
    var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    val fragmentList: MutableList<Pair<String, Fragment>> = arrayListOf()
    val page2Child1Fragment by lazy {
        Page2Child1Fragment()
    }
    val page2Child2Fragment by lazy {
        Page2Child2Fragment()
    }
    override fun init(view: View, savedInstanceState: Bundle?) {
        fragmentList.add("OPENCV-基础" to page2Child1Fragment)
        fragmentList.add("OPENCV-颜色识别" to page2Child2Fragment)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        val viewPager2 = view.findViewById<ViewPager2>(R.id.viewPager2).apply {
            this.adapter = object: FragmentStateAdapter(this@Page2Fragment){
                override fun createFragment(position: Int): Fragment {
                    return fragmentList[position].second
                }

                override fun getItemCount(): Int = fragmentList.size

            }
        }


        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = fragmentList[position].first
        }.attach()



//        val previewView = view.findViewById<PreviewView>(R.id.preView)
//        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
//
//        cameraProviderFuture?.addListener(Runnable {
//            val cameraProvider = cameraProviderFuture?.get()
//            bindPreview(previewView,cameraProvider!!)
//        }, ContextCompat.getMainExecutor(requireContext()))


    }

}