package cn.xj.kokoro.mobile.album.model

import android.net.Uri

/**
 * Created by xianjie on 2022年12月28日15:18:27
 *
 * Description:
 */
data class Folder(
    //文件夹名称
    val name: String,
    //文件夹选中状态
    var state: Boolean,
    //文件夹内容
    val fileList: ArrayList<Photo>
) {
    data class Photo(
        //文件路径
        val name: Uri,
        //照片选中状态
        var state: Boolean,
        //id
        var id: Int = -1,
        var sizeLong: Long = 0,
        var size: String = "",
        var extension: String
    )
}