package com.mobile.hotel

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.mobile.hotel.base.createBasicShape
import com.mobile.hotel.base.dp
import com.mobile.hotel.base.dpToInt
import com.mobile.hotel.base.getSafeCompatColor
import com.mobile.hotel.base.setOnIntervalClickListener
import com.mobile.hotel.model.SearchModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DetailActivity: AppCompatActivity() {
    companion object{
        fun start(context: Context,data: SearchModel.Data){
            context.startActivity(Intent(context, DetailActivity::class.java).apply {
                putExtra("data",data)
            })
        }
    }

    val data by lazy{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("data",SearchModel.Data::class.java)
        } else {
            intent.getSerializableExtra("data") as SearchModel.Data
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,0, systemBars.right, systemBars.bottom)
            insets
        }
        ImmersionBar.with(this)
            .barColor(R.color.main).init()

        findViewById<ImageView>(R.id.imageView).apply{
            Glide.with(this@DetailActivity).load(data?.images[0]).into(this)
        }

        findViewById<TextView>(R.id.companyName).text = data?.companyName
        findViewById<TextView>(R.id.address).text =  "位置：${data?.address}"
        findViewById<TextView>(R.id.phone).text =  "电话：${data?.tel}"

        findViewById<StarProgressView>(R.id.starView).apply {
            if (data?.score!!.isNotBlank()){
                visibility = View.VISIBLE
                setStarCount(data?.score!!.toFloat().toInt())
            }else{
                visibility = View.GONE
            }
        }

        findViewById<TextView>(R.id.starCount).text = data?.score?.ifBlank { "" }
        findViewById<TextView>(R.id.price).text = "￥${data?.minPrice.toString()}起"
        findViewById<TextView>(R.id.type1price).text = "￥${data?.minPrice.toString()}"
        findViewById<TextView>(R.id.type2price).text = "￥${data?.minPrice!!+50}"
        findViewById<TextView>(R.id.type3price).text = "￥${data?.minPrice!!+100}"
        findViewById<Button>(R.id.type1button).setOnIntervalClickListener {
            OrderActivity.start(this,data?.companyName!!,data?.minPrice!!)
        }
        findViewById<Button>(R.id.type2button).setOnIntervalClickListener {
            OrderActivity.start(this,data?.companyName!!,data?.minPrice!!+50)
        }
        findViewById<Button>(R.id.type3button).setOnIntervalClickListener {
            OrderActivity.start(this,data?.companyName!!,data?.minPrice!!+100)
        }

        val df: DateFormat = SimpleDateFormat("MM月dd日", Locale.CHINA)
        val today = Date()

        val calendar = Calendar.getInstance()
        calendar.setTime(today)
        calendar.add(Calendar.DAY_OF_YEAR, 1) // 加1天
        val tomorrow = calendar.getTime()
        findViewById<TextView>(R.id.startTime).text = "${df.format(today)} 今天  -"
        findViewById<TextView>(R.id.centerFlag).apply {
            text = " 一晚 "
            background = createBasicShape(4F.dp,getSafeCompatColor(R.color.content_main),1F.dpToInt,getSafeCompatColor(R.color.main))
        }
        findViewById<TextView>(R.id.endTime).text = "-  ${df.format(tomorrow)}明天"
    }
}