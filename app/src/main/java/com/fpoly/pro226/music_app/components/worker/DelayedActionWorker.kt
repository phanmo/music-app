package com.fpoly.pro226.music_app.components.worker

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fpoly.pro226.music_app.components.services.FMusicPlaybackService
import com.google.common.util.concurrent.ListenableFuture

class DelayedActionWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    override fun doWork(): Result {
        Log.d("DelayedActionWorker", "Hành động đã được thực hiện!")
        browserFuture =
            MediaBrowser.Builder(
                context,
                SessionToken(context, ComponentName(context, FMusicPlaybackService::class.java))
            ).buildAsync()

        browserFuture.addListener({
            try {
                val browser =
                    if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null
                if (browser?.isPlaying == true) {
                    browser.pause()

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
        return Result.success()
    }
}
