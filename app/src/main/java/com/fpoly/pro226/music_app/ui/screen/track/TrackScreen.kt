package com.fpoly.pro226.music_app.ui.screen.track

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.components.factory.TrackViewModelFactory
import com.fpoly.pro226.music_app.data.models.TrackDestination
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._1E1E1E_85
import com.fpoly.pro226.music_app.ui.theme._8A9A9D

@Composable
fun TrackScreen(
    trackDestination: TrackDestination,
    appContainer: AppContainer,
    onBack: () -> Unit,
    onItemClick: (track: List<Track>, startIndex: Int) -> Unit,
    onLoadTrackList: (track: List<Track>) -> Unit
) {
    val vm: TrackViewModel = viewModel(
        factory = TrackViewModelFactory(trackDestination, appContainer.deezerRepository)
    )
    val uiState = vm.tracksUiState

    LaunchedEffect(uiState) {
        if (uiState.tracks.isNotEmpty()) {
            Log.d("TrackScreen", "TrackScreen: ${uiState.tracks}")
            onLoadTrackList(uiState.tracks)
        }
    }

    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            TopBar(
                top = uiState.tracks.size.toString(),
                title = if (uiState.tracks.isNotEmpty()) {
                    uiState.tracks[0].artist.name
                } else {
                    ""
                },
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(uiState.tracks.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable {
                            onItemClick(uiState.tracks, index)
                        },
                    colors = CardDefaults.cardColors(containerColor = _1E1E1E_85)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "#${index + 1}",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Spacer(modifier = Modifier.width(18.dp))
                        AsyncImage(
                            model = uiState.tracks[index].album.cover_medium,
                            contentScale = ContentScale.Crop,
                            contentDescription = "Artists avatar",
                            placeholder = painterResource(R.drawable.ic_app),
                            error = painterResource(R.drawable.ic_app),
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(32.dp))
                        Column {
                            Text(
                                text = uiState.tracks[index].title,
                                fontSize = 17.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = uiState.tracks[index].artist.name,
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

    }
}


@Composable
fun TopBar(
    top: String,
    title: String,
    onBack: () -> Unit
) {
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
                    text = title.uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Cyan
                )
                Text(
                    text = "Top $top songs",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Thin,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackScreenPreview() {
    MusicAppTheme {
//        TrackScreen()
    }
}