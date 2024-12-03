package com.fpoly.pro226.music_app.ui.screen.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

@Composable
fun ChangePasswordScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF102B2D),
                        Color(0xFF000000),
                        Color(0xFF000000),
                    )
                )
            )
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
                    .clickable {
                        onBack()
                    }
            )
            Text(
                text = "Change password",
                color = _00C2CB,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        TextField(
            label = "Current password",
            isPassword = false,
            onValueChange = {},
        )
        TextField(
            label = "New password",
            isPassword = false,
            onValueChange = {},
        )
        TextField(
            label = "Confirm new password",
            isPassword = false,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithElevation(label = "Save", onclick = {
        })
    }
}

@Preview(showBackground = true)
@Composable
fun SongContentPreview() {
    MusicAppTheme {
        ChangePasswordScreen() {}
    }
}