package com.fpoly.pro226.music_app.ui.screen.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme

@Composable
fun PlaylistScreen() {
    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            TopBar()
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(
                innerPadding
            )
        ) {
            item {
                AvatarArtist()
            }
        }

    }
}

@Composable
fun AvatarArtist() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = "https://e-cdns-images.dzcdn.net/images/misc/b0b8efcbc3cb688864ce69da0061e525/1000x1000-000000-80-0-0.jpg",
            contentScale = ContentScale.Crop,
            contentDescription = "Artists avatar",
            placeholder = painterResource(R.drawable.ic_app),
            error = painterResource(R.drawable.ic_app),
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .padding(horizontal = 60.dp, vertical = 24.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = "Lofi Loft",
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TopBar() {
    TopAppBar(backgroundColor = Color.Black) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_24),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                    }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "FROM \"ARTIST\"",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraLight,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlaylistScreenPreview() {
    MusicAppTheme {
        PlaylistScreen()
    }
}