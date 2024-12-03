package com.fpoly.pro226.music_app.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.fpoly.pro226.music_app.FMusicDestinations
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.network.models.Genre
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.components.FMusicBottomNavigation
import com.fpoly.pro226.music_app.ui.screen.main.explore.ExploreScreen
import com.fpoly.pro226.music_app.ui.screen.main.home.HomeScreen
import com.fpoly.pro226.music_app.ui.screen.main.library.LibraryScreen
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme

@Composable
fun MainScreen(
    onLoadTrackList: (track: List<Track>) -> Unit,
    navController: NavHostController,
    appContainer: AppContainer,
    onClickGenreItem: (Genre) -> Unit,
    onBack: () -> Unit,
    onClickRadioItem: (id: String) -> Unit,
    startPlayerActivity: (tracks: List<Track>, startIndex: Int) -> Unit,
    onPlayGame: () -> Unit,
    onAddPlaylist: () -> Unit,
    onFavorite: () -> Unit,
    pagerState: PagerState,
    selectedItem: MutableIntState,
    onClickProfile: () -> Unit

) {
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
                        },
                        onClickProfile = onClickProfile
                    )

                    1 -> ExploreScreen(
                        appContainer,
                        onClickRadioItem = onClickRadioItem,
                        onClickGenreItem = onClickGenreItem,
                        onBack = onBack,
                        startPlayerActivity = startPlayerActivity,
                        onLoadTrackList = onLoadTrackList
                    )

                    2 -> LibraryScreen(
                        onFavorite = {
                            onFavorite()
                        },
                        onPlayGame = {
                            onPlayGame()
                        },
                        onSettingClick = {
                            navController.navigate(FMusicDestinations.SETTING_ROUTE)
                        },
                        onAddPlaylist = {
                            onAddPlaylist()
                        }
                    )
                }
            }
            FMusicBottomNavigation(
                selectedItem = selectedItem,
                modifier = Modifier.align(Alignment.BottomCenter),
                onItemSelected = { index ->
                    pagerState.requestScrollToPage(index)

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