package com.fpoly.pro226.music_app.ui.screen.splash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.screen.login.ButtonWithElevation
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._121111
import com.fpoly.pro226.music_app.ui.theme._44D7DD

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(_44D7DD)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_girl), // Thay thế 'your_image' bằng tên ảnh của bạn
            contentDescription = "Splash Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 54.dp, topEnd = 54.dp))
                .background(_121111)
                .border(
                    BorderStroke(2.dp, Color.Black),
                    shape = RoundedCornerShape(topStart = 54.dp, topEnd = 54.dp)
                )
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Dòng chữ
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.White)) {
                            append("From the ")
                        }
                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = _44D7DD)) {
                            append("latest ")
                        }
                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.White)) {
                            append("to the ")
                        }
                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = _44D7DD)) {
                            append("greatest ")
                        }
                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.White)) {
                            append("hits, play your favorite tracks on ")
                        }
                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = _44D7DD)) {
                            append("musium ")
                        }
                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.White)) {
                            append("now!")
                        }
                    },
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(30.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button
                ButtonWithElevation(label = "Get Started") {

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongContentPreview() {
    MusicAppTheme {
        SplashScreen()
    }
}