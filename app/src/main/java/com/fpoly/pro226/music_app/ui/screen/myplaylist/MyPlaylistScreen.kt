package com.fpoly.pro226.music_app.ui.screen.myplaylist

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.ui.components.LoadingDialog
import com.fpoly.pro226.music_app.ui.theme.Black
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._8A9A9D

@Composable
fun MyPlaylistScreen(
    fMusicRepository: FMusicRepository,
    onBack: () -> Unit,
    onClickItemPlaylist: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var playlistName by remember { mutableStateOf("") }
    val extras = MutableCreationExtras().apply {
        set(MyPlaylistViewModel.MY_REPOSITORY_KEY, fMusicRepository)
    }
    val vm: MyPlaylistViewModel = viewModel(
        factory = MyPlaylistViewModel.provideFactory(),
        extras = extras
    )

    val uiState = vm.myPlaylistUiState
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    DisposableEffect(Unit) {
        val sharedPreferences = PreferencesManager(context)
        sharedPreferences.getUserId()?.let { vm.getAllPlaylist(it) }
        onDispose { }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(48.dp),
                onClick = { showDialog = true },
                content = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add playlist",
                        tint = Color.Black
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        backgroundColor = Color.Black,
        topBar = { TopBar({ onBack() }) }
    )
    { innerPadding ->
        Box {
            LazyColumn(
                modifier = Modifier.padding(
                    innerPadding
                )
            ) {
                uiState.playListResponse?.data?.let { data ->
                    items(data.size) { index ->
                        Card(
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable {
//                                    onClickItemPlaylist()
                                },
                            colors = CardDefaults.cardColors(containerColor = Black)
                        ) {
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp, horizontal = 8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_app),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Artists avatar",
                                        modifier = Modifier
                                            .size(52.dp)
                                            .border(
                                                width = 2.dp,
                                                color = _00C2CB,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clip(RoundedCornerShape(5.dp))

                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            color = Color.White,
                                            text = data[index].name,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "${data[index].count} songs",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray,
                                            fontSize = 12.sp

                                        )
                                    }
                                    androidx.compose.material.Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More options",
                                        tint = Color.White
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(0.5.dp)
                                        .background(color = Color.Gray)
                                )
                            }
                        }
                    }
                }


            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 8.dp,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            color = _00C2CB,
                            text = "NEW PLAYLIST",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        OutlinedTextField(
                            value = playlistName,
                            onValueChange = { playlistName = it },
                            textStyle = TextStyle(
                                color = _00C2CB,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = _00C2CB
                            ),
                            label = {
                                Text(
                                    "Give your playlist a title",
                                    color = _8A9A9D,
                                    fontSize = 14.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showDialog = false }) {
                                Text(
                                    "Cancel", fontSize = 14.sp,
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            androidx.compose.material3.Button(
                                onClick = {
                                    showDialog = false
                                    vm.addPlaylist(playlistName)
                                },
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(_00C2CB),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .height(40.dp),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 10.dp,
                                    pressedElevation = 15.dp,
                                    disabledElevation = 0.dp,
                                )

                            ) {
                                Text(
                                    text = "Create",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
        if (vm.myPlaylistUiState.isLoading) {
            LoadingDialog(onDismiss = { })
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
                    text = "MY PLAYLIST",
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
fun MyPlaylistScreenPreview() {
    MusicAppTheme {
//        MyPlaylistScreen()
    }
}