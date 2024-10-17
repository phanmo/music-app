package com.fpoly.pro226.music_app

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.components.FMusicBottomNavigation
import com.fpoly.pro226.music_app.ui.screen.explore.ExploreScreen
import com.fpoly.pro226.music_app.ui.screen.genre.GenreScreen
import com.fpoly.pro226.music_app.ui.screen.song.SongScreen
import com.fpoly.pro226.music_app.ui.screen.track.TrackScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun FMusicNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = FMusicDestinations.EXPLORE_ROUTE,
    appContainer: AppContainer,
    startPlayerActivity: (tracks: List<Track>, startIndex: Int) -> Unit,
    onLoadTrackList: (track: List<Track>) -> Unit

) {
    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(FMusicDestinations.SONG_ROUTE) {
                SongScreen(viewModel = appContainer.songViewModelFactory.create())
            }

            composable(FMusicDestinations.EXPLORE_ROUTE) {
                ExploreScreen(
                    appContainer,
                    onBack = {
                    },
                    onClick = {
                        navController.navigate("${FMusicDestinations.GENRE_ROUTE}/${it.id}")
                    })
            }

            composable("${FMusicDestinations.GENRE_ROUTE}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: "0"
                GenreScreen(
                    appContainer, id.toInt(),
                    onBack = {
                        navController.popBackStack()
                    },
                    onItemArtistClick = {
                        navController.navigate("${FMusicDestinations.TRACK_ROUTE}/${it.id}")

                    }
                )
            }

            composable("${FMusicDestinations.TRACK_ROUTE}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: "0"
                TrackScreen(
                    id.toInt(),
                    appContainer,
                    onBack = {
                        navController.popBackStack()
                    },
                    onItemClick = { tracks, index ->
                        startPlayerActivity(tracks, index)
                    },
                    onLoadTrackList = {
                       onLoadTrackList(it)
                    }
                    )
            }
        }
        FMusicBottomNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            onItemSelected = {

            })

    }


}