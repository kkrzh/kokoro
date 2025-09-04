package cn.xj.kokoro.mobile.album

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object FileUtils {
    /**
     * 创建一个文件
     * 如果存在将会先删除，因此这个文件总是最新的
     */
    fun createNewFile(directory: File, fileName: String): File {
        val file = File(directory, fileName)
        if (!directory.exists()) directory.mkdirs()
        if (file.exists() && file.isFile) file.delete()
        file.createNewFile()
        return file
    }

    fun saveFileToPicture(context:Context,fis:InputStream,fileName: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, AppDirectoryProvider.PublicDirectories.picturesRelativePath)
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            val uri10 = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val fos: OutputStream =   context.contentResolver.openOutputStream(uri10!!)!!
            FileManage.dataTransfer(fis,fos)
        }else{
            val image = FileManage.getPicturesFile("${fileName}.jpg")
            val fos: OutputStream =    FileOutputStream(image)
            FileManage.dataTransfer(fis,fos)
            //更新
            MediaScannerConnection.scanFile(
                context, arrayOf("${AppDirectoryProvider.PublicDirectories.getPicturesDirectory()}/${fileName}.jpg"), arrayOf("image/jpeg")
            ) { _: String, _: Uri ->

            }
        }
    }

    /**
     * 保存bitmap至相册，最终会保存为一张${filename}.png
     * @param context 上下文
     * @param bitmap 图像
     * @param fileName 保存的名称
     */
    fun saveFileToPicture(context:Context,bitmap: Bitmap, fileName: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, AppDirectoryProvider.PublicDirectories.picturesRelativePath)
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            val uri10 = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val fos: OutputStream =   context.contentResolver.openOutputStream(uri10!!)!!
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
        }else{
            val image = FileManage.getPicturesFile("${fileName}.png")
            val fos: OutputStream =    FileOutputStream(image)
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
            //更新
            MediaScannerConnection.scanFile(
                context, arrayOf("${AppDirectoryProvider.PublicDirectories.getPicturesDirectory()}/${fileName}.png"), arrayOf("image/png")
            ) { _: String, _: Uri ->

            }
        }
    }
}