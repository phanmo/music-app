package com.fpoly.pro226.music_app.ui.screen.ranking

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.ranking.RankingUser
import com.fpoly.pro226.music_app.ui.components.LoadingDialog
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._00C2CB_80
import com.fpoly.pro226.music_app.ui.theme._7CEEFF
import com.fpoly.pro226.music_app.ui.theme._A6F3FF

@Composable
fun RankingScreen(onBack: () -> Unit, onStart: () -> Unit, appContainer: AppContainer) {
    val extras = MutableCreationExtras().apply {
        set(RankingViewModel.MY_REPOSITORY_KEY, appContainer.fMusicRepository)
    }
    val context = LocalContext.current
    val vm: RankingViewModel = viewModel(
        factory = RankingViewModel.provideFactory(PreferencesManager(context).getUserId()),
        extras = extras,
    )

    DisposableEffect(Unit) {
        vm.getRanking()
        vm.getCoin()
        onDispose {

        }

    }

    val uiState = vm.rankingUiState

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 24.dp)
                    .clip(CircleShape),
                containerColor = Color.Transparent,
                onClick = { onStart() },
            ) {
                CircularButton(modifier = Modifier)

            }

        }) { innerPadding ->
        if (vm.rankingUiState.isLoading) {
            LoadingDialog(onDismiss = { })
        }
        uiState.rankingResponse?.data?.let { ranking ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Image(
                    painter = painterResource(R.drawable.background_ranking),
                    contentDescription = "header game",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    TopAppBar(total = "${uiState.coinTotal}") {
                        onBack()
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(end = 196.dp)
                                .fillMaxHeight()

                        ) {
                            Image(
                                painter = painterResource(R.drawable.rank_2),
                                contentDescription = "2",
                                contentScale = ContentScale.None,
                                modifier = Modifier.padding(top = 100.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 196.dp)
                            ) {
                                Text(
                                    text = "${ranking[1].coin}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,

                                    )
                                Image(
                                    painter = painterResource(id = R.drawable.coin_3d),
                                    contentDescription = "Coin",
                                    modifier = Modifier
                                        .size(24.dp)

                                )
                            }
                            AsyncImage(
                                model = ranking[1].avatar,
                                contentDescription = "Profile Picture",
                                placeholder = painterResource(R.drawable.cuteboy),
                                error = painterResource(R.drawable.cuteboy),
                                modifier = Modifier
                                    .padding(top = 50.dp, end = 24.dp)
                                    .size(56.dp)
                                    .background(Color.Gray, shape = CircleShape)
                                    .align(Alignment.TopCenter)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxHeight()
                        ) {
                            Image(
                                painter = painterResource(R.drawable.rank_1),
                                contentDescription = "1",
                                contentScale = ContentScale.None,
                                modifier = Modifier.padding(top = 62.dp)

                            )
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 196.dp)
                            ) {
                                Text(
                                    text = "${ranking[0].coin}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,

                                    )
                                Image(
                                    painter = painterResource(id = R.drawable.coin_3d),
                                    contentDescription = "Coin",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            AsyncImage(
                                model = ranking[0].avatar,
                                contentDescription = "Profile Picture",
                                placeholder = painterResource(R.drawable.cuteboy),
                                error = painterResource(R.drawable.cuteboy),
                                modifier = Modifier
                                    .padding(bottom = 50.dp, end = 24.dp)
                                    .size(56.dp)
                                    .background(Color.Gray, shape = CircleShape)
                                    .align(Alignment.TopCenter)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(start = 180.dp)
                                .fillMaxHeight()
                        ) {
                            Image(
                                painter = painterResource(R.drawable.rank_3),
                                contentDescription = "3",
                                contentScale = ContentScale.None,
                                modifier = Modifier.padding(top = 100.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 196.dp)
                            ) {
                                Text(
                                    text = "${ranking[2].coin}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,

                                    )
                                Image(
                                    painter = painterResource(id = R.drawable.coin_3d),
                                    contentDescription = "Coin",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            AsyncImage(
                                model = ranking[2].avatar,
                                contentDescription = "Profile Picture",
                                placeholder = painterResource(R.drawable.cuteboy),
                                error = painterResource(R.drawable.cuteboy),
                                modifier = Modifier
                                    .padding(top = 50.dp, start = 24.dp)
                                    .size(56.dp)
                                    .background(Color.Gray, shape = CircleShape)
                                    .align(Alignment.TopCenter)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color.White)
                        .padding(vertical = 8.dp)
                        .height(
                            LocalConfiguration.current.screenHeightDp.dp / 1.8f
                        )
                        .align(Alignment.BottomCenter)
                ) {
                    ranking.let {
                        items(it.size) { index ->
                            if (index >= 3) {
                                RankingItem(index + 1, it[index])
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun RankingItem(index: Int, rankingUser: RankingUser) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp, start = 16.dp, end = 16.dp),
    ) {
        Text(
            text = "$index",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            AsyncImage(
                model = rankingUser.avatar,
                contentDescription = "Profile Picture",
                placeholder = painterResource(R.drawable.cuteboy),
                error = painterResource(R.drawable.cuteboy),
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Gray, shape = CircleShape)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = rankingUser.name,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${rankingUser.coin}",
                color = Color.Black,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp
            )
            Image(
                painter = painterResource(id = R.drawable.coin_3d),
                contentDescription = "Coin",
                modifier = Modifier
                    .size(24.dp)

            )

        }
    }

}

@Composable
fun TopAppBar(total: String?, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
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

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$total",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Image(
            painter = painterResource(id = R.drawable.coin_3d),
            contentDescription = "Coin",
            modifier = Modifier
                .size(44.dp)

        )
    }
}

@Composable
fun CircularButton(modifier: Modifier) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val outerCircleColor by animateColorAsState(
        targetValue = if (isPressed) _7CEEFF else _00C2CB_80,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    Box(
        modifier = modifier
            .size(80.dp)
            .background(outerCircleColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(_A6F3FF, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Text "Start"
            Image(
                colorFilter = ColorFilter.tint(_00C2CB),
                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                contentDescription = "Coin",
                modifier = Modifier
                    .size(20.dp)

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingScreenPreview() {
    MusicAppTheme {
        RankingScreen({}, {}, appContainer = AppContainer(rememberCoroutineScope()))
    }
}