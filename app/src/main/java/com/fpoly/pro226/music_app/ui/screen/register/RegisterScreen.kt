package com.fpoly.pro226.music_app.ui.screen.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.screen.login.ButtonWithElevation
import com.fpoly.pro226.music_app.ui.screen.login.ContinueWith
import com.fpoly.pro226.music_app.ui.screen.login.TextField
import com.fpoly.pro226.music_app.ui.theme.FFFFFF_87
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._121111
import com.fpoly.pro226.music_app.ui.theme._7CEEFF
import com.fpoly.pro226.music_app.ui.theme._DBE7E8

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(_121111)
            .padding(16.dp)
            .fillMaxHeight()
    ) {
//        Image(
//            painterResource(
//                id = R.drawable.back
//            ),
//            contentDescription = "Logo",
//            modifier = Modifier
//                .width(24.dp)
//                .height(24.dp)
//                .fillMaxWidth()
//                .clickable {
//                    onBack()
//                }
//                .align(alignment = Alignment.Start)
//        )
        Image(
            painterResource(
                id = R.drawable.ic_app
            ),
            contentDescription = "Logo",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally)
        )
        Text(
            text = "Create a new account",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
        TextField(
            label = "Email",
            isPassword = false,
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.mail),
                    contentDescription = "Email Icon",
                    tint = FFFFFF_87,
                    modifier = Modifier.size(16.dp)
                )
            },
            onValueChange = {},
        )
        TextField(
            label = "Password",
            isPassword = true,
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.lock),
                    contentDescription = "Password Icon",
                    tint = FFFFFF_87,
                    modifier = Modifier.size(16.dp)
                )
            },
            onValueChange = {},
        )
        TextField(
            label = "Confirm password",
            isPassword = true,
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.lock),
                    contentDescription = "Password Icon",
                    tint = FFFFFF_87,
                    modifier = Modifier.size(16.dp)
                )
            },
            onValueChange = {},
        )
        ButtonWithElevation(
            label = "Register",
            onclick = {
                onRegisterSuccess()
            })
        ContinueWith()
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 40.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(1.dp, _DBE7E8),
                        shape = CircleShape
                    )
                    .padding(5.dp)
                    .clickable {}
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(1.dp, _DBE7E8),
                        shape = CircleShape
                    )
                    .padding(5.dp)
                    .clickable {}
            ) {
                Image(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                color = Color.White,
            )
            Text(
                text = "Log in",
                fontSize = 16.sp,
                color = _7CEEFF,
                modifier = Modifier.clickable {
                    onBack()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongContentPreview() {
    MusicAppTheme {
        RegisterScreen({}, {})
    }
}