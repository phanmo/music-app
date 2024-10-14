package com.fpoly.pro226.music_app

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.fpoly.pro226.music_app.components.FMusicApplication
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.components.services.FMusicPlaybackService
import com.fpoly.pro226.music_app.components.services.MediaItemTree
import com.fpoly.pro226.music_app.ui.screen.song.SongViewModel
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    companion object {
        const val URI_EXAMPLE_1 =
            "https://cdn-preview-d.dzcdn.net/stream/c-deda7fa9316d9e9e880d2c6207e92260-10.mp3"
        const val URI_EXAMPLE_2 =
            "https://cdnt-preview.dzcdn.net/api/1/1/f/8/c/0/f8c5dc3837912dba37c9a1ab3170cc3f.mp3?hdnea=exp=1728805721~acl=/api/1/1/f/8/c/0/f8c5dc3837912dba37c9a1ab3170cc3f.mp3*~data=user_id=0,application_id=42~hmac=d7b061b62d97cd04511981ebfefac61aaa7e195f333176818507f12acf01f6b0"
    }

    private val appContainer: AppContainer by lazy {
        (application as FMusicApplication).appContainer
    }
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val subItemMediaList: MutableList<MediaItem> = mutableListOf()

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    private val browser: MediaBrowser?
        get() = if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null

    private var songViewModel : SongViewModel? = null

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
                        Button(onClick = {
                            run {
                                Log.d("TAG", "onCreate: browser = $browser ")
                                val browser = browser ?: return@run
                                browser.setMediaItems(
                                    subItemMediaList,
                                    /* startIndex= */ 0,
                                    /* startPositionMs= */ C.TIME_UNSET
                                )
                                browser.shuffleModeEnabled = false
                                browser.prepare()
                                browser.play()
                                browser.sessionActivity?.send()
                            }
                        }) {
                            Text(text = "Send session to PlayerActivity")
                        }
                    }
                }
            }
        }

        // Onclick button
        // Start the session activity that shows the playback activity. The System UI uses the same
        // intent in the same way to start the activity from the notification.
        // browser?.sessionActivity?.send()

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.notification_permission_denied,
                    Toast.LENGTH_LONG
                ).show()
            }

        }
        if (
            Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), /* requestCode= */
                0
            )
        }
        songViewModel = appContainer.songViewModelFactory.create()

        songViewModel?.getAlbum()

    }

    private fun displayFolder(title: String?) {
        val browser = this.browser ?: return
        val id = "[album]$title"
        val mediaItemFuture = browser.getItem(id)
        val childrenFuture = browser.getChildren(
            id, /* page= */
            0, /* pageSize= */
            Int.MAX_VALUE, /* params= */
            null
        )
        mediaItemFuture.addListener(
            {
//                val title: TextView = findViewById(R.id.folder_description)
//                val result = mediaItemFuture.get()!!
//                title.text = result.value!!.mediaMetadata.title
            },
            ContextCompat.getMainExecutor(this)
        )
        childrenFuture.addListener(
            {
                val result = childrenFuture.get()!!
                val children = result.value!!

                subItemMediaList.clear()
                subItemMediaList.addAll(children)
            },
            ContextCompat.getMainExecutor(this)
        )
    }

    override fun onStart() {
        super.onStart()
        initializeBrowser()
        lifecycleScope.launch {
            songViewModel?.fetchAlbumEvent?.collect{alb->
                alb?.let {
                    browserFuture.addListener({
                        MediaItemTree.initialize(alb)
                        displayFolder(alb.title)
                    }, ContextCompat.getMainExecutor(this@MainActivity))
                }

            }
        }
    }

    override fun onStop() {
        releaseBrowser()
        super.onStop()
    }

    private fun initializeBrowser() {
        browserFuture =
            MediaBrowser.Builder(
                this,
                SessionToken(this, ComponentName(this, FMusicPlaybackService::class.java))
            )
                .buildAsync()
//        browserFuture.addListener({
//            displayFolder()
//                                  }, ContextCompat.getMainExecutor(this))
    }

    private fun releaseBrowser() {
        MediaBrowser.releaseFuture(browserFuture)
    }

}
