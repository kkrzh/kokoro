package cn.xj.kokoro.mobile.album.list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.xj.kokoro.mobile.view.FlagView
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.album.PhotoDataManage
import cn.xj.kokoro.mobile.utils.createBasicShape
import com.bumptech.glide.Glide
import cn.xj.kokoro.mobile.base.BaseAdapter
import cn.xj.kokoro.mobile.album.show.showImage

/**
 * Created by xianjie on 2022年12月28日16:01:18
 *
 * Description:
 */
class PhotoListAdapter(context:Context,val photoDataManage: PhotoDataManage) : BaseAdapter(context){
    private val takePhoto = 0x1000
    private val showPhoto = 0x2000
    override fun getItemViewType(position: Int): Int =
        if (position == 0 && getChoosePosition?.invoke() == 0) takePhoto else showPhoto

    override fun setItemLayout(viewType: Int): Int = if (viewType == showPhoto) R.layout.adapter_photo_selector_detail else R.layout.adapter_photo_selector_add

    override fun setItemCount(): Int = if (getChoosePosition?.invoke() == 0) photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList.size + 1 else photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val imageView = holder.getViewById<ImageView>(R.id.image)
        when(getItemViewType(position)){
            showPhoto ->{
                val positionX = if (getChoosePosition?.invoke() == 0) position - 1 else position
                val shadeView = holder.getViewById<ImageView>(R.id.shadeView)
                val idView = holder.getViewById<TextView>(R.id.idView)
                val clickView = holder.getViewById<View>(R.id.clickView)
                val flagView = holder.getViewById<FlagView>(R.id.flagView)
                flagView.setFlag( photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList[positionX].extension,Color.parseColor("#33FFFFFF"),24f)
                if (photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList[positionX].state){
                    shadeView.setImageDrawable(createBasicShape(backGroundColor = Color.parseColor("#59000000")))
                    idView.setBackgroundResource(R.drawable.shape_check_true)
                    idView.text = photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList[positionX].id.toString()
                } else {
                    shadeView.setImageDrawable(createBasicShape(backGroundColor = Color.parseColor("#0A000000")))
                    idView.setBackgroundResource(R.drawable.shape_check_false_v2)
                    idView.text = ""
                }

                clickView.setOnClickForAdapterListener(position) {
                    setState?.invoke(positionX)
                }

                Glide.with(context).load(photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList[positionX].name).into(imageView)
                imageView.setOnClickForAdapterListener {
                    val array = arrayListOf<String>()
                    for (i in photoDataManage.dataList[getChoosePosition?.invoke()!!].fileList){
                        array.add(i.name.toString())
                    }
                    context.showImage(array.toArray(arrayOf<String>()),positionX)
                }
            }
            takePhoto ->{
                imageView.setOnClickForAdapterListener {
                    startTakePhoto?.invoke()
                }
            }
        }

    }
    var getChoosePosition:(()->Int)?= null
    var startTakePhoto:(()->Unit)?= null
    var setState:((Int)->Unit)?= null
}