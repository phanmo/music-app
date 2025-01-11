package com.fpoly.pro226.music_app.ui.screen.downloaded

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.screen.favorite.TopBar
import com.fpoly.pro226.music_app.ui.theme._1E1E1E_85
import com.fpoly.pro226.music_app.ui.theme._8A9A9D

@Composable
fun DownloadedScreen(
    appContainer: AppContainer,
    onBack: () -> Unit,
    onItemClick: (track: List<Track>, startIndex: Int) -> Unit,
) {
    val extras = MutableCreationExtras().apply {
        set(DownloadedViewModel.MY_REPOSITORY_KEY, appContainer.fMusicRepository)
    }
    val context = LocalContext.current

    val vm: DownloadedViewModel = viewModel(
        factory = DownloadedViewModel.provideFactory(PreferencesManager(context).getUserId()),
        extras = extras,
    )
    val uiState = vm.downloadedUiState

    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            TopBar(
                top = uiState.tracks.size.toString(),
                title = "Downloaded",
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
                            model = uiState.tracks[index].album?.cover_medium,
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
                                text = uiState.tracks[index].artist?.name ?: "",
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
