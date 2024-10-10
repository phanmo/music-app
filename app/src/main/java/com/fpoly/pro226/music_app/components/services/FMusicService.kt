package com.fpoly.pro226.music_app.components.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import com.fpoly.pro226.music_app.MainActivity
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.FMusicApplication

private const val MY_MEDIA_ROOT_ID = "media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

class FMusicService : MediaBrowserServiceCompat(), MediaPlayer.OnPreparedListener {
    companion object {
        const val TAG = "FMusicService"
        private const val ACTION_PLAY: String = "com.example.action.PLAY"
        const val url =
            "https://cdn-preview-d.dzcdn.net/stream/c-deda7fa9316d9e9e880d2c6207e92260-10.mp3" // your URL here


    }

    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private var mMediaPlayer: MediaPlayer? = null

    private val binder = FMusicBinder()
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        // Clients can connect, but this BrowserRoot is an empty hierarchy
        // so onLoadChildren returns nothing. This disables the ability to browse for content.
        return BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)

    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        //  Browsing not allowed
        if (MY_EMPTY_MEDIA_ROOT_ID == parentId) {
            result.sendResult(null)
            return
        }

        // Assume for example that the music catalog is already loaded/cached.

        val mediaItems = emptyList<MediaBrowserCompat.MediaItem>()

        // Check if this is the root menu:
        if (MY_MEDIA_ROOT_ID == parentId) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }
        result.sendResult(mediaItems.toMutableList())
    }

    override fun onCreate() {
        super.onCreate()
        // Create a MediaSessionCompat
        mediaSession = MediaSessionCompat(baseContext, TAG).apply {

            // Enable callbacks from MediaButtons and TransportControls
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())

            // MySessionCallback() has methods that handle callbacks from a media controller
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    super.onPlay()
                    Log.d(TAG, "onPlay: ")
                }

                override fun onPause() {
                    super.onPause()
                    Log.d(TAG, "onPause: ")
                }
            })

            // Set the session's token so that client activities can communicate with it.
            setSessionToken(sessionToken)
        }
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
