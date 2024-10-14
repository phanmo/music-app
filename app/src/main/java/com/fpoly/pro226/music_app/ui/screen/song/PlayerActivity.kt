package com.fpoly.pro226.music_app.ui.screen.song

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.C
import androidx.media3.common.C.TRACK_TYPE_TEXT
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED
import androidx.media3.common.Player.EVENT_TIMELINE_CHANGED
import androidx.media3.common.Player.EVENT_TRACKS_CHANGED
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.fpoly.pro226.music_app.components.FMusicApplication
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.components.services.FMusicPlaybackService
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.guava.await
import java.util.concurrent.TimeUnit


class PlayerActivity : ComponentActivity() {
    companion object {
        const val TAG = "PlayerActivity"
    }
    private val appContainer: AppContainer by lazy {
        (application as FMusicApplication).appContainer
    }

    private lateinit var controllerFuture: ListenableFuture<MediaController>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    FMusicNavGraph(appContainer = appContainer)
                    Column {
                        ExoPlayerView()

                    }
                }
            }
        }
    }

//    private suspend fun initializeController() {
//        controllerFuture =
//            MediaController.Builder(
//                this,
//                SessionToken(this, ComponentName(this, FMusicPlaybackService::class.java)),
//            )
//                .buildAsync()
//        updateMediaMetadataUI()
//        setController()
//    }

    private fun releaseController() {
        MediaController.releaseFuture(controllerFuture)
    }


    private fun updateCurrentPlaylistUI() {
//        if (controller == null) {
//            return
//        }
//        mediaItemList.clear()
//        for (i in 0 until controller.mediaItemCount) {
//            mediaItemList.add(controller.getMediaItemAt(i))
//        }
//        mediaItemListAdapter.notifyDataSetChanged()
    }

    private fun updateMediaMetadataUI() {
//        if (controller == null || controller?.mediaItemCount == 0) {
////            findViewById<TextView>(R.id.media_title).text = getString(R.string.waiting_for_metadata)
////            findViewById<TextView>(R.id.media_artist).text = ""
//            return
//        }
//
//        val mediaMetadata = controller?.mediaMetadata
//        val title: CharSequence = mediaMetadata?.title ?: ""

//        findViewById<TextView>(R.id.media_title).text = title
//        findViewById<TextView>(R.id.media_artist).text = mediaMetadata.artist
    }
}

@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView() {
    // Get the current context
    val context = LocalContext.current
    val controllerFuture = remember {
        MediaController.Builder(
            context,
            SessionToken(context, ComponentName(context, FMusicPlaybackService::class.java))
        ).buildAsync()
    }
    val mediaController = remember { mutableStateOf<MediaController?>(null) }
    LaunchedEffect(controllerFuture) {
        controllerFuture.addListener({
            mediaController.value = controllerFuture.get()
        }, Runnable::run)
    }

    mediaController.value?.let { controller ->
        Log.d(PlayerActivity.TAG, "mediaItemCount = ${controller.mediaItemCount} ")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nút chuyển bài hát trước
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_previous),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        controller.seekToPrevious()
                    }
            )

            // Nút phát hoặc tạm dừng
            Icon(
                painter = painterResource(
                    id = if (controller.isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
//                            isPlaying = !isPlaying
//                            exoPlayer?.playWhenReady = isPlaying
                    }
            )

            // Nút chuyển bài hát tiếp theo
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_next),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        controller.seekToNext()
                    }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Thanh SeekBar
            if (controller.duration > 0) {
                Slider(
                    value = controller.currentPosition.toFloat(),
                    valueRange = 0f..controller.duration.toFloat(),
                    onValueChange = { newValue ->
                        controller.seekTo(newValue.toLong())
//                            currentPosition = newValue.toLong()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Thời gian hiện tại và tổng thời gian
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(controller.currentPosition),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(text = formatTime(controller.duration), fontSize = 14.sp, color = Color.Black)
            }
        }
    }

}

fun formatTime(timeMs: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMs)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMs) % 60
    return String.format("%02d:%02d", minutes, seconds)
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