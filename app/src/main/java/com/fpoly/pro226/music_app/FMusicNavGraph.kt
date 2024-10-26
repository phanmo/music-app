package com.fpoly.pro226.music_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.models.TrackDestination
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.components.FMusicBottomNavigation
import com.fpoly.pro226.music_app.ui.screen.explore.ExploreScreen
import com.fpoly.pro226.music_app.ui.screen.genre.GenreScreen
import com.fpoly.pro226.music_app.ui.screen.login.LoginScreen
import com.fpoly.pro226.music_app.ui.screen.register.RegisterScreen
import com.fpoly.pro226.music_app.ui.screen.splash.GuideScreen
import com.fpoly.pro226.music_app.ui.screen.track.TrackScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun FMusicNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = FMusicDestinations.GUIDE_ROUTE,
    appContainer: AppContainer,
    startPlayerActivity: (tracks: List<Track>, startIndex: Int) -> Unit,
    onLoadTrackList: (track: List<Track>) -> Unit

) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isShowBottomNavigation = remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(FMusicDestinations.GUIDE_ROUTE) {
                GuideScreen {
                    navController.navigate(FMusicDestinations.LOGIN_ROUTE)
                }
            }

            composable(FMusicDestinations.LOGIN_ROUTE) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(FMusicDestinations.EXPLORE_ROUTE)
                    },
                    onClickRegister = {
                        navController.navigate(FMusicDestinations.REGISTER_ROUTE)
                    })
            }

            composable(FMusicDestinations.REGISTER_ROUTE) {
                RegisterScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.popBackStack()
                    })
            }

            composable(FMusicDestinations.EXPLORE_ROUTE) {
                ExploreScreen(
                    appContainer,
                    onBack = {
                    },
                    onClickGenreItem = {
                        navController.navigate("${FMusicDestinations.GENRE_ROUTE}/${it.id}")
                    }, onClickRadioItem = { idRadio ->
                        navController.navigate("${FMusicDestinations.TRACK_ROUTE_RADIO}/${idRadio}")

                    })
            }

            composable("${FMusicDestinations.GENRE_ROUTE}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: "0"
                GenreScreen(
                    appContainer, id,
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
                    trackDestination = TrackDestination.Artist(id),
                    appContainer,
                    onBack = {
                        navController.popBackStack()
                    },
                    onItemClick = { tracks, index ->
                        // go to Playback screen
                        startPlayerActivity(tracks, index)
                    },
                    onLoadTrackList = {
                        onLoadTrackList(it)
                    }
                )
            }
            composable("${FMusicDestinations.TRACK_ROUTE_RADIO}/{idRadio}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("idRadio") ?: "0"
                TrackScreen(
                    trackDestination = TrackDestination.Radio(id),
                    appContainer,
                    onBack = {
                        navController.popBackStack()
                    },
                    onItemClick = { tracks, index ->
                        // go to Playback screen
                        startPlayerActivity(tracks, index)
                    },
                    onLoadTrackList = {
                        onLoadTrackList(it)
                    }
                )
            }


        }
        if (isShowBottomNavigation.value) {
            FMusicBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter),
                onItemSelected = {

                })
        }
        LaunchedEffect(navBackStackEntry) {
            val currentRoue = navBackStackEntry?.destination?.route
            isShowBottomNavigation.value = !(currentRoue == FMusicDestinations.REGISTER_ROUTE ||
                    currentRoue == FMusicDestinations.LOGIN_ROUTE ||
                    currentRoue == FMusicDestinations.GUIDE_ROUTE)
        }

    }


}