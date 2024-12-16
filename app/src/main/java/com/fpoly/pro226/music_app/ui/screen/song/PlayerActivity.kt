package com.fpoly.pro226.music_app.ui.screen.song

import android.content.Context
import android.os.Bundle
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
                    SongScreen(appContainer = appContainer) { targetTime ->
                        if (targetTime > 0) {
                            Toast.makeText(
                                this,
                                "The music will turn off in $targetTime minutes.",
                                Toast.LENGTH_LONG
                            ).show()
                            scheduleDelayedAction(this, targetTime)
                        } else {
                            Toast.makeText(this, "Appointment at least 1 minute", Toast.LENGTH_LONG)
                                .show()
                        }

                    }
                }
            }
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