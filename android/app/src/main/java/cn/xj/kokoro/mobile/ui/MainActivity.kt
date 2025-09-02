package cn.xj.kokoro.mobile.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.base.BaseActivity
import cn.xj.kokoro.mobile.ui.page3.Page3Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity(R.layout.activity_main) {
    val fragmentList: MutableList<Pair<String, Fragment>> = arrayListOf()

    val page1Fragment by lazy {
        Page1Fragment()
    }

    val page2Fragment by lazy {
        Page2Fragment()
    }
    val page3Fragment by lazy {
        Page3Fragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fragmentList.add("OBJECT-绘制" to page1Fragment)
        fragmentList.add("OBJECT-OPENCV" to page2Fragment)
        fragmentList.add("vision-quickstart" to page3Fragment)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager2).apply {
            this.adapter = object: FragmentStateAdapter(this@MainActivity){
                override fun createFragment(position: Int): Fragment {
                    // Return a NEW fragment instance in createFragment(int).
//                    val fragment = Page1Fragment()
//                    fragment.arguments = Bundle().apply {
//                        // The object is just an integer.
////                        putInt(ARG_OBJECT, position + 1)
//                    }
                    return fragmentList[position].second
                }

                override fun getItemCount(): Int = fragmentList.size

            }
        }


        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = fragmentList[position].first
        }.attach()
    }
}