package com.fpoly.pro226.music_app.ui.screen.song

import android.content.ComponentName
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_IS_PLAYING_CHANGED
import androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.components.services.FMusicPlaybackService
import com.fpoly.pro226.music_app.components.services.MediaItemTree
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.comment.CommentBody
import com.fpoly.pro226.music_app.data.source.network.models.toItemPlaylistBody
import com.fpoly.pro226.music_app.ui.components.InputTextField
import com.fpoly.pro226.music_app.ui.theme.D9D9D9
import com.fpoly.pro226.music_app.ui.theme.FFFFFF_70
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._1E1E1E_85
import com.fpoly.pro226.music_app.ui.theme._7CEEFF
import com.fpoly.pro226.music_app.ui.theme._8A9A9D
import com.fpoly.pro226.music_app.ui.theme._A6F3FF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun SongScreen(
    modifier: Modifier = Modifier,
    appContainer: AppContainer
) {

    val extras = MutableCreationExtras().apply {
        set(SongViewModel.MY_REPOSITORY_KEY, appContainer.fMusicRepository)
        set(SongViewModel.MY_REPOSITORY_KEY_2, appContainer.deezerRepository)
    }
    val vm: SongViewModel = viewModel(
        factory = SongViewModel.provideFactory(),
        extras = extras,
    )

    val uiState = vm.songUiState
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val isShowComment = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val controllerFuture = remember {
        MediaController.Builder(
            context,
            SessionToken(context, ComponentName(context, FMusicPlaybackService::class.java))
        ).buildAsync()
    }
    val mediaController = remember { mutableStateOf<MediaController?>(null) }
    val currentMediaMetadata = remember { mutableStateOf(mediaController.value?.mediaMetadata) }
    val isPlaying = remember { mutableStateOf(false) }

    LaunchedEffect(controllerFuture) {
        controllerFuture.addListener({
            mediaController.value = controllerFuture.get()
            currentMediaMetadata.value = controllerFuture.get().mediaMetadata
            isPlaying.value = controllerFuture.get().playWhenReady
            controllerFuture.get()?.currentMediaItemIndex?.let {
                val currentTrack = MediaItemTree.currentTracks[it]
                vm.getAllComment(currentTrack.id)
            }

        }, Runnable::run)
    }

    LaunchedEffect(Unit) {
        vm.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    var lastMetadata by remember { mutableStateOf<MediaMetadata?>(null) }

    mediaController.value?.let { controller ->
        controller.addListener(
            object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    if (events.contains(EVENT_MEDIA_METADATA_CHANGED)) {
                        currentMediaMetadata.value = controller.mediaMetadata
                    }
                    if (events.contains(EVENT_IS_PLAYING_CHANGED)) {
                        isPlaying.value = player.playWhenReady
                    }
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    if (mediaMetadata != lastMetadata) {
                        try {
                            lastMetadata = mediaMetadata
                            val currentTrack =
                                MediaItemTree.currentTracks[controller.currentMediaItemIndex]
                            vm.getAllComment(currentTrack.id)
                        } catch (e: Exception) {
                            Log.d(
                                "TAG",
                                "onMediaMetadataChanged: Exception =${e.printStackTrace()}"
                            )
                        }

                    }
                }
            }
        )

    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SongTopAppBar(currentMediaMetadata.value?.albumTitle.toString())
        },

        ) { innerPadding ->
        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
            sheetState = sheetState,
            sheetContent = {
                if (!isShowComment.value) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        item {
                            Text(
                                text = "Add to playlist",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(bottom = 8.dp, top = 12.dp)
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        uiState.playListResponse?.data?.let { data ->
                            items(data.size) { index ->
                                Card(
                                    shape = RectangleShape,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp)
                                        .clickable {
                                            mediaController.value?.currentMediaItemIndex?.let {
                                                val currentTrack = MediaItemTree.currentTracks[it]
                                                val itemPlaylistBody =
                                                    currentTrack.toItemPlaylistBody(data[index]._id)
                                                vm.addItemToPlaylist(itemPlaylistBody)
                                            }

                                        },
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column {
                                        Row(
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp, horizontal = 8.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.logotransparent),
                                                contentScale = ContentScale.Crop,
                                                contentDescription = "Artists avatar",
                                                modifier = Modifier
                                                    .size(52.dp)
                                                    .border(
                                                        width = 2.dp,
                                                        color = _00C2CB,
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .clip(RoundedCornerShape(5.dp))
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    color = _00C2CB,
                                                    text = data[index].name,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = "${data[index].count} songs",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.Gray,
                                                    fontSize = 12.sp

                                                )
                                            }
                                            Image(
                                                painter = painterResource(id = R.drawable.baseline_playlist_add_24),
                                                contentDescription = "Add",
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                } else {
                    Box {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(LocalConfiguration.current.screenHeightDp.dp / 1.5f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp, top = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Comments",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                            LazyColumn {
                                vm.songUiState.commentResponse?.data?.let { comments ->
                                    items(comments.size) { index ->
                                        Row(
                                            modifier = modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 12.dp, horizontal = 16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Avatar
                                            AsyncImage(
                                                model = comments[index].avatar,
                                                contentScale = ContentScale.Crop,
                                                contentDescription = "Avatar",
                                                modifier = Modifier
                                                    .size(42.dp)
                                                    .clip(RoundedCornerShape(10.dp)),
                                                placeholder = painterResource(R.drawable.ic_app),
                                                error = painterResource(R.drawable.ic_app)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Start,
                                                ) {
                                                    Text(
                                                        color = _1E1E1E_85,
                                                        text = "${comments[index].username ?: "#anonymous"}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))

                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        Text(
                                                            text = comments[index].getFormatDate(),
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = Color.Gray,
                                                            fontSize = 10.sp

                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))

                                                Text(
                                                    text = comments[index].content,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = _00C2CB,
                                                    fontSize = 12.sp

                                                )
                                            }
                                        }
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(80.dp))
                                }
                            }

                        }
                        CommentSection(Modifier.align(Alignment.BottomCenter)) { content ->
                            mediaController.value?.currentMediaItemIndex?.let {
                                val currentTrack = MediaItemTree.currentTracks[it]
                                vm.addComment(
                                    commentBody = CommentBody(
                                        content = content,
                                        id_track = currentTrack.id,
                                    )
                                )

                            }
                        }

                    }
                }
            }
        ) {
            SongContent(
                innerPadding = innerPadding,
                mediaController,
                currentMediaMetadata.value,
                isPlaying.value,
                openBottomSheet = {
                    scope.launch {
                        isShowComment.value = false
                        sheetState.show()

                    }
                },
                openCommentBottomSheet = {
                    scope.launch {
                        isShowComment.value = true
                        sheetState.show()
                    }
                },
                viewModel = vm

            )
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongTopAppBar(albumName: String?) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "PLAYING FROM PLAYLIST:",
                    fontSize = 9.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = FFFFFF_70,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = albumName ?: "",
                        fontSize = 12.sp,
                        color = _7CEEFF, // Light blue color
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Icon(
                        modifier = Modifier.clickable {

                        },
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.Gray
                    )

                }
            }
        },
        actions = {
            IconButton(onClick = { /* Handle menu click */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black // App bar background color
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSection(modifier: Modifier, onSend: (String) -> Unit) {
    var comment by remember { mutableStateOf("") }
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        focusedLabelColor = Color.Gray,
        unfocusedLabelColor = Color.Gray,
        cursorColor = _00C2CB,
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
        )

        InputTextField(
            textFieldColors = textFieldColors,
            singleLine = true,
            value = comment,
            placeHolder = { Text("Write a comment", color = D9D9D9, fontSize = 13.sp) },
            onValueChange = { comment = it },
            modifier = Modifier
                .weight(1.3f)
                .padding(horizontal = 8.dp)
                .height(40.dp)
                .border(width = 1.dp, shape = RoundedCornerShape(24.dp), color = D9D9D9)

        )

        IconButton(
            onClick = {
                onSend(comment)
                comment = ""
            },
            enabled = comment.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send Comment",
                tint = if (comment.isNotBlank()) _00C2CB else Color.Gray
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongContent(
    innerPadding: PaddingValues,
    mediaController: MutableState<MediaController?>,
    currentMediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    openBottomSheet: () -> Unit,
    openCommentBottomSheet: () -> Unit,
    viewModel: SongViewModel
) {
    var comment by remember { mutableStateOf(TextFieldValue("")) }

    val interactionSource = remember { MutableInteractionSource() }
    val currentPosition = remember { mutableLongStateOf(0L) }
    val duration = remember { mutableLongStateOf(0L) }
    val handler = remember { android.os.Handler(android.os.Looper.getMainLooper()) }
    val state = rememberScrollState()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val sharedPreferences = PreferencesManager(context)
        sharedPreferences.getUserId()?.let { viewModel.getAllPlaylist(it) }
        onDispose { }
    }

    LaunchedEffect(Unit) { state.animateScrollTo(0) }
    val updatePositionRunnable = remember {
        object : Runnable {
            override fun run() {
                mediaController.value?.let { controller ->
                    currentPosition.longValue = controller.currentPosition
                    duration.longValue = if (controller.getDuration() < 0) {
                        0
                    } else {
                        controller.getDuration()
                    }
                }
                handler.postDelayed(this, 100L)
            }
        }
    }
    LaunchedEffect(mediaController) {

        //Ngan
        handler.post(updatePositionRunnable)
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
                .verticalScroll(state),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            // Image
            AsyncImage(
                model = "${currentMediaMetadata?.artworkUri ?: ""}",
                contentScale = ContentScale.Crop,
                contentDescription = "Album Art",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(4.dp))

            )
            Text(
                text = "${currentMediaMetadata?.title ?: "..."}",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 27.dp, start = 24.dp, end = 24.dp)
                    .align(alignment = Alignment.Start)
                    .basicMarquee()
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "${currentMediaMetadata?.artist ?: "..."}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,

                    )
                Row {
                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(R.drawable.share),
                        contentDescription = "null" // decorative element
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.love),
                        contentDescription = "null" // decorative element
                    )
                }
            }
            Slider(
                value = currentPosition.longValue.toFloat(),
                valueRange = 0f..duration.longValue.toFloat(),
                onValueChange = { newValue ->
                    currentPosition.longValue = newValue.toLong()
                },
                onValueChangeFinished = {
                    mediaController.value?.seekTo(currentPosition.longValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .padding(horizontal = 16.dp),
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        modifier = Modifier.size(17.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = _7CEEFF,
                            activeTrackColor = _7CEEFF,
                        )
                    )
                },
                colors = SliderDefaults.colors(
                    thumbColor = _7CEEFF,
                    activeTrackColor = _7CEEFF,
                )
            )


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Timeline(formatTime(currentPosition.longValue))
                Timeline(formatTime(duration.longValue))
            }

            // Media Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    //                openBottomSheet()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_alarm_24),
                        contentDescription = "Next",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = {
                    mediaController.value?.seekToPrevious()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.previous),
                        contentDescription = "Previous",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(_A6F3FF, _00C2CB)
                        ), CircleShape
                    )
                    .clickable {
                        if (isPlaying) {
                            mediaController.value?.pause()
                        } else {
                            mediaController.value?.play()
                        }
                    }) {
                    if (isPlaying) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_pause_24),
                            contentDescription = "Play",
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = "Play",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = {
                    mediaController.value?.seekToNext()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.next),
                        contentDescription = "Next",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = {
                    openBottomSheet()
                }) {
                    Image(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.baseline_playlist_add_24),
                        contentDescription = "Next",
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 24.dp)
                .align(AbsoluteAlignment.BottomLeft)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = _1E1E1E_85,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable {
                        openCommentBottomSheet()
                    },
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.baseline_comment_24),
                    contentDescription = "Comment (${viewModel.songUiState.commentResponse?.data?.size ?: 0})",
                )
                Spacer(modifier = Modifier.width(width = 4.dp))

                Text(
                    text = "Comment (${viewModel.songUiState.commentResponse?.data?.size ?: 0})",
                    color = Color.Gray,
                    fontSize = 12.sp
                )


            }
        }
    }
}

fun formatTime(timeMs: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMs)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMs) % 60
    val value = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    return if (value.length > 6) {
        "00:00"
    } else {
        value
    }
}

@Composable
fun Timeline(value: String) {
    Text(
        text = value,
        color = _8A9A9D,
        fontSize = 12.sp,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
fun SongTopAppBarPreview() {
    MusicAppTheme {
        SongTopAppBar(null)
    }
}

@Preview(showBackground = true)
@Composable
fun SongContentPreview() {
    val extras = MutableCreationExtras().apply {
        set(
            SongViewModel.MY_REPOSITORY_KEY,
            AppContainer(CoroutineScope(Dispatchers.Main)).fMusicRepository
        )
        set(
            SongViewModel.MY_REPOSITORY_KEY_2,
            AppContainer(CoroutineScope(Dispatchers.Main)).deezerRepository
        )
    }
    val vm: SongViewModel = viewModel(
        factory = SongViewModel.provideFactory(),
        extras = extras,
    )
    MusicAppTheme {
        Scaffold { innerPadding ->
            SongContent(innerPadding, remember { mutableStateOf(null) }, null, false, {}, {}, vm)
        }
    }
}