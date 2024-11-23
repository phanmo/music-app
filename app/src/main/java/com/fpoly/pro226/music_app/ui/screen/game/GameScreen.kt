package com.fpoly.pro226.music_app.ui.screen.game

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.ui.components.LoadingDialog
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameScreen(appContainer: AppContainer, onBack: () -> Unit) {
    val extras = MutableCreationExtras().apply {
        set(GameViewModel.MY_REPOSITORY_KEY, appContainer.fMusicRepository)
        set(GameViewModel.MY_REPOSITORY_KEY_2, appContainer.deezerRepository)
    }
    val context = LocalContext.current

    val vm: GameViewModel = viewModel(
        factory = GameViewModel.provideFactory(PreferencesManager(context).getUserId()),
        extras = extras,
    )

    val uiState = vm.gameUiState
    var isReady by remember { mutableStateOf(false) }
    var songDuration by remember { mutableStateOf(0L) }
    var progress by remember { mutableStateOf(1f) }
    var currentPosition by remember { mutableStateOf(0L) }
    var selectedOption by remember { mutableStateOf("") }
    var startAnimation by remember { mutableStateOf(false) }
    var inCorrect by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.musiceffect))
    val progressLottie by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isReady,
    )

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        vm.enableSelectAnswer()
                        inCorrect = true
                        songDuration = this@apply.duration
                    }
                    isReady = state == Player.STATE_READY
                    Log.d("TAG", "onPlaybackStateChanged: state = $state")
                }
            })
        }
    }

    LaunchedEffect(isReady) {
        while (isReady) {
            currentPosition = exoPlayer.currentPosition
            progress = if (songDuration > 0) 1f - (currentPosition.toFloat() / songDuration) else 1f
            Log.d("TAG", "GameScreen: $currentPosition")
            delay(100L)
        }
    }

    LaunchedEffect(uiState.trackAnswer) {
        uiState.trackAnswer?.let { track ->
            Log.d("TAG", "GameScreen: track = ${track.preview}")
            val mediaItem = MediaItem.fromUri(track.preview)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.background(Color.Black)) {
        Image(
            painter = painterResource(R.drawable.header_game),
            contentDescription = "header game",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
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
                    text = "${uiState.coinTotal}",
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
            Spacer(modifier = Modifier.height(70.dp))
            // Question
            Box {
                Column {
                    Spacer(modifier = Modifier.height(30.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(170.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(20.dp),
                            )
                            .background(Color.White, shape = RoundedCornerShape(20.dp))
                            .padding(top = 48.dp)
                    ) {
                        Column {
                            Text(
                                text = "Guess the name of the song?",
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                style = TextStyle(color = Color.Black),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LottieAnimation(
                                composition, progressLottie,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .clickable {
                                        vm.nextQuestion()
                                    }
                            ) {
                                Text(
                                    text = "Skip",
                                    color = Color.Black,
                                    fontWeight = FontWeight.ExtraLight,
                                    fontSize = 18.sp
                                )
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(_00C2CB),
                                    painter = painterResource(id = R.drawable.arrow_next_question),
                                    contentDescription = "Next",
                                )
                            }
                        }
                    }
                }
                // Progress and Score
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(67.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                            )
                            .background(color = Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center

                    ) {
                        if (songDuration > 0) {
                            CircularProgressIndicator(
                                progress = progress,
                                modifier = Modifier.size(56.dp),
                                color = _00C2CB,
                                strokeWidth = 4.dp
                            )
                            Text(
                                text = "${((songDuration - currentPosition) / 1000).toInt()}",
                                color = _00C2CB,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

            }

            Spacer(modifier = Modifier.height(32.dp))

            // Options
            val options = vm.gameUiState.tracksQuestion.map { track ->
                track
            }
            Column {
                options.forEach { track ->
                    OptionButton(
                        isCorrect = !inCorrect,
                        option = track.title,
                        isSelected = selectedOption == track.title,
                        onClick = {
                            selectedOption = track.title
                            if (vm.selectAnswer(track.id)) {
                                startAnimation = true
                                inCorrect = false
                            } else {
                                inCorrect = true
                            }
                        },
                        gameViewModel = vm
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    val coinCount = 1
    val showCoins = remember { mutableStateListOf(*Array(coinCount) { false }) }

    val startX = 16.dp
    val startY = LocalConfiguration.current.screenHeightDp.dp - 100.dp
    val endX = LocalConfiguration.current.screenWidthDp.dp - 50.dp
    val endY = 16.dp
    val coinPositions = remember {
        List(coinCount) { Animatable(startX.value) to Animatable(startY.value) }
    }

    LaunchedEffect(startAnimation) {
        if (startAnimation) {
            for (i in 0 until coinCount) {
                showCoins[i] = true

                val (xAnim, yAnim) = coinPositions[i]
                launch {
                    xAnim.animateTo(endX.value, animationSpec = tween(durationMillis = 1000))
                }
                yAnim.animateTo(endY.value, animationSpec = tween(durationMillis = 1000))

                showCoins[i] = false
                // Reset position
                xAnim.snapTo(startX.value)
                yAnim.snapTo(startY.value)

                delay(100)
            }

            startAnimation = false
        }
    }

    coinPositions.forEachIndexed { index, (xAnim, yAnim) ->
        if (showCoins[index]) {
            Image(
                painter = painterResource(id = R.drawable.coin_3d),
                contentDescription = "Coin $index",
                modifier = Modifier
                    .offset(x = xAnim.value.dp, y = yAnim.value.dp)
                    .size(50.dp)
            )
        }
    }
    if (vm.gameUiState.isLoading) {
        LoadingDialog(onDismiss = { })
    }
}


@Composable
fun OptionButton(
    option: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit,
    gameViewModel: GameViewModel
) {
    val backgroundColor = if (isSelected) Color.White else _00C2CB
    val textColor = if (isSelected) _00C2CB else Color.White
    val borderColor = if (isSelected) {
        if (!isCorrect) {
            Color.Red
        } else {
            _00C2CB
        }
    } else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .padding(horizontal = 16.dp)
            .background(backgroundColor, shape = RoundedCornerShape(24.dp))
            .clickable {
                if (gameViewModel.disableSelectAnswer) {
                    return@clickable
                }
                onClick()
            }
            .border(1.dp, borderColor, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(16.dp),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    MusicAppTheme {
        GameScreen(appContainer = AppContainer(CoroutineScope(Dispatchers.Main))) {}
    }
}