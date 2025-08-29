package cn.xj.kokoro.mobile.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import cn.xj.kokoro.mobile.EventFrameLayout
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.base.BaseFragment
import cn.xj.kokoro.mobile.utils.createGradientShape
import cn.xj.kokoro.mobile.album.selectorImage
import cn.xj.kokoro.mobile.utils.setOnIntervalClickListener
import cn.xj.kokoro.mobile.view.DashboardView
import cn.xj.kokoro.mobile.view.material500List

class Page1Fragment : BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_page1

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

    }

    override fun init(view: View, savedInstanceState: Bundle?) {
        view.apply {
            findViewById<TextView>(R.id.textView).setOnIntervalClickListener {
                requireActivity().selectorImage(9, false) {

                }
            }
            val view1 = findViewById<DashboardView>(R.id.cusView1)
            val seekBar = findViewById<SeekBar>(R.id.seekBar).apply {
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        Log.e(TAG, "onProgressChanged: ${progress}")
                        view1.setValue(progress.toFloat(), false)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }

                })
            }
            view1.valueChangeCallback = {
                seekBar.progress = it.toInt()
            }
            findViewById<Button>(R.id.button1).apply {
                setOnIntervalClickListener {
                    view1.startAnimation(20F)
                }
            }
            findViewById<Button>(R.id.button2).apply {
                setOnIntervalClickListener {
                    view1.startAnimation(50F)
                }
            }
            findViewById<Button>(R.id.button3).apply {
                setOnIntervalClickListener {
                    view1.startAnimation(80F)
                }
            }
            val drawable = createGradientShape(
                backGroundColor =
                    material500List.toIntArray(),
                positions = floatArrayOf(
                    0.0F,
                    0.05F,
                    0.1F,
                    0.15F,
                    0.2F,
                    0.25F,
                    0.3F,
                    0.35F,
                    0.4F,
                    0.45F,
                    0.5F,
                    0.55F,
                    0.6F,
                    0.65F,
                    0.7F,
                    0.75F,
                    0.8F,
                    0.85F,
                    0.9F,

                    ),
                orientation = GradientDrawable.Orientation.TOP_BOTTOM
            )
            findViewById<View>(R.id.canvan3).apply {
                background = drawable
            }


            findViewById<View>(R.id.canvan2).apply {
                background = drawable
            }


            findViewById<View>(R.id.canvan).apply {
                background = drawable

            }
            val root = findViewById<EventFrameLayout>(R.id.rootLayout)
            root.apply {
                //想要拦截事件，有此处处理不传递给子View

                setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(
                        v: View?,
                        event: MotionEvent?
                    ): Boolean {
                        v?.performClick()
                        return true
                    }

                })

                setOnIntervalClickListener {
                    Toast.makeText(requireContext(), "根视图", Toast.LENGTH_SHORT).show()
                }
            }

            findViewById<Switch>(R.id.seekbar1).apply {
                setOnCheckedChangeListener { view, check ->
                    root.interceptEnabled = check
                }
            }

            findViewById<Switch>(R.id.seekbar2).apply {
                setOnCheckedChangeListener { view, check ->
                    if (check) {
                        root.addExcludedViewType(ScrollView::class)
                        root.addExcludedViewType(NestedScrollView::class)
                    } else {
                        root.removeAllExcludeViews()
                    }

                }
            }
        }
    }
}