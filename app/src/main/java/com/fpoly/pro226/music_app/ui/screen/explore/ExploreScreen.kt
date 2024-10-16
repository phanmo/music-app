package com.fpoly.pro226.music_app.ui.screen.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import com.fpoly.pro226.music_app.ui.theme.D9D9D9
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme

@Composable
fun ExploreScreen(appContainer: AppContainer) {
    val vm: ExploreViewModel = remember {
        appContainer.exploreViewModelFactory.create()
    }
    val uiState = vm.exploreUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp)
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            Image(
                painter = painterResource(R.drawable.ic_app),
                contentDescription = "Icon app",
                modifier = Modifier.size(width = 63.dp, height = 48.dp)
            )
            Text(
                text = "Search",
                color = Color.Cyan,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 54.dp)
            )

        }
        Spacer(modifier = Modifier.height(22.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your Top Genres",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        uiState.genres?.let { GridViewGenres(it) }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Browse All",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        uiState.radios?.let { GridViewRadios(it) }


    }
}


@Composable
fun GridViewGenres(items: Genres) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(4) { index ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray)
            ) {
                Box {
                    AsyncImage(
                        model = items.data[index].picture,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .height(100.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        placeholder = painterResource(R.drawable.ic_app),
                        error = painterResource(R.drawable.ic_app)
                    )
                    Text(
                        text = items.data[index].name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }

            }
        }
    }

}

@Composable
fun GridViewRadios(items: Radios) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(items.data.size) { index ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray)
            ) {
                Box {
                    AsyncImage(
                        model = items.data[index].picture_medium,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .height(100.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        placeholder = painterResource(R.drawable.ic_app),
                        error = painterResource(R.drawable.ic_app)
                    )
                    Text(
                        text = items.data[index].title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }

            }
        }
    }

}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val containerColor = D9D9D9
    TextField(
        maxLines = 1,
        singleLine = true,
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            cursorColor = Color.Cyan,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
        shape = RoundedCornerShape(24.dp),
        placeholder = {
            Text(
                "Songs, Artists, Podcasts & More",
                color = Color.Gray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    MusicAppTheme {
//        ExploreScreen(null)
    }
}