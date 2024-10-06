package com.fpoly.pro226.music_app.components.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fpoly.pro226.music_app.MainActivity
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.FMusicApplication

class FMusicService : Service(), MediaPlayer.OnPreparedListener {
    companion object {
        const val TAG = "FMusicService"
        private const val ACTION_PLAY: String = "com.example.action.PLAY"
        const val url =
            "https://cdn-preview-d.dzcdn.net/stream/c-deda7fa9316d9e9e880d2c6207e92260-10.mp3" // your URL here


    }

    private var mMediaPlayer: MediaPlayer? = null

    private val binder = FMusicBinder()
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification)
//        val action: String? = intent?.action
//        when (action) {
//            ACTION_PLAY -> {
//                mMediaPlayer = MediaPlayer().apply {
//                    setAudioAttributes(
//                        AudioAttributes.Builder()
//                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                            .setUsage(AudioAttributes.USAGE_MEDIA)
//                            .build()
//                    )
//                    setDataSource(url)
//                    prepare() // might take long! (for buffering, etc)
//                    start()
//                }
//                mMediaPlayer?.apply {
//                    setOnPreparedListener(this@FMusicService)
//                    prepareAsync() // prepare async to not block main thread
//                }
//
//            }
//        }
        mMediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            setOnPreparedListener(this@FMusicService)
            prepareAsync() // prepare async to not block main thread
        }
        Log.d(TAG, "onStartCommand: called")
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, FMusicApplication.CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Service đang chạy...")
            .setSmallIcon(R.drawable.love)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.release()
        Log.d(TAG, "onDestroy: called ")
    }

    inner class FMusicBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): FMusicBinder = this@FMusicBinder
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mMediaPlayer?.start()
    }
}