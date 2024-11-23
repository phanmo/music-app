package com.fpoly.pro226.music_app.ui.screen.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.network.models.Playlist
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.theme.Black
import com.fpoly.pro226.music_app.ui.theme.FFFFFF_70
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme

@Composable
fun PlaylistScreen(
    onBack: () -> Unit,
    startPlayerActivity: (tracks: List<Track>, startIndex: Int) -> Unit,
    appContainer: AppContainer,
    idPlaylist: String,
    onLoadTrackList: (track: List<Track>) -> Unit,
    isMyPlaylist: Boolean = false

) {
    val extras = MutableCreationExtras().apply {
        set(PlaylistViewModel.MY_REPOSITORY_KEY_2, appContainer.deezerRepository)
        set(PlaylistViewModel.MY_REPOSITORY_KEY, appContainer.fMusicRepository)
    }

    val vm: PlaylistViewModel = viewModel(
        factory = PlaylistViewModel.provideFactory(
            idPlaylist = idPlaylist,
            isMyPlaylist = isMyPlaylist
        ),
        extras = extras
    )

    val uiState = vm.playlistUiState

    LaunchedEffect(uiState) {
        if (uiState.tracks?.data?.isNotEmpty() == true) {
            onLoadTrackList(uiState.tracks.data)
        }
    }

    Scaffold(
        backgroundColor = Color.Black,
        topBar = { TopBar(onBack) }
    )
    { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(
                innerPadding
            )
        ) {
            item {
                AvatarPlaylist(uiState.playlist)
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
            uiState.tracks?.data?.let { tracks ->
                items(tracks.size) { index ->
                    Card(
                        shape = RectangleShape, // This removes the corner radius
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable {
                                startPlayerActivity(tracks, index)
                            },
                        colors = CardDefaults.cardColors(containerColor = Black)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 8.dp)
                        ) {
                            AsyncImage(
                                model = tracks[index].album?.cover_medium,
                                contentScale = ContentScale.Crop,
                                contentDescription = "Artists avatar",
                                placeholder = painterResource(R.drawable.ic_app),
                                error = painterResource(R.drawable.ic_app),
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    color = Color.White,
                                    text = tracks[index].title,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = tracks[index].artist?.name ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    fontSize = 12.sp

                                )
                            }
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun AvatarPlaylist(playlist: Playlist?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = playlist?.picture_xl,
            contentScale = ContentScale.Crop,
            contentDescription = "Artists avatar",
            placeholder = painterResource(R.drawable.ic_app),
            error = painterResource(R.drawable.ic_app),
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .padding(horizontal = 60.dp, vertical = 24.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = playlist?.title ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee(),
        )
        Text(
            text = playlist?.description ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = FFFFFF_70,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center

        )
    }
}

@Composable
fun TopBar(onBack: () -> Unit) {
    TopAppBar(backgroundColor = Color.Black) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_24),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onBack()
                    }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "FROM \"PLAYLISTS\"",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraLight,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlaylistScreenPreview() {
    MusicAppTheme {
//        PlaylistScreen(onBack = {}, onItemClick = {})
    }
}