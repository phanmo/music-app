package com.fpoly.pro226.music_app.ui.screen.song

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.theme.FFFFFF_70
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._7CEEFF
import com.fpoly.pro226.music_app.ui.theme._8A9A9D
import com.fpoly.pro226.music_app.ui.theme._A6F3FF

@Composable
fun SongScreen(
    viewModel: SongViewModel,
    modifier: Modifier = Modifier,

    ) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SongTopAppBar()
        },

        ) { innerPadding ->
        SongContent(innerPadding = innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongTopAppBar() {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "PLAYING FROM PLAYLIST:",
                    fontSize = 9.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = FFFFFF_70,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lofi Loft",
                        fontSize = 12.sp,
                        color = _7CEEFF, // Light blue color
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Icon(
                        modifier = Modifier.clickable {

                        },
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.Gray
                    )

                }
            }
        },
        actions = {
            IconButton(onClick = { /* Handle menu click */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black // App bar background color
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        // Image
        AsyncImage(
            model = "https://e-cdns-images.dzcdn.net/images/cover/2e018122cb56986277102d2041a592c8/1000x1000-000000-80-0-0.jpg",
            contentScale = ContentScale.Crop,
            contentDescription = "Album Art",
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(4.dp))

        )
        Text(
            text = "grainy days",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
                .padding(top = 27.dp, start = 24.dp)
                .align(alignment = Alignment.Start)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 20.dp)
        ) {
            Text(
                text = "moody.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,

                )
            Row {
                Image(
                    modifier = Modifier.size(17.dp),
                    painter = painterResource(R.drawable.share),
                    contentDescription = "null" // decorative element
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    modifier = Modifier.size(17.dp),
                    painter = painterResource(R.drawable.love),
                    contentDescription = "null" // decorative element
                )
            }
        }

        // Slider for the song progress
        var sliderPosition by remember { mutableStateOf(0.2f) }
        val interactionSource = remember { MutableInteractionSource() }

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .padding(horizontal = 16.dp),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    modifier = Modifier.size(17.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = _7CEEFF,
                        activeTrackColor = _7CEEFF,
                    )
                )
            },
            colors = SliderDefaults.colors(
                thumbColor = _7CEEFF,
                activeTrackColor = _7CEEFF,
            )
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "0:00",
                color = _8A9A9D,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "0:20",
                color = _8A9A9D,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        // Media Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /* Previous song */ }) {
                Image(
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = "Previous",
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .size(56.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(_A6F3FF, _00C2CB)
                    ), CircleShape
                )
                .clickable {

                }) {
                Image(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = { /* Next song */ }) {
                Image(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "Next",
                )
            }
        }

        Text(
            text = "LYRICS",
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp),
            fontSize = 12.sp
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
                .background(
                    color = _7CEEFF,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = """
                        You never look at the sky
                        Cause you think it's too high
                        You never look at the stars
                        Cause you think they're too far
                    """.trimIndent(),
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongTopAppBarPreview() {
    MusicAppTheme {
        SongTopAppBar()
    }
}

@Preview(showBackground = true)
@Composable
fun SongContentPreview() {
    MusicAppTheme {
        Scaffold { innerPadding ->
            SongContent(innerPadding)
        }
    }
}