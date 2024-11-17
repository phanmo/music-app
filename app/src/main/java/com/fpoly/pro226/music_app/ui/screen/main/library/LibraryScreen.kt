package com.fpoly.pro226.music_app.ui.screen.main.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._A6F3FF

@Composable
fun LibraryScreen(onLogoutClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF102B2D),
                        Color(0xFF000000),
                        Color(0xFF000000),
                    )
                )
            )
    ) {
        CustomTopAppBar(onLogoutClick)
        LazyColumn(
            modifier = Modifier
                .padding(top = 68.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            item {
                ItemButton(
                    content = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add New Playlist",
                            tint = Color.Black
                        )
                    },
                    title = "Add New Playlist",
                    onClick = {}
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                ItemButton(
                    content = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Your Liked Songs",
                            tint = Color.Black
                        )
                    },
                    title = "Your Liked Songs",
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun CustomTopAppBar(onLogoutClick: () -> Unit) {
    val context = LocalContext.current
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        title = {
            Box(contentAlignment = Alignment.CenterStart) {
                Image(
                    painter = painterResource(R.drawable.logotransparent),
                    contentDescription = "Icon app",
                    modifier = Modifier.size(width = 63.dp, height = 48.dp)
                )
                Text(
                    text = "Your Library",
                    color = _00C2CB,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 54.dp)
                )

            }
        },
        actions = {
            IconButton(
                onClick = {
                    val preferencesManager = PreferencesManager(context)
                    preferencesManager.clearAccessToken()
                    onLogoutClick()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_logout_24),
                    contentDescription = "Notification",
                    tint = Color.White
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()

    )
}

@Composable
fun ItemButton(onClick: () -> Unit, content: @Composable () -> Unit, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier
                .size(56.dp)
                .background(
                    brush = Brush.linearGradient(colors = listOf(_A6F3FF, _00C2CB)),
                    shape = CircleShape
                ),
        ) {
            content.invoke()
        }

        Spacer(modifier = Modifier.width(24.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    MusicAppTheme {
        LibraryScreen(){}
    }
}