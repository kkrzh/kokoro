package com.mobile.hotel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gyf.immersionbar.ImmersionBar
import com.mobile.hotel.base.setOnIntervalClickListener
import com.mobile.hotel.base.toast

class OrderActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context, name: String, price: Int) {
            context.startActivity(Intent(context, OrderActivity::class.java).apply {
                putExtra("name", name)
                putExtra("price", price)
            })
        }
    }

    val name by lazy {
        intent.getStringExtra("name")
    }
    val price by lazy {
        intent.getIntExtra("price", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ImmersionBar.with(this)  //导航栏透明度，不写默认0.0F
            .barColor(R.color.main).init()
        findViewById<TextView>(R.id.toolbarTextView).text = "<   ${name}"
        val priceView = findViewById<TextView>(R.id.typeprice).apply {
            text = "￥ ${price}"
        }

        val couont = findViewById<TextView>(R.id.count)
        val number = findViewById<TextView>(R.id.number)
        findViewById<TextView>(R.id.reduce).setOnIntervalClickListener {
            val current = couont.text.toString().toInt()
            if (current > 1) {
                couont.text = (current - 1).toString()
                priceView.text = "￥ ${price * (current - 1)}"
                number.text = "${(current - 1)}间 1晚共"
            }
        }
        findViewById<TextView>(R.id.add).setOnIntervalClickListener {
            val current = couont.text.toString().toInt()
            couont.text = (current + 1).toString()
            priceView.text = "￥ ${price * (current + 1)}"
            number.text = "${(current + 1)}间 1晚共"
        }
        findViewById<Button>(R.id.next).setOnIntervalClickListener {
            toast("下一步")
        }

    }
}