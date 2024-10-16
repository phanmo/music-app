package com.fpoly.pro226.music_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.fpoly.pro226.music_app.ui.theme._00C2CB

@Composable
fun FMusicBottomNavigation(modifier: Modifier) {
    val selectedItem = remember { mutableStateOf(0) }

    BottomAppBar(
        modifier = modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.1f),
                    Color.Black.copy(alpha = 0.2f),
                    Color.Black.copy(alpha = 0.3f),
                    Color.Black.copy(alpha = 0.4f),
                    Color.Black.copy(alpha = 0.5f),
                    Color.Black.copy(alpha = 0.6f),
                    Color.Black.copy(alpha = 0.7f),
                    Color.Black.copy(alpha = 0.8f),
                    Color.Black.copy(alpha = 0.9f),
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                    Color.Black,
                )
            )
        ), containerColor = Color.Transparent
    ) {
        BottomNavigationItem(
            selected = true,
            onClick = { },
            selectedContentColor = _00C2CB,
            unselectedContentColor = Color.White,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home",) },
            label = { Text("Home",) }
        )
        BottomNavigationItem(
            selected = false,
            selectedContentColor = _00C2CB,
            unselectedContentColor = Color.White,
            onClick = { /* Handle explore click */ },
            icon = { Icon(Icons.Default.Search, contentDescription = "Explore", tint = _00C2CB) },
            label = { Text("Explore", color = _00C2CB) }
        )
        BottomNavigationItem(
            selectedContentColor = _00C2CB,
            unselectedContentColor = Color.White,
            selected = false,
            onClick = { /* Handle library click */ },
            icon = { Icon(Icons.Default.Menu, contentDescription = "Library", tint = _00C2CB) },
            label = { Text("Library", color = _00C2CB) }
        )
    }


}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    FMusicBottomNavigation(Modifier)

}