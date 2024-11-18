package com.fpoly.pro226.music_app.ui.screen.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.ui.components.LoadingDialog
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._7CEEFF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(appContainer: AppContainer) {
    val extras = MutableCreationExtras().apply {
        set(GameViewModel.MY_REPOSITORY_KEY, appContainer.fMusicRepository)
        set(GameViewModel.MY_REPOSITORY_KEY_2, appContainer.deezerRepository)
    }
    val vm: GameViewModel = viewModel(
        factory = GameViewModel.provideFactory(),
        extras = extras,
    )

    val uiState = vm.gameUiState

    var selectedOption by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    var startAnimation by remember { mutableStateOf(false) }

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
                        }
                )

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "10",
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
                            Spacer(modifier = Modifier.height(30.dp))
                            androidx.compose.material3.Slider(
                                value = 0f,
                                valueRange = 0f..0F,
                                onValueChange = { newValue ->
//                            currentPosition.longValue = newValue.toLong()
                                },
                                onValueChangeFinished = {
//                            mediaController.value?.seekTo(currentPosition.longValue)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .padding(horizontal = 16.dp),
                                thumb = {
                                    SliderDefaults.Thumb(
                                        interactionSource = interactionSource,
                                        modifier = Modifier.size(0.dp),
                                        colors = SliderDefaults.colors(
                                            thumbColor = _7CEEFF,
                                            activeTrackColor = _7CEEFF,
                                        )
                                    )
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = _7CEEFF,
                                    activeTrackColor = _7CEEFF,
                                )
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .clickable {
                                        vm.nextQuestion()
                                    }
                            ) {
                                Text(
                                    text = "Next question",
                                    color = _00C2CB,
                                    fontWeight = FontWeight.ExtraLight,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
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
                        CircularProgressIndicator(
                            progress = 0.65f,
                            modifier = Modifier.size(56.dp),
                            color = _00C2CB,
                            strokeWidth = 4.dp
                        )
                        Text(
                            text = "18",
                            color = _00C2CB,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

            }

            Spacer(modifier = Modifier.height(32.dp))

            // Options
            val options = vm.gameUiState.tracksQuestion.map { track ->
                track
            }
            options.forEach { track ->
                OptionButton(
                    option = track.title,
                    isSelected = selectedOption == track.title,
                    onClick = {
                        selectedOption = track.title
                        if (vm.selectAnswer(track.id)) {
                            startAnimation = true
                        }
                        // Nếu thời gian kết thúc thì gọi next
                        vm.nextQuestion()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
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
fun OptionButton(option: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color.White else _00C2CB
    val textColor = if (isSelected) _00C2CB else Color.White
    val borderColor = if (isSelected) _00C2CB else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .padding(horizontal = 16.dp)
            .background(backgroundColor, shape = RoundedCornerShape(24.dp))
            .clickable {
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
        GameScreen(appContainer = AppContainer(CoroutineScope(Dispatchers.Main)))
    }
}