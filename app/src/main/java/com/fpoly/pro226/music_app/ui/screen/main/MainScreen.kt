package com.fpoly.pro226.music_app.ui.screen.main

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.fpoly.pro226.music_app.FMusicDestinations
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.network.models.Genre
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.components.FMusicBottomNavigation
import com.fpoly.pro226.music_app.ui.screen.main.home.HomeScreen
import com.fpoly.pro226.music_app.ui.screen.main.explore.ExploreScreen
import com.fpoly.pro226.music_app.ui.screen.main.library.LibraryScreen
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    onLoadTrackList: (track: List<Track>) -> Unit,
    navController: NavHostController,
    appContainer: AppContainer,
    onClickGenreItem: (Genre) -> Unit,
    onBack: () -> Unit,
    onClickRadioItem: (id: String) -> Unit,
    startPlayerActivity: (tracks: List<Track>, startIndex: Int) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                selectedTabIndex = page
                when (page) {
                    0 -> HomeScreen(
                        appContainer = appContainer,
                        onClickItemArtist = { id ->
                            navController.navigate("${FMusicDestinations.TRACK_ROUTE}/${id}")
                        },
                        onClickItemTrack = startPlayerActivity,
                        onLoadTrackList = onLoadTrackList,
                        onClickItemPlaylist = { id ->
                            navController.navigate("${FMusicDestinations.PLAYLIST_ROUTE}/${id}")
                        }
                    )

                    1 -> ExploreScreen(
                        appContainer,
                        onClickRadioItem = onClickRadioItem,
                        onClickGenreItem = onClickGenreItem,
                        onBack = onBack
                    )

                    2 -> LibraryScreen()
                }
            }
            FMusicBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter),
                onItemSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index, animationSpec = tween())
                    }
                })
        }
    }


}

@Preview(showBackground = true)
@Composable
fun TrackScreenPreview() {
    MusicAppTheme {
//        HomeScreen()
    }
}