package com.fpoly.pro226.music_app.ui.screen.game

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._7CEEFF

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen() {
    var selectedOption by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

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
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = {
                                }) {
                                    Image(
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(_00C2CB),
                                        painter = painterResource(id = R.drawable.previous),
                                        contentDescription = "Previous",

                                        )
                                }
                                IconButton(onClick = {
                                }) {
                                    Image(
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(_00C2CB),
                                        painter = painterResource(id = R.drawable.next),
                                        contentDescription = "Next",

                                        )
                                }
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
            val options = listOf("come", "comes", "are coming", "came")
            options.forEach { option ->
                OptionButton(
                    option = option,
                    isSelected = selectedOption == option,
                    onClick = { selectedOption = option }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
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
            .clickable { onClick() }
            .border(1.dp, borderColor, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    MusicAppTheme {
        GameScreen()
    }
}