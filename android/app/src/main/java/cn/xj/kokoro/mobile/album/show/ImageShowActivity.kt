package cn.xj.kokoro.mobile.album.show

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import cn.xj.kokoro.mobile.base.DetailData
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.base.BaseActivity


/**
 * 图片展示类
 * image为要展示的图片
 * position为初始展示的图片
 */

fun Context.showImage(array: Array<String>, position: Int) {
    if (this is Activity) {
        when{
            array.isEmpty() -> {
                Toast.makeText(this, "空", Toast.LENGTH_SHORT).show()
            }
            else ->{
                val intent = Intent(this, ImageShowActivity::class.java).apply {
                    putExtra("image", array)
                    putExtra("check", position)
                }
                this.startActivity(intent)
                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            }
        }
    } else {
        throw RuntimeException("调用showImage()请使用Activity的Context")
    }
}

class ImageShowActivity : BaseActivity(R.layout.activity_image_show) {
    val adapter by lazy {
        ImageShowAdapter(this, imageList)
    }
    val imageList: ArrayList<DetailData> by lazy {
        arrayListOf<DetailData>().apply {
            intent.getStringArrayExtra("image")?.forEach { url->
                add(DetailData(url))
            }
        }
    }
    var position = 0
    val viewPager by lazy{
        findViewById<ViewPager2>(R.id.viewPager)
    }
    val countView by lazy {
        findViewById<TextView>(R.id.countView)
    }
    val showTextView by lazy{
        findViewById<TextView>(R.id.showTextView)
    }


    override fun init(savedInstanceState: Bundle?) {
//        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
//        window.decorView.setBackgroundColor(resources.getColor(R.color.trBlack,null))
        position = intent.getIntExtra("check",0)

        viewPager.adapter = adapter
        countView.text = "${position + 1}/${imageList!!.size}"
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                countView.text = "${position + 1}/${imageList!!.size}"
            }
        })

        showTextView.setOnClickListener {
            imageList[viewPager.currentItem].state = true
            adapter.notifyItemChanged(viewPager.currentItem)
        }

        viewPager.post {
            viewPager.setCurrentItem(position, false)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
    }
}