package cn.xj.kokoro.mobile.album.show

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import cn.xj.kokoro.mobile.base.DetailData
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.base.BaseAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * Created by xianjie on 2022年12月28日15:12:20
 *
 * Description:
 */
class ImageShowAdapter(context:Context, val imageList: List<DetailData>) : BaseAdapter(context){
    override fun setItemLayout(viewType: Int): Int = R.layout.fragment_image_show

    override fun setItemCount(): Int =imageList!!.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val constraint = holder.getViewById<ConstraintLayout>(R.id.constraint)
        val showImage = holder.getViewById<ImageView>(R.id.showImage)
        if (imageList[position].state){
            if (imageList!![position].name!!.contains("gif")) Glide.with(context).setDefaultRequestOptions(
                RequestOptions()
                    .override(
                        Target.SIZE_ORIGINAL,
                        Target.SIZE_ORIGINAL)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .skipMemoryCache(true)

            ).asGif().load(imageList!![position].name).into(showImage)
            else  Glide.with(context).setDefaultRequestOptions(RequestOptions()
                .override(
                    Target.SIZE_ORIGINAL,
                    Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .skipMemoryCache(true)
            ).load(imageList!![position].name).into(showImage)

        }else{
            Glide.with(context).load(imageList!![position].name).into(showImage)
        }
        showImage.setOnClickListener {
            if (context is Activity){
                (context as Activity).finishAfterTransition()
                (context as Activity).overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            }
        }
        constraint.setOnClickListener {
            if (context is Activity){
                (context as Activity).finishAfterTransition()
                (context as Activity).overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            }
        }
    }
}