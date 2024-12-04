package com.fpoly.pro226.music_app.ui.screen.main.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.UserInfo
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.theme.FFFFFF94
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._1E1E1E_85
import com.fpoly.pro226.music_app.ui.theme._436369
import com.fpoly.pro226.music_app.ui.theme._8A9A9D

@Composable
fun HomeScreen(
    onLoadTrackList: (track: List<Track>) -> Unit,
    appContainer: AppContainer,
    onClickItemArtist: (String) -> Unit,
    onClickItemTrack: (tracks: List<Track>, startIndex: Int) -> Unit,
    onClickItemPlaylist: (String) -> Unit,
    onClickProfile: () -> Unit
) {
    val extras = MutableCreationExtras().apply {
        set(HomeViewModel.MY_REPOSITORY_KEY, appContainer.deezerRepository)
    }
    val vm: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory,
        extras = extras,
    )
    val uiState = vm.homeUiState
    val context = LocalContext.current

    DisposableEffect(Unit) {
        PreferencesManager(context).getUser()?.let { user ->
            vm.setCurrentUser(user)
        }
        onDispose { }
    }

    LaunchedEffect(uiState) {
        if (uiState.tracks?.data?.isNotEmpty() == true) {
            Log.d("TrackScreen", "TrackScreen: ${uiState.tracks}")
            onLoadTrackList(uiState.tracks.data)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF102B2D),
                        Color(0xFF000000),
                        Color(0xFF000000),
                    )
                )
            )
    ) {
        CustomTopAppBar(onClickProfile, uiState.userInfo)
        LazyColumn(modifier = Modifier.padding(top = 68.dp, start = 16.dp, end = 16.dp)) {
            item {
                Text(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    text = "Top Artists ",
                    modifier = Modifier.padding(bottom = 10.dp)

                )
            }
            uiState.artists?.data?.let { artists ->
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = (114 * 6).dp),
                    ) {
                        items(artists.size) { index ->
                            Card(
                                modifier = Modifier
                                    .padding(end = 8.dp, top = 6.dp, bottom = 6.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        onClickItemArtist(artists[index].id)
                                    },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = _436369)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = artists[index].picture,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Album Art",
                                        modifier = Modifier
                                            .size(55.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        placeholder = painterResource(R.drawable.ic_app),
                                        error = painterResource(R.drawable.ic_app)
                                    )
                                    Text(
                                        text = artists[index].name,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .fillMaxWidth()
                                    )
                                }

                            }
                        }
                    }
                }
            }

            item {
                Text(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    text = "Trending Playlists",
                    modifier = Modifier.padding(top = 35.dp)
                )
            }
            uiState.playlists?.data?.let { playlists ->
                item {
                    LazyRow {
                        items(playlists.size) { index ->
                            Card(
                                modifier = Modifier
                                    .padding(top = 16.dp, bottom = 16.dp, end = 12.dp)
                                    .fillMaxWidth()
                                    .shadow(
                                        10.dp,
//                                    shape = RoundedCornerShape(8.dp),
                                    )
                                    .clickable {
                                        onClickItemPlaylist(playlists[index].id.toString())
                                    },
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Gray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(130.dp)
                                ) {
                                    AsyncImage(
                                        model = playlists[index].picture_medium,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Album Art",
                                        modifier = Modifier
                                            .size(130.dp)
                                            .clip(RoundedCornerShape(24.dp)),
                                        placeholder = painterResource(R.drawable.ic_app),
                                        error = painterResource(R.drawable.ic_app)
                                    )
                                }

                            }
                        }
                    }
                }

            }
            item {
                Text(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    text = "Top 10 songs",
                    modifier = Modifier.padding(top = 35.dp, bottom = 10.dp)
                )
            }
            uiState.tracks?.data?.let { tracks ->
                items(tracks.size) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                onClickItemTrack(tracks, index)
                            },
                        colors = CardDefaults.cardColors(containerColor = _1E1E1E_85)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = "#${index + 1}",
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            AsyncImage(
                                model = tracks[index].album?.cover_medium,
                                contentScale = ContentScale.Crop,
                                contentDescription = "Artists avatar",
                                placeholder = painterResource(R.drawable.ic_app),
                                error = painterResource(R.drawable.ic_app),
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = tracks[index].title,
                                    fontSize = 17.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = tracks[index].artist?.name ?: "",
                                    fontSize = 14.sp,
                                    color = _8A9A9D,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }

        }

    }

}


@Composable
fun CustomTopAppBar(onClickProfile: () -> Unit, userInfo: UserInfo?) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "${userInfo?.avatar}",
                    contentDescription = "Profile Picture",
                    placeholder = painterResource(R.drawable.ic_app),
                    error = painterResource(R.drawable.ic_app),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable {
                            onClickProfile()
                        },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Welcome back!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${userInfo?.name}",
                        fontSize = 12.sp,
                        color = _00C2CB,
                    )
                }
            }
        },
        actions = {
            Image(
                painter = painterResource(id = R.drawable.coin_3d),
                contentDescription = "Notification",
                modifier = Modifier.size(28.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()

    )
}

@Preview(showBackground = true)
@Composable
fun CustomTopAppBarPreview() {
    MusicAppTheme {
        CustomTopAppBar({}, null)
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MusicAppTheme {
//        HomeScreen()
    }
}