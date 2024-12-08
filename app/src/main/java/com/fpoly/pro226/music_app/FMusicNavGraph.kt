package com.fpoly.pro226.music_app

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.models.TrackDestination
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.screen.favorite.FavoriteScreen
import com.fpoly.pro226.music_app.ui.screen.game.GameScreen
import com.fpoly.pro226.music_app.ui.screen.genre.GenreScreen
import com.fpoly.pro226.music_app.ui.screen.login.LoginScreen
import com.fpoly.pro226.music_app.ui.screen.main.MainScreen
import com.fpoly.pro226.music_app.ui.screen.myplaylist.MyPlaylistScreen
import com.fpoly.pro226.music_app.ui.screen.playlist.PlaylistScreen
import com.fpoly.pro226.music_app.ui.screen.profile.ChangePasswordScreen
import com.fpoly.pro226.music_app.ui.screen.profile.EditProfileScreen
import com.fpoly.pro226.music_app.ui.screen.settings.SettingScreen
import com.fpoly.pro226.music_app.ui.screen.ranking.RankingScreen
import com.fpoly.pro226.music_app.ui.screen.register.RegisterScreen
import com.fpoly.pro226.music_app.ui.screen.splash.GuideScreen
import com.fpoly.pro226.music_app.ui.screen.track.TrackScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun FMusicNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String,
    appContainer: AppContainer,
    startPlayerActivity: (tracks: List<Track>, startIndex: Int) -> Unit,
    onLoadTrackList: (track: List<Track>) -> Unit,
    onGaming: () -> Unit,

    ) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val selectedItem = remember { mutableIntStateOf(0) }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(FMusicDestinations.MAIN_ROUTE) {
            MainScreen(
                onLoadTrackList = onLoadTrackList,
                startPlayerActivity = startPlayerActivity,
                navController = navController,
                appContainer = appContainer,
                onClickRadioItem = { idRadio ->
                    navController.navigate("${FMusicDestinations.TRACK_ROUTE_RADIO}/${idRadio}")
                },
                onClickGenreItem = {
                    navController.navigate("${FMusicDestinations.GENRE_ROUTE}/${it.id}")
                },
                onBack = {},
                onPlayGame = {
                    onGaming()
                    navController.navigate(FMusicDestinations.RAKING_ROUTE)
                },
                onAddPlaylist = {
                    navController.navigate(FMusicDestinations.MY_PLAYLIST_ROUTE)
                },
                onFavorite = {
                    navController.navigate(FMusicDestinations.FAVORITE_ROUTE)
                },
                pagerState = pagerState,
                selectedItem = selectedItem,
                onClickProfile = {

                }
            )
        }
        composable(FMusicDestinations.RAKING_ROUTE) {
            RankingScreen(
                appContainer = appContainer,
                onBack = {
                    navController.popBackStack()
                },
                onStart = {
                    navController.navigate(FMusicDestinations.GAME_ROUTE)
                })
        }

        composable(FMusicDestinations.GUIDE_ROUTE) {
            GuideScreen {
                navController.navigate(FMusicDestinations.LOGIN_ROUTE) {
                    popUpTo(FMusicDestinations.GUIDE_ROUTE) { inclusive = true }
                }
            }
        }

        composable(FMusicDestinations.FAVORITE_ROUTE) {
            FavoriteScreen(
                appContainer = appContainer,
                onBack = { navController.popBackStack() },
                onItemClick = { tracks, startIndex ->
                    startPlayerActivity(tracks, startIndex)
                },
                onLoadTrackList = {
                    onLoadTrackList(it)
                },
            )
        }

        composable(FMusicDestinations.MY_PLAYLIST_ROUTE) {
            MyPlaylistScreen(
                appContainer.fMusicRepository,
                onClickItemPlaylist = { id ->
                    navController.navigate("${FMusicDestinations.MY_PLAYLIST_DETAIL_ROUTE}/${id}")
                },
                onBack = {
                    navController.popBackStack()

                })
        }

        composable(FMusicDestinations.GAME_ROUTE) {
            GameScreen(appContainer) {
                navController.popBackStack()
            }
        }

        composable(FMusicDestinations.LOGIN_ROUTE) {
            LoginScreen(
                fMusicRepository = appContainer.fMusicRepository,
                onLoginSuccess = {
                    navController.navigate(FMusicDestinations.MAIN_ROUTE) {
                        popUpTo(FMusicDestinations.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onClickRegister = {
                    navController.navigate(FMusicDestinations.REGISTER_ROUTE)
                })
        }

        composable(FMusicDestinations.REGISTER_ROUTE) {
            RegisterScreen(
                fMusicRepository = appContainer.fMusicRepository,
                onBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(FMusicDestinations.LOGIN_ROUTE) {
                        popUpTo(FMusicDestinations.REGISTER_ROUTE) { inclusive = true }
                    }
                })
        }

        composable(FMusicDestinations.CHANGE_PASSWORD_ROUTE) {
            ChangePasswordScreen(
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        composable(FMusicDestinations.SETTING_ROUTE) {
            SettingScreen(
                onLogoutClick = {
                    navController.navigate(FMusicDestinations.LOGIN_ROUTE) {
                        popUpTo(FMusicDestinations.SETTING_ROUTE) { inclusive = true }
                        popUpTo(FMusicDestinations.MAIN_ROUTE) { inclusive = true }
                    }
                },
                onChangePassword = {
                    navController.navigate(FMusicDestinations.CHANGE_PASSWORD_ROUTE)

                },
                onEditProfile = {
                    navController.navigate(FMusicDestinations.EDIT_PROFILE_ROUTE)
                },
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        composable(FMusicDestinations.EDIT_PROFILE_ROUTE) {
            EditProfileScreen(
                appContainer = appContainer,
                onBack = {
                    navController.popBackStack()
                },
            )
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

        composable("${FMusicDestinations.PLAYLIST_ROUTE}/{idPlaylist}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("idPlaylist") ?: "0"
            PlaylistScreen(
                onBack = {
                    navController.popBackStack()
                },
                startPlayerActivity = startPlayerActivity,
                appContainer = appContainer,
                idPlaylist = id,
                onLoadTrackList = {
                    onLoadTrackList(it)
                }
            )
        }


        composable("${FMusicDestinations.MY_PLAYLIST_DETAIL_ROUTE}/{idPlaylist}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("idPlaylist") ?: "0"
            PlaylistScreen(
                onBack = {
                    navController.popBackStack()
                },
                startPlayerActivity = startPlayerActivity,
                appContainer = appContainer,
                idPlaylist = id,
                onLoadTrackList = {
                    onLoadTrackList(it)
                },
                isMyPlaylist = true
            )
        }
    }

}