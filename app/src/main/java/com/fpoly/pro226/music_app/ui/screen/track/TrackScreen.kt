package com.fpoly.pro226.music_app.ui.screen.track

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._1E1E1E_85
import com.fpoly.pro226.music_app.ui.theme._8A9A9D

@Composable
fun TrackScreen(artists: List<Track> = listOf()) {
    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            TopBar()
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(10) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = _1E1E1E_85)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "#${index + 1}",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Spacer(modifier = Modifier.width(18.dp))
                        AsyncImage(
                            model = "https://e-cdns-images.dzcdn.net/images/misc/b0b8efcbc3cb688864ce69da0061e525/1000x1000-000000-80-0-0.jpg",
                            contentScale = ContentScale.Crop,
                            contentDescription = "Artists avatar",
                            placeholder = painterResource(R.drawable.ic_app),
                            error = painterResource(R.drawable.ic_app),
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(32.dp))
                        Column {
                            Text(
                                text = "Swim",
                                fontSize = 17.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Ngan",
                                fontSize = 14.sp,
                                color = _8A9A9D,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

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
                    text = "ROSE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Cyan
                )
                Text(
                    text = "Top 50 songs",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Thin,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackScreenPreview() {
    MusicAppTheme {
        TrackScreen()
    }
}