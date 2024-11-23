package com.fpoly.pro226.music_app.ui.screen.main.explore

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
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
import com.fpoly.pro226.music_app.data.source.network.models.Track
import com.fpoly.pro226.music_app.ui.theme.D9D9D9
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme.black85
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    appContainer: AppContainer,
    onClickGenreItem: (Genre) -> Unit,
    onBack: () -> Unit,
    onClickRadioItem: (id: String) -> Unit,
    startPlayerActivity: (tracks: List<Track>, startIndex: Int) -> Unit,
    onLoadTrackList: (track: List<Track>) -> Unit,
) {
    val extras = MutableCreationExtras().apply {
        set(ExploreViewModel.MY_REPOSITORY_KEY, appContainer.deezerRepository)
    }
    val vm: ExploreViewModel = viewModel(
        factory = ExploreViewModel.Factory,
        extras = extras,
    )

    val uiState = vm.exploreUiState

    LaunchedEffect(uiState) {
        if (uiState.resulTrack?.data?.isNotEmpty() == true) {
            onLoadTrackList(uiState.resulTrack.data)
        }
    }

    Box {
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
                SearchBar(vm)
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
        uiState.resulTrack?.data?.let { tracks ->
            ListTrackSearch(tracks, startPlayerActivity)
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
fun ListTrackSearch(
    tracks: List<Track>,
    startPlayerActivity: (tracks: List<Track>, startIndex: Int) -> Unit,
) {
    LazyColumn(modifier = Modifier.padding(top = 136.dp)) {
        items(tracks.size) { index ->
            Card(
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clickable {
                        startPlayerActivity(tracks, index)
                    },
                colors = CardDefaults.cardColors(containerColor = black85)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 8.dp)
                ) {
                    AsyncImage(
                        model = tracks[index].album?.cover_medium,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Artists avatar",
                        placeholder = painterResource(R.drawable.ic_app),
                        error = painterResource(R.drawable.ic_app),
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            color = Color.White,
                            text = tracks[index].title,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = tracks[index].artist?.name?:"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Cyan,
                            fontSize = 12.sp

                        )
                    }
                    androidx.compose.material.Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "More options",
                        tint = Color.White
                    )
                }
            }
        }

    }
}

@Composable
fun SearchBar(vm: ExploreViewModel) {
    val context = LocalContext.current

    var fieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()
    val containerColor = Color.White

    val voiceRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val text =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
            fieldValue = fieldValue.copy(
                text = text,
            )
            vm.searchTrack(text)
        }
    }

    fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói gì đó để tìm kiếm...")
        }
        try {
            voiceRecognitionLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Thiết bị của bạn không hỗ trợ nhận diện giọng nói",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    TextField(
        leadingIcon = {
            Image(
                colorFilter = ColorFilter.tint(D9D9D9),
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (fieldValue.text.isNotEmpty()) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear text",
                    tint = Color.Gray,
                    modifier = Modifier.clickable {
                        vm.clearTracks()
                        fieldValue = fieldValue.copy("", selection = TextRange.Zero)
                    }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.baseline_mic_24),
                    contentDescription = "Search",
                    modifier = Modifier.clickable {
                        startVoiceRecognition()
                    }
                )
            }
        },
        maxLines = 1,
        singleLine = true,
        value = fieldValue,
        onValueChange = { newText ->
            Log.d("TAG", "SearchBar: $newText")
            fieldValue = newText
            coroutineScope.launch {
                vm.searchTrack(newText.text)
                delay(500)
            }
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
//    SearchBar()
}

@Preview(showBackground = true)
@Composable
fun ListTrackSearchPreview() {
//    ListTrackSearch()
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