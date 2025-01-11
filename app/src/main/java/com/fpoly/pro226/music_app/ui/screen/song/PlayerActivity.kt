package com.fpoly.pro226.music_app.ui.screen.song

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.fpoly.pro226.music_app.components.FMusicApplication
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.components.worker.DelayedActionWorker
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import java.io.File
import java.util.concurrent.TimeUnit


class PlayerActivity : ComponentActivity() {
    companion object {
        const val TAG = "PlayerActivity"
    }

    private val appContainer: AppContainer by lazy {
        (application as FMusicApplication).appContainer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SongScreen(
                        appContainer = appContainer,
                        downloadTrack = {trackUrl, fileName ->
                            downloadTrack( trackUrl, fileName)
                        },
                        scheduleDelayedAction = { targetTime ->
                            if (targetTime > 0) {
                                Toast.makeText(
                                    this,
                                    "The music will turn off in $targetTime minutes.",
                                    Toast.LENGTH_LONG
                                ).show()
                                scheduleDelayedAction(this, targetTime)
                            } else {
                                Toast.makeText(
                                    this, "Appointment at least 1 minute",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                }
            }
        }
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                Toast.makeText(context, "Download successfully !", Toast.LENGTH_SHORT).show()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                RECEIVER_EXPORTED
            )

        }
    }

    private fun downloadTrack(url: String, fileName: String) {
        try {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val request = DownloadManager.Request(Uri.parse(url))

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val finalFileName = if (fileName.endsWith(".mp3", ignoreCase = true)) {
                fileName
            } else {
                "$fileName.mp3"
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, finalFileName)

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

            val downloadId = downloadManager.enqueue(request)
            Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Download error", Toast.LENGTH_LONG).show()
        }
    }
}

fun scheduleDelayedAction(context: Context, targetTime: Long) {
    val workRequest = OneTimeWorkRequestBuilder<DelayedActionWorker>()
        .setInitialDelay(targetTime, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicAppTheme {
        Greeting("Android")
    }
}