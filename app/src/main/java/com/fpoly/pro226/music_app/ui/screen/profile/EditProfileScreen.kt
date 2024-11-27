package com.fpoly.pro226.music_app.ui.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.screen.login.ButtonWithElevation
import com.fpoly.pro226.music_app.ui.screen.login.TextField
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._121111

@Composable
fun EditProfileScreen() {
    Column(
        modifier = Modifier
            .background(_121111)
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = "Edit user profile",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight(600),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Box(
            modifier = Modifier
                .padding(vertical = 40.dp)
                .size(120.dp)
                .border(
                    border = BorderStroke(2.dp, _00C2CB),
                    shape = CircleShape
                )
                .padding(2.dp)
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.matcha),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize()
            )
        }
        TextField(
            label = "Username",
            isPassword = false,
            onValueChange = {},
        )
        TextField(
            label = "Email",
            isPassword = false,
            onValueChange = {
            },
        )
        TextField(
            label = "Full name",
            isPassword = false,
            onValueChange = {},
        )
        TextField(
            label = "Birthday",
            isPassword = false,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithElevation(label = "Save", onclick = {
        })
    }
}
