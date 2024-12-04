package com.fpoly.pro226.music_app.ui.screen.settings

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB

@Composable
fun SettingScreen(
    onLogoutClick: () -> Unit,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit
) {
    val context = LocalContext.current
    val userInfo = PreferencesManager(context).getUser()
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
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Row {
            Box(
                modifier = Modifier.size(64.dp)
            ) {
                AsyncImage(
                    model = "${userInfo?.avatar}",
                    contentDescription = "Avatar",
                    placeholder = painterResource(R.drawable.ic_app),
                    error = painterResource(R.drawable.ic_app),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = "${userInfo?.name}",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${userInfo?.email}",
                    style = TextStyle(fontSize = 14.sp, color = _00C2CB)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = _00C2CB, thickness = 0.25.dp)


        // Settings List
        Column(modifier = Modifier.fillMaxWidth()) {
            SettingsItem(
                icon = R.drawable.baseline_person_24,
                text = "Edit profile",
                onClick = {
                    onEditProfile()
                }
            )

            SettingsItem(
                icon = R.drawable.baseline_lock_outline_24,
                text = "Change password",
                onClick = {
                    onChangePassword()
                }
            )

            SettingsItem(
                icon = R.drawable.outline_info_24,
                text = "About us",
                onClick = {

                }
            )
            SettingsItem(
                icon = R.drawable.baseline_logout_24,
                text = "Logout",
                onClick = {
                    val preferencesManager = PreferencesManager(context)
                    preferencesManager.clearUserInfo()
                    onLogoutClick()
                }
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
            .padding(vertical = 12.dp),
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
            style = MaterialTheme.typography.labelMedium,
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal,
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

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    MusicAppTheme {
        SettingScreen({}, {}, {}, {})
    }
}