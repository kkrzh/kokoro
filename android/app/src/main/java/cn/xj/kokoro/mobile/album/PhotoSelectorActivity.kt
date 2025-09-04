package cn.xj.kokoro.mobile.album

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.xj.kokoro.mobile.album.list.FolderWindowAdapter
import cn.xj.kokoro.mobile.album.list.PhotoListAdapter
import cn.xj.kokoro.mobile.base.BaseActivity
import cn.xj.kokoro.mobile.base.BaseBindingActivity
import cn.xj.kokoro.mobile.databinding.ActivityPhotoSelectorBinding
import cn.xj.kokoro.mobile.utils.PopupWindowManager
import cn.xj.kokoro.mobile.utils.setOnIntervalClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 跳转该类并返回选择的照片 未确认的情况将会返回空数组
 * @param maxChoose 最大选择数、默认为1，为1时即为单选
 * @param useVideo 是否获取视频
 */
fun Context.selectorImage(maxChoose:Int = 1,enableCompression:Boolean = true,useVideo:Boolean = false,resultSuccess:((Array<String>)->Unit)){
    if (this is BaseActivity){
            val bundle = Bundle().apply {
                putInt("maxChoose", maxChoose)
                putBoolean("useVideo", useVideo)
            }
            val intent = Intent(this, ImageSelectorActivity::class.java).apply {
                putExtras(bundle)
            }
        window.enterTransition = Slide().setDuration(250)

        startActivityForResult(intent){
                if (resultCode == AppCompatActivity.RESULT_OK){
                    if (data != null && data!!.extras != null && data!!.extras!!.getStringArray("PhotoArray")!=null){
                        val array:Array<String> = data!!.extras!!.getStringArray("PhotoArray")!!
                        resultSuccess.invoke(array)
                    } else throw RuntimeException("数据丢失")
                }else if (resultCode == AppCompatActivity.RESULT_CANCELED){
                    resultSuccess.invoke(arrayOf())
                }
            }
        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
    }else{
        throw RuntimeException("调用selectorImage()请使用BaseActivity的Context")
    }
}


class ImageSelectorActivity : BaseBindingActivity<ActivityPhotoSelectorBinding>() {
    //最大选中数
    val maxChoose by lazy { intent.getIntExtra("maxChoose",0) }
    val useVideo by lazy { intent.getBooleanExtra("useVideo",false) }
    val enableCompression by lazy { intent.getBooleanExtra("enableCompression",false) }

    //当前选中目录下标
    internal var choosePosition = 0
    val photoDataManage = PhotoDataManage(this)

    //目录列表是否为展开
    private var isSelector = false
    private var chooseWindow: PopupWindowManager? = null
    private var uri: Uri? = null
    //拍照
    private var takePhoneLauncher: ActivityResultLauncher<Uri>? = null



    //目录选择适配器
    private val windowAdapter by lazy{
        FolderWindowAdapter(this,photoDataManage).apply {
            clickListener = {
                gridAdapter.notifyItemRangeRemoved(0,if(choosePosition == 0)photoDataManage.dataList[choosePosition].fileList.size + 1 else photoDataManage.dataList[choosePosition].fileList.size)
                choosePosition = it
                gridAdapter.notifyItemRangeInserted(0,if(choosePosition == 0) photoDataManage.dataList[choosePosition].fileList.size + 1  else photoDataManage.dataList[choosePosition].fileList.size)
                changeState()
            }
        }
    }

    //图片列表适配器
    private val gridAdapter by lazy{
        PhotoListAdapter(this@ImageSelectorActivity,photoDataManage).apply {
            getChoosePosition = {
                choosePosition
            }
            startTakePhoto = {
                uri = FileManage.getTakePhotoUri(this@ImageSelectorActivity)
                takePhoneLauncher!!.launch(uri!!)
            }
            setState = { position->
                if (photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList[position].state){
                    photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList[position].state = false
                    for (i in 0 until photoDataManage.dataList[0].fileList.size){
                        if (photoDataManage.dataList[0].fileList[i].id >  photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList[position].id){
                            photoDataManage.dataList[0].fileList[i].id--
                            notifyItemRangeChanged(if(getChoosePosition?.invoke() == 0)i+1 else i,1)
                        }
                    }
                    photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList[position].id = -1
                    if (photoDataManage.getCurrentChooseSize() == 0){
                        binding.finishButton.text = "完成"
                        binding.finishButton.isEnabled = false
                    }else{
                        binding.finishButton.text = "完成${photoDataManage.getCurrentChooseSize()}/${maxChoose}"
                        binding.finishButton.isEnabled = true
                    }
                }else{
                    if (photoDataManage.getCurrentChooseSize() < maxChoose){
                        photoDataManage.dataList[choosePosition].fileList[position].state = true
                        photoDataManage.dataList[choosePosition].fileList[position].id = photoDataManage.getCurrentChooseSize()
                        binding.finishButton.text = "完成${photoDataManage.getCurrentChooseSize()}/${maxChoose}"
                        binding.finishButton.isEnabled = true
                    }else Toast.makeText(this@ImageSelectorActivity, "最多选择${maxChoose}张图片", Toast.LENGTH_SHORT).show()
                }
                notifyItemRangeChanged(if(getChoosePosition?.invoke() == 0)position+1 else position,1)
            }
        }
    }

    override fun getViewId(): Int = cn.xj.kokoro.mobile.R.layout.activity_photo_selector

    override fun observerUI() {

    }

    override fun init(savedInstanceState: Bundle?) {
//        ImmersionBar.with(this).barColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(cn.xj.kokoro.mobile.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val array = arrayListOf<String>()
         array.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        array.add(Manifest.permission.CAMERA)
//        array.add(Manifest.permission.READ_MEDIA_IMAGES)
        array.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        takePhoneLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it){
                    photoDataManage.addPhoto(uri!!){
                        gridAdapter.notifyItemRangeInserted(1,1)
                        gridAdapter.notifyItemRangeChanged(1,photoDataManage.dataList[0].fileList.size)
                    }
                }
            }
        requestPermission(array.toArray(arrayOf())){
            if (!it){
                Toast.makeText(this, "无权限", Toast.LENGTH_SHORT).show()
                finishAfterTransition()
            }else {
                binding.titleView.requestFocus()
                binding.finishButton.isEnabled = false
                binding.finishButton.text = "完成"
                photoDataManage.initializeData(lifecycleScope,{
                    loadingWindow.showWindow()
                }) {size,finish ->
                    loadingWindow.hindWindow {
                        binding.refreshLayout.setEnableLoadMore(!finish)
                        binding.recycler.apply {
                            layoutManager = GridLayoutManager(this@ImageSelectorActivity,4)
                            adapter = gridAdapter
                        }
                    }
                }
            }
        }


        binding.alphaView.setOnClickListener {
            changeState()
        }
        binding.arrow.setOnClickListener {
            changeState()
        }
        binding.titleView.setOnClickListener {
            changeState()
        }
        binding.finishButton.setOnIntervalClickListener {
            val sortList = photoDataManage.getSortCheck()
            if (enableCompression){
                loadingWindow.showWindow("正在处理图片...")
                GlobalScope.launch(Dispatchers.IO){
                    for (i in sortList.indices){
                        FileManage.compressImage(this@ImageSelectorActivity,sortList[i].toUri()){
                            Log.e(TAG, "initListener: ${sortList[i]}", )
                            Log.e(TAG, "initListener: ${sortList[i].toUri()}", )
                            Log.e(TAG, "initListener: ${it}", )
                            sortList[i] = it.toString()
                        }
                    }
                    withContext(Dispatchers.Main){
                        loadingWindow.hindWindow {
                            val intent = Intent()
                            val bundle = Bundle()
                            bundle.putStringArray("PhotoArray",sortList)
                            intent.putExtras(bundle)
                            setResult(RESULT_OK, intent)
                            finishAfterTransition()
                        }
                    }
                }
            }else{
                val intent = Intent()
                val bundle = Bundle()
                bundle.putStringArray("PhotoArray",sortList)
                intent.putExtras(bundle)
                setResult(RESULT_OK, intent)
                finishAfterTransition()
            }

        }
        binding.layoutToolbarIconClick.setOnClickListener {
            finishAfterTransition()
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
        }
        binding.refreshLayout.setOnLoadMoreListener {
            if(choosePosition == 0){
                val lastSize = photoDataManage.dataList[0].fileList.size
                lifecycleScope
                photoDataManage.addPhoto(lifecycleScope,{
                    loadingWindow.showWindow()
                }){size,finish ->
                    loadingWindow.hindWindow {
                        binding.refreshLayout.setEnableLoadMore(!finish)
                        binding.refreshLayout.finishLoadMore()
                        gridAdapter.notifyItemRangeInserted(lastSize+1,size)
                        windowAdapter.notifyItemRangeChanged(0,photoDataManage.dataList.size)
                    }
                }
            }
        }
    }


    /**
     * 改变目录列表状态
     */
    private fun changeState(){
        if (isSelector){
            if (chooseWindow!=null) chooseWindow!!.dismiss()
        }else{
            binding.arrow.animate().apply {
                duration = 300
                rotation(180F)
            }
            showPopupWindow()
        }
        binding.titleView.text = photoDataManage.dataList[choosePosition].name
    }
    /**
     * 显示目录列表
     */
    private fun showPopupWindow(){
        photoDataManage.sortFolder()
        binding.alphaView.visibility = View.VISIBLE
        if (chooseWindow == null){
            chooseWindow = PopupWindowManager(
                this@ImageSelectorActivity,
                cn.xj.kokoro.mobile.R.layout.popup_recycler,
                PopupWindowManager.Companion.MATCH_PARENT,
                PopupWindowManager.Companion.WRAP_CONTENT,
                outsideTouchable = true,
                focusable = true,
                false,
                finishListener = {
                    isSelector = false
                    binding.arrow.animate().apply {
                        duration = 300
                        rotation(0F)
                    }
                    binding.alphaView.visibility = View.INVISIBLE
                },
            ) { popupWindow, _ ->
                popupWindow.enterTransition = Slide().apply {
                    duration = 300
                    slideEdge = Gravity.TOP
                }
                popupWindow.exitTransition = Slide().apply {
                    duration = 300
                    slideEdge = Gravity.TOP
                }
                findViewById<RecyclerView>(cn.xj.kokoro.mobile.R.id.listRecycler).apply {
                    layoutManager = LinearLayoutManager(this@ImageSelectorActivity)
                    adapter = windowAdapter
                }
            }
        }
        chooseWindow!!.show( binding.toolbar,0, 0, Gravity.BOTTOM)
        isSelector = true
    }

    override fun onPause() {
        super.onPause()
        if (chooseWindow!=null){
            chooseWindow!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (chooseWindow!=null){
//            chooseWindow!!.onDestroy()
            chooseWindow = null
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
    }
}