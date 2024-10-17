package com.fpoly.pro226.music_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.theme._44D7DD

@Composable
fun FMusicBottomNavigation(modifier: Modifier, onItemSelected: (Int) -> Unit) {
    val items = listOf("Home", "Explore", "Library")
    val selectedItem = remember { mutableIntStateOf(0) }

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
        items.forEachIndexed { index, item ->
            val isSelected = selectedItem.intValue == index
            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    selectedItem.intValue = index
                    onItemSelected(index)
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = getIconForItem(item)),
                        contentDescription = item,
                        tint = if (isSelected) _44D7DD else Color.White
                    )
                },
                label = {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = item,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) _44D7DD else Color.White
                    )
                },
                alwaysShowLabel = true,
                modifier = Modifier.weight(1f)
            )
        }
    }


}

fun getIconForItem(item: String): Int {
    return when (item) {
        "Home" -> R.drawable.home
        "Explore" -> R.drawable.search
        "Library" -> R.drawable.library
        else -> R.drawable.home
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    FMusicBottomNavigation(Modifier) {}

}