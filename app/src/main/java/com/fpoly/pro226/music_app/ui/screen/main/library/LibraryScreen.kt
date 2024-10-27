package com.fpoly.pro226.music_app.ui.screen.main.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme

@Composable
fun LibraryScreen() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF102B2D),
                            Color(0xFF000000),
                            Color(0xFF000000),
                        )
                    )
                )
                .fillMaxSize()
        ) {
            Text("LibraryScreen")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    MusicAppTheme {
        LibraryScreen()
    }
}