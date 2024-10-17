package com.fpoly.pro226.music_app.ui.screen.genre

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.data.source.network.models.Artist
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme

@Composable
fun ArtistsViewPage(
    artists: List<Artist> = listOf(),
    onItemClick: (Artist) -> Unit
) {
    LazyColumn {
        items(artists.size) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .clickable {
                        onItemClick(artists[index])
                    },
                colors = CardDefaults.cardColors(containerColor = Color.Black)

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = artists[index].picture_medium,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Artists avatar",
                        modifier = Modifier
                            .size(85.dp)
                            .clip(RoundedCornerShape(50.dp)),
                        placeholder = painterResource(R.drawable.ic_app),
                        error = painterResource(R.drawable.ic_app)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = artists[index].name,
                        color = Color.White,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistsViewPagePreview() {
    MusicAppTheme {
        ArtistsViewPage() {}
    }
}