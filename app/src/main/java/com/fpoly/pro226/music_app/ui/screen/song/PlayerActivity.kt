package com.fpoly.pro226.music_app.ui.screen.song

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.session.MediaController
import com.fpoly.pro226.music_app.components.FMusicApplication
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.google.common.util.concurrent.ListenableFuture


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
                    SongScreen(appContainer.songViewModelFactory.create())
                }
            }
        }
    }
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