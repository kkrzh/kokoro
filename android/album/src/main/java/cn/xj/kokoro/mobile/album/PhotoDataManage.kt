package cn.xj.kokoro.mobile.album

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LifecycleCoroutineScope
import cn.xj.kokoro.mobile.album.model.Folder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by 张先杰 on 2022年12月28日15:23:23
 *
 * Description:
 */
class PhotoDataManage(private val contet: Context) {
    //可以理解为一个资源管理器
    private val cursor by lazy {
        contet.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE),
            "${MediaStore.Images.Media.MIME_TYPE}=? or ${MediaStore.Images.Media.MIME_TYPE}=? or ${MediaStore.Images.Media.MIME_TYPE}=?",
            arrayOf("image/jpeg", "image/png", "image/gif"),
             "${MediaStore.Images.Media.DATE_MODIFIED} desc")
    }
    //数据
    var dataList = arrayListOf<Folder>()
    //初始化数据
    fun initializeData(lifecycleCoroutineScope: LifecycleCoroutineScope, onLoading: (String)->Unit, success:(Int, Boolean)->Unit){
            dataList.clear()
            dataList.add(Folder("全部照片", true, arrayListOf()))
            //关闭cursor
//            cursor?.close()
        addPhoto(lifecycleCoroutineScope,onLoading,true,success)
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun addPhoto(lifecycleCoroutineScope: LifecycleCoroutineScope, onLoading: (String)->Unit, isFirst:Boolean = false, success:(Int, Boolean)->Unit){
//        try {
            lifecycleCoroutineScope.launch(Dispatchers.IO) {
                if (cursor != null){
                    var count = 0
                    val maxCount =  if (isFirst) 123 else 124
                    withContext(Dispatchers.Main) {
                        onLoading.invoke("")
                    }
                    //生成Bitmap以校验是否为破损图片 由于不需要展示 所以只需要考虑内存消耗问题
                    val option = BitmapFactory.Options().apply {
                        //设置为内存消耗最小的颜色通道
                        this.inPreferredConfig = Bitmap.Config.ALPHA_8
                        //不为Bitmap分配内存 会导致Bitmap为空
                        this.inJustDecodeBounds = true
                        //设置压缩倍率
                        this.inSampleSize = 16
                    }
                    while (count < maxCount && cursor!!.moveToNext()){
                        count++
                        withContext(Dispatchers.Main){
                            onLoading.invoke("${cursor!!.position+1}/${cursor!!.count}")
                        }
                        try {
                            //图片路径 一般是绝对路径
                            val path: String =
                                cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                            Log.e("TAG", "addPhoto: $path", )
                            //文件不存在跳过
                            if (!File(path).exists()){
                                continue
                            }
                            //此处为获取上级目录名
                            val dirPath: String = File(path).parentFile?.absolutePath?:""
                            if (dirPath.isBlank()) continue
                            val dirList = dirPath.split("/")
                            if (dirList.isEmpty()) continue
                            try {
                                //此处必定会抛出NullPointerException，因为Bitmap为null
                                BitmapFactory.decodeFile(path,option)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            //若outWidth为-1则表示为破损图片 跳过该图片
                            if(option.outWidth == -1) continue
                            val folder = dirList[dirList.size-1]
                            val size :String = cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                            //data数组中是否已经拥有该目录
                            var haveFolder = false
                            val folderKt = Folder.Photo(
                                FileManage.getProviderUri(contet, path),
                                false,
                                -1,
                                size.toLong(),
                                FileManage.format(size.toLong()),
                                FileManage.getFileExtensionFromUrl(path)
                            )
                            //无论是否拥有都添加进全部图片中
                            dataList[0].fileList.add(folderKt)
                            for (i in dataList){
                                if (i.name == folder){
                                    //若有该目录直接加入该目录
                                    haveFolder = true
                                    i.fileList.add(folderKt)
                                    break
                                }
                            }
                            //若无该目录，添加该目录并添加图片
                            if (!haveFolder) dataList.add(
                                Folder(
                                    folder,
                                    false,
                                    arrayListOf(folderKt)
                                )
                            )
                        }catch(e:Exception){
                            e.printStackTrace()
                            continue
                        }
                    }
                    val isFinish = if (count<maxCount && !cursor!!.moveToNext()){
                        cursor!!.close()
                        true
                    }else false
                    withContext(Dispatchers.Main){
                        success.invoke(count,isFinish)
                    }
                }
            }
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
    }
    /**
     * 插入图片
     */
     fun addPhoto(uri: Uri,success: () -> Unit){
        //目录名
        val folderName : String = Environment.getExternalStoragePublicDirectory(FileManage.pictureFlag).toString()
        //文件名
        val fileName : String = DocumentFile.fromSingleUri(contet, uri)!!.name!!.split(".")[0]
        val fis: InputStream = contet.contentResolver.openInputStream(uri)!!
        fun updateList(){
            //data数组中是否已经拥有该目录
            var haveFolder = false
            val newUri = FileManage.getProviderUri(contet,"${folderName}/${fileName}.jpg")
            val size = FileManage.getFileSize(contet,newUri)
            val folderKt = Folder.Photo(newUri, false, -1, size, FileManage.format(size), "jpg")
            //无论是否拥有都添加进全部图片中
            dataList[0].fileList.add(0,folderKt)
            for (i in dataList){
                if (i.name == FileManage.fileFlag){
                    //若有该目录直接加入该目录
                    haveFolder = true
                    i.fileList.add(0,folderKt)
                    break
                }
            }
            //若无该目录，添加该目录并添加图片
            if (!haveFolder) dataList.add(Folder(FileManage.fileFlag, false, arrayListOf(folderKt)))
            success.invoke()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, FileManage.pictureFlag)
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            val uri10 = contet.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val fos: OutputStream =   contet.contentResolver.openOutputStream(uri10!!)!!
            FileManage.dataTransfer(fis,fos)
            updateList()
        }else{
            val image = FileManage.getPicturesFile("${fileName}.jpg")
            val fos: OutputStream =    FileOutputStream(image)
            FileManage.dataTransfer(fis,fos)
            //更新
            MediaScannerConnection.scanFile(
                contet, arrayOf("${folderName}/${fileName}.jpg"), arrayOf("image/jpeg")
            ) { _: String, _: Uri ->

            }
            updateList()
        }
    }

    /**
     * 目录列表从多到少排序
     */
     fun sortFolder() {
        if (dataList.size < 2) {
            return
        }
        var temp: Folder
        var len = dataList.size - 1
        for (i in 0 until len) {
            var lastChange = 0
            for (j in 0 until len) {
                if (dataList[j].fileList.size < dataList[j + 1].fileList.size) {
                    temp = dataList[j]
                    dataList[j] = dataList[j + 1]
                    dataList[j + 1] = temp
                    lastChange = j
                }
            }
            if (lastChange == 0) {
                return
            }
            len = lastChange
        }
        return
    }

    /**
     * 获取排序后的列表
     */
     fun getSortCheck():Array<String>{
        val map = HashMap<Int,String>()
        for (i in  dataList[0].fileList){
            if (i.state){
                map[i.id] = i.name.toString()
            }
        }
        val array = arrayListOf<String>()
        for (i in 1..map.size){
            if (map[i] != null){
                array.add(map[i]!!)
            }
        }
        return array.toArray(arrayOf())
    }

    /**
     * 获取当前选中图片个数
     */
    fun getCurrentChooseSize(): Int {
        var size = 0
        for (i in dataList[0].fileList){
            if (i.state){
                size++
            }
        }
        return size
    }
}