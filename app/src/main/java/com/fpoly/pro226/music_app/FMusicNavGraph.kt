package com.fpoly.pro226.music_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.ui.components.FMusicBottomNavigation
import com.fpoly.pro226.music_app.ui.screen.song.SongScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun FMusicNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = FMusicDestinations.SONG_ROUTE,
    appContainer: AppContainer
) {
    Box (modifier = modifier.fillMaxSize()){
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(FMusicDestinations.SONG_ROUTE) {
                SongScreen(viewModel = appContainer.songViewModelFactory.create())
            }
        }
        FMusicBottomNavigation(modifier = Modifier.align(Alignment.BottomCenter))

    }


}