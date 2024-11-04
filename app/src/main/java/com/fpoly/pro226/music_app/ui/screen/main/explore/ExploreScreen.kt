package com.fpoly.pro226.music_app.ui.screen.main.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.network.models.Genre
import com.fpoly.pro226.music_app.data.source.network.models.Genres
import com.fpoly.pro226.music_app.data.source.network.models.Radios
import com.fpoly.pro226.music_app.ui.theme.D9D9D9
import com.fpoly.pro226.music_app.ui.theme._00C2CB

@Composable
fun ExploreScreen(
    appContainer: AppContainer,
    onClickGenreItem: (Genre) -> Unit,
    onBack: () -> Unit,
    onClickRadioItem: (id: String) -> Unit
) {
    val extras = MutableCreationExtras().apply {
        set(ExploreViewModel.MY_REPOSITORY_KEY, appContainer.deezerRepository)
    }
    val vm: ExploreViewModel = viewModel(
        factory = ExploreViewModel.Factory,
        extras = extras,
    )

    val uiState = vm.exploreUiState

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF102B2D),
                        Color(0xFF000000),
                        Color(0xFF000000),
                    )
                )
            )
            .padding(8.dp)

    ) {
        item {
            CustomTopAppBar()
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            SearchBar()
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Your Top Genres",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        uiState.genres?.let {
            item {
                GridViewGenres(it, onClickGenreItem)
            }
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Text(
                text = "Browse All",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        uiState.radios?.let {
            item {
                GridViewRadios(it, onClickRadioItem)
            }
        }
    }
}

@Composable
fun CustomTopAppBar() {
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
                    text = "Search",
                    color = _00C2CB,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 54.dp)
                )

            }
        },
        modifier = Modifier
            .fillMaxWidth()

    )
}

@Composable
fun GridViewGenres(items: Genres, onClick: (Genre) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = (114 * items.data.size).dp),
    ) {
        items(4) { index ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .fillMaxWidth()
                    .clickable {
                        onClick(items.data[index + 1])
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray)
            ) {
                Box {
                    AsyncImage(
                        model = items.data[index + 1].picture,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .height(100.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        placeholder = painterResource(R.drawable.ic_app),
                        error = painterResource(R.drawable.ic_app)
                    )
                    Text(
                        text = items.data[index + 1].name,
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
fun GridViewRadios(items: Radios, onClickRadioItem: (id: String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = (114 * items.data.size).dp),
    ) {
        items(items.data.size) { index ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .fillMaxWidth()
                    .clickable {
                        onClickRadioItem(items.data[index].id)
                    },
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
    val containerColor = Color.White
    TextField(
        leadingIcon = {
            Image(
                colorFilter = ColorFilter.tint(D9D9D9),
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search"
            )
        },
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
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
        shape = RoundedCornerShape(24.dp),
        placeholder = {
            Text(
                "Songs, Artists, Podcasts & More",
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar()
}

@Preview(showBackground = true)
@Composable
fun GridViewGenresPreview() {
    GridViewGenres(Genres(data = listOf())) {}
}

@Preview(showBackground = true)
@Composable
fun GridViewRadiossPreview() {
    GridViewRadios(Radios(data = listOf())) {}
}