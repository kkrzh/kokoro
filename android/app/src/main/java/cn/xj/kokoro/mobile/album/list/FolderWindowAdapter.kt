package cn.xj.kokoro.mobile.album.list

import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.album.PhotoDataManage
import com.bumptech.glide.Glide
import cn.xj.kokoro.mobile.base.BaseAdapter

/**
 * Created by xianjie on 2022年12月28日15:56:34
 *
 * Description:
 */
class FolderWindowAdapter(context: Context,val photoDataManage: PhotoDataManage) : BaseAdapter(context){
    override fun setItemLayout(viewType: Int): Int = R.layout.adapter_photo_selector
    override fun setItemCount(): Int = photoDataManage.dataList.size
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.getViewById<ConstraintLayout>(R.id.constraint).setOnClickListener {
            clickListener?.invoke(holder.adapterPosition)
        }
        holder.getViewById<TextView>(R.id.name).text = photoDataManage.dataList[position].name
        holder.getViewById<TextView>(R.id.count).text = photoDataManage.dataList[position].fileList.size.toString()
        if (photoDataManage.dataList[position].fileList.isNotEmpty())
            Glide.with(context).load(photoDataManage.dataList[position].fileList[photoDataManage.dataList[position].fileList.size-1].name).into(holder.getViewById(
                R.id.image))
    }
    var clickListener:((Int)->Unit)? = null
}