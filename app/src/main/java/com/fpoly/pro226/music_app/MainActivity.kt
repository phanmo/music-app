package com.fpoly.pro226.music_app

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.fpoly.pro226.music_app.components.FMusicApplication
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.components.services.FMusicPlaybackService
import com.fpoly.pro226.music_app.components.services.MediaItemTree
import com.fpoly.pro226.music_app.ui.screen.song.SongViewModel
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.google.common.util.concurrent.ListenableFuture

class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val appContainer: AppContainer by lazy {
        (application as FMusicApplication).appContainer
    }
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val subItemMediaList: MutableList<MediaItem> = mutableListOf()

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    private val browser: MediaBrowser?
        get() = if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null

    private var songViewModel: SongViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                // A surface container using the 'background' color from the theme

                FMusicNavGraph(
                    appContainer = appContainer,
                    startPlayerActivity = { tracks, startIndex ->
                        run {
                            Log.d("TAG", "onCreate: browser = ${tracks[startIndex].title} ")
                            // Start the session activity that shows the playback activity. The System UI uses the same
                            // intent in the same way to start the activity from the notification.
                            // browser?.sessionActivity?.send()
                            val browser = browser ?: return@run
                            browser.setMediaItems(
                                subItemMediaList,
                                /* startIndex= */ startIndex,
                                /* startPositionMs= */ C.TIME_UNSET
                            )
                            browser.shuffleModeEnabled = false
                            browser.prepare()
                            browser.play()
                            browser.sessionActivity?.send()
                        }
                    },
                    onLoadTrackList = {
                        val album = it[0].album
                        browserFuture.addListener({
                            MediaItemTree.initialize(album, it)
                            displayFolder(album.title)
                        }, ContextCompat.getMainExecutor(this@MainActivity))
                    }
                )
            }
        }

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

//        songViewModel?.getAlbum()

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
                val result = childrenFuture.get()
                if (result != null) {
                    val children = result.value
                    children?.let {
                        subItemMediaList.clear()
                        subItemMediaList.addAll(it)
                    }
                }

            },
            ContextCompat.getMainExecutor(this)
        )
    }

    override fun onStart() {
        super.onStart()
        initializeBrowser()
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
