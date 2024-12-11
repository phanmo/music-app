package com.fpoly.pro226.music_app.ui.screen.genre

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.network.models.Artist
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import kotlinx.coroutines.launch

@Composable
fun GenreScreen(
    appContainer: AppContainer,
    id: String,
    onBack: () -> Unit,
    onItemArtistClick: (Artist) -> Unit
) {
    val vm: GenreViewModel = remember {
        appContainer.genreViewModelFactory.create(id)
    }
    val uiState = vm.genreUiState
    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            TopBar(onBack)
        }) { innerPadding ->
        val tabs = listOf("Artists")
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = Modifier.padding(innerPadding)) {
//            TabRow(
//                selectedTabIndex = selectedTabIndex,
//                indicator = { tabPositions ->
//                    Box(
//                        modifier = Modifier
//                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
//                            .height(4.dp)
//                            .background(Color.Cyan)
//                    )
//                },
//                containerColor = Color.Black
//            ) {
//                tabs.forEachIndexed { index, title ->
//                    Tab(
//                        selected = selectedTabIndex == index,
//                        onClick = {
//                            selectedTabIndex = index
//                            coroutineScope.launch {
//                                pagerState.animateScrollToPage(index)
//                            }
//                        },
//                        text = {
//                            Text(
//                                text = title,
//                                color = if (selectedTabIndex == index) Color.Cyan else Color.White,
//                                fontWeight = FontWeight.Bold,
//                                style = MaterialTheme.typography.bodyLarge,
//                                fontSize = 16.sp
//                            )
//                        }
//                    )
//                }
//            }
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> ArtistsViewPage(uiState.artists, onItemArtistClick)
                    1 -> RadiosViewPage()
                }
            }
        }
    }
}

@Composable
fun TopBar(onBack: () -> Unit) {
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
                        onBack()
                    }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "ARTISTS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Cyan
                )
                Text(
                    text = "Past 30 Days",
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
fun GenreScreenPreview() {
    MusicAppTheme {
//        GenreScreen()
    }
}