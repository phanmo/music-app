package com.fpoly.pro226.music_app.components.services

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Build
import androidx.core.app.TaskStackBuilder
import com.fpoly.pro226.music_app.MainActivity
import com.fpoly.pro226.music_app.ui.screen.song.PlayerActivity

class FMusicPlaybackService: FMusicService() {

    companion object {
        private val immutableFlag = if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
    }

    override fun getSingleTopActivity(): PendingIntent? {
        return getActivity(
            this,
            0,
            Intent(this, PlayerActivity::class.java),//PlayerActivity
            immutableFlag or FLAG_UPDATE_CURRENT
        )
    }

    override fun getBackStackedActivity(): PendingIntent? {
        return TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@FMusicPlaybackService, MainActivity::class.java))//MainActivity
            addNextIntent(Intent(this@FMusicPlaybackService, PlayerActivity::class.java))//PlayerActivity
            getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
        }
    }
}