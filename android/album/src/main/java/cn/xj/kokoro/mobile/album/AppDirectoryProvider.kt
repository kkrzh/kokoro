package cn.xj.kokoro.mobile.album

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_ALARMS
import android.os.Environment.DIRECTORY_AUDIOBOOKS
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.DIRECTORY_DOCUMENTS
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.DIRECTORY_MOVIES
import android.os.Environment.DIRECTORY_MUSIC
import android.os.Environment.DIRECTORY_NOTIFICATIONS
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.DIRECTORY_PODCASTS
import android.os.Environment.DIRECTORY_RECORDINGS
import android.os.Environment.DIRECTORY_RINGTONES
import android.os.Environment.DIRECTORY_SCREENSHOTS
import androidx.annotation.RequiresApi

import java.io.File

/**
 * ContentUrl AbsolutePath Bitmap
 * Environment 是公共目录 /storage/emulated/0/Pictures
 * getExternalFilesDir 是私有目录
 */
object AppDirectoryProvider {

    var appDirectory = "kokoro"
    val fileProviderAuthorities = "cn.xj.kokoro.mobile.fileprovider"

    object PublicDirectories {
        // [/storage/emulated/0/Music/${appDirectory}]
        fun getMusicDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_MUSIC}${File.separator}${appDirectory}")

        // [/storage/emulated/0/Podcasts/${appDirectory}]
        fun getPodcastsDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_PODCASTS}${File.separator}${appDirectory}")

        // [/storage/emulated/0/Ringtones/${appDirectory}]
        fun getRingtonesDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_RINGTONES}${File.separator}${appDirectory}")

        // [/storage/emulated/0/Alarms/${appDirectory}]
        fun getAlarmsDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_ALARMS}${File.separator}${appDirectory}")

        // [/storage/emulated/0/Notifications/${appDirectory}]
        fun getNotificationsDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_NOTIFICATIONS}${File.separator}${appDirectory}")

        // [/storage/emulated/0/Pictures/${appDirectory}]
        val picturesRelativePath: String = "${DIRECTORY_PICTURES}${File.separator}${appDirectory}"
        fun getPicturesDirectory(): File =
            Environment.getExternalStoragePublicDirectory(picturesRelativePath)

        // [/storage/emulated/0/Movies/${appDirectory}]
        fun getMoviesDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_MOVIES}${File.separator}${appDirectory}")

        // [/storage/emulated/0/Download/${appDirectory}]
        fun getDownloadDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_DOWNLOADS}${File.separator}${appDirectory}")

        // [/storage/emulated/0/DCIM/${appDirectory}]
        fun getDCIMDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_DCIM}${File.separator}${appDirectory}")

        // [/storage/emulated/0/Documents/${appDirectory}]
        fun getDocumentsDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_DOCUMENTS}${File.separator}${appDirectory}")

        @RequiresApi(Build.VERSION_CODES.Q)
        // [/storage/emulated/0/Screenshots/${appDirectory}]
        fun getScreenshotsDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_SCREENSHOTS}${File.separator}${appDirectory}")

        @RequiresApi(Build.VERSION_CODES.Q)
        // [/storage/emulated/0/Audiobooks/${appDirectory}]
        fun getAudiobooksDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_AUDIOBOOKS}${File.separator}${appDirectory}")

        @RequiresApi(Build.VERSION_CODES.S)
        // [/storage/emulated/0/Recordings/${appDirectory}]
        fun getRecordingsDirectory(): File =
            Environment.getExternalStoragePublicDirectory("${DIRECTORY_RECORDINGS}${File.separator}${appDirectory}")
    }

    object PrivateDirectories {
        // [/storage/emulated/0/Android/data/${packet}/files/${name}]
        fun Context.getPrivateDirectory(name: String?) = getExternalFilesDir(name)
    }
}