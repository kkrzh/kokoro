package cn.xj.kokoro.mobile.ui.page3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.SCREEN_CAPTURE_ACTION

class MediaProjectionService: Service() {
    private val CHANNEL_ID = "media_projection_channel"
    override fun onCreate() {
        super.onCreate()
        // 创建通知通道（只在 Android 8.0 及以上需要）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Media Projection Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // 创建前台服务的通知
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Media Projection Service")
            .setContentText("Capturing screen...")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        // 将服务设置为前台服务
        startForeground(1, notification)
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // 在这里执行屏幕捕获逻辑，例如调用 MediaProjection API
        val broadcastIntent = Intent(SCREEN_CAPTURE_ACTION)
        sendBroadcast(broadcastIntent)

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null
}