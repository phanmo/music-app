package com.fpoly.pro226.music_app.ui.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.theme._121111

@Composable
fun SettingScreen() {
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
                text = "Setting",
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
        Row {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(BorderStroke(2.dp, Color.Black), shape = CircleShape)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.matcha),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Mo Phan",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        color = Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "moptkpd08532@fpt.edu.vn",
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Settings List
        Column(modifier = Modifier.fillMaxWidth()) {
            SettingsItem(
                icon = R.drawable.profile,
                text = "Cập nhật thông tin cá nhân",
                onClick = { /* TODO: Handle Notification Click */ }
            )
            Divider()
            SettingsItem(
                icon = R.drawable.change_password,
                text = "Đổi mật khẩu",
                onClick = { /* TODO: Handle Delete Account Click */ }
            )
            Divider()
            SettingsItem(
                icon = R.drawable.information,
                text = "Giới thiệu",
                onClick = { /* TODO: Handle About Click */ }
            )
            Divider()
            SettingsItem(
                icon = R.drawable.baseline_logout_24,
                text = "Đăng xuất",
                onClick = { /* TODO: Handle Logout Click */ }
            )
        }

    }
}
@Composable
fun SettingsItem(icon: Int, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = TextStyle(fontSize = 16.sp, color = Color.White),
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}